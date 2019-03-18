package com.company.simon.androidproject2;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class AddPropertyActivity extends AppCompatActivity implements LogOut, NavigationView.OnNavigationItemSelectedListener{

    private String user;
    private String address;
    private String squareMeters;
    private String bedrooms;
    private String price;
    private Bitmap imgBitmap;
    private boolean sameUser = false;
    private Properties properties;

    private RelativeLayout relativeLayoutImgAdded;
    private RelativeLayout relativeLayoutAddProperty;
    private ImageView imgAddedPhoto;
    private EditText inpAddress;
    private EditText inpCity;
    private EditText inpState;
    private EditText inpSquareMeters;
    private EditText inpBedrooms;
    private EditText inpPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_navigation_add_property);
        properties = Properties.getProperties();

        relativeLayoutImgAdded = findViewById(R.id.relativeLayoutImgAdded);
        relativeLayoutAddProperty = findViewById(R.id.relativeLayoutAddProperty);
        imgAddedPhoto = findViewById(R.id.imgAddedPhoto);
        inpAddress = findViewById(R.id.inpAddress);
        inpCity = findViewById(R.id.inpCity);
        inpState = findViewById(R.id.inpState);
        inpSquareMeters = findViewById(R.id.inpSquareMeters);
        inpBedrooms = findViewById(R.id.inpBedrooms);
        inpPrice = findViewById(R.id.inpPrice);

        loadNavBar();
        user = getIntent().getStringExtra(Variables.USERNAME);
        checkIfSameUser();
    }

    private void loadNavBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAddProperty);
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
            Toast.makeText(this, "Currently in 'Add Properties'.", Toast.LENGTH_SHORT).show();
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

    public void checkIfSameUser(){
        if(user != null || !user.isEmpty()) {
            sameUser = true;
        }else {
            logOut(sameUser);
        }
    }

    public void btnClickedAddProperty(View view) {
        address = inpAddress.getText().toString();
        address += ", ";
        address += inpCity.getText().toString();
        address += ", ";
        address += inpState.getText().toString();
        squareMeters = inpSquareMeters.getText().toString();
        bedrooms = inpBedrooms.getText().toString();
        price = inpPrice.getText().toString();

//        imgBitmap = ((BitmapDrawable)imgAddedPhoto.getDrawable()).getBitmap();

        if(address == null || squareMeters == null || bedrooms == null || imgBitmap == null || address.isEmpty() || squareMeters.isEmpty() || bedrooms.isEmpty() || price.isEmpty()){
            Toast.makeText(this, "Must fill in all forms.", Toast.LENGTH_SHORT).show();
            defaultActivity();
        }else {
            Property property = new Property(user, address, squareMeters, bedrooms, price);
            if (properties.addPropertyToServerAndClient(property)){
                Toast.makeText(this, "Property Added!", Toast.LENGTH_SHORT).show();
                goToPropertiesActivity();
                finish();
            }else {
                Toast.makeText(this, "Property already exists.", Toast.LENGTH_SHORT).show();
                defaultActivity();
            }
        }
    }

    public void defaultActivity(){
        inpAddress.setText(Variables.EMPTY);
        inpSquareMeters.setText(Variables.EMPTY);
        inpBedrooms.setText(Variables.EMPTY);
        inpPrice.setText(Variables.EMPTY);
        if(imgBitmap == null){
            relativeLayoutAddProperty.setVisibility(View.VISIBLE);
            relativeLayoutImgAdded.setVisibility(View.GONE);
        }
    }

    public void btnClickedAddPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, Variables.REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Variables.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            assert extras != null;
            imgBitmap = (Bitmap) extras.get(Variables.DATA);
            imgAddedPhoto.setImageBitmap(imgBitmap);
            relativeLayoutAddProperty.setVisibility(View.GONE);
            relativeLayoutImgAdded.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Currently, picture will not be used for property.", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToPropertiesActivity(){
        Intent intent = new Intent(this, PropertiesActivity.class);
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


    public void btnClickedDeleteTakenPhoto(View view) {
        relativeLayoutAddProperty.setVisibility(View.VISIBLE);
        relativeLayoutImgAdded.setVisibility(View.GONE);
        Toast.makeText(this, "Photo deleted.", Toast.LENGTH_SHORT).show();
    }


}
