package com.example.webviewsampleapp.Login_Registration;

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

import com.example.webviewsampleapp.Models.Users;
import com.example.webviewsampleapp.Prevalent.Prevalent;
import com.example.webviewsampleapp.R;
import com.example.webviewsampleapp.WebView.MainWeb;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editnumber, editPassword;
    Button loginButton;
    ProgressDialog loadingBar;
    String parentDbname="Users";
    CheckBox checkBox;
    TextView adminLink, notAdmin, forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginButton=findViewById(R.id.login_btn);
        editnumber=findViewById(R.id.enter_phone_nuber_input);
        editPassword=findViewById(R.id.login_password_input);
        adminLink=findViewById(R.id.panel_link);
        notAdmin=findViewById(R.id.not_panel_link);
        forgetPassword=findViewById(R.id.forget_password);

        loadingBar= new ProgressDialog(this);
        adminLink.setOnClickListener(this);
        checkBox = findViewById(R.id.rememeber_me_check);
        Paper.init(this);


        forgetPassword.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        adminLink.setOnClickListener(this);
        notAdmin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view==loginButton)
        {
            loginUser();
        }
        if(view==adminLink)
        {
            Toast.makeText(getApplicationContext(), "This feature is not working on this demo App", Toast.LENGTH_SHORT).show();


        }if(view==notAdmin) {

            Toast.makeText(getApplicationContext(), "This feature is not working on this demo App", Toast.LENGTH_SHORT).show();

        }
        if (view==forgetPassword)
        {
            Toast.makeText(getApplicationContext(), "This feature is not working on this demo App", Toast.LENGTH_SHORT).show();

        }
    }
    private void loginUser() {

        String getPhoneNum = editnumber.getText().toString();
        String getPassword = editPassword.getText().toString();

        if(TextUtils.isEmpty(String.valueOf(editnumber)))
        {
            Toast.makeText(getApplicationContext(), "Please write your name...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(String.valueOf(editPassword)))
        {
            Toast.makeText(getApplicationContext(), "Please write your name...", Toast.LENGTH_SHORT).show();
        }else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAccessToAccount(getPhoneNum,getPassword);
        }
    }
    private void allowAccessToAccount(final String phone, final String password)
    {

        if(checkBox.isChecked())
        {
            //Storing value to Paper
            Paper.book().write(Prevalent.userPhoneKey, phone);
            Paper.book().write(Prevalent.userPasswordKey, password);

        }

        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(parentDbname).child(phone).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbname).child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone)){

                        if(usersData.getPassword().equals(password))
                        {


                                Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Prevalent.currentOnlineUser = usersData;
                                startActivity(new Intent(getApplicationContext(), MainWeb.class));

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Passwrod is incorrect", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Phone is incorrect", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Account with this " + phone +" Does not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
