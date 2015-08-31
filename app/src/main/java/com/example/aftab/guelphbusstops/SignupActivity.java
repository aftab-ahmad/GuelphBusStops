package com.example.aftab.guelphbusstops;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* Activity to show bus stops for a particular route */
public class SignupActivity extends ActionBarActivity {

    private final String TAG = "SignupActivity";
    private Toolbar toolbar;

    private ArrayList <RouteInfo> list = new ArrayList<>();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private SharedPreferences stopPref;
    private SharedPreferences.Editor stopEditor;

    private String route = "";
    private String stopName = "";
    private String routeID[] = new String[]{"1A","1B","2A","2B","3A","3B","4","5","6","7","8","9","10","11","12","13","14","15","16","20","50","56","57","58"};
    private String description[] = new String[]{
            "College Edinburgh Clockwise",
            "College Edinburgh Counter Clockwise",
            "West Loop Clockwise",
            "West Loop Counter Clockwise",
            "East Loop Clockwise",
            "East Loop Counter Clockwise",
            "York",
            "Gordon",
            "Harvard Ironwood",
            "Kortright Downey",
            "Stone Road Mall",
            "Waterloo",
            "Imperial",
            "Willow West",
            "General Hospital",
            "Victoria Road Recreation Centre",
            "Grange",
            "University College",
            "Southgate",
            "Northwest Industrial",
            "Stone Road Express",
            "Victoria Express",
            "Harvard Express",
            "Edinburgh Express"
    };

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        // set toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Context context = this;

        /* Handle the intent */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            route = extras.getString("route_name");
            stopName = extras.getString("stop_name");
        }

        TextView routeView = (TextView) findViewById(R.id.routeID);
        TextView stopView = (TextView) findViewById(R.id.stopName);

        if (route.contains ("Route")){
            String split [] = route.split(" ");

            int x=0;
            for(String s: routeID){
                if(s.equals(split[1]))
                    routeView.setText(description[x]);
                x++;
            }
        }
        else
        {
            int x=0;
            for(String s: routeID){
                if(s.equals(route))
                    routeView.setText(description[x]);
                x++;
            }
        }

        stopView.setText(stopName);

        /* Load shared preferences for bookmarks
        * will be used to determine which routes have been favourited */
        pref = context.getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
        editor = pref.edit();

        stopPref = context.getSharedPreferences("stops", Context.MODE_PRIVATE);
        stopEditor = stopPref.edit();
    }

    public void testFunc (View view) {

        boolean nameCheck = false;
        boolean addressCheck = false;
        boolean emailCheck = false;
        boolean phoneNumCheck = false;
        boolean termsCheck = false;

        TextView name = (TextView) findViewById(R.id.Name);
        TextView address = (TextView) findViewById(R.id.address);
        TextView email = (TextView) findViewById(R.id.email);
        TextView phoneNum = (TextView) findViewById(R.id.phoneNum);

        CheckBox terms = (CheckBox) findViewById(R.id.terms);

        Log.d (TAG, "name is: " + name.getText().toString());
        Log.d (TAG, "address is: " + address.getText().toString());
        Log.d (TAG, "email is: " + email.getText().toString());
        Log.d (TAG, "phoneNum is: " + phoneNum.getText().toString());
        Log.d (TAG, "terms is: " + terms.isChecked());

        if (name.getText().toString().equalsIgnoreCase("")) {
            nameCheck = true;
            Toast.makeText(this, "Please enter a name.", Toast.LENGTH_SHORT).show();
        }
        if (address.getText().toString().equalsIgnoreCase("")) {
            addressCheck = true;
            Toast.makeText(this, "Please enter an address.", Toast.LENGTH_SHORT).show();
        }
        if (email.getText().toString().equalsIgnoreCase("")) {
            emailCheck = true;
            Toast.makeText(this, "Please enter an email address.", Toast.LENGTH_SHORT).show();
        }
        if (phoneNum.getText().toString().equalsIgnoreCase("")) {
            phoneNumCheck = true;
            Toast.makeText(this, "Please enter a phone number.", Toast.LENGTH_SHORT).show();
        }
        if (terms.isChecked() == false) {
            termsCheck = true;
            Toast.makeText(this, "Please accept terms of use.", Toast.LENGTH_SHORT).show();
        }

        if (nameCheck == false && addressCheck == false && emailCheck == false &&
                phoneNumCheck == false && termsCheck == false) {
            Toast.makeText(this, "Signup Complete! Please check your email.", Toast.LENGTH_SHORT).show();

            Set <String> S;
            S = stopPref.getStringSet(route, null);

            if (S != null) {
                S.add(stopName);
                stopEditor.putStringSet(route, S);
                stopEditor.commit();
                Log.d("SignupActivity", "Size is: " + S.size());
            }
            else {
                S = new HashSet<>();
                S.add(stopName);
                stopEditor.putStringSet(route, S);
                stopEditor.commit();
            }

            S = stopPref.getStringSet(route, null);
            Iterator<String> test = S.iterator();

            while (test.hasNext()) {
                Log.d ("SignupActivity", "String is: " + test.next());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        /* Set the search view */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stops_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.action_search).getActionView();

//        ComponentName cn = new ComponentName(this, SearchActivity.class);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));

        /* Check if route has been favourited, and color the star if needed*/
        if (route.contains ("Route"))
            route = route.replace ("Route ", "");

        boolean fav = pref.getBoolean(route, false);
        Log.i("SignupActivity_Boolean", "Boolean is: " + fav);

        if (fav == false){
            Drawable myIcon = getResources().getDrawable( R.drawable.ic_star_rate_white );
            ColorFilter filter = new LightingColorFilter( Color.BLACK, Color.LTGRAY);
            myIcon.setColorFilter(filter);
        }
        else{
            int hex = 0xFFFF66;

            Drawable myIcon = getResources().getDrawable( R.drawable.ic_star_rate_white );
            ColorFilter filter = new LightingColorFilter( Color.BLACK, hex);
            myIcon.setColorFilter(filter);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            showSettings();
        }
        if (id == R.id.action_star) { // check if route needs to be added or removed from favorites

            boolean fav = pref.getBoolean(route, false);

            if ( fav == false){

                Drawable myIcon = getResources().getDrawable( R.drawable.ic_star_rate_white );

                int color = getResources().getColor(R.color.goldStar);
                myIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN);

                if (route.contains ("Route"))
                    Toast.makeText(this, route + " added to favourites.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Route " + route + " added to favourites.", Toast.LENGTH_SHORT).show();

                if (route.contains ("Route"))
                    route = route.replace ("Route ", "");

                editor.putBoolean(route, true);
                editor.commit();

            }
            else
            {
                Drawable myIcon = getResources().getDrawable( R.drawable.ic_star_rate_white );
                int color = getResources().getColor(R.color.goldStar);
                myIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN);

                if (route.contains ("Route"))
                    Toast.makeText(this, route + " removed from favourites.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Route " + route + " removed from favourites.", Toast.LENGTH_SHORT).show();

                if (route.contains ("Route"))
                    route = route.replace ("Route ", "");

                editor.putBoolean(route, false);
                editor.commit();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettings()
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity (intent);
    }
}
