package com.example.oana.paperart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oana.paperart.database.AppDatabase;
import com.example.oana.paperart.database.CategoryWithItems;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oana on 11/8/2017.
 */

public class ItemList extends Activity {
    ArrayList<PaperItem> items = new ArrayList<>();
    CategoryWithItems category;
    ListAdapter adapter;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getAppDatabase(getApplicationContext());
        setContentView(R.layout.activity_list);
        ListView listview = (ListView) findViewById(R.id.listview);
        final CategoryWithItems category = (CategoryWithItems) getIntent().getSerializableExtra("category");
        this.items = new ArrayList<>(category.items);
        this.category = category;

        //add header to list view
        TextView textView = new TextView(listview.getContext());
        textView.setText("Items in " + this.category.category.getName() + " category");
        listview.addHeaderView(textView, "", false);
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);

        //add footer to list view
        Button addButton = new Button(listview.getContext());
        addButton.setText("Add new item in this category");
        listview.addFooterView(addButton, "", true);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ItemList.this, ItemDetailsActivity.class);
                intent.putExtra("item", new PaperItem(category.category.getId()));
                startActivityForResult(intent, 1);
            }
        });

        adapter = new ListAdapter(this, R.layout.list_item, items);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(ItemList.this, ItemDetailsActivity.class);
                intent.putExtra("item", items.get(position-1));
                startActivityForResult(intent, 1);
            }

        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int arg2, long arg3) {

                mDb.paperItemDAO().delete(items.get(arg2-1));
                loadData();
                Toast.makeText(ItemList.this, "The item has been removed!", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    public void loadData() {
        this.items = new ArrayList<>(mDb.categoryDAO().getOne(this.category.category.getId()).items);
        adapter.clear();
        adapter.addAll(this.items);
        adapter.notifyDataSetChanged();
    }

    public void onBackPressed() {
        Intent resultIntent = new Intent();
        this.category.items = items;
        resultIntent.putExtra("category", this.category);
        Log.wtf(ItemList.class.getSimpleName(), "xxx" + this.category.toString());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            loadData();
        }
    }

    private class ListAdapter extends ArrayAdapter<PaperItem> {

        public ListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ListAdapter(Context context, int resource, List<PaperItem> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.list_item, null);
            }

            PaperItem p = getItem(position);

            if (p != null) {
                TextView t = (TextView) v.findViewById(R.id.textView);

                if (t != null) {
                    t.setText("Model name: " + p.getName() + "\n"
                            + "Paper type: " + p.getPaperType() + "\n"
                            + "Duration: " + p.getDuration() + " minutes");
                    t.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rsz_img_4068, 0, 0, 0);
                }
            }
            return v;
        }

    }
}
