package mich.gwan.sarensa.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.helpers.InputValidation;
import mich.gwan.sarensa.info.InformationApi;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutSpinner;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextSpinner;

    private AppCompatButton appCompatButtonLogin;

    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private InformationApi informationApi;
    private AppCompatSpinner spinner;

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String PASSWORD_KEY = "password_key";
    public static final String USER_TYPE_KEY = "user_type_key";
    SharedPreferences sharedPreferences;
    String email, userType, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Objects.requireNonNull(getSupportActionBar()).hide();
        initViews();
        initListeners();
        initObjects();

        List<String> categories = new ArrayList<String>();
         categories.add("Select User Type");
         categories.add("ADMIN");
         categories.add("EMPLOYEE");
         categories.add("MANAGER");

         ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
         dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinner.setAdapter(dataAdapter);
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.loginWindow));
        } catch (InflateException e){
            e.getCause().printStackTrace();
        } catch (Exception e) {

            // generic exception handling
            e.printStackTrace();
        }
    }
    /**
     * This method is to initialize views
     */
    private void initViews() {

        nestedScrollView = findViewById(R.id.nestedScrollView);

        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutSpinner = findViewById(R.id.textInputLayoutSpinner);

        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = findViewById(R.id.textViewLinkRegister);

        spinner = findViewById(R.id.spinner);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(this);
        informationApi = new InformationApi();
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL_KEY,null);
        userType = sharedPreferences.getString(USER_TYPE_KEY,null);
        password = sharedPreferences.getString(PASSWORD_KEY, null);

    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                if (!databaseHelper.checkUser()) {
                    Intent intentRegister = new Intent(getApplicationContext(), mich.gwan.sarensa.activities.RegisterActivity.class);
                    startActivity(intentRegister);
                } else {
                    informationApi.snackBar(nestedScrollView,"A user already exists! Please login.",Color.MAGENTA);
                }
                break;
        }
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromSQLite() {
        if (!inputValidation.isSpinnerEmpty(spinner, textInputLayoutSpinner, getString(R.string.error_message_spinner))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_mssage_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (databaseHelper.checkUser( spinner.getSelectedItem().toString().trim().toUpperCase(), textInputEditTextEmail.getText().toString().trim(), textInputEditTextPassword.getText().toString().trim())){

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            // your code here
                            Intent intent;
                            intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();
                        }
                    },
                    1000
            );
            informationApi.snackBar(nestedScrollView, "Session opened Successfully!", Color.GREEN);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
            // put values for password, email and usertype
            editor.putString(EMAIL_KEY, textInputEditTextEmail.getText().toString());
            editor.putString(PASSWORD_KEY, textInputEditTextPassword.getText().toString());
            editor.putString(USER_TYPE_KEY, spinner.getSelectedItem().toString());
            // save data with key and value
            editor.apply();
            // clear fields
            emptyInputEditText();

        } else {
            // Snack Bar to show message that record is wrong
            informationApi.snackBar(nestedScrollView, "Invalid Email or Password!", Color.RED);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (email != null && password != null && userType != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    private Boolean exit = false;

    /**
     * exit app
     */
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        spinner.setPrompt("Select User Type");
    }
}
