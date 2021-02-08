package modernexpensetrackerbyamitmaity.example.modernexpensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText password,email,name;
    private TextView signin;
    private Button submit;
    private DatabaseReference RootRef;
    private DatabaseReference RootRefmain;
    private FirebaseAuth mauth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mauth = FirebaseAuth.getInstance ();
        RootRefmain = FirebaseDatabase.getInstance ().getReference ();
        password = findViewById(R.id.signup_password);
        email = findViewById(R.id.signup_email_adress);
        name = findViewById(R.id.signup_name);
        signin = findViewById(R.id.signup_signin_button);
        submit = findViewById(R.id.signup_button);
        progressDialog = new ProgressDialog (this);
        progressDialog.setContentView ( R.layout.loading );
        progressDialog.setTitle ( "Please Wait" );
        progressDialog.setMessage ( "Tips: Please Cheak your Internet or Wi-fi Connection" );
        progressDialog.setCanceledOnTouchOutside ( false );

        RootRef= FirebaseDatabase.getInstance ().getReference ();


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoLoginActivity();
            }
        });

        submit.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String passwordstring = password.getText ().toString ();
                String emialiIDstring= email.getText ().toString ();
                String namestring= name.getText ().toString ();


                if (TextUtils.isEmpty ( passwordstring ))
                {
                    Toast.makeText ( RegisterActivity.this, "You donot input Password", Toast.LENGTH_SHORT ).show ();
                }

                else if (TextUtils.isEmpty ( emialiIDstring ))
                {
                    Toast.makeText ( RegisterActivity.this, "You donot input Email ID ", Toast.LENGTH_SHORT ).show ();
                }
                else if (TextUtils.isEmpty ( namestring ))
                {
                    Toast.makeText ( RegisterActivity.this, "You donot input Name ", Toast.LENGTH_SHORT ).show ();
                }
                else
                {
                    CreateNewAccount ();
                }


            }
        } );
    }
    private void  CreateNewAccount()
    {
        String emailstring = email.getText ().toString ();
        String passwordstring = password.getText ().toString ();
        String namestring = name.getText ().toString ();

        progressDialog.show();


        mauth.createUserWithEmailAndPassword ( emailstring,passwordstring )
                .addOnCompleteListener ( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful ())
                        {
                            String cuurrentUserID = mauth.getCurrentUser ().getUid ();
                            RootRef.child ( "Users" ).child ( cuurrentUserID ).child ( "Name" ).setValue ( namestring );
                            RootRef.child ( "Users" ).child ( cuurrentUserID ).child ( "Email" ).setValue ( emailstring );
                            RootRef.child ( "Users" ).child ( cuurrentUserID ).child ( "MyTrip" ).setValue ( "" );

                            Toast.makeText (RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT ).show ();

                            GotoLoginActivity();
                            progressDialog.dismiss();
                        }
                        else
                        {
                            String message = task.getException ().toString ();
                            progressDialog.dismiss();
                            Toast.makeText ( RegisterActivity.this, "Error" + message, Toast.LENGTH_SHORT ).show ();

                        }
                    }
                } );


    }
    private void GotoLoginActivity() {


        Intent loginIntent = new Intent ( RegisterActivity.this,LoginActivity.class );
        loginIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity ( loginIntent );
        finish ();
    }
}