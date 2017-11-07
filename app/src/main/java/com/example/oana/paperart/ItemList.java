package com.example.oana.paperart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oana on 11/8/2017.
 */

public class ItemList extends Activity {
    final List<PaperItem> items = new ArrayList<>(Arrays.asList(new PaperItem(0, "Cat", "Regular", "Grey", 25),
            new PaperItem(1, "Dragon", "Tant", "Red", 225),
            new PaperItem(2, "Boat", "Kami", "Blue", 20)));
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ListView listview = (ListView) findViewById(R.id.listview);

        adapter = new ListAdapter(this, R.layout.list_item, items);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(ItemList.this, ItemDetailsActivity.class);
                intent.putExtra("item", items.get(position));
                startActivityForResult(intent, 1);
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            PaperItem returnValue = (PaperItem) data.getSerializableExtra("newItem");
            items.set(items.indexOf(returnValue), returnValue);
            adapter.notifyDataSetChanged();
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
                }
            }
            return v;
        }

    }
}
