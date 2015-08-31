package com.example.aftab.guelphbusstops;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aftab on 2015-02-28.
 */

public class FavouritesFragment extends Fragment {

    private List<RouteInfo> routeList;

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

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private RecyclerView recList;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private View view;

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity().getApplicationContext(),
                        new RecyclerItemClickListener.OnItemClickListener() {

                            @Override public void onItemClick(View view, int position) {

                                RouteInfo item = routeList.get(position);
                                Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();

                                /* start new activity with route */
                                if (item.getTitle().equalsIgnoreCase("no favourites") == false) {
                                    Intent intent = new Intent(getActivity().getApplicationContext(), StopsActivity.class);
                                    intent.putExtra("route_name", item.getTitle());
                                    startActivity(intent);
                                }
                            }
                        })
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.list_fragment, container, false);

        recList = (RecyclerView) view.findViewById(R.id.cardList);
        mDrawerList = (ListView) view.findViewById(R.id.list_slidermenu);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        //recList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        Thread myThread;

        Runnable myRunnableThread = new CountDownRunner();
        myThread= new Thread(myRunnableThread);
        myThread.start();

        routeList  = createList();
        if (routeList.isEmpty()){

            RouteInfo routeData = new RouteInfo();
            routeData.setTitle("No Favourites");
            routeData.setRouteName("Add your favourite routes!");
            routeData.setRouteDescription("View routes by swiping from the left.");
            routeList.add(routeData);
        }
        RouteAdapter ca = new RouteAdapter(routeList);
        recList.setAdapter(ca);

        return view;
    }

    /* Thread to update favourites list */
    public void doWork() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                try{

                    routeList = createList();

                    if (routeList.isEmpty()){

                        RouteInfo routeData = new RouteInfo();
                        routeData.setTitle("No Favourites");
                        routeData.setRouteName("Add your favourite routes!");
                        routeData.setRouteDescription("View routes by swiping from the left.");
                        routeList.add(routeData);
                    }

                    RouteAdapter ca = new RouteAdapter(routeList);
                    recList.setAdapter(ca);
                }
                catch (Exception e) {}
            }
        });
    }

    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(1000); // Pause of 1 Second
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                catch(Exception e){
                }
            }
        }
    }

    /* Function to create list of routes */
    private List<RouteInfo> createList() {

        List<RouteInfo> result = new ArrayList<RouteInfo>();

        pref = getActivity().getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
        editor = pref.edit();

        int x=0;
        for (String route : routeID){

            boolean fav = pref.getBoolean(route, false);
            //Log.i("FavouritesFragment_Boolean", "Boolean " + route + " is: " + fav);

            if (fav == true){
                RouteInfo routeData = new RouteInfo();
                routeData.setTitle("Route " + routeID[x]);
                routeData.setRouteName(description[x]);
                routeData.setRouteDescription("Description");
                result.add(routeData);
            }
            x++;
        }
        return result;
    }

}
