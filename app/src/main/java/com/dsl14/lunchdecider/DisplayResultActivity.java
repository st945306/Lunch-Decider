package com.dsl14.lunchdecider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class DisplayResultActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);
        Intent intent = getIntent();
        String[] result = intent.getStringArrayExtra("RESULT");
        TextView[] tv = new TextView[3];
        tv[0] = (TextView)findViewById(R.id.result1);
        tv[1] = (TextView)findViewById(R.id.result2);
        tv[2] = (TextView)findViewById(R.id.result3);
        for (int i = 0; i < 3; i++){
            tv[i].setTextSize(30);
            tv[i].setText(result[i]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchResultMap(View view){
        Intent intent = new Intent(this,searchResultMapsActivity.class);
        startActivity(intent);
    }
}
