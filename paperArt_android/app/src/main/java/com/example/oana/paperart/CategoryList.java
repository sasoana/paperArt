package com.example.oana.paperart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oana on 12/6/2017.
 */

public class CategoryList extends AppCompatActivity {
    private static final String TAG = "CategoryList";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseAuth mAuth;

    List<Category> categories = new ArrayList<>();
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        final ListView listview = (ListView) findViewById(R.id.listview);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference().child("categories");
        // [END create_database_reference]
        mAuth = FirebaseAuth.getInstance();

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Category category = dataSnapshot.getValue(Category.class);
                categories.add(category);
                Log.wtf("Locations updated", "location: " + category.toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //something changed!
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    Category category = locationSnapshot.getValue(Category.class);
                    //categories.add(category);
                    Log.wtf("Locations updated", "location: " + category.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        TextView title = (TextView) findViewById(R.id.categories_title);
        title.setText("Categories");

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

        /*FirebaseListOptions<Category> options = new FirebaseListOptions.Builder<Category>()
                .setQuery(mDatabase, Category.class)
                .setLayout(R.layout.list_item)
                .build();
        adapter = new FirebaseListAdapter<Category>(options) {
            @Override
            protected void populateView(View view, Category category, int i) {
                Log.wtf("aaa", category.toString());
                TextView t = (TextView) view.findViewById(R.id.textView);
                TextView d = (TextView) view.findViewById(R.id.descriptionView);

                t.setText(category.getName() + "\n");
                d.setText("Description: " + category.getDescription() + "\n");
                Context context = t.getContext();
                t.setCompoundDrawablesWithIntrinsicBounds(
                        context.getResources().getIdentifier(category.getImageName(), "drawable", context.getPackageName()), 0, 0, 0);

            }
        };*/
        adapter = new ListAdapter(this, R.layout.list_item, categories);
        listview.setAdapter(adapter);

        //go to item list activity
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                //Intent intent = new Intent(CategoryList.this, ItemList.class);
                //intent.putExtra("category", categories.get(position-1));
                //startActivityForResult(intent, 1);
            }

        });

        //delete category
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int arg2, long arg3) {
                final Category category = categories.get(arg2-1);
                if (1==1){//category.items.size() >= 1) {
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
                                    //mDb.categoryDAO().delete(category.category);
                                    //loadData();
                                    Toast.makeText(CategoryList.this, "The category has been removed!", Toast.LENGTH_SHORT).show();
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
                    //mDb.categoryDAO().delete(category.category);
                    //loadData();
                    Toast.makeText(CategoryList.this, "The category has been removed!", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
    }

    /*public void loadData() {
        this.categories = mDb.categoryDAO().getAll();
        adapter.clear();
        adapter.addAll(this.categories);
        adapter.notifyDataSetChanged();
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Log.wtf("xxx", this.categories.toString());
        }
    }

    private class ListAdapter extends ArrayAdapter<Category> {

        public ListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ListAdapter(Context context, int resource, List<Category> categories) {
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

            Category c = getItem(position);

            if (c != null) {
                TextView t = (TextView) v.findViewById(R.id.textView);
                TextView d = (TextView) v.findViewById(R.id.descriptionView);

                if (t != null) {
                    t.setText(c.getName() + "\n");
                    d.setText("Description: " + c.getDescription() + "\n");
                    Context context = t.getContext();
                    t.setCompoundDrawablesWithIntrinsicBounds(
                            context.getResources().getIdentifier(c.getImageName(), "drawable", context.getPackageName()), 0, 0, 0);
                }
            }
            return v;
        }

    }
}
