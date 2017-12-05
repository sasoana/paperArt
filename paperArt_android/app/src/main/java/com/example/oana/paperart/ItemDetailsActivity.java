package com.example.oana.paperart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by oana on 11/7/2017.
 */

public class ItemDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        final PaperItem item = (PaperItem) getIntent().getExtras().getSerializable("item");

        final EditText editText_name = (EditText) findViewById(R.id.edit_name);
        editText_name.setText(item.getName());

        final EditText editText_paper = (EditText) findViewById(R.id.edit_paper);
        editText_paper.setText(item.getPaperType());

        final EditText editText_color = (EditText) findViewById(R.id.edit_color);
        editText_color.setText(item.getColor());

        final EditText editText_duration = (EditText) findViewById(R.id.edit_duration);
        editText_duration.setText(item.getDuration().toString());

        Button saveButton = (Button) findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PaperItem newItem = new PaperItem(item.getId(), editText_name.getText().toString(),
                        editText_paper.getText().toString(),
                        editText_color.getText().toString(),
                        Integer.parseInt(editText_duration.getText().toString()));
                Intent resultIntent = new Intent();
                resultIntent.putExtra("newItem", newItem);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
