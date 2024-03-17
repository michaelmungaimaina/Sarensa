package mich.gwan.sarensa.ui.home;

import static java.time.ZonedDateTime.now;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.adapters.StationRecyclerAdapter;
import mich.gwan.sarensa.databinding.FragmentHomeBinding;
import mich.gwan.sarensa.helpers.InputValidation;
import mich.gwan.sarensa.helpers.RecyclerTouchListener;
import mich.gwan.sarensa.model.Station;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class HomeFragment extends Fragment{
    private RecyclerView recyclerView;
    private List<Station> list;
    private DatabaseHelper databaseHelper;
    private InputValidation inputValidation;
    private StationRecyclerAdapter recyclerAdapter;
    private FragmentHomeBinding binding;
    private FloatingActionButton register;
    private CoordinatorLayout coordinatorLayout;
    private Context mContext;
    private TextView textView;
    private TextView title;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) throws InflateException{

            binding = FragmentHomeBinding.inflate(inflater, container, false);
            initViews();
            initobjects();
            toggleEmptyList();
        return binding.getRoot();
    }

    private  void initViews(){
        recyclerView = binding.homeView.recyclerStation;
        register = binding.register;
        coordinatorLayout = binding.homeLayout;
        textView = binding.homeView.emptyStation;

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws InflateException{
                showNoteDialog(false, null, -1);
            }
        });

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
    }

    private void initobjects() {
        list = new ArrayList<>();
        recyclerAdapter = new StationRecyclerAdapter(list);

        RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);
        databaseHelper = new DatabaseHelper(mContext);
        inputValidation = new InputValidation(mContext);
        getDataFromSQLite();
    }


    /**
     * Inserting new value in db
     * and refreshing the list
     */
    @SuppressLint("NotifyDataSetChanged")
    private void insertValue(String station, String location) {
        // inserting value in db
        Station par = new Station();
        par.setName(station);
        par.setLocation(location);
        par.setStationIdentifier(station);
        databaseHelper.addStation(par);

        // adding new values to array list at 0 position
        list.add(0, par);
        // refreshing the list
        recyclerAdapter.notifyDataSetChanged();
        toggleEmptyList();
    }

    /**
     * Updating values in db and updating
     * item in the list by its position
     */
    private void updateValue(String station, String location, int position) {
        Station par = list.get(position);
        // updating values
        par.setName(station);
        par.setLocation(location);
        // updating values in db
        databaseHelper.updateStation(par);
        // refreshing the list
        list.set(position, par);
        recyclerAdapter.notifyItemChanged(position);

        toggleEmptyList();
    }

    /**
     * Deleting values from SQLite and removing the
     * item from the list by its position
     */
    private void deleteValue(int position) {
        // deleting the value from db
        databaseHelper.deleteStation(list.get(position));
        // removing value from the list
        list.remove(position);
        recyclerAdapter.notifyItemRemoved(position);

        toggleEmptyList();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence[] colors = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Choose Action");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, list.get(position), position);
                } else {
                    deleteValue(position);
                }
            }
        });
        builder.show();
    }


    /**
     * Shows alert dialog with EditText options to enter / edit
     * a note.
     * when shouldUpdate=true, it automatically displays old note and changes the
     * button text to UPDATE
     */
    private void showNoteDialog(final boolean shouldUpdate, final Station station, final int position){
        try {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mContext.getApplicationContext());
            View view = layoutInflaterAndroid.inflate(R.layout.station_dialog, null);

            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(mContext);
            alertDialogBuilderUserInput.setView(view);

            final EditText stationName = view.findViewById(R.id.textInputEditTextStationName);
            final EditText stationLocation = view.findViewById(R.id.textInputEditTextStationLocation);
            //final TextInputLayout stationNameLayout = view.findViewById(R.id.textInputLayoutStationName);
            //final TextInputLayout stationLocationLayout = view.findViewById(R.id.textInputLayoutStationLocation);
            TextView dialogTitle = view.findViewById(R.id.title);
            dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_register) : getString(R.string.lbl_edit));

            if (shouldUpdate && station != null) {
                // display text to views
                stationName.setText(station.getName());
                stationLocation.setText(station.getLocation());
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
                    if (!inputValidation.isEditTextOccupied(stationName, getString(R.string.error_message_station))) {
                        stationName.requestFocus();
                        return;
                    }
                    if (!inputValidation.isEditTextOccupied(stationLocation, getString(R.string.error_message_location))) {
                        stationLocation.requestFocus();
                        return;
                    }

                    // check if user is updating values
                    if (shouldUpdate && station != null) {
                        // update values by it's position
                        updateValue(stationName.getText().toString().toUpperCase(),
                                stationLocation.getText().toString().toUpperCase(), position);
                        Toast.makeText(mContext,getString(R.string.station_update),Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    } else {
                        if (databaseHelper.checkStation(stationName.getText().toString().toUpperCase())){
                            Toast.makeText(mContext,getString(R.string.station_exists),Toast.LENGTH_SHORT).show();
                        } else {
                            // create new note
                            insertValue(stationName.getText().toString().toUpperCase(), stationLocation.getText().toString().toUpperCase());
                            Toast.makeText(mContext,getString(R.string.station_registered),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } catch (InflateException e){
            e.getCause().printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Toggling list and empty notes view
     */
    private void toggleEmptyList() {
        // check list.size() > 0
        if (databaseHelper.getStationCount() > 0) {
            textView.setVisibility(View.GONE);
        }
        else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void getDataFromSQLite(){
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                list.clear();
                list.addAll(databaseHelper.getAllStation());
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