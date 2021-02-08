package modernexpensetrackerbyamitmaity.example.modernexpensetracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyprofileActivity extends AppCompatActivity {
    private TextView name,emailId,trip;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private CircleImageView circleImageView;
    private StorageReference UserProfileImagesRef;
    private String currentUserID;
    private StorageTask uploadtask;
    private ImageView imageView,editprofile;
    private String cheaker = "";
    private DatabaseReference RootRef,UserRef;
    private ImageView profilimage;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);



        UserProfileImagesRef = FirebaseStorage.getInstance ().getReference ().child ( "Profilee Images" );

        mAuth= FirebaseAuth.getInstance ();
        currentUserID = mAuth.getCurrentUser ().getUid ();
        RootRef= FirebaseDatabase.getInstance ().getReference ();
        UserRef= FirebaseDatabase.getInstance ().getReference ().child ( "Users" );

        name = findViewById ( R.id.user_profile_name );
        trip = findViewById(R.id.user_profile_trip);
        emailId =findViewById ( R.id.user_profile_email );
        editprofile = findViewById(R.id.edit_profile_name);
        profilimage = findViewById ( R.id.profile_image);
        circleImageView = findViewById ( R.id.profiele_image );
        logout = findViewById(R.id.log_out);


        progressDialog = new ProgressDialog (this);
        progressDialog.setContentView ( R.layout.loading );
        progressDialog.setTitle ( "Please Wait" );
        progressDialog.setMessage ( "Tips: Please Cheak your Internet or Wi-fi Connection" );
        progressDialog.setCanceledOnTouchOutside ( false );


        profilimage.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                cheaker = "image";
                Intent intent = new Intent ();
                intent.setAction ( Intent.ACTION_GET_CONTENT );
                intent.setType ( "image/*" );
                startActivityForResult ( Intent.createChooser( intent, "Select Image" ), 438 );


            }
        } );
        RetriveYouserInfo();
        RetriveUserInformation();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent loginIntenttt = new Intent ( MyprofileActivity.this,LoginActivity.class );
                loginIntenttt.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity ( loginIntenttt );
                finish ();
            }
        });


        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyprofileActivity.this);
                alertDialog.setTitle("Set up Your Name");
                alertDialog.setMessage("Enter your name..");
                final EditText input = new EditText(MyprofileActivity.this);
                input.setHint("Enter Your Name..");
                alertDialog.setView(input);
                alertDialog.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.show();
                                String nameouser = input.getText().toString();
                                mAuth= FirebaseAuth.getInstance ();
                                currentUserID = mAuth.getCurrentUser ().getUid ();
                                UserRef.child(currentUserID).child("Name").setValue(nameouser);
                                dialogInterface.cancel();
                                progressDialog.cancel();
                            }
                        });
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                alertDialog.show();

            }
        });



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult ( requestCode, resultCode, data );

        progressDialog.show ();

        if (requestCode == 438 && resultCode == RESULT_OK && data != null && data.getData () != null) {


            Uri resultUri = data.getData ();
            StorageReference filepath = UserProfileImagesRef.child ( currentUserID + ".jpg");

            filepath.putFile ( resultUri ).addOnSuccessListener ( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Task<Uri> firebaseUri  = taskSnapshot.getStorage ().getDownloadUrl ();
                    firebaseUri.addOnSuccessListener ( new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final  String downloadUrl = uri.toString ();
                            RootRef.child ( "Users" ).child ( currentUserID ).child ( "Image" ).setValue ( downloadUrl )
                                    .addOnCompleteListener ( new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful ()){
                                                Toast.makeText ( MyprofileActivity.this, "Suceed", Toast.LENGTH_SHORT ).show ();
                                                progressDialog.dismiss ();
                                            }
                                            else {
                                                progressDialog.dismiss ();
                                                String message = task.getException ().toString ();
                                                Toast.makeText ( MyprofileActivity.this, "Error"+message, Toast.LENGTH_SHORT ).show ();
                                            }
                                        }
                                    } );
                        }
                    } );
                }
            } );


        }
        else {
            progressDialog.dismiss ();
        }
    }

    private void RetriveUserInformation() {
        progressDialog.show ();

        UserRef.child ( currentUserID ).addValueEventListener ( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if ((dataSnapshot.exists ()) && (dataSnapshot.hasChild ( "Image" )))
                {
                    String StudentNamea = dataSnapshot.child ("Image" ).getValue ().toString ();

                    Picasso.get ().load ( StudentNamea ).placeholder ( R.drawable.profile_image ).error ( R.drawable.profile_image ).into ( circleImageView );
                    progressDialog.dismiss ();
                }
                else
                {
                    progressDialog.dismiss ();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss ();
            }
        } );


    }

    private void RetriveYouserInfo() {

        progressDialog.show ();
        RootRef.child ( "Users" ).child ( currentUserID )
                .addValueEventListener ( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String retrieveUserNAme = dataSnapshot.child ( "Name" ).getValue ().toString ();
                        String retrieveEmail = dataSnapshot.child ( "Email" ).getValue ().toString ();


                        String stringperson = String.valueOf((int)dataSnapshot.child("MyTrip").getChildrenCount());

                        name.setText ( retrieveUserNAme );
                        emailId.setText ( retrieveEmail );
                        trip.setText(stringperson);
                        progressDialog.dismiss ();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        progressDialog.dismiss();
                        Toast.makeText(MyprofileActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                    }
                } );
    }

}