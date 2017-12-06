package com.example.oana.paperart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.oana.paperart.database.AppDatabase;
import com.example.oana.paperart.database.DatabaseInitializer;

import java.util.List;

/**
 * Created by oana on 12/6/2017.
 */

public class CategoryList extends Activity {
    private AppDatabase mDb;

    List<Category> categories;

    /*List<Category> categories = new ArrayList<>(Arrays.asList(
            new Category(0, "Modular", "Origami composed of 2 or more parts", "modular", new ArrayList<PaperItem>(Arrays.asList(
                    new PaperItem(0, 0, "Cat", "Regular", "Grey", 25),
                    new PaperItem(1, 0, "Dragon", "Tant", "Red", 225)))),
            new Category(1, "Pure", "Origami from one sheet of paper, no cuts", "pure", new ArrayList<PaperItem>(Arrays.asList(
                    new PaperItem(0, 1, "Cat", "Regular", "Grey", 25)))),
            new Category(2, "Kusudama", "Flower-like spheres", "kusudama", new ArrayList<PaperItem>(Arrays.asList(
                    new PaperItem(2, 2, "Boat", "Kami", "Blue", 20))))));
*/
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ListView listview = (ListView) findViewById(R.id.listview);

        mDb = AppDatabase.getAppDatabase(getApplicationContext());

        DatabaseInitializer.populateAsync(mDb);

        this.categories = mDb.categoryDAO().getAll();

        TextView textView = new TextView(listview.getContext());
        textView.setText("Categories");
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);

        listview.addHeaderView(textView, "", false);

        adapter = new ListAdapter(this, R.layout.activity_list, categories);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Category returnValue = (Category) data.getSerializableExtra("category");
            categories.set(categories.indexOf(returnValue), returnValue);
            adapter.notifyDataSetChanged();
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
