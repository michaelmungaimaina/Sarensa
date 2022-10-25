package mich.gwan.sarensa.ui.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.adapters.SalesRecyclerAdapter;
import mich.gwan.sarensa.adapters.UserRecyclerAdapter;
import mich.gwan.sarensa.databinding.FragmentUserBinding;
import mich.gwan.sarensa.databinding.UserFragmentBinding;
import mich.gwan.sarensa.helpers.InputValidation;
import mich.gwan.sarensa.helpers.RecyclerTouchListener;
import mich.gwan.sarensa.info.InformationApi;
import mich.gwan.sarensa.model.Item;
import mich.gwan.sarensa.model.Sales;
import mich.gwan.sarensa.model.Station;
import mich.gwan.sarensa.model.User;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class UserFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<User> list;
    private DatabaseHelper databaseHelper;
    private UserRecyclerAdapter recyclerAdapter;
    private InputValidation inputValidation;
    private InformationApi informationApi;
    private UserFragmentBinding binding;
    private Context mContext;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton addFab;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = UserFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initViews();
        initobjects();
        return root;
    }

    private void initViews() {
        recyclerView = binding.userView.recyclerViewUsers;
        addFab = binding.userRegister;
        coordinatorLayout = binding.userLayout;

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         **/
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false, null, -1);
            }
        });
    }

    private void initobjects() {
        list = new ArrayList<>();
        recyclerAdapter = new UserRecyclerAdapter(list);
        inputValidation = new InputValidation(mContext);
        informationApi = new InformationApi();

        RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);
        databaseHelper = new DatabaseHelper(mContext);

        getDataFromSQLite();
    }

    /**
     * Inserting new value in db
     * and refreshing the list
     */
    @SuppressLint("NotifyDataSetChanged")
    private void insertValue(String name, String email,String usertype, String phone, String password) {
        // inserting value in db
        User par = new User();
        par.setUserName(name);
        par.setUserEmail(email);
        par.setUserPhone(phone);
        par.setUserType(usertype);
        par.setUserPassword(password);
        databaseHelper.addUser(par);

        // adding new values to array list at 0 position
        list.add(0, par);
        // refreshing the list
        recyclerAdapter.notifyDataSetChanged();
    }

    /**
     * Updating values in db and updating
     * item in the list by its position
     */
    private void updateValue(String name, String email,String usertype, String phone, String password, int position) {
        User par = list.get(position);
        // updating values
        par.setUserName(name);
        par.setUserEmail(email);
        par.setUserType(usertype);
        par.setUserPhone(phone);
        par.setUserPassword(password);
        // updating values in db
        databaseHelper.updateUser(par);
        // refreshing the list
        list.set(position, par);
        recyclerAdapter.notifyItemChanged(position);
    }

    /**
     * Delete the item from SQLite and remove
     * it from the list
     **/
    private void deleteItem(int position) {
        // deleting the note from db
        databaseHelper.deleteUser(list.get(position));

        // removing the note from the list
        list.remove(position);
        recyclerAdapter.notifyItemRemoved(position);
    }
    /**
     * Opens dialog with edit - Delete options
     * Delete - 0
     * Edit - 0
     **/
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit","Delete"};

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getView().getContext());
        builder.setTitle("Delete Selected Transaction");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, list.get(position), position);
                } else{
                    deleteItem(position);
                    informationApi.snackBar(coordinatorLayout, "Transaction Deleted Successfully!", Color.WHITE);
                }
            }
        });
        builder.show();
    }

    /**
     * Shows alert dialog with EditText options to enter / edit
     * a user.
     * when shouldUpdate=true, it automatically displays user and changes the
     * button text to UPDATE
     */
    private void showNoteDialog(final boolean shouldUpdate, final User user, final int position){
        try {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mContext.getApplicationContext());
            View view = layoutInflaterAndroid.inflate(R.layout.user_dialog, null);

            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(mContext);
            alertDialogBuilderUserInput.setView(view);

            final EditText name = view.findViewById(R.id.editTextName);
            final EditText email = view.findViewById(R.id.editTextEmail);
            final EditText phone = view.findViewById(R.id.editTextPhone);
            final EditText password = view.findViewById(R.id.editTextPassword);
            final EditText spinner = view.findViewById(R.id.spinner);
            //final TextInputLayout spinnerLayout = view.findViewById(R.id.textInputLayoutSpinner);
            //final TextInputLayout stationLocationLayout = view.findViewById(R.id.textInputLayoutStationLocation);
            TextView dialogTitle = view.findViewById(R.id.title);

            dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_register_user) : getString(R.string.lbl_edit_user));

            if (shouldUpdate && user != null) {
                // display text to views
                name.setText(user.getUserName());
                email.setText(user.getUserEmail());
                spinner.setText(user.getUserType());
                phone.setText(String.valueOf(user.getUserPhone()));
                password.setText(String.valueOf(user.getUserPassword()));


            }
            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton(shouldUpdate ? "UPDATE" : "SAVE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {

                        }
                    })
                    .setNegativeButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                }
                            });

            final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
            alertDialog.show();

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show error message when no text is entered
                    if (!inputValidation.isEditTextOccupied(name, getString(R.string.error_message_user_name))) {
                        name.requestFocus();
                        return;
                    }
                    if (!inputValidation.isEditTextOccupied(spinner, getString(R.string.error_spinner))){
                        spinner.requestFocus();
                        return;
                    }
                    if (!inputValidation.isEditTextMatch(spinner, getString(R.string.error_spinner))){
                        spinner.requestFocus();
                        return;
                    }
                    if (!inputValidation.isEditTextOccupied(email, getString(R.string.error_mssage_email))) {
                        email.requestFocus();
                        return;
                    }
                    if (!inputValidation.isInputEditTextEmail(email, getString(R.string.error_message_email))) {
                        email.requestFocus();
                        return;
                    }
                    if (!inputValidation.isEditTextOccupied(phone, getString(R.string.error_message_phone))) {
                        phone.requestFocus();
                        return;
                    }
                    if (!inputValidation.isValidPhoneNumber(phone, getString(R.string.error_mssage_phone))) {
                        phone.requestFocus();
                        return;
                    }
                    if (!inputValidation.isEditTextOccupied(password, getString(R.string.error_message_password))) {
                        password.requestFocus();
                        return;
                    }
                    else {
                        alertDialog.dismiss();
                    }

                    // check if user is updating values
                    if (shouldUpdate && user != null) {
                        // update values by it's position
                        updateValue(name.getText().toString().toUpperCase(),
                                email.getText().toString(),(String) spinner.getText().toString().toUpperCase(),
                                phone.getText().toString().toUpperCase(),password.getText().toString(), position);
                    } else {
                        // create new user
                        insertValue(name.getText().toString().toUpperCase(),
                                email.getText().toString(),(String) spinner.getText().toString().toUpperCase(),
                                phone.getText().toString().toUpperCase(),password.getText().toString());
                    }
                }
            });
        } catch (InflateException e){
            e.getCause().printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @SuppressLint("StaticFieldLeak")
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                list.clear();
                list.addAll(databaseHelper.getAllUser());
                return null;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                recyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}