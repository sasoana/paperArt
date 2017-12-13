package com.example.oana.paperart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oana.paperart.database.AppDatabase;
import com.example.oana.paperart.database.CategoryWithItems;

import java.util.ArrayList;
import java.util.List;

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

        Spinner spinner = (Spinner) findViewById(R.id.imageSpinner);
        List<String> list = new ArrayList<String>();
        list.add("kusudama");
        list.add("pure");
        list.add("modular");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        Button saveButton = (Button) findViewById(R.id.button_save_cat);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView nameView = (TextView) findViewById(R.id.edit_cat_name);
                TextView descriptionView = (TextView) findViewById(R.id.edit_description);
                Spinner imageSpinner = (Spinner) findViewById(R.id.imageSpinner);
                Integer newId = 0;
                Category newCategory = new Category(newId, nameView.getText().toString(),
                        descriptionView.getText().toString(),
                        imageSpinner.getSelectedItem().toString());
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
