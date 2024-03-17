package mich.gwan.sarensa.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation {

    private final Context context;

    /**
     * ---------------------------------------------------------------------------------------------
     * Constructor
     * @param context
     * ---------------------------------------------------------------------------------------------
     * */
    public InputValidation(Context context) {
        this.context = context;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * method to check InputEditText filled .
     * @param textInputEditText
     * @param textInputLayout
     * @param message
     * @return
     * ---------------------------------------------------------------------------------------------
     * */
    public boolean isInputEditTextFilled(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * method checks whether spinner has a value
     * @param spinner AppCompatSpinner
     * @param textInputLayout Layout to display error message
     * @param message the error message
     * @return boolean
     * ---------------------------------------------------------------------------------------------
     * */
    public boolean isSpinnerEmpty(AppCompatSpinner spinner, TextInputLayout textInputLayout, String message){
        if(spinner.getSelectedItem() == "Select User Type" ) {
            textInputLayout.setError(message);
            return false;
        } else  {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * method to check InputEditText occupied.
     * @param textInputEditText
     * @param message
     * @return
     * ---------------------------------------------------------------------------------------------
     * */
    public boolean isEditTextOccupied(EditText textInputEditText, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty()) {
            textInputEditText.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            //textInputEditText.set(false);
        }

        return true;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * method to check InputEditText occupied.
     * @param textInputEditText
     * @param message
     * @return
     * ---------------------------------------------------------------------------------------------
     * */
    public boolean isEditTextMatch(EditText textInputEditText, String message) {
        String value = textInputEditText.getText().toString().toUpperCase().trim();
        String [] options =  {"ADMIN","EMPLOYEE","MANAGER"};
            if(!Arrays.asList(options).contains(value))
            {
                textInputEditText.setError(message);
                hideKeyboardFrom(textInputEditText);
                return false;
            }
        return true;
    }
    /**
     * check transaction type.
     * @param textInputEditText
     * @param message
     * @return
     * ---------------------------------------------------------------------------------------------
     * */
    public boolean isCredit(EditText textInputEditText, String message) {
        String value = textInputEditText.getText().toString().toUpperCase().trim();
        String [] options =  {"CASH","CREDIT"};
            if(!Arrays.asList(options).contains(value))
            {
                textInputEditText.setError(message);
                hideKeyboardFrom(textInputEditText);
                return false;
            }
        return true;
    }


    /**
     * ---------------------------------------------------------------------------------------------
     * method to check InputEditText has valid email .
     * @param textInputEditText
     * @param textInputLayout
     * @param message
     * @return
     * ---------------------------------------------------------------------------------------------
     * */
    public boolean isInputEditTextEmail(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }
    public boolean isInputEditTextEmail(EditText textEditText, String message) {
        String value = textEditText.getText().toString().trim();
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            textEditText.setError(message);
            hideKeyboardFrom(textEditText);
            return false;
        }
        return true;
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * checks whether the the two input texts content match
     * @param textInputEditText1
     * @param textInputEditText2
     * @param textInputLayout
     * @param message
     * @return
     * ---------------------------------------------------------------------------------------------
     * */
    public boolean isInputEditTextMatches(TextInputEditText textInputEditText1, TextInputEditText textInputEditText2, TextInputLayout textInputLayout, String message) {
        String value1 = textInputEditText1.getText().toString().trim();
        String value2 = textInputEditText2.getText().toString().trim();
        if (!value1.contentEquals(value2)) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText2);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * Method to check the strength of the password
     * Ensures that the password includes at least 1 uppercase letter and 1 lowercase with at least
     * 1 special character and a minimum length of 6 characters
     * @param message error message
     * @param textInputEditText value
     * @param textInputLayout displays error message
     * @return boolean
     * ---------------------------------------------------------------------------------------------
     * */
    public boolean isValidPassword(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message){
        String value = textInputEditText.getText().toString();
        String password="^(?=.*[0-9]).{5,}$";
        Pattern pattern=Pattern.compile(password);
        Matcher matcher=pattern.matcher(value);
        if (value.isEmpty() || !matcher.matches()){
            textInputLayout.setError(message);
            return false;
        } else{
            textInputLayout.setErrorEnabled(false);
        }
        return true;
        //(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+,.\\\/;':"-]).{5,}
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * method to validate phone number
     * @param textInputEditText value
     * @param textInputLayout displays error message
     * @param message error message
     * @return true
     * ---------------------------------------------------------------------------------------------
     * */
    public boolean isValidPhoneNumber(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message){
        String value = textInputEditText.getText().toString();
        String password="^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$";
        Pattern pattern= Pattern.compile(password);
        Matcher matcher=pattern.matcher(value);
        if (value.isEmpty() || !matcher.matches()){
            textInputLayout.setError(message);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }
    public boolean isValidPhoneNumber(EditText textEditText, String message){
        String value = textEditText.getText().toString();
        String password="^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$";
        Pattern pattern= Pattern.compile(password);
        Matcher matcher=pattern.matcher(value);
        if (value.isEmpty() || !matcher.matches()){
            textEditText.setError(message);
            return false;
        }
        return true;
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * method to Hide keyboard
     * @param view
     * ---------------------------------------------------------------------------------------------
     **/
    public void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
