package modernexpensetrackerbyamitmaity.example.modernexpensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripviewActivity extends AppCompatActivity {

    private TextView end_trip,trip_name,trip_cost,trip_person;
    private String trip_key;
    private RecyclerView History,Memberlist;
    private DatabaseReference RootRef,ChatRef,MemberRef,MainRef,CostRef;
    private LinearLayout history_layout,money_layout;
    private LinearLayout Add_Freind_Layout;
    private ProgressDialog progressDialog;
    public Button yes;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private int as=1,am=1;
    private EditText moneyamount,moneypurpose;

    private ExtendedFloatingActionButton trip_add_money_buton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripview);




        Intent intent = getIntent();
        trip_key= intent.getExtras().getString("TRIPID");
        yes = (Button) findViewById(R.id.add_money_submit_butyyon);
        mAuth= FirebaseAuth.getInstance ();
        currentUserID = mAuth.getCurrentUser ().getUid ();
        moneyamount = (EditText) findViewById(R.id.add_money_custom_money_edittext);
        moneypurpose = (EditText) findViewById(R.id.add_money_custom_money_purpose_edittext);

        history_layout = findViewById(R.id.history_layout);
        money_layout = findViewById(R.id.money_layout);
        progressDialog = new ProgressDialog( TripviewActivity.this);
        progressDialog.setContentView ( R.layout.loading );
        RootRef =  FirebaseDatabase.getInstance ().getReference ().child ( "AllTrip" ).child(trip_key);
        CostRef =  FirebaseDatabase.getInstance ().getReference ().child ( "AllTrip" ).child(trip_key).child("Cost");
        MainRef =  FirebaseDatabase.getInstance ().getReference ();
        ChatRef =  FirebaseDatabase.getInstance ().getReference ().child ( "AllTrip" ).child(trip_key).child("Chat");
        MemberRef =  FirebaseDatabase.getInstance ().getReference ().child ( "AllTrip" ).child(trip_key).child("Member");

        Add_Freind_Layout = findViewById(R.id.Add_Freind_Layout);
        trip_name = findViewById(R.id.trp_view_activity_name);
        Memberlist = findViewById(R.id.Trip_View_Member_RecyclerView);
        trip_cost = findViewById(R.id.trp_view_activity_money);
        trip_person = findViewById(R.id.trp_view_activity_member);
        History = findViewById(R.id.trp_view_activity_Recyclerview);
        progressDialog.setTitle ( "Please Wait..." );
        Memberlist.setLayoutManager(new LinearLayoutManager(TripviewActivity.this, LinearLayoutManager.HORIZONTAL, false));
        progressDialog.setCanceledOnTouchOutside ( false );
        progressDialog.setMessage ( "Tips: Please Check your Internet or Wi-fi Connection" );
        History.setLayoutManager(new LinearLayoutManager(TripviewActivity.this));

        end_trip = findViewById(R.id.end_trip_button);
        trip_add_money_buton = findViewById(R.id.trip_add_money_button);
        trip_add_money_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelloMoneyButton();
            }
        });




        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String saveCurrentTime,SaveCurrentData;
                Calendar calendar = Calendar.getInstance ();

                SimpleDateFormat currentdate = new SimpleDateFormat ( "MMM dd, yyyy" );
                SaveCurrentData = currentdate.format ( calendar.getTime () );
                SimpleDateFormat currenttime = new SimpleDateFormat ( "hh:mm a" );
                saveCurrentTime = currenttime.format ( calendar.getTime () );

                MoneyAddToRealFuntction(trip_key,SaveCurrentData,saveCurrentTime);



            }
        });




        end_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialDialog mDialog = new MaterialDialog.Builder ( TripviewActivity.this )
                        .setTitle ( "Are you sure end this trip ?" )
                        .setCancelable ( false )
                        .setPositiveButton ( "Confirm",R.drawable.confirm_image, new MaterialDialog.OnClickListener () {
                            @Override
                            public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {

                            }


                        } )
                        .setNegativeButton ( "Cancel", new MaterialDialog.OnClickListener () {
                            @Override
                            public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                dialogInterface.cancel ();
                            }

                        } )
                        .build ();

                // Show Dialog
                mDialog.show ();
            }
        });
        Add_Freind_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent15 = new Intent(TripviewActivity.this,ShowfreindActivity.class);
                intent15.putExtra("TRIPANDAND",trip_key);
                startActivity(intent15);
            }
        });

        RetriveAlltheData();
        RetriveAllMoney();
    }

    private void RetriveAllMoney() {

         RootRef.addValueEventListener ( new ValueEventListener() {
            @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                float sum = 0;
                int costnominal = (int)dataSnapshot.child("Cost").getChildrenCount();
                for (int i = 1;i<=costnominal;i++)
                {

                    String stringo = dataSnapshot.child("Cost").child(String.valueOf(i)).child("Cost").getValue().toString();
                    float fl = Float.parseFloat(stringo);
                    sum = sum+fl;

                }

                String fg = String.valueOf(sum);
                trip_cost.setText("₹: "+fg+"0");





          }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
               Toast.makeText(TripviewActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                                    }
            } );






    }

    private void MoneyAddToRealFuntction(String trip_key, String saveCurrentData, String saveCurrentTime) {
        String string1 = moneyamount.getText().toString();
        String string2 = moneypurpose.getText().toString();

        if (TextUtils.isEmpty(string1))
        {
            Toast.makeText(this, "Enter Some Money..", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(string2))
        {
            Toast.makeText(this, "Enter Some Reason..", Toast.LENGTH_SHORT).show();

        }
        else {


            MainRef.child ( "Users" ).child ( currentUserID )
                    .addValueEventListener ( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String retrieveUserNAme = dataSnapshot.child ( "Name" ).getValue ().toString ();


                            RootRef.addValueEventListener ( new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshottt) {







                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                            progressDialog.dismiss();
                                            Toast.makeText(TripviewActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                                        }
                                    } );

                            String trip_key_chat = RootRef.child("Chat").push().getKey();
                            RootRef.child("Cost").child(String.valueOf(as+1)).child("Cost").setValue(string1);
                            RootRef.child("Member").child(currentUserID).child("Cost").child(String.valueOf(am+1)).child("Cost").setValue(string1);
                            RootRef.child("Chat").child(trip_key_chat).child("Chat").setValue(saveCurrentData+" "+saveCurrentTime+"\n"+retrieveUserNAme+" spend ₹:"+string1+" for "+string2);

                            history_layout.setVisibility(View.VISIBLE);
                            money_layout.setVisibility(View.GONE);
                            moneyamount.setText(null);
                            moneypurpose.setText(null);
                            progressDialog.dismiss ();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            progressDialog.dismiss();
                            history_layout.setVisibility(View.VISIBLE);
                            money_layout.setVisibility(View.GONE);
                            Toast.makeText(TripviewActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                        }
                    } );



        }

    }


    private void RetriveAlltheData() {


        progressDialog.show();

        RootRef.addValueEventListener ( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String retrieveUserName = dataSnapshot.child ( "TripName" ).getValue ().toString ();




                        trip_name.setText(retrieveUserName);

                        String stringperson = String.valueOf((int)dataSnapshot.child("Member").getChildrenCount());

                        trip_person.setText("Trip Member ("+stringperson+")");

                        progressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        progressDialog.dismiss();
                        Toast.makeText(TripviewActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                    }
                } );


    }

    private void HelloMoneyButton() {
       history_layout.setVisibility(View.GONE);
       money_layout.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<ChatModel> optionsss =
                new FirebaseRecyclerOptions.Builder<ChatModel> ()
                        .setQuery ( ChatRef,ChatModel.class )
                        .build ();


        FirebaseRecyclerAdapter<ChatModel, StudentViewHolderX> adapterrr =
                new FirebaseRecyclerAdapter<ChatModel,StudentViewHolderX> (optionsss) {
                    @Override
                    protected void onBindViewHolder(@NonNull final StudentViewHolderX holder, final int position, @NonNull final ChatModel model) {

                        progressDialog.show();


                        holder.chat.setText(model.getChat());

                        progressDialog.dismiss();



                    }

                    @NonNull
                    @Override
                    public StudentViewHolderX onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view  = LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.trip_view_history_layout,viewGroup,false );
                        StudentViewHolderX viewHolder  = new StudentViewHolderX(  view);
                        return viewHolder;

                    }

                    @Override
                    public int getItemCount() {


                        return super.getItemCount();
                    }



                };
        History.setAdapter ( adapterrr );

        adapterrr.startListening ();








        FirebaseRecyclerOptions<MemberModel> optionssst =
                new FirebaseRecyclerOptions.Builder<MemberModel> ()
                        .setQuery ( MemberRef,MemberModel.class )
                        .build ();


        FirebaseRecyclerAdapter<MemberModel, StudentViewHolderY> adapterrrt =
                new FirebaseRecyclerAdapter<MemberModel,StudentViewHolderY> (optionssst) {
                    @Override
                    protected void onBindViewHolder(@NonNull final StudentViewHolderY holder, final int position, @NonNull final MemberModel model) {

                        progressDialog.show();


                        String stringperson = model.getPerson();

                        MainRef.child ( "Users" ).child ( stringperson )
                                .addValueEventListener ( new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        String retrieveUserNAmeer = dataSnapshot.child ( "Name" ).getValue ().toString ();

                                        if (dataSnapshot.hasChild("Image"))
                                        {
                                            String retrieveUserImage= dataSnapshot.child ( "Image" ).getValue ().toString ();
                                            Picasso.get ().load ( retrieveUserImage ).placeholder ( R.drawable.profile_image ).error ( R.drawable.profile_image ).into ( holder.circleImageView );

                                        }


                                        holder.name.setText ( retrieveUserNAmeer );
                                        progressDialog.dismiss ();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                        progressDialog.dismiss();
                                        Toast.makeText(TripviewActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                                    }
                                } );



                    }

                    @NonNull
                    @Override
                    public StudentViewHolderY onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view  = LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.trip_member_view_layout,viewGroup,false );
                        StudentViewHolderY viewHolder  = new StudentViewHolderY(  view);
                        return viewHolder;

                    }



                };
        Memberlist.setAdapter ( adapterrrt );

        adapterrrt.startListening ();









    }


    public static class StudentViewHolderX extends  RecyclerView.ViewHolder
    {

        TextView chat;
        public StudentViewHolderX(@NonNull View itemView) {
            super ( itemView );
            chat= itemView.findViewById ( R.id.trip_history_text);

        }
    }

    public static class StudentViewHolderY extends  RecyclerView.ViewHolder
    {

        TextView name;
        CircleImageView circleImageView;
        public StudentViewHolderY(@NonNull View itemView) {
            super ( itemView );
            name = itemView.findViewById ( R.id.trip_view_layout_name);
            circleImageView = itemView.findViewById ( R.id.trip_view_layout_image);


        }
    }

}