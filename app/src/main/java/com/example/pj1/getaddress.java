package com.example.pj1;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class getaddress extends IntentService {
    private ResultReceiver resultReceiver;
    public getaddress(){
        super("getaddress");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            String errorMessage="";
            resultReceiver=intent.getParcelableExtra(constants.Receiver);
            Location location=intent.getParcelableExtra(constants.Location_Data_Extra);
            if(location==null){
                return;
            }
            Geocoder geocoder=new Geocoder(this, Locale.getDefault());
            List<Address> addresses=null;
            try{
                addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            }catch (Exception e){
                errorMessage = e.getMessage();
            }
            if(addresses==null && addresses.isEmpty()){
                DELIVERRESULTTORECEIVER(constants.FailureResult,errorMessage);
            }
            else{
                Address address=addresses.get(0);
                ArrayList<String> addressFragment=new ArrayList<>();
                for(int i=0;i<= address.getMaxAddressLineIndex();i++){
                    addressFragment.add(address.getAddressLine(i));
                }
                DELIVERRESULTTORECEIVER(
                        constants.ScucessResult,
                        TextUtils.join(
                                Objects.requireNonNull(System.getProperty("line.separator")),
                                addressFragment
                        )
                );
            }
        }
    }

    private void DELIVERRESULTTORECEIVER(int resultcode,String addressmessage){
        Bundle bundle=new Bundle();
        bundle.putString(constants.Result_Data_key,addressmessage);
        resultReceiver.send(resultcode,bundle);
    }
}
