package com.dsl14.lunchdecider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    static public Spinner tSpinner;
    static public int weatherP, budgetP, drinkP;
    public static void findViews(View view){
        tSpinner = (Spinner)view.findViewById(R.id.weatherSpinner);
        tSpinner.setOnItemSelectedListener(wlistener);
        tSpinner = (Spinner)view.findViewById(R.id.budgetSpinner);
        tSpinner.setOnItemSelectedListener(blistener);
    }
    static Spinner.OnItemSelectedListener wlistener =
            new Spinner.OnItemSelectedListener(){
                public void onItemSelected(AdapterView parent, View view,
                                           int pos, long id){
                    weatherP = pos;
                }
                public void onNothingSelected(AdapterView parent){}
            };
    static Spinner.OnItemSelectedListener blistener =
            new Spinner.OnItemSelectedListener(){
                public void onItemSelected(AdapterView parent, View view,
                                           int pos, long id){
                    budgetP = pos;
                }
                public void onNothingSelected(AdapterView parent){}
            };
    public void click(View view){
        if (((CheckBox)view).isChecked())
            drinkP = 1;
        else
            drinkP = 0;
    }
    public void buttonClick(View view){
        switch (view.getId()){
            case R.id.yesButton:
                drinkP = 1;
                break;
            case R.id.noButton:
                drinkP = 0;
        }
    }
    public View viewNow;
    public boolean addRest;
    public Intent intent;
    class thread extends Thread{
        public void run(){
            try {
                Socket socket = new Socket("140.112.30.39", 12345);
                if (socket.isConnected()) {
                    PrintWriter pw = new
                            PrintWriter(socket.getOutputStream(), true);
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()));
                    if (!addRest) {
                        pw.println("search");
                        pw.println(weatherP);
                        pw.println(budgetP);
                        pw.println(drinkP);
                        String[] result = new String[3];
                        double[] location = new double[6];

                        result[0] = br.readLine();
                        location[0] = Double.parseDouble(br.readLine());
                        location[1] = Double.parseDouble(br.readLine());
                        /*if (location[0] == 25.022109)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(viewNow.getContext(),
                                        "here",
                                        Toast.LENGTH_LONG).show();
                            }
                        });*/
                        result[1] = br.readLine();
                        location[2] = Double.parseDouble(br.readLine());
                        location[3] = Double.parseDouble(br.readLine());
                        result[2] = br.readLine();
                        location[4] = Double.parseDouble(br.readLine());
                        location[5] = Double.parseDouble(br.readLine());
                        intent.putExtra("LOCATION", location);
                        intent.putExtra("RESULT", result);
                        startActivity(intent);
                    }
                    else {
                        pw.println("addRest");
                        pw.println(weatherP);
                        pw.println(budgetP);
                        pw.println(drinkP);
                        pw.println(newRest);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(viewNow.getContext(),
                                        "Thanks for your recommendation!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            } catch(IOException e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(viewNow.getContext(),
                                "Sorry, you are not connected to server.",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
    public void decide(View view){
        viewNow = view;
        addRest = false;
        intent = new Intent(this, DisplayResultActivity.class);
        Thread t = new thread();
        t.start();
    }
    public String newRest;
    public void addNewRestaurant(View view){
        viewNow = view;
        addRest = true;
        EditText editText = (EditText) findViewById(R.id.newRest);
        newRest = editText.getText().toString();
        if(newRest.equals("")) {
            Toast.makeText(viewNow.getContext(),
                    "Please enter the restaurant name. ",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Thread t = new thread();
        t.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            int section=getArguments().getInt(ARG_SECTION_NUMBER);
            switch(section){
                case 1: {
                    rootView = inflater.inflate(R.layout.fragment_main1, container, false);
                    findViews(rootView);
                    RadioButton n = (RadioButton)rootView.findViewById(R.id.noButton);
                    n.setChecked(true);
                    break;
                }
                case 2: {
                    rootView = inflater.inflate(R.layout.fragment_main2, container, false);
                    findViews(rootView);
                    break;
                }
                default: {
                    rootView = inflater.inflate(R.layout.fragment_main, container, false);
                }
            }
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
