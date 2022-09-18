package mich.gwan.sarensa.model;

import android.content.Context;

import java.util.List;

import mich.gwan.sarensa.adapters.ItemRecyclerAdapter;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class ListItem {
    List<Item> item;
    Context context;
    ItemRecyclerAdapter adapter;
    DatabaseHelper dbHelper;

    public void setItem(List<Item> item) {
        this.item = item;
    }
    public List<Item> getItem() {
        return item;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public Context getContext() {
        return context;
    }

    public void setAdapter(ItemRecyclerAdapter adapter) {
        this.adapter = adapter;
    }
    public ItemRecyclerAdapter getAdapter() {
        return adapter;
    }

    public void setDbHelper(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
    public DatabaseHelper getDbHelper() {
        return dbHelper;
    }
}
