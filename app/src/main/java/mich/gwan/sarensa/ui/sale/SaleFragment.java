package mich.gwan.sarensa.ui.sale;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import mich.gwan.sarensa.adapters.SalesRecyclerAdapter;
import mich.gwan.sarensa.databinding.ActivitySaleBinding;
import mich.gwan.sarensa.helpers.InputValidation;
import mich.gwan.sarensa.helpers.RecyclerTouchListener;
import mich.gwan.sarensa.info.InformationApi;
import mich.gwan.sarensa.model.Item;
import mich.gwan.sarensa.model.Sales;
import mich.gwan.sarensa.pdf.PDFUtility;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class SaleFragment extends Fragment implements PDFUtility.OnDocumentClose {
    private RecyclerView recyclerView;
    private List<Sales> list;
    private DatabaseHelper databaseHelper;
    private SalesRecyclerAdapter recyclerAdapter;
    private InputValidation inputValidation;
    private InformationApi informationApi;
    DatePickerDialog froDatePickerDialog;
    DatePickerDialog toDatePickerDialog;
    @SuppressLint("StaticFieldLeak")
    private TextView froDatePicker;
    private TextView toDatePicker;
    private TextView textNetTotal;
    private TextView textNetProfit;
    private Spinner spinner;
    private EditText categoryEditText;
    private EditText itemEditText;
    private CardView search;
    private CardView pdf;
    private ActivitySaleBinding binding;
    private Context mContext;
    private LinearLayout lap;
    private CoordinatorLayout coordinatorLayout;
    private String filePath;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = ActivitySaleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initViews();
        initobjects();
       // buttonEvents();

        return root;
    }

    private void initViews() {

        recyclerView = binding.saleFragment.recyclerViewSales;
        toDatePicker = binding.saleFragment.toDatePicker;
        froDatePicker = binding.saleFragment.froDatePicker;
        categoryEditText = binding.saleFragment.catEditText;
        itemEditText = binding.saleFragment.itemEditText;
        pdf = binding.sellCard;
        search = binding.cardSearch;
        spinner = binding.spinner;
        coordinatorLayout = binding.saleLayout;
        textNetProfit = binding.saleFragment.profitText;
        textNetTotal = binding.saleFragment.totalText;

        lap = binding.saleFragment.linearLayout;
        froDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                froDatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if (dayOfMonth < 10) {
                            String strDate = year + "-" + (month + 1) + "-" + "0" + dayOfMonth;
                            froDatePicker.setText(strDate);
                        }
                        if (month < 9) {
                            String strDate = year + "-0" + (month + 1) + "-" + dayOfMonth;
                            froDatePicker.setText(strDate);
                        }
                        if (month < 9 && dayOfMonth < 10) {
                            String strDate = year + "-0" + (month + 1) + "-" + "0" + dayOfMonth;
                            froDatePicker.setText(strDate);
                        } else {
                            String strDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                            froDatePicker.setText(strDate);
                        }
                    }
                }, mYear, mMonth, mDay);
                froDatePickerDialog.show();
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

        toDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                toDatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if (dayOfMonth < 10) {
                            String strDate = year + "-" + (month + 1) + "-" + "0" + dayOfMonth;
                            toDatePicker.setText(strDate);
                        }
                        if (month < 9) {
                            String strDate = year + "-0" + (month + 1) + "-" + dayOfMonth;
                            toDatePicker.setText(strDate);
                        }
                        if (month < 9 && dayOfMonth < 10) {
                            String strDate = year + "-0" + (month + 1) + "-" + "0" + dayOfMonth;
                            toDatePicker.setText(strDate);
                        } else {
                            String strDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                            toDatePicker.setText(strDate);
                        }
                    }
                }, mYear, mMonth, mDay);
                toDatePickerDialog.show();
            }
        });

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePath = path();
                // Make sure the path directory exists.
                try {
                    PDFUtility.createPdf(v.getContext(), SaleFragment.this, station(),
                            getSampleData(), path(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                    //Log.e(TAG,"Error Creating Pdf");
                    Toast.makeText(mContext, "Error Creating Pdf", Toast.LENGTH_SHORT).show();
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonEvents();
            }
        });
    }

    @Override
    public void onPDFDocumentClose(File file)
    {
        Toast.makeText(mContext,"Data processed successfully",Toast.LENGTH_SHORT).show();
        // Get the File location and file name.
        Log.d("pdfFIle", "" + filePath);

        // Get the URI Path of file.
        Uri uriPdfPath = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", new File(filePath));
        Log.d("pdfPath", "" + uriPdfPath);

        // Start Intent to View PDF from the Installed Applications.
        Intent pdfOpenIntent = new Intent(Intent.ACTION_VIEW);
        pdfOpenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenIntent.setClipData(ClipData.newRawUri("", uriPdfPath));
        pdfOpenIntent.setDataAndType(uriPdfPath, "application/pdf");
        pdfOpenIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |  Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        try {
            startActivity(pdfOpenIntent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            Toast.makeText(mContext,"There is no app to load corresponding PDF",Toast.LENGTH_LONG).show();

        }
    }
    private String station(){
        String station = null;
        if(spinner.getSelectedItem() == "Select Station" ) {
            station = "ALL STATIONS";
        } else {
            station = (String) spinner.getSelectedItem();
        }
        return station;
    }
    private String path(){
        String path = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +
                    "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+"_Sales.pdf";
        }
        else
        {
            path = Environment.getExternalStorageDirectory() + "/Sarensa/"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))+"_Sales.pdf";
        }
        return path;
    }

        private List<String[]> getSampleData(){
            int count = list.size();

            List<String[]> temp = new ArrayList<>();
            for (int i = 0; i < count; i++)
            {
                Sales sales = list.get(i);
                temp.add(new String[] {sales.getDate(), sales.getItemName(),
                        String.valueOf(sales.getItemQnty()), String.valueOf(sales.getSellPrice()) + ".00",
                        String.valueOf(sales.getTotal()) + ".00", String.valueOf(sales.getProfit()) + ".00"});
            }
            temp.add(new String[] {"TOTAL", "", "", "", String.valueOf(tot() + ".00"), String.valueOf(prof() + ".00")});
            return  temp;
        }

    private void initobjects() {
        list = new ArrayList<>();
        recyclerAdapter = new SalesRecyclerAdapter(list);
        inputValidation = new InputValidation(mContext);
        informationApi = new InformationApi();

        RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);
        databaseHelper = new DatabaseHelper(mContext);

        List<String> categories = databaseHelper.getStation();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        getDataFromSQLite();
    }

    /**
     * Delete the item from SQLite and remove
     * it from the list
     **/
    private void deleteItem(int position) {
    // deleting the note from db
    databaseHelper.deleteSale(list.get(position));
    Item par = new Item();
    par.setCategoryName(list.get(position).getItemCategory());
    par.setItemName(list.get(position).getItemName());
    par.setStationName(list.get(position).getStationName());
    par.setItemQnty(list.get(position).getItemQnty());

    databaseHelper.updateItemQnty(par);

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
        CharSequence colors[] = new CharSequence[]{"Delete"};

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getView().getContext());
        builder.setTitle("Delete Selected Transaction");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    deleteItem(position);
                    informationApi.snackBar(coordinatorLayout, "Transaction Deleted Successfully!", Color.WHITE);

                }
            }
        });
        builder.show();
    }

    // sum total
    public int tot() {
        int total = 0;
        for(int i = 0; i <list.size(); i++) {
            total += list.get(i).getTotal();
        }
        return total;
    }
    // sum profit
    public int prof() {
        int profit = 0;
        for(int i = 0; i <list.size(); i++) {
            profit += list.get(i).getProfit();
        }
        return profit;
    }

    @SuppressLint("StaticFieldLeak")
    private void buttonEvents(){
        //search.setOnClickListener(new View.OnClickListener() {
            //@SuppressLint("StaticFieldLeak")
           // @Override
           // public void onClick(View v) {}
       // });
            if(Objects.equals(categoryEditText.getText().toString(), "") &&
                    Objects.equals(itemEditText.getText().toString(), "") &&
                    Objects.equals(spinner.getSelectedItem().toString(), "Select Station")) {
                if (froDatePicker.getText() == "" && toDatePicker.getText() == ""){
                    //textNetProfit.setText("mommy");
                    new AsyncTask<Void, Void, Void>() {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                            list.clear();
                            list.addAll(databaseHelper.getAllSales());
                            return null;
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            recyclerAdapter.notifyDataSetChanged();
                            textNetProfit.setText(String.valueOf(prof()));
                            textNetTotal.setText(String.valueOf(tot()));
                        }
                    }.execute();
                }
                else if(froDatePicker.getText() != "" && toDatePicker.getText() == "") {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                            list.clear();
                            list.addAll(databaseHelper.getAllSales((String) froDatePicker.getText()));
                            return null;
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            recyclerAdapter.notifyDataSetChanged();
                            textNetProfit.setText(String.valueOf(prof()));
                            textNetTotal.setText(String.valueOf(tot()));
                        }
                    }.execute();
                }
                else if(froDatePicker.getText() != "" && toDatePicker.getText() != "") {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                            list.clear();
                            list.addAll(databaseHelper.getAllSalesBetween((String) froDatePicker.getText(), (String) toDatePicker.getText()));
                            toDatePicker.setText("");
                            return null;
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            recyclerAdapter.notifyDataSetChanged();
                            textNetProfit.setText(String.valueOf(prof()));
                            textNetTotal.setText(String.valueOf(tot()));
                        }
                    }.execute();
                }

            }
            else if (Objects.equals(categoryEditText.getText().toString(), "") &&
                    Objects.equals(itemEditText.getText().toString(), "") &&
                    !Objects.equals(spinner.getSelectedItem().toString(), "Select Station")) {
                if(froDatePicker.getText() != "" && toDatePicker.getText() != "" ) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                            list.clear();
                            list.addAll(databaseHelper.getAllSalesBetween((String) froDatePicker.getText(),
                                    (String) toDatePicker.getText(), spinner.getSelectedItem().toString().toUpperCase()));
                            toDatePicker.setText("");
                            return null;
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            recyclerAdapter.notifyDataSetChanged();
                            textNetProfit.setText(String.valueOf(prof()));
                            textNetTotal.setText(String.valueOf(tot()));
                        }
                    }.execute();
                }
                else if(froDatePicker.getText() != "" && toDatePicker.getText() == "" ) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                            list.clear();
                            list.addAll(databaseHelper.getAllSales((String) froDatePicker.getText(),
                                    spinner.getSelectedItem().toString().toUpperCase()));
                            toDatePicker.setText("");
                            return null;
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            recyclerAdapter.notifyDataSetChanged();
                            textNetProfit.setText(String.valueOf(prof()));
                            textNetTotal.setText(String.valueOf(tot()));
                        }
                    }.execute();
                }

            }
            else if (!Objects.equals(categoryEditText.getText().toString(), "") &&
                    Objects.equals(itemEditText.getText().toString(), "") &&
                    !Objects.equals(spinner.getSelectedItem().toString(), "Select Station")){
                if(froDatePicker.getText() != "" && toDatePicker.getText() != "" ) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                            list.clear();
                            list.addAll(databaseHelper.getAllSalesBetweenCat((String) froDatePicker.getText(),
                                    (String) toDatePicker.getText(), spinner.getSelectedItem().toString().toUpperCase(),
                                    categoryEditText.getText().toString().toUpperCase()));
                            toDatePicker.setText("");
                            return null;
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            recyclerAdapter.notifyDataSetChanged();
                            textNetProfit.setText(String.valueOf(prof()));
                            textNetTotal.setText(String.valueOf(tot()));
                        }
                    }.execute();
                }
                else if(froDatePicker.getText() != "" && toDatePicker.getText() == "" ) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                            list.clear();
                            list.addAll(databaseHelper.getAllSale((String) froDatePicker.getText(),
                                    spinner.getSelectedItem().toString().toUpperCase(),
                                    categoryEditText.getText().toString().toUpperCase()));
                            toDatePicker.setText("");
                            return null;
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            recyclerAdapter.notifyDataSetChanged();
                            textNetProfit.setText(String.valueOf(prof()));
                            textNetTotal.setText(String.valueOf(tot()));
                        }
                    }.execute();
                }

            }
            else if(Objects.equals(categoryEditText.getText().toString(), "") &&
                    !Objects.equals(itemEditText.getText().toString(), "") &&
                    !Objects.equals(spinner.getSelectedItem().toString(), "Select Station")){
                if(froDatePicker.getText() != "" && toDatePicker.getText() != "" ) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                            list.clear();
                            list.addAll(databaseHelper.getAllSalesBetween((String) froDatePicker.getText(),
                                    (String) toDatePicker.getText(), spinner.getSelectedItem().toString().toUpperCase(),
                                    itemEditText.getText().toString().toUpperCase()));
                            toDatePicker.setText("");
                            return null;
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            recyclerAdapter.notifyDataSetChanged();
                            textNetProfit.setText(String.valueOf(prof()));
                            textNetTotal.setText(String.valueOf(tot()));
                        }
                    }.execute();
                }
                else if(froDatePicker.getText() != "" && toDatePicker.getText() == "" ) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                            list.clear();
                            list.addAll(databaseHelper.getAllSaleItem((String) froDatePicker.getText(),
                                    spinner.getSelectedItem().toString().toUpperCase(),
                                    itemEditText.getText().toString().toUpperCase()));
                            toDatePicker.setText("");
                            return null;
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            recyclerAdapter.notifyDataSetChanged();
                            textNetProfit.setText(String.valueOf(prof()));
                            textNetTotal.setText(String.valueOf(tot()));
                        }
                    }.execute();
                }

            }
            else if (!Objects.equals(categoryEditText.getText().toString(), "") &&
                    !Objects.equals(itemEditText.getText().toString(), "") &&
                    !Objects.equals(spinner.getSelectedItem().toString(), "Select Station")){
                if(froDatePicker.getText() != "" && toDatePicker.getText() != "" ) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                            list.clear();
                            list.addAll(databaseHelper.getAllSalesBetween((String) froDatePicker.getText(),
                                    (String) toDatePicker.getText(), spinner.getSelectedItem().toString().toUpperCase(),
                                    categoryEditText.getText().toString().toUpperCase(),itemEditText.getText().toString().toUpperCase()));
                            toDatePicker.setText("");
                            return null;
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            recyclerAdapter.notifyDataSetChanged();
                            textNetProfit.setText(String.valueOf(prof()));
                            textNetTotal.setText(String.valueOf(tot()));
                        }
                    }.execute();
                }
                else if(froDatePicker.getText() != "" && toDatePicker.getText() == "" ) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                            list.clear();
                            list.addAll(databaseHelper.getAllSaleItem((String) froDatePicker.getText(),
                                    spinner.getSelectedItem().toString().toUpperCase(), categoryEditText.getText().toString().toUpperCase(),
                                    itemEditText.getText().toString().toUpperCase()));
                            toDatePicker.setText("");
                            return null;
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            recyclerAdapter.notifyDataSetChanged();
                            textNetProfit.setText(String.valueOf(prof()));
                            textNetTotal.setText(String.valueOf(tot()));
                        }
                    }.execute();
                }


            }
            else if (!Objects.equals(categoryEditText.getText().toString(), "") &&
                    Objects.equals(itemEditText.getText().toString(), "") &&
                    Objects.equals(spinner.getSelectedItem().toString(), "Select Station")){
                if(froDatePicker.getText() != "" && toDatePicker.getText() != "" ) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                            list.clear();
                            list.addAll(databaseHelper.getAllSalesBtwnCat((String) froDatePicker.getText(),
                                    (String) toDatePicker.getText(), categoryEditText.getText().toString().toUpperCase()));
                            toDatePicker.setText("");
                            return null;
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            recyclerAdapter.notifyDataSetChanged();
                            textNetProfit.setText(String.valueOf(prof()));
                            textNetTotal.setText(String.valueOf(tot()));
                        }
                    }.execute();
                }
                else if(froDatePicker.getText() != "" && toDatePicker.getText() == "" ) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                            list.clear();
                            list.addAll(databaseHelper.getAllSalesCat((String) froDatePicker.getText(),
                                    categoryEditText.getText().toString().toUpperCase()));
                            toDatePicker.setText("");
                            return null;
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            recyclerAdapter.notifyDataSetChanged();
                            textNetProfit.setText(String.valueOf(prof()));
                            textNetTotal.setText(String.valueOf(tot()));
                        }
                    }.execute();
                }

            }
        }

    @SuppressLint("StaticFieldLeak")
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                list.clear();
                list.addAll(databaseHelper.getAllSales());
                return null;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                recyclerAdapter.notifyDataSetChanged();
                textNetProfit.setText(String.valueOf(prof()));
                textNetTotal.setText(String.valueOf(tot()));
            }
        }.execute();
    }

    public void actionSearch(View view) {
        buttonEvents();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}