package modernexpensetrackerbyamitmaity.example.modernexpensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private ExtendedFloatingActionButton crreat_trip;
    private String m_Text = "";
    private FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference UserRef,MainRef,RootRef;
    private ProgressDialog progressDialog;
    private CircleImageView circleImageView;
    private TextView Expensetracker;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance ();
        progressDialog = new ProgressDialog( MainActivity.this);
        progressDialog.setContentView ( R.layout.loading );
        circleImageView = findViewById(R.id.Home_Profile_Image);
        RootRef =  FirebaseDatabase.getInstance ().getReference ().child ( "AllTrip" );
        UserRef= FirebaseDatabase.getInstance ().getReference ().child ( "Users" );
        MainRef= FirebaseDatabase.getInstance ().getReference ();
        progressDialog.setTitle ( "Please Wait..." );
        recyclerView = findViewById(R.id.ALLTripRecyclerView);
        Expensetracker = findViewById(R.id.expense_tracker);
        progressDialog.setCanceledOnTouchOutside ( false );
        progressDialog.setMessage ( "Tips: Please Check your Internet or Wi-fi Connection" );
        crreat_trip = findViewById(R.id.create_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        crreat_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreteATripFunction();
            }
        });


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenty = new Intent(MainActivity.this,MyprofileActivity.class);
                startActivity(intenty);
            }
        });
        Expensetracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Project Creator and Developer")
                        .setMessage("Amit Maity")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })

                        .setIcon(R.drawable.me)
                        .show();
            }
        });


    }






    @Override
    protected void onStart() {

        super.onStart();


        progressDialog.show();
        FirebaseUser currentUser = mAuth.getCurrentUser ();

        if (currentUser == null)
        {

            SendUserToLoginActivity();
        }
        else
        {
            currentUserID = mAuth.getCurrentUser ().getUid ();
            progressDialog.dismiss();
            UserRef.child ( currentUserID ).addValueEventListener ( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if ((dataSnapshot.exists ()) && (dataSnapshot.hasChild ( "Image" )))
                    {
                        String StudentNamea = dataSnapshot.child ("Image" ).getValue ().toString ();

                        Picasso.get ().load ( StudentNamea ).placeholder ( R.drawable.profile_image ).error ( R.drawable.profile_image ).into ( circleImageView );
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss ();
                }
            } );



            DatabaseReference DODREF = FirebaseDatabase.getInstance ().getReference ().child("Users").child(currentUserID).child("MyTrip");




            FirebaseRecyclerOptions<DODModel> optionsss =
                    new FirebaseRecyclerOptions.Builder<DODModel> ()
                            .setQuery ( DODREF,DODModel.class )
                            .build ();


            FirebaseRecyclerAdapter<DODModel, StudentViewHolderX> adapterrr =
                    new FirebaseRecyclerAdapter<DODModel, StudentViewHolderX> (optionsss) {
                        @Override
                        protected void onBindViewHolder(@NonNull final StudentViewHolderX holder, final int position, @NonNull final DODModel model) {

                            progressDialog.show();


                            String st = model.getID();

                            MainRef.child ( "AllTrip" ).child ( st )
                                    .addValueEventListener ( new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                                            String stringkey = getRef(position).getKey().toString();
                                            String retrieveUserNAme = dataSnapshot.child ( "TripName" ).getValue ().toString ();
                                            String retrieveEmail = dataSnapshot.child ( "TripDate" ).getValue ().toString ();
                                            String stringperson = String.valueOf((int)dataSnapshot.child("Member").getChildrenCount());

                                            holder.name.setText(retrieveUserNAme);
                                            holder.date.setText(retrieveEmail);
                                            holder.person.setText(stringperson);



                                            RootRef.child(stringkey).addValueEventListener ( new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    if ((dataSnapshot.child("Cost")).exists())
                                                    {
                                                        float sum = 0;
                                                        int costnominal = (int)dataSnapshot.child("Cost").getChildrenCount();
                                                        for (int i = 1;i<=costnominal;i++)
                                                        {

                                                            String stringo = dataSnapshot.child("Cost").child(String.valueOf(i)).child("Cost").getValue().toString();
                                                            float fl = Float.parseFloat(stringo);
                                                            sum = sum+fl;

                                                        }

                                                        String fg = String.valueOf(sum);
                                                        holder.cost.setText("₹: "+fg+"0");
                                                    }
                                                    else {
                                                        holder.cost.setText("₹: "+"0.00");
                                                    }






                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    progressDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                                                }
                                            } );









                                            progressDialog.dismiss();

                                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intenti = new Intent(MainActivity.this,TripviewActivity.class);
                                                    intenti.putExtra("TRIPID",stringkey);
                                                    startActivity(intenti);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                            progressDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                                        }
                                    } );



                        }

                        @NonNull
                        @Override
                        public StudentViewHolderX onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                            View view  = LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.trip_view_layout,viewGroup,false );
                            StudentViewHolderX viewHolder  = new StudentViewHolderX (  view);
                            return viewHolder;

                        }

                        @Override
                        public int getItemCount() {


                            return super.getItemCount();
                        }



                    };
            recyclerView.setAdapter ( adapterrr );

            adapterrr.startListening ();










        }
    }



    private void SendUserToLoginActivity() {


        Intent loginIntent = new Intent ( MainActivity.this,LoginActivity.class );
        loginIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity ( loginIntent );
        finish ();
    }


    private void CreteATripFunction() {
        CreateTripCustomDialogClass cdd=new CreateTripCustomDialogClass(MainActivity.this);
        cdd.show();

    }


    public static class StudentViewHolderX extends  RecyclerView.ViewHolder
    {

        TextView name,date,person,cost;
        public StudentViewHolderX(@NonNull View itemView) {
            super ( itemView );
            name = itemView.findViewById ( R.id.trip_view_trip_name);
            date = itemView.findViewById ( R.id.trip_view_trip_date);
            person = itemView.findViewById ( R.id.trip_view_trip_person);
            cost = itemView.findViewById ( R.id.trip_view_trip_cost);

        }
    }
}