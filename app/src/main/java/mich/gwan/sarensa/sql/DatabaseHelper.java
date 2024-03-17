package mich.gwan.sarensa.sql;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import mich.gwan.sarensa.activities.LoginActivity;
import mich.gwan.sarensa.activities.MainActivity;
import mich.gwan.sarensa.model.Cart;
import mich.gwan.sarensa.model.Category;
import mich.gwan.sarensa.model.Item;
import mich.gwan.sarensa.model.Receipt;
import mich.gwan.sarensa.model.Sales;
import mich.gwan.sarensa.model.Station;
import mich.gwan.sarensa.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "sarensalimited.db";

    // Table names
    private static final String TABLE_USER = "users";
    private static final String TABLE_STATION = "stations";
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_ITEM = "items";
    private static final String TABLE_SALES = "sales";
    private static final String TABLE_CART = "cart";
    private static final String TABLE_RECEIPT = "receipt";

    // Station Table column names
    private static final String COLUMN_STATION_ID = "id";
    private static final String COLUMN_STATION_NAME = "name";
    private static final String COLUMN_STATION_LOCATION = "location";
    private static final String COLUMN_STATION_IDENTIFIER = "identifier";

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
    private static final String COLUMN_SALE_DAY = "_date";
    private static final String COLUMN_SALE_TIME = "time";
    private static final String COLUMN_SALE_STATION = "station";
    private static final String COLUMN_SALE_TYPE = "type";
    private static final String COLUMN_SALE_CATEGORY = "category";
    private static final String COLUMN_SALE_ITEM = "item";
    private static final String COLUMN_SALE_QUANTITY = "quantity";
    private static final String COLUMN_SALE_UNITPRICE = "sellprice";
    private static final String COLUMN_SALE_TOTAL = "total";
    private static final String COLUMN_SALE_PROFIT = "profit";

    // Receipts Table Column
    private static final String COLUMN_RECEIPT_ID = "receipt_id";
    private static final String COLUMN_RECEIPT_DATE = "_date";
    private static final String COLUMN_RECEIPT_TIME = "receipt_time";
    private static final String COLUMN_RECEIPT_TYPE = "receipt_type";
    private static final String COLUMN_RECEIPT_STATION = "receipt_station";
    private static final String COLUMN_RECEIPT_CATEGORY = "receipt_category";
    private static final String COLUMN_RECEIPT_ITEM = "receipt_item";
    private static final String COLUMN_RECEIPT_CUSTOMER = "receipt_customer";
    private static final String COLUMN_RECEIPT_QUANTITY = "receipt_quantity";
    private static final String COLUMN_RECEIPT_PRICE = "receipt_price";
    private static final String COLUMN_RECEIPT_TOTAL = "receipt_total";

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
            + COLUMN_CATEGORY_STATION + " TEXT,"
            + "FOREIGN KEY(" +COLUMN_CATEGORY_STATION+ ") REFERENCES " +TABLE_STATION+ "(" +COLUMN_STATION_NAME+ ") ON DELETE CASCADE ON UPDATE CASCADE" + ")";

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
            + COLUMN_SALE_PROFIT + " INTEGER,"
            + "FOREIGN KEY(" +COLUMN_SALE_CATEGORY+ ") REFERENCES " +TABLE_CATEGORY+ "(" +COLUMN_CATEGORY_NAME+ ") ON DELETE CASCADE ON UPDATE CASCADE,"
            + "FOREIGN KEY(" +COLUMN_SALE_ITEM+ ") REFERENCES " +TABLE_ITEM+ "(" +COLUMN_ITEM_NAME+ ") ON DELETE CASCADE ON UPDATE CASCADE,"
            + "FOREIGN KEY(" +COLUMN_SALE_STATION+ ") REFERENCES " +TABLE_STATION+ "(" +COLUMN_STATION_NAME+ ") ON DELETE CASCADE ON UPDATE CASCADE" + ")";

    // create table sales sql query
    private final String CREATE_RECEIPT_TABLE = "CREATE TABLE " + TABLE_RECEIPT + "("
            + COLUMN_RECEIPT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,"
            + COLUMN_RECEIPT_DATE + " DATE,"
            + COLUMN_RECEIPT_TIME + " TIME,"
            + COLUMN_RECEIPT_TYPE + " VARCHAR(10) DEFAULT 'CASH',"
            + COLUMN_RECEIPT_CUSTOMER + " VARCHAR(20),"
            + COLUMN_RECEIPT_STATION + " INTEGER,"
            + COLUMN_RECEIPT_CATEGORY + " INTEGER,"
            + COLUMN_RECEIPT_ITEM + " INTEGER,"
            + COLUMN_RECEIPT_QUANTITY + " INTEGER,"
            + COLUMN_RECEIPT_PRICE + " INTEGER,"
            + COLUMN_RECEIPT_TOTAL + " INTEGER,"
            + "FOREIGN KEY(" +COLUMN_RECEIPT_CATEGORY+ ") REFERENCES " +TABLE_CATEGORY+ "(" +COLUMN_CATEGORY_NAME+ ") ON DELETE CASCADE ON UPDATE CASCADE,"
            + "FOREIGN KEY(" +COLUMN_RECEIPT_ITEM+ ") REFERENCES " +TABLE_ITEM+ "(" +COLUMN_ITEM_NAME+ ") ON DELETE CASCADE ON UPDATE CASCADE,"
            + "FOREIGN KEY(" +COLUMN_RECEIPT_STATION+ ") REFERENCES " +TABLE_STATION+ "(" +COLUMN_STATION_NAME+ ") ON DELETE CASCADE ON UPDATE CASCADE" + ")";

    // create table sales sql query
    private final String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
            + COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,"
            + COLUMN_CART_STATION + " TEXT,"
            + COLUMN_CART_CATEGORY + " TEXT,"
            + COLUMN_CART_ITEM + " TEXT,"
            + COLUMN_CART_QUANTITY + " INTEGER,"
            + COLUMN_CART_BUYPRICE + " INTEGER,"
            + COLUMN_CART_UNITPRICE + " INTEGER,"
            + "FOREIGN KEY(" +COLUMN_CART_CATEGORY+ ") REFERENCES " +TABLE_CATEGORY+ "(" +COLUMN_CATEGORY_NAME+ ") ON DELETE CASCADE ON UPDATE CASCADE,"
            + "FOREIGN KEY(" +COLUMN_CART_ITEM+ ") REFERENCES " +TABLE_ITEM+ "(" +COLUMN_ITEM_NAME+ ") ON DELETE CASCADE ON UPDATE CASCADE,"
            + "FOREIGN KEY(" +COLUMN_CART_STATION+ ") REFERENCES " +TABLE_STATION+ "(" +COLUMN_STATION_NAME+ ") ON DELETE CASCADE ON UPDATE CASCADE" + ")";

    // create table item sql query
    private final String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
            + COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,"
            + COLUMN_ITEM_STATION + " TEXT,"
            + COLUMN_ITEM_CATEGORY + " TEXT,"
            + COLUMN_ITEM_NAME + " TEXT,"
            + COLUMN_ITEM_QUANTITY + " TEXT,"
            + COLUMN_ITEM_BUYPRICE + " TEXT,"
            + COLUMN_ITEM_SELLPRICE + " TEXT,"
            + "FOREIGN KEY(" +COLUMN_ITEM_CATEGORY+ ") REFERENCES " +TABLE_CATEGORY+ "(" +COLUMN_CATEGORY_NAME+ ") ON DELETE CASCADE ON UPDATE CASCADE,"
            + "FOREIGN KEY(" +COLUMN_ITEM_STATION+ ") REFERENCES " +TABLE_STATION+ "(" +COLUMN_STATION_NAME+ ") ON DELETE CASCADE ON UPDATE CASCADE" + ")";

