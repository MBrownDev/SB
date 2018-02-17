package com.example.brown.sb;

import android.Manifest;
import android.location.Location;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
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
import java.util.List;
import java.util.Vector;

/**
 * Created by Brown on 12/28/2016.
 */

public class home_screen extends Fragment implements OnMapReadyCallback {
    MapView mapView;
    GoogleMap map;
    LocationManager locationManager;
    Location currentlocation;
    double lat,lon;

    ArrayAdapter<String> gpsAdapter;
    String lataddress = "http://gradebook2go.000webhostapp.com/retrievelat.php";
    String lngaddress = "http://gradebook2go.000webhostapp.com/retrievelng.php";
    InputStream is;
    String line = null;
    String result = null;
    String[] locations,location;
    SupportMapFragment mapFragment;
    FrameLayout home;

    private pager_adapter pagerAdapter;
    ViewPager pager;
    BufferedReader br;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        View v = inflater.inflate(R.layout.home_screen, container, false);

        pager = (ViewPager)v.findViewById(R.id.page_holder);

        return v;
    }

    public void initializePaging(){
        List<Fragment> fragments = new Vector<>();
        fragments.add(Fragment.instantiate(getContext(),class_list.class.getName()));
        fragments.add(Fragment.instantiate(getContext(),add_screen.class.getName()));
        pagerAdapter = new pager_adapter(getChildFragmentManager(),fragments);

        pager.setAdapter(pagerAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        //Inflate frame with MapView fragment
        home_map hm = new home_map();
        FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
        tran.replace(R.id.frame,hm);
        tran.addToBackStack(null);
        tran.commit();

        List<Fragment> fragments = new Vector<>();
        fragments.add(Fragment.instantiate(getContext(),class_list.class.getName()));
        fragments.add(Fragment.instantiate(getContext(),assignment_list.class.getName()));
        fragments.add(Fragment.instantiate(getContext(),pending_friends.class.getName()));
        pagerAdapter = new pager_adapter(getChildFragmentManager(),fragments);

        pager.setAdapter(new pager_adapter(getChildFragmentManager(),fragments));
    }

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

//        getLat();
//        getLng();
//        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//
//        currentlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//        lat = currentlocation.getLatitude();
//        lon = currentlocation.getLongitude();
//
//        //final LatLng taj = new LatLng(27.173891, 78.042068);
//        final LatLng cl = new LatLng(lat,lon);
//
//        map = googleMap;
//        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//
//        map.addMarker(new MarkerOptions().position(cl).title("Taj Mahal"));
//        map.getUiSettings().setZoomControlsEnabled(true);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cl,17));
//        map.getUiSettings().setScrollGesturesEnabled(true);
//        map.getUiSettings().setMapToolbarEnabled(true);
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        map.getUiSettings().setMyLocationButtonEnabled(false);
//        map.setMyLocationEnabled(true);
//
//        for(int i = 0; i < locations.length; i++){
//            if(locations == null){
//                MessageToast.message(getContext(),"No Friends Online");
//            }else {
//                if((locations[i] != "")&&(location[i] != "")) {
//                    double lt = Double.parseDouble(locations[i]);
//                    double ln = Double.parseDouble(location[i]);
//
//                    map.addMarker(new MarkerOptions()
//                            .position(new LatLng(lt, ln))
//                            .title("Marker")
//                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//                }
//            }
//        }
    }
}
