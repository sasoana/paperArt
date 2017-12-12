package com.example.oana.paperart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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

import java.util.List;

/**
 * Created by oana on 12/6/2017.
 */

public class CategoryList extends Activity {
    private AppDatabase mDb;

    List<CategoryWithItems> categories;

    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        final ListView listview = (ListView) findViewById(R.id.listview);

        mDb = AppDatabase.getAppDatabase(getApplicationContext());

        //DatabaseInitializer.populateAsync(mDb);
        //DatabaseInitializer.populate(mDb);
        this.categories = mDb.categoryDAO().getAll();

        //add header to list view
        TextView textView = new TextView(listview.getContext());
        textView.setText("Categories");
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        listview.addHeaderView(textView, "", false);

        //add footer to list view
        Button addButton = new Button(listview.getContext());
        addButton.setText("Add new category");
        listview.addFooterView(addButton, "", true);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CategoryList.this, CategoryDetalis.class);
                startActivityForResult(intent, 0);
            }
        });

        adapter = new ListAdapter(this, R.layout.activity_list, mDb.categoryDAO().getAll());
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(CategoryList.this, ItemList.class);
                intent.putExtra("category", categories.get(position-1));
                startActivityForResult(intent, 1);
            }

        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int arg2, long arg3) {
                final CategoryWithItems category = categories.get(arg2-1);
                if (category.items.size() >= 1) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(listview.getContext(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(listview.getContext());
                    }
                    builder.setTitle("Delete category")
                            .setMessage("This category has items that will also be removed.Are you sure you want to delete this entry?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    mDb.categoryDAO().delete(category.category);
                                    loadData();
                                    Toast.makeText(CategoryList.this, "The category has been removed!", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else {
                    mDb.categoryDAO().delete(category.category);
                    loadData();
                    Toast.makeText(CategoryList.this, "The category has been removed!", Toast.LENGTH_LONG).show();
                }

                return false;
            }
        });
    }

    public void loadData() {
        this.categories = mDb.categoryDAO().getAll();
        adapter.clear();
        adapter.addAll(this.categories);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            loadData();
            Log.wtf("xxx", this.categories.toString());
        }
    }

    private class ListAdapter extends ArrayAdapter<CategoryWithItems> {

        public ListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ListAdapter(Context context, int resource, List<CategoryWithItems> categories) {
            super(context, resource, categories);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.list_item, null);
            }

            CategoryWithItems c = getItem(position);

            if (c != null) {
                TextView t = (TextView) v.findViewById(R.id.textView);
                TextView d = (TextView) v.findViewById(R.id.descriptionView);

                if (t != null) {
                    t.setText(c.category.getName() + "\n");
                    d.setText("Description: " + c.category.getDescription() + "\n");
                    Context context = t.getContext();
                    t.setCompoundDrawablesWithIntrinsicBounds(
                            context.getResources().getIdentifier(c.category.getImageName(), "drawable", context.getPackageName()), 0, 0, 0);
                }
            }
            return v;
        }

    }
}
