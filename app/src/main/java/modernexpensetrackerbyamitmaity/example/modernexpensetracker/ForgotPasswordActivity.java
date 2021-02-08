package modernexpensetrackerbyamitmaity.example.modernexpensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button submit;
    private FirebaseAuth mAuth;
    private EditText email;
    private TextView confirm;
    private String string ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth= FirebaseAuth.getInstance ();

        submit = findViewById(R.id.forgot_email_button);
        email = findViewById(R.id.forgot_email);
        confirm = findViewById(R.id.confirm_sent);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelloEmail();
            }
        });



    }

    public void HelloEmail()
    {

        String stringo = email.getText().toString();
        if (stringo.equals(null))
        {
            Toast.makeText(ForgotPasswordActivity.this, "Enter the Email Address..", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ForgotPasswordActivity.this, stringo, Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().sendPasswordResetEmail(stringo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            email.setText(null);
                            confirm.setVisibility(View.VISIBLE);
                            submit.setAlpha(0.5f);
                        }
                    });
        }
    }
}