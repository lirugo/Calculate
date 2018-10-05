package com.lirugo.calculate;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set using menu for bottom bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.bottom_app_bar);
        setSupportActionBar(toolbar);

        //Init
        editText = (EditText) findViewById(R.id.edit_text);
        textView = (TextView) findViewById(R.id.text_view);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                textView.setText(s);
            }
        });

    }

    //Init options for menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_app_bar_menu, menu);
        return true;
    }

    //Set simplify action on click item in menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.app_bar_fav :
                Toast.makeText(this, "I'm like it", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    //Action on click digits buttons
    public void onClickButtonsDigits(View v) {
        Button btn = (Button) v;
        editText.append(btn.getText().toString());
    }

    //Action on click actions buttons
    public void onClickButtonsActions(View v) {
        Button btn = (Button) v;
        editText.append(btn.getText().toString());
    }

    public void onClickRemoveLast(View v){
        int length = editText.getText().length();
        if(length > 0)
            editText.getText().delete(length - 1, length);
    }

    public void onClickRemoveAll(View v){
        editText.getText().clear();
    }


}
