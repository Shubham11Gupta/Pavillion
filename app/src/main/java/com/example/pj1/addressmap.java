package com.example.pj1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Toast;

import com.example.pj1.databinding.ActivityAddressmapBinding;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

public class addressmap extends AppCompatActivity {
    private static final int req_code = 1;
    ActivityAddressmapBinding binding;
    double latitude,longitude;
    private ResultReceiver resultReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressmapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        resultReceiver=new AddressResultReceiver(new Handler());
        binding.loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, req_code);
                } else {
                    getlocation();
                }
            }
        });
        binding.addaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address=binding.add.getText().toString();
                Intent intent=new Intent(addressmap.this,MainActivity.class);
                intent.putExtra("address",address);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int code, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(code, permissions, grantResults);
        if (code == req_code && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getlocation();
            }
        }
    }

    private void getlocation() {
        binding.progressBar.setVisibility(View.VISIBLE);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(addressmap.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(addressmap.this)
                                .removeLocationUpdates(this);
                        if(locationResult!=null && locationResult.getLocations().size()>0){
                            int lastlocationIndex= locationResult.getLocations().size()-1;
                            latitude=locationResult.getLocations().get(lastlocationIndex).getLatitude();
                            longitude=locationResult.getLocations().get(lastlocationIndex).getLongitude();
                            Location location =new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                            fetchAddressFromLatLong(location);
                        }
                        else {
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    }
                }, Looper.getMainLooper());
    }
    private void fetchAddressFromLatLong(Location location){
        Intent intent=new Intent(this,getaddress.class);
        intent.putExtra(constants.Receiver,resultReceiver);
        intent.putExtra(constants.Location_Data_Extra,location);
        startService(intent);
    }
    private  class AddressResultReceiver extends ResultReceiver{
        AddressResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultCode==constants.ScucessResult){
                binding.add.setText(resultData.getString(constants.Result_Data_key));
            }
            else{
                Toast.makeText(addressmap.this, "Address Not Found", Toast.LENGTH_SHORT).show();
            }
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}