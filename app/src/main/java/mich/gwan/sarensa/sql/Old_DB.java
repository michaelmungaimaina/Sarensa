package mich.gwan.sarensa.sql;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import mich.gwan.sarensa.model.Cart;
import mich.gwan.sarensa.model.Category;
import mich.gwan.sarensa.model.Item;
import mich.gwan.sarensa.model.Sales;
import mich.gwan.sarensa.model.Station;
import mich.gwan.sarensa.model.User;

public class Old_DB extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "sarensalimited.db";

    // Table names
    private static final String TABLE_USER = "users";
    private static final String TABLE_STATION = "stations";
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_ITEM = "items";
    private static final String TABLE_SALES = "sales";
    private static final String TABLE_CART = "cart";

    // Station Table column names
    private static final String COLUMN_STATION_ID = "id";
    private static final String COLUMN_STATION_NAME = "name";
    private static final String COLUMN_STATION_LOCATION = "location";

    // User Table Column names
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PHONE = "phone";
    private static final String COLUMN_USER_PASSWORD = "password";
    private  static final String COLUMN_USER_TYPE = "type";

    // Category Table Column names
    private static final String COLUMN_CATEGORY_ID ="id";
    private static final String COLUMN_CATEGORY_NAME = "name";
    private static final String COLUMN_CATEGORY_STATION = "station";

    // Item Table Column names
    private static final String COLUMN_ITEM_ID = "itemid";
    private static final String COLUMN_ITEM_STATION = "station";
    private static final String COLUMN_ITEM_CATEGORY = "category";
    private static final String COLUMN_ITEM_NAME = "name";
    private static final String COLUMN_ITEM_QUANTITY = "quantity";
    private static final String COLUMN_ITEM_BUYPRICE = "buyprice";
    private static final String COLUMN_ITEM_SELLPRICE = "sellprice";

    // Sales Table Column names
    private static final String COLUMN_SALE_ID = "id";
    private static final String COLUMN_SALE_DAY = "date";
    private static final String COLUMN_SALE_TIME = "time";
    private static final String COLUMN_SALE_STATION = "station";
    private static final String COLUMN_SALE_TYPE = "type";
    private static final String COLUMN_SALE_CATEGORY = "category";
    private static final String COLUMN_SALE_ITEM = "item";
    private static final String COLUMN_SALE_QUANTITY = "quantity";
    private static final String COLUMN_SALE_UNITPRICE = "sellprice";
    private static final String COLUMN_SALE_TOTAL = "total";
    private static final String COLUMN_SALE_PROFIT = "profit";

    // Cart Table Column names
    private static final String COLUMN_CART_ID = "id";
    private static final String COLUMN_CART_STATION = "station";
    private static final String COLUMN_CART_CATEGORY = "category";
    private static final String COLUMN_CART_ITEM = "item";
    private static final String COLUMN_CART_QUANTITY = "quantity";
    private static final String COLUMN_CART_UNITPRICE = "sellprice";
    private static final String COLUMN_CART_BUYPRICE = "buyprice";

    /**
     * ---------------------------------------------------------------------------------------------
     * SQL Queries
     * These is a list of all queries in this program for CREATE TABLE
     * ---------------------------------------------------------------------------------------------
     */

    // create table user sql query
    private final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_USER_PHONE + " TEXT,"
            + COLUMN_USER_TYPE + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT" + ")";

    // create table category sql query
    private final String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,"
            + COLUMN_CATEGORY_NAME + " VARCHAR,"
            + COLUMN_CATEGORY_STATION + " TEXT" + ")";

    // create table sales sql query
    private final String CREATE_SALES_TABLE = "CREATE TABLE " + TABLE_SALES + "("
            + COLUMN_SALE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,"
            + COLUMN_SALE_DAY + " TEXT,"
            + COLUMN_SALE_TIME + " TIME,"
            + COLUMN_SALE_TYPE + " TEXT DEFAULT 'CASH',"
            + COLUMN_SALE_STATION + " TEXT,"
            + COLUMN_SALE_CATEGORY + " TEXT,"
            + COLUMN_SALE_ITEM + " TEXT,"
            + COLUMN_SALE_QUANTITY + " INTEGER,"
            + COLUMN_SALE_UNITPRICE + " INTEGER,"
            + COLUMN_SALE_TOTAL + " INTEGER,"
            + COLUMN_SALE_PROFIT + " INTEGER" + ")";

    // create table sales sql query
    private final String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
            + COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,"
            + COLUMN_CART_STATION + " TEXT,"
            + COLUMN_CART_CATEGORY + " TEXT,"
            + COLUMN_CART_ITEM + " TEXT,"
            + COLUMN_CART_QUANTITY + " INTEGER,"
            + COLUMN_CART_BUYPRICE + " INTEGER,"
            + COLUMN_CART_UNITPRICE + " INTEGER" + ")";

    // create table item sql query
    private final String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
            + COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,"
            + COLUMN_ITEM_STATION + " TEXT,"
            + COLUMN_ITEM_CATEGORY + " TEXT,"
            + COLUMN_ITEM_NAME + " TEXT,"
            + COLUMN_ITEM_QUANTITY + " TEXT,"
            + COLUMN_ITEM_BUYPRICE + " TEXT,"
            + COLUMN_ITEM_SELLPRICE + " TEXT" + ")";

    // create table station sql query
    private final String CREATE_STATION_TABLE = "CREATE TABLE " + TABLE_STATION + "("
            + COLUMN_STATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,"
            + COLUMN_STATION_NAME + " TEXT,"
            + COLUMN_STATION_LOCATION + " TEXT" + ")";

    /**
     * ---------------------------------------------------------------------------------------------
     * A LIST of all "DROP TABLE IF EXIST" Query
     * ---------------------------------------------------------------------------------------------
     */
    // drop table user sql query
    private final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private final String DROP_STATION_TABLE = "DROP TABLE IF EXISTS " + TABLE_STATION;
    private final String DROP_SALES_TABLE = "DROP TABLE IF EXISTS " + TABLE_SALES;
    private final String DROP_CART_TABLE = "DROP TABLE IF EXISTS " + TABLE_CART;
    private final String DROP_ITEM_TABLE = "DROP TABLE IF EXISTS " + TABLE_ITEM;
    private final String DROP_CATEGORY_TABLE = "DROP TABLE IF EXISTS " + TABLE_CATEGORY;


    /**8
     * ---------------------------------------------------------------------------------------------
     * Constructor for databasehelper
     * It's used to switch database based on the context parameter
     * @param context open SQLite database
     * ---------------------------------------------------------------------------------------------
     */
    public Old_DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    /**
     * ---------------------------------------------------------------------------------------------
     * Method for creating the database oce the apk has been launched
     * @param db SQLite database
     * ---------------------------------------------------------------------------------------------
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_SALES_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_STATION_TABLE);
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * Method for applying changes to db on upgrade
     * newVersion is greater than oldVersion
     * @param db SQLite database
     * @param oldVersion current db version
     * @param newVersion next db version
     * ---------------------------------------------------------------------------------------------
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        db.execSQL(DROP_STATION_TABLE);
        db.execSQL(DROP_SALES_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_STATION_TABLE);
        db.execSQL(DROP_ITEM_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(DROP_CART_TABLE);
        db.execSQL(CREATE_CART_TABLE);
         */

    }

    /**
     * ---------------------------------------------------------------------------------------------
     * Method for applying changes to db on downgrade
     * newVersion less than oldVersion
     * @param db SQLite database
     * @param oldVersion current db version
     * @param newVersion next db version
     * ---------------------------------------------------------------------------------------------
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_SALES_TABLE);
        db.execSQL(CREATE_SALES_TABLE);
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * Method to create sale record
     * @param sales Sales model
     * ---------------------------------------------------------------------------------------------
     */
    public void addSale(Sales sales) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SALE_DAY, sales.getDate());
        values.put(COLUMN_SALE_TIME, sales.getTime());
        //values.put(COLUMN_SALE_TYPE, sales.getSaleType());
        values.put(COLUMN_SALE_STATION, sales.getStationName());
        values.put(COLUMN_SALE_CATEGORY, sales.getItemCategory());
        values.put(COLUMN_SALE_ITEM, sales.getItemName());
        values.put(COLUMN_SALE_QUANTITY, sales.getItemQnty());
        values.put(COLUMN_SALE_UNITPRICE, sales.getSellPrice());
        values.put(COLUMN_SALE_TOTAL, sales.getTotal());
        values.put(COLUMN_SALE_PROFIT, sales.getProfit());

        //Insert Row
        db.insert(TABLE_SALES, null, values);
        db.close();
    }
    /**
     *
     * Method to create cart record
     * @param cart Cart model
     * ---------------------------------------------------------------------------------------------
     */
    public void addCart(Cart cart) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_STATION, cart.getStationName());
        values.put(COLUMN_CART_CATEGORY, cart.getCategoryName());
        values.put(COLUMN_CART_ITEM, cart.getItemName());
        values.put(COLUMN_CART_QUANTITY, cart.getItemQnty());
        values.put(COLUMN_CART_UNITPRICE, cart.getSellPrice());
        values.put(COLUMN_CART_BUYPRICE, cart.getBuyPrice());

        //Insert Row
        db.insert(TABLE_CART, null, values);
        db.close();
    }
    /**
     *---------------------------------------------------------------------------------------------
     Method to create sale record
     @param item Item model
      *---------------------------------------------------------------------------------------------
     */
    public void addItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(COLUMN_ITEM_ID, item.getItemId());
        values.put(COLUMN_ITEM_NAME, item.getItemName());
        values.put(COLUMN_ITEM_STATION, item.getStationName());
        values.put(COLUMN_ITEM_CATEGORY, item.getCategoryName());
        values.put(COLUMN_ITEM_QUANTITY, item.getItemQnty());
        values.put(COLUMN_ITEM_SELLPRICE, item.getSellPrice());
        values.put(COLUMN_ITEM_BUYPRICE, item.getBuyPrice());

        //Insert Row
        db.insert(TABLE_ITEM, null, values);
        db.close();
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * Method to create category record
     * @param category Category model
     * ---------------------------------------------------------------------------------------------
     */
    public void addCategory(Category category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(COLUMN_CATEGORY_ID, category.getCategoryId());
        values.put(COLUMN_CATEGORY_NAME, category.getCategoryName());
        values.put(COLUMN_CATEGORY_STATION, category.getStationName());

        //Insert Row
        db.insert(TABLE_CATEGORY,null,values);
        db.close();
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * Method to create category record
     * @param station Station model
     * ---------------------------------------------------------------------------------------------
     */
    public void addStation(Station station){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(COLUMN_STATION_ID, station.getStationId());
        values.put(COLUMN_STATION_NAME, station.getName());
        values.put(COLUMN_STATION_LOCATION, station.getLocation());

        //Insert Row
        db.insert(TABLE_STATION,null,values);
        db.close();
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method is to create user record
     * @param user User model
     * ---------------------------------------------------------------------------------------------
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_USER_EMAIL, user.getUserEmail());
        values.put(COLUMN_USER_PASSWORD, user.getUserPassword());
        values.put(COLUMN_USER_TYPE, user.getUserType());
        values.put(COLUMN_USER_PHONE, user.getUserPhone());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /**
     * method to check if cart table is empty
     * @return false
     */
    public boolean checkIfEmptyCart(String station) {
        String[] columns = {"*"};
        //selection argument
        String[] selectionArgs = {station};
        //selection criteria
        String selection = COLUMN_CART_STATION + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, columns, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            try {
                //if it is empty, returns true.
                cursor.moveToFirst();
                return cursor.getInt(0) == 0;
            } catch(CursorIndexOutOfBoundsException e) {
                return true;
            }
        }
        return false;
    }
    /**
     *
     * fetch all data from cart where station is specified
     * @param station Specified station
     * ---------------------------------------------------------------------------------------------
     **/
    @SuppressLint("Range")
    public List<Cart> getAllCat(String station){
        //Columns to fetch
        String[] columns = {
                COLUMN_CART_STATION,
                COLUMN_CART_CATEGORY,
                COLUMN_CART_ITEM,
                COLUMN_CART_QUANTITY,
                COLUMN_CART_UNITPRICE,
                COLUMN_CART_BUYPRICE
        };
        //selection argument
        String[] selectionArgs = {station};
        //Sorting order
        String sortOder = COLUMN_CART_ID + " ASC";
        //selection criteria
        String selection = COLUMN_CART_STATION + " = ?";
        List<Cart> list = new ArrayList<Cart>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_CART, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);
        //Traversing through all rows and adding to list
        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Cart cart = new Cart();
                    cart.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_CART_STATION)));
                    cart.setCategoryName(cursor.getString(cursor.getColumnIndex(COLUMN_CART_CATEGORY)));
                    cart.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_CART_ITEM)));
                    cart.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CART_QUANTITY))));
                    cart.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CART_UNITPRICE))));
                    cart.setBuyPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CART_BUYPRICE))));
                    list.add(cart);
                }
                while (cursor.moveToNext());
            }
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method for returning category name for the specified station and item names
     * @return category name
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public String getCartCategory(String station, String item){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +COLUMN_ITEM_CATEGORY+" FROM "+TABLE_ITEM+" WHERE "
                +COLUMN_ITEM_STATION+" =? AND "+COLUMN_ITEM_NAME+" =?";
        Cursor cursor = db.rawQuery(query,new String[]{station,item});
        String category = null;
        while (cursor.moveToNext()){
            category = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return category;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method for returning category name for the specified station, category and item names
     * @param item item name
     * @param station station name
     * @param category category name
     * @return selling price
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public int getSellingPrice(String station,String category, String item){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +COLUMN_ITEM_SELLPRICE+" FROM "+TABLE_ITEM+" WHERE "
                +COLUMN_ITEM_STATION+" =? AND "+COLUMN_ITEM_CATEGORY+" =? AND "+COLUMN_ITEM_NAME+" =?";
        Cursor cursor = db.rawQuery(query,new String[]{station,category,item});
        int sellPrice = 0;
        while (cursor.moveToNext()){
            sellPrice = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return sellPrice;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method for returning item quantity for the specified station, category and item names
     * @param item item name
     * @param station station name
     * @param category category name
     * @return selling price
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public int getItemQnty(String station,String category, String item){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +COLUMN_ITEM_QUANTITY+" FROM "+TABLE_ITEM+" WHERE "
                +COLUMN_ITEM_STATION+" =? AND "+COLUMN_ITEM_CATEGORY+" =? AND "+COLUMN_ITEM_NAME+" =?";
        Cursor cursor = db.rawQuery(query,new String[]{station,category,item});
        int qnty = 0;
        while (cursor.moveToNext()){
            qnty = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return qnty;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method for returning category name for the specified station, category and item names
     * @param item item name
     * @param station station name
     * @param category category name
     * @return selling price
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public int getBuyingPrice(String station,String category, String item){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +COLUMN_ITEM_BUYPRICE+" FROM "+TABLE_ITEM+" WHERE "
                +COLUMN_ITEM_STATION+" =? AND "+COLUMN_ITEM_CATEGORY+" =? AND "+COLUMN_ITEM_NAME+" =?";
        Cursor cursor = db.rawQuery(query,new String[]{station,category,item});
        int buyingPrice = 0;
        while (cursor.moveToNext()){
            buyingPrice = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return buyingPrice;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method is to fetch all allsales and return the list of the sale records
     * This is the deafault method as it returns allsale as at today's date
     * @return list
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public List<Sales> getAllSales(){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                null,  //columns for the where clause
                null,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * single date all stations
     * fetch data from all stations where date is specified
     * @param date searchDate
     * ---------------------------------------------------------------------------------------------
     **/
    @SuppressLint("Range")
    public List<Sales> getAllSales(String date){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_DAY + " = ?";
        //selection argument
        String[] selectionArgs = {date};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * single date all station selective item
     * fetch data from all stations where date and category is specified
     * @param date dateSpecified
     * @param item categorySpecified
     * ---------------------------------------------------------------------------------------------
     **/
    @SuppressLint("Range")
    public List<Sales> getAllSalesItem(String date, String item){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_DAY + " = ? AND "+ COLUMN_SALE_ITEM+ " =?";
        //selection argument
        String[] selectionArgs = {date,item};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * single date all station selective category
     * fetch data from all stations where date and category is specified
     * @param date dateSpecified
     * @param category categorySpecified
     * ---------------------------------------------------------------------------------------------
     **/
    @SuppressLint("Range")
    public List<Sales> getAllSalesItemCat(String date, String category){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_DAY + " = ? AND "+ COLUMN_SALE_CATEGORY+ " =?";
        //selection argument
        String[] selectionArgs = {date,category};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * single date selective station
     * fetch data from the station and date specified
     * @param date dateSpecified
     * @param station stationSpecified
     * ---------------------------------------------------------------------------------------------
     **/
    @SuppressLint("Range")
    public List<Sales> getAllSales(String date, String station){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_DAY + " = ? AND " + COLUMN_SALE_STATION + " = ?";
        //selection argument
        String[] selectionArgs = {date,station};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * single date selective station, selective item
     * fetch data from specified station using specified date and item
     * @param station spicificStation
     * @param date specificDate
     * @param item specifiedItem
     * ---------------------------------------------------------------------------------------------
     **/
    @SuppressLint("Range")
    public List<Sales> getAllSales(String date, String station, String item){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_DAY + " = ? AND " + COLUMN_SALE_STATION + " = ? AND " +COLUMN_SALE_ITEM+" =?";
        //selection argument
        String[] selectionArgs = {date,station,item};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * single date selective station, selective item
     * fetch data from specified station using specified date and item
     * @param date specificDate
     * @param category specified category
     * ---------------------------------------------------------------------------------------------
     **/
    @SuppressLint("Range")
    public List<Sales> getAllSalesCat(String date, String category){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_DAY + " = ? AND "+COLUMN_SALE_CATEGORY+" = ?";
        //selection argument
        String[] selectionArgs = {date,category};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * double date all station
     * fetch data from all station between the specified dates
     * @param startDate from date
     * @param endDate to date
     * ---------------------------------------------------------------------------------------------
     **/
    @SuppressLint("Range")
    public List<Sales> getAllSalesBetween(String startDate, String endDate){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_DAY + " >= ? AND " + COLUMN_SALE_DAY + " <= ?";
        //selection argument
        String[] selectionArgs = {startDate,endDate};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * double date all station, selective item
     * fetch data from all station with the specified item between the specified dates
     * @param endDate 'to' date
     * @param startDate 'fro' date
     * @param item specifiedItem
     * ---------------------------------------------------------------------------------------------
     **/
    @SuppressLint("Range")
    public List<Sales> getAllSalesBtwn(String startDate, String endDate, String item){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_DAY + " >= ? AND " + COLUMN_SALE_DAY + " <= ? AND "+COLUMN_SALE_ITEM+" =?";
        //selection argument
        String[] selectionArgs = {startDate,endDate,item};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * double date all station, selective category
     * fetch sold items from all stations with the specified category between the specified dates
     * @param endDate 'to' date
     * @param startDate 'fro' date
     * @param category specifiedItem
     * ---------------------------------------------------------------------------------------------
     **/
    @SuppressLint("Range")
    public List<Sales> getAllSalesBtwnCat(String startDate, String endDate, String category){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_DAY + " >= ? AND " + COLUMN_SALE_DAY + " <= ? AND "+COLUMN_SALE_CATEGORY+" =?";
        //selection argument
        String[] selectionArgs = {startDate,endDate,category};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * double date selective station
     * fetch data from the specifed station and between the specified dates
     * @param startDate fro date
     * @param endDate to date
     * @param station specificStation
     * ---------------------------------------------------------------------------------------------
     **/
    @SuppressLint("Range")
    public List<Sales> getAllSalesBetween(String startDate, String endDate, String station){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_DAY + " >= ? AND " + COLUMN_SALE_DAY + " <= ? AND " + COLUMN_SALE_STATION + " = ?";
        //selection argument
        String[] selectionArgs = {startDate,endDate,station};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * double date selective station, selective item
     * fetch specified item between specified dates and station
     * @param station specificStation
     * @param startDate fro date
     * @param item specificItem
     * @param endDate to date
     * ---------------------------------------------------------------------------------------------
     **/
    @SuppressLint("Range")
    public List<Sales> getAllSalesBetween(String startDate, String endDate, String station, String item){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_DAY + " >= ? AND " + COLUMN_SALE_DAY + " <= ? AND "
                +COLUMN_SALE_STATION+" = ? AND "+ COLUMN_SALE_ITEM+" = ?";
        //selection argument
        String[] selectionArgs = {startDate,endDate,station,item};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    @SuppressLint("Range")
    public List<Sales> getAllSale(String startDate, String station, String cat){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_DAY + " = ? AND "
                +COLUMN_SALE_STATION+" = ? AND "+ COLUMN_SALE_CATEGORY+" = ?";
        //selection argument
        String[] selectionArgs = {startDate,station,cat};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    @SuppressLint("Range")
    public List<Sales> getAllSaleItem(String startDate, String station, String item){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_DAY + " = ? AND "
                +COLUMN_SALE_STATION+" = ? AND "+ COLUMN_SALE_ITEM+" = ?";
        //selection argument
        String[] selectionArgs = {startDate,station,item};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    @SuppressLint("Range")
    public List<Sales> getAllSaleItem(String startDate, String station, String category, String item){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_DAY + " >= ? AND "
                +COLUMN_SALE_STATION+" = ? AND "+COLUMN_SALE_CATEGORY + " = ? AND "+ COLUMN_SALE_ITEM+" = ?";
        //selection argument
        String[] selectionArgs = {startDate,station,category,item};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * double date selective station, selective item
     * fetch specified item between specified dates and station
     * @param station specificStation
     * @param startDate fro date
     * @param item specificItem
     * @param endDate to date
     * ---------------------------------------------------------------------------------------------
     **/
    @SuppressLint("Range")
    public List<Sales> getAllSalesBetween(String startDate, String endDate, String station, String category, String item){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_DAY + " >= ? AND " + COLUMN_SALE_DAY + " <= ? AND "
                +COLUMN_SALE_STATION+" = ? AND "+COLUMN_SALE_CATEGORY+" = ? AND "+ COLUMN_SALE_ITEM+" = ?";
        //selection argument
        String[] selectionArgs = {startDate,endDate,station,category,item};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * Fetch all sales for a particular station
     * @param station the provided station
     * @return arraylist of all sale for @param station
     */
    @SuppressLint("Range")
    public List<Sales> getAllStationSales(String station){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_STATION+" = ?";
        //selection argument
        String[] selectionArgs = {station};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * double date selective station, selective item
     * fetch specified item between specified dates and station
     * @param station specificStation
     * @param startDate fro date
     * @param category specificCategory
     * @param endDate to date
     * ---------------------------------------------------------------------------------------------
     **/
    @SuppressLint("Range")
    public List<Sales> getAllSalesBetweenCat(String startDate, String endDate, String station, String category){
        //Columns to fetch
        String[] columns = {
                COLUMN_SALE_DAY,
                COLUMN_SALE_TIME,
                COLUMN_SALE_TYPE,
                COLUMN_SALE_STATION,
                COLUMN_SALE_CATEGORY,
                COLUMN_SALE_ITEM,
                COLUMN_SALE_QUANTITY,
                COLUMN_SALE_UNITPRICE,
                COLUMN_SALE_TOTAL,
                COLUMN_SALE_PROFIT
        };
        //Sorting order
        String sortOder = COLUMN_SALE_ID + " ASC";
        //selection criteria
        String selection = COLUMN_SALE_DAY + " >= ? AND " + COLUMN_SALE_DAY + " <= ? AND "
                +COLUMN_SALE_STATION+" = ? AND "+ COLUMN_SALE_CATEGORY+" = ?";
        //selection argument
        String[] selectionArgs = {startDate,endDate,station,category};
        List<Sales> list = new ArrayList<Sales>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_SALES, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Sales sale = new Sales();
                sale.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_DAY)));
                sale.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TIME)));
                sale.setSaleType(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TYPE)));
                sale.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_STATION)));
                sale.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_CATEGORY)));
                sale.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_ITEM)));
                sale.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_QUANTITY))));
                sale.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_UNITPRICE))));
                sale.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SALE_TOTAL))));
                sale.setProfit(cursor.getInt(cursor.getColumnIndex(COLUMN_SALE_PROFIT)));
                list.add(sale);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method is to fetch all all items and return the list of the item records
     * This is the deafault method as it returns all items
     * @return list
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public List<Item> getAllItems(){
        //Columns to fetch
        String[] columns = {
                COLUMN_ITEM_STATION,
                COLUMN_ITEM_CATEGORY,
                COLUMN_ITEM_NAME,
                COLUMN_ITEM_QUANTITY,
                COLUMN_ITEM_BUYPRICE,
                COLUMN_ITEM_SELLPRICE
        };
        //Sorting order
        String sortOder = COLUMN_ITEM_ID + " ASC";
        List<Item> list = new ArrayList<Item>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_ITEM, //table to query
                columns,  //columns to return
                null,  //columns for the where clause
                null,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Item par = new Item();
                par.setCategoryName(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_CATEGORY)));
                par.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_STATION)));
                par.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)));
                par.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_QUANTITY))));
                par.setBuyPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_BUYPRICE))));
                par.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_SELLPRICE))));
                list.add(par);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method is to fetch all all items and return the list of the item records
     *
     * @return filtered list as per category and station
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public List<Item> getAllItems(String station, String category){
        //Columns to fetch
        String[] columns = {
                COLUMN_ITEM_STATION,
                COLUMN_ITEM_CATEGORY,
                COLUMN_ITEM_NAME,
                COLUMN_ITEM_QUANTITY,
                COLUMN_ITEM_BUYPRICE,
                COLUMN_ITEM_SELLPRICE
        };
        //Sorting order
        String sortOder = COLUMN_ITEM_ID + " ASC";
        //selection criteria
        String selection = COLUMN_ITEM_STATION + " = ? AND " + COLUMN_ITEM_CATEGORY + " = ?";
        //selection argument
        String[] selectionArgs = {station,category};
        List<Item> list = new ArrayList<Item>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_ITEM, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Item par = new Item();
                par.setCategoryName(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_CATEGORY)));
                par.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_STATION)));
                par.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)));
                par.setItemQnty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_QUANTITY))));
                par.setBuyPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_BUYPRICE))));
                par.setSellPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_SELLPRICE))));
                list.add(par);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method is to fetch all all category and return the list of the category records
     * This is the deafault method as it returns all category
     * @return list
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public List<Category> getAllCategory(){
        //Columns to fetch
        String[] columns = {
                COLUMN_CATEGORY_NAME,
                COLUMN_CATEGORY_STATION
        };
        //Sorting order
        String sortOder = COLUMN_CATEGORY_ID + " ASC";
        List<Category> list = new ArrayList<Category>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_CATEGORY, //table to query
                columns,  //columns to return
                null,  //columns for the where clause
                null,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Category par = new Category();
                par.setCategoryName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
                par.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_STATION)));
                list.add(par);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method is to fetch all all category  for a particular station and return the list of
     * the category records
     * @return category list filtered with station
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public List<Category> getAllCategory(String station){
        //Columns to fetch
        String[] columns = {
                COLUMN_CATEGORY_NAME,
                COLUMN_CATEGORY_STATION
        };
        //Sorting order
        String sortOder = COLUMN_CATEGORY_ID + " ASC";
        //selection criteria
        String selection = COLUMN_CATEGORY_STATION + " = ?";
        //selection argument
        String[] selectionArgs = {station};
        List<Category> list = new ArrayList<Category>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_CATEGORY, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Category par = new Category();
                par.setCategoryName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
                par.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_STATION)));
                list.add(par);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method is to fetch all all station and return the list of the station records
     * This is the deafault method as it returns all station
     * @return list
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public List<Station> getAllStation(){
        //Columns to fetch
        String[] columns = {
                COLUMN_STATION_NAME,
                COLUMN_STATION_LOCATION
        };
        //Sorting order
        String sortOder = COLUMN_STATION_ID + " ASC";
        List<Station> list = new ArrayList<Station>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_STATION, //table to query
                columns,  //columns to return
                null,  //columns for the where clause
                null,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Station par = new Station();
                par.setName(cursor.getString(cursor.getColumnIndex(COLUMN_STATION_NAME)));
                par.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_STATION_LOCATION)));
                list.add(par);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * fetch all station names
     * @return list of all station names
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public List<String> getStation(){
        //Columns to fetch
        String[] columns = {
                COLUMN_STATION_NAME
        };
        //Sorting order
        String sortOder = COLUMN_STATION_ID + " ASC";
        List<String> list = new ArrayList<String>();
        list.add("Select Station");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_STATION, //table to query
                columns,  //columns to return
                null,  //columns for the where clause
                null,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                list.add(cursor.getString(cursor.getColumnIndex(COLUMN_STATION_NAME)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method is to fetch all all user and return the list of the user records
     * This is the deafault method as it returns all user
     * @return list
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public List<User> getAllUser(){
        //Columns to fetch
        String[] columns = {
                COLUMN_USER_NAME,
                COLUMN_USER_TYPE,
                COLUMN_USER_PHONE,
                COLUMN_USER_EMAIL,
                COLUMN_USER_PASSWORD
        };
        //Sorting order
        String sortOder = COLUMN_USER_ID + " ASC";
        List<User> list = new ArrayList<User>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_USER, //table to query
                columns,  //columns to return
                null,  //columns for the where clause
                null,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                User par = new User();
                par.setUserName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                par.setUserType(cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE)));
                par.setUserPhone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
                par.setUserEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                par.setUserPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                list.add(par);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * method to update user record
     * @param user user model
     *
     * ---------------------------------------------------------------------------------------------
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_USER_PHONE, user.getUserPhone());
        values.put(COLUMN_USER_TYPE, user.getUserType());
        values.put(COLUMN_USER_EMAIL, user.getUserEmail());
        values.put(COLUMN_USER_PASSWORD, user.getUserPassword());
        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
        db.close();
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * method to update item record
     * @param par item model
     *
     * ---------------------------------------------------------------------------------------------
     */
    public void updateItem(Item par) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, par.getItemName());
        values.put(COLUMN_ITEM_QUANTITY, par.getItemQnty());
        values.put(COLUMN_ITEM_BUYPRICE, par.getBuyPrice());
        values.put(COLUMN_ITEM_SELLPRICE, par.getSellPrice());
        // updating row
        db.update(TABLE_ITEM, values, COLUMN_ITEM_STATION + " = ? AND "
                        + COLUMN_ITEM_CATEGORY + " = ? AND " + COLUMN_ITEM_NAME + " = ?",
                new String[]{par.getStationName(),par.getCategoryName(),String.valueOf(par.getItemName())});
        db.close();
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * method to update item quantity
     * @param par item model
     *
     * ---------------------------------------------------------------------------------------------
     */
    public void updateItemQnty(Item par) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_QUANTITY, par.getItemQnty());
        // updating row
        db.update(TABLE_ITEM, values, COLUMN_ITEM_STATION + " = ? AND "
                        + COLUMN_ITEM_CATEGORY + " = ? AND " + COLUMN_ITEM_NAME + " = ?",
                new String[]{par.getStationName(),par.getCategoryName(),String.valueOf(par.getItemName())});
        db.close();
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * method to update sale record
     * @param par sale model
     *
     * ---------------------------------------------------------------------------------------------
     */
    public void updateSale(Sales par) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SALE_TYPE, par.getSaleType());

        // updating row
        db.update(TABLE_SALES, values, COLUMN_SALE_ID + " = ?",
                new String[]{String.valueOf(par.getSaleId())});
        db.close();
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * method to update station record
     * @param par station model
     *
     * ---------------------------------------------------------------------------------------------
     */
    public void updateStation(Station par) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATION_LOCATION, par.getLocation());

        // updating row
        db.update(TABLE_STATION, values, COLUMN_STATION_ID + " = ?",
                new String[]{String.valueOf(par.getStationId())});
        db.close();
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * method to update cart quantity
     * @param par cart model
     *
     * ---------------------------------------------------------------------------------------------
     */
    public void updateQuantity(Cart par) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_QUANTITY, par.getItemQnty());

        // updating row
        db.update(TABLE_CART, values, COLUMN_CART_STATION + " = ? AND " + COLUMN_CART_CATEGORY
                        + " = ? AND " +COLUMN_CART_ITEM + " = ?",
                new String[]{par.getStationName(), par.getCategoryName(),par.getItemName()});
        db.close();
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * method to update category record
     * @param par category model
     *
     * ---------------------------------------------------------------------------------------------
     */
    public void updateCategory(Category par) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, par.getCategoryName());

        // updating row
        db.update(TABLE_CATEGORY, values, COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(par.getCategoryId())});
        db.close();
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method is to delete user record
     * @param user
     * ---------------------------------------------------------------------------------------------
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
        db.close();
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method is to delete station record
     * @param par
     * ---------------------------------------------------------------------------------------------
     */
    public void deleteStation(Station par) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete record by id
        db.delete(TABLE_STATION, COLUMN_STATION_ID + " = ?",
                new String[]{String.valueOf(par.getStationId())});
        db.close();
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method is to delete category record
     * @param par attributes of the category model
     * ---------------------------------------------------------------------------------------------
     */
    public void deleteCategory(Category par) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete record by id
        db.delete(TABLE_CATEGORY, COLUMN_CATEGORY_STATION + " = ? AND " + COLUMN_CATEGORY_NAME + " = ?",
                new String[]{par.getStationName(), par.getCategoryName()});
        db.close();
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method is to delete item record
     * @param par attributes of the item model
     * ---------------------------------------------------------------------------------------------
     */
    public void deleteItem(Item par) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete record by id
        db.delete(TABLE_ITEM, COLUMN_ITEM_STATION + " = ? AND " + COLUMN_ITEM_CATEGORY + " = ? AND "
                        + COLUMN_ITEM_NAME + " = ?",
                new String[]{par.getStationName(), par.getCategoryName(), par.getItemName()});
        db.close();
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method is to delete sales record
     * @param par attributes of the sales model
     * ---------------------------------------------------------------------------------------------
     */
    public void deleteSale(Sales par) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete record by id
        db.delete(TABLE_SALES, COLUMN_SALE_ID + " = ?",
                new String[]{String.valueOf(par.getSaleId())});
        db.close();
    }
    /**
     * This method is to delete cart records
     * ---------------------------------------------------------------------------------------------
     */
    public void deleteCart(String station) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete all records
        db.delete(TABLE_CART, COLUMN_CART_STATION + " =?", new String[]{station});
        db.close();
    }
    /**
     * This method is to delete cart records
     * that matches the given parameter
     * @param station name of the station
     * @param category name of the cart category
     * @param item name of the cart item
     * ---------------------------------------------------------------------------------------------
     */
    public void deleteCart(String station, String category, String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete all records
        db.delete(TABLE_CART, COLUMN_CART_STATION + " = ? AND " +COLUMN_CART_CATEGORY
                        + " = ? AND " + COLUMN_CART_ITEM + " = ?",
                new String[]{station,category,item});
        db.close();
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method to check user exist or not
     * @param email
     * @param password
     * @return true/false
     * */
    public boolean checkUser(String userType, String email, String password) {
        //columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_TYPE + " = ? AND "+ COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {userType, email, password};
        // query user table with conditions
        Cursor cursor = db.query(
                TABLE_USER, //Table to query
                columns,  //columns to return
                selection,   //columns for the WHERE clause
                selectionArgs, //The values for the WHERE clause
                null,  //group the rows
                null,  //filter by row groups
                null);  //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    /**
     * Check whether an item already exists
     * @param station
     * @param category
     * @param item
     * @return
     */
    public boolean checkItem(String station, String category, String item) {
        //columns to fetch
        String[] columns = {
                COLUMN_ITEM_NAME
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_ITEM_STATION + " = ? AND "+ COLUMN_ITEM_CATEGORY + " = ? AND "
                + COLUMN_ITEM_NAME + " = ?";
        // selection arguments
        String[] selectionArgs = {station, category, item};
        // query user table with conditions
        Cursor cursor = db.query(
                TABLE_ITEM, //Table to query
                columns,  //columns to return
                selection,   //columns for the WHERE clause
                selectionArgs, //The values for the WHERE clause
                null,  //group the rows
                null,  //filter by row groups
                null);  //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    /**
     * Check whether a category exists
     * @param station
     * @param category
     * @return
     */
    public boolean checkCategory(String station, String category) {
        //columns to fetch
        String[] columns = {
                COLUMN_CATEGORY_NAME
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_CATEGORY_STATION + " = ? AND "+ COLUMN_CATEGORY_NAME + " = ?";
        // selection arguments
        String[] selectionArgs = {station, category};
        // query user table with conditions
        Cursor cursor = db.query(
                TABLE_CATEGORY, //Table to query
                columns,  //columns to return
                selection,   //columns for the WHERE clause
                selectionArgs, //The values for the WHERE clause
                null,  //group the rows
                null,  //filter by row groups
                null);  //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    /**
     * Check whether a station already exists
     * @param station
     * @return
     */
    public boolean checkStation(String station) {
        //columns to fetch
        String[] columns = {
                COLUMN_STATION_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_STATION_NAME + " = ?";
        // selection arguments
        String[] selectionArgs = {station};
        // query user table with conditions
        Cursor cursor = db.query(
                TABLE_STATION, //Table to query
                columns,  //columns to return
                selection,   //columns for the WHERE clause
                selectionArgs, //The values for the WHERE clause
                null,  //group the rows
                null,  //filter by row groups
                null);  //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }
    /**
     * ---------------------------------------------------------------------------------------------
     * This method to check user exist or not
     * @param email
     * @return true/false
     * ---------------------------------------------------------------------------------------------
     * */
    public boolean checkUser(String email) {
        //columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection =COLUMN_USER_EMAIL + " = ?";
        // selection arguments
        String[] selectionArgs = {email};
        // query user table with conditions
        Cursor cursor = db.query(
                TABLE_USER, //Table to query
                columns,  //columns to return
                selection,   //columns for the WHERE clause
                selectionArgs, //The values for the WHERE clause
                null,  //group the rows
                null,  //filter by row groups
                null);  //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    /**
     * method to fetch registered Stations
     * @return no of stations
     */
    public int getStationCount() {
        //columns to fetch
        String[] columns = {"*"};
        SQLiteDatabase db = this.getReadableDatabase();
        // query user table with conditions
        Cursor cursor = db.query(
                TABLE_STATION, columns, null, null, null, null, null);
        // return count
        return cursor.getCount();
    }

    /**
     * method to fetch category on the given station
     * @param station station name
     * @return available category in that station
     */
    public int getCategoryCount(String station) {
        //columns to fetch
        String[] columns = {"*"};
        // selection criteria
        String selection =COLUMN_CATEGORY_STATION + " =?";
        // selection arguments
        String[] selectionArgs = {station};
        SQLiteDatabase db = this.getReadableDatabase();
        // query table with conditions
        Cursor cursor = db.query(
                TABLE_CATEGORY, columns, selection, selectionArgs, null, null, null);
        // return count
        return cursor.getCount();
    }

    /**
     * method to return the no of items from the give station
     * and categry parameters
     * @param station
     * @param category
     * @return no of items
     */
    public int getItemCount(String station, String category) {
        //columns to fetch
        String[] columns = {"*"};
        // selection criteria
        String selection =COLUMN_CATEGORY_STATION + " = ? AND " + COLUMN_CATEGORY_NAME + " = ?";
        // selection arguments
        String[] selectionArgs = {station,category};
        SQLiteDatabase db = this.getReadableDatabase();
        // query table with conditions
        Cursor cursor = db.query(
                TABLE_CATEGORY, columns, selection, selectionArgs, null, null, null);
        // return count
        return cursor.getCount();
    }

    /**
     * fetch the registered number of items for a given category item
     * @param station specific station
     * @param category specific category
     * @return no of registered items for the give category and station
     */
    public int getItemTypesCount(String station, String category) {
        //columns to fetch
        String[] columns = {"*"};
        // selection criteria
        String selection =COLUMN_ITEM_STATION + " = ? AND " + COLUMN_ITEM_CATEGORY + " = ?";
        // selection arguments
        String[] selectionArgs = {station,category};
        SQLiteDatabase db = this.getReadableDatabase();
        // query table with conditions
        Cursor cursor = db.query(
                TABLE_ITEM, columns, selection, selectionArgs, null, null, null);
        // return count
        return cursor.getCount();
    }

    /**
     * Method to return available distinct item qnty from cart table
     * @param station specified station name
     * @param category specified category name
     * @param item specified item name
     * @return item count
     */
    public int getCartQnty(String station, String category, String item) {
        //columns to fetch
        String[] columns = {COLUMN_CART_QUANTITY};
        // selection criteria
        String selection =COLUMN_CART_STATION + " = ? AND " + COLUMN_CART_CATEGORY + " = ? AND " + COLUMN_CART_ITEM + " = ?";
        // selection arguments
        String[] selectionArgs = {station,category,item};
        SQLiteDatabase db = this.getReadableDatabase();
        // query table with conditions
        Cursor cursor = db.query(
                TABLE_CART, columns, selection, selectionArgs, null, null, null);
        // return count
        int qnty = 0;
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                qnty = cursor.getInt(0);
            }
        }
        return qnty;
    }

    /**
     * boolean method to check if an item exists from cart table
     * @param station station name
     * @param category category name
     * @param item item name
     * @return true if exists
     */
    public boolean checkCart(String station,String category,String item) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_CART_ITEM
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_CART_STATION + " = ? AND "+ COLUMN_CART_CATEGORY + " = ? AND "+ COLUMN_CART_ITEM + " = ?";
        // selection argument
        String[] selectionArgs = {station,category,item};
        // query table with condition
        Cursor cursor = db.query(
                TABLE_CART, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount >= 1;
    }
}
