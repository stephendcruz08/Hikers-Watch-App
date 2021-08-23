package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocation(location);
            }
        };

        // Checking if the User has granted us location permission if not requesting location permission

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                updateLocation(lastLocation);
            }
        }
    }

     // Responding to users answers regarding granting location permission

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();

        }
    }

    // Function to execute the next steps if after the user has granted us location permission

    public  void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }
         // Function for Updating the location

        public void updateLocation(Location location){
            TextView lattitude = findViewById(R.id.latTextView);
            TextView longitude= findViewById(R.id.longTextView);
            TextView accuracy = findViewById(R.id.accTextView);
            TextView address = findViewById(R.id.addTextView);

            lattitude.setText("Lattitude : " + Double.toString(location.getLatitude()));
            longitude.setText("Longitude : " + Double.toString(location.getLongitude()));
            accuracy.setText("Accuracy : " + Double.toString(location.getAccuracy()));

            String adress = " Could not find address :(  ";
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if(addressList != null && addressList.size() > 0){
                    adress = " Address : " + "\n";
                    if(addressList.get(0).getCountryName() != null){
                        adress += addressList.get(0).getCountryName() + "\n";
                    }
                    if(addressList.get(0).getAdminArea() != null){
                        adress += addressList.get(0).getAdminArea() + "\n";
                    }
                    if(addressList.get(0).getLocality() != null){
                        adress += addressList.get(0).getLocality() + "\n";
                    }
                    if(addressList.get(0).getThoroughfare()!= null){
                        adress += addressList.get(0).getThoroughfare() + "\n";
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            address.setText(adress);
    }

}