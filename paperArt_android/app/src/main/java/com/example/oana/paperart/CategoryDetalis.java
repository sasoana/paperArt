package com.example.oana.paperart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oana.paperart.database.AppDatabase;
import com.example.oana.paperart.database.CategoryWithItems;

import java.util.ArrayList;

/**
 * Created by oana on 12/10/2017.
 */

public class CategoryDetalis extends Activity {

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_details);
        mDb = AppDatabase.getAppDatabase(getApplicationContext());

        //final PaperItem item = (PaperItem) getIntent().getExtras().getSerializable("item");

        /*final EditText editText_name = (EditText) findViewById(R.id.edit_name);
        editText_name.setText(item.getName());

        final EditText editText_paper = (EditText) findViewById(R.id.edit_paper);
        editText_paper.setText(item.getPaperType());

        final EditText editText_color = (EditText) findViewById(R.id.edit_color);
        editText_color.setText(item.getColor());

        final EditText editText_duration = (EditText) findViewById(R.id.edit_duration);
        editText_duration.setText(item.getDuration().toString());*/

        Button saveButton = (Button) findViewById(R.id.button_save_cat);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView nameView = (TextView) findViewById(R.id.edit_cat_name);
                TextView descriptionView = (TextView) findViewById(R.id.edit_description);
                TextView imageView = (TextView) findViewById(R.id.edit_image);
                Integer newId = 0;
                Category newCategory = new Category(newId, nameView.getText().toString(),
                        descriptionView.getText().toString(),
                        imageView.getText().toString());
                Intent resultIntent = new Intent();
                long id = mDb.categoryDAO().addCategory(newCategory);
                newCategory.setId((int)id);
                resultIntent.putExtra("newCategory", new CategoryWithItems(newCategory, new ArrayList<PaperItem>()));
                Toast.makeText(CategoryDetalis.this, "The category has been added!", Toast.LENGTH_LONG).show();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
