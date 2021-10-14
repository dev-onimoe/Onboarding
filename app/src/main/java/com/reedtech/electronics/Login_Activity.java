package com.reedtech.electronics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.reedtech.electronics.interfaces.ApiQueries;
import com.reedtech.electronics.models.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login_Activity extends AppCompatActivity {

    private Button loginpage_signupbtn, sign_inbtn;
    private EditText login_username_edittext, login_password_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginpage_signupbtn = findViewById(R.id.login_signupbtn);
        sign_inbtn = findViewById(R.id.login_signupbtn);
        login_username_edittext = findViewById(R.id.login_username_et);
        login_password_edittext = findViewById(R.id.login_password_et);

        sign_inbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_email = login_username_edittext.getText().toString();
                String password = login_password_edittext.getText().toString();

                if (!username_email.isEmpty() && !password.isEmpty()) {
                    signin_user(username_email, password);
                } else {
                    Toast.makeText(Login_Activity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void signin_user(String username_email, String password) {
        Retrofit rfit = new Retrofit.Builder().baseUrl("http://b9cadab8af18.ngrok.io/").addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiQueries apiq = rfit.create(ApiQueries.class);
        Call<List<Users>> getcurrentuser = apiq.getusers();
        getcurrentuser.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if (javax.ws.rs.core.Response.Status.Family.familyOf(response.code()).equals(javax.ws.rs.core.Response.Status.Family.SUCCESSFUL)) {
                    List<Users> usr_list = response.body();
                    for (int i = 0; i < usr_list.size(); i++) {
                        if (usr_list.get(i).getEmail().equalsIgnoreCase(username_email) || usr_list.get(i).getUsername().equalsIgnoreCase(username_email)) {
                            Users current_user = usr_list.get(i);
                            if (password.equalsIgnoreCase(current_user.getPassword())) {
                                Prevalent.currentuser = current_user;
                                FirebaseAuth fauth = FirebaseAuth.getInstance();
                                fauth.signInWithEmailAndPassword(current_user.getEmail(), current_user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (fauth.getCurrentUser().isEmailVerified()) {
                                            Intent intent = new Intent(Login_Activity.this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                                break;
                            }
                        } else if (i == usr_list.size() - 1 && (!usr_list.get(i).getEmail().equalsIgnoreCase(username_email) || usr_list.get(i).getUsername().equalsIgnoreCase(username_email))) {
                            Toast.makeText(Login_Activity.this, "This user does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(Login_Activity.this, response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Toast.makeText(Login_Activity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}