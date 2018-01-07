package com.example.oana.paperart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oana on 11/8/2017.
 */

public class ItemList extends AppCompatActivity {
    ArrayList<PaperItem> items = new ArrayList<>();
    Category category;
    ListAdapter adapter;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_list);
        ListView listview = (ListView) findViewById(R.id.listview);
        final Category category = (Category) getIntent().getSerializableExtra("category");
        this.items = new ArrayList<>();
        this.category = category;

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference itemsRef = mDatabase.child("category-items").child(category.getKey());
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot itemsSnapshot : dataSnapshot.getChildren()) {
                    PaperItem paperItem = itemsSnapshot.getValue(PaperItem.class);
                    paperItem.setKey(itemsSnapshot.getKey());
                    items.add(paperItem);
                    Log.wtf("items updated", "item: " + paperItem.toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new ListAdapter(this, R.layout.list_item, items);
        listview.setAdapter(adapter);

        TextView title = (TextView) findViewById(R.id.categories_title);
        title.setText("Items in " + this.category.getName() + " category");

        //add footer to list view
        Button addButton = new Button(listview.getContext());
        addButton.setText("Add new item in this category");
        listview.addFooterView(addButton, "", true);

        //add new item
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ItemList.this, ItemDetailsActivity.class);
                intent.putExtra("type", "add");
                intent.putExtra("item", new PaperItem(category.getKey(), userId));
                startActivityForResult(intent, 0);
            }
        });

        //update existing item
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                if (items.get(position).getUid().equals(userId)) {
                    Intent intent = new Intent(ItemList.this, ItemDetailsActivity.class);
                    intent.putExtra("type", "update");
                    intent.putExtra("item", items.get(position));
                    startActivityForResult(intent, 1);
                }
                else {
                    Toast.makeText(ItemList.this, "Cannot update item. You are not the author!", Toast.LENGTH_SHORT).show();
                }
            }

        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int arg2, long arg3) {

                final PaperItem item = items.get(arg2);
                if (item.getUid().equals(userId)) {
                    mDatabase.child("items").child(item.getKey()).removeValue();
                    mDatabase.child("user-items").child(userId).child(item.getKey()).removeValue();
                    mDatabase.child("category-items").child(category.getKey()).child(item.getKey()).removeValue();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(ItemList.this, "The item has been removed!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ItemList.this, "Item could not be deleted. You are not the author!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("category", this.category);
        Log.wtf(ItemList.class.getSimpleName(), "xxx" + this.category.toString());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //loadData();
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
