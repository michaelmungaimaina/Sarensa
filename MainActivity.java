package mich.gwan.sarensa.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.InflateException;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.databinding.ActivityMainBinding;
import mich.gwan.sarensa.databinding.AppBarMainBinding;
import mich.gwan.sarensa.databinding.BottomDialogBinding;
import mich.gwan.sarensa.helpers.InputValidation;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DatabaseHelper db;
    private InputValidation inputValidation;

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String PASSWORD_KEY = "password_key";
    public static final String USER_TYPE_KEY = "user_type_key";
    SharedPreferences sharedPreferences;
    String email, userType, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            setSupportActionBar(binding.mainAppBar.toolbar);

            db = new DatabaseHelper(this);
            inputValidation = new InputValidation(this);
            // initialize sharedprefs
            sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            email = sharedPreferences.getString(EMAIL_KEY,null);
            userType = sharedPreferences.getString(USER_TYPE_KEY,null);
            password = sharedPreferences.getString(PASSWORD_KEY, null);

            DrawerLayout drawer = binding.drawerLayout;
            NavigationView navigationView = binding.navView;
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_sales, R.id.nav_stock,R.id.nav_receipt, R.id.nav_users)
                    .setOpenableLayout(drawer)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        } catch (InflateException e){
            Objects.requireNonNull(e.getCause()).printStackTrace();
        } catch (Exception e) {

            // generic exception handling
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void logout(MenuItem item) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // clear data in shared prefs
        editor.clear();
        // apply changes
        editor.apply();
        // finish this activity
        Intent intent;
        intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    public void setTrigger(MenuItem item) {
        showBottomSheet();
    }

    private void showBottomSheet() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this,R.style.BottomSheetDialog);
        BottomDialogBinding dialogBinding = BottomDialogBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(dialogBinding.getRoot());

        final EditText firstName = dialogBinding.textInputEditTextTime;
        final CardView cancel = dialogBinding.cardViewCancel;
        final CardView submit = dialogBinding.cardViewSubmit;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inputValidation.isEditTextOccupied(firstName,"Null")){
                    firstName.requestFocus();
                    return;
                }else {
                    db.createReceiptDeleteTrigger(Integer.parseInt(firstName.getText().toString()));
                    db.createSalesDeleteTrigger(Integer.parseInt(firstName.getText().toString()));
                    Toast.makeText(getApplicationContext(),"Trigger set successfully",Toast.LENGTH_LONG).show();
                    bottomSheetDialog.cancel();

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
            }
        });

        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }


    private Boolean exit = false;

    /**
     * method for exiting the app
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
}