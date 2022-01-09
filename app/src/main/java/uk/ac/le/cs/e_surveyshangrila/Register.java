package uk.ac.le.cs.e_surveyshangrila;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.widget.Toast.LENGTH_SHORT;

public class Register extends AppCompatActivity {

    private static final String TAG = "Register";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText mEmail, mPassword, mSNI;
    private Button mRegisterButton;
    private FirebaseAuth fAuth;
    private String userID;
    private TextView loginLink;
    private TextView QRScannerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = findViewById(R.id.editTextEmail);
        mPassword = findViewById(R.id.editTextPassword);
        mRegisterButton = findViewById(R.id.registerButton);
        fAuth = FirebaseAuth.getInstance();
        mDisplayDate = findViewById(R.id.selectDOBText);
        loginLink = findViewById(R.id.loginLink);
        QRScannerLink = findViewById(R.id.QRScannerLink);
        mSNI = findViewById(R.id.editTextSNI);

        if (getIntent().getExtras() != null) {
            String SNI = getIntent().getExtras().getString("SNI");
            mSNI.setText(SNI);
        }

        QRScannerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(), SNIScanner.class);
               startActivity(intent);
            }
        });


        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Register.this, mDateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String stringMonth = (month < 9 ? "0" : "") + (month + 1);
                String stringDay = (day < 10 ? "0" : "") + day;
                String date = stringDay + "/" + stringMonth + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User Created", LENGTH_SHORT).show();

                            userID = fAuth.getCurrentUser().getUid();

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(Register.this, task.getException().getMessage(), LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}
