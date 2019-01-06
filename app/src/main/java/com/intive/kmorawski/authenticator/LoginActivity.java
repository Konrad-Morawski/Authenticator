package com.intive.kmorawski.authenticator;

import android.app.ActivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private final Authenticator authenticator = new Authenticator(AccountsProviderFactory.getAccountsProvider());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText login = findViewById(R.id.login_input);
        final EditText password = findViewById(R.id.password_input);

        // hide the status bar from the evil monkey
        if (ActivityManager.isUserAMonkey()) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        induceArtificialBug();

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationResult result = authenticate(login, password);
                presentResult(result);
            }
        });
    }

    // just to make monkey tests crash
    private void induceArtificialBug() {
        ((TextView) findViewById(R.id.login_input))
                .addTextChangedListener(
                        new TextWatcher() {
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (s.toString().length() > 2) {
                                    throw new IllegalStateException("Stupid bug crashes the app once this fairly normal condition occurs");
                                }
                            }

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });
    }

    private AuthenticationResult authenticate(EditText login, EditText password) {
        String enteredLogin = login.getText().toString();
        String enteredPassword = password.getText().toString();
        return authenticator.authenticate(
                enteredLogin,
                enteredPassword);
    }

    private void presentResult(AuthenticationResult result) {
        TextView authenticationError = findViewById(R.id.error_message);
        authenticationError.setVisibility(
                result.isAuthenticated()
                        ? View.GONE
                        : View.VISIBLE);

        if (result.isAuthenticated()) {
            onAuthenticatedSuccessfully();
        } else {
            onAuthenticationFailure(
                    result,
                    authenticationError);
        }
    }

    private void onAuthenticatedSuccessfully() {
        Toast.makeText(LoginActivity.this, "Logging in!", Toast.LENGTH_LONG).show();
        // ...and let the user through to "the app" (had it existed)
    }

    private void onAuthenticationFailure(AuthenticationResult result, TextView authenticationError) {
        String errorMessage = getErrorMessageFor(result);
        authenticationError.setText(errorMessage);
    }

    @NonNull
    private String getErrorMessageFor(AuthenticationResult result) {
        final String errorMessage;
        switch (result) {
            case NO_USER:
                errorMessage = "User is not registered";
                break;
            case INCORRECT_PASSWORD:
                errorMessage = "Password is incorrect";
                break;
            case BLANK_LOGIN:
                errorMessage = "Login can't be blank";
                break;
            case BLANK_PASSWORD:
                errorMessage = "Password can't be blank";
                break;
            case OK:
                throw new IllegalStateException("Code shouldn't try to obtain an error message for " + result);
            default:
                errorMessage = "Unknown error (" + result + ")";
        }
        return errorMessage;
    }
}