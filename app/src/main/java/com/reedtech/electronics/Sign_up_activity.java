package com.reedtech.electronics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.reedtech.electronics.interfaces.ApiQueries;
import com.reedtech.electronics.models.Users;

import java.util.List;

import javax.ws.rs.core.Response.Status.Family;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Sign_up_activity extends AppCompatActivity {
    private ViewFlipper welcome_vf;
    private TextView prev;
    private Button next_btn;
    private EditText fname, lname, uname, pass, cpass, email;
    private ProgressBar signup_pbar;
    private ApiQueries apiQueries;
    private FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activity);

        welcome_vf = findViewById(R.id.welcome_vf);
        prev = findViewById(R.id.prev_text);
        next_btn = findViewById(R.id.next_btn);
        fname = findViewById(R.id.signup_fname);
        lname = findViewById(R.id.signup_lname);
        uname = findViewById(R.id.signup_uname);
        pass = findViewById(R.id.signup_pass);
        cpass = findViewById(R.id.signup_cpass);
        email = findViewById(R.id.signup_email);
        signup_pbar = findViewById(R.id.signup_pbar);
        fauth = FirebaseAuth.getInstance();

        welcome_vf.addOnLayoutChangeListener(vf_listener);

    }

    public void checkuser(String fname_text, String lname_text, String uname_text, String email_text, String pass_text){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://b9cadab8af18.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        signup_pbar.setVisibility(View.VISIBLE);

        ApiQueries apiQ = retrofit.create(ApiQueries.class);

        Call<List<Users>> user_checklist = apiQueries.getusers();
        user_checklist.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if (javax.ws.rs.core.Response.Status.Family.familyOf(response.code()).equals(Family.SUCCESSFUL)) {
                   List<Users> usr_list = response.body();
                   for(int i = 0; i<usr_list.size(); i++){
                       if(usr_list.get(i).getEmail().equalsIgnoreCase(email_text)){
                           Toast.makeText(Sign_up_activity.this, "This email already exists", Toast.LENGTH_SHORT).show();
                           signup_pbar.setVisibility(View.GONE);
                       }else if(i==usr_list.size()-1 && !usr_list.get(i).getEmail().equalsIgnoreCase(email_text)){
                           createuser(fname_text, lname_text, uname_text, email_text, pass_text);
                       }
                   }
                } else {
                    Toast.makeText(Sign_up_activity.this, response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    signup_pbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Toast.makeText(Sign_up_activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                signup_pbar.setVisibility(View.GONE);
            }
        });
    }

    public void createuser(String fname_text, String lname_text, String uname_text, String email_text, String pass_text) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://b9cadab8af18.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        signup_pbar.setVisibility(View.VISIBLE);

        apiQueries = retrofit.create(ApiQueries.class);
        Call<Users> cur_user =
                apiQueries.
                        newuser(new Users(null, fname_text, lname_text, uname_text, email_text, pass_text));
        cur_user.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {

                if (javax.ws.rs.core.Response.Status.Family.familyOf(response.code()).equals(Family.SUCCESSFUL)) {
                    fauth.createUserWithEmailAndPassword(email_text, pass_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                fauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Sign_up_activity.this, "Registration successful, Please check your mail for verification", Toast.LENGTH_LONG).show();
                                            signup_pbar.setVisibility(View.GONE);
                                            Intent intent = new Intent(Sign_up_activity.this, Welcome_Activity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(Sign_up_activity.this, "Email verification couldn't be sent, Please check your email and try again", Toast.LENGTH_LONG).show();
                                            signup_pbar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(Sign_up_activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                signup_pbar.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    Toast.makeText(Sign_up_activity.this, response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    signup_pbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(Sign_up_activity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                signup_pbar.setVisibility(View.GONE);

            }
        });

    }

    View.OnLayoutChangeListener vf_listener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            email = findViewById(R.id.signup_email);
            uname = findViewById(R.id.signup_uname);
            pass = findViewById(R.id.signup_pass);
            cpass = findViewById(R.id.signup_cpass);
            fname = findViewById(R.id.signup_fname);
            lname = findViewById(R.id.signup_lname);

            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://b9cadab8af18.ngrok.io/")
                    .addConverterFactory(GsonConverterFactory.create()).build();
            signup_pbar.setVisibility(View.VISIBLE);

            ApiQueries apiQ = retrofit.create(ApiQueries.class);



            if (welcome_vf.getCurrentView() == welcome_vf.getChildAt(0)) {
                prev.setVisibility(View.INVISIBLE);
                next_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String fname_text = fname.getText().toString();
                        String lname_text = lname.getText().toString();
                        if (!fname_text.isEmpty() && !lname_text.isEmpty()) {
                            welcome_vf.showNext();
                        } else {
                            Toast.makeText(Sign_up_activity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (welcome_vf.getCurrentView() == welcome_vf.getChildAt(1)) {
                prev.setVisibility(View.VISIBLE);

                next_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String uname_text = uname.getText().toString();
                        String pass_text = pass.getText().toString();
                        String cpass_text = cpass.getText().toString();
                        Call<List<Users>> user_checklist = apiQueries.getusers();

                        if (!uname_text.isEmpty() && !pass_text.isEmpty() && !cpass_text.isEmpty()) {
                            if (pass_text.equals(cpass_text)) {
                                user_checklist.enqueue(new Callback<List<Users>>() {
                                    @Override
                                    public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                                        if (javax.ws.rs.core.Response.Status.Family.familyOf(response.code()).equals(Family.SUCCESSFUL)) {
                                            List<Users> usr_list = response.body();
                                            for(int i = 0; i<usr_list.size(); i++){
                                                if(usr_list.get(i).getUsername().equalsIgnoreCase(uname_text)){
                                                    Toast.makeText(Sign_up_activity.this, "This username already exists", Toast.LENGTH_SHORT).show();
                                                    break;
                                                }else if(i==usr_list.size()-1 && !usr_list.get(i).getUsername().equalsIgnoreCase(uname_text)){
                                                    welcome_vf.showNext();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(Sign_up_activity.this, response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                                            signup_pbar.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<Users>> call, Throwable t) {
                                        Toast.makeText(Sign_up_activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        signup_pbar.setVisibility(View.GONE);
                                    }
                                });
                            } else {
                                Toast.makeText(Sign_up_activity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(Sign_up_activity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        welcome_vf.showPrevious();
                    }
                });
            } else if (welcome_vf.getCurrentView() == welcome_vf.getChildAt(2)) {
                next_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String fname_text = fname.getText().toString();
                        String lname_text = lname.getText().toString();
                        String uname_text = uname.getText().toString();
                        String pass_text = pass.getText().toString();
                        String cpass_text = cpass.getText().toString();
                        String email_text = email.getText().toString();

                        if (!email_text.isEmpty()) {
                            checkuser(fname_text, lname_text, uname_text, email_text, pass_text);
                        } else {
                            Toast.makeText(Sign_up_activity.this, "Please fill out the email field", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        welcome_vf.showPrevious();
                    }
                });
            }
        }
    };
}