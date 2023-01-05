package mich.gwan.sarensa.ui.stock;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
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
import java.util.List;
import java.util.Objects;

import mich.gwan.sarensa.adapters.SalesRecyclerAdapter;
import mich.gwan.sarensa.adapters.StockRecyclerAdapter;
import mich.gwan.sarensa.databinding.ActivitySaleBinding;
import mich.gwan.sarensa.databinding.ActivityStockBinding;
import mich.gwan.sarensa.helpers.InputValidation;
import mich.gwan.sarensa.info.InformationApi;
import mich.gwan.sarensa.model.Item;
import mich.gwan.sarensa.model.Sales;
import mich.gwan.sarensa.pdf.PDFUtility;
import mich.gwan.sarensa.pdf.StockPDF;
import mich.gwan.sarensa.sql.DatabaseHelper;
import mich.gwan.sarensa.ui.sale.SaleFragment;

public class StockFragment extends Fragment implements StockPDF.OnDocClose {
    private RecyclerView recyclerView;
    private List<Item> list;
    private DatabaseHelper databaseHelper;
    private StockRecyclerAdapter recyclerAdapter;
    private InformationApi informationApi;

    private Spinner spinner;
    private CardView retrieve;
    private CardView print;

    private ActivityStockBinding binding;
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

        binding = ActivityStockBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initViews();
        initobjects();
        //buttonEvents();
        return root;
    }

    private void initViews() {
        recyclerView = binding.stockFragment.recyclerViewStock;
        spinner = binding.spinner;
        retrieve = binding.cardRetrieve;
        print = binding.cardPDF;
        coordinatorLayout = binding.stockLayout;

        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonEvents();
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filePath = path();
                // Make sure the path directory exists.
                try {
                    StockPDF.createPdf(mContext, StockFragment.this, station(),
                            getSampleData(), path(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                    //Log.e(TAG,"Error Creating Pdf");
                    Toast.makeText(mContext, "Error Creating Pdf", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void buttonEvents() {
        if(Objects.equals(spinner.getSelectedItem().toString(), "Select Station")) {
                new AsyncTask<Void, Void, Void>() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                        list.clear();
                        list.addAll(databaseHelper.getAllItems());
                        return null;
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        recyclerAdapter.notifyDataSetChanged();
                    }
                }.execute();
            } else {
            new AsyncTask<Void, Void, Void>() {
                @SuppressLint("StaticFieldLeak")
                @Override
                protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                    list.clear();
                    list.addAll(databaseHelper.getAllItems(spinner.getSelectedItem().toString()));
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
    }

    private void initobjects() {
        list = new ArrayList<>();
        recyclerAdapter = new StockRecyclerAdapter(list);
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

    @SuppressLint("StaticFieldLeak")
    private void getDataFromSQLite() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                list.clear();
                list.addAll(databaseHelper.getAllItems());
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

    @Override
    public void onPDFClose(File file) {
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
            Item sales = list.get(i);
            temp.add(new String[] {sales.getStationName(), sales.getCategoryName(),
                    String.valueOf(sales.getItemName()), String.valueOf(sales.getItemQnty())});
        }
        temp.add(new String[] {"TOTAL", "", "", String.valueOf(tot())});
        return  temp;
    }

    public int tot() {
        int total = 0;
        for(int i = 0; i <list.size(); i++) {
            total += list.get(i).getItemQnty();
        }
        return total;
    }

}

