package com.company.simon.androidproject2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class PropertiesActivity extends AppCompatActivity implements LogOut, NavigationView.OnNavigationItemSelectedListener{

//    private TextView txtUsername;
    private String user;
    private boolean sameUser = false;
    private ListView propertyListView;
    private ArrayAdapter<Property> adapter;


    private int[] propertyFlags = {R.drawable.house1, R.drawable.house2, R.drawable.house3,R.drawable.house4};
    //private String[] addresses = {"1234 Main St. Manhattan, NY", "1390 E12th St. Brooklyn, NY", "1898 Ocean Pkwy Brooklyn, NY", "1273 E73rd St. Brooklyn, NY"};
    public List<Property> propertyList;
    public List<Property> propertiesFromServer;
    public Properties properties;
    int amountOfProperties;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        properties = Properties.getProperties();
        getPropertiesFromServer();
        setContentView(R.layout.menu_navigation);
        propertyListView = findViewById(R.id.propertyList);
        loadProperties();
        loadNavBar();
        user = getIntent().getStringExtra(Variables.USERNAME);
        checkIfSameUser();
    }

    private void getPropertiesFromServer() {
//        ServerSideGet serverSideGetProperties = new ServerSideGet();
//        serverSideGetProperties.executeRequestToServer(Variables.LOAD_ALL_PROPERTIES);
        amountOfProperties = properties.getPropertyMapSize();
        propertiesFromServer = new ArrayList<>(properties.propertyMap.values());
    }

    private void loadNavBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadProperties() {
        propertyList = new ArrayList<>();
        for (int i = 0; i < amountOfProperties; i++) {
            Property tempProperty = propertiesFromServer.get(i);
            Property property = new Property(
                    tempProperty.getUser(),
                    tempProperty.getAddress(),
                    tempProperty.getSquareMeters(),
                    tempProperty.getBedrooms(),
                    tempProperty.getPrice(),
                    propertyFlags[i % propertyFlags.length]);
            propertyList.add(property);
        }
        adapter = new PropertyAdapter(this, propertyList);

        propertyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String property = String.valueOf(propertyList.get(position));
                Toast.makeText(PropertiesActivity.this, "You clicked on: " + property, Toast.LENGTH_SHORT).show();
            }
        });
        propertyListView.setAdapter(adapter);
    }


    public void checkIfSameUser(){
        if(user != null || !user.isEmpty()) {
            sameUser = true;
        }else {
            logOut(sameUser);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navProperties) {
            Toast.makeText(this, "Currently in 'Properties Activity'.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.navAddProperty) {
            goToAddPropertyActivity();
        } else if (id == R.id.navReviews) {
            goToReviewsActivity();
        } else if (id == R.id.navMyAccount) {
            goToMyAccountActivity();
        } else if (id == R.id.navLogOut) {
            logOut(false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToAddPropertyActivity(){
        Intent intent = new Intent(this, AddPropertyActivity.class);
        intent.putExtra(Variables.USERNAME, user);
        startActivity(intent);
    }

    public void goToReviewsActivity(){
        Intent intent = new Intent(this, ReviewsActivity.class);
        intent.putExtra(Variables.USERNAME, user);
        startActivity(intent);
    }

    public void goToMyAccountActivity(){
        Intent intent = new Intent(this, MyAccountActivity.class);
        intent.putExtra(Variables.USERNAME, user);
        startActivity(intent);
    }

    @Override
    public void logOut(boolean sameUser) {
        if(!sameUser){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("from", "activity");
            startActivity(intent);
            finish();
        }
    }



}
