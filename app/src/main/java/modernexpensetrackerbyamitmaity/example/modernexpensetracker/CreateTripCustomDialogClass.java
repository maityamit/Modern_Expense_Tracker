package modernexpensetrackerbyamitmaity.example.modernexpensetracker;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CreateTripCustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes,no;
    private EditText tripname;
    private String currentUserID;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatePicker datepicker;
    private DatabaseReference RootRef;
    public CreateTripCustomDialogClass(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_create_trip_layout);
        mAuth= FirebaseAuth.getInstance ();
        currentUserID = mAuth.getCurrentUser ().getUid ();
        RootRef= FirebaseDatabase.getInstance ().getReference ();


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setContentView ( R.layout.loading );
        progressDialog.setTitle ( "Please Wait" );
        progressDialog.setMessage ( "Tips: Please Cheak your Internet or Wi-fi Connection" );
        progressDialog.setCanceledOnTouchOutside ( false );
        yes = (Button) findViewById(R.id.create_trip_submit_butyyon);
        no = (Button) findViewById(R.id.cancel_trip_submit_butyyon);
        tripname = (EditText)findViewById(R.id.edit_text_trip_name);
        datepicker = (DatePicker) findViewById(R.id.edit_text_trip_date);



        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trip_key = RootRef.child("AllTrip").push().getKey();
                CreteATripNew(trip_key);
                Intent loginIntent = new Intent ( getContext(),MainActivity.class );
                loginIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                getContext().startActivity ( loginIntent );
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent ( getContext(),MainActivity.class );
                loginIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                getContext().startActivity ( loginIntent );
            }
        });

    }

    private void CreteATripNew(String string_trip) {


        int year = datepicker.getYear();
        int month = datepicker.getMonth();
        int day = datepicker.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = format.format(calendar.getTime());
        String string = tripname.getText().toString();


        if (TextUtils.isEmpty (string))
        {
            Toast.makeText(c, "Enter any Trip name ..", Toast.LENGTH_SHORT).show();
        }
        else
        {
            tripname.setText(null);
            progressDialog.show();
            RootRef.child ( "Users" ).child ( currentUserID )
                    .addValueEventListener ( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            String retrieveUserNAme = dataSnapshot.child ( "Name" ).getValue ().toString ();

                            HashMap<String,Object> onlineStat = new HashMap<> (  );
                            onlineStat.put ( "TripName", string);
                            onlineStat.put ( "TripDate", strDate);
                            onlineStat.put ( "Creator", retrieveUserNAme);
                            onlineStat.put ( "Chat","");
                            onlineStat.put ( "Status","Active");
                            onlineStat.put ( "Member/"+currentUserID+"/Person",currentUserID);

                            Calendar calendarrr = Calendar.getInstance ();

                            SimpleDateFormat currentdate = new SimpleDateFormat ( "MMM dd, yyyy" );
                            String SaveCurrentData = currentdate.format ( calendarrr.getTime () );


                            RootRef.child ( "AllTrip" ).child (string_trip )
                                    .updateChildren ( onlineStat );
                            String chat_key = RootRef.child("AllTrip").child(string_trip).child("Chat").push().getKey();
                            HashMap<String,Object> onlineStatt = new HashMap<> (  );
                            onlineStatt.put ( "AllTrip/"+string_trip+"/Chat/"+chat_key+"/Chat", SaveCurrentData+"\n"+retrieveUserNAme+" created this trip");
                            onlineStatt.put("Users/"+currentUserID+"/MyTrip/"+string_trip+"/ID",string_trip);
                            RootRef.updateChildren(onlineStatt);
                            tripname.setText(null);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Error !", Toast.LENGTH_SHORT).show();
                        }
                    } );
        }











    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.create_trip_submit_butyyon) {
            c.finish();
            dismiss();
        }else if (v.getId()==R.id.cancel_trip_submit_butyyon){
            c.finish();
            dismiss();
        }
        dismiss();
    }


}