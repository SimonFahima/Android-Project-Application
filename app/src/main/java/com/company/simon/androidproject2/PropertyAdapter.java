package com.company.simon.androidproject2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PropertyAdapter extends ArrayAdapter<Property> {

    public static final String SIMON = "Simon";
    private Activity activity;
    private List<Property> properties;

    public PropertyAdapter(Activity activity, List<Property> properties) {
        super(activity, R.layout.activity_properties_cards, properties);
        this.activity = activity;
        this.properties = properties;
    }

    static class ViewContainer{
        TextView txtHowManyDaysOnMarket;
        ImageView imgProperty;
        TextView txtAddress;
        TextView txtPostedByUser;
        TextView txtPrice;
        TextView txtBedrooms;
        TextView txtSqmt;
        ImageView btnShowProperty;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewContainer viewContainer;
        View view = convertView;
        if(view == null){
            viewContainer = new ViewContainer();
            view = activity.getLayoutInflater().inflate(R.layout.activity_properties_cards,parent,false);
            viewContainer.imgProperty = view.findViewById(R.id.imgProperty);
            viewContainer.txtAddress = view.findViewById(R.id.txtAddress);
            viewContainer.txtPostedByUser = view.findViewById(R.id.txtPostedByUser);
            viewContainer.txtPrice = view.findViewById(R.id.txtPrice);
            viewContainer.txtBedrooms = view.findViewById(R.id.txtBedrooms);
            viewContainer.txtSqmt = view.findViewById(R.id.txtSqmt);
            view.setTag(viewContainer);
            viewContainer.btnShowProperty = view.findViewById(R.id.btnImgMaps);
            viewContainer.btnShowProperty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    Property property = properties.get(position);
                    Uri uri = Uri.parse("geo:?q=" + Uri.encode(property.getAddress()));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.google.android.apps.maps");
                    if(intent.resolveActivity(activity.getPackageManager()) != null){
                        activity.startActivity(intent);
                    }else {
                        Toast.makeText(activity, "Must download Google Maps.", Toast.LENGTH_SHORT).show();
                        Intent intentForGoogleMaps = new Intent(Intent.ACTION_VIEW);
                        intentForGoogleMaps.setData(Uri.parse("market://details?id=com.google.android.apps.maps"));
                        activity.startActivity(intentForGoogleMaps);
                    }
                }
            });
        }else {
            viewContainer = (ViewContainer) view.getTag();
        }
        Property property = properties.get(position);

//        viewContainer.txtHowManyDaysOnMarket.setText(property.getDaysOnHH());
        viewContainer.txtAddress.setText(property.getAddress());
        viewContainer.txtPostedByUser.setText(property.getUser());
        viewContainer.txtPrice.setText(property.setPrice(property.getPrice()));
        viewContainer.txtBedrooms.setText(property.getBedrooms() + "br");
        viewContainer.txtSqmt.setText(property.getSquareMeters() + "m.");
        viewContainer.imgProperty.setImageResource(property.getFlag());
        viewContainer.btnShowProperty.setTag(position);

        Log.d(SIMON, "Position: " + position);
        return view;
    }
}
