package mich.gwan.sarensa.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.adapters.CartRecyclerAdapter;
import mich.gwan.sarensa.interfaces.AdapterCallback;
import mich.gwan.sarensa.adapters.CategoryRecyclerAdapter;
import mich.gwan.sarensa.adapters.ItemRecyclerAdapter;
import mich.gwan.sarensa.databinding.ActivityCategoryBinding;
import mich.gwan.sarensa.helpers.InputValidation;
import mich.gwan.sarensa.helpers.RecyclerTouchListener;
import mich.gwan.sarensa.interfaces.CartAdapterCallback;
import mich.gwan.sarensa.model.Cart;
import mich.gwan.sarensa.model.Category;
import mich.gwan.sarensa.model.Item;
import mich.gwan.sarensa.model.Sales;
import mich.gwan.sarensa.model.TempCart;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class SaleCategoryViewActivity extends AppCompatActivity implements AdapterCallback {
    private RecyclerView recyclerViewCat;
    private RecyclerView recyclerViewItem;
    private final AppCompatActivity activity = SaleCategoryViewActivity.this;
    private List<Category> listCat;
    private List<Item> listItem;
    public List<Sales> salesList;
    public List<TempCart> cartList;
    public List<Cart> catList;
    private DatabaseHelper databaseHelper;
    private CategoryRecyclerAdapter categoryRecyclerAdapter;
    private CartRecyclerAdapter cartRecyclerAdapter;
    private ItemRecyclerAdapter itemRecyclerAdapter;
    private InputValidation inputValidation;
    private ActivityCategoryBinding binding;
    private AppCompatImageView iconBack;
    private AppCompatTextView stationName;
    private AppCompatTextView emptyCategory;
    private AppCompatTextView emptyItem;
    private FloatingActionButton catRegister;
    private FloatingActionButton itemRegister;
    private CardView sellCard;
    private Intent intent;
    public  String myStation;
    public  String myCategory;

    protected void onCreate(Bundle savedInstanceState){
        try{
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //getSupportActionBar().setTitle("Gas Transactions");
        initViews();
        initobjects();
        actionEvents();
        //setListItem(listItem);
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.window));
        } catch (InflateException e){
            e.getCause().printStackTrace();
        } catch (Exception e) {
            // generic exception handling
            e.printStackTrace();
        }
    }

    @Override
    public void onMethodCallback(String id) {
        this.myCategory = id;
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
        iconBack = binding.categoryView.iconBack;
        stationName = binding.categoryView.textViewStationName;
        catRegister = binding.categoryRegister;
        itemRegister = binding.itemRegister;
        emptyCategory = binding.categoryView.noCatTextView;
        emptyItem = binding.categoryView.noItemTextView;
        sellCard = binding.sellCard;
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
        cartList = new ArrayList<>();
        catList = new ArrayList<>();
        itemRecyclerAdapter = new ItemRecyclerAdapter(listItem);
        cartRecyclerAdapter = new CartRecyclerAdapter(catList);
        categoryRecyclerAdapter = new CategoryRecyclerAdapter(listCat, this);
        inputValidation = new InputValidation(activity);

        RecyclerView.LayoutManager catLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewCat.setLayoutManager(catLayoutManager);
        recyclerViewCat.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCat.setHasFixedSize(true);
        recyclerViewCat.setAdapter(categoryRecyclerAdapter);

        RecyclerView.LayoutManager itemLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewItem.setLayoutManager(itemLayoutManager);
        recyclerViewItem.setItemAnimator(new DefaultItemAnimator());
        recyclerViewItem.setHasFixedSize(true);
        recyclerViewItem.setAdapter(itemRecyclerAdapter);

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         **/
        // action for category model recycler
        recyclerViewCat.addOnItemTouchListener(new RecyclerTouchListener(activity,
                recyclerViewCat, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //myCategory = listCat.get((int) recyclerViewCat.getChildViewHolder(view).getItemId()).getCategoryName();
                listItem.clear();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        getItemsFromSQlite();
                        toggleItemViews();
                    }
                }, 1000);
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
        // action for item model recycler
        recyclerViewItem.addOnItemTouchListener(new RecyclerTouchListener(activity,
                recyclerViewItem, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                toggleSellCardView();
                // add data to cart


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
    }


    /**
     * handle events
     */
    public void addItem(View view) {
        showItemDialog(false, null, -1);
    }
    public void actionSell(View view) {
        getCartFromSQlite();
        cartDialog();

    }

    public void addCategory(View view) {
        showNoteDialog(false, null, -1);
    }
    public void navigateBack(View view) {
        activity.finish();
    }
    private void actionEvents(){
        //itemRegister.setVisibility(View.GONE);

    }
    /**
     * Inserting new value in db
     * and refreshing the list
     */
    @SuppressLint("NotifyDataSetChanged")
    private void insertValue(String catName, String station) {
        // inserting value in db and getting
        // newly inserted value id
        Category par = new Category();
        par.setCategoryName(catName);
        par.setStationName(station);
        databaseHelper.addCategory(par);
        //long id = db.insertNote(station, location);
        // get the newly inserted note from db
        //Station n = db.getNote(id);

        // adding new values to array list at 0 position
        listCat.add(0, par);
        // refreshing the list
        categoryRecyclerAdapter.notifyDataSetChanged();
        toggleCatViews();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void insertItem(String itemName,String quantity, String buyPrice, String sellPrice) {
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
        //long id = db.insertNote(station, location);
        // get the newly inserted note from db
        //Station n = db.getNote(id);

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
    private void updateValue(String station, String catName, int position) {
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
    private void updateItem(String station,String category,String itemName,String quantity, String buyPrice, String sellPrice, int position) {
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
        itemRecyclerAdapter.notifyItemChanged(position);

        toggleItemViews();
    }

    /**
     * Deleting values from SQLite and removing the
     * item from the list by its position
     */
    private void deleteValue(int position) {
        // deleting the value from db
        databaseHelper.deleteCategory(listCat.get(position));
        // removing value from the list
        listCat.remove(position);
        categoryRecyclerAdapter.notifyItemRemoved(position);

        toggleCatViews();
    }
    private void deleteItem(int position) {
        // deleting the value from db
        databaseHelper.deleteItem(listItem.get(position));
        // removing value from the list
        listItem.remove(position);
        itemRecyclerAdapter.notifyItemRemoved(position);

        toggleItemViews();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence[] action = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Choose Action");
        builder.setItems(action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, listCat.get(position), position);
                } else {
                    deleteValue(position);
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
                    deleteItem(position);
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
    private void showNoteDialog(final boolean shouldUpdate, final Category category, final int position) {
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
                    } else {
                        alertDialog.dismiss();
                    }

                    // check if user is updating values
                    if (shouldUpdate && category != null) {
                        // update values by it's position
                        updateValue(stationName.getText().toString().toUpperCase(),
                                categoryName.getText().toString().toUpperCase(), position);
                    } else {
                        // create new note
                        insertValue(categoryName.getText().toString().toUpperCase(), myStation);
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
        final EditText qnty = view.findViewById(R.id.textInputEditTextItemQnty);
        final EditText buyPrice = view.findViewById(R.id.textInputEditTextItemBuyPrice);
        final EditText sellPrice = view.findViewById(R.id.textInputEditTextItemSellPrice);
        final TextView stationName = view.findViewById(R.id.textStationName);
        TextView dialogTitle = view.findViewById(R.id.title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_register_item) : getString(R.string.lbl_edit));

        if (shouldUpdate && item != null) {
            // display text to views
            categoryName.setText(item.getCategoryName());
            itemName.setText(item.getItemName());
            qnty.setText(String.valueOf(item.getItemQnty()));
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
                //if(!inputValidation.isEditTextOccupied(categoryName,getString(R.string.error_message_category))){
                //    categoryName.requestFocus();
                //    return;
                //}
                if(!inputValidation.isEditTextOccupied(itemName,getString(R.string.error_message_item_name))){
                    itemName.requestFocus();
                    return;
                }
                if(!inputValidation.isEditTextOccupied(qnty,getString(R.string.error_message_item_qnty))){
                    qnty.requestFocus();
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
                else {
                    alertDialog.dismiss();
                }

                // check if user is updating values
                if (shouldUpdate && item != null) {
                    // update values by it's position
                    updateItem(stationName.getText().toString().toUpperCase(), categoryName.getText().toString().toUpperCase(),
                            itemName.getText().toString().toUpperCase(), qnty.getText().toString().toUpperCase(),
                            buyPrice.getText().toString().toUpperCase(), sellPrice.getText().toString().toUpperCase(), position);
                } else {
                    // create new note
                    insertItem(itemName.getText().toString().toUpperCase(), qnty.getText().toString().toUpperCase(),
                            buyPrice.getText().toString().toUpperCase(), sellPrice.getText().toString().toUpperCase());
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

            final AlertDialog.Builder cartBuilder = new AlertDialog.Builder(activity);
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
                    .setPositiveButton("COMPLETE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {

                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
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
                    //perform sale transaction
                    Sales par = new Sales();
                    Item item = new Item();
                    System.out.println("Beginning loop");
                    for (int i = 0; i < catList.size(); i++) {
                        int sellPrice = databaseHelper.getSellingPrice(myStation, catList.get(i).getCategoryName(), catList.get(i).getItemName());
                        int buyPrice = databaseHelper.getBuyingPrice(myStation, catList.get(i).getCategoryName(), catList.get(i).getItemName());
                        int total = sellPrice * catList.get(i).getItemQnty();
                        int profit = total - (buyPrice * catList.get(i).getItemQnty());
                        par.setTime(String.valueOf(LocalTime.now()));
                        par.setDate(String.valueOf(LocalDate.now()));
                        par.setStationName(myStation);
                        par.setItemCategory(catList.get(i).getCategoryName());
                        par.setItemName(catList.get(i).getItemName());
                        par.setItemQnty(catList.get(i).getItemQnty());
                        par.setSellPrice(catList.get(i).getSellPrice());
                        par.setTotal(catList.get(i).getSellPrice());
                        par.setProfit(profit);
                        // complete transactions
                        databaseHelper.addSale(par);
                        System.out.println("Sales completed!!!!!!!");

                        //update affected quantities
                        // arithmetically subtract
                        int newQnty = databaseHelper.getItemQnty(myStation, catList.get(i).getCategoryName(),
                                catList.get(i).getItemName()) - catList.get(i).getItemQnty();
                        // set the values to the item object
                        item.setItemQnty(newQnty);
                        item.setItemName(catList.get(i).getItemName());
                        item.setCategoryName(catList.get(i).getCategoryName());
                        item.setStationName(myStation);
                        // update database
                        databaseHelper.updateItemQnty(item);
                        System.out.println("Data updated");
                    }
                    System.out.println("End of loop");
                    // delete cart table items
                    databaseHelper.deleteCart();
                    System.out.println("Table Cart cleared");
                    //clear list
                    catList.clear();
                    System.out.println("List cleared");
                    cartRecyclerAdapter.notifyDataSetChanged();
                    // remove button sell
                    toggleSellCardView();
                    itemRecyclerAdapter.notifyDataSetChanged();
                    dialog.cancel();
                    System.out.println("Dialog dismissed");
                }
            });
        } catch (InflateException e){
            e.getCause().printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * create a new cart list with updated category name and quantity
     * @return cart list
     *
    public List<Cart> cart(){
        Cart par = new Cart();
        List<Cart> cart = new ArrayList<>();
        for (int i = 0; i <cartList.size(); i++){
            String category = databaseHelper.getCartCategory(myStation,cartList.get(i).getItemName());
            int quantity = databaseHelper.getCartCount(myStation,category,cartList.get(i).getItemName());
            par.setStationName(myStation);
            par.setCategoryName(category);
            par.setItemName(cartList.get(i).getItemName());
            par.setItemQnty(quantity);
            par.setBuyPrice(cartList.get(i).getBuyPrice());
            par.setSellPrice(cartList.get(i).getSellPrice());
            cart.add(par);
        }
        return cart;
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
        if (databaseHelper.checkIfEmptyCart()) {
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
        itemRegister.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() ->  itemRegister.setVisibility(View.GONE), 180000);
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
                //item.clear();
                listItem.addAll(databaseHelper.getAllItems(myStation,myCategory));
                return null;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
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
                itemRecyclerAdapter.notifyDataSetChanged();

            }
        }.execute();
    }
}
