package mich.gwan.sarensa.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.databinding.ActivityCategoryBinding;
import mich.gwan.sarensa.databinding.ActivityRegisterBinding;
import mich.gwan.sarensa.helpers.InputValidation;
import mich.gwan.sarensa.info.InformationApi;
import mich.gwan.sarensa.model.User;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private final AppCompatActivity activity = RegisterActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutPhone;
    private TextInputLayout textInputLayoutConfirmPassword;
    private TextInputLayout textInputLayoutSpinner;

    private TextInputEditText textInputEditTextName;
    private AppCompatSpinner spinner;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextPhone;
    private TextInputEditText textInputEditTextConfirmPassword;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;

    private InputValidation inputValidation;
    private InformationApi informationApi;
    private DatabaseHelper databaseHelper;
    private User user;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        initListeners();
        initObjects();
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.window));
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = binding.nestedScrollView;

        textInputLayoutName = binding.textInputLayoutName;
        textInputLayoutPhone = binding.textInputLayoutPhone;
        textInputLayoutEmail = binding.textInputLayoutEmail;
        textInputLayoutPassword = binding.textInputLayoutPassword;
        textInputLayoutConfirmPassword = binding.textInputLayoutConfirmPassword;
        textInputLayoutSpinner = binding.textInputLayoutSpinner;

        spinner = binding.spinner;
        textInputEditTextName = binding.textInputEditTextName;
        textInputEditTextEmail = binding.textInputEditTextEmail;
        textInputEditTextPhone = binding.textPhone;
        textInputEditTextPassword = binding.textPassword;
        textInputEditTextConfirmPassword = binding.textConfirmPassword;

        appCompatButtonRegister = binding.appCompatButtonRegister;

        appCompatTextViewLoginLink = binding.appCompatTextViewLoginLink;

        List<String> categories = new ArrayList<String>();
         categories.add("Select User Type");
         categories.add("ADMIN");
         categories.add("EMPLOYEE");
         categories.add("MANAGER");
         ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
         dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinner.setAdapter(dataAdapter);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDataToSQLite();
            }
        });
        databaseHelper = new DatabaseHelper(this);
        appCompatTextViewLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(this);
        informationApi = new InformationApi();
        user = new User();

    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {
        if (!inputValidation.isSpinnerEmpty(spinner, textInputLayoutSpinner, getString(R.string.error_message_spinner))) {
            spinner.requestFocus();
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            textInputEditTextName.requestFocus();
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_mssage_email))) {
            textInputEditTextEmail.requestFocus();
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            textInputEditTextEmail.requestFocus();
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPhone, textInputLayoutPhone, getString(R.string.error_message_phone))) {
            textInputEditTextPhone.requestFocus();
            return;
        }
        if (!inputValidation.isValidPhoneNumber(textInputEditTextPhone, textInputLayoutPhone, getString(R.string.error_mssage_phone))) {
            textInputEditTextPhone.requestFocus();
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            textInputEditTextPassword.requestFocus();
            return;
        }
        if (!inputValidation.isValidPassword(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_mssage_password))) {
            textInputEditTextPassword.requestFocus();
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextConfirmPassword, textInputLayoutConfirmPassword, getString(R.string.error_message_password_confirm))) {
            textInputEditTextConfirmPassword.requestFocus();
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword, textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            textInputEditTextPassword.requestFocus();
            return;
        }

        if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {

            user.setUserName(textInputEditTextName.getText().toString().trim().toUpperCase());
            user.setUserPhone(textInputEditTextPhone.getText().toString().trim().toUpperCase());
            user.setUserEmail(textInputEditTextEmail.getText().toString().trim());
            user.setUserPassword(textInputEditTextPassword.getText().toString().trim());
            user.setUserType(spinner.getSelectedItem().toString().toUpperCase());
            // insert to database
            databaseHelper.addUser(user);
            // display success message
            informationApi.snackBar(nestedScrollView, getString(R.string.success_message), Color.BLUE);
            emptyInputEditText();

        } else {
            // display error message
            informationApi.snackBar(nestedScrollView, getString(R.string.error_email_exists), Color.RED);
            textInputEditTextEmail.requestFocus();
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
        textInputEditTextPhone.setText(null);
        spinner.setPrompt("Select User Type");
    }
}
