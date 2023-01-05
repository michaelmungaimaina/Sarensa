package mich.gwan.sarensa.ui.receipt;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
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

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.activities.SaleCategoryViewActivity;
import mich.gwan.sarensa.adapters.ReceiptRecyclerAdapter;
import mich.gwan.sarensa.databinding.ActivityReceiptBinding;
import mich.gwan.sarensa.helpers.RecyclerTouchListener;
import mich.gwan.sarensa.info.InformationApi;
import mich.gwan.sarensa.model.Cart;
import mich.gwan.sarensa.model.Receipt;
import mich.gwan.sarensa.pdf.ReceiptPDF;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class ReceiptFragment extends Fragment implements ReceiptPDF.OnDocumentClose {
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private ActivityReceiptBinding binding;
    private ReceiptRecyclerAdapter recyclerAdapter;
    private InformationApi informationApi;

    private List<Receipt> list;

    DatePickerDialog froDatePickerDialog;
    DatePickerDialog toDatePickerDialog;

    private TextView froDatePicker;
    private TextView toDatePicker;

    private Spinner spinner;
    private Context mContext;
    private EditText customerEditText;
    private CardView search;
    private CardView pdf;
    private CoordinatorLayout coordinatorLayout;
    private String filePath;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = ActivityReceiptBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initViews();
        buttonEvents();
        initobjects();
        return root;
    }
    private void initViews() {
        recyclerView = binding.receiptFragment.recyclerViewReceipts;
        toDatePicker = binding.receiptFragment.toDatePicker;
        froDatePicker = binding.receiptFragment.froDatePicker;
        customerEditText = binding.receiptFragment.editTextCustomer;
        pdf = binding.sellCard;
        search = binding.cardSearch;
        spinner = binding.spinner;
        coordinatorLayout = binding.receiptLayout;
    }

    private void initobjects() {
        list = new ArrayList<>();
        recyclerAdapter = new ReceiptRecyclerAdapter(list);
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

    private void buttonEvents() {
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
                        if (dayOfMonth < 10 && month < 9 ){
                            String strDate = year + "-0" + (month + 1) + "-0" + dayOfMonth;
                            froDatePicker.setText(strDate);
                        }
                        if (dayOfMonth < 10 && month >= 9){
                            String strDate = year + "-" + (month + 1) + "-0" + dayOfMonth;
                            froDatePicker.setText(strDate);
                        }
                        if (dayOfMonth >= 10 && month < 9){
                            String strDate = year + "-0" + (month + 1) + "-" + dayOfMonth;
                            froDatePicker.setText(strDate);
                        }
                        if (dayOfMonth >= 10 && month >= 9){
                            String strDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                            froDatePicker.setText(strDate);
                        }
                    }
                }, mYear, mMonth, mDay);
                froDatePickerDialog.show();
            }
        });

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
                        if (dayOfMonth < 10 && month < 9 ){
                            String strDate = year + "-0" + (month + 1) + "-0" + dayOfMonth;
                            toDatePicker.setText(strDate);
                        }
                        if (dayOfMonth < 10 && month >= 9){
                            String strDate = year + "-" + (month + 1) + "-0" + dayOfMonth;
                            toDatePicker.setText(strDate);
                        }
                        if (dayOfMonth >= 10 && month < 9){
                            String strDate = year + "-0" + (month + 1) + "-" + dayOfMonth;
                            toDatePicker.setText(strDate);
                        }
                        if (dayOfMonth >= 10 && month >= 9){
                            String strDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                            toDatePicker.setText(strDate);
                        }
                    }
                }, mYear, mMonth, mDay);
                toDatePickerDialog.show();
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
            }
        }));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchAction();
            }
        });
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateReceipt(view);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void searchAction() {
        if(!Objects.equals(froDatePicker.getText().toString(), "") ) {
            if (Objects.equals(customerEditText.getText().toString(), "") &&
                    Objects.equals(spinner.getSelectedItem().toString(), "Select Station")){
                new AsyncTask<Void, Void, Void>() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                        list.clear();
                        list.addAll(databaseHelper.getAllReceipts(froDatePicker.getText().toString()));
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
            else if(!Objects.equals(customerEditText.getText().toString(), "") &&
                    Objects.equals(spinner.getSelectedItem().toString(), "Select Station")) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                        list.clear();
                        list.addAll(databaseHelper.getReceipts((String) froDatePicker.getText(), customerEditText.getText().toString().toUpperCase()));
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
            else if(Objects.equals(customerEditText.getText().toString(), "") &&
                    !Objects.equals(spinner.getSelectedItem().toString(), "Select Station")) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                        list.clear();
                        list.addAll(databaseHelper.getAllReceipts((String) froDatePicker.getText(), spinner.getSelectedItem().toString().toUpperCase()));
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
            else if(!Objects.equals(customerEditText.getText().toString(), "") &&
                    !Objects.equals(spinner.getSelectedItem().toString(), "Select Station")) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                        list.clear();
                        list.addAll(databaseHelper.getAllReceipts((String) froDatePicker.getText(),
                                spinner.getSelectedItem().toString().toUpperCase(),customerEditText.getText().toString().toUpperCase()));
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

        } else if(Objects.equals(froDatePicker.getText().toString(), "")){
            informationApi.snackBar(coordinatorLayout, getString(R.string.error_no_date), Color.RED);
        }

    }

    @SuppressLint("StaticFieldLeak")
    public void getDataFromSQLite(){
        new AsyncTask<Void, Void, Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                list.clear();
                list.addAll(databaseHelper.getAllReceipts());
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
    public void onPDFDocumentClose(File file) {
        Toast.makeText(mContext, "Receipt processed successfully", Toast.LENGTH_SHORT).show();
        // Get the File location and file name.
        Log.d("pdfFIle", "" + filePath);

        // Get the URI Path of file.
        Uri uriPdfPath = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            uriPdfPath = FileProvider.getUriForFile(mContext, mContext.getOpPackageName() + ".provider", new File(filePath));
        }
        Log.d("pdfPath", "" + uriPdfPath);

        // Start Intent to View PDF from the Installed Applications.
        Intent pdfOpenIntent = new Intent(Intent.ACTION_VIEW);
        pdfOpenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenIntent.setClipData(ClipData.newRawUri("", uriPdfPath));
        pdfOpenIntent.setDataAndType(uriPdfPath, "application/pdf");
        pdfOpenIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        try {
            startActivity(pdfOpenIntent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            Toast.makeText(mContext, "There is no app to load corresponding PDF", Toast.LENGTH_LONG).show();

        }
    }

    private String path(){
        String path = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +
                    "/000" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddHHmm"))+".pdf";
        }
        else
        {
            path = Environment.getExternalStorageDirectory() + "/Sarensa/Receipts/"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))+"_Sales.pdf";
        }
        return path;
    }

    private List<String[]> getSampleData(){
        int count = list.size();

        List<String[]> temp = new ArrayList<>();
        for (int i = 0; i < count; i++)
        {
            int total = list.get(i).getSellingPrice() * list.get(i).getItemQuantity();
            Receipt sales = list.get(i);
            temp.add(new String[] {sales.getItemName(),
                    String.valueOf(sales.getItemQuantity()), String.valueOf(sales.getSellingPrice()) + ".00",
                    String.valueOf(total) + ".00"});
        }
        temp.add(new String[] {"TOTAL", "", "", String.valueOf(tot() + ".00")});
        return  temp;
    }

    String station = null;
    public int tot() {
        int total = 0;
        int netTotal = 0;
        for(int i = 0; i <list.size(); i++) {
            station = list.get(i).getStationName();
            netTotal = list.get(i).getSellingPrice() * list.get(i).getItemQuantity();
            total += netTotal;
        }
        return total;
    }

    public void generateReceipt(View view) {
        if(Objects.equals(froDatePicker.getText().toString(), "") && Objects.equals(customerEditText.getText().toString(), "")){
            informationApi.snackBar(coordinatorLayout, getString(R.string.error_no_date), Color.RED);
        } else{
            int receiptNO = Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddHHmm")));
            Toast.makeText(mContext, "Transaction Completed Successfully!", Toast.LENGTH_SHORT).show();
            filePath = path();
            // Make sure the path directory exists.
            try {
                ReceiptPDF.createPdf(mContext.getApplicationContext(), ReceiptFragment.this,station,receiptNO,
                        getSampleData(), path(), true);
            } catch (Exception e) {
                e.printStackTrace();
                //Log.e(TAG,"Error Creating Pdf");
                Toast.makeText(mContext, "Error Creating Pdf", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
