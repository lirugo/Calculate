package com.lirugo.calculate;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

        //Create Adapter
        // начальная инициализация списка
        setInitialData();
        // получаем элемент ListView
        historiesView = (ListView) findViewById(R.id.list_view_history);
        historyAdapter = new HistoryAdapter(this, R.layout.history_item, histories);
        // устанавливаем адаптер
        historiesView.setAdapter(historyAdapter);

        //Actions on editText
        editTextActions();
    }

    //Init data for ListView
    private void setInitialData(){

        histories.add(new History ("2+2*2", "6"));
        histories.add(new History ("1+2+3+4+5", "15"));
        histories.add(new History ("8+2*2", "12"));
        histories.add(new History ("1.5+2.5", "4"));
        histories.add(new History ("1.5+2.5", "4"));
        histories.add(new History ("1.5+2.5", "4"));
        histories.add(new History ("1+1+1+2", "5"));
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

    public void onClickAdd(View v){
        if(!editText.getText().toString().equals("")) {
            histories.add(new History(editText.getText().toString(), textView.getText().toString()));
            editText.getText().clear();
            historyAdapter.notifyDataSetChanged();
            historiesView.setSelection(historyAdapter.getCount() - 1);
        }
    }
}
