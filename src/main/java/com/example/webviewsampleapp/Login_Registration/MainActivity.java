package com.example.webviewsampleapp.Login_Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button joinNowButton, loginButton;
    ProgressDialog loadingBar;
    TextView becomeSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        joinNowButton=findViewById(R.id.main_join_now_btn);
        loginButton=findViewById(R.id.main_login_btn);
        becomeSeller=findViewById(R.id.seller_begin);
        becomeSeller.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        joinNowButton.setOnClickListener(this);
        loadingBar= new ProgressDialog(this);

        Paper.init(this);
        //Getting the value from Paper
        String userPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String userPasswordKey = Paper.book().read(Prevalent.userPasswordKey);

        if(userPhoneKey !="" && userPasswordKey !="")
        {
            if(!TextUtils.isEmpty(userPhoneKey )&& !TextUtils.isEmpty(userPasswordKey))
            {
                allowAccess(userPhoneKey,userPasswordKey);

                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

    }
    private void allowAccess(final String phone, final String password) {


        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("Users").child(phone).exists())
                {
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone)){

                        if(usersData.getPassword().equals(password))
                        {
                            Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            //Adding User Info to Prevalent when logging in;
                            Prevalent.currentOnlineUser =usersData;
                            startActivity(new Intent(getApplicationContext(), MainWeb.class));
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Passwrod is incorrect", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Phone is incorrect", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Account with this " + phone +" Does not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onClick(View view) {
        if(view==loginButton){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        if(view==joinNowButton){
            startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));

        }
    }
}