// create table station sql query
    private final String CREATE_STATION_TABLE = "CREATE TABLE " + TABLE_STATION + "("
            + COLUMN_STATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,"
            + COLUMN_STATION_NAME + " TEXT,"
            + COLUMN_STATION_IDENTIFIER + " TEXT,"
            + COLUMN_STATION_LOCATION + " TEXT" + ")";

    private final String INSERT_STATION = "INSERT INTO "+TABLE_STATION+ " " +
            "("+COLUMN_STATION_ID + ","+ COLUMN_STATION_NAME + ","+COLUMN_STATION_IDENTIFIER+","+ COLUMN_STATION_LOCATION + ") " +
            "VALUES( 1, 'KATHARAKA', 'KATHARAKA', 'KATHARAKA'),(2, 'KITHIMU AUTO', 'KITHIMU AUTO', 'KITHIMU - EMBU'),(3, 'IRIGA', 'IRIGA', 'IRIGA'),(4, 'BARAKA', 'BARAKA', 'BARAKA')," +
            "(5, 'NGUSISHI', 'NGUSISHI', 'NGUSISHI'),(6, 'IGOJI', 'IGOJI', 'IGOJI'),(7, 'KINANA', 'KINANA', 'KINANA'),(8,'KITHIMU','KITHIMU','KITHIMU - EMBU');";

    private final String INSERT_CATEGORY = "INSERT INTO "+TABLE_CATEGORY+" " +
            "("+COLUMN_CATEGORY_ID+","+COLUMN_CATEGORY_NAME+","+COLUMN_CATEGORY_STATION+") VALUES" +
            "(1, 'BOLTS', 'KATHARAKA')," +
            "(9, 'OILS', 'KATHARAKA')," +
            "(17, 'SPARES', 'KATHARAKA')," +
            "(2, 'BOLT/SPANNER', 'KITHIMU AUTO')," +
            "(11, 'OILS', 'KITHIMU AUTO')," +
            "(16, 'SPARES', 'KITHIMU AUTO')," +
            "(20, 'PWS PARTS', 'KITHIMU AUTO')," +
            "(3, 'BOLTS', 'NGUSISHI')," +
            "(13, 'OILS', 'NGUSISHI')," +
            "(19, 'SPARES', 'NGUSISHI')," +
            "(4, 'BOLT/SPANNER', 'IRIGA')," +
            "(8, 'OILS', 'IRIGA')," +
            "(15, 'SPARES', 'IRIGA')," +
            "(21, 'PWS PARTS', 'IRIGA')," +
            "(5, 'BOLT/SPANNER', 'BARAKA')," +
            "(6, 'OILS', 'BARAKA')," +
            "(14, 'SPARES', 'BARAKA')," +
            "(7, 'OILS', 'IGOJI')," +
            "(10, 'OILS', 'KINANA')," +
            "(12,'OILS','KITHIMU')";

    private final String INSERT_ITEM = "INSERT INTO "+TABLE_ITEM+" " +
            "("+ COLUMN_ITEM_ID +","+ COLUMN_ITEM_STATION + ","+ COLUMN_ITEM_CATEGORY +","+ COLUMN_ITEM_NAME + ","+
            COLUMN_ITEM_QUANTITY +","+ COLUMN_ITEM_BUYPRICE + ","+ COLUMN_ITEM_SELLPRICE + ") VALUES" +
            "(1, 'IGOJI', 'OILS', 'ADVANCE AX3', 10000000, 376, 450)," +
            "(2, 'IGOJI', 'OILS', 'ADVANCE AX5', 10000000, 431, 480)," +
            "(3, 'IGOJI', 'OILS', 'ATF/CC', 10000000, 165, 180)," +
            "(4, 'IGOJI', 'OILS', 'ATF/CC L', 10000000, 325, 400)," +
            "(5, 'IGOJI', 'OILS', 'B.ACID', 10000000, 55, 100)," +
            "(6, 'IGOJI', 'OILS', 'B.WATER - CE', 10000000, 50, 80)," +
            "(7, 'IGOJI', 'OILS', 'DOT 4', 10000000, 219, 250)," +
            "(8, 'IGOJI', 'OILS', 'DOT 4 ½', 10000000, 285, 350)," +
            "(9, 'IGOJI', 'OILS', 'HELIX ½', 10000000, 200, 240)," +
            "(10, 'IGOJI', 'OILS', 'HELIX L', 10000000, 376, 450)," +
            "(11, 'IGOJI', 'OILS', 'K-LUBE', 10000000, 187, 220)," +
            "(12, 'IGOJI', 'OILS', 'LACHEKA ½', 10000000, 135, 160)," +
            "(13, 'IGOJI', 'OILS', 'LACHEKA L', 10000000, 285, 300)," +
            "(14, 'IGOJI', 'OILS', 'P.SAFARI ½', 10000000, 200, 240)," +
            "(15, 'IGOJI', 'OILS', 'P.SAFARI L', 10000000, 332, 450)," +
            "(16, 'IGOJI', 'OILS', 'T.CLASSIC ½', 10000000, 217, 240)," +
            "(17, 'IGOJI', 'OILS', 'T.CLASSIC L', 10000000, 410, 450)," +
            "(20, 'KINANA', 'OILS', 'K-LUBE', 10000000, 200, 220)," +
            "(21, 'KATHARAKA', 'SPARES', '6001 BEARING', 10000000, 50, 100)," +
            "(22, 'KATHARAKA', 'SPARES', '6004 BEARING', 10000000, 65, 100)," +
            "(23, 'KATHARAKA', 'SPARES', '6202 BEARING', 10000000, 50, 100)," +
            "(24, 'KATHARAKA', 'SPARES', '6301 BEARING', 10000000, 60, 100)," +
            "(25, 'KATHARAKA', 'SPARES', '6302 BEARING', 10000000, 60, 100)," +
            "(26, 'KATHARAKA', 'SPARES', '6328 BEARING', 10000000, 180, 250)," +
            "(27, 'KATHARAKA', 'SPARES', 'ACC CABLE', 10000000, 65, 120)," +
            "(28, 'KATHARAKA', 'SPARES', 'BATTERY WIRE', 10000000, 20, 30)," +
            "(29, 'KATHARAKA', 'SPARES', 'BRAKE ARM', 10000000, 100, 150)," +
            "(30, 'KATHARAKA', 'SPARES', 'BRAKE CABLE', 10000000, 80, 100)," +
            "(31, 'KATHARAKA', 'SPARES', 'BRAKE PEDAL', 10000000, 200, 250)," +
            "(32, 'KATHARAKA', 'SPARES', 'BRAKE ROD', 10000000, 50, 80)," +
            "(33, 'KATHARAKA', 'SPARES', 'BRAKE SHOE', 10000000, 200, 250)," +
            "(34, 'KATHARAKA', 'SPARES', 'BRAKE SWITCH', 10000000, 20, 30)," +
            "(35, 'KATHARAKA', 'SPARES', 'BULB HOLDER - FRONT', 10000000, 50, 100)," +
            "(36, 'KATHARAKA', 'SPARES', 'BULB HOLDER - REAR', 10000000, 30, 50)," +
            "(37, 'KATHARAKA', 'SPARES', 'BULL BAR CLAMPS', 10000000, 80, 100)," +
            "(38, 'KATHARAKA', 'SPARES', 'BULL BAR U – CLAMPS', 10000000, 30, 50)," +
            "(39, 'KATHARAKA', 'SPARES', 'CELLONOID', 10000000, 160, 200)," +
            "(40, 'KATHARAKA', 'SPARES', 'CENTRE AXLE', 10000000, 90, 120)," +
            "(41, 'KATHARAKA', 'SPARES', 'CENTRE BOLT', 10000000, 130, 170)," +
            "(42, 'KATHARAKA', 'SPARES', 'CHAIN CPTN', 10000000, 420, 500)," +
            "(43, 'KATHARAKA', 'SPARES', 'CHAIN ADJUSTER', 10000000, 100, 125)," +
            "(44, 'KATHARAKA', 'SPARES', 'CHAIN LOCK', 10000000, 20, 30)," +
            "(45, 'KATHARAKA', 'SPARES', 'CHEST GUARD', 10000000, 400, 500)," +
            "(46, 'KATHARAKA', 'SPARES', 'CLUTCH CABLE', 10000000, 80, 120)," +
            "(47, 'KATHARAKA', 'SPARES', 'CLUTCH DISC', 10000000, 200, 250)," +
            "(48, 'KATHARAKA', 'SPARES', 'CLUTCH PLATE', 10000000, 140, 250)," +
            "(49, 'KATHARAKA', 'SPARES', 'CLUTCH RELEASER', 10000000, 90, 150)," +
            "(50, 'KATHARAKA', 'SPARES', 'ENGINE ROCK ARMS', 10000000, 180, 250)," +
            "(51, 'KATHARAKA', 'SPARES', 'EXHAUST CLAMPS', 10000000, 80, 100)," +
            "(52, 'KATHARAKA', 'SPARES', 'FLASH UNIT', 10000000, 50, 100)," +
            "(53, 'KATHARAKA', 'SPARES', 'FOOT REST', 10000000, 450, 500)," +
            "(54, 'KATHARAKA', 'SPARES', 'FRONT AXLE', 10000000, 80, 120)," +
            "(55, 'KATHARAKA', 'SPARES', 'FUSE BOX', 10000000, 35, 50)," +
            "(56, 'KATHARAKA', 'SPARES', 'GASKET - BLOCK', 10000000, 30, 50)," +
            "(57, 'KATHARAKA', 'SPARES', 'GASKET - CLUTCH', 10000000, 30, 50)," +
            "(58, 'KATHARAKA', 'SPARES', 'GASKET - EXHAUST', 10000000, 20, 50)," +
            "(59, 'KATHARAKA', 'SPARES', 'GASKET - MAGNETO', 10000000, 25, 50)," +
            "(60, 'KATHARAKA', 'SPARES', 'GASKET SET', 10000000, 180, 220)," +
            "(61, 'KATHARAKA', 'SPARES', 'GEAR LEVER', 10000000, 120, 200)," +
            "(62, 'KATHARAKA', 'SPARES', 'GEAR NO.3', 10000000, 280, 350)," +
            "(63, 'KATHARAKA', 'SPARES', 'GEAR SELECTOR', 10000000, 180, 250)," +
            "(64, 'KATHARAKA', 'SPARES', 'GEAR SENSOR', 10000000, 120, 180)," +
            "(65, 'KATHARAKA', 'SPARES', 'GLOVES', 10000000, 180, 250)," +
            "(66, 'KATHARAKA', 'SPARES', 'HALOGEN BULB', 10000000, 60, 100)," +
            "(67, 'KATHARAKA', 'SPARES', 'HAND GRIP', 10000000, 50, 100)," +
            "(68, 'KATHARAKA', 'SPARES', 'HANDLE LEVER', 10000000, 80, 100)," +
            "(69, 'KATHARAKA', 'SPARES', 'HANDLE SWITCH ', 10000000, 260, 300)," +
            "(70, 'KATHARAKA', 'SPARES', 'HORN - PARARIRA', 10000000, 450, 600)," +
            "(71, 'KATHARAKA', 'SPARES', 'HORN – SMALL', 10000000, 80, 100)," +
            "(72, 'KATHARAKA', 'SPARES', 'HORN - SNAIL', 10000000, 250, 350)," +
            "(73, 'KATHARAKA', 'SPARES', 'IGNITION COIL', 10000000, 160, 200)," +
            "(74, 'KATHARAKA', 'SPARES', 'IGNITION COVER', 10000000, 50, 80)," +
            "(75, 'KATHARAKA', 'SPARES', 'IGNITION SWITCH', 10000000, 200, 250)," +
            "(76, 'KATHARAKA', 'SPARES', 'INDICATOR BULB', 10000000, 8, 20)," +
            "(77, 'KATHARAKA', 'SPARES', 'INNER CLUTCH CABLE', 10000000, 25, 50)," +
            "(78, 'KATHARAKA', 'SPARES', 'INSULATION TAPE', 10000000, 25, 30)," +
            "(79, 'KATHARAKA', 'SPARES', 'INSURANCE HOLDER', 10000000, 25, 50)," +
            "(80, 'KATHARAKA', 'SPARES', 'KICK START', 10000000, 200, 250)," +
            "(81, 'KATHARAKA', 'SPARES', 'KICK START SHAFT', 10000000, 380, 500)," +
            "(82, 'KATHARAKA', 'SPARES', 'LED 2 EYES', 10000000, 200, 300)," +
            "(83, 'KATHARAKA', 'SPARES', 'LED 3 EYES', 10000000, 50, 100)," +
            "(84, 'KATHARAKA', 'SPARES', 'LED 5 EYES', 10000000, 80, 120)," +
            "(85, 'KATHARAKA', 'SPARES', 'LED FLAT', 10000000, 60, 100)," +
            "(86, 'KATHARAKA', 'SPARES', 'LED SWEET', 10000000, 60, 100)," +
            "(87, 'KATHARAKA', 'SPARES', 'MAGNETO ROLLERS', 10000000, 85, 150)," +
            "(88, 'KATHARAKA', 'SPARES', 'MAIN STAND', 10000000, 400, 500)," +
            "(89, 'KATHARAKA', 'SPARES', 'MAIN STAND AXLE', 10000000, 100, 150)," +
            "(90, 'KATHARAKA', 'SPARES', 'MIRROR HOLDER', 10000000, 80, 100)," +
            "(91, 'KATHARAKA', 'SPARES', 'OIL CAP', 10000000, 130, 200)," +
            "(92, 'KATHARAKA', 'SPARES', 'OIL FILTER', 10000000, 100, 150)," +
            "(93, 'KATHARAKA', 'SPARES', 'OIL PUMP – SMALL', 10000000, 180, 220)," +
            "(94, 'KATHARAKA', 'SPARES', 'OIL SEAL 28', 10000000, 25, 50)," +
            "(95, 'KATHARAKA', 'SPARES', 'OIL SEAL 34', 10000000, 30, 50)," +
            "(96, 'KATHARAKA', 'SPARES', 'PISTON KIT', 10000000, 480, 550)," +
            "(97, 'KATHARAKA', 'SPARES', 'PISTON RINGS', 10000000, 180, 250)," +
            "(98, 'KATHARAKA', 'SPARES', 'PLUG', 10000000, 50, 100)," +
            "(99, 'KATHARAKA', 'SPARES', 'PLUG CAP', 10000000, 30, 50)," +
            "(100, 'KATHARAKA', 'SPARES', 'REAR AXLE', 10000000, 80, 150)," +
            "(101, 'KATHARAKA', 'SPARES', 'SHOCK BOOT RUBBER', 10000000, 60, 100)," +
            "(102, 'KATHARAKA', 'SPARES', 'SHOCK SEAL', 10000000, 25, 50)," +
            "(103, 'KATHARAKA', 'SPARES', 'SIDE COVER LOCK', 10000000, 34, 50)," +
            "(104, 'KATHARAKA', 'SPARES', 'SIDE MIRROR - BLACK', 10000000, 100, 120)," +
            "(105, 'KATHARAKA', 'SPARES', 'SIDE MIRROR - BM150', 10000000, 125, 150)," +
            "(106, 'KATHARAKA', 'SPARES', 'SIDE MIRROR - CHROME', 10000000, 125, 175)," +
            "(107, 'KATHARAKA', 'SPARES', 'SILICONE', 10000000, 45, 60)," +
            "(108, 'KATHARAKA', 'SPARES', 'SPEED CABLE', 10000000, 60, 100)," +
            "(109, 'KATHARAKA', 'SPARES', 'SPRINGS – BRAKE', 10000000, 15, 30)," +
            "(110, 'KATHARAKA', 'SPARES', 'SPRINGS - MAIN', 10000000, 25, 50)," +
            "(111, 'KATHARAKA', 'SPARES', 'SPROCKET - REAR', 10000000, 280, 350)," +
            "(112, 'KATHARAKA', 'SPARES', 'SPROCKET SHAFT', 10000000, 180, 250)," +
            "(113, 'KATHARAKA', 'SPARES', 'SPROCKET SITTING', 10000000, 300, 400)," +
            "(114, 'KATHARAKA', 'SPARES', 'SPROCKET STANDS', 10000000, 50, 100)," +
            "(115, 'KATHARAKA', 'SPARES', 'STAND HOOK', 10000000, 15, 50)," +
            "(116, 'KATHARAKA', 'SPARES', 'STEERING LOCK', 10000000, 40, 70)," +
            "(117, 'KATHARAKA', 'SPARES', 'SUPER GLUE', 10000000, 18, 30)," +
            "(118, 'KATHARAKA', 'SPARES', 'TAIL LAMP BULB', 10000000, 12, 30)," +
            "(119, 'KATHARAKA', 'SPARES', 'TOP BRIDGE', 10000000, 400, 500)," +
            "(120, 'KATHARAKA', 'SPARES', 'TUBE - MTR', 10000000, 280, 330)," +
            "(121, 'KATHARAKA', 'SPARES', 'TUPPET – SMALL', 10000000, 280, 400)," +
            "(122, 'KATHARAKA', 'SPARES', 'TYRE 3.00 - PRODRIVE', 10000000, 1550, 2000)," +
            "(123, 'KATHARAKA', 'SPARES', 'VALVE GUIDE', 10000000, 180, 250)," +
            "(124, 'KATHARAKA', 'SPARES', 'VALVE SEAL', 10000000, 20, 50)," +
            "(125, 'KATHARAKA', 'SPARES', 'VALVE SET', 10000000, 200, 250)," +
            "(126, 'KATHARAKA', 'SPARES', 'WORLD CLASS AB', 10000000, 80, 100)," +
            "(127, 'KATHARAKA', 'BOLTS', '10 X 1.5', 10000000, 5, 10)," +
            "(128, 'KATHARAKA', 'BOLTS', '10 X 2', 10000000, 6, 20)," +
            "(129, 'KATHARAKA', 'BOLTS', '10 X 3', 10000000, 10, 20)," +
            "(130, 'KATHARAKA', 'BOLTS', '13 X 1.5', 10000000, 10, 20)," +
            "(131, 'KATHARAKA', 'BOLTS', '13 X 3', 10000000, 17, 30)," +
            "(132, 'KATHARAKA', 'BOLTS', '13 X 4', 10000000, 20, 30)," +
            "(133, 'KATHARAKA', 'BOLTS', '14 X 1', 10000000, 10, 20)," +
            "(134, 'KATHARAKA', 'BOLTS', '14 X 1.5', 10000000, 10, 20)," +
            "(135, 'KATHARAKA', 'BOLTS', 'WASHER', 10000000, 2, 5)," +
            "(136, 'KATHARAKA', 'OILS', 'ADVANCE AX3', 10000000, 427, 450)," +
            "(137, 'KATHARAKA', 'OILS', 'ADVANCE AX5', 10000000, 456, 500)," +
            "(138, 'KATHARAKA', 'OILS', 'ATF', 10000000, 150, 180)," +
            "(139, 'KATHARAKA', 'OILS', 'B.WATER – CE', 10000000, 60, 80)," +
            "(140, 'KATHARAKA', 'OILS', 'BITUL', 10000000, 280, 330)," +
            "(141, 'KATHARAKA', 'OILS', 'CC ½ - OG', 10000000, 165, 200)," +
            "(142, 'KATHARAKA', 'OILS', 'DOT 4 - KOBIL', 10000000, 220, 250)," +
            "(143, 'KATHARAKA', 'OILS', 'DOT 4 - OG', 10000000, 115, 150)," +
            "(144, 'KATHARAKA', 'OILS', 'DOT 4 ½ OG', 10000000, 230, 300)," +
            "(145, 'KATHARAKA', 'OILS', 'GREASE', 10000000, 205, 300)," +
            "(146, 'KATHARAKA', 'OILS', 'HELIX ½', 10000000, 210, 250)," +
            "(147, 'KATHARAKA', 'OILS', 'HELIX L', 10000000, 392, 450)," +
            "(148, 'KATHARAKA', 'OILS', 'K-LUBE', 10000000, 190, 220)," +
            "(149, 'KATHARAKA', 'OILS', 'LACHEKA 2T ½', 10000000, 145, 160)," +
            "(150, 'KATHARAKA', 'OILS', 'LACHEKA PE L', 10000000, 285, 300)," +
            "(151, 'KATHARAKA', 'OILS', 'P.SAFARI ½', 10000000, 210, 250)," +
            "(152, 'KATHARAKA', 'OILS', 'P.SAFARI L', 10000000, 332, 400)," +
            "(153, 'BARAKA', 'SPARES', '1512 BEARING', 10000000, 80, 150)," +
            "(154, 'BARAKA', 'SPARES', '6001 BEARING', 10000000, 50, 100)," +
            "(155, 'BARAKA', 'SPARES', '6004 BEARING', 10000000, 75, 100)," +
            "(156, 'BARAKA', 'SPARES', '6200 BEARING', 10000000, 50, 100)," +
            "(157, 'BARAKA', 'SPARES', '6202 BEARING', 10000000, 50, 100)," +
            "(158, 'BARAKA', 'SPARES', '6203 BEARING', 10000000, 55, 100)," +
            "(159, 'BARAKA', 'SPARES', '6204 BEARING', 10000000, 80, 150)," +
            "(160, 'BARAKA', 'SPARES', '6300 BEARING', 10000000, 50, 100)," +
            "(161, 'BARAKA', 'SPARES', '6301 BEARING', 10000000, 70, 100)," +
            "(162, 'BARAKA', 'SPARES', '6302 BEARING', 10000000, 70, 100)," +
            "(163, 'BARAKA', 'SPARES', '6306 BEARING', 10000000, 400, 500)," +
            "(164, 'BARAKA', 'SPARES', 'ACC CABLE', 10000000, 110, 150)," +
            "(165, 'BARAKA', 'SPARES', 'AIR CLEANER', 10000000, 850, 1000)," +
            "(166, 'BARAKA', 'SPARES', 'AIR CLEANER JOINT/PIPE', 10000000, 100, 150)," +
            "(167, 'BARAKA', 'SPARES', 'ARM BUSH', 10000000, 50, 100)," +
            "(168, 'BARAKA', 'SPARES', 'B.B PLASTIC SET - ORDINARY', 10000000, 450, 600)," +
            "(169, 'BARAKA', 'SPARES', 'B.B PLASTIC SET - ORIGINAL', 10000000, 800, 1000)," +
            "(170, 'BARAKA', 'SPARES', 'BALL RACE SET', 10000000, 130, 250)," +
            "(171, 'BARAKA', 'SPARES', 'BATTERY N7', 10000000, 1350, 1500)," +
            "(172, 'BARAKA', 'SPARES', 'BATTERY N9', 10000000, 1400, 1700)," +
            "(173, 'BARAKA', 'SPARES', 'BATTERY STRIP', 10000000, 30, 50)," +
            "(174, 'BARAKA', 'SPARES', 'BATTERY WIRE', 10000000, 20, 30)," +
            "(175, 'BARAKA', 'SPARES', 'BLOCK SET', 10000000, 1800, 2000)," +
            "(176, 'BARAKA', 'SPARES', 'BLOCK SET 200', 10000000, 2500, 2850)," +
            "(177, 'BARAKA', 'SPARES', 'BRAKE ARM', 10000000, 110, 150)," +
            "(178, 'BARAKA', 'SPARES', 'BRAKE CABLE', 10000000, 70, 150)," +
            "(179, 'BARAKA', 'SPARES', 'BRAKE PEDAL – BOXER', 10000000, 280, 350)," +
            "(180, 'BARAKA', 'SPARES', 'BRAKE PEDAL', 10000000, 200, 250)," +
            "(181, 'BARAKA', 'SPARES', 'BRAKE PEDAL SHOE', 10000000, 150, 200)," +
            "(182, 'BARAKA', 'SPARES', 'BRAKE ROD', 10000000, 55, 100)," +
            "(183, 'BARAKA', 'SPARES', 'BRAKE SHOE', 10000000, 190, 250)," +
            "(184, 'BARAKA', 'SPARES', 'BRAKE SWITCH', 10000000, 30, 50)," +
            "(185, 'BARAKA', 'SPARES', 'BULB HOLDER - FRONT', 10000000, 50, 100)," +
            "(186, 'BARAKA', 'SPARES', 'BULB HOLDER - HEADLIGHT', 10000000, 115, 150)," +
            "(187, 'BARAKA', 'SPARES', 'BULB HOLDER -REAR', 10000000, 60, 100)," +
            "(188, 'BARAKA', 'SPARES', 'BULL BAR CLAMPS', 10000000, 80, 100)," +
            "(189, 'BARAKA', 'SPARES', 'BULL BAR SET', 10000000, 1750, 2000)," +
            "(190, 'BARAKA', 'SPARES', 'CAMSHAFT', 10000000, 450, 600)," +
            "(191, 'BARAKA', 'SPARES', 'CAP TAPPET', 10000000, 50, 100)," +
            "(192, 'BARAKA', 'SPARES', 'CARBURATOR', 10000000, 700, 900)," +
            "(193, 'BARAKA', 'SPARES', 'CARBURATOR JOINT', 10000000, 90, 150)," +
            "(194, 'BARAKA', 'SPARES', 'CARRIER', 10000000, 3500, 4000)," +
            "(195, 'BARAKA', 'SPARES', 'CDI PRO', 10000000, 240, 300)," +
            "(196, 'BARAKA', 'SPARES', 'CELLONOID', 10000000, 200, 300)," +
            "(197, 'BARAKA', 'SPARES', 'CENTRE AXLE', 10000000, 80, 150)," +
            "(198, 'BARAKA', 'SPARES', 'CENTRE BOLT', 10000000, 140, 200)," +
            "(199, 'BARAKA', 'SPARES', 'CHAIN', 10000000, 370, 450)," +
            "(200, 'BARAKA', 'SPARES', 'CHAIN ADJUSTER', 10000000, 100, 150)," +
            "(201, 'BARAKA', 'SPARES', 'CHAIN KIT', 10000000, 1030, 1200)," +
            "(202, 'BARAKA', 'SPARES', 'CHAIN LOCK', 10000000, 25, 50)," +
            "(203, 'BARAKA', 'SPARES', 'CLUTCH 5H', 10000000, 600, 700)," +
            "(204, 'BARAKA', 'SPARES', 'CLUTCH 6H', 10000000, 710, 900)," +
            "(205, 'BARAKA', 'SPARES', 'CLUTCH BOX', 10000000, 440, 700)," +
            "(206, 'BARAKA', 'SPARES', 'CLUTCH CABLE', 10000000, 125, 150)," +
            "(207, 'BARAKA', 'SPARES', 'CLUTCH DISC', 10000000, 200, 300)," +
            "(208, 'BARAKA', 'SPARES', 'CLUTCH PLATE', 10000000, 170, 300)," +
            "(209, 'BARAKA', 'SPARES', 'CLUTCH RELEASER', 10000000, 110, 150)," +
            "(210, 'BARAKA', 'SPARES', 'CRANKCASE', 10000000, 1750, 2200)," +
            "(211, 'BARAKA', 'SPARES', 'CRANKSHAFT', 10000000, 2500, 2850)," +
            "(212, 'BARAKA', 'SPARES', 'CRANKSHAFT 175/200 CC', 10000000, 2700, 3000)," +
            "(213, 'BARAKA', 'SPARES', 'CYLINDER HEAD', 10000000, 3500, 4000)," +
            "(214, 'BARAKA', 'SPARES', 'CYLINDER HEAD 200', 10000000, 3800, 4300)," +
            "(215, 'BARAKA', 'SPARES', 'DASHBOARD - BOXER', 10000000, 900, 1200)," +
            "(216, 'BARAKA', 'SPARES', 'DASHBOARD - DAYUN', 10000000, 1650, 1750)," +
            "(217, 'BARAKA', 'SPARES', 'DASHBOARD 1 PIN', 10000000, 700, 1000)," +
            "(218, 'BARAKA', 'SPARES', 'DASHBOARD COVER', 10000000, 250, 400)," +
            "(219, 'BARAKA', 'SPARES', 'DIM BULB', 10000000, 8, 20)," +
            "(220, 'BARAKA', 'SPARES', 'DRUM GEAR SHIFT', 10000000, 400, 500)," +
            "(221, 'BARAKA', 'SPARES', 'ENGINE BOLT', 10000000, 30, 50)," +
            "(222, 'BARAKA', 'SPARES', 'ENGINE COVER O - LEFT', 10000000, 1550, 1900)," +
            "(223, 'BARAKA', 'SPARES', 'ENGINE COVER O - RIGHT', 10000000, 1800, 2100)," +
            "(224, 'BARAKA', 'SPARES', 'ENGINE ROCK ARMS', 10000000, 250, 300)," +
            "(225, 'BARAKA', 'SPARES', 'EXHAUST CLAMPS', 10000000, 80, 100)," +
            "(226, 'BARAKA', 'SPARES', 'EXHAUST SLEEVE', 10000000, 20, 50)," +
            "(227, 'BARAKA', 'SPARES', 'FLASH UNIT', 10000000, 70, 100)," +
            "(228, 'BARAKA', 'SPARES', 'FOOT REST BAR', 10000000, 500, 600)," +
            "(229, 'BARAKA', 'SPARES', 'FOOT REST RUBBER', 10000000, 120, 200)," +
            "(230, 'BARAKA', 'SPARES', 'FRONT AXLE', 10000000, 75, 120)," +
            "(231, 'BARAKA', 'SPARES', 'FRONT GUARD', 10000000, 150, 250)," +
            "(232, 'BARAKA', 'SPARES', 'FUEL CORK', 10000000, 30, 50)," +
            "(233, 'BARAKA', 'SPARES', 'FUEL FILTER BOXER', 10000000, 100, 150)," +
            "(234, 'BARAKA', 'SPARES', 'FUEL TAP', 10000000, 145, 200)," +
            "(235, 'BARAKA', 'SPARES', 'FUSE BOX', 10000000, 35, 50)," +
            "(236, 'BARAKA', 'SPARES', 'GASKET - BLOCK', 10000000, 15, 50)," +
            "(237, 'BARAKA', 'SPARES', 'GASKET - CLUTCH', 10000000, 30, 50)," +
            "(238, 'BARAKA', 'SPARES', 'GASKET - EXHAUST', 10000000, 20, 50)," +
            "(239, 'BARAKA', 'SPARES', 'GASKET - MAGNETO', 10000000, 25, 50)," +
            "(240, 'BARAKA', 'SPARES', 'GASKET - PLASTIC', 10000000, 20, 50)," +
            "(241, 'BARAKA', 'SPARES', 'GASKET SET', 10000000, 180, 250)," +
            "(242, 'BARAKA', 'SPARES', 'GEAR BOX', 10000000, 1250, 1600)," +
            "(243, 'BARAKA', 'SPARES', 'GEAR LEVER', 10000000, 160, 250)," +
            "(244, 'BARAKA', 'SPARES', 'GEAR LEVER BOXER', 10000000, 200, 350)," +
            "(245, 'BARAKA', 'SPARES', 'GEAR LEVER – TUKTUK', 10000000, 350, 450)," +
            "(246, 'BARAKA', 'SPARES', 'GEAR NO 3', 10000000, 300, 350)," +
            "(247, 'BARAKA', 'SPARES', 'GEAR SELECTOR', 10000000, 180, 300)," +
            "(248, 'BARAKA', 'SPARES', 'GEAR SENSOR', 10000000, 120, 200)," +
            "(249, 'BARAKA', 'SPARES', 'GLOVES', 10000000, 180, 250)," +
            "(250, 'BARAKA', 'SPARES', 'GRILL', 10000000, 900, 1400)";
    private final String INSERT_ITEM_ = "INSERT INTO "+TABLE_ITEM+" " +
            "("+ COLUMN_ITEM_ID +","+ COLUMN_ITEM_STATION + ","+ COLUMN_ITEM_CATEGORY +","+ COLUMN_ITEM_NAME + ","+
            COLUMN_ITEM_QUANTITY +","+ COLUMN_ITEM_BUYPRICE + ","+ COLUMN_ITEM_SELLPRICE + ") VALUES" +
            "(251, 'BARAKA', 'SPARES', 'HALOGEN BULB', 10000000, 80, 100)," +
            "(252, 'BARAKA', 'SPARES', 'HAND GRIP', 10000000, 55, 100)," +
            "(253, 'BARAKA', 'SPARES', 'HAND GRIP BOXER', 10000000, 75, 100)," +
            "(254, 'BARAKA', 'SPARES', 'HAND GRIP GUARD', 10000000, 100, 160)," +
            "(255, 'BARAKA', 'SPARES', 'HAND GUARD - CAPTAIN', 10000000, 450, 500)," +
            "(256, 'BARAKA', 'SPARES', 'HAND GUARD - MONSTER', 10000000, 150, 250)," +
            "(257, 'BARAKA', 'SPARES', 'HANDLE LEVER', 10000000, 80, 120)," +
            "(258, 'BARAKA', 'SPARES', 'HANDLE LEVER BOXER', 10000000, 113, 150)," +
            "(259, 'BARAKA', 'SPARES', 'HANDLE SWITCH', 10000000, 275, 350)," +
            "(260, 'BARAKA', 'SPARES', 'HANDLE SWITCH BOXER', 10000000, 225, 300)," +
            "(261, 'BARAKA', 'SPARES', 'HEAD BULB', 10000000, 30, 50)," +
            "(262, 'BARAKA', 'SPARES', 'HEAD GUARD LEVER', 10000000, 210, 250)," +
            "(263, 'BARAKA', 'SPARES', 'HEADLIGHT', 10000000, 700, 900)," +
            "(264, 'BARAKA', 'SPARES', 'HEADLIGHT BULB', 10000000, 50, 120)," +
            "(265, 'BARAKA', 'SPARES', 'HELMET D104', 10000000, 700, 950)," +
            "(266, 'BARAKA', 'SPARES', 'HELMET D106', 10000000, 2500, 2800)," +
            "(267, 'BARAKA', 'SPARES', 'HELMET GLASS', 10000000, 70, 100)," +
            "(268, 'BARAKA', 'SPARES', 'HELMET GLASS - TINTED', 10000000, 150, 200)," +
            "(269, 'BARAKA', 'SPARES', 'HORN - PARARIRA', 10000000, 470, 600)," +
            "(270, 'BARAKA', 'SPARES', 'HORN - SMALL', 10000000, 100, 120)," +
            "(271, 'BARAKA', 'SPARES', 'HUB COVER - FRONT', 10000000, 680, 800)," +
            "(272, 'BARAKA', 'SPARES', 'HUB COVER - REAR', 10000000, 860, 1000)," +
            "(273, 'BARAKA', 'SPARES', 'IGNITION COIL', 10000000, 190, 300)," +
            "(274, 'BARAKA', 'SPARES', 'IGNITION COVER', 10000000, 50, 100)," +
            "(275, 'BARAKA', 'SPARES', 'IGNITION SWITCH', 10000000, 250, 300)," +
            "(276, 'BARAKA', 'SPARES', 'IGNITION SWITCH - CPTN', 10000000, 300, 350)," +
            "(277, 'BARAKA', 'SPARES', 'INDICATOR', 10000000, 100, 120)," +
            "(278, 'BARAKA', 'SPARES', 'INDICATOR – BOXER', 10000000, 90, 150)," +
            "(279, 'BARAKA', 'SPARES', 'INDICATOR - DAYUN', 10000000, 150, 200)," +
            "(280, 'BARAKA', 'SPARES', 'INDICATOR BULB', 10000000, 10, 20)," +
            "(281, 'BARAKA', 'SPARES', 'INNER CLUTCH CABLE', 10000000, 30, 50)," +
            "(282, 'BARAKA', 'SPARES', 'INSULATION TAPE', 10000000, 35, 50)," +
            "(283, 'BARAKA', 'SPARES', 'INSURANCE HOLDER', 10000000, 25, 50)," +
            "(284, 'BARAKA', 'SPARES', 'KICK START', 10000000, 200, 250)," +
            "(285, 'BARAKA', 'SPARES', 'KICK START - BOXER', 10000000, 250, 300)," +
            "(286, 'BARAKA', 'SPARES', 'KICK START – TUKTUK', 10000000, 400, 500)," +
            "(287, 'BARAKA', 'SPARES', 'KICK START SHAFT', 10000000, 400, 550)," +
            "(288, 'BARAKA', 'SPARES', 'LED SPORTLIGHT SMALL', 10000000, 250, 300)," +
            "(289, 'BARAKA', 'SPARES', 'LED SPORTLIGHT BIG', 10000000, 300, 350)," +
            "(290, 'BARAKA', 'SPARES', 'LED SWEET', 10000000, 60, 100)," +
            "(291, 'BARAKA', 'SPARES', 'MAGNET', 10000000, 800, 950)," +
            "(292, 'BARAKA', 'SPARES', 'MAGNETO COIL', 10000000, 700, 800)," +
            "(293, 'BARAKA', 'SPARES', 'MAGNETO ROLLERS', 10000000, 100, 200)," +
            "(294, 'BARAKA', 'SPARES', 'MAIN CABLE', 10000000, 600, 700)," +
            "(295, 'BARAKA', 'SPARES', 'MAIN STAND', 10000000, 450, 500)," +
            "(296, 'BARAKA', 'SPARES', 'MAIN STAND AXLE', 10000000, 100, 150)," +
            "(297, 'BARAKA', 'SPARES', 'MIRROR HOLDER', 10000000, 80, 100)," +
            "(298, 'BARAKA', 'SPARES', 'MIRROR HOLDER - BOXER', 10000000, 113, 150)," +
            "(299, 'BARAKA', 'SPARES', 'MUD FLAP', 10000000, 130, 200)," +
            "(300, 'BARAKA', 'SPARES', 'MUDGUARD', 10000000, 250, 400)," +
            "(301, 'BARAKA', 'SPARES', 'MUDGUARD ORIGINAL', 10000000, 1000, 1200)," +
            "(302, 'BARAKA', 'SPARES', 'OIL CAP', 10000000, 150, 300)," +
            "(303, 'BARAKA', 'SPARES', 'OIL DIPSTICK', 10000000, 30, 50)," +
            "(304, 'BARAKA', 'SPARES', 'OIL MIRROR', 10000000, 40, 100)," +
            "(305, 'BARAKA', 'SPARES', 'OIL PUMP - MEDIUM', 10000000, 220, 300)," +
            "(306, 'BARAKA', 'SPARES', 'OIL PUMP - SMALL', 10000000, 190, 300)," +
            "(307, 'BARAKA', 'SPARES', 'OIL SEAL 28', 10000000, 25, 50)," +
            "(308, 'BARAKA', 'SPARES', 'OIL SEAL 34', 10000000, 30, 50)," +
            "(309, 'BARAKA', 'SPARES', 'PISTON KIT', 10000000, 480, 550)," +
            "(310, 'BARAKA', 'SPARES', 'PISTON KIT 200', 10000000, 600, 700)," +
            "(311, 'BARAKA', 'SPARES', 'PISTON RING 175/200', 10000000, 350, 400)," +
            "(312, 'BARAKA', 'SPARES', 'PISTON RINGS', 10000000, 180, 300)," +
            "(313, 'BARAKA', 'SPARES', 'PISTON RINGS XY', 10000000, 400, 500)," +
            "(314, 'BARAKA', 'SPARES', 'PLUG', 10000000, 50, 100)," +
            "(315, 'BARAKA', 'SPARES', 'PLUG CAP', 10000000, 30, 50)," +
            "(316, 'BARAKA', 'SPARES', 'PUMP', 10000000, 170, 300)," +
            "(317, 'BARAKA', 'SPARES', 'PUMP CONNECTOR', 10000000, 70, 100)," +
            "(318, 'BARAKA', 'SPARES', 'PUSH ROD', 10000000, 100, 150)," +
            "(319, 'BARAKA', 'SPARES', 'RADIO MTUNGI', 10000000, 900, 1100)," +
            "(320, 'BARAKA', 'SPARES', 'RADIO SMALL', 10000000, 1100, 1300)," +
            "(321, 'BARAKA', 'SPARES', 'REAR AXLE', 10000000, 120, 150)," +
            "(322, 'BARAKA', 'SPARES', 'REAR AXLE - LONG', 10000000, 150, 180)," +
            "(323, 'BARAKA', 'SPARES', 'REGULATOR 4 WIRE', 10000000, 360, 450)," +
            "(324, 'BARAKA', 'SPARES', 'SCREW DRIVER', 10000000, 60, 100)," +
            "(325, 'BARAKA', 'SPARES', 'SEAT', 10000000, 1500, 1800)," +
            "(326, 'BARAKA', 'SPARES', 'SEAT COVER', 10000000, 275, 350)," +
            "(327, 'BARAKA', 'SPARES', 'SHOCK - FRONT', 10000000, 2800, 3400)," +
            "(328, 'BARAKA', 'SPARES', 'SHOCK - REAR', 10000000, 1600, 1900)," +
            "(329, 'BARAKA', 'SPARES', 'SHOCK - REAR DYQ7', 10000000, 2100, 2500)," +
            "(330, 'BARAKA', 'SPARES', 'SHOCK - TUKTUK', 10000000, 2800, 3200)," +
            "(331, 'BARAKA', 'SPARES', 'SHOCK BOOT RUBBER', 10000000, 60, 100)," +
            "(332, 'BARAKA', 'SPARES', 'SHOCK LIFTER', 10000000, 140, 200)," +
            "(333, 'BARAKA', 'SPARES', 'SHOCK SEAL', 10000000, 25, 50)," +
            "(334, 'BARAKA', 'SPARES', 'SIDE COVER', 10000000, 450, 800)," +
            "(335, 'BARAKA', 'SPARES', 'SIDE COVER LOCK', 10000000, 34, 50)," +
            "(336, 'BARAKA', 'SPARES', 'SIDE MIRROR - BLACK', 10000000, 100, 150)," +
            "(337, 'BARAKA', 'SPARES', 'SIDE MIRROR – BM150', 10000000, 125, 150)," +
            "(338, 'BARAKA', 'SPARES', 'SIDE MIRROR – CHROME', 10000000, 200, 225)," +
            "(339, 'BARAKA', 'SPARES', 'SILICONE', 10000000, 45, 60)," +
            "(340, 'BARAKA', 'SPARES', 'SILICONE BIG', 10000000, 130, 200)," +
            "(341, 'BARAKA', 'SPARES', 'SPEED CABLE', 10000000, 80, 150)," +
            "(342, 'BARAKA', 'SPARES', 'SPRAY', 10000000, 220, 350)," +
            "(343, 'BARAKA', 'SPARES', 'SPRING - BRAKE', 10000000, 15, 50)," +
            "(344, 'BARAKA', 'SPARES', 'SPRING - MAIN', 10000000, 25, 50)," +
            "(345, 'BARAKA', 'SPARES', 'SPROCKET - FRONT', 10000000, 60, 100)," +
            "(346, 'BARAKA', 'SPARES', 'SPROCKET - REAR', 10000000, 400, 450)," +
            "(347, 'BARAKA', 'SPARES', 'SPROCKET LOCK', 10000000, 20, 50)," +
            "(348, 'BARAKA', 'SPARES', 'SPROCKET SHAFT', 10000000, 250, 300)," +
            "(349, 'BARAKA', 'SPARES', 'SPROCKET SITTING', 10000000, 450, 600)," +
            "(350, 'BARAKA', 'SPARES', 'SPROCKET STANDS', 10000000, 50, 100)," +
            "(351, 'BARAKA', 'SPARES', 'STAND HOOK', 10000000, 15, 50)," +
            "(352, 'BARAKA', 'SPARES', 'STARTER MOTOR', 10000000, 1020, 1200)," +
            "(353, 'BARAKA', 'SPARES', 'STEERING', 10000000, 650, 750)," +
            "(354, 'BARAKA', 'SPARES', 'STEERING LOCK', 10000000, 40, 100)," +
            "(355, 'BARAKA', 'SPARES', 'STEERING STEM', 10000000, 470, 650)," +
            "(356, 'BARAKA', 'SPARES', 'STICKER', 10000000, 25, 50)," +
            "(357, 'BARAKA', 'SPARES', 'SUPER GLUE', 10000000, 30, 40)," +
            "(358, 'BARAKA', 'SPARES', 'SURPUSS', 10000000, 500, 750)," +
            "(359, 'BARAKA', 'SPARES', 'SWING ARM METAL', 10000000, 1600, 2000)," +
            "(360, 'BARAKA', 'SPARES', 'TAIL FLAP', 10000000, 80, 100)," +
            "(361, 'BARAKA', 'SPARES', 'TAIL LAMP', 10000000, 700, 1000)," +
            "(362, 'BARAKA', 'SPARES', 'TAIL LAMP – ORIGINAL', 10000000, 1200, 1500)," +
            "(363, 'BARAKA', 'SPARES', 'TAIL LAMP BULB', 10000000, 12, 30)," +
            "(364, 'BARAKA', 'SPARES', 'TAIL LAMP BULB - DIGITAL', 10000000, 100, 120)," +
            "(365, 'BARAKA', 'SPARES', 'TAIL MUDGUARD', 10000000, 250, 300)," +
            "(366, 'BARAKA', 'SPARES', 'TANK CAP - ROUND', 10000000, 350, 400)," +
            "(367, 'BARAKA', 'SPARES', 'TANK CAP - SQUARE', 10000000, 220, 300)," +
            "(368, 'BARAKA', 'SPARES', 'TIE WRAP', 10000000, 4, 10)," +
            "(369, 'BARAKA', 'SPARES', 'TOP BRIDGE', 10000000, 430, 600)," +
            "(370, 'BARAKA', 'SPARES', 'TOP NUT', 10000000, 40, 50)," +
            "(371, 'BARAKA', 'SPARES', 'TOP NUT - BOXER', 10000000, 150, 200)," +
            "(372, 'BARAKA', 'SPARES', 'TUBE', 10000000, 200, 250)," +
            "(373, 'BARAKA', 'SPARES', 'TUBE - 3.50/4.10 X 18', 10000000, 270, 350)," +
            "(374, 'BARAKA', 'SPARES', 'TUBE - MTR', 10000000, 240, 300)," +
            "(375, 'BARAKA', 'SPARES', 'TUBE – TUKTUK', 10000000, 500, 600)," +
            "(376, 'BARAKA', 'SPARES', 'TUBE - 175 X13/14', 10000000, 550, 650)," +
            "(377, 'BARAKA', 'SPARES', 'TUBELESS NOZZLE', 10000000, 40, 70)," +
            "(378, 'BARAKA', 'SPARES', 'TUPPET – SMALL', 10000000, 350, 450)," +
            "(379, 'BARAKA', 'SPARES', 'TUPPET BOLT', 10000000, 15, 30)," +
            "(380, 'BARAKA', 'SPARES', 'TUTOR', 10000000, 100, 150)," +
            "(381, 'BARAKA', 'SPARES', 'TYRE - COMFORSER SIZE 13', 10000000, 3900, 4300)," +
            "(382, 'BARAKA', 'SPARES', 'TYRE - COMFORSER SIZE 14', 10000000, 4000, 4400)," +
            "(383, 'BARAKA', 'SPARES', 'TYRE - GT INDONESIA SIZE 13', 10000000, 4250, 4700)," +
            "(384, 'BARAKA', 'SPARES', 'TYRE - GT INDONESIA SIZE 14', 10000000, 6800, 7300)," +
            "(385, 'BARAKA', 'SPARES', 'TYRE - LINGLONG SIZE 13', 10000000, 3550, 3950)," +
            "(386, 'BARAKA', 'SPARES', 'TYRE - LINGLONG SIZE 14', 10000000, 3750, 4200)," +
            "(387, 'BARAKA', 'SPARES', 'TYRE - PETROMAX 13', 10000000, 4000, 4500)," +
            "(388, 'BARAKA', 'SPARES', 'TYRE - PETROMAX 14', 10000000, 4300, 4800)," +
            "(389, 'BARAKA', 'SPARES', 'TYRE - TUKTUK', 10000000, 2800, 3000)," +
            "(390, 'BARAKA', 'SPARES', 'TYRE 2.75 X 17 - YAWADA', 10000000, 1550, 2000)," +
            "(391, 'BARAKA', 'SPARES', 'TYRE 2.75 X 18 - CC', 10000000, 1700, 2000)," +
            "(392, 'BARAKA', 'SPARES', 'TYRE 2.75 X 18 - CORDIAL', 10000000, 2200, 2300)," +
            "(393, 'BARAKA', 'SPARES', 'TYRE 3.00 X 17 - CAPTAIN', 10000000, 1800, 2000)," +
            "(394, 'BARAKA', 'SPARES', 'TYRE 3.00 X 18 - MRF', 10000000, 3300, 3600)," +
            "(395, 'BARAKA', 'SPARES', 'TYRE 3.00 X 18 C.C', 10000000, 1750, 2200)," +
            "(396, 'BARAKA', 'SPARES', 'TYRE 3.00 X 18 CORDIAL', 10000000, 2200, 2400)," +
            "(397, 'BARAKA', 'SPARES', 'TYRE 3.00 X 18 SUPERRUN', 10000000, 2850, 2900)," +
            "(398, 'BARAKA', 'SPARES', 'TYRE 4.10 X 18 SUPERRUN', 10000000, 3000, 3300)," +
            "(399, 'BARAKA', 'SPARES', 'TYRE ROPE - LONG', 10000000, 30, 50)," +
            "(400, 'BARAKA', 'SPARES', 'TYRE ROPE - SHORT', 10000000, 15, 30)," +
            "(401, 'BARAKA', 'SPARES', 'TYRE SEAL', 10000000, 12, 50)," +
            "(402, 'BARAKA', 'SPARES', 'UMBRELLA', 10000000, 1800, 2100)," +
            "(403, 'BARAKA', 'SPARES', 'UNIFORM', 10000000, 1200, 1350)," +
            "(404, 'BARAKA', 'SPARES', 'VALVE GUIDE', 10000000, 100, 150)," +
            "(405, 'BARAKA', 'SPARES', 'VALVE SEAL', 10000000, 25, 50)," +
            "(406, 'BARAKA', 'SPARES', 'VALVE SET', 10000000, 200, 300)," +
            "(407, 'BARAKA', 'SPARES', 'VALVE SET – TUKTUK', 10000000, 200, 300)," +
            "(408, 'BARAKA', 'SPARES', 'WINDSCREEN – BIG', 10000000, 650, 800)," +
            "(409, 'BARAKA', 'SPARES', 'WINDSCREEN – SMALL', 10000000, 500, 650)," +
            "(410, 'BARAKA', 'SPARES', 'WORLD CLASS AB - BIG', 10000000, 150, 200)," +
            "(411, 'BARAKA', 'SPARES', 'WORLD CLASS AB - SMALL', 10000000, 80, 100)," +
            "(412, 'BARAKA', 'BOLT/SPANNER', '10 - 11 FIXED', 10000000, 100, 130)," +
            "(413, 'BARAKA', 'BOLT/SPANNER', '10 COMBINED', 10000000, 90, 150)," +
            "(414, 'BARAKA', 'BOLT/SPANNER', '12 - 13 FIXED', 10000000, 150, 180)," +
            "(415, 'BARAKA', 'BOLT/SPANNER', '12 - 13 RING', 10000000, 130, 180)," +
            "(416, 'BARAKA', 'BOLT/SPANNER', '12 COMBINED', 10000000, 120, 180)," +
            "(417, 'BARAKA', 'BOLT/SPANNER', '13 COMBINED', 10000000, 130, 200)," +
            "(418, 'BARAKA', 'BOLT/SPANNER', '14 - 15 FIXED', 10000000, 150, 200)," +
            "(419, 'BARAKA', 'BOLT/SPANNER', '14 -15 RING', 10000000, 140, 200)," +
            "(420, 'BARAKA', 'BOLT/SPANNER', '14 COMBINED', 10000000, 130, 200)," +
            "(421, 'BARAKA', 'BOLT/SPANNER', '16  - 17 RING', 10000000, 160, 200)," +
            "(422, 'BARAKA', 'BOLT/SPANNER', '16 - 17 FIXED', 10000000, 160, 220)," +
            "(423, 'BARAKA', 'BOLT/SPANNER', '16 COMBINED', 10000000, 160, 220)," +
            "(424, 'BARAKA', 'BOLT/SPANNER', '17 COMBINED', 10000000, 160, 220)," +
            "(425, 'BARAKA', 'BOLT/SPANNER', '18 - 19 FIXED', 10000000, 180, 250)," +
            "(426, 'BARAKA', 'BOLT/SPANNER', '18 - 19 RING', 10000000, 190, 250)," +
            "(427, 'BARAKA', 'BOLT/SPANNER', '18 COMBINED', 10000000, 180, 250)," +
            "(428, 'BARAKA', 'BOLT/SPANNER', '19 COMBINED', 10000000, 180, 250)," +
            "(429, 'BARAKA', 'BOLT/SPANNER', '30 - 32 FIXED', 10000000, 450, 500)," +
            "(430, 'BARAKA', 'BOLT/SPANNER', '30 COMBINED', 10000000, 450, 500)," +
            "(431, 'BARAKA', 'BOLT/SPANNER', '32 COMBINED', 10000000, 460, 500)," +
            "(432, 'BARAKA', 'BOLT/SPANNER', 'PLIERS - MEDIUM', 10000000, 180, 250)," +
            "(433, 'BARAKA', 'BOLT/SPANNER', 'PLIERS - SMALL', 10000000, 150, 200)," +
            "(434, 'BARAKA', 'BOLT/SPANNER', '10 X 0.5', 10000000, 5, 10)," +
            "(435, 'BARAKA', 'BOLT/SPANNER', '10 X 1.5', 10000000, 5, 10)," +
            "(436, 'BARAKA', 'BOLT/SPANNER', '10 X 1', 10000000, 5, 10)," +
            "(437, 'BARAKA', 'BOLT/SPANNER', '10 X 2', 10000000, 6, 10)," +
            "(438, 'BARAKA', 'BOLT/SPANNER', '10 X 3', 10000000, 10, 20)," +
            "(439, 'BARAKA', 'BOLT/SPANNER', '12 SHOCK', 10000000, 30, 40)," +
            "(440, 'BARAKA', 'BOLT/SPANNER', '13 X 1.5', 10000000, 10, 20)," +
            "(441, 'BARAKA', 'BOLT/SPANNER', '13 X 1', 10000000, 8, 20)," +
            "(442, 'BARAKA', 'BOLT/SPANNER', '13 X 2', 10000000, 11, 20)," +
            "(443, 'BARAKA', 'BOLT/SPANNER', '13 X 3', 10000000, 17, 30)," +
            "(444, 'BARAKA', 'BOLT/SPANNER', '13 X 4', 10000000, 20, 30)," +
            "(445, 'BARAKA', 'BOLT/SPANNER', '14 X 2 SHOCK', 10000000, 25, 40)," +
            "(446, 'BARAKA', 'BOLT/SPANNER', '14 X 1.5', 10000000, 12, 20)," +
            "(447, 'BARAKA', 'BOLT/SPANNER', '14 X 1', 10000000, 10, 20)," +
            "(448, 'BARAKA', 'BOLT/SPANNER', '14 X 2 FINE', 10000000, 16, 25)," +
            "(449, 'BARAKA', 'BOLT/SPANNER', '14 X 2', 10000000, 13, 25)," +
            "(450, 'BARAKA', 'BOLT/SPANNER', '14 X 4.5', 10000000, 35, 50)," +
            "(451, 'BARAKA', 'BOLT/SPANNER', '17 X 2', 10000000, 20, 30)," +
            "(452, 'BARAKA', 'BOLT/SPANNER', '8 X 1.5', 10000000, 5, 10)," +
            "(453, 'BARAKA', 'BOLT/SPANNER', '8 X 1', 10000000, 5, 10)," +
            "(454, 'BARAKA', 'BOLT/SPANNER', '10 X 13', 10000000, 10, 20)," +
            "(455, 'BARAKA', 'BOLT/SPANNER', 'NUT 10', 10000000, 2, 5)," +
            "(456, 'BARAKA', 'BOLT/SPANNER', 'NUT 13', 10000000, 3, 5)," +
            "(457, 'BARAKA', 'BOLT/SPANNER', 'NUT 14', 10000000, 4, 10)," +
            "(458, 'BARAKA', 'BOLT/SPANNER', 'NUT 17', 10000000, 15, 25)," +
            "(459, 'BARAKA', 'BOLT/SPANNER', 'NUT 19', 10000000, 15, 30)," +
            "(460, 'BARAKA', 'BOLT/SPANNER', 'WASHER 10', 10000000, 2, 5)," +
            "(461, 'BARAKA', 'BOLT/SPANNER', 'WASHER 13', 10000000, 3, 5)," +
            "(462, 'BARAKA', 'BOLT/SPANNER', 'WASHER 14', 10000000, 2, 5)," +
            "(463, 'BARAKA', 'BOLT/SPANNER', 'WASHER 17', 10000000, 4, 10)," +
            "(464, 'BARAKA', 'BOLT/SPANNER', 'WASHER 19', 10000000, 4, 10)," +
            "(465, 'BARAKA', 'OILS', 'ADVANCE AX5', 10000000, 456, 500)," +
            "(466, 'BARAKA', 'OILS', 'ATF', 10000000, 140, 170)," +
            "(467, 'BARAKA', 'OILS', 'B.ACID', 10000000, 75, 100)," +
            "(468, 'BARAKA', 'OILS', 'B.WATER', 10000000, 36, 70)," +
            "(469, 'BARAKA', 'OILS', 'BITUL 4T', 10000000, 280, 350)," +
            "(470, 'BARAKA', 'OILS', 'CC', 10000000, 135, 170)," +
            "(471, 'BARAKA', 'OILS', 'CAPTAIN 4T', 10000000, 260, 400)," +
            "(472, 'BARAKA', 'OILS', 'COOLANT', 10000000, 100, 200)," +
            "(473, 'BARAKA', 'OILS', 'DOT 4 OG - 200ML', 10000000, 120, 150)," +
            "(474, 'BARAKA', 'OILS', 'DOT 4 OG - 500ML', 10000000, 230, 300)," +
            "(475, 'BARAKA', 'OILS', 'DOT 4 TOT -250ML', 10000000, 219, 250)," +
            "(476, 'BARAKA', 'OILS', 'DOT 4 TOT -500ML', 10000000, 421, 500)," +
            "(477, 'BARAKA', 'OILS', 'GREASE', 10000000, 205, 250)," +
            "(478, 'BARAKA', 'OILS', 'HELIX ½', 10000000, 210, 250)," +
            "(479, 'BARAKA', 'OILS', 'HELIX L', 10000000, 376, 450)," +
            "(480, 'BARAKA', 'OILS', 'K-LUBE', 10000000, 188, 250)," +
            "(481, 'BARAKA', 'OILS', 'K LUBE 5L', 10000000, 1550, 1750)," +
            "(482, 'BARAKA', 'OILS', 'LACHEKA ½', 10000000, 135, 170)," +
            "(483, 'BARAKA', 'OILS', 'MOTOR OIL', 10000000, 375, 420)," +
            "(484, 'BARAKA', 'OILS', 'POWEREX ½', 10000000, 140, 200)," +
            "(485, 'BARAKA', 'OILS', 'PETROMAX L', 10000000, 270, 300)," +
            "(486, 'BARAKA', 'OILS', 'OG ½', 10000000, 165, 200)," +
            "(487, 'BARAKA', 'OILS', 'OG L', 10000000, 295, 400)," +
            "(488, 'BARAKA', 'OILS', 'POWEREX L', 10000000, 315, 350)," +
            "(489, 'BARAKA', 'OILS', 'T. CLASSIC ½', 10000000, 217, 250)," +
            "(490, 'BARAKA', 'OILS', 'T.CLASSIC L', 10000000, 405, 450)," +
            "(491, 'BARAKA', 'OILS', 'T.CLASSIC 4L', 10000000, 1528, 1900)," +
            "(492, 'BARAKA', 'OILS', 'TYRE SEALANT', 10000000, 240, 300)," +
            "(493, 'KITHIMU AUTO', 'SPARES', '1512 BEARING', 10000000, 80, 150)," +
            "(494, 'KITHIMU AUTO', 'SPARES', '6004 BEARING', 10000000, 80, 100)," +
            "(495, 'KITHIMU AUTO', 'SPARES', '6202 BEARING', 10000000, 55, 100)," +
            "(496, 'KITHIMU AUTO', 'SPARES', '6204 BEARING', 10000000, 150, 200)," +
            "(497, 'KITHIMU AUTO', 'SPARES', '6301 BEARING', 10000000, 55, 100)," +
            "(498, 'KITHIMU AUTO', 'SPARES', '6302 BEARING', 10000000, 60, 100)," +
            "(499, 'KITHIMU AUTO', 'SPARES', '6328 BEARING', 10000000, 180, 250)," +
            "(500, 'KITHIMU AUTO', 'SPARES', 'ACC CABLE', 10000000, 110, 150)," +
            "(501, 'KITHIMU AUTO', 'SPARES', 'ACC CABLE - BOXER', 10000000, 100, 150)," +
            "(502, 'KITHIMU AUTO', 'SPARES', 'AIR CLEANER', 10000000, 1200, 1500)," +
            "(503, 'KITHIMU AUTO', 'SPARES', 'AIR CLEANER JOINT', 10000000, 100, 150)," +
            "(504, 'KITHIMU AUTO', 'SPARES', 'ARM BUSH', 10000000, 50, 75)," +
            "(505, 'KITHIMU AUTO', 'SPARES', 'BALL RACE SET', 10000000, 200, 250)," +
            "(506, 'KITHIMU AUTO', 'SPARES', 'BATTERY N7', 10000000, 1350, 1600)," +
            "(507, 'KITHIMU AUTO', 'SPARES', 'BATTERY N9', 10000000, 1450, 1800)," +
            "(508, 'KITHIMU AUTO', 'SPARES', 'BATTERY STRIP', 10000000, 30, 50)," +
            "(509, 'KITHIMU AUTO', 'SPARES', 'BATTERY WIRE', 10000000, 20, 30)," +
            "(510, 'KITHIMU AUTO', 'SPARES', 'BLOCK SET', 10000000, 1620, 1800)," +
            "(511, 'KITHIMU AUTO', 'SPARES', 'BRAKE ARM', 10000000, 130, 150)," +
            "(512, 'KITHIMU AUTO', 'SPARES', 'BRAKE CABLE', 10000000, 85, 120)," +
            "(513, 'KITHIMU AUTO', 'SPARES', 'BRAKE PEDAL', 10000000, 220, 250)," +
            "(514, 'KITHIMU AUTO', 'SPARES', 'BRAKE PEDAL SHOE', 10000000, 150, 200)," +
            "(515, 'KITHIMU AUTO', 'SPARES', 'BRAKE ROD', 10000000, 55, 100)," +
            "(516, 'KITHIMU AUTO', 'SPARES', 'BRAKE SHOE', 10000000, 190, 250)," +
            "(517, 'KITHIMU AUTO', 'SPARES', 'BRAKE SWITCH', 10000000, 20, 50)," +
            "(518, 'KITHIMU AUTO', 'SPARES', 'BULB HOLDER - FRONT', 10000000, 50, 100)," +
            "(519, 'KITHIMU AUTO', 'SPARES', 'BULB HOLDER 3 PIN', 10000000, 110, 150)," +
            "(520, 'KITHIMU AUTO', 'SPARES', 'BULB HOLDER - REAR', 10000000, 30, 50)," +
            "(521, 'KITHIMU AUTO', 'SPARES', 'BULL BAR CLAMPS', 10000000, 80, 125)," +
            "(522, 'KITHIMU AUTO', 'SPARES', 'BULL BAR PLASTIC ORIGINAL', 10000000, 800, 1000)," +
            "(523, 'KITHIMU AUTO', 'SPARES', 'BULL BAR PLASTIC SET', 10000000, 450, 600)," +
            "(524, 'KITHIMU AUTO', 'SPARES', 'BULL BAR SET - SPORT', 10000000, 1700, 2200)," +
            "(525, 'KITHIMU AUTO', 'SPARES', 'CAMSHAFT', 10000000, 450, 600)," +
            "(526, 'KITHIMU AUTO', 'SPARES', 'CAP TAPPET', 10000000, 50, 80)," +
            "(527, 'KITHIMU AUTO', 'SPARES', 'CARBURATOR', 10000000, 750, 900)," +
            "(528, 'KITHIMU AUTO', 'SPARES', 'CARBURATOR JOINT', 10000000, 90, 150)," +
            "(529, 'KITHIMU AUTO', 'SPARES', 'CARBURATOR KIT', 10000000, 150, 200)," +
            "(530, 'KITHIMU AUTO', 'SPARES', 'CDI PRO', 10000000, 150, 300)," +
            "(531, 'KITHIMU AUTO', 'SPARES', 'CELLONOID', 10000000, 200, 250)," +
            "(532, 'KITHIMU AUTO', 'SPARES', 'CENTRE AXLE', 10000000, 120, 150)," +
            "(533, 'KITHIMU AUTO', 'SPARES', 'CENTRE BOLT', 10000000, 140, 200)," +
            "(534, 'KITHIMU AUTO', 'SPARES', 'CHAIN', 10000000, 420, 450)," +
            "(535, 'KITHIMU AUTO', 'SPARES', 'CHAIN - CAPTAIN', 10000000, 450, 500)," +
            "(536, 'KITHIMU AUTO', 'SPARES', 'CHAIN ADJUSTER', 10000000, 100, 125)," +
            "(537, 'KITHIMU AUTO', 'SPARES', 'CHAIN BUSH', 10000000, 90, 150)," +
            "(538, 'KITHIMU AUTO', 'SPARES', 'CHAIN COVER', 10000000, 120, 150)," +
            "(539, 'KITHIMU AUTO', 'SPARES', 'CHAIN LOCK', 10000000, 20, 30)," +
            "(540, 'KITHIMU AUTO', 'SPARES', 'CLUTCH 5H', 10000000, 600, 700)," +
            "(541, 'KITHIMU AUTO', 'SPARES', 'CLUTCH 6H', 10000000, 720, 850)," +
            "(542, 'KITHIMU AUTO', 'SPARES', 'CLUTCH BOX', 10000000, 550, 650)," +
            "(543, 'KITHIMU AUTO', 'SPARES', 'CLUTCH CABLE', 10000000, 100, 120)," +
            "(544, 'KITHIMU AUTO', 'SPARES', 'CLUTCH DISC', 10000000, 200, 300)," +
            "(545, 'KITHIMU AUTO', 'SPARES', 'CLUTCH PLATE', 10000000, 185, 250)," +
            "(546, 'KITHIMU AUTO', 'SPARES', 'CLUTCH RELEASER', 10000000, 100, 150)," +
            "(547, 'KITHIMU AUTO', 'SPARES', 'CRANKSHAFT', 10000000, 2200, 2500)," +
            "(548, 'KITHIMU AUTO', 'SPARES', 'CYLINDER HEAD', 10000000, 3300, 3700)," +
            "(549, 'KITHIMU AUTO', 'SPARES', 'DASHBOARD', 10000000, 820, 900)," +
            "(550, 'KITHIMU AUTO', 'SPARES', 'DASHBOARD COVER', 10000000, 250, 350)," +
            "(551, 'KITHIMU AUTO', 'SPARES', 'DIM BULB', 10000000, 10, 20)," +
            "(552, 'KITHIMU AUTO', 'SPARES', 'DRUM GEAR SHIFT', 10000000, 300, 450)," +
            "(553, 'KITHIMU AUTO', 'SPARES', 'ENGINE BOLT', 10000000, 30, 50)," +
            "(554, 'KITHIMU AUTO', 'SPARES', 'ENGINE COVER - LEFT', 10000000, 700, 1000)," +
            "(555, 'KITHIMU AUTO', 'SPARES', 'ENGINE COVER - RIGHT', 10000000, 800, 1000)," +
            "(556, 'KITHIMU AUTO', 'SPARES', 'ENGINE ROCK ARMS', 10000000, 250, 300)," +
            "(557, 'KITHIMU AUTO', 'SPARES', 'EXHAUST CLAMPS', 10000000, 80, 120)," +
            "(558, 'KITHIMU AUTO', 'SPARES', 'EXHAUST SLEEVE', 10000000, 20, 50)," +
            "(559, 'KITHIMU AUTO', 'SPARES', 'FLASH UNIT', 10000000, 70, 100)," +
            "(560, 'KITHIMU AUTO', 'SPARES', 'FOOT REST BAR', 10000000, 420, 500)," +
            "(561, 'KITHIMU AUTO', 'SPARES', 'FOOT REST RUBBER', 10000000, 60, 100)," +
            "(562, 'KITHIMU AUTO', 'SPARES', 'FRONT AXLE', 10000000, 90, 120)," +
            "(563, 'KITHIMU AUTO', 'SPARES', 'FRONT GUARD', 10000000, 160, 250)," +
            "(564, 'KITHIMU AUTO', 'SPARES', 'FUEL FILTER/CORK', 10000000, 30, 50)," +
            "(565, 'KITHIMU AUTO', 'SPARES', 'FILTER 3152', 10000000, 135, 200)," +
            "(566, 'KITHIMU AUTO', 'SPARES', 'FILTER 3047', 10000000, 145, 250)," +
            "(567, 'KITHIMU AUTO', 'SPARES', 'FUEL TAP', 10000000, 150, 180)," +
            "(568, 'KITHIMU AUTO', 'SPARES', 'FUSE BOX', 10000000, 35, 50)," +
            "(569, 'KITHIMU AUTO', 'SPARES', 'GASKET - BLOCK', 10000000, 20, 50)," +
            "(570, 'KITHIMU AUTO', 'SPARES', 'GASKET - CLUTCH', 10000000, 30, 50)," +
            "(571, 'KITHIMU AUTO', 'SPARES', 'GASKET - EXHAUST', 10000000, 20, 50)," +
            "(572, 'KITHIMU AUTO', 'SPARES', 'GASKET - MAGNETO', 10000000, 25, 50)," +
            "(573, 'KITHIMU AUTO', 'SPARES', 'GASKET SET', 10000000, 180, 250)," +
            "(574, 'KITHIMU AUTO', 'SPARES', 'GASKET SET WP', 10000000, 200, 250)," +
            "(575, 'KITHIMU AUTO', 'SPARES', 'GEAR BOX', 10000000, 1150, 1500)," +
            "(576, 'KITHIMU AUTO', 'SPARES', 'GEAR LEVER', 10000000, 150, 200)," +
            "(577, 'KITHIMU AUTO', 'SPARES', 'GEAR NO 3', 10000000, 260, 350)," +
            "(578, 'KITHIMU AUTO', 'SPARES', 'GEAR SELECTOR', 10000000, 250, 300)," +
            "(579, 'KITHIMU AUTO', 'SPARES', 'GEAR SENSOR', 10000000, 120, 200)," +
            "(580, 'KITHIMU AUTO', 'SPARES', 'GLOVES', 10000000, 180, 250)," +
            "(581, 'KITHIMU AUTO', 'SPARES', 'GRILL', 10000000, 1400, 1500)," +
            "(582, 'KITHIMU AUTO', 'SPARES', 'HALOGEN BULB', 10000000, 80, 120)," +
            "(583, 'KITHIMU AUTO', 'SPARES', 'HAND GRIP', 10000000, 55, 100)," +
            "(584, 'KITHIMU AUTO', 'SPARES', 'HAND GRIP GUARD', 10000000, 100, 150)," +
            "(585, 'KITHIMU AUTO', 'SPARES', 'HAND GUARD', 10000000, 370, 500)," +
            "(586, 'KITHIMU AUTO', 'SPARES', 'HANDLE LEVER', 10000000, 60, 100)," +
            "(587, 'KITHIMU AUTO', 'SPARES', 'HANDLE SWITCH', 10000000, 270, 300)," +
            "(588, 'KITHIMU AUTO', 'SPARES', 'HEAD BULB', 10000000, 30, 50)," +
            "(589, 'KITHIMU AUTO', 'SPARES', 'HEADLIGHT', 10000000, 700, 900)," +
            "(590, 'KITHIMU AUTO', 'SPARES', 'HEADLIGHT BULB', 10000000, 100, 150)," +
            "(591, 'KITHIMU AUTO', 'SPARES', 'HEADLIGHT GLASS', 10000000, 70, 150)," +
            "(592, 'KITHIMU AUTO', 'SPARES', 'HEADLIGHT CASE', 10000000, 150, 250)," +
            "(593, 'KITHIMU AUTO', 'SPARES', 'HELMET', 10000000, 1200, 1400)," +
            "(594, 'KITHIMU AUTO', 'SPARES', 'HELMET GLASS', 10000000, 70, 100)," +
            "(595, 'KITHIMU AUTO', 'SPARES', 'HELMET GLASS - TINTED', 10000000, 150, 200)," +
            "(596, 'KITHIMU AUTO', 'SPARES', 'HORN - PARARIRA', 10000000, 450, 600)," +
            "(597, 'KITHIMU AUTO', 'SPARES', 'HORN - SMALL', 10000000, 100, 120)," +
            "(598, 'KITHIMU AUTO', 'SPARES', 'HORN - MEDIUM', 10000000, 150, 200)," +
            "(599, 'KITHIMU AUTO', 'SPARES', 'HUB COVER - FRONT', 10000000, 680, 900)," +
            "(600, 'KITHIMU AUTO', 'SPARES', 'HUB COVER - REAR', 10000000, 800, 1000)";
    private final String INSERT_ITEM__ = "INSERT INTO "+TABLE_ITEM+" " +
            "("+ COLUMN_ITEM_ID +","+ COLUMN_ITEM_STATION + ","+ COLUMN_ITEM_CATEGORY +","+ COLUMN_ITEM_NAME + ","+
            COLUMN_ITEM_QUANTITY +","+ COLUMN_ITEM_BUYPRICE + ","+ COLUMN_ITEM_SELLPRICE + ") VALUES" +
            "(601, 'KITHIMU AUTO', 'SPARES', 'HUB COVER 125', 10000000, 550, 700)," +
            "(602, 'KITHIMU AUTO', 'SPARES', 'HUB LOCK', 10000000, 20, 40)," +
            "(603, 'KITHIMU AUTO', 'SPARES', 'IGNITION COIL', 10000000, 190, 250)," +
            "(604, 'KITHIMU AUTO', 'SPARES', 'IGNITION COVER', 10000000, 50, 80)," +
            "(605, 'KITHIMU AUTO', 'SPARES', 'IGNITION SWITCH', 10000000, 250, 300)," +
            "(606, 'KITHIMU AUTO', 'SPARES', 'INDICATOR', 10000000, 100, 120)," +
            "(607, 'KITHIMU AUTO', 'SPARES', 'INDICATOR BX', 10000000, 110, 150)," +
            "(608, 'KITHIMU AUTO', 'SPARES', 'INDICATOR BULB', 10000000, 10, 20)," +
            "(609, 'KITHIMU AUTO', 'SPARES', 'INNER CLUTCH CABLE', 10000000, 25, 50)," +
            "(610, 'KITHIMU AUTO', 'SPARES', 'INSULATION TAPE', 10000000, 32, 50)," +
            "(611, 'KITHIMU AUTO', 'SPARES', 'INSURANCE HOLDER', 10000000, 25, 50)," +
            "(612, 'KITHIMU AUTO', 'SPARES', 'KICK START', 10000000, 200, 250)," +
            "(613, 'KITHIMU AUTO', 'SPARES', 'KICK START SHAFT', 10000000, 420, 500)," +
            "(614, 'KITHIMU AUTO', 'SPARES', 'LED SPORTLIGHT BIG', 10000000, 300, 350)," +
            "(615, 'KITHIMU AUTO', 'SPARES', 'LED SPORTLIGHT DOUBLE', 10000000, 600, 750)," +
            "(616, 'KITHIMU AUTO', 'SPARES', 'LED SPORTLIGHT SMALL', 10000000, 250, 300)," +
            "(617, 'KITHIMU AUTO', 'SPARES', 'LED SWEET', 10000000, 60, 100)," +
            "(618, 'KITHIMU AUTO', 'SPARES', 'MAGNET', 10000000, 750, 1000)," +
            "(619, 'KITHIMU AUTO', 'SPARES', 'MAGNETO COIL', 10000000, 650, 800)," +
            "(620, 'KITHIMU AUTO', 'SPARES', 'MAGNETO COIL CAPTAIN', 10000000, 890, 1000)," +
            "(621, 'KITHIMU AUTO', 'SPARES', 'MAGNETO ROLLERS', 10000000, 85, 150)," +
            "(622, 'KITHIMU AUTO', 'SPARES', 'MAIN CABLE', 10000000, 600, 700)," +
            "(623, 'KITHIMU AUTO', 'SPARES', 'MAIN STAND', 10000000, 400, 500)," +
            "(624, 'KITHIMU AUTO', 'SPARES', 'MAIN STAND AXLE', 10000000, 100, 150)," +
            "(625, 'KITHIMU AUTO', 'SPARES', 'MIRROR HOLDER', 10000000, 75, 100)," +
            "(626, 'KITHIMU AUTO', 'SPARES', 'MUD FLAP', 10000000, 140, 200)," +
            "(627, 'KITHIMU AUTO', 'SPARES', 'MUD FLAP - SKYGO', 10000000, 300, 350)," +
            "(628, 'KITHIMU AUTO', 'SPARES', 'MUDGUARD', 10000000, 250, 300)," +
            "(629, 'KITHIMU AUTO', 'SPARES', 'MUDGUARD - MABATI/CAP', 10000000, 700, 1000)," +
            "(630, 'KITHIMU AUTO', 'SPARES', 'MUDGUARD ORIGINAL', 10000000, 1200, 1400)," +
            "(631, 'KITHIMU AUTO', 'SPARES', 'OIL CAP', 10000000, 130, 250)," +
            "(632, 'KITHIMU AUTO', 'SPARES', 'OIL DIPSTICK', 10000000, 30, 50)," +
            "(633, 'KITHIMU AUTO', 'SPARES', 'OIL MIRROR', 10000000, 50, 100)," +
            "(634, 'KITHIMU AUTO', 'SPARES', 'OIL PUMP - SMALL', 10000000, 190, 250)," +
            "(635, 'KITHIMU AUTO', 'SPARES', 'OIL SEAL KIT', 10000000, 100, 200)," +
            "(636, 'KITHIMU AUTO', 'SPARES', 'OIL SEAL 28', 10000000, 25, 50)," +
            "(637, 'KITHIMU AUTO', 'SPARES', 'OIL SEAL 34', 10000000, 30, 50)," +
            "(638, 'KITHIMU AUTO', 'SPARES', 'PISTON KIT', 10000000, 480, 550)," +
            "(639, 'KITHIMU AUTO', 'SPARES', 'PISTON KIT SPECIAL', 10000000, 750, 1000)," +
            "(640, 'KITHIMU AUTO', 'SPARES', 'PISTON RINGS', 10000000, 160, 250)," +
            "(641, 'KITHIMU AUTO', 'SPARES', 'PISTON RING WP', 10000000, 300, 350)," +
            "(642, 'KITHIMU AUTO', 'SPARES', 'PISTON RING SPECIAL', 10000000, 350, 500)," +
            "(643, 'KITHIMU AUTO', 'SPARES', 'PLUG K16', 10000000, 175, 250)," +
            "(644, 'KITHIMU AUTO', 'SPARES', 'PLUG', 10000000, 50, 100)," +
            "(645, 'KITHIMU AUTO', 'SPARES', 'PLUG CAP', 10000000, 25, 50)," +
            "(646, 'KITHIMU AUTO', 'SPARES', 'PUSH ROD', 10000000, 100, 150)," +
            "(647, 'KITHIMU AUTO', 'SPARES', 'RADIO', 10000000, 1300, 1400)," +
            "(648, 'KITHIMU AUTO', 'SPARES', 'REAR AXLE', 10000000, 120, 150)," +
            "(649, 'KITHIMU AUTO', 'SPARES', 'REGULATOR 4 WIRE', 10000000, 200, 300)," +
            "(650, 'KITHIMU AUTO', 'SPARES', 'RIM - FRONT', 10000000, 3000, 3350)," +
            "(651, 'KITHIMU AUTO', 'SPARES', 'RIM - REAR', 10000000, 3800, 4300)," +
            "(652, 'KITHIMU AUTO', 'SPARES', 'SCREW DRIVER', 10000000, 100, 130)," +
            "(653, 'KITHIMU AUTO', 'SPARES', 'SEAT', 10000000, 1680, 2000)," +
            "(654, 'KITHIMU AUTO', 'SPARES', 'SEAT COVER', 10000000, 217, 300)," +
            "(655, 'KITHIMU AUTO', 'SPARES', 'SHOCK  - REAR', 10000000, 1650, 2000)," +
            "(656, 'KITHIMU AUTO', 'SPARES', 'SHOCK - FRONT', 10000000, 3000, 3500)," +
            "(657, 'KITHIMU AUTO', 'SPARES', 'SHOCK DYQ7', 10000000, 2450, 3000)," +
            "(658, 'KITHIMU AUTO', 'SPARES', 'SHOCK BOOT RUBBER', 10000000, 60, 100)," +
            "(659, 'KITHIMU AUTO', 'SPARES', 'SHOCK LIFTER', 10000000, 140, 200)," +
            "(660, 'KITHIMU AUTO', 'SPARES', 'SHOCK SEAL', 10000000, 33, 50)," +
            "(661, 'KITHIMU AUTO', 'SPARES', 'SHOCK SPRING', 10000000, 1200, 1400)," +
            "(662, 'KITHIMU AUTO', 'SPARES', 'SIDE COVER', 10000000, 500, 750)," +
            "(663, 'KITHIMU AUTO', 'SPARES', 'SIDE COVER ORIGINAL', 10000000, 1100, 1400)," +
            "(664, 'KITHIMU AUTO', 'SPARES', 'SIDE COVER LOCK', 10000000, 34, 50)," +
            "(665, 'KITHIMU AUTO', 'SPARES', 'SIDE MIRROR', 10000000, 95, 130)," +
            "(666, 'KITHIMU AUTO', 'SPARES', 'SIDE MIRROR - CAPTAIN', 10000000, 140, 150)," +
            "(667, 'KITHIMU AUTO', 'SPARES', 'SIDE MIRROR - CHROME', 10000000, 225, 250)," +
            "(668, 'KITHIMU AUTO', 'SPARES', 'SIDE STAND', 10000000, 120, 200)," +
            "(669, 'KITHIMU AUTO', 'SPARES', 'SILICONE', 10000000, 45, 60)," +
            "(670, 'KITHIMU AUTO', 'SPARES', 'SPEED CABLE', 10000000, 100, 150)," +
            "(671, 'KITHIMU AUTO', 'SPARES', 'SPEED GEAR', 10000000, 150, 200)," +
            "(672, 'KITHIMU AUTO', 'SPARES', 'SPRAY', 10000000, 220, 300)," +
            "(673, 'KITHIMU AUTO', 'SPARES', 'SPRING - BRAKE', 10000000, 15, 30)," +
            "(674, 'KITHIMU AUTO', 'SPARES', 'SPRING - MAIN', 10000000, 25, 50)," +
            "(675, 'KITHIMU AUTO', 'SPARES', 'SPROCKET - FRONT', 10000000, 60, 100)," +
            "(676, 'KITHIMU AUTO', 'SPARES', 'SPROCKET - REAR', 10000000, 400, 450)," +
            "(677, 'KITHIMU AUTO', 'SPARES', 'SPROCKET BUSH', 10000000, 100, 200)," +
            "(678, 'KITHIMU AUTO', 'SPARES', 'SPROCKET LOCK', 10000000, 20, 30)," +
            "(679, 'KITHIMU AUTO', 'SPARES', 'SPROCKET SHAFT', 10000000, 200, 250)," +
            "(680, 'KITHIMU AUTO', 'SPARES', 'SPROCKET SITTING', 10000000, 450, 600)," +
            "(681, 'KITHIMU AUTO', 'SPARES', 'SPROCKET STANDS', 10000000, 50, 100)," +
            "(682, 'KITHIMU AUTO', 'SPARES', 'STAND HOOK', 10000000, 15, 50)," +
            "(683, 'KITHIMU AUTO', 'SPARES', 'STARTER BRUSH', 10000000, 150, 200)," +
            "(684, 'KITHIMU AUTO', 'SPARES', 'STARTER MOTOR', 10000000, 1200, 1500)," +
            "(685, 'KITHIMU AUTO', 'SPARES', 'STEERING GUARD', 10000000, 450, 600)," +
            "(686, 'KITHIMU AUTO', 'SPARES', 'STEERING', 10000000, 600, 700)," +
            "(687, 'KITHIMU AUTO', 'SPARES', 'STEERING LOCK', 10000000, 50, 100)," +
            "(688, 'KITHIMU AUTO', 'SPARES', 'STEERING STEM', 10000000, 530, 650)," +
            "(689, 'KITHIMU AUTO', 'SPARES', 'STEERING SUPPORTER', 10000000, 150, 200)," +
            "(690, 'KITHIMU AUTO', 'SPARES', 'STICKER', 10000000, 35, 50)," +
            "(691, 'KITHIMU AUTO', 'SPARES', 'STICKER - SKYGO', 10000000, 200, 250)," +
            "(692, 'KITHIMU AUTO', 'SPARES', 'SUPER GLUE', 10000000, 18, 40)," +
            "(693, 'KITHIMU AUTO', 'SPARES', 'TAIL FLAP', 10000000, 80, 100)," +
            "(694, 'KITHIMU AUTO', 'SPARES', 'TAIL LAMP', 10000000, 800, 1000)," +
            "(695, 'KITHIMU AUTO', 'SPARES', 'TAIL LAMP – ORIGINAL', 10000000, 1200, 1500)," +
            "(696, 'KITHIMU AUTO', 'SPARES', 'TAIL LAMP - SKYGO', 10000000, 1800, 2250)," +
            "(697, 'KITHIMU AUTO', 'SPARES', 'TAIL LAMP BULB', 10000000, 12, 30)," +
            "(698, 'KITHIMU AUTO', 'SPARES', 'TAIL LENS', 10000000, 250, 300)," +
            "(699, 'KITHIMU AUTO', 'SPARES', 'TAIL MUDGUARD', 10000000, 200, 250)," +
            "(700, 'KITHIMU AUTO', 'SPARES', 'TANK BUSH', 10000000, 10, 50)," +
            "(701, 'KITHIMU AUTO', 'SPARES', 'TANK CAP - ROUND', 10000000, 330, 400)," +
            "(702, 'KITHIMU AUTO', 'SPARES', 'TANK CAP - SQUARE', 10000000, 180, 300)," +
            "(703, 'KITHIMU AUTO', 'SPARES', 'TANK CAP 125', 10000000, 150, 250)," +
            "(704, 'KITHIMU AUTO', 'SPARES', 'TIE WRAP', 10000000, 4, 10)," +
            "(705, 'KITHIMU AUTO', 'SPARES', 'TOP BRIDGE', 10000000, 450, 500)," +
            "(706, 'KITHIMU AUTO', 'SPARES', 'TOP NUT', 10000000, 30, 50)," +
            "(707, 'KITHIMU AUTO', 'SPARES', 'TUBE', 10000000, 190, 250)," +
            "(708, 'KITHIMU AUTO', 'SPARES', 'TUBE 3.50/4.10', 10000000, 300, 350)," +
            "(709, 'KITHIMU AUTO', 'SPARES', 'TUBE 13/14', 10000000, 800, 1000)," +
            "(710, 'KITHIMU AUTO', 'SPARES', 'TUBE - MTR', 10000000, 280, 330)," +
            "(711, 'KITHIMU AUTO', 'SPARES', 'TUBELESS NOZLE - CAR', 10000000, 30, 50)," +
            "(712, 'KITHIMU AUTO', 'SPARES', 'TUBELESS NOZLE - BODA', 10000000, 50, 80)," +
            "(713, 'KITHIMU AUTO', 'SPARES', 'TUPPET – SMALL', 10000000, 280, 400)," +
            "(714, 'KITHIMU AUTO', 'SPARES', 'TUPPET BOLT', 10000000, 15, 30)," +
            "(715, 'KITHIMU AUTO', 'SPARES', 'TYRE 2.75 X 18 CC', 10000000, 1900, 2100)," +
            "(716, 'KITHIMU AUTO', 'SPARES', 'TYRE 2.75 X 18 CORDIAL', 10000000, 1900, 2300)," +
            "(717, 'KITHIMU AUTO', 'SPARES', 'TYRE 2.75 X 18 YAWADA', 10000000, 1500, 1900)," +
            "(718, 'KITHIMU AUTO', 'SPARES', 'TYRE 3.00 X 17', 10000000, 1700, 2200)," +
            "(719, 'KITHIMU AUTO', 'SPARES', 'TYRE 3.00 X 18 CC', 10000000, 2000, 2300)," +
            "(720, 'KITHIMU AUTO', 'SPARES', 'TYRE 3.00 X 18 CORDIAL', 10000000, 2400, 2900)," +
            "(721, 'KITHIMU AUTO', 'SPARES', 'TYRE 3.00 X 18 MRF', 10000000, 3300, 3750)," +
            "(722, 'KITHIMU AUTO', 'SPARES', 'TYRE 3.50 X 18 TUBELESS', 10000000, 2300, 2750)," +
            "(723, 'KITHIMU AUTO', 'SPARES', 'TYRE 3.50 X 18 TVS', 10000000, 3300, 3800)," +
            "(724, 'KITHIMU AUTO', 'SPARES', 'TYRE SEAL', 10000000, 2, 30)," +
            "(725, 'KITHIMU AUTO', 'SPARES', 'VALVE GUIDE', 10000000, 120, 250)," +
            "(726, 'KITHIMU AUTO', 'SPARES', 'VALVE SEAL', 10000000, 25, 50)," +
            "(727, 'KITHIMU AUTO', 'SPARES', 'VALVE SET', 10000000, 150, 200)," +
            "(728, 'KITHIMU AUTO', 'SPARES', 'VALVE SET CP', 10000000, 200, 250)," +
            "(729, 'KITHIMU AUTO', 'SPARES', 'VALVE SET WP', 10000000, 250, 300)," +
            "(730, 'KITHIMU AUTO', 'SPARES', 'WINDSCREEN BIG', 10000000, 650, 800)," +
            "(731, 'KITHIMU AUTO', 'SPARES', 'WINDSCREEN SMALL', 10000000, 500, 650)," +
            "(732, 'KITHIMU AUTO', 'SPARES', 'WORLD CLASS AB - BIG', 10000000, 140, 200)," +
            "(733, 'KITHIMU AUTO', 'SPARES', 'WORLD CLASS AB - SMALL', 10000000, 70, 100)," +
            "(734, 'KITHIMU AUTO', 'PWS PARTS', 'ACCELERATOR', 10000000, 70, 100)," +
            "(735, 'KITHIMU AUTO', 'PWS PARTS', 'AIR CLEANER', 10000000, 150, 200)," +
            "(736, 'KITHIMU AUTO', 'PWS PARTS', 'ALLEN KEY 3 & 4', 10000000, 100, 120)," +
            "(737, 'KITHIMU AUTO', 'PWS PARTS', 'BAR NUTS', 10000000, 20, 50)," +
            "(738, 'KITHIMU AUTO', 'PWS PARTS', 'BLOCK SCREWS', 10000000, 10, 20)," +
            "(739, 'KITHIMU AUTO', 'PWS PARTS', 'BLOCK SET 272/268', 10000000, 1600, 2000)," +
            "(740, 'KITHIMU AUTO', 'PWS PARTS', 'CARBURATOR', 10000000, 1300, 1500)," +
            "(741, 'KITHIMU AUTO', 'PWS PARTS', 'CARBURATOR KIT', 10000000, 280, 450)," +
            "(742, 'KITHIMU AUTO', 'PWS PARTS', 'CARBURATOR SCREW', 10000000, 70, 100)," +
            "(743, 'KITHIMU AUTO', 'PWS PARTS', 'CHAIN CRFTP S20', 10000000, 1000, 1300)," +
            "(744, 'KITHIMU AUTO', 'PWS PARTS', 'CHAIN CRFTP S24', 10000000, 1150, 1400)," +
            "(745, 'KITHIMU AUTO', 'PWS PARTS', 'CHAIN ORGN S20', 10000000, 1750, 2000)," +
            "(746, 'KITHIMU AUTO', 'PWS PARTS', 'CHAIN ORGN S24', 10000000, 1800, 2000)," +
            "(747, 'KITHIMU AUTO', 'PWS PARTS', 'CHOKE', 10000000, 50, 100)," +
            "(748, 'KITHIMU AUTO', 'PWS PARTS', 'CLUTCH DRUM', 10000000, 400, 500)," +
            "(749, 'KITHIMU AUTO', 'PWS PARTS', 'CORN ROD', 10000000, 400, 500)," +
            "(750, 'KITHIMU AUTO', 'PWS PARTS', 'CRANKSHAFT CRFTP', 10000000, 1800, 2050)," +
            "(751, 'KITHIMU AUTO', 'PWS PARTS', 'CRANKSHAFT ORD', 10000000, 1500, 1750)," +
            "(752, 'KITHIMU AUTO', 'PWS PARTS', 'EXHAUST', 10000000, 700, 900)," +
            "(753, 'KITHIMU AUTO', 'PWS PARTS', 'EXHAUST GASKET', 10000000, 70, 100)," +
            "(754, 'KITHIMU AUTO', 'PWS PARTS', 'EXHAUST HOLDER', 10000000, 50, 100)," +
            "(755, 'KITHIMU AUTO', 'PWS PARTS', 'EXHAUST SCREW', 10000000, 20, 40)," +
            "(756, 'KITHIMU AUTO', 'PWS PARTS', 'FILE S.O', 10000000, 180, 220)," +
            "(757, 'KITHIMU AUTO', 'PWS PARTS', 'FILE MEDIUM', 10000000, 130, 180)," +
            "(758, 'KITHIMU AUTO', 'PWS PARTS', 'FILE SMALL', 10000000, 100, 150)," +
            "(759, 'KITHIMU AUTO', 'PWS PARTS', 'FLYWHEEL NUT', 10000000, 50, 80)," +
            "(760, 'KITHIMU AUTO', 'PWS PARTS', 'FUEL FILTER', 10000000, 100, 150)," +
            "(761, 'KITHIMU AUTO', 'PWS PARTS', 'FUEL PIPE', 10000000, 60, 100)," +
            "(762, 'KITHIMU AUTO', 'PWS PARTS', 'GASKET SET', 10000000, 70, 100)," +
            "(763, 'KITHIMU AUTO', 'PWS PARTS', 'GASKET SET ORIJI', 10000000, 100, 200)," +
            "(764, 'KITHIMU AUTO', 'PWS PARTS', 'HANDLE SCREW', 10000000, 10, 20)," +
            "(765, 'KITHIMU AUTO', 'PWS PARTS', 'MOULTING', 10000000, 70, 100)," +
            "(766, 'KITHIMU AUTO', 'PWS PARTS', 'MOULTING ORIJI', 10000000, 150, 200)," +
            "(767, 'KITHIMU AUTO', 'PWS PARTS', 'MOULTING SCREW', 10000000, 10, 20)," +
            "(768, 'KITHIMU AUTO', 'PWS PARTS', 'NOSE KIT', 10000000, 400, 500)," +
            "(769, 'KITHIMU AUTO', 'PWS PARTS', 'OIL GEAR', 10000000, 50, 100)," +
            "(770, 'KITHIMU AUTO', 'PWS PARTS', 'OIL GEAR 0', 10000000, 80, 150)," +
            "(771, 'KITHIMU AUTO', 'PWS PARTS', 'OIL GUIDE', 10000000, 70, 100)," +
            "(772, 'KITHIMU AUTO', 'PWS PARTS', 'OIL PUMP', 10000000, 700, 1000)," +
            "(773, 'KITHIMU AUTO', 'PWS PARTS', 'OIL SEAL', 10000000, 70, 100)," +
            "(774, 'KITHIMU AUTO', 'PWS PARTS', 'PISTON BEARING', 10000000, 80, 120)," +
            "(775, 'KITHIMU AUTO', 'PWS PARTS', 'PISTON KIT 268/72', 10000000, 450, 550)," +
            "(776, 'KITHIMU AUTO', 'PWS PARTS', 'PISTON KIT 272 0RIJI', 10000000, 1300, 1500)," +
            "(777, 'KITHIMU AUTO', 'PWS PARTS', 'PISTON RING 272', 10000000, 70, 150)," +
            "(778, 'KITHIMU AUTO', 'PWS PARTS', 'PLUG', 10000000, 100, 150)," +
            "(779, 'KITHIMU AUTO', 'PWS PARTS', 'PLUG OREGON', 10000000, 180, 250)," +
            "(780, 'KITHIMU AUTO', 'PWS PARTS', 'PLUG SPANNER', 10000000, 100, 150)," +
            "(781, 'KITHIMU AUTO', 'PWS PARTS', 'SIDE BEARING', 10000000, 120, 200)," +
            "(782, 'KITHIMU AUTO', 'PWS PARTS', 'SPROCKET', 10000000, 50, 100)," +
            "(783, 'KITHIMU AUTO', 'PWS PARTS', 'STARTER ROPE', 10000000, 30, 50)," +
            "(784, 'KITHIMU AUTO', 'PWS PARTS', 'STARTER SPRING', 10000000, 100, 150)," +
            "(785, 'KITHIMU AUTO', 'PWS PARTS', 'TAPE MEASURE', 10000000, 80, 130)," +
            "(786, 'KITHIMU AUTO', 'BOLT/SPANNER', 'TOP COVER SCREW', 10000000, 25, 50)," +
            "(787, 'KITHIMU AUTO', 'BOLT/SPANNER', '10 - 11 FIXED', 10000000, 100, 130)," +
            "(788, 'KITHIMU AUTO', 'BOLT/SPANNER', '10 COMBINED', 10000000, 90, 130)," +
            "(789, 'KITHIMU AUTO', 'BOLT/SPANNER', '12  - 13 RING', 10000000, 130, 180)," +
            "(790, 'KITHIMU AUTO', 'BOLT/SPANNER', '12 - 13 FIXED', 10000000, 130, 180)," +
            "(791, 'KITHIMU AUTO', 'BOLT/SPANNER', '12 COMBINED', 10000000, 120, 170)," +
            "(792, 'KITHIMU AUTO', 'BOLT/SPANNER', '13 COMBINED', 10000000, 130, 180)," +
            "(793, 'KITHIMU AUTO', 'BOLT/SPANNER', '14 - 15 FIXED', 10000000, 140, 200)," +
            "(794, 'KITHIMU AUTO', 'BOLT/SPANNER', '14 COMBINED', 10000000, 130, 200)," +
            "(795, 'KITHIMU AUTO', 'BOLT/SPANNER', '16 - 17 FIXED', 10000000, 160, 220)," +
            "(796, 'KITHIMU AUTO', 'BOLT/SPANNER', '16 - 17 RING', 10000000, 160, 200)," +
            "(797, 'KITHIMU AUTO', 'BOLT/SPANNER', '16 COMBINED', 10000000, 160, 220)," +
            "(798, 'KITHIMU AUTO', 'BOLT/SPANNER', '17 COMBINED', 10000000, 160, 220)," +
            "(799, 'KITHIMU AUTO', 'BOLT/SPANNER', '18 - 19 RING', 10000000, 180, 230)," +
            "(800, 'KITHIMU AUTO', 'BOLT/SPANNER', '8 X 1.5', 10000000, 5, 10)," +
            "(801, 'KITHIMU AUTO', 'BOLT/SPANNER', '8 X 1', 10000000, 5, 10)," +
            "(802, 'KITHIMU AUTO', 'BOLT/SPANNER', 1013, 10000000, 15, 30)," +
            "(803, 'KITHIMU AUTO', 'BOLT/SPANNER', '10 X 0.5', 10000000, 4, 10)," +
            "(804, 'KITHIMU AUTO', 'BOLT/SPANNER', '10 X 1', 10000000, 5, 10)," +
            "(805, 'KITHIMU AUTO', 'BOLT/SPANNER', '10 X 1 R', 10000000, 5, 10)," +
            "(806, 'KITHIMU AUTO', 'BOLT/SPANNER', '10 X 1.5', 10000000, 5, 10)," +
            "(807, 'KITHIMU AUTO', 'BOLT/SPANNER', '10 X 1.5 R', 10000000, 6, 10)," +
            "(808, 'KITHIMU AUTO', 'BOLT/SPANNER', '10 X 2', 10000000, 6, 10)," +
            "(809, 'KITHIMU AUTO', 'BOLT/SPANNER', '10 X 3', 10000000, 10, 20)," +
            "(810, 'KITHIMU AUTO', 'BOLT/SPANNER', '11 X 2', 10000000, 12, 25)," +
            "(811, 'KITHIMU AUTO', 'BOLT/SPANNER', '12 SHOCK', 10000000, 24, 40)," +
            "(812, 'KITHIMU AUTO', 'BOLT/SPANNER', '13 X 1', 10000000, 8, 20)," +
            "(813, 'KITHIMU AUTO', 'BOLT/SPANNER', '13 X 1.5', 10000000, 10, 20)," +
            "(814, 'KITHIMU AUTO', 'BOLT/SPANNER', '13 X 2', 10000000, 12, 20)," +
            "(815, 'KITHIMU AUTO', 'BOLT/SPANNER', '13 X 3', 10000000, 17, 30)," +
            "(816, 'KITHIMU AUTO', 'BOLT/SPANNER', '13 X 4', 10000000, 20, 40)," +
            "(817, 'KITHIMU AUTO', 'BOLT/SPANNER', '14 X 1', 10000000, 10, 20)," +
            "(818, 'KITHIMU AUTO', 'BOLT/SPANNER', '14 X 1.5', 10000000, 10, 20)," +
            "(819, 'KITHIMU AUTO', 'BOLT/SPANNER', '14 X 2', 10000000, 18, 25)," +
            "(820, 'KITHIMU AUTO', 'BOLT/SPANNER', '14 X 4.5', 10000000, 40, 70)," +
            "(821, 'KITHIMU AUTO', 'BOLT/SPANNER', '14 SHOCK', 10000000, 20, 40)," +
            "(822, 'KITHIMU AUTO', 'BOLT/SPANNER', '17 X 2', 10000000, 20, 30)," +
            "(823, 'KITHIMU AUTO', 'BOLT/SPANNER', 'NUT 10', 10000000, 2, 5)," +
            "(824, 'KITHIMU AUTO', 'BOLT/SPANNER', 'NUT 13', 10000000, 3, 5)," +
            "(825, 'KITHIMU AUTO', 'BOLT/SPANNER', 'NUT 14', 10000000, 5, 10)," +
            "(826, 'KITHIMU AUTO', 'BOLT/SPANNER', 'NUT 17', 10000000, 15, 20)," +
            "(827, 'KITHIMU AUTO', 'BOLT/SPANNER', 'NUT 19', 10000000, 15, 25)," +
            "(828, 'KITHIMU AUTO', 'BOLT/SPANNER', 'WASHER SMALL', 10000000, 2, 5)," +
            "(829, 'KITHIMU AUTO', 'BOLT/SPANNER', 'WASHER BIG', 10000000, 5, 10)," +
            "(830, 'KITHIMU AUTO', 'OILS', 'ADVANCE AX3', 10000000, 402, 450)," +
            "(831, 'KITHIMU AUTO', 'OILS', 'ADVANCE AX5', 10000000, 456, 520)," +
            "(832, 'KITHIMU AUTO', 'OILS', 'ATF', 10000000, 165, 200)," +
            "(833, 'KITHIMU AUTO', 'OILS', 'BITUL 4T', 10000000, 260, 350)," +
            "(834, 'KITHIMU AUTO', 'OILS', 'CC', 10000000, 165, 200)," +
            "(835, 'KITHIMU AUTO', 'OILS', 'COOLANT', 10000000, 100, 200)," +
            "(836, 'KITHIMU AUTO', 'OILS', 'EURO BURNER', 10000000, 350, 450)," +
            "(837, 'KITHIMU AUTO', 'OILS', 'GREASE', 10000000, 200, 250)," +
            "(838, 'KITHIMU AUTO', 'OILS', 'HELIX ½', 10000000, 210, 250)," +
            "(839, 'KITHIMU AUTO', 'OILS', 'LACHEKA ½', 10000000, 135, 160)," +
            "(840, 'KITHIMU AUTO', 'OILS', 'LACHEKA L', 10000000, 246, 300)," +
            "(841, 'KITHIMU AUTO', 'OILS', 'MOTOR OIL', 10000000, 370, 420)," +
            "(842, 'KITHIMU AUTO', 'OILS', 'MOTOROL L', 10000000, 435, 500)," +
            "(843, 'KITHIMU AUTO', 'OILS', 'P. SAFARI ½', 10000000, 200, 230)," +
            "(844, 'KITHIMU AUTO', 'OILS', 'P. SAFARI L', 10000000, 331, 400)," +
            "(845, 'KITHIMU AUTO', 'OILS', 'POWEREX', 10000000, 140, 200)," +
            "(846, 'KITHIMU AUTO', 'OILS', 'TYRE SEALANT', 10000000, 115, 150)," +
            "(847, 'IRIGA', 'SPARES', '1512 BEARING', 10000000, 100, 150)," +
            "(848, 'IRIGA', 'SPARES', '6001 BEARING', 10000000, 50, 100)," +
            "(849, 'IRIGA', 'SPARES', '6004 BEARING', 10000000, 65, 100)," +
            "(850, 'IRIGA', 'SPARES', '6005 BEARING', 10000000, 100, 150)," +
            "(851, 'IRIGA', 'SPARES', '6006 BEARING', 10000000, 100, 150)," +
            "(852, 'IRIGA', 'SPARES', '6202 BEARING', 10000000, 50, 100)," +
            "(853, 'IRIGA', 'SPARES', '6203 BEARING', 10000000, 150, 200)," +
            "(854, 'IRIGA', 'SPARES', '6204 BEARING', 10000000, 150, 200)," +
            "(855, 'IRIGA', 'SPARES', '6206 BEARING', 10000000, 200, 300)," +
            "(856, 'IRIGA', 'SPARES', '6301 BEARING', 10000000, 70, 100)," +
            "(857, 'IRIGA', 'SPARES', '6301 BEARING - CAPTAIN', 10000000, 80, 120)," +
            "(858, 'IRIGA', 'SPARES', '6302 BEARING', 10000000, 70, 100)," +
            "(859, 'IRIGA', 'SPARES', '6302 BEARING - CAPTAIN', 10000000, 80, 120)," +
            "(860, 'IRIGA', 'SPARES', '6328 BEARING', 10000000, 180, 250)," +
            "(861, 'IRIGA', 'SPARES', 'ACC CABLE', 10000000, 115, 150)," +
            "(862, 'IRIGA', 'SPARES', 'AIR CLEANER', 10000000, 850, 1000)," +
            "(863, 'IRIGA', 'SPARES', 'AIR CLEANER JOINT', 10000000, 100, 150)," +
            "(864, 'IRIGA', 'SPARES', 'ARM BUSH', 10000000, 50, 100)," +
            "(865, 'IRIGA', 'SPARES', 'B.B PLASTIC SET', 10000000, 450, 600)," +
            "(866, 'IRIGA', 'SPARES', 'B.B PLASTIC SET ORIGINAL', 10000000, 1000, 1200)," +
            "(867, 'IRIGA', 'SPARES', 'BALL RACE SET', 10000000, 200, 250)," +
            "(868, 'IRIGA', 'SPARES', 'BATTERY N7', 10000000, 1350, 1600)," +
            "(869, 'IRIGA', 'SPARES', 'BATTERY N9', 10000000, 1450, 1800)," +
            "(870, 'IRIGA', 'SPARES', 'BATTERY STRIP', 10000000, 30, 50)," +
            "(871, 'IRIGA', 'SPARES', 'BATTERY WIRE', 10000000, 20, 30)," +
            "(872, 'IRIGA', 'SPARES', 'BLOCK SET', 10000000, 1850, 2200)," +
            "(873, 'IRIGA', 'SPARES', 'BRAKE ARM', 10000000, 130, 150)," +
            "(874, 'IRIGA', 'SPARES', 'BRAKE CABLE', 10000000, 90, 120)," +
            "(875, 'IRIGA', 'SPARES', 'BRAKE PEDAL', 10000000, 220, 280)," +
            "(876, 'IRIGA', 'SPARES', 'BRAKE PEDAL SHOE', 10000000, 150, 200)," +
            "(877, 'IRIGA', 'SPARES', 'BRAKE ROD', 10000000, 60, 100)," +
            "(878, 'IRIGA', 'SPARES', 'BRAKE SHOE', 10000000, 210, 250)," +
            "(879, 'IRIGA', 'SPARES', 'BRAKE SWITCH', 10000000, 20, 70)," +
            "(880, 'IRIGA', 'SPARES', 'BULB HOLDER - FRONT', 10000000, 80, 150)," +
            "(881, 'IRIGA', 'SPARES', 'BULB HOLDER - REAR', 10000000, 30, 80)," +
            "(882, 'IRIGA', 'SPARES', 'BULL BAR CLAMPS', 10000000, 80, 150)," +
            "(883, 'IRIGA', 'SPARES', 'BULL BAR SET - SPORT', 10000000, 1800, 2250)," +
            "(884, 'IRIGA', 'SPARES', 'CAMSHAFT', 10000000, 450, 600)," +
            "(885, 'IRIGA', 'SPARES', 'CAP TAPPET', 10000000, 50, 80)," +
            "(886, 'IRIGA', 'SPARES', 'CARBURATOR', 10000000, 700, 1000)," +
            "(887, 'IRIGA', 'SPARES', 'CARBURATOR JOINT', 10000000, 100, 150)," +
            "(888, 'IRIGA', 'SPARES', 'CARRIER', 10000000, 5000, 5500)," +
            "(889, 'IRIGA', 'SPARES', 'CDI PRO', 10000000, 280, 320)," +
            "(890, 'IRIGA', 'SPARES', 'CELLONOID', 10000000, 200, 300)," +
            "(891, 'IRIGA', 'SPARES', 'CENTRE AXLE', 10000000, 90, 150)," +
            "(892, 'IRIGA', 'SPARES', 'CENTRE BOLT', 10000000, 140, 200)," +
            "(893, 'IRIGA', 'SPARES', 'CHAIN', 10000000, 370, 450)," +
            "(894, 'IRIGA', 'SPARES', 'CHAIN - CAPTAIN', 10000000, 420, 500)," +
            "(895, 'IRIGA', 'SPARES', 'CHAIN ADJUSTER', 10000000, 100, 150)," +
            "(896, 'IRIGA', 'SPARES', 'CHAIN LOCK', 10000000, 20, 30)," +
            "(897, 'IRIGA', 'SPARES', 'CHEST GUARD', 10000000, 600, 700)," +
            "(898, 'IRIGA', 'SPARES', 'CLUTCH 5H', 10000000, 520, 700)," +
            "(899, 'IRIGA', 'SPARES', 'CLUTCH 6H', 10000000, 720, 850)," +
            "(900, 'IRIGA', 'SPARES', 'CLUTCH BOX', 10000000, 500, 650)," +
            "(901, 'IRIGA', 'SPARES', 'CLUTCH CABLE', 10000000, 125, 150)," +
            "(902, 'IRIGA', 'SPARES', 'CLUTCH DISC', 10000000, 250, 300)," +
            "(903, 'IRIGA', 'SPARES', 'CLUTCH PLATE', 10000000, 140, 250)," +
            "(904, 'IRIGA', 'SPARES', 'CLUTCH PLATE - MTR', 10000000, 190, 350)," +
            "(905, 'IRIGA', 'SPARES', 'CLUTCH RELEASER', 10000000, 100, 150)," +
            "(906, 'IRIGA', 'SPARES', 'CRANKCASE', 10000000, 1950, 2250)," +
            "(907, 'IRIGA', 'SPARES', 'CRANKSHAFT', 10000000, 2400, 2700)," +
            "(908, 'IRIGA', 'SPARES', 'CYLINDER HEAD', 10000000, 3750, 4000)," +
            "(909, 'IRIGA', 'SPARES', 'DASHBOARD', 10000000, 800, 1000)," +
            "(910, 'IRIGA', 'SPARES', 'DASHBOARD CP', 10000000, 950, 1200)," +
            "(911, 'IRIGA', 'SPARES', 'DASHBOARD DAYUN', 10000000, 1200, 1500)," +
            "(912, 'IRIGA', 'SPARES', 'DIM BULB', 10000000, 10, 20)," +
            "(913, 'IRIGA', 'SPARES', 'DRUM GEAR SHIFT', 10000000, 300, 400)," +
            "(914, 'IRIGA', 'SPARES', 'ENGINE BOLT', 10000000, 30, 50)," +
            "(915, 'IRIGA', 'SPARES', 'ENGINE COVER - LEFT', 10000000, 1550, 1900)," +
            "(916, 'IRIGA', 'SPARES', 'ENGINE COVER - RIGHT', 10000000, 650, 850)," +
            "(917, 'IRIGA', 'SPARES', 'ENGINE ROCK ARMS', 10000000, 220, 280)," +
            "(918, 'IRIGA', 'SPARES', 'EXHAUST', 10000000, 2000, 2500)," +
            "(919, 'IRIGA', 'SPARES', 'EXHAUST CLAMPS', 10000000, 80, 120)," +
            "(920, 'IRIGA', 'SPARES', 'EXHAUST SLEEVE', 10000000, 30, 50)," +
            "(921, 'IRIGA', 'SPARES', 'FLASH UNIT', 10000000, 50, 100)," +
            "(922, 'IRIGA', 'SPARES', 'FOOT REST BAR', 10000000, 480, 550)," +
            "(923, 'IRIGA', 'SPARES', 'FORK GEAR', 10000000, 300, 450)," +
            "(924, 'IRIGA', 'SPARES', 'FRONT AXLE', 10000000, 80, 120)," +
            "(925, 'IRIGA', 'SPARES', 'FRONT GUARD', 10000000, 150, 250)," +
            "(926, 'IRIGA', 'SPARES', 'FUEL FILTER', 10000000, 30, 50)," +
            "(927, 'IRIGA', 'SPARES', 'FUEL TAP', 10000000, 140, 200)," +
            "(928, 'IRIGA', 'SPARES', 'FUSE BOX', 10000000, 45, 60)," +
            "(929, 'IRIGA', 'SPARES', 'GASKET - BLOCK', 10000000, 30, 50)," +
            "(930, 'IRIGA', 'SPARES', 'GASKET - CLUTCH', 10000000, 30, 50)," +
            "(931, 'IRIGA', 'SPARES', 'GASKET - EXHAUST', 10000000, 20, 50)," +
            "(932, 'IRIGA', 'SPARES', 'GASKET - MAGNETO', 10000000, 17, 50)," +
            "(933, 'IRIGA', 'SPARES', 'GASKET SET', 10000000, 180, 220)," +
            "(934, 'IRIGA', 'SPARES', 'GEAR BOX', 10000000, 1370, 1700)," +
            "(935, 'IRIGA', 'SPARES', 'GEAR LEVER', 10000000, 150, 200)," +
            "(936, 'IRIGA', 'SPARES', 'GEAR NO 3', 10000000, 300, 350)," +
            "(937, 'IRIGA', 'SPARES', 'GEAR SELECTOR', 10000000, 180, 250)," +
            "(938, 'IRIGA', 'SPARES', 'GEAR SENSOR DAYUN', 10000000, 200, 250)," +
            "(939, 'IRIGA', 'SPARES', 'GEAR SENSOR', 10000000, 120, 180)," +
            "(940, 'IRIGA', 'SPARES', 'GEAR STAR', 10000000, 100, 150)," +
            "(941, 'IRIGA', 'SPARES', 'GLOVES', 10000000, 200, 250)," +
            "(942, 'IRIGA', 'SPARES', 'GRILL', 10000000, 1200, 1500)," +
            "(943, 'IRIGA', 'SPARES', 'GRILL - DAYUN', 10000000, 1300, 1750)," +
            "(944, 'IRIGA', 'SPARES', 'HALOGEN BULB', 10000000, 70, 100)," +
            "(945, 'IRIGA', 'SPARES', 'HAND GRIP', 10000000, 55, 100)," +
            "(946, 'IRIGA', 'SPARES', 'HAND GRIP GUARD', 10000000, 30, 50)," +
            "(947, 'IRIGA', 'SPARES', 'HAND SHIELD', 10000000, 450, 500)," +
            "(948, 'IRIGA', 'SPARES', 'HAND SHIELD LEVER', 10000000, 210, 250)," +
            "(949, 'IRIGA', 'SPARES', 'HANDLE LEVER', 10000000, 100, 120)," +
            "(950, 'IRIGA', 'SPARES', 'HANDLE LEVER GUARD', 10000000, 20, 30)," +
            "(951, 'IRIGA', 'SPARES', 'HANDLE SWITCH', 10000000, 300, 350)," +
            "(952, 'IRIGA', 'SPARES', 'HEAD BULB', 10000000, 25, 50)," +
            "(953, 'IRIGA', 'SPARES', 'HEAD GUARD', 10000000, 225, 250)," +
            "(954, 'IRIGA', 'SPARES', 'HEADLIGHT', 10000000, 780, 900)," +
            "(955, 'IRIGA', 'SPARES', 'HEADLIGHT – CG125', 10000000, 600, 900)," +
            "(956, 'IRIGA', 'SPARES', 'HEADLIGHT - SPORTLIGHT', 10000000, 250, 350)," +
            "(957, 'IRIGA', 'SPARES', 'HEADLIGHT BULB', 10000000, 110, 150)," +
            "(958, 'IRIGA', 'SPARES', 'HEADLIGHT CASE', 10000000, 150, 200)," +
            "(959, 'IRIGA', 'SPARES', 'HEADLIGHT GLASS', 10000000, 70, 150)," +
            "(960, 'IRIGA', 'SPARES', 'HELMET DY 104', 10000000, 1200, 1400)," +
            "(961, 'IRIGA', 'SPARES', 'HELMET GLASS', 10000000, 70, 150)," +
            "(962, 'IRIGA', 'SPARES', 'HELMET GLASS -TINTED', 10000000, 150, 200)," +
            "(963, 'IRIGA', 'SPARES', 'HORN – MEDIUM', 10000000, 120, 150)," +
            "(964, 'IRIGA', 'SPARES', 'HORN - PARARIRA', 10000000, 500, 600)," +
            "(965, 'IRIGA', 'SPARES', 'HORN - SMALL', 10000000, 90, 120)," +
            "(966, 'IRIGA', 'SPARES', 'HUB COVER - FRONT', 10000000, 700, 900)," +
            "(967, 'IRIGA', 'SPARES', 'HUB COVER - REAR', 10000000, 840, 1000)," +
            "(968, 'IRIGA', 'SPARES', 'HUB LOCK', 10000000, 20, 40)," +
            "(969, 'IRIGA', 'SPARES', 'IGNITION COIL', 10000000, 230, 300)," +
            "(970, 'IRIGA', 'SPARES', 'IGNITION COVER', 10000000, 50, 100)," +
            "(971, 'IRIGA', 'SPARES', 'IGNITION SWITCH', 10000000, 240, 300)," +
            "(972, 'IRIGA', 'SPARES', 'IGNITION SWITCH - CAP', 10000000, 315, 350)," +
            "(973, 'IRIGA', 'SPARES', 'INDICATOR', 10000000, 100, 120)," +
            "(974, 'IRIGA', 'SPARES', 'INDICATOR - CAPTAIN', 10000000, 120, 150)," +
            "(975, 'IRIGA', 'SPARES', 'INDICATOR BULB', 10000000, 8, 20)," +
            "(976, 'IRIGA', 'SPARES', 'INNER CLUTCH CABLE', 10000000, 25, 50)," +
            "(977, 'IRIGA', 'SPARES', 'INSULATION TAPE', 10000000, 32, 50)," +
            "(978, 'IRIGA', 'SPARES', 'INSURANCE HOLDER', 10000000, 40, 50)," +
            "(979, 'IRIGA', 'SPARES', 'KICK START', 10000000, 200, 250)," +
            "(980, 'IRIGA', 'SPARES', 'KICK START SHAFT', 10000000, 420, 550)," +
            "(981, 'IRIGA', 'SPARES', 'KICK SHAFT SPRING', 10000000, 50, 100)," +
            "(982, 'IRIGA', 'SPARES', 'LED SWEET', 10000000, 60, 100)," +
            "(983, 'IRIGA', 'SPARES', 'MAGNET', 10000000, 750, 1000)," +
            "(984, 'IRIGA', 'SPARES', 'MAGNETO COIL', 10000000, 750, 850)," +
            "(985, 'IRIGA', 'SPARES', 'MAGNETO ROLLERS', 10000000, 75, 200)," +
            "(986, 'IRIGA', 'SPARES', 'MAIN CABLE', 10000000, 750, 800)," +
            "(987, 'IRIGA', 'SPARES', 'MAIN STAND', 10000000, 550, 600)," +
            "(988, 'IRIGA', 'SPARES', 'MAIN STAND AXLE', 10000000, 100, 150)," +
            "(989, 'IRIGA', 'SPARES', 'MIRROR HOLDER', 10000000, 60, 120)," +
            "(990, 'IRIGA', 'SPARES', 'MUD FLAP', 10000000, 130, 200)," +
            "(991, 'IRIGA', 'SPARES', 'MUDGUARD', 10000000, 270, 350)," +
            "(992, 'IRIGA', 'SPARES', 'MUDGUARD CP', 10000000, 600, 800)," +
            "(993, 'IRIGA', 'SPARES', 'OIL CAP', 10000000, 140, 250)," +
            "(994, 'IRIGA', 'SPARES', 'OIL DIPSTICK', 10000000, 40, 50)," +
            "(995, 'IRIGA', 'SPARES', 'OIL DRAINER BOLT', 10000000, 50, 80)," +
            "(996, 'IRIGA', 'SPARES', 'OIL MIRROR', 10000000, 50, 100)," +
            "(997, 'IRIGA', 'SPARES', 'OIL PUMP - SMALL', 10000000, 250, 300)," +
            "(998, 'IRIGA', 'SPARES', 'OIL SEAL 28', 10000000, 25, 50)," +
            "(999, 'IRIGA', 'SPARES', 'OIL SEAL 34', 10000000, 30, 50)," +
            "(1000, 'IRIGA', 'SPARES', 'PISTON KIT', 10000000, 480, 550)";
    private final String INSERT_ITEM___ = "INSERT INTO "+TABLE_ITEM+" " +
            "("+ COLUMN_ITEM_ID +","+ COLUMN_ITEM_STATION + ","+ COLUMN_ITEM_CATEGORY +","+ COLUMN_ITEM_NAME + ","+
            COLUMN_ITEM_QUANTITY +","+ COLUMN_ITEM_BUYPRICE + ","+ COLUMN_ITEM_SELLPRICE + ") VALUES" +
            "(1001, 'IRIGA', 'SPARES', 'PISTON RINGS', 10000000, 185, 250)," +
            "(1002, 'IRIGA', 'SPARES', 'PLUG', 10000000, 60, 100)," +
            "(1003, 'IRIGA', 'SPARES', 'PLUG CAP', 10000000, 30, 50)," +
            "(1004, 'IRIGA', 'SPARES', 'PUMP CONNECTOR', 10000000, 55, 100)," +
            "(1005, 'IRIGA', 'SPARES', 'PUSH ROD', 10000000, 100, 150)," +
            "(1006, 'IRIGA', 'SPARES', 'RADIO - MTUNGI', 10000000, 1100, 1400)," +
            "(1007, 'IRIGA', 'SPARES', 'RADIO – SMALL', 10000000, 1300, 1500)," +
            "(1008, 'IRIGA', 'SPARES', 'RADIO - TYRE', 10000000, 3300, 3600)," +
            "(1009, 'IRIGA', 'SPARES', 'REAR AXLE', 10000000, 120, 150)," +
            "(1010, 'IRIGA', 'SPARES', 'REAR AXLE - LONG', 10000000, 150, 180)," +
            "(1011, 'IRIGA', 'SPARES', 'REGULATOR 4 WIRE', 10000000, 360, 400)," +
            "(1012, 'IRIGA', 'SPARES', 'RIM - FRONT', 10000000, 3600, 4200)," +
            "(1013, 'IRIGA', 'SPARES', 'RIM - REAR', 10000000, 5300, 6100)," +
            "(1014, 'IRIGA', 'SPARES', 'SEAT', 10000000, 1800, 2000)," +
            "(1015, 'IRIGA', 'SPARES', 'SEAT COVER', 10000000, 250, 300)," +
            "(1016, 'IRIGA', 'SPARES', 'SHOCK  - REAR', 10000000, 1850, 2000)," +
            "(1017, 'IRIGA', 'SPARES', 'SHOCK BOOT RUBBER', 10000000, 75, 100)," +
            "(1018, 'IRIGA', 'SPARES', 'SHOCK DYQ7 – REAR', 10000000, 2450, 3000)," +
            "(1019, 'IRIGA', 'SPARES', 'SHOCK - MONO', 10000000, 2800, 3200)," +
            "(1020, 'IRIGA', 'SPARES', 'SHOCK - FRONT', 10000000, 3200, 3500)," +
            "(1021, 'IRIGA', 'SPARES', 'SHOCK SEAL', 10000000, 38, 50)," +
            "(1022, 'IRIGA', 'SPARES', 'SIDE COVER', 10000000, 450, 700)," +
            "(1023, 'IRIGA', 'SPARES', 'SIDE COVER ORIGINAL', 10000000, 1100, 1500)," +
            "(1024, 'IRIGA', 'SPARES', 'SIDE COVER LOCK', 10000000, 34, 50)," +
            "(1025, 'IRIGA', 'SPARES', 'SIDE MIRROR - BLACK', 10000000, 100, 120)," +
            "(1026, 'IRIGA', 'SPARES', 'SIDE MIRROR - CHROM', 10000000, 225, 250)," +
            "(1027, 'IRIGA', 'SPARES', 'SIDE MIRROR – R VIU', 10000000, 185, 225)," +
            "(1028, 'IRIGA', 'SPARES', 'SIDE STAND', 10000000, 120, 200)," +
            "(1029, 'IRIGA', 'SPARES', 'SILICONE', 10000000, 45, 60)," +
            "(1030, 'IRIGA', 'SPARES', 'SPEED CABLE', 10000000, 60, 120)," +
            "(1031, 'IRIGA', 'SPARES', 'SPRAYS', 10000000, 160, 300)," +
            "(1032, 'IRIGA', 'SPARES', 'SPRING - BRAKE', 10000000, 15, 30)," +
            "(1033, 'IRIGA', 'SPARES', 'SPRING - MAIN', 10000000, 25, 50)," +
            "(1034, 'IRIGA', 'SPARES', 'SPROCKET - FRONT', 10000000, 60, 100)," +
            "(1035, 'IRIGA', 'SPARES', 'SPROCKET - FRONT 17T', 10000000, 200, 250)," +
            "(1036, 'IRIGA', 'SPARES', 'SPROCKET - REAR', 10000000, 370, 450)," +
            "(1037, 'IRIGA', 'SPARES', 'SPROCKET - REAR 47/49T', 10000000, 550, 700)," +
            "(1038, 'IRIGA', 'SPARES', 'SPROCKET - REAR 56T', 10000000, 600, 700)," +
            "(1039, 'IRIGA', 'SPARES', 'SPROCKET BUSH', 10000000, 100, 200)," +
            "(1040, 'IRIGA', 'SPARES', 'SPROCKET LOCK -F', 10000000, 20, 30)," +
            "(1041, 'IRIGA', 'SPARES', 'SPROCKET SHAFT', 10000000, 200, 250)," +
            "(1042, 'IRIGA', 'SPARES', 'SPROCKET SITTING', 10000000, 450, 600)," +
            "(1043, 'IRIGA', 'SPARES', 'SPROCKET STAND', 10000000, 50, 100)," +
            "(1044, 'IRIGA', 'SPARES', 'STAND HOOK', 10000000, 30, 50)," +
            "(1045, 'IRIGA', 'SPARES', 'STARTER BRUSH', 10000000, 150, 200)," +
            "(1046, 'IRIGA', 'SPARES', 'STARTER MOTOR', 10000000, 950, 1250)," +
            "(1047, 'IRIGA', 'SPARES', 'STEERING', 10000000, 600, 700)," +
            "(1048, 'IRIGA', 'SPARES', 'STEERING LOCK', 10000000, 50, 100)," +
            "(1049, 'IRIGA', 'SPARES', 'STEERING STEM', 10000000, 550, 700)," +
            "(1050, 'IRIGA', 'SPARES', 'STICKER', 10000000, 25, 50)," +
            "(1051, 'IRIGA', 'SPARES', 'SUPER GLUE', 10000000, 30, 40)," +
            "(1052, 'IRIGA', 'SPARES', 'SURPUSS', 10000000, 600, 700)," +
            "(1053, 'IRIGA', 'SPARES', 'SWING ARM METAL', 10000000, 1680, 2000)," +
            "(1054, 'IRIGA', 'SPARES', 'TAIL FLAP', 10000000, 150, 200)," +
            "(1055, 'IRIGA', 'SPARES', 'TAIL LAMP', 10000000, 750, 900)," +
            "(1056, 'IRIGA', 'SPARES', 'TAIL LAMP – ORIGINAL', 10000000, 1400, 1600)," +
            "(1057, 'IRIGA', 'SPARES', 'TAIL LAMP BULB', 10000000, 12, 30)," +
            "(1058, 'IRIGA', 'SPARES', 'TAIL LAMP BULB - LED', 10000000, 100, 120)," +
            "(1059, 'IRIGA', 'SPARES', 'TAIL MUDGUARD', 10000000, 180, 250)," +
            "(1060, 'IRIGA', 'SPARES', 'TANK', 10000000, 3800, 4300)," +
            "(1061, 'IRIGA', 'SPARES', 'TANK BUSH', 10000000, 10, 50)," +
            "(1062, 'IRIGA', 'SPARES', 'TANK CAP - ROUND', 10000000, 350, 500)," +
            "(1063, 'IRIGA', 'SPARES', 'TANK CAP - SQUARE', 10000000, 220, 300)," +
            "(1064, 'IRIGA', 'SPARES', 'TIE WRAP', 10000000, 5, 10)," +
            "(1065, 'IRIGA', 'SPARES', 'TOP BRIDGE', 10000000, 380, 600)," +
            "(1066, 'IRIGA', 'SPARES', 'TOP NUT', 10000000, 40, 70)," +
            "(1067, 'IRIGA', 'SPARES', 'TUBE', 10000000, 200, 250)," +
            "(1068, 'IRIGA', 'SPARES', 'TUBE - MTR', 10000000, 280, 350)," +
            "(1069, 'IRIGA', 'SPARES', 'TUBE - 3.50', 10000000, 350, 400)," +
            "(1070, 'IRIGA', 'SPARES', 'TUBELESS NOZZLE', 10000000, 50, 70)," +
            "(1071, 'IRIGA', 'SPARES', 'TUITOR', 10000000, 90, 150)," +
            "(1072, 'IRIGA', 'SPARES', 'TUPPET', 10000000, 280, 400)," +
            "(1073, 'IRIGA', 'SPARES', 'TUPPET BOLT', 10000000, 15, 30)," +
            "(1074, 'IRIGA', 'SPARES', 'TYRE 2.75 X 18', 10000000, 1750, 2000)," +
            "(1075, 'IRIGA', 'SPARES', 'TYRE 3.00 X 18 CC', 10000000, 2200, 2500)," +
            "(1076, 'IRIGA', 'SPARES', 'TYRE 3.00 X 18 MRF', 10000000, 3400, 3750)," +
            "(1077, 'IRIGA', 'SPARES', 'TYRE 3.50 X 18 MRF', 10000000, 3600, 4200)," +
            "(1078, 'IRIGA', 'SPARES', 'TYRE 3.00 X 18 SUPERRUN', 10000000, 2900, 3400)," +
            "(1079, 'IRIGA', 'SPARES', 'TYRE 3.50 X 18 SUPERRUN', 10000000, 3300, 3800)," +
            "(1080, 'IRIGA', 'SPARES', 'TYRE SEAL', 10000000, 8, 30)," +
            "(1081, 'IRIGA', 'SPARES', 'UNIFORM', 10000000, 1100, 1500)," +
            "(1082, 'IRIGA', 'SPARES', 'VALVE GUIDE', 10000000, 180, 250)," +
            "(1083, 'IRIGA', 'SPARES', 'VALVE SEAL', 10000000, 20, 50)," +
            "(1084, 'IRIGA', 'SPARES', 'VALVE SET', 10000000, 200, 250)," +
            "(1085, 'IRIGA', 'SPARES', 'VALVE SET ORIGINAL', 10000000, 250, 300)," +
            "(1086, 'IRIGA', 'SPARES', 'WINDSCREEN - BIG', 10000000, 750, 950)," +
            "(1087, 'IRIGA', 'SPARES', 'WINDSCREEN – SMALL', 10000000, 550, 700)," +
            "(1088, 'IRIGA', 'SPARES', 'WORLD CLASS AB S', 10000000, 70, 120)," +
            "(1089, 'IRIGA', 'PWS PARTS', 'ACCELERATOR', 10000000, 80, 100)," +
            "(1090, 'IRIGA', 'PWS PARTS', 'AIR CLEANER', 10000000, 200, 250)," +
            "(1091, 'IRIGA', 'PWS PARTS', 'AIR CLEANER TABLE', 10000000, 80, 120)," +
            "(1092, 'IRIGA', 'PWS PARTS', 'AIR CLEANER CLIPS', 10000000, 20, 50)," +
            "(1093, 'IRIGA', 'PWS PARTS', 'AIR CLEANER COVER', 10000000, 100, 150)," +
            "(1094, 'IRIGA', 'PWS PARTS', 'ALLEN KEY 3 & 4', 10000000, 100, 120)," +
            "(1095, 'IRIGA', 'PWS PARTS', 'BAR NUTS', 10000000, 30, 50)," +
            "(1096, 'IRIGA', 'PWS PARTS', 'BAR STANDS', 10000000, 40, 80)," +
            "(1097, 'IRIGA', 'PWS PARTS', 'BLOCK SCREW', 10000000, 10, 20)," +
            "(1098, 'IRIGA', 'PWS PARTS', 'BLOCK SET 268', 10000000, 2800, 3200)," +
            "(1099, 'IRIGA', 'PWS PARTS', 'BLOCK SET 272', 10000000, 1800, 2000)," +
            "(1100, 'IRIGA', 'PWS PARTS', 'CARBURATOR', 10000000, 1500, 1650)," +
            "(1101, 'IRIGA', 'PWS PARTS', 'CARBURATOR KIT', 10000000, 250, 400)," +
            "(1102, 'IRIGA', 'PWS PARTS', 'CARBURATOR SCREW', 10000000, 60, 100)," +
            "(1103, 'IRIGA', 'PWS PARTS', 'CHAIN ORGN S20', 10000000, 1830, 2000)," +
            "(1104, 'IRIGA', 'PWS PARTS', 'CHAIN ORGN S24', 10000000, 1950, 2100)," +
            "(1105, 'IRIGA', 'PWS PARTS', 'CHOKE', 10000000, 50, 120)," +
            "(1106, 'IRIGA', 'PWS PARTS', 'CLUTCH', 10000000, 650, 800)," +
            "(1107, 'IRIGA', 'PWS PARTS', 'CLUTCH DRUM', 10000000, 400, 500)," +
            "(1108, 'IRIGA', 'PWS PARTS', 'CORN ROD', 10000000, 400, 500)," +
            "(1109, 'IRIGA', 'PWS PARTS', 'CRANKCASE', 10000000, 2400, 2650)," +
            "(1110, 'IRIGA', 'PWS PARTS', 'CRANKSHAFT CRFTP', 10000000, 1800, 2050)," +
            "(1111, 'IRIGA', 'PWS PARTS', 'ELBOW', 10000000, 100, 150)," +
            "(1112, 'IRIGA', 'PWS PARTS', 'EXHAUST', 10000000, 700, 900)," +
            "(1113, 'IRIGA', 'PWS PARTS', 'EXHAUST GASKET', 10000000, 70, 100)," +
            "(1114, 'IRIGA', 'PWS PARTS', 'EXHAUST HOLDER', 10000000, 70, 100)," +
            "(1115, 'IRIGA', 'PWS PARTS', 'EXHAUST SCREW', 10000000, 20, 40)," +
            "(1116, 'IRIGA', 'PWS PARTS', 'FILE - SMALL', 10000000, 100, 150)," +
            "(1117, 'IRIGA', 'PWS PARTS', 'FILE M.O', 10000000, 150, 200)," +
            "(1118, 'IRIGA', 'PWS PARTS', 'FLYWHEEL', 10000000, 900, 1100)," +
            "(1119, 'IRIGA', 'PWS PARTS', 'FLYWHEEL NUT O', 10000000, 100, 120)," +
            "(1120, 'IRIGA', 'PWS PARTS', 'FUEL FILTER', 10000000, 80, 100)," +
            "(1121, 'IRIGA', 'PWS PARTS', 'FUEL PIPE', 10000000, 120, 150)," +
            "(1122, 'IRIGA', 'PWS PARTS', 'GASKET SET O', 10000000, 120, 150)," +
            "(1123, 'IRIGA', 'PWS PARTS', 'HANDLE', 10000000, 600, 750)," +
            "(1124, 'IRIGA', 'PWS PARTS', 'HANDLE SCREW', 10000000, 10, 20)," +
            "(1125, 'IRIGA', 'PWS PARTS', 'IGNITION COIL SCREW', 10000000, 8, 20)," +
            "(1126, 'IRIGA', 'PWS PARTS', 'MOULTING', 10000000, 80, 120)," +
            "(1127, 'IRIGA', 'PWS PARTS', 'MOULTING ORIGINAL', 10000000, 150, 200)," +
            "(1128, 'IRIGA', 'PWS PARTS', 'MOULTING SCREW', 10000000, 10, 20)," +
            "(1129, 'IRIGA', 'PWS PARTS', 'NOSE KIT', 10000000, 450, 550)," +
            "(1130, 'IRIGA', 'PWS PARTS', 'OIL GEAR', 10000000, 60, 100)," +
            "(1131, 'IRIGA', 'PWS PARTS', 'OIL GEAR ORIGINAL', 10000000, 100, 150)," +
            "(1132, 'IRIGA', 'PWS PARTS', 'OIL GUIDE', 10000000, 100, 150)," +
            "(1133, 'IRIGA', 'PWS PARTS', 'OIL PUMP', 10000000, 750, 900)," +
            "(1134, 'IRIGA', 'PWS PARTS', 'OIL PUMP SCREW', 10000000, 100, 150)," +
            "(1135, 'IRIGA', 'PWS PARTS', 'OIL SEAL', 10000000, 70, 100)," +
            "(1136, 'IRIGA', 'PWS PARTS', 'PISTON BEARING', 10000000, 70, 100)," +
            "(1137, 'IRIGA', 'PWS PARTS', 'PISTON KIT 268', 10000000, 450, 500)," +
            "(1138, 'IRIGA', 'PWS PARTS', 'PISTON KIT 272', 10000000, 450, 550)," +
            "(1139, 'IRIGA', 'PWS PARTS', 'PISTON RING', 10000000, 70, 150)," +
            "(1140, 'IRIGA', 'PWS PARTS', 'PLUG', 10000000, 100, 150)," +
            "(1141, 'IRIGA', 'PWS PARTS', 'PLUG OREGON', 10000000, 180, 250)," +
            "(1142, 'IRIGA', 'PWS PARTS', 'PLUG CABLE', 10000000, 80, 100)," +
            "(1143, 'IRIGA', 'PWS PARTS', 'PLUG SPANNER', 10000000, 120, 150)," +
            "(1144, 'IRIGA', 'PWS PARTS', 'SIDE BEARING', 10000000, 100, 200)," +
            "(1145, 'IRIGA', 'PWS PARTS', 'SPACER', 10000000, 100, 150)," +
            "(1146, 'IRIGA', 'PWS PARTS', 'SPROCKET', 10000000, 100, 150)," +
            "(1147, 'IRIGA', 'PWS PARTS', 'STARTER COVER', 10000000, 600, 750)," +
            "(1148, 'IRIGA', 'PWS PARTS', 'STARTER HANDLE', 10000000, 50, 100)," +
            "(1149, 'IRIGA', 'PWS PARTS', 'STARTER ROPE', 10000000, 30, 50)," +
            "(1150, 'IRIGA', 'PWS PARTS', 'STARTER SCREW', 10000000, 15, 30)," +
            "(1151, 'IRIGA', 'PWS PARTS', 'STARTER SPRING', 10000000, 70, 100)," +
            "(1152, 'IRIGA', 'PWS PARTS', 'TOP COVER SCREW', 10000000, 30, 50)," +
            "(1153, 'IRIGA', 'PWS PARTS', 'TAPE MEASURE', 10000000, 90, 120)," +
            "(1154, 'IRIGA', 'BOLT/SPANNER', '10 - 11 FIXED', 10000000, 110, 130)," +
            "(1155, 'IRIGA', 'BOLT/SPANNER', '10 COMBINED', 10000000, 90, 130)," +
            "(1156, 'IRIGA', 'BOLT/SPANNER', '12  - 13 RING', 10000000, 130, 180)," +
            "(1157, 'IRIGA', 'BOLT/SPANNER', '12 - 13 FIXED', 10000000, 130, 180)," +
            "(1158, 'IRIGA', 'BOLT/SPANNER', '12 COMBINED', 10000000, 120, 180)," +
            "(1159, 'IRIGA', 'BOLT/SPANNER', '13 COMBINED', 10000000, 130, 180)," +
            "(1160, 'IRIGA', 'BOLT/SPANNER', '14 - 15 FIXED', 10000000, 140, 200)," +
            "(1161, 'IRIGA', 'BOLT/SPANNER', '14 COMBINED', 10000000, 130, 200)," +
            "(1162, 'IRIGA', 'BOLT/SPANNER', '16 - 17 FIXED', 10000000, 160, 220)," +
            "(1163, 'IRIGA', 'BOLT/SPANNER', '16 - 17 RING', 10000000, 160, 200)," +
            "(1164, 'IRIGA', 'BOLT/SPANNER', '16 COMBINED', 10000000, 160, 220)," +
            "(1165, 'IRIGA', 'BOLT/SPANNER', '17 COMBINED', 10000000, 160, 220)," +
            "(1166, 'IRIGA', 'BOLT/SPANNER', '18 - 19 FIXED', 10000000, 180, 250)," +
            "(1167, 'IRIGA', 'BOLT/SPANNER', '18 - 19 RING', 10000000, 180, 230)," +
            "(1168, 'IRIGA', 'BOLT/SPANNER', '18 COMBINED', 10000000, 180, 250)," +
            "(1169, 'IRIGA', 'BOLT/SPANNER', '19 COMBINED', 10000000, 180, 250)," +
            "(1170, 'IRIGA', 'BOLT/SPANNER', '30 - 32 FIXED', 10000000, 450, 500)," +
            "(1171, 'IRIGA', 'BOLT/SPANNER', '32 COMBINED', 10000000, 460, 500)," +
            "(1172, 'IRIGA', 'SPARES', 'BLADE', 10000000, 90, 110)," +
            "(1173, 'IRIGA', 'SPARES', 'CUTTING DISC S7', 10000000, 100, 150)," +
            "(1174, 'IRIGA', 'SPARES', 'CUTTING DISC S9', 10000000, 170, 220)," +
            "(1175, 'IRIGA', 'SPARES', 'GLIDING DISC', 10000000, 180, 220)," +
            "(1176, 'IRIGA', 'BOLT/SPANNER', '10 X 0.5', 10000000, 5, 10)," +
            "(1177, 'IRIGA', 'BOLT/SPANNER', '10 X 1.5', 10000000, 5, 10)," +
            "(1178, 'IRIGA', 'BOLT/SPANNER', '10 X 1', 10000000, 5, 10)," +
            "(1179, 'IRIGA', 'BOLT/SPANNER', '10 X 2', 10000000, 6, 10)," +
            "(1180, 'IRIGA', 'BOLT/SPANNER', '10 X 3', 10000000, 10, 20)," +
            "(1181, 'IRIGA', 'BOLT/SPANNER', '10 X 4', 10000000, 10, 30)," +
            "(1182, 'IRIGA', 'BOLT/SPANNER', '11 X 2', 10000000, 12, 30)," +
            "(1183, 'IRIGA', 'BOLT/SPANNER', '12 SHOCK', 10000000, 24, 40)," +
            "(1184, 'IRIGA', 'BOLT/SPANNER', '13 X 1.5', 10000000, 10, 20)," +
            "(1185, 'IRIGA', 'BOLT/SPANNER', '13 X 1', 10000000, 8, 20)," +
            "(1186, 'IRIGA', 'BOLT/SPANNER', '13 X 2', 10000000, 12, 20)," +
            "(1187, 'IRIGA', 'BOLT/SPANNER', '13 X 3', 10000000, 17, 30)," +
            "(1188, 'IRIGA', 'BOLT/SPANNER', '13 X 4', 10000000, 20, 40)," +
            "(1189, 'IRIGA', 'BOLT/SPANNER', '14 SHOCK', 10000000, 25, 40)," +
            "(1190, 'IRIGA', 'BOLT/SPANNER', '14 X 1.5', 10000000, 12, 20)," +
            "(1191, 'IRIGA', 'BOLT/SPANNER', '14 X 1', 10000000, 10, 20)," +
            "(1192, 'IRIGA', 'BOLT/SPANNER', '14 X 2 FINE', 10000000, 16, 30)," +
            "(1193, 'IRIGA', 'BOLT/SPANNER', '14 X 2', 10000000, 15, 30)," +
            "(1194, 'IRIGA', 'BOLT/SPANNER', '14 X 4.5', 10000000, 35, 70)," +
            "(1195, 'IRIGA', 'BOLT/SPANNER', '17 X 2', 10000000, 24, 40)," +
            "(1196, 'IRIGA', 'BOLT/SPANNER', '8 X 1.5', 10000000, 5, 10)," +
            "(1197, 'IRIGA', 'BOLT/SPANNER', '8 X 1', 10000000, 5, 10)," +
            "(1198, 'IRIGA', 'BOLT/SPANNER', 'NUT 10', 10000000, 2, 5)," +
            "(1199, 'IRIGA', 'BOLT/SPANNER', 'NUT 13', 10000000, 3, 5)," +
            "(1200, 'IRIGA', 'BOLT/SPANNER', 'NUT 13 JPN', 10000000, 10, 15)," +
            "(1201, 'IRIGA', 'BOLT/SPANNER', 'NUT 14', 10000000, 5, 10)," +
            "(1202, 'IRIGA', 'BOLT/SPANNER', 'NUT 17', 10000000, 15, 25)," +
            "(1203, 'IRIGA', 'BOLT/SPANNER', 'NUT 19', 10000000, 15, 30)," +
            "(1204, 'IRIGA', 'BOLT/SPANNER', 'WASHER', 10000000, 2, 5)," +
            "(1205, 'IRIGA', 'BOLT/SPANNER', 'WASHER 17/19', 10000000, 6, 10)," +
            "(1206, 'IRIGA', 'OILS', '13KG REFILL', 10000000, 2700, 3000)," +
            "(1207, 'IRIGA', 'OILS', '6KG REFILL', 10000000, 1270, 1350)," +
            "(1208, 'IRIGA', 'OILS', 'ADVANCE AX3', 10000000, 402, 450)," +
            "(1209, 'IRIGA', 'OILS', 'ADVANCE AX5', 10000000, 456, 500)," +
            "(1210, 'IRIGA', 'OILS', 'ATF', 10000000, 140, 200)," +
            "(1211, 'IRIGA', 'OILS', 'ATF L', 10000000, 325, 400)," +
            "(1212, 'IRIGA', 'OILS', 'BITUL 4T', 10000000, 280, 350)," +
            "(1213, 'IRIGA', 'OILS', 'B. ACID CE', 10000000, 80, 100)," +
            "(1214, 'IRIGA', 'OILS', 'B.WATER CE', 10000000, 60, 80)," +
            "(1215, 'IRIGA', 'OILS', 'CAPTAIN 4T', 10000000, 360, 420)," +
            "(1216, 'IRIGA', 'OILS', 'CC', 10000000, 165, 200)," +
            "(1217, 'IRIGA', 'OILS', 'DOT 4 250ML', 10000000, 220, 250)," +
            "(1218, 'IRIGA', 'OILS', 'DOT 4 500ML', 10000000, 445, 500)," +
            "(1219, 'IRIGA', 'OILS', 'GREASE', 10000000, 235, 300)," +
            "(1220, 'IRIGA', 'OILS', 'HELIX ½', 10000000, 210, 250)," +
            "(1221, 'IRIGA', 'OILS', 'HELIX L', 10000000, 402, 450)," +
            "(1222, 'IRIGA', 'OILS', 'K-LUBE', 10000000, 190, 220)," +
            "(1223, 'IRIGA', 'OILS', 'LACHEKA ½', 10000000, 145, 180)," +
            "(1224, 'IRIGA', 'OILS', 'MOTOR HONEY', 10000000, 220, 250)," +
            "(1225, 'IRIGA', 'OILS', 'P.SAFARI ½', 10000000, 200, 250)," +
            "(1226, 'IRIGA', 'OILS', 'P.SAFARI L', 10000000, 332, 450)," +
            "(1227, 'IRIGA', 'OILS', 'T. SPECIAL 2T', 10000000, 206, 230)," +
            "(1228, 'IRIGA', 'OILS', 'T. CLASSIC ½', 10000000, 217, 250)," +
            "(1229, 'IRIGA', 'OILS', 'T. CLASSIC L', 10000000, 406, 450)," +
            "(1230, 'IRIGA', 'OILS', 'TYRE SEALANT', 10000000, 100, 150)," +
            "(1231, 'KITHIMU', 'OILS', 'ADVANCE AX5', 10000000, 456, 520)," +
            "(1232, 'KITHIMU', 'OILS', 'ATF', 10000000, 165, 200)," +
            "(1233, 'KITHIMU', 'OILS', 'ATF/CC L', 10000000, 325, 400)," +
            "(1234, 'KITHIMU', 'OILS', 'B. ACID', 10000000, 87, 120)," +
            "(1235, 'KITHIMU', 'OILS', 'B.WATER', 10000000, 40, 70)," +
            "(1236, 'KITHIMU', 'OILS', 'BURNER', 10000000, 200, 350)," +
            "(1237, 'KITHIMU', 'OILS', 'CC ½', 10000000, 165, 200)," +
            "(1238, 'KITHIMU', 'OILS', 'COOLANT', 10000000, 100, 200)," +
            "(1239, 'KITHIMU', 'OILS', 'DOT 4 200 ML', 10000000, 110, 150)," +
            "(1240, 'KITHIMU', 'OILS', 'GREASE', 10000000, 200, 250)," +
            "(1241, 'KITHIMU', 'OILS', 'HELIX ½', 10000000, 210, 250)," +
            "(1242, 'KITHIMU', 'OILS', 'HELIX L', 10000000, 402, 450)," +
            "(1243, 'KITHIMU', 'OILS', 'HELIX 4L', 10000000, 1913, 2200)," +
            "(1244, 'KITHIMU', 'OILS', 'LACHEKA ½', 10000000, 135, 180)," +
            "(1245, 'KITHIMU', 'OILS', 'LACHEKA L', 10000000, 273, 300)," +
            "(1246, 'KITHIMU', 'OILS', 'MOTOR OIL', 10000000, 370, 420)," +
            "(1247, 'KITHIMU', 'OILS', 'P.SAFARI ½', 10000000, 200, 250)," +
            "(1248, 'KITHIMU', 'OILS', 'POWEREX ½', 10000000, 140, 200)," +
            "(1249, 'KITHIMU', 'OILS', 'RIMULA R3 5L', 10000000, 2388, 2600)," +
            "(1250, 'KITHIMU', 'OILS', 'RIMULA R3 L', 10000000, 472, 550)," +
            "(1251, 'KITHIMU', 'OILS', 'T.B. FLUID', 10000000, 225, 250)," +
            "(1252, 'KITHIMU', 'OILS', 'T.B. FLUID ½L', 10000000, 421, 500)," +
            "(1253, 'KITHIMU', 'OILS', 'T.CLASSIC 4L', 10000000, 1647, 1800)," +
            "(1254, 'KITHIMU', 'OILS', 'T.CLASSIC 5L', 10000000, 2053, 2400)," +
            "(1255, 'KITHIMU', 'OILS', 'T.CLASSIC L', 10000000, 407, 450)," +
            "(1256, 'KITHIMU', 'OILS', 'T.SPECIAL 2T', 10000000, 203, 230)," +
            "(1257, 'NGUSISHI', 'SPARES', '1512 BEARING', 10000000, 100, 150)," +
            "(1258, 'NGUSISHI', 'SPARES', '6001 BEARING', 10000000, 50, 100)," +
            "(1259, 'NGUSISHI', 'SPARES', '6004 BEARING', 10000000, 65, 100)," +
            "(1260, 'NGUSISHI', 'SPARES', '6202 BEARING', 10000000, 55, 100)," +
            "(1261, 'NGUSISHI', 'SPARES', '6301 BEARING', 10000000, 70, 100)," +
            "(1262, 'NGUSISHI', 'SPARES', '6302 BEARING', 10000000, 60, 100)," +
            "(1263, 'NGUSISHI', 'SPARES', 'ACC CABLE', 10000000, 110, 150)," +
            "(1264, 'NGUSISHI', 'SPARES', 'AIR CLEANER JOINT', 10000000, 120, 150)," +
            "(1265, 'NGUSISHI', 'SPARES', 'ARM BUSH', 10000000, 50, 75)," +
            "(1266, 'NGUSISHI', 'SPARES', 'B.B PLASTIC SET ORIGINAL', 10000000, 1100, 1250)," +
            "(1267, 'NGUSISHI', 'SPARES', 'BALL RACE SET', 10000000, 200, 250)," +
            "(1268, 'NGUSISHI', 'SPARES', 'BATTERY N7', 10000000, 1300, 1500)," +
            "(1269, 'NGUSISHI', 'SPARES', 'BATTERY N9', 10000000, 1400, 1700)," +
            "(1270, 'NGUSISHI', 'SPARES', 'BATTERY STRIP', 10000000, 40, 50)," +
            "(1271, 'NGUSISHI', 'SPARES', 'BATTERY WIRE', 10000000, 20, 30)," +
            "(1272, 'NGUSISHI', 'SPARES', 'BLOCK SET', 10000000, 1650, 2000)," +
            "(1273, 'NGUSISHI', 'SPARES', 'BRAKE ARM', 10000000, 130, 200)," +
            "(1274, 'NGUSISHI', 'SPARES', 'BRAKE CABLE', 10000000, 90, 120)," +
            "(1275, 'NGUSISHI', 'SPARES', 'BRAKE PEDAL', 10000000, 220, 280)," +
            "(1276, 'NGUSISHI', 'SPARES', 'BRAKE ROD', 10000000, 60, 100)," +
            "(1277, 'NGUSISHI', 'SPARES', 'BRAKE SHOE', 10000000, 190, 250)," +
            "(1278, 'NGUSISHI', 'SPARES', 'BRAKE SWITCH', 10000000, 30, 50)," +
            "(1279, 'NGUSISHI', 'SPARES', 'BULB HOLDER - FRONT', 10000000, 50, 100)," +
            "(1280, 'NGUSISHI', 'SPARES', 'BULB HOLDER - 3 PIN', 10000000, 100, 150)," +
            "(1281, 'NGUSISHI', 'SPARES', 'BULB HOLDER - REAR', 10000000, 50, 80)," +
            "(1282, 'NGUSISHI', 'SPARES', 'BULL BAR CLAMPS', 10000000, 200, 300)," +
            "(1283, 'NGUSISHI', 'SPARES', 'CAMSHAFT', 10000000, 450, 600)," +
            "(1284, 'NGUSISHI', 'SPARES', 'CAP TAPPET', 10000000, 50, 80)," +
            "(1285, 'NGUSISHI', 'SPARES', 'CARBURATOR', 10000000, 690, 900)," +
            "(1286, 'NGUSISHI', 'SPARES', 'CARBURATOR JOINT', 10000000, 100, 150)," +
            "(1287, 'NGUSISHI', 'SPARES', 'CDI PRO', 10000000, 280, 300)," +
            "(1288, 'NGUSISHI', 'SPARES', 'CELLONOID', 10000000, 240, 300)," +
            "(1289, 'NGUSISHI', 'SPARES', 'CENTRE AXLE', 10000000, 90, 150)," +
            "(1290, 'NGUSISHI', 'SPARES', 'CHAIN', 10000000, 440, 500)," +
            "(1291, 'NGUSISHI', 'SPARES', 'CHAIN ADJUSTER', 10000000, 100, 150)," +
            "(1292, 'NGUSISHI', 'SPARES', 'CHAIN LOCK', 10000000, 20, 30)," +
            "(1293, 'NGUSISHI', 'SPARES', 'CHAIN KIT', 10000000, 1100, 1350)," +
            "(1294, 'NGUSISHI', 'SPARES', 'CLUTCH 5H', 10000000, 600, 850)," +
            "(1295, 'NGUSISHI', 'SPARES', 'CLUTCH 6H', 10000000, 720, 850)," +
            "(1296, 'NGUSISHI', 'SPARES', 'CLUTCH BOX', 10000000, 500, 650)," +
            "(1297, 'NGUSISHI', 'SPARES', 'CLUTCH CABLE', 10000000, 120, 150)," +
            "(1298, 'NGUSISHI', 'SPARES', 'CLUTCH DISC', 10000000, 200, 300)," +
            "(1299, 'NGUSISHI', 'SPARES', 'CLUTCH PLATE', 10000000, 140, 300)," +
            "(1300, 'NGUSISHI', 'SPARES', 'CLUTCH RELEASER', 10000000, 90, 150)," +
            "(1301, 'NGUSISHI', 'SPARES', 'CRANKSHAFT', 10000000, 2400, 2700)," +
            "(1302, 'NGUSISHI', 'SPARES', 'CYLINDER HEAD', 10000000, 3600, 4000)," +
            "(1303, 'NGUSISHI', 'SPARES', 'DASHBOARD', 10000000, 750, 900)," +
            "(1304, 'NGUSISHI', 'SPARES', 'ENGINE ROCK ARMS', 10000000, 200, 280)," +
            "(1305, 'NGUSISHI', 'SPARES', 'EXHAUST SLEEVE', 10000000, 30, 50)," +
            "(1306, 'NGUSISHI', 'SPARES', 'FLASH UNIT', 10000000, 50, 100)," +
            "(1307, 'NGUSISHI', 'SPARES', 'FOOT REST BAR', 10000000, 500, 600)," +
            "(1308, 'NGUSISHI', 'SPARES', 'FOOT REST ORIGINAL', 10000000, 800, 1000)," +
            "(1309, 'NGUSISHI', 'SPARES', 'FRONT AXLE', 10000000, 80, 120)," +
            "(1310, 'NGUSISHI', 'SPARES', 'FRONT GUARD', 10000000, 150, 250)," +
            "(1311, 'NGUSISHI', 'SPARES', 'FUEL FILTER/CORK', 10000000, 30, 50)," +
            "(1312, 'NGUSISHI', 'SPARES', 'FUEL TAP', 10000000, 140, 200)," +
            "(1313, 'NGUSISHI', 'SPARES', 'FUSE BOX', 10000000, 35, 50)," +
            "(1314, 'NGUSISHI', 'SPARES', 'GASKET - BLOCK', 10000000, 30, 50)," +
            "(1315, 'NGUSISHI', 'SPARES', 'GASKET - CLUTCH', 10000000, 30, 50)," +
            "(1316, 'NGUSISHI', 'SPARES', 'GASKET - EXHAUST', 10000000, 20, 50)," +
            "(1317, 'NGUSISHI', 'SPARES', 'GASKET - MAGNETO', 10000000, 25, 50)," +
            "(1318, 'NGUSISHI', 'SPARES', 'GASKET SET', 10000000, 160, 200)," +
            "(1319, 'NGUSISHI', 'SPARES', 'GEAR BOX', 10000000, 1400, 1700)," +
            "(1320, 'NGUSISHI', 'SPARES', 'GEAR LEVER', 10000000, 160, 200)," +
            "(1321, 'NGUSISHI', 'SPARES', 'GEAR NO 3', 10000000, 260, 350)," +
            "(1322, 'NGUSISHI', 'SPARES', 'GEAR SELECTOR', 10000000, 245, 300)," +
            "(1323, 'NGUSISHI', 'SPARES', 'GEAR SENSOR', 10000000, 150, 250)," +
            "(1324, 'NGUSISHI', 'SPARES', 'GLOVES', 10000000, 180, 250)," +
            "(1325, 'NGUSISHI', 'SPARES', 'GRILL', 10000000, 1300, 1500)," +
            "(1326, 'NGUSISHI', 'SPARES', 'HALOGEN BULB', 10000000, 55, 120)," +
            "(1327, 'NGUSISHI', 'SPARES', 'HAND GRIP', 10000000, 55, 100)," +
            "(1328, 'NGUSISHI', 'SPARES', 'HAND GRIP GUARD', 10000000, 100, 160)," +
            "(1329, 'NGUSISHI', 'SPARES', 'HANDLE LEVER', 10000000, 75, 100)," +
            "(1330, 'NGUSISHI', 'SPARES', 'HANDLE SWITCH', 10000000, 275, 350)," +
            "(1331, 'NGUSISHI', 'SPARES', 'HEADLIGHT', 10000000, 550, 900)," +
            "(1332, 'NGUSISHI', 'SPARES', 'HEADLIGHT BULB', 10000000, 110, 150)," +
            "(1333, 'NGUSISHI', 'SPARES', 'HELMET', 10000000, 1200, 1400)," +
            "(1334, 'NGUSISHI', 'SPARES', 'HELMET GLASS', 10000000, 70, 100)," +
            "(1335, 'NGUSISHI', 'SPARES', 'HELMET GLASS - TINTED', 10000000, 150, 200)," +
            "(1336, 'NGUSISHI', 'SPARES', 'HORN - SMALL', 10000000, 90, 120)," +
            "(1337, 'NGUSISHI', 'SPARES', 'HUB COVER - FRONT', 10000000, 680, 1000)," +
            "(1338, 'NGUSISHI', 'SPARES', 'HUB COVER - REAR', 10000000, 800, 1000)," +
            "(1339, 'NGUSISHI', 'SPARES', 'IGNITION COIL', 10000000, 230, 300)," +
            "(1340, 'NGUSISHI', 'SPARES', 'IGNITION SWITCH', 10000000, 250, 300)," +
            "(1341, 'NGUSISHI', 'SPARES', 'IGNITION SWITCH CPTN', 10000000, 300, 350)," +
            "(1342, 'NGUSISHI', 'SPARES', 'INDICATOR', 10000000, 90, 120)," +
            "(1343, 'NGUSISHI', 'SPARES', 'INDICATOR BOXER', 10000000, 110, 150)," +
            "(1344, 'NGUSISHI', 'SPARES', 'INDICATOR BULB', 10000000, 8, 20)," +
            "(1345, 'NGUSISHI', 'SPARES', 'INNER CLUTCH CABLE', 10000000, 25, 50)," +
            "(1346, 'NGUSISHI', 'SPARES', 'INSULATION TAPE', 10000000, 30, 40)," +
            "(1347, 'NGUSISHI', 'SPARES', 'INSURANCE HOLDER', 10000000, 25, 50)," +
            "(1348, 'NGUSISHI', 'SPARES', 'LED SPORTLIGHT - BIG', 10000000, 250, 350)," +
            "(1349, 'NGUSISHI', 'SPARES', 'LED SPORTLIGHT - SMALL', 10000000, 200, 300)," +
            "(1350, 'NGUSISHI', 'SPARES', 'KICK START', 10000000, 200, 250)," +
            "(1351, 'NGUSISHI', 'SPARES', 'KICK START SHAFT', 10000000, 400, 500)," +
            "(1352, 'NGUSISHI', 'SPARES', 'MAGNET', 10000000, 850, 1000)," +
            "(1353, 'NGUSISHI', 'SPARES', 'MAGNETO COIL', 10000000, 750, 850)," +
            "(1354, 'NGUSISHI', 'SPARES', 'MAGNETO ROLLERS', 10000000, 85, 150)," +
            "(1355, 'NGUSISHI', 'SPARES', 'MAIN CABLE', 10000000, 735, 800)," +
            "(1356, 'NGUSISHI', 'SPARES', 'MAIN STAND', 10000000, 450, 500)," +
            "(1357, 'NGUSISHI', 'SPARES', 'MAIN STAND AXLE', 10000000, 100, 150)," +
            "(1358, 'NGUSISHI', 'SPARES', 'MIRROR HOLDER', 10000000, 80, 100)," +
            "(1359, 'NGUSISHI', 'SPARES', 'MUDGUARD', 10000000, 420, 750)," +
            "(1360, 'NGUSISHI', 'SPARES', 'MUD FLAP', 10000000, 175, 250)," +
            "(1361, 'NGUSISHI', 'SPARES', 'OIL CAP', 10000000, 140, 200)," +
            "(1362, 'NGUSISHI', 'SPARES', 'OIL DIPSTICK', 10000000, 40, 50)," +
            "(1363, 'NGUSISHI', 'SPARES', 'OIL PUMP - SMALL', 10000000, 190, 250)," +
            "(1364, 'NGUSISHI', 'SPARES', 'OIL SEAL 28', 10000000, 25, 50)," +
            "(1365, 'NGUSISHI', 'SPARES', 'OIL SEAL 34', 10000000, 30, 50)," +
            "(1366, 'NGUSISHI', 'SPARES', 'PISTON KIT', 10000000, 480, 550)," +
            "(1367, 'NGUSISHI', 'SPARES', 'PISTON RINGS', 10000000, 185, 300)," +
            "(1368, 'NGUSISHI', 'SPARES', 'PLUG', 10000000, 50, 100)," +
            "(1369, 'NGUSISHI', 'SPARES', 'PLUG CAP', 10000000, 25, 50)," +
            "(1370, 'NGUSISHI', 'SPARES', 'PUSH ROD', 10000000, 100, 150)," +
            "(1371, 'NGUSISHI', 'SPARES', 'REAR AXLE', 10000000, 100, 150)," +
            "(1372, 'NGUSISHI', 'SPARES', 'REAR AXLE LONG', 10000000, 120, 180)," +
            "(1373, 'NGUSISHI', 'SPARES', 'REGULATOR 4 WIRE', 10000000, 300, 350)," +
            "(1374, 'NGUSISHI', 'SPARES', 'RIM - FRONT', 10000000, 3800, 4300)," +
            "(1375, 'NGUSISHI', 'SPARES', 'RIM - REAR', 10000000, 3800, 4300)," +
            "(1376, 'NGUSISHI', 'SPARES', 'SEAT', 10000000, 1800, 2000)," +
            "(1377, 'NGUSISHI', 'SPARES', 'SHOCK BOOT RUBBER', 10000000, 60, 100)," +
            "(1378, 'NGUSISHI', 'SPARES', 'SHOCK LIFTER', 10000000, 120, 200)," +
            "(1379, 'NGUSISHI', 'SPARES', 'SHOCK SEAL', 10000000, 25, 50)," +
            "(1380, 'NGUSISHI', 'SPARES', 'SHOCK - FRONT', 10000000, 2800, 3200)," +
            "(1381, 'NGUSISHI', 'SPARES', 'SHOCK - REAR', 10000000, 1800, 2200)," +
            "(1382, 'NGUSISHI', 'SPARES', 'SHOCK REAR - DYQ7', 10000000, 2450, 3000)," +
            "(1383, 'NGUSISHI', 'SPARES', 'SIDE COVER 0RIGINAL', 10000000, 1430, 1650)," +
            "(1384, 'NGUSISHI', 'SPARES', 'SIDE COVER LOCK', 10000000, 34, 50)," +
            "(1385, 'NGUSISHI', 'SPARES', 'SIDE MIRROR - BLACK', 10000000, 95, 125)," +
            "(1386, 'NGUSISHI', 'SPARES', 'SIDE MIRROR - CHROM', 10000000, 210, 250)," +
            "(1387, 'NGUSISHI', 'SPARES', 'SIDE MIRROR – R VIU', 10000000, 175, 225)," +
            "(1388, 'NGUSISHI', 'SPARES', 'SIDE STAND', 10000000, 150, 250)," +
            "(1389, 'NGUSISHI', 'SPARES', 'SILICONE', 10000000, 45, 60)," +
            "(1390, 'NGUSISHI', 'SPARES', 'SPEED CABLE', 10000000, 85, 150)," +
            "(1391, 'NGUSISHI', 'SPARES', 'SPRING - BRAKE', 10000000, 15, 30)," +
            "(1392, 'NGUSISHI', 'SPARES', 'SPRING - MAIN', 10000000, 25, 50)," +
            "(1393, 'NGUSISHI', 'SPARES', 'SPROCKET - FRONT', 10000000, 65, 100)," +
            "(1394, 'NGUSISHI', 'SPARES', 'SPROCKET - REAR', 10000000, 420, 450)," +
            "(1395, 'NGUSISHI', 'SPARES', 'SPROCKET LOCK -F', 10000000, 15, 30)," +
            "(1396, 'NGUSISHI', 'SPARES', 'SPROCKET SHAFT', 10000000, 250, 350)," +
            "(1397, 'NGUSISHI', 'SPARES', 'SPROCKET SITTING', 10000000, 500, 600)," +
            "(1398, 'NGUSISHI', 'SPARES', 'SPROCKET STAND', 10000000, 60, 100)," +
            "(1399, 'NGUSISHI', 'SPARES', 'STAND HOOK', 10000000, 25, 50)," +
            "(1400, 'NGUSISHI', 'SPARES', 'STARTER BRUSH', 10000000, 150, 200)," +
            "(1401, 'NGUSISHI', 'SPARES', 'STARTER MOTOR', 10000000, 1000, 1250)," +
            "(1402, 'NGUSISHI', 'SPARES', 'STEERING', 10000000, 600, 700)," +
            "(1403, 'NGUSISHI', 'SPARES', 'STEERING STEM', 10000000, 530, 700)," +
            "(1404, 'NGUSISHI', 'SPARES', 'SUPER GLUE', 10000000, 20, 30)," +
            "(1405, 'NGUSISHI', 'SPARES', 'SWING ARM METAL', 10000000, 1600, 2000)," +
            "(1406, 'NGUSISHI', 'SPARES', 'TAIL LAMP – ORIGINAL', 10000000, 1650, 2000)," +
            "(1407, 'NGUSISHI', 'SPARES', 'TAIL LAMP BULB', 10000000, 12, 30)," +
            "(1408, 'NGUSISHI', 'SPARES', 'TAIL LENS', 10000000, 250, 300)," +
            "(1409, 'NGUSISHI', 'SPARES', 'TAIL MUDGUARD', 10000000, 200, 300)," +
            "(1410, 'NGUSISHI', 'SPARES', 'TANK CAP - ROUND', 10000000, 450, 500)," +
            "(1411, 'NGUSISHI', 'SPARES', 'TANK CAP - SQUARE', 10000000, 350, 450)," +
            "(1412, 'NGUSISHI', 'SPARES', 'TOP BRIDGE', 10000000, 530, 600)," +
            "(1413, 'NGUSISHI', 'SPARES', 'TOP NUT', 10000000, 40, 50)," +
            "(1414, 'NGUSISHI', 'SPARES', 'TUBE', 10000000, 200, 250)," +
            "(1415, 'NGUSISHI', 'SPARES', 'TUBE 3.50', 10000000, 300, 350)," +
            "(1416, 'NGUSISHI', 'SPARES', 'TUBE - MTR', 10000000, 260, 300)," +
            "(1417, 'NGUSISHI', 'SPARES', 'TUBELESS NOZZLE - BODA', 10000000, 35, 80)," +
            "(1418, 'NGUSISHI', 'SPARES', 'TUBELESS NOZZLE - CAR', 10000000, 20, 50)," +
            "(1419, 'NGUSISHI', 'SPARES', 'TUPPET – SMALL', 10000000, 280, 400)," +
            "(1420, 'NGUSISHI', 'SPARES', 'TUPPET BOLT', 10000000, 15, 25)," +
            "(1421, 'NGUSISHI', 'SPARES', 'TYRE 2.75 X 18 CAPTAIN', 10000000, 1700, 2000)," +
            "(1422, 'NGUSISHI', 'SPARES', 'TYRE 3.00 X 17 YAWADA', 10000000, 2100, 2400)," +
            "(1423, 'NGUSISHI', 'SPARES', 'TYRE 3.00 X 18 CC', 10000000, 2000, 2250)," +
            "(1424, 'NGUSISHI', 'SPARES', 'TYRE 3.00 X 18 CORDIAL', 10000000, 2100, 2400)," +
            "(1425, 'NGUSISHI', 'SPARES', 'TYRE 3.00 X 18 MRF', 10000000, 3300, 3700)," +
            "(1426, 'NGUSISHI', 'SPARES', 'TYRE 3.00 X 18 SUPERRUN', 10000000, 2550, 2850)," +
            "(1427, 'NGUSISHI', 'SPARES', 'TYRE 3.50 X 18', 10000000, 3400, 3700)," +
            "(1428, 'NGUSISHI', 'SPARES', 'TYRE SEAL', 10000000, 12, 30)," +
            "(1429, 'NGUSISHI', 'SPARES', 'VALVE GUIDE', 10000000, 120, 300)," +
            "(1430, 'NGUSISHI', 'SPARES', 'VALVE LOCK', 10000000, 25, 50)," +
            "(1431, 'NGUSISHI', 'SPARES', 'VALVE SEAL', 10000000, 25, 50)," +
            "(1432, 'NGUSISHI', 'SPARES', 'VALVE SET', 10000000, 200, 250)," +
            "(1433, 'NGUSISHI', 'SPARES', 'WINDSCREEN B', 10000000, 750, 950)," +
            "(1434, 'NGUSISHI', 'SPARES', 'WORLD CLASS AB - SMALL', 10000000, 80, 120)," +
            "(1435, 'NGUSISHI', 'BOLTS', '10 X 0.5', 10000000, 5, 10)," +
            "(1436, 'NGUSISHI', 'BOLTS', '10 X 1.5', 10000000, 5, 10)," +
            "(1437, 'NGUSISHI', 'BOLTS', '10 X 1', 10000000, 5, 10)," +
            "(1438, 'NGUSISHI', 'BOLTS', '10 X 2', 10000000, 6, 10)," +
            "(1439, 'NGUSISHI', 'BOLTS', '12 SHOCK', 10000000, 24, 40)," +
            "(1440, 'NGUSISHI', 'BOLTS', '13 X 1.5', 10000000, 10, 20)," +
            "(1441, 'NGUSISHI', 'BOLTS', '13 X 1', 10000000, 8, 20)," +
            "(1442, 'NGUSISHI', 'BOLTS', '13 X 2', 10000000, 12, 20)," +
            "(1443, 'NGUSISHI', 'BOLTS', '13 X 3', 10000000, 17, 30)," +
            "(1444, 'NGUSISHI', 'BOLTS', '13 X 4', 10000000, 20, 30)," +
            "(1445, 'NGUSISHI', 'BOLTS', '14 X 2 SHOCK', 10000000, 25, 40)," +
            "(1446, 'NGUSISHI', 'BOLTS', '14 X 1.5', 10000000, 13, 20)," +
            "(1447, 'NGUSISHI', 'BOLTS', '14 X 1', 10000000, 11, 20)," +
            "(1448, 'NGUSISHI', 'BOLTS', '14 X 2', 10000000, 15, 25)," +
            "(1449, 'NGUSISHI', 'BOLTS', '14 X 4.5', 10000000, 35, 50)," +
            "(1450, 'NGUSISHI', 'BOLTS', '17 X 2', 10000000, 24, 30)," +
            "(1451, 'NGUSISHI', 'BOLTS', '8 X 1.5', 10000000, 5, 10)," +
            "(1452, 'NGUSISHI', 'BOLTS', '8 X 1', 10000000, 5, 10)," +
            "(1453, 'NGUSISHI', 'BOLTS', 'NUT 10', 10000000, 2, 5)," +
            "(1454, 'NGUSISHI', 'BOLTS', 'NUT 13', 10000000, 3, 5)," +
            "(1455, 'NGUSISHI', 'BOLTS', 'NUT 14', 10000000, 5, 10)," +
            "(1456, 'NGUSISHI', 'BOLTS', 'NUT 17', 10000000, 10, 25)," +
            "(1457, 'NGUSISHI', 'BOLTS', 'NUT 19', 10000000, 15, 30)," +
            "(1458, 'NGUSISHI', 'BOLTS', 'WASHER', 10000000, 2, 5)," +
            "(1459, 'NGUSISHI', 'BOLTS', 'WASHER 17/19', 10000000, 6, 10)," +
            "(1460, 'NGUSISHI', 'OILS', 'ADVANCE AX3', 10000000, 392, 450)," +
            "(1461, 'NGUSISHI', 'OILS', 'ADVANCE AX5', 10000000, 456, 500)," +
            "(1462, 'NGUSISHI', 'OILS', 'ATF', 10000000, 165, 200)," +
            "(1463, 'NGUSISHI', 'OILS', 'B. ACID', 10000000, 70, 100)," +
            "(1464, 'NGUSISHI', 'OILS', 'B. WATER', 10000000, 60, 80)," +
            "(1465, 'NGUSISHI', 'OILS', 'BITUL 4T', 10000000, 280, 350)," +
            "(1466, 'NGUSISHI', 'OILS', 'CC', 10000000, 135, 150)," +
            "(1467, 'NGUSISHI', 'OILS', 'COOLANT', 10000000, 150, 200)," +
            "(1468, 'NGUSISHI', 'OILS', 'DOT 4 TOT', 10000000, 215, 250)," +
            "(1469, 'NGUSISHI', 'OILS', 'DOT 4 ½ - TOT', 10000000, 445, 500)," +
            "(1470, 'NGUSISHI', 'OILS', 'GREASE', 10000000, 200, 250)," +
            "(1471, 'NGUSISHI', 'OILS', 'HELIX ½', 10000000, 200, 220)," +
            "(1472, 'NGUSISHI', 'OILS', 'HELIX L', 10000000, 376, 450)," +
            "(1473, 'NGUSISHI', 'OILS', 'HELIX 4L', 10000000, 1868, 2100)," +
            "(1474, 'NGUSISHI', 'OILS', 'LACHEKA L', 10000000, 285, 300)," +
            "(1475, 'NGUSISHI', 'OILS', 'LACHEKA ½', 10000000, 135, 150)," +
            "(1476, 'NGUSISHI', 'OILS', 'MOTOR OIL', 10000000, 375, 420)," +
            "(1477, 'NGUSISHI', 'OILS', 'SAFARI L', 10000000, 350, 420)," +
            "(1478, 'NGUSISHI', 'OILS', 'SAFARI ½', 10000000, 200, 240)," +
            "(1479, 'NGUSISHI', 'OILS', 'T. SPECIAL 2T', 10000000, 212, 230)," +
            "(1480, 'NGUSISHI', 'OILS', 'TYRE SEALANT', 10000000, 115, 160)";

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
    private final String DROP_RECEIPT_TABLE = "DROP TABLE IF EXISTS " + TABLE_RECEIPT;


    /**8
     * ---------------------------------------------------------------------------------------------
     * Constructor for databasehelper
     * It's used to switch database based on the context parameter
     * @param context open SQLite database
     * ---------------------------------------------------------------------------------------------
     */
    public DatabaseHelper(Context context) {
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
        db.execSQL(CREATE_STATION_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_SALES_TABLE);
        db.execSQL(CREATE_RECEIPT_TABLE);

                        db.execSQL(INSERT_STATION);
                        db.execSQL(INSERT_CATEGORY);
                        db.execSQL(INSERT_ITEM);
                        db.execSQL(INSERT_ITEM_);
                        db.execSQL(INSERT_ITEM__);
                        db.execSQL(INSERT_ITEM___);

        // Create the trigger to delete data after the specified time for multiple tables
        //createSalesDeleteTrigger(6);
        //createReceiptDeleteTrigger(6);
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
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(DROP_CART_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_SALES_TABLE);
        db.execSQL(CREATE_RECEIPT_TABLE);
         */

        db.execSQL(DROP_ITEM_TABLE);
        db.execSQL(DROP_CATEGORY_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);

        db.execSQL(INSERT_ITEM);
        db.execSQL(INSERT_ITEM_);
        db.execSQL(INSERT_ITEM__);
        db.execSQL(INSERT_ITEM___);
        db.execSQL(INSERT_CATEGORY);
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

    }

    /**
     * Creates a delete trigger on sales table
     * @param timeInterval the iterval in which data will be deleted
     */
    public void createSalesDeleteTrigger(int timeInterval) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Drop the trigger if it already exists for the specified table
        String triggerName = "delete_sale_trigger_" + TABLE_SALES;
        db.execSQL("DROP TRIGGER IF EXISTS " + triggerName);

        // Create the trigger with the specified time interval for the specified table
        String triggerSql = String.format("CREATE TRIGGER IF NOT EXISTS %s " +
                "AFTER INSERT ON %s " +
                "BEGIN " +
                "DELETE FROM %s WHERE _date <= strftime('%%s', 'now', '-%s');" +
                "END;", triggerName, TABLE_SALES, TABLE_SALES, timeInterval + " months");

        db.execSQL(triggerSql);
        db.close();
    }
    /**
     * Creates a delete trigger on sales table
     * @param timeInterval the iterval in which data will be deleted
     */
    public void createReceiptDeleteTrigger(int timeInterval) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Drop the trigger if it already exists for the specified table
        String triggerName = "delete_receipt_trigger_" + TABLE_RECEIPT;
        db.execSQL("DROP TRIGGER IF EXISTS " + triggerName);

        // Create the trigger with the specified time interval for the specified table
        String triggerSql = String.format("CREATE TRIGGER IF NOT EXISTS %s " +
                "AFTER INSERT ON %s " +
                "BEGIN " +
                "DELETE FROM %s WHERE _date <= strftime('%%s', 'now', '-%s');" +
                "END;", triggerName, TABLE_RECEIPT, TABLE_RECEIPT, timeInterval + " months");

        db.execSQL(triggerSql);
        db.close();
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
        values.put(COLUMN_SALE_TYPE, sales.getSaleType());
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
     * Method to create receipt record
     * @param par Receipt model
     * ---------------------------------------------------------------------------------------------
     */
    public void addReceipt(Receipt par) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECEIPT_DATE, par.getReceiptDate());
        values.put(COLUMN_RECEIPT_TIME, par.getReceiptTime());
        values.put(COLUMN_RECEIPT_TYPE, par.getReceiptTransType());
        values.put(COLUMN_RECEIPT_STATION, par.getStationName());
        values.put(COLUMN_RECEIPT_CATEGORY, par.getItemCategory());
        values.put(COLUMN_RECEIPT_ITEM, par.getItemName());
        values.put(COLUMN_RECEIPT_QUANTITY, par.getItemQuantity());
        values.put(COLUMN_RECEIPT_PRICE, par.getSellingPrice());
        values.put(COLUMN_RECEIPT_TOTAL, par.getTotal());
        values.put(COLUMN_RECEIPT_CUSTOMER, par.getCustomerName());

        //Insert Row
        db.insert(TABLE_RECEIPT, null, values);
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
        values.put(COLUMN_STATION_IDENTIFIER, station.getStationIdentifier());
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
     * This method is to fetch all receipts and return the list of the receipt records
     * This is the deafault method as it returns allreceipts as at today's date
     * @return list
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public List<Receipt> getAllReceipts(){
        //Columns to fetch
        String[] columns = {
                COLUMN_RECEIPT_DATE,
                COLUMN_RECEIPT_TIME,
                COLUMN_RECEIPT_CUSTOMER,
                COLUMN_RECEIPT_TYPE,
                COLUMN_RECEIPT_STATION,
                COLUMN_RECEIPT_CATEGORY,
                COLUMN_RECEIPT_ITEM,
                COLUMN_RECEIPT_PRICE,
                COLUMN_RECEIPT_QUANTITY,
                COLUMN_RECEIPT_TOTAL
        };
        //Sorting order
        String sortOder = COLUMN_RECEIPT_ID + " ASC";
        List<Receipt> list = new ArrayList<Receipt>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_RECEIPT, //table to query
                columns,  //columns to return
                null,  //columns for the where clause
                null,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Receipt par = new Receipt();
                par.setReceiptDate(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_DATE)));
                par.setReceiptTime(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_TIME)));
                par.setReceiptTransType(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_TYPE)));
                par.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_STATION)));
                par.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_CATEGORY)));
                par.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_ITEM)));
                par.setItemQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_QUANTITY))));
                par.setSellingPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_PRICE))));
                par.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_TOTAL))));
                par.setCustomerName(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_CUSTOMER)));
                list.add(par);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * Tfetch all receipts as at the given date
     * @param date the date
     * @return list
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public List<Receipt> getAllReceipts(String date){
        //Columns to fetch
        String[] columns = {
                COLUMN_RECEIPT_DATE, COLUMN_RECEIPT_TIME,
                COLUMN_RECEIPT_CUSTOMER, COLUMN_RECEIPT_TYPE,
                COLUMN_RECEIPT_STATION, COLUMN_RECEIPT_CATEGORY,
                COLUMN_RECEIPT_ITEM, COLUMN_RECEIPT_PRICE,
                COLUMN_RECEIPT_QUANTITY, COLUMN_RECEIPT_TOTAL
        };
        //Sorting order
        String sortOder = COLUMN_RECEIPT_ID + " ASC";
        //values for the selection arguments
        String[] selectionArgs = {date};
        //selection criteria
        String selection = COLUMN_RECEIPT_DATE + " = ?";
        List<Receipt> list = new ArrayList<Receipt>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_RECEIPT, //table to query
                columns,  //columns to return
                selection,  //columns for the where clause
                selectionArgs,  //the values for the where clause
                null,   //group the rows
                null,   //filter by row groups
                sortOder);  //the sortorder
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Receipt par = new Receipt();
                par.setReceiptDate(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_DATE)));
                par.setReceiptTime(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_TIME)));
                par.setReceiptTransType(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_TYPE)));
                par.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_STATION)));
                par.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_CATEGORY)));
                par.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_ITEM)));
                par.setItemQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_QUANTITY))));
                par.setSellingPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_PRICE))));
                par.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_TOTAL))));
                par.setCustomerName(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_CUSTOMER)));
                list.add(par);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * Tfetch all receipts as at the given date value and station
     * @param date the date when the receipt was generated
     * @param station station where to fetch the receipt from
     * @return list
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public List<Receipt> getAllReceipts(String date,String station){
        //Columns to fetch
        String[] columns = {
                COLUMN_RECEIPT_DATE, COLUMN_RECEIPT_TIME, COLUMN_RECEIPT_CUSTOMER, COLUMN_RECEIPT_TYPE,
                COLUMN_RECEIPT_STATION, COLUMN_RECEIPT_CATEGORY, COLUMN_RECEIPT_ITEM, COLUMN_RECEIPT_PRICE,
                COLUMN_RECEIPT_QUANTITY, COLUMN_RECEIPT_TOTAL
        };
        //Sorting order
        String sortOder = COLUMN_RECEIPT_ID + " ASC";
        //selection criteria
        String selection = COLUMN_RECEIPT_DATE + " = ? AND " + COLUMN_RECEIPT_STATION + " = ?";
        //values for the selection argument
        String[] selectionArgs = {date,station};
        List<Receipt> list = new ArrayList<Receipt>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_RECEIPT,columns,selection,selectionArgs, null, null,sortOder);
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Receipt par = new Receipt();
                par.setReceiptDate(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_DATE)));
                par.setReceiptTime(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_TIME)));
                par.setReceiptTransType(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_TYPE)));
                par.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_STATION)));
                par.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_CATEGORY)));
                par.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_ITEM)));
                par.setItemQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_QUANTITY))));
                par.setSellingPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_PRICE))));
                par.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_TOTAL))));
                par.setCustomerName(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_CUSTOMER)));
                list.add(par);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * Tfetch all receipts as at the given date value and station
     * @param date the date when the receipt was generated
     * @param station station where to fetch the receipt from
     * @param customer customer to which the the receipt was issued
     * @return list
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public List<Receipt> getAllReceipts(String date,String station, String customer){
        //Columns to fetch
        String[] columns = {
                COLUMN_RECEIPT_DATE, COLUMN_RECEIPT_TIME, COLUMN_RECEIPT_CUSTOMER, COLUMN_RECEIPT_TYPE,
                COLUMN_RECEIPT_STATION, COLUMN_RECEIPT_CATEGORY, COLUMN_RECEIPT_ITEM, COLUMN_RECEIPT_PRICE,
                COLUMN_RECEIPT_QUANTITY, COLUMN_RECEIPT_TOTAL
        };
        //Sorting order
        String sortOder = COLUMN_RECEIPT_ID + " ASC";
        //selection criteria
        String selection = COLUMN_RECEIPT_DATE + " = ? AND " + COLUMN_RECEIPT_STATION + " = ? AND "
                + COLUMN_RECEIPT_CUSTOMER + " = ?";
        //values for the selection argument
        String[] selectionArgs = {date,station,customer};
        List<Receipt> list = new ArrayList<Receipt>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_RECEIPT,columns,selection,selectionArgs, null, null,sortOder);
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Receipt par = new Receipt();
                par.setReceiptDate(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_DATE)));
                par.setReceiptTime(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_TIME)));
                par.setReceiptTransType(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_TYPE)));
                par.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_STATION)));
                par.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_CATEGORY)));
                par.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_ITEM)));
                par.setItemQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_QUANTITY))));
                par.setSellingPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_PRICE))));
                par.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_TOTAL))));
                par.setCustomerName(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_CUSTOMER)));
                list.add(par);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * Tfetch all receipts as at the given date value and customer name
     * @param date the date when the receipt was generated
     * @param customer customer to which the receipt was issued
     * @return list
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public List<Receipt> getReceipts(String date,String customer){
        //Columns to fetch
        String[] columns = {
                COLUMN_RECEIPT_DATE, COLUMN_RECEIPT_TIME, COLUMN_RECEIPT_CUSTOMER, COLUMN_RECEIPT_TYPE,
                COLUMN_RECEIPT_STATION, COLUMN_RECEIPT_CATEGORY, COLUMN_RECEIPT_ITEM, COLUMN_RECEIPT_PRICE,
                COLUMN_RECEIPT_QUANTITY, COLUMN_RECEIPT_TOTAL
        };
        //Sorting order
        String sortOder = COLUMN_RECEIPT_ID + " ASC";
        //selection criteria
        String selection = COLUMN_RECEIPT_DATE + " = ? AND " + COLUMN_RECEIPT_CUSTOMER + " = ?";
        //values for the selection argument
        String[] selectionArgs = {date,customer};
        List<Receipt> list = new ArrayList<Receipt>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_RECEIPT,columns,selection,selectionArgs, null, null,sortOder);
        //Traversing through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                Receipt par = new Receipt();
                par.setReceiptDate(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_DATE)));
                par.setReceiptTime(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_TIME)));
                par.setReceiptTransType(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_TYPE)));
                par.setStationName(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_STATION)));
                par.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_CATEGORY)));
                par.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_ITEM)));
                par.setItemQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_QUANTITY))));
                par.setSellingPrice(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_PRICE))));
                par.setTotal(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_TOTAL))));
                par.setCustomerName(cursor.getString(cursor.getColumnIndex(COLUMN_RECEIPT_CUSTOMER)));
                list.add(par);
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
        String sortOder = COLUMN_ITEM_NAME + " ASC";
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
        String sortOder = COLUMN_ITEM_NAME + " ASC";
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
     * This method is to fetch all all items from the given station
     * @return a list of all items from the given station
     * ---------------------------------------------------------------------------------------------
     */

    @SuppressLint("Range")
    public List<Item> getAllItems(String station){
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
        String sortOder = COLUMN_ITEM_NAME + " ASC";
        //selection criteria
        String selection = COLUMN_ITEM_STATION + " = ?";
        //selection argument
        String[] selectionArgs = {station};
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
        String[] columns = {"*"};
        //Sorting order
        String sortOder = COLUMN_CATEGORY_NAME + " ASC";
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
                COLUMN_STATION_LOCATION,
                COLUMN_STATION_IDENTIFIER
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
                par.setStationIdentifier(cursor.getString(cursor.getColumnIndex(COLUMN_STATION_IDENTIFIER)));
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
        String sortOder = COLUMN_STATION_NAME + " ASC";
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
        values.put(COLUMN_CATEGORY_STATION, par.getStationName());

        // updating row
        db.update(TABLE_CATEGORY, values, COLUMN_CATEGORY_ID + " = ? AND "+COLUMN_CATEGORY_STATION+" = ?",
                new String[]{String.valueOf(par.getCategoryId()),par.getStationName()});
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
     *
     * This method is to delete receipt record
     * @param par attributes of the receipt model
     * ---------------------------------------------------------------------------------------------
     */
    public void deleteReceipt(Receipt par) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete record by id
        db.delete(TABLE_RECEIPT, COLUMN_RECEIPT_STATION + " = ? AND " + COLUMN_RECEIPT_DATE + " = ? AND "
                + COLUMN_RECEIPT_TIME + " = ?",
                new String[]{par.getStationName(), par.getReceiptDate(),par.getReceiptTime()});
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
        db.delete(TABLE_SALES, COLUMN_SALE_DAY + " = ? AND " + COLUMN_SALE_TIME + " = ?",
                new String[]{String.valueOf(par.getDate()),par.getTime()});
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
    public boolean checkUser() {
        //columns to fetch
        String[] columns = {
                "*"
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        Cursor cursor = db.query(
                TABLE_USER,
                columns,
                null,
                null,
                null,
                null,
                null);
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
