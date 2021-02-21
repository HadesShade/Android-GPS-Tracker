package com.example.locationchecker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.libraries.maps.model.LatLng;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private Bundle bundle = new Bundle();
    protected static LocationManager locMan;
    public static Double lat;
    public static Double lng;
    protected LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateLocation();
        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                String payload = MainActivity.lat + ":" + MainActivity.lng;
                bundle.putString("PAYLOAD", payload);
                intent.putExtra("BUNDLE", bundle);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        else {
            Toast.makeText(this.getApplicationContext(), "Error! Permission denied!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.lat = location.getLatitude();
        this.lng = location.getLongitude();
        showLoc();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(this.getApplicationContext(),String.format("Location Updated!",status,provider),Toast.LENGTH_LONG).show();
    }

    public void showLoc() {
        try {
            Geocoder geo = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(this.lat, this.lng, 1);
            Address obj = addresses.get(0);
            String add = "Address : "+obj.getAddressLine(0);
            add = add + "\nCountry : " + obj.getCountryName();
            add = add + "\nCountry Code : " + obj.getCountryCode();
            add = add + "\nAdministrative Area : " + obj.getAdminArea();
            add = add + "\nPostal Code : " + obj.getPostalCode();
            add = add + "\nSub-Administrative Area : " + obj.getSubAdminArea();
            add = add + "\nLocality : " + obj.getLocality();
            add = add + "\nSub-Thoroughfare : " + obj.getSubThoroughfare();
            add = add + "\nLatitude : " + this.lat;
            add = add + "\nLongitude : " + this.lng;

            TextView txtAD = findViewById(R.id.textView2);

            txtAD.setText(add);
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}