package com.example.brown.sb;

import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Brown on 5/3/2017.
 */

public class home_map extends Fragment implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    LocationManager locationManager;
    Location currentlocation;
    double lat,lon;

    String lataddress = "http://gradebook2go.000webhostapp.com/retrievelat.php";
    String lngaddress = "http://gradebook2go.000webhostapp.com/retrievelng.php";
    InputStream is;
    String line = null;
    String result = null;
    String[] locations,location;
    BufferedReader br;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_map,container,false);

        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapV);
        mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Runnable lat = new Runnable() {
            @Override
            public void run() {
                getLat();
            }
        };

        Runnable lng = new Runnable() {
            @Override
            public void run() {
                getLng();
            }
        };

        Thread one = new Thread(lat);
        Thread two = new Thread(lng);
        one.start();
        two.start();
    }

    //Thread retrieves longitude from database
    public void getLng(){
        try
        {
            URL url = new URL(lngaddress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            is = new BufferedInputStream(con.getInputStream());
        }
        catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try
        {
            br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while((line = br.readLine()) != null)
            {
                sb.append(line+"\n");
            }

            is.close();
            result = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray js = new JSONArray(result);
            JSONObject jo = null;

            location = new String[js.length()];

            for(int i = 0; i < js.length(); i++){
                jo = js.getJSONObject(i);
                location[i] = jo.getString("lng");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Thread retrieves latitude from database
    private void getLat(){
        try
        {
            URL url = new URL(lataddress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            is = new BufferedInputStream(con.getInputStream());
        }
        catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while((line = br.readLine()) != null)
            {
                sb.append(line+"\n");
            }

            is.close();
            result = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray js = new JSONArray(result);
            JSONObject jo = null;

            locations = new String[js.length()];

            for(int i = 0; i < js.length(); i++){
                jo = js.getJSONObject(i);
                locations[i] = jo.getString("lat");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.getUiSettings().setZoomControlsEnabled(true);
        getLat();
        getLng();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        currentlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        lat = currentlocation.getLatitude();
        lon = currentlocation.getLongitude();

        //final LatLng taj = new LatLng(27.173891, 78.042068);
        final LatLng cl = new LatLng(lat,lon);

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Uses user's latitude and longitude to create place marker
        mMap.addMarker(new MarkerOptions().position(cl).title("I'm Here"));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cl,17));
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);

        for(int i = 0; i < locations.length; i++){
            if(locations == null){
                MessageToast.message(getContext(),"No Friends Online");
            }else {
                if((locations[i] != "")&&(location[i] != "")) {
                    double lt = Double.parseDouble(locations[i]);
                    double ln = Double.parseDouble(location[i]);

                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lt, ln))
                            .title("Marker")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }
            }
        }
    }
}
