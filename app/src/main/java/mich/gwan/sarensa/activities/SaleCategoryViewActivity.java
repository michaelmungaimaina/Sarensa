package mich.gwan.sarensa.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.adapters.CartRecyclerAdapter;
import mich.gwan.sarensa.adapters.CategoryRecyclerAdapter;
import mich.gwan.sarensa.adapters.ItemRecyclerAdapter;
import mich.gwan.sarensa.databinding.ActivityCategoryBinding;
import mich.gwan.sarensa.helpers.InputValidation;
import mich.gwan.sarensa.helpers.ProgressDialogHelper;
import mich.gwan.sarensa.helpers.RecyclerTouchListener;
import mich.gwan.sarensa.model.Cart;
import mich.gwan.sarensa.model.Category;
import mich.gwan.sarensa.model.Item;
import mich.gwan.sarensa.model.Receipt;
import mich.gwan.sarensa.model.Sales;
import mich.gwan.sarensa.pdf.ReceiptPDF;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class SaleCategoryViewActivity extends AppCompatActivity implements ReceiptPDF.OnDocumentClose {
    private RecyclerView recyclerViewCat;
    private RecyclerView recyclerViewItem;
    private final AppCompatActivity activity = SaleCategoryViewActivity.this;
    private List<Category> listCat;
    private List<Item> listItem;
    public List<Sales> salesList;
    public List<Cart> catList;
    private DatabaseHelper databaseHelper;
    private CategoryRecyclerAdapter categoryRecyclerAdapter;
    private CartRecyclerAdapter cartRecyclerAdapter;
    private ItemRecyclerAdapter itemRecyclerAdapter;
    private InputValidation inputValidation;
    private ActivityCategoryBinding binding;
    private AppCompatTextView stationName;
    private AppCompatTextView emptyCategory;
    private AppCompatTextView emptyItem;
    private FloatingActionButton catRegister;
    private FloatingActionButton itemRegister;
    private RelativeLayout relativeLayout;
    private CardView sellCard;
    private DatePickerDialog datePickerDialog;
    private TextView selectDate;
    private ImageView toolbarBackIcon;
    private ImageView toolbarFilterIcon;
    private TextView toolbarTitleTextView;
    private CardView toolbarCardView;
    private EditText toolbarFilterEditText;
    private ImageView toolbarCancelIcon;
    private SearchView searchView;
    private Window window;
    private Intent intent;
    public  String myStation;
    public  String myCategory;
    private String filePath;
    HashMap<String,Integer> cartPosition;
    private boolean isDateSet = false;
    private boolean isFilterMode = false;
    private ProgressBar progressBar;
    private ConstraintLayout dateLayout;
    private RecyclerView.LayoutManager itemLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private boolean isDateLayoutVisible = false;

    public ItemRecyclerAdapter.ItemViewHolder.OnFirstItemVisibilityCallBack onFirstItemVisibilityCallback;

    public void setOnFirstItemVisibilityCallback(ItemRecyclerAdapter.ItemViewHolder.OnFirstItemVisibilityCallBack onFirstItemVisibilityCallback) {
        this.onFirstItemVisibilityCallback = onFirstItemVisibilityCallback;
    }

    protected void onCreate(Bundle savedInstanceState){
        try{
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /**Objects.requireNonNull(this.getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.toolbar_content);
        /**/
        //getSupportActionBar().setTitle("Gas Transactions");
        initViews();
        initobjects();
        enableFilterMode();
        //setListItem(listItem);
        window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.window));
        } catch (InflateException e){
            e.getCause().printStackTrace();
        } catch (Exception e) {
            // generic exception handling
            e.printStackTrace();
        }
    }
    /**
    @Override
    public void onMethodCall(List<Cart> list) {
        //this.catList = list;
    }
    /**
     * Initialize all views
     */
    private  void initViews(){
        recyclerViewCat = binding.categoryView.recyclerCategory;
        recyclerViewItem = binding.categoryView.recyclerItem;
        stationName = binding.categoryView.textViewStationName;
        catRegister = binding.categoryRegister;
        itemRegister = binding.itemRegister;
        emptyCategory = binding.categoryView.noCatTextView;
        emptyItem = binding.categoryView.noItemTextView;
        sellCard = binding.sellCard;
        relativeLayout = binding.relativeLayout;
        selectDate = binding.categoryView.selectDate;
        progressBar = binding.categoryView.progressbar;
        dateLayout = binding.categoryView.dateLayout;

        //initialize the custom toolbar views
        ///View view = Objects.requireNonNull(getSupportActionBar()).getCustomView();
        toolbarBackIcon = binding.categoryView.backIcon;
        toolbarFilterIcon = binding.categoryView.filterIcon;
        toolbarTitleTextView = binding.categoryView.titleTextView;
        toolbarCardView = binding.categoryView.searchCardView;
        toolbarFilterEditText = binding.categoryView.searchTextField;
        toolbarCancelIcon = binding.categoryView.cancelIcon;

        //searchView = (SearchView) toolbarFilterIcon.getActionView()
    }

    /**
     * Enable filter mode
     */
    public void enableFilterMode(){
        if(isFilterMode){
            toolbarCardView.setVisibility(View.VISIBLE);
            toolbarTitleTextView.setVisibility(View.GONE);
        } else {
            toolbarCardView.setVisibility(View.GONE);
            toolbarTitleTextView.setVisibility(View.VISIBLE);
            inputValidation.hideKeyboardFrom(toolbarFilterEditText);
        }
    }

    /**
     * initialize objects
     */
    private void initobjects() {
        intent = getIntent();
        myStation = intent.getStringExtra("myStationName");
        stationName.setText(myStation);
        listCat = new ArrayList<>();
        listItem = new ArrayList<>();
        salesList = new ArrayList<>();
        catList = new ArrayList<>();
        // maintain cart with position
        cartPosition = new HashMap<String,Integer>();
        itemRecyclerAdapter = new ItemRecyclerAdapter( listItem);
        //itemLIstAdapter = new ItemListAdapter(this,R.layout.item_recycler,listItem);
        cartRecyclerAdapter = new CartRecyclerAdapter(catList);
        categoryRecyclerAdapter = new CategoryRecyclerAdapter(listCat);
        inputValidation = new InputValidation(activity);

        RecyclerView.LayoutManager catLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewCat.setLayoutManager(catLayoutManager);
        recyclerViewCat.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCat.setHasFixedSize(true);
        recyclerViewCat.setAdapter(categoryRecyclerAdapter);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
         //itemLayoutManager = new LinearLayoutManager(getApplicationContext());
         recyclerViewItem.setLayoutManager(linearLayoutManager);
         recyclerViewItem.setItemAnimator(new DefaultItemAnimator());
         recyclerViewItem.setHasFixedSize(true);
         recyclerViewItem.setAdapter(itemRecyclerAdapter);

         selectDate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 final Calendar c = Calendar.getInstance();
                 int mYear = c.get(Calendar.YEAR);
                 int mMonth = c.get(Calendar.MONTH);
                 int mDay = c.get(Calendar.DAY_OF_MONTH);

                 datePickerDialog = new DatePickerDialog(SaleCategoryViewActivity.this, R.style.CustomDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                     @SuppressLint("SetTextI18n")
                     @Override
                     public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                         if (dayOfMonth < 10 && month < 9 ){
                             String strDate = year + "-0" + (month + 1) + "-0" + dayOfMonth;
                             selectDate.setText(strDate);
                         }
                         if (dayOfMonth < 10 && month >= 9){
                             String strDate = year + "-" + (month + 1) + "-0" + dayOfMonth;
                             selectDate.setText(strDate);
                         }
                         if (dayOfMonth >= 10 && month < 9){
                             String strDate = year + "-0" + (month + 1) + "-" + dayOfMonth;
                             selectDate.setText(strDate);
                         }
                         if (dayOfMonth >= 10 && month >= 9){
                             String strDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                             selectDate.setText(strDate);
                         }
                         isDateSet = true;
                     }
                 }, mYear, mMonth, mDay);
                 datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                 datePickerDialog.show();
             }
         });

         /* *
         * Attach adapter to listview
         */
        //recyclerViewItem.setAdapter(itemLIstAdapter);
        itemRecyclerAdapter.setOnItemClickListener(position -> addToCart(position));
        cartRecyclerAdapter.setOnItemClickListener(position -> removeFromCart(position));

        /*
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         **/
        // action for category model recycler
        recyclerViewCat.addOnItemTouchListener(new RecyclerTouchListener(activity,
                recyclerViewCat, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //showLoader(this, "Loading data ...");
                progressBar.setVisibility(View.VISIBLE);
                myCategory = listCat.get(position).getCategoryName();
                listItem.clear();
                showAddFAB();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        getItemsFromSQlite();
                        toggleItemViews();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);                            }
                        }, 600);
                    }
                }, 1000);
            }

            @Override
            public void onLongClick(View view, int position) {
                showCategoryActionsDialog(position);
            }
        }));

        recyclerViewItem.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(linearLayoutManager.findFirstVisibleItemPosition() == 0){
                    //hide
                    if(onFirstItemVisibilityCallback != null){
                        onFirstItemVisibilityCallback.onChange(false);
                    }
                }else{
                    //show
                    if(onFirstItemVisibilityCallback != null){
                        onFirstItemVisibilityCallback.onChange(true);
                    }
                }

            }
        });

        setOnFirstItemVisibilityCallback(new ItemRecyclerAdapter.ItemViewHolder.OnFirstItemVisibilityCallBack() {
            @Override
            public void onChange(boolean isVisible) {
                if(isVisible){
                    if(!isDateLayoutVisible){
                        isDateLayoutVisible = true;
                        dateLayout.setVisibility(View.GONE);
                    }
                }else{
                    if(isDateLayoutVisible){
                        isDateLayoutVisible = false;
                        dateLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        /**
         * action for item model recycler
        **/

        recyclerViewItem.addOnItemTouchListener(new RecyclerTouchListener(activity,
                recyclerViewItem, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                toggleSellCardView();
            }

            @Override
            public void onLongClick(View view, int position) {
                showItemActionsDialog(position);
            }
        }));

        databaseHelper = new DatabaseHelper(this);
        getDataFromSQLite();
        //getCartFromSQlite();
        toggleCatViews();
        toggleSellCardView();

        toolbarFilterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFilterMode = true;
                enableFilterMode();
                toolbarFilterEditText.requestFocus();
                WindowCompat.getInsetsController(getWindow(), toolbarFilterEditText).show(WindowInsetsCompat.Type.ime());
                Toast.makeText(SaleCategoryViewActivity.this, "Filter mode enabled", Toast.LENGTH_LONG).show();
            }
        });

        toolbarCancelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFilterMode = false;
                enableFilterMode();
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                itemRecyclerAdapter.updateList(listItem);
                Toast.makeText(SaleCategoryViewActivity.this, "Filter mode disabled", Toast.LENGTH_LONG).show();
            }
        });

        toolbarBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });

        toolbarFilterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    void filter(String text){
        List<Item> temp = new ArrayList<>();
        for(Item item: listItem){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(item.getItemName().contains(text.toUpperCase())){
                temp.add(item);
            }
        }
        //update recyclerview
        itemRecyclerAdapter.updateList(temp);
    }


    @SuppressLint("NotifyDataSetChanged")
    private void removeFromCart(int position) {
            int count = databaseHelper.getCartQnty(catList.get(position).getStationName(),
                    catList.get(position).getCategoryName(), catList.get(position).getItemName());
        // create new item parameter
        Item parr = new Item();
        parr.setStationName(catList.get(position).getStationName());
        parr.setCategoryName(catList.get(position).getCategoryName());
        parr.setItemName(catList.get(position).getItemName());
        parr.setBuyPrice(catList.get(position).getBuyPrice());
        parr.setSellPrice(catList.get(position).getSellPrice());
            count--;
            if (count >= 1) {
                Cart par = new Cart();
                par.setStationName(catList.get(position).getStationName());
                par.setCategoryName(catList.get(position).getCategoryName());
                par.setItemName(catList.get(position).getItemName());
                par.setBuyPrice(catList.get(position).getBuyPrice());
                par.setSellPrice(catList.get(position).getSellPrice());
                par.setItemQnty(count);
                // update new cart quantity
                databaseHelper.updateQuantity(par);
                // get existing item quantity
                int itemQnty = databaseHelper.getItemQnty(catList.get(position).getStationName(),
                        catList.get(position).getCategoryName(), catList.get(position).getItemName());
                // add 1 to the new item quantity parameter
                parr.setItemQnty(itemQnty + 1);
                // update the item par on the database
                databaseHelper.updateItemQnty(parr);
                // Notify adapter
                cartRecyclerAdapter.notifyDataSetChanged();
                // update the arrayList
                catList.set(position, par);
                // show toast
                Toast.makeText(this, "Quantity Reduced!", Toast.LENGTH_SHORT).show();
            } else {
                // get existing item quantity
                int itemQnty = databaseHelper.getItemQnty(catList.get(position).getStationName(),
                        catList.get(position).getCategoryName(), catList.get(position).getItemName());
                // add 1 to the new item quantity parameter
                parr.setItemQnty(itemQnty + 1);
                // update the item par on the database
                databaseHelper.updateItemQnty(parr);
                  // delete cart entry from database
                  databaseHelper.deleteCart(catList.get(position).getStationName(),
                          catList.get(position).getCategoryName(), catList.get(position).getItemName());
                  // remove cart entry from list
                  catList.remove(position);
                  int currentSize = catList.size();
                //tell the recycler view that all the old items are gone
                cartRecyclerAdapter.notifyItemChanged(position);
                cartRecyclerAdapter.notifyItemRangeRemoved(0, currentSize);
                //tell the recycler view how many new items we added
                //notifyItemRangeInserted(0, newArticles.size());
                  Toast.makeText(this, "Item Removed!", Toast.LENGTH_SHORT).show();
                  //notifyDataSetChanged();
            }

    }

    @SuppressLint("NotifyDataSetChanged")
    private void addToCart(int position) {
        if (isDateSet) {
            // add item to cart
            int count = databaseHelper.getItemQnty(listItem.get(position).getStationName(),
                    listItem.get(position).getCategoryName(), listItem.get(position).getItemName());
            count--;
            if (count >= 0) {
                Item parr = new Item();
                parr.setStationName(listItem.get(position).getStationName());
                parr.setCategoryName(listItem.get(position).getCategoryName());
                parr.setItemName(listItem.get(position).getItemName());
                parr.setBuyPrice(listItem.get(position).getBuyPrice());
                parr.setSellPrice(listItem.get(position).getSellPrice());
                Cart par = new Cart();
                par.setStationName(listItem.get(position).getStationName());
                par.setCategoryName(listItem.get(position).getCategoryName());
                par.setItemName(listItem.get(position).getItemName());
                par.setBuyPrice(listItem.get(position).getBuyPrice());
                par.setSellPrice(listItem.get(position).getSellPrice());
                if (!databaseHelper.checkCart(listItem.get(position).getStationName(), listItem.get(position).getCategoryName(),
                        listItem.get(position).getItemName())) {
                    //add 1 quantity
                    par.setItemQnty(1);
                    databaseHelper.addCart(par);
                    int qnty = databaseHelper.getItemQnty(listItem.get(position).getStationName(),
                            listItem.get(position).getCategoryName(), listItem.get(position).getItemName());
                    // less 1 quantity from items
                    parr.setItemQnty(qnty - 1);
                    // update values
                    databaseHelper.updateItemQnty(parr);
                    // notify adapter
                    itemRecyclerAdapter.notifyItemChanged(position);

                } else {
                    // get existing cart quantity
                    int qnty = databaseHelper.getCartQnty(listItem.get(position).getStationName(),
                            listItem.get(position).getCategoryName(), listItem.get(position).getItemName());
                    //set new qnty value
                    par.setItemQnty(qnty + 1);
                    // update quantity instead
                    databaseHelper.updateQuantity(par);
                    int quanty = databaseHelper.getItemQnty(listItem.get(position).getStationName(),
                            listItem.get(position).getCategoryName(), listItem.get(position).getItemName());
                    // less 1 quantity from items
                    parr.setItemQnty(quanty - 1);
                    // update values
                    databaseHelper.updateItemQnty(parr);
                    itemRecyclerAdapter.notifyDataSetChanged();
                    //itemRecyclerAdapter.notifyItemChanged(position);
                }
            } else {
                Toast.makeText(this, "Stock is Depleted, Kindly update Stock", Toast.LENGTH_SHORT).show();
                System.out.println("OUT OF STOCK. PLEASE UPDATE STOCK");
                //itemRecyclerAdapter.notifyItemChanged(position);
                itemRecyclerAdapter.notifyDataSetChanged();
            }
            toggleSellCardView();
        }else{
            Toast.makeText(this, "Please set date first", Toast.LENGTH_SHORT).show();
            selectDate.requestFocus();
        }
    }


    /**
     * handle events
     */
    public void addItem(View view) {
        showItemDialog(false, null, -1);
    }
    public void actionSell(View view) {
        getCartFromSQlite();
        if (isDateSet) {
            cartDialog();
        } else {
            Toast.makeText(getApplicationContext(),"Please set date first",Toast.LENGTH_LONG).show();
        }

    }

    /**
     * onClick event to show add category dialog
     * @param view floating action button that is clicked
     */
    public void addCategory(View view) {
        showCategoryDialog(false, null, -1);
    }

    /**
     * onClick event to navigate back to main view
     * @param view backbutton to be clicked
     */
    public void navigateBack(View view) {
        activity.finish();
    }
    /**
     * Inserting new value in db
     * and refreshing the list
     */
    @SuppressLint("NotifyDataSetChanged")
    private void insertNewCategory(String catName, String station) {
        // inserting value in db and getting
        // newly inserted value id
        Category par = new Category();
        par.setCategoryName(catName);
        par.setStationName(station);
        databaseHelper.addCategory(par);

        // adding new values to array list at 0 position
        listCat.add(0, par);
        // refreshing the list
        categoryRecyclerAdapter.notifyDataSetChanged();
        toggleCatViews();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void insertNewItem(String itemName, String quantity, String buyPrice, String sellPrice) {
        // inserting value in db and getting
        // newly inserted value id
        Item par = new Item();
        par.setCategoryName(myCategory);
        par.setItemName(itemName);
        par.setStationName(myStation);
        par.setItemQnty(Integer.parseInt(quantity));
        par.setBuyPrice(Integer.parseInt(buyPrice));
        par.setSellPrice(Integer.parseInt(sellPrice));
        databaseHelper.addItem(par);

        // adding new values to array list at 0 position
        listItem.add(0, par);
        // refreshing the list
        itemRecyclerAdapter.notifyDataSetChanged();
        toggleItemViews();
    }

    /**
     * Updating values in db and updating
     * item in the list by its position
     */
    private void updateCategoryValue(String station, String catName, int position) {
        Category par = listCat.get(position);
        // updating values
        par.setStationName(station);
        par.setCategoryName(catName);
        // updating values in db
        databaseHelper.updateCategory(par);
        // refreshing the list
        listCat.set(position, par);
        categoryRecyclerAdapter.notifyItemChanged(position);

        toggleCatViews();
    }
    private void updateItemValue(String station, String category, String itemName, String quantity, String buyPrice, String sellPrice, int position) {
        Item par = listItem.get(position);
        // updating values
        par.setStationName(station);
        par.setCategoryName(category);
        par.setItemName(itemName);
        par.setItemQnty(Integer.parseInt(quantity));
        par.setBuyPrice(Integer.parseInt(buyPrice));
        par.setSellPrice(Integer.parseInt(sellPrice));
        // updating values in db
        databaseHelper.updateItem(par);
        // refreshing the list
        listItem.set(position, par);
        //itemLIstAdapter.notifyDataSetChanged();
        itemRecyclerAdapter.notifyItemChanged(position);

        toggleItemViews();
    }

    /**
     * Deleting values from SQLite and removing the
     * item from the list by its position
     */
    private void deleteCategoryValue(int position) {
        // deleting the value from db
        databaseHelper.deleteCategory(listCat.get(position));
        // removing value from the list
        listCat.remove(position);
        categoryRecyclerAdapter.notifyItemRemoved(position);

        toggleCatViews();
    }
    private void deleteItemValue(int position) {
        // deleting the value from db
        databaseHelper.deleteItem(listItem.get(position));
        // removing value from the list
        listItem.remove(position);
        //itemLIstAdapter.notifyDataSetChanged();
        itemRecyclerAdapter.notifyItemRemoved(position);

        toggleItemViews();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showCategoryActionsDialog(final int position) {
        CharSequence[] action = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Choose Action");
        builder.setItems(action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showCategoryDialog(true, listCat.get(position), position);
                } else {
                    deleteCategoryValue(position);
                }
            }
        });
        builder.show();
    }
    private void showItemActionsDialog(final int position) {
        CharSequence[] colors = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Choose Action");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showItemDialog(true, listItem.get(position), position);
                } else {
                    deleteItemValue(position);
                }
            }
        });
        builder.show();
    }


    /**
     * Shows alert dialog with EditText options to enter / edit
     * a category.
     * when shouldUpdate=true, it automatically displays old value and changes the
     * button text to UPDATE
     */
    private void showCategoryDialog(final boolean shouldUpdate, final Category category, final int position) {
        try {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
            View view = layoutInflaterAndroid.inflate(R.layout.category_dialog, null);

            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
            alertDialogBuilderUserInput.setView(view);

            final EditText categoryName = view.findViewById(R.id.textInputEditTextCatName);
            final TextView stationName = view.findViewById(R.id.textStationName);
            TextView dialogTitle = view.findViewById(R.id.title);
            dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_register) : getString(R.string.lbl_edit));

            if (shouldUpdate && category != null) {
                // display text to views
                categoryName.setText(category.getCategoryName());
                stationName.setText(myStation);
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
                    if (!inputValidation.isEditTextOccupied(categoryName, getString(R.string.error_message_category))) {
                        categoryName.requestFocus();
                        return;
                    }

                    // check if user is updating values
                    if (shouldUpdate && category != null) {
                        // update values by it's position
                        updateCategoryValue(stationName.getText().toString().toUpperCase(),
                                categoryName.getText().toString().toUpperCase(), position);
                        // toast succesful update
                        Toast.makeText(activity,getString(R.string.category_update),Toast.LENGTH_SHORT).show();
                        //dismiss dialog
                        alertDialog.dismiss();
                    } else {
                        if (databaseHelper.checkCategory(myStation,categoryName.getText().toString().toUpperCase())){
                            Toast.makeText(activity,getString(R.string.category_exists),Toast.LENGTH_SHORT).show();
                        } else {
                            // create new note
                            insertNewCategory(categoryName.getText().toString().toUpperCase(), myStation);
                            // notify successful registration
                            Toast.makeText(activity,getString(R.string.category_registered),Toast.LENGTH_SHORT).show();
                            // clear fields
                            categoryName.setText("");
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
     * show dialog for editing or registering a new item
     * @param shouldUpdate boolean parameter for update or new item
     * @param item item model parameter
     * @param position item position on recyclerview
     */
    private void showItemDialog(final boolean shouldUpdate, final Item item, final int position) {
        try{
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.item_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
        alertDialogBuilderUserInput.setView(view);

        final EditText categoryName = view.findViewById(R.id.textInputEditTextCatName);
        final EditText itemName = view.findViewById(R.id.textInputEditTextItemName);
        final EditText newQuantity = view.findViewById(R.id.textInputEditTextItemNewQnty);
        final EditText buyPrice = view.findViewById(R.id.textInputEditTextItemBuyPrice);
        final EditText sellPrice = view.findViewById(R.id.textInputEditTextItemSellPrice);
        final TextView stationName = view.findViewById(R.id.textStationName);
        final TextView currentQuantity = view.findViewById(R.id.textViewCurrentQnty);
        final TextView currentQuantityText = view.findViewById(R.id.textViewCurrentQntyText);
        TextView dialogTitle = view.findViewById(R.id.title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_register_item) : getString(R.string.lbl_edit));

        if (shouldUpdate && item != null) {
            // display text to views
            categoryName.setText(item.getCategoryName());
            categoryName.setInputType(InputType.TYPE_NULL);
            itemName.setText(item.getItemName());
            currentQuantityText.setText(String.valueOf(databaseHelper.getItemQnty(
                    listItem.get(position).getStationName(),listItem.get(position).getCategoryName(),
                    listItem.get(position).getItemName())));
            buyPrice.setText(String.valueOf(item.getBuyPrice()));
            sellPrice.setText(String.valueOf(item.getSellPrice()));
            stationName.setText(item.getStationName());
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
                if(!inputValidation.isEditTextOccupied(itemName,getString(R.string.error_message_item_name))){
                    itemName.requestFocus();
                    return;
                }
                if(!inputValidation.isEditTextOccupied(newQuantity,getString(R.string.error_message_item_qnty))){
                    newQuantity.requestFocus();
                    return;
                }
                if(!inputValidation.isEditTextOccupied(buyPrice,getString(R.string.error_message_item_buy_price))){
                    buyPrice.requestFocus();
                    return;
                }
                if(!inputValidation.isEditTextOccupied(sellPrice,getString(R.string.error_message_item_sell_price))){
                    sellPrice.requestFocus();
                    return;
                }

                // check if user is updating values
                if (shouldUpdate && item != null) {
                    int quantity = Integer.parseInt(currentQuantityText.getText().toString()) + Integer.parseInt(newQuantity.getText().toString());
                    // update values by it's position
                    updateItemValue(stationName.getText().toString().toUpperCase(), categoryName.getText().toString().toUpperCase(),
                            itemName.getText().toString().toUpperCase(), String.valueOf(quantity),
                            buyPrice.getText().toString().toUpperCase(), sellPrice.getText().toString().toUpperCase(), position);
                    //toast to notify successful update
                    Toast.makeText(activity,getString(R.string.item_updated),Toast.LENGTH_SHORT).show();
                    //dismiss dialog
                    alertDialog.dismiss();
                } else {
                    //check whether the item already exists
                    if (databaseHelper.checkItem(myStation,myCategory,itemName.getText().toString().toUpperCase())){
                        Toast.makeText(activity,getString(R.string.item_exists),Toast.LENGTH_SHORT).show();
                    } else {
                        // create new note
                        insertNewItem(itemName.getText().toString().toUpperCase(), newQuantity.getText().toString().toUpperCase(),
                                buyPrice.getText().toString().toUpperCase(), sellPrice.getText().toString().toUpperCase());
                        // clear fields
                        categoryName.setText("");
                        itemName.setText("");
                        newQuantity.setText("");
                        buyPrice.setText("");
                        sellPrice.setText("");
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
     * method for displaying alert dialog with recyclerview
     * actual selling happens here
     */
    public void cartDialog(){
        try {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
            View view = layoutInflaterAndroid.inflate(R.layout.cart_dialog, null);

            final AlertDialog.Builder cartBuilder = new AlertDialog.Builder(activity,R.style.BottomSheetDialog);

            final EditText customerName = view.findViewById(R.id.editTextCustomerName);
            final EditText transType = view.findViewById(R.id.editTextTransactionType);
            // declare and initialize recyclerview
            RecyclerView cartRecycler = view.findViewById(R.id.recyclerCart);
            cartRecycler.setAdapter(cartRecyclerAdapter);
            // assign a layout manager
            RecyclerView.LayoutManager catLayoutManager = new LinearLayoutManager(getApplicationContext());
            cartRecycler.setLayoutManager(catLayoutManager);
            cartRecycler.setItemAnimator(new DefaultItemAnimator());
            cartRecycler.setHasFixedSize(true);
            cartBuilder.setView(view); // setView

            cartBuilder
                    .setCancelable(false)
                    .setPositiveButtonIcon(ContextCompat.getDrawable(activity,R.drawable.complete))
                    .setNegativeButtonIcon(ContextCompat.getDrawable(activity,R.drawable.close))
                    .setPositiveButton("", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {

                        }
                    })
                    .setNegativeButton("", new DialogInterface.OnClickListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        public void onClick(DialogInterface dialogBox, int id) {
                            //itemLIstAdapter.notifyDataSetChanged();
                            itemRecyclerAdapter.notifyDataSetChanged();
                            toggleSellCardView();
                            dialogBox.cancel();
                        }
                    });

             final AlertDialog dialog = cartBuilder.create();
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View v) {
                    String customer = "";
                    String transaction = "";
                    /*
                    validate inputs
                     */
                    if (Objects.equals(customerName.getText().toString(), "")){
                        customerName.setError(getString(R.string.error_message_customer_name));
                        customer = "NULL";
                    } else {
                        customer = customerName.getText().toString().toUpperCase();
                    }
                    if(Objects.equals(transType.getText().toString(), "")){
                        transType.setError(getString(R.string.error_message_cash_name));
                        transaction = "CASH";
                    } else{
                        if(!inputValidation.isCredit(transType,getString(R.string.error_message_credit_name))) {
                            transType.requestFocus();
                            return;
                        } else {
                            transaction = transType.getText().toString().toUpperCase();
                        }
                    }

                    //perform sale transaction
                    Sales par = new Sales();
                    for (int i = 0; i < catList.size(); i++) {
                        int sellPrice = databaseHelper.getSellingPrice(myStation, catList.get(i).getCategoryName(), catList.get(i).getItemName());
                        int buyPrice = databaseHelper.getBuyingPrice(myStation, catList.get(i).getCategoryName(), catList.get(i).getItemName());
                        int total = sellPrice * catList.get(i).getItemQnty();
                        int profit = total - (buyPrice * catList.get(i).getItemQnty());
                        par.setTime(String.valueOf(LocalTime.now()));
                        par.setDate(String.valueOf(selectDate.getText()));
                        par.setStationName(myStation);
                        par.setSaleType(transaction);
                        par.setItemCategory(catList.get(i).getCategoryName());
                        par.setItemName(catList.get(i).getItemName());
                        par.setItemQnty(catList.get(i).getItemQnty());
                        par.setSellPrice(catList.get(i).getSellPrice());
                        par.setTotal(total);
                        par.setProfit(profit);
                        // complete transactions
                        databaseHelper.addSale(par);
                        databaseHelper.addReceipt(new Receipt(String.valueOf(selectDate.getText()),String.valueOf(LocalTime.now()),
                                transaction,customer, myStation,catList.get(i).getCategoryName(),catList.get(i).getItemName(),
                                catList.get(i).getItemQnty(), catList.get(i).getSellPrice(),total));
                    }
                    int receiptNO = Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddHHmm")));
                    Toast.makeText(activity, "Transaction Completed Successfully!", Toast.LENGTH_SHORT).show();
                    filePath = path();
                    // Make sure the path directory exists.
                    try {
                        ReceiptPDF.createPdf(v.getContext(), SaleCategoryViewActivity.this,myStation,receiptNO,
                                getSampleData(), path(), true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Log.e(TAG,"Error Creating Pdf");
                        Toast.makeText(getApplicationContext(), "Error Creating Pdf", Toast.LENGTH_SHORT).show();
                    }
                    // delete cart table items
                    databaseHelper.deleteCart(myStation);
                    System.out.println("Table Cart cleared");
                    //clear list
                    catList.clear();
                    System.out.println("List cleared");
                    cartRecyclerAdapter.notifyDataSetChanged();
                    // remove button sell
                    itemRecyclerAdapter.notifyDataSetChanged();
                    toggleSellCardView();
                    dialog.cancel();
                    //getItemsFromSQlite();
                }
            });
        } catch (InflateException e){
            e.getCause().printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Alternate views if list is empty
     */
    public void toggleCatViews(){
        if (databaseHelper.getCategoryCount(myStation) > 0) {
            emptyCategory.setVisibility(View.GONE);
        }
        else {
            emptyCategory.setVisibility(View.VISIBLE);
        }
    }
    /**
     * Alternate views if list is empty
     */
    public void toggleItemViews(){
        if (databaseHelper.getItemCount(myStation,myCategory) > 0) {
            emptyItem.setVisibility(View.GONE);
        }
        else {
            emptyItem.setVisibility(View.VISIBLE);
        }
    }
    /**
     * Display sellcard if Cart has items
     */
    public void toggleSellCardView(){
        if (databaseHelper.checkIfEmptyCart(myStation)) {
            sellCard.setVisibility(View.GONE);
        }
        else {
            sellCard.setVisibility(View.VISIBLE);
        }
    }

    /**
     * method to display item fab
     */
    public void showAddFAB(){
        relativeLayout.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> relativeLayout.setVisibility(View.GONE), 5000);
    }

    /**
     * Fetch data from the database
     */
    @SuppressLint("StaticFieldLeak")
    private void getDataFromSQLite(){
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                listCat.clear();
                listCat.addAll(databaseHelper.getAllCategory(myStation));
                return null;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                categoryRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
    @SuppressLint("StaticFieldLeak")
    public void getItemsFromSQlite(){
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                listItem.clear();
                listItem.addAll(databaseHelper.getAllItems(myStation,myCategory));
                return null;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //itemLIstAdapter.notifyDataSetChanged();
                itemRecyclerAdapter.notifyDataSetChanged();

            }
        }.execute();
    }
    @SuppressLint("StaticFieldLeak")
    public void getCartFromSQlite(){
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(@SuppressLint("StaticFieldLeak") Void... params) {
                catList.clear();
                catList.addAll(databaseHelper.getAllCat(myStation));
                return null;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                cartRecyclerAdapter.notifyDataSetChanged();

            }
        }.execute();
    }

    @Override
    public void onPDFDocumentClose(File file) {
        Toast.makeText(this, "Receipt processed successfully", Toast.LENGTH_SHORT).show();
        // Get the File location and file name.
        Log.d("pdfFIle", "" + filePath);

        // Get the URI Path of file.
        Uri uriPdfPath = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", new File(filePath));
        Log.d("pdfPath", "" + uriPdfPath);

        // Start Intent to View PDF from the Installed Applications.
        Intent pdfOpenIntent = new Intent(Intent.ACTION_VIEW);
        pdfOpenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenIntent.setClipData(ClipData.newRawUri("", uriPdfPath));
        pdfOpenIntent.setDataAndType(uriPdfPath, "pdf");
        pdfOpenIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        try {
            startActivity(pdfOpenIntent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            Toast.makeText(this, "There is no app to load corresponding PDF", Toast.LENGTH_LONG).show();

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
            int count = catList.size();

            List<String[]> temp = new ArrayList<>();
            for (int i = 0; i < count; i++)
            {
                int total = catList.get(i).getSellPrice() * catList.get(i).getItemQnty();
                Cart sales = catList.get(i);
                temp.add(new String[] {sales.getItemName(),
                        String.valueOf(sales.getItemQnty()), String.valueOf(sales.getSellPrice()) + ".00",
                        String.valueOf(total) + ".00"});
            }
            temp.add(new String[] {"TOTAL", "", "", String.valueOf(tot() + ".00")});
            return  temp;
        }
    public int tot() {
        int total = 0;
        int netTotal = 0;
        for(int i = 0; i <catList.size(); i++) {
            netTotal = catList.get(i).getSellPrice() * catList.get(i).getItemQnty();
            total += netTotal;
        }
        return total;
    }

}
