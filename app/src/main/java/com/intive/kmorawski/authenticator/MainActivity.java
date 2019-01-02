package com.intive.kmorawski.authenticator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String USER_NAME_KEY = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null) {
            return;
        }
        String userName = intent.getExtras().getString(USER_NAME_KEY);
        TextView prompt = findViewById(R.id.prompt);
        prompt.setText(getString(R.string.logged_in_prompt, userName));
    }
}
