package com.lirugo.calculate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private ListView historiesView;
    private List<History> histories = new ArrayList();
    private HistoryAdapter historyAdapter;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor calculationsCursor;

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
        historiesView = (ListView) findViewById(R.id.list_view_history);

        //Create DB
        databaseHelper = new DatabaseHelper(getApplicationContext());

        //Get ListView
        historiesView = (ListView) findViewById(R.id.list_view_history);
        historyAdapter = new HistoryAdapter(this, R.layout.history_item, histories);

        historiesView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Toast.makeText(MainActivity.this, ""+position, Toast.LENGTH_SHORT).show();
            }
        });

        //Add Adapter
        historiesView.setAdapter(historyAdapter);

        //Actions on editText
        editTextActions();
    }

    //Actions with editText field
    private void editTextActions(){
        //Hide soft keyboard
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });

        //Action on change edit
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                textView.setText(RPN.getSolution(s.toString()).toString());
            }
        });
    }

    //Init objects for DB
    @Override
    protected void onResume() {
        super.onResume();
        //Open connection
        db = databaseHelper.getReadableDatabase();
        //Get data from DB
        calculationsCursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE, null);
        //Init data from DB
        if (calculationsCursor.moveToFirst()) {
            //Loop through the table rows
            do {
                //Add movie details to list
                histories.add(new History(calculationsCursor.getString(1), calculationsCursor.getString(2)));
            } while (calculationsCursor.moveToNext());
        }
        db.close();
    }

    //Close cursor
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        calculationsCursor.close();
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
        editText.getText().insert(editText.getSelectionStart(), btn.getText().toString());
    }

    //Action on click actions buttons
    public void onClickButtonsActions(View v) {
        Button btn = (Button) v;
        editText.getText().insert(editText.getSelectionStart(), btn.getText().toString());
    }

    //Remove left from cursor
    public void onClickRemove(View v){
        if(editText.getSelectionStart() > 0)
            editText.getText().delete(editText.getSelectionStart() - 1,editText.getSelectionStart());
        else if(histories.size() == 0)
            return;
        else {
            //Open connection
            db = databaseHelper.getWritableDatabase();
            //Get count rows
            calculationsCursor = db.rawQuery("SELECT MAX(_id) FROM " + DatabaseHelper.TABLE, null);
            calculationsCursor.moveToFirst();
            //Delete
            db.delete(DatabaseHelper.TABLE, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(calculationsCursor.getInt(0))});
            db.close();
            histories.remove(histories.size() - 1);
            historyAdapter.notifyDataSetChanged();
        }
    }

    //Remove all
    public void onClickRemoveAll(View v){
        editText.getText().clear();
    }

    //Actions for move cursor button
    public void onClickMoveCursor(View v){
        if(v.getId() == R.id.image_button_move_left){
            if((editText.getSelectionStart() - 1) < 0)
                return;
            else
                editText.setSelection(editText.getSelectionStart() - 1);
        }
        else if(v.getId() == R.id.image_button_move_right){
            if((editText.getSelectionStart() + 1) > editText.length())
                return;
            else
                editText.setSelection(editText.getSelectionStart() + 1);
        }
    }

    //Actions on adding
    public void onClickAdd(View v){
        if(!editText.getText().toString().equals("")) {
            //Open connection
            db = databaseHelper.getReadableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COLUMN_INPUT, editText.getText().toString());
            cv.put(DatabaseHelper.COLUMN_OUTPUT, textView.getText().toString());
            db.insert(DatabaseHelper.TABLE, null, cv);
            db.close();

            histories.add(new History(editText.getText().toString(), textView.getText().toString()));
            editText.getText().clear();
            historyAdapter.notifyDataSetChanged();
            historiesView.setSelection(historyAdapter.getCount() - 1);
        }
    }

}
