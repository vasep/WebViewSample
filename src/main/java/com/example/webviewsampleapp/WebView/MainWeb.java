package com.example.webviewsampleapp.WebView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.webviewsampleapp.Login_Registration.MainActivity;
import com.example.webviewsampleapp.R;

import io.paperdb.Paper;

public class MainWeb extends AppCompatActivity {
    private WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_web);
        myWebView =findViewById(R.id.webview);
        myWebView.loadUrl("https://lab.safetylabs.org//main_page/main.jsp");
    }

}
