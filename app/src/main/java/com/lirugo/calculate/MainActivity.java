package com.lirugo.calculate;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set using menu for bottom bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.bottom_app_bar);
        setSupportActionBar(toolbar);
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

    //Action on click fab button
    public void onClickAddButton(View view) {
        Toast.makeText(this, "Add toast", Toast.LENGTH_SHORT).show();
    }
}
