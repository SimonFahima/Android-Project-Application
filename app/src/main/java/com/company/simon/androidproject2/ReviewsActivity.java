package com.company.simon.androidproject2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class ReviewsActivity extends AppCompatActivity implements LogOut, NavigationView.OnNavigationItemSelectedListener {

    UnderConstructionFragment underConstructionFragment;
    private String user;
    private boolean sameUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_navigation_reviews);
        user = getIntent().getStringExtra(Variables.USERNAME);
        checkIfSameUser();
        loadNavBar();
        showUnderConstructionFragment();

    }

    public void showUnderConstructionFragment(){
        underConstructionFragment = new UnderConstructionFragment();
        underConstructionFragment.setActivity(Variables.REVIEW);
        underConstructionFragment.setListener(new UnderConstructionFragment.OnClickedGotItListener() {
            @Override
            public void gotItClicked() {
                Toast.makeText(ReviewsActivity.this, "Thanks for understanding.", Toast.LENGTH_SHORT).show();
            }
        });
        underConstructionFragment.showNow(getSupportFragmentManager(), "");
    }

    private void loadNavBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarReviews);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navProperties) {
            goToPropertiesActivity();
        } else if (id == R.id.navAddProperty) {
            goToAddPropertyActivity();
        } else if (id == R.id.navReviews) {
            Toast.makeText(this, "Currently in 'Reviews'.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.navMyAccount) {
            goToMyAccountActivity();
        } else if (id == R.id.navLogOut) {
            logOut(false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkIfSameUser() {
        if (user != null || !user.isEmpty()) {
            sameUser = true;
        } else {
            logOut(sameUser);
        }
    }

    public void goToPropertiesActivity(){
        Intent intent = new Intent(this, PropertiesActivity.class);
        intent.putExtra(Variables.USERNAME, user);
        startActivity(intent);
    }

    public void goToAddPropertyActivity(){
        Intent intent = new Intent(this, AddPropertyActivity.class);
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
