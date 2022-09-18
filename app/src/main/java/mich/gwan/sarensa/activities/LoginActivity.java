package mich.gwan.sarensa.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

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
    private AppCompatImageView icon;

    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private InformationApi informationApi;
    private AppCompatSpinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        window.setStatusBarColor(this.getResources().getColor(android.R.color.holo_green_dark));

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
        icon = findViewById(R.id.icon);
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
                //emptyInputEditText();
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), mich.gwan.sarensa.activities.RegisterActivity.class);
                startActivity(intentRegister);
                //emptyInputEditText();
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
        if (databaseHelper.checkUser( spinner.getSelectedItem().toString().trim().toLowerCase(), textInputEditTextEmail.getText().toString().trim(), textInputEditTextPassword.getText().toString().trim())){

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            // your code here
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    },
                    1000
            );
            informationApi.snackBar(nestedScrollView, "Session opened Successfully!", Color.MAGENTA);
            emptyInputEditText();

        } else {
            // Snack Bar to show message that record is wrong
            informationApi.snackBar(nestedScrollView, "Invalid Email or Password!", Color.RED);
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
