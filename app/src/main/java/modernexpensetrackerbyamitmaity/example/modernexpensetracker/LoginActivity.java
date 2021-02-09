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

public class LoginActivity extends AppCompatActivity {
    private EditText password,email;
    private TextView signup,forgotpass;
    private Button submit;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mAuth = FirebaseAuth.getInstance ();
        progressDialog = new ProgressDialog (this);
        progressDialog.setContentView ( R.layout.loading );
        progressDialog.setTitle ( "Please Wait" );
        progressDialog.setMessage ( "Tips: Please Cheak your Internet or Wi-fi Connection" );
        progressDialog.setCanceledOnTouchOutside ( false );
        password = findViewById(R.id.login_password);
        email = findViewById(R.id.login_email_adress);
        signup = findViewById(R.id.login_signup_button);
        forgotpass = findViewById(R.id.login_forgot_pass);
        submit = findViewById(R.id.login_submit_button);


        submit.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        } );

        signup.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                GOTOTSIGNUPACTIVITY();
            }
        } );
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntentrtyh = new Intent ( LoginActivity.this,ForgotPasswordActivity.class );
                startActivity ( loginIntentrtyh );
            }
        });

    }

    private void GOTOTSIGNUPACTIVITY() {
        Intent loginIntentrt = new Intent ( LoginActivity.this,RegisterActivity.class );
        startActivity ( loginIntentrt );
    }

    private void AllowUserToLogin() {
        String emailstring = email.getText ().toString ();
        String passwordstring = password.getText ().toString ();
        if (TextUtils.isEmpty ( emailstring )) {
            Toast.makeText ( this, "Please Enter email.....", Toast.LENGTH_SHORT ).show ();
        }
        if (TextUtils.isEmpty ( passwordstring )) {
            Toast.makeText ( this, "Please Enter Password.....", Toast.LENGTH_SHORT ).show ();
        }
        else {
            progressDialog.show();

            mAuth.signInWithEmailAndPassword ( emailstring, passwordstring )
                    .addOnCompleteListener ( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful ()) {


                                progressDialog.dismiss();


                                SendUserToMainActivity();
                                Toast.makeText ( LoginActivity.this, "Successfully Logged IN", Toast.LENGTH_SHORT ).show ();


                            } else {
                                progressDialog.dismiss();
                                String message = task.getException ().toString ();
                                Toast.makeText ( LoginActivity.this, "Error" + message, Toast.LENGTH_SHORT ).show ();

                            }
                        }
                    } );
        }
    }

    private void SendUserToMainActivity() {
        Intent loginIntent = new Intent ( LoginActivity.this,MainActivity.class );
        loginIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity ( loginIntent );
        finish ();
    }

}