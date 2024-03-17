package mich.gwan.sarensa.ui.sale;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.summingInt;

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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.adapters.SalesRecyclerAdapter;
import mich.gwan.sarensa.databinding.ActivitySaleBinding;
import mich.gwan.sarensa.helpers.InputValidation;
import mich.gwan.sarensa.helpers.RecyclerTouchListener;
import mich.gwan.sarensa.info.InformationApi;
import mich.gwan.sarensa.model.Item;
import mich.gwan.sarensa.model.Receipt;
import mich.gwan.sarensa.model.Sales;
import mich.gwan.sarensa.pdf.PDFUtility;
import mich.gwan.sarensa.pdf.ProfitPDF;
import mich.gwan.sarensa.sql.DatabaseHelper;
import mich.gwan.sarensa.ui.home.HomeFragment;

public class SaleFragment extends Fragment implements PDFUtility.OnDocumentClose, ProfitPDF.OnDocClose {
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
    private CardView cardProfit;
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
        //buttonEvents();
        return root;
    }

    private void initViews() {

        recyclerView = binding.saleFragment.recyclerViewSales;
        toDatePicker = binding.saleFragment.toDatePicker;
        froDatePicker = binding.saleFragment.froDatePicker;
        categoryEditText = binding.saleFragment.editTextCustomer;
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

                froDatePickerDialog = new DatePickerDialog(mContext, R.style.CustomDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
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
                froDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
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

                toDatePickerDialog = new DatePickerDialog(mContext, R.style.CustomDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
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
                toDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                toDatePickerDialog.show();
            }
        });

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseRepoertDialog();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonEvents();
            }
        });
    }

    private void ChooseRepoertDialog() {
        CharSequence colors[] = new CharSequence[]{"POS","PROFIT"};

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getView().getContext());
        builder.setTitle("Choose Action");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    filePath = path();
                    // Make sure the path directory exists.
                    try {
                        PDFUtility.createPdf(mContext, SaleFragment.this, station(),
                                getSampleData(), path(), true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Log.e(TAG,"Error Creating Pdf");
                        Toast.makeText(mContext, "Error Creating Pdf", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    filePath = profitPath();
                    // Make sure the path directory exists.
                    try {
                        ProfitPDF.createPdf(mContext, SaleFragment.this, station(),
                                profitData(), filePath, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        StringWriter stringWriter = new StringWriter();
                        PrintWriter printWriter = new PrintWriter(stringWriter);
                        e.printStackTrace(printWriter);
                        String stackTrace = stringWriter.toString();
                        String path = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                        {
                            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +
                                    "/_Error.txt";
                        }
                        else
                        {
                            path = Environment.getExternalStorageDirectory() + "/Sarensa/_Error.txt";
                        }
                        try (FileWriter fileWriter = new FileWriter(path)){
                            fileWriter.write(stackTrace);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        //Log.e(TAG,"Error Creating Pdf");
                        Toast.makeText(mContext, "Error Creating Pdf", Toast.LENGTH_SHORT).show();
                        informationApi.snackBar(coordinatorLayout,"Error saved in "+ path,Color.RED);
                    }
                }
            }
        });
        builder.show();
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
    @Override
    public void onPDFClose(File file)
    {
        Toast.makeText(mContext,"Profit report processed successfully",Toast.LENGTH_SHORT).show();
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
                    "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+" POS.pdf";
        }
        else
        {
            path = Environment.getExternalStorageDirectory() + "/Sarensa/"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))+" POS.pdf";
        }
        return path;
    }
    private String profitPath(){
        String path = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +
                    "/000" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddHHmmss"))+" profit report.pdf";
        }
        else
        {
            path = Environment.getExternalStorageDirectory() + "/Sarensa/Profit/"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddHHmmss"))+" profit report.pdf";
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
                        String.valueOf(sales.getTotal()) + ".00"});
            }
            temp.add(new String[] {"TOTAL", "", "", "", String.valueOf(tot() + ".00")});
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

    Item par = new Item();
    par.setCategoryName(list.get(position).getItemCategory());
    par.setItemName(list.get(position).getItemName());
    par.setStationName(list.get(position).getStationName());

    Receipt receipt = new Receipt();
    receipt.setReceiptDate(list.get(position).getDate());
    receipt.setReceiptTime(list.get(position).getTime());
    receipt.setStationName(list.get(position).getStationName());
    Sales sales = new Sales();
    sales.setDate(list.get(position).getDate());
    sales.setTime(list.get(position).getTime());

    // deleting the note from db
    int currentQnty = databaseHelper.getItemQnty(list.get(position).getStationName(),list.get(position).getItemCategory(),
                list.get(position).getItemName());
    int newQuantity = list.get(position).getItemQnty() + currentQnty;
    //set new Quantity
        par.setItemQnty(newQuantity);
        databaseHelper.deleteSale(sales);
        databaseHelper.deleteReceipt(receipt);
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
                    informationApi.snackBar(coordinatorLayout, "Transaction Cancelled Successfully!", Color.GREEN);

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

    private List<String> date(){
        List<String> date = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            date.add(list.get(i).getDate());
        }
        return date;
    }

    /**
     * Hashmap for grouping similar items and sum them.
     * @return an arrylist of item name and the total profit
     */
    public List<String[]> profitData(){
        String maxDate = list.stream().map(Sales::getDate).max(String::compareTo).get();
        String minDate = list.stream().map(Sales::getDate).min(String::compareTo).get();
        List<Item> temp = new ArrayList<>();
        List<String> count = new ArrayList<>();
        List<String[]> value = new ArrayList<>();
        Map<String, Double> totalProfit = list.stream()
                .collect(groupingBy(Sales::getItemName, TreeMap::new, summingDouble(Sales::getProfit)));
        Map<String, Double> quantity = list.stream()
                .collect(groupingBy(Sales::getItemName, TreeMap::new, summingDouble(Sales::getItemQnty)));

        quantity.forEach((k,v) -> count.add(String.valueOf((int) Math.abs(v))));
        totalProfit.forEach((k, v) -> temp.add(new Item(minDate + " - " + maxDate, k, (int) Math.abs(v))));
        for (int i = 0; i < count.size(); i++) {
            value.add(new String[]{temp.get(i).getCategoryName(),temp.get(i).getItemName(),count.get(i), String.valueOf(temp.get(i).getItemQnty())});
        }
        value.add(new String[]{" ","TOTAL","",String.valueOf(prof()+".00")});
        return value;
    }
    @SuppressLint("StaticFieldLeak")
    private void buttonEvents(){
            if(Objects.equals(categoryEditText.getText().toString(), "") &&
                    Objects.equals(itemEditText.getText().toString(), "") &&
                    Objects.equals(spinner.getSelectedItem().toString(), "Select Station")) {
                if (froDatePicker.getText() == "" && toDatePicker.getText() == ""){
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
            if (Objects.equals(categoryEditText.getText().toString(), "") &&
                    Objects.equals(itemEditText.getText().toString(), "") &&
                    !Objects.equals(spinner.getSelectedItem().toString(), "Select Station")) {
                if(froDatePicker.getText() != "" && toDatePicker.getText() == "" ) {
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
                else if(froDatePicker.getText() != "" && toDatePicker.getText() != "" ) {
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
                else if(froDatePicker.getText() == "" && toDatePicker.getText() == "" ) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                            list.clear();
                            list.addAll(databaseHelper.getAllStationSales((spinner.getSelectedItem().toString())));
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
            if (!Objects.equals(categoryEditText.getText().toString(), "") &&
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
            if(Objects.equals(categoryEditText.getText().toString(), "") &&
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
           if (!Objects.equals(categoryEditText.getText().toString(), "") &&
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
            if (!Objects.equals(categoryEditText.getText().toString(), "") &&
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}