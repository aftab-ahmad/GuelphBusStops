package com.example.aftab.guelphbusstops;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by aftab on 2015-02-28.
 */
public class ManageActivity extends ActionBarActivity {

    private final String TAG = "SignupActivity";
    private Toolbar toolbar;

    private ArrayList<RouteInfo> list = new ArrayList<>();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private SharedPreferences stopPref;
    private SharedPreferences.Editor stopEditor;

    private int year, month, day;

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
        setContentView(R.layout.manage_activity);

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

    @SuppressWarnings("deprecation")
    public void setDate(View view) {

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener
            = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            Toast.makeText(getApplicationContext(), "Reminder set!", Toast.LENGTH_SHORT).show();
        }
    };

    private void showDate(int year, int month, int day) {
        Toast.makeText(getApplicationContext(), day + " " + month + " " + year, Toast.LENGTH_SHORT).show();
    }

    public void leaveStop (View view) {

        Set <String> S = stopPref.getStringSet(route, null);

        if (S != null) {
            Iterator<String> test = S.iterator();

            while (test.hasNext()) {

                String t = test.next();
                Log.d ("SignupActivity", "String is: " + t);

                if (t.equalsIgnoreCase(stopName)) {
                    S.remove(stopName);
                }
            }
        }

        stopEditor.putStringSet(route, S);
        stopEditor.commit();

        stopEditor.apply();

        Toast.makeText(getApplicationContext(), "No longer registered for route.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity (intent);
    }

    public void uploadPhoto (View view) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    public void maintenanceTime (View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Hours saved!", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.input_dialog, null);
        dialog.setView(dialogLayout);
        dialog.setTitle("Enter Maintenance Time");
        dialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            Uri chosenImageUri = data.getData();
            Bitmap mBitmap = null;

            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageUri);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Image uploaded!", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Image not saved.", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                LayoutInflater inflater = getLayoutInflater();

                View dialogLayout = inflater.inflate(R.layout.image_dialog, null);
                dialog.setView(dialogLayout);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.show();

                ImageView image = (ImageView) dialog.findViewById(R.id.goProDialogImage);
                image.setImageBitmap(mBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
