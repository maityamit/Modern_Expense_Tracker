package modernexpensetrackerbyamitmaity.example.modernexpensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class EndtripActivity extends AppCompatActivity {

    private String trip_key="";
    private String fg="0.0";
    private  float money_cal=0;
    private TextView end_trip_total_cost,end_trip_each_cost;
    private ProgressDialog progressDialog;
    private Button print,submit;
    private DatabaseReference CostRef,RootRef,UserRef,MainRef,MemberRef;
    private RecyclerView hmm_recycler_end,end_trp,falsee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endtrip);
        Intent intent = getIntent();
        trip_key= intent.getExtras().getString("TRIPKEYID").toString();
        end_trip_total_cost = findViewById(R.id.end_trip_total_cost);

        CostRef =  FirebaseDatabase.getInstance ().getReference ().child ( "AllTrip" ).child(trip_key).child("Cost");
        RootRef =  FirebaseDatabase.getInstance ().getReference ().child ( "AllTrip" ).child(trip_key);
        MemberRef =  FirebaseDatabase.getInstance ().getReference ().child ( "AllTrip" ).child(trip_key).child("Member");
        MainRef =  FirebaseDatabase.getInstance ().getReference ();
        UserRef = FirebaseDatabase.getInstance ().getReference ().child ( "AllTrip" ).child(trip_key).child("Cost");

        print = findViewById(R.id.print_button);
        submit = findViewById(R.id.submit_button);
        progressDialog = new ProgressDialog( EndtripActivity.this);
        progressDialog.setContentView ( R.layout.loading );
        progressDialog.setTitle ( "Please Wait..." );
        progressDialog.setCanceledOnTouchOutside ( false );
        progressDialog.setMessage ( "Tips: Please Check your Internet or Wi-fi Connection" );
        end_trip_each_cost = findViewById(R.id.end_trip_each_cost);
        hmm_recycler_end = findViewById(R.id.hmmrecycle_end);
        hmm_recycler_end.setLayoutManager(new LinearLayoutManager(EndtripActivity.this, LinearLayoutManager.HORIZONTAL, false));
        end_trp = findViewById(R.id.end_tripsorrow);
        end_trp.setLayoutManager(new LinearLayoutManager(EndtripActivity.this));
        falsee = findViewById(R.id.hmmrecycle_endfalse);
        falsee.setLayoutManager(new LinearLayoutManager(EndtripActivity.this));


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RootRef.child("TripDate").setValue("Trip Completed.");
                Intent intent17 = new Intent(EndtripActivity.this,MainActivity.class);
                startActivity(intent17);
            }
        });

    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }



    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<MainMoneyModel2> optionamitt =
                new FirebaseRecyclerOptions.Builder<MainMoneyModel2> ()
                        .setQuery ( UserRef,MainMoneyModel2.class )
                        .build ();


        progressDialog.show();



        FirebaseRecyclerAdapter<MainMoneyModel2, StudentViewHolderP> adaptt =
                new FirebaseRecyclerAdapter<MainMoneyModel2, StudentViewHolderP> (optionamitt) {
                    @Override
                    protected void onBindViewHolder(@NonNull final StudentViewHolderP holder, final int position, @NonNull final MainMoneyModel2 model) {

                        MainRef.child ( "AllTrip" ).child(trip_key).child("Cost")
                                .addValueEventListener ( new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                                        int costnominal = (int)dataSnapshot.getChildrenCount();

                                        float sum = 0;
                                        for (int i = 0;i<costnominal;i++)
                                        {

                                            String df = getRef(i).getKey().toString();
                                            String stringo = dataSnapshot.child(df).child("Cost").getValue().toString();
                                            float fl = Float.parseFloat(stringo);
                                            sum = sum+fl;

                                        }

                                        String fg = String.valueOf(sum);
                                        end_trip_total_cost.setText("₹: "+fg+"0");


                                        HeelooFn(sum);

                                        progressDialog.dismiss();




                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                        progressDialog.dismiss();
                                        Toast.makeText(EndtripActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                                    }
                                } );


                    }

                    @Override
                    public StudentViewHolderP onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view  = LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.dummy_layout,viewGroup,false );
                        StudentViewHolderP viewHolder  = new StudentViewHolderP(  view);
                        return viewHolder;

                    }



                };
        end_trp.setAdapter(adaptt);
        adaptt.startListening ();












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

                                        final String retrieveUserNAmeer = dataSnapshot.child ( "Name" ).getValue ().toString ();

                                        if (dataSnapshot.hasChild("Image"))
                                        {
                                            String retrieveUserImage= dataSnapshot.child ( "Image" ).getValue ().toString ();
                                            Picasso.get ().load ( retrieveUserImage ).placeholder ( R.drawable.profile_image ).error ( R.drawable.profile_image ).into ( holder.circleImageView );

                                        }



                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                DatabaseReference db = MemberRef.child(stringperson).child("Cost");

                                                FirebaseRecyclerOptions<MainMoneyModel> optionamitt =
                                                        new FirebaseRecyclerOptions.Builder<MainMoneyModel> ()
                                                                .setQuery (db,MainMoneyModel.class )
                                                                .build ();


                                                FirebaseRecyclerAdapter<MainMoneyModel, StudentViewHolderPY> adaptt =
                                                        new FirebaseRecyclerAdapter<MainMoneyModel, StudentViewHolderPY> (optionamitt) {
                                                            @Override
                                                            protected void onBindViewHolder(@NonNull final StudentViewHolderPY holderrr, final int position, @NonNull final MainMoneyModel model) {



                                                                db.addValueEventListener ( new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshout) {

                                                                        int costnominal = (int)dataSnapshout.getChildrenCount();

                                                                        float sum = 0;
                                                                        for (int i = 0;i<costnominal;i++)
                                                                        {

                                                                            String df = getRef(i).getKey().toString();
                                                                            String stringo = dataSnapshout.child(df).child("Cost").getValue().toString();
                                                                            float fl = Float.parseFloat(stringo);
                                                                            sum = sum+fl;

                                                                        }

                                                                        fg = String.valueOf(sum);

                                                                        if (money_cal>=sum)
                                                                        {

                                                                            String ghy  = String.valueOf(money_cal-sum);

                                                                            holder.name.setText ( retrieveUserNAmeer +"\n\n₹:"+fg+"0\n\nGIVE:₹"+ghy);
                                                                        }
                                                                        else
                                                                        {

                                                                            String ghy  = String.valueOf(sum-money_cal);

                                                                            holder.name.setText ( retrieveUserNAmeer +"\n\n₹:"+fg+"0\n\nGET:₹"+ghy);
                                                                        }







                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(EndtripActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } );


                                                            }

                                                            @NonNull
                                                            @Override
                                                            public StudentViewHolderPY onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                                                                View view  = LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.dummy_layout,viewGroup,false );
                                                                StudentViewHolderPY viewHolder  = new StudentViewHolderPY(  view);
                                                                return viewHolder;

                                                            }



                                                        };
                                                falsee.setAdapter(adaptt);
                                                adaptt.startListening ();

                                            }
                                        });






                                        holder.name.setText ( retrieveUserNAmeer);
                                        progressDialog.dismiss();






                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                        progressDialog.dismiss();
                                        Toast.makeText(EndtripActivity.this, "Error !", Toast.LENGTH_SHORT).show();
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
        hmm_recycler_end.setAdapter ( adapterrrt );

        adapterrrt.startListening ();












    }





    private void HeelooFn(float sum) {

        MainRef.child ( "AllTrip" ).child(trip_key).child("Member")
                .addValueEventListener ( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                        int costnominalr = (int)dataSnapshot.getChildrenCount();


                        float gl = (float)sum/costnominalr;
                        money_cal = gl;

                        String fg = String.valueOf(gl);
                        end_trip_each_cost.setText("₹: "+fg+"0");


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        progressDialog.dismiss();
                        Toast.makeText(EndtripActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                    }
                } );

    }

    public static class StudentViewHolderP extends  RecyclerView.ViewHolder
    {

        TextView textView;


        public StudentViewHolderP(@NonNull View itemView) {
            super ( itemView );


            textView = itemView.findViewById(R.id.dummy_textview);


        }
    }

    public static class StudentViewHolderPY extends  RecyclerView.ViewHolder
    {

        TextView textView;


        public StudentViewHolderPY(@NonNull View itemView) {
            super ( itemView );


            textView = itemView.findViewById(R.id.dummy_textview);


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