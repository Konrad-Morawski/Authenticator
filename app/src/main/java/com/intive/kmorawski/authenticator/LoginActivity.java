package com.intive.kmorawski.authenticator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private final Authenticator authenticator = new Authenticator(AccountsProviderFactory.getAccountsProvider());

    private EditText loginInput;
    private EditText passwordInput;

    @Nullable
    private BackgroundOperationObserver backgroundOperationObserver;

    public void injectBackgroundOperationObserver(BackgroundOperationObserver backgroundOperationObserver) {
        this.backgroundOperationObserver = backgroundOperationObserver;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginInput = findViewById(R.id.login_input);
        passwordInput = findViewById(R.id.password_input);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationResult result = authenticate();
                presentResult(result);
            }
        });
    }

    private AuthenticationResult authenticate() {
        String enteredLogin = loginInput.getText().toString();
        String enteredPassword = passwordInput.getText().toString();
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
        final Handler handler  = new Handler();

        final Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("Logging in...")
                .setMessage("It can take a moment")
                .create();

        if (backgroundOperationObserver != null) {
            backgroundOperationObserver.onStarted();
        }
        dialog.show();

        final Runnable goToMainScreen = new Runnable() {
            @Override
            public void run() {
                if (backgroundOperationObserver != null) {
                    backgroundOperationObserver.onFinished();
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                openMainActivity();
            }
        };

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(goToMainScreen);
            }
        });

        handler.postDelayed(goToMainScreen, 5000);
    }

    private void openMainActivity() {
        // this is a bit of a hack obviously; irrelevant to the example
        String successfulLogin = loginInput.getText().toString();
        Intent goToMain = new Intent(LoginActivity.this, MainActivity.class)
                .putExtra(
                        MainActivity.USER_NAME_KEY,
                        successfulLogin);
        startActivity(goToMain);
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