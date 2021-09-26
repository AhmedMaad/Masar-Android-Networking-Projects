package com.maad.mapsapp;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.maad.mapsapp.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private double latitude;
    private double longitude;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    LatLng userLocation = new LatLng(latitude, longitude);
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                                    getLocationFromLatLng();
                                }
                            }
                        });
            }
        });

        binding.btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng egypt = new LatLng(30.0444, 31.2357);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(egypt, 15));

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                binding.ivDot.setVisibility(View.VISIBLE);

                Animation animation = AnimationUtils.loadAnimation(MapsActivity.this
                        , R.anim.pointer_zoom_out);
                binding.ivLocation.startAnimation(animation);
                animation.setFillAfter(true);

                binding.locationText.setText("");
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                binding.ivDot.setVisibility(View.INVISIBLE);

                Animation animation = AnimationUtils.loadAnimation(MapsActivity.this
                        , R.anim.pointer_zoom_in);
                binding.ivLocation.startAnimation(animation);

                latitude = mMap.getCameraPosition().target.latitude;
                longitude = mMap.getCameraPosition().target.longitude;
                Log.d("trace", latitude + ", " + longitude);

                getLocationFromLatLng();

            }
        });

    }

    public void getLocationFromLatLng() {

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            binding.locationText.setText(address);
        } catch (IOException e) {
            Log.d("trace", "Location Error: " + e.getLocalizedMessage());
            binding.locationText.setText("Unknown Location");
        }

    }

}