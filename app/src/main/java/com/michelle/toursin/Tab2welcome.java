package com.michelle.toursin;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

//https://stackoverflow.com/questions/6091194/how-to-handle-button-clicks-using-the-xml-onclick-within-fragments

public class Tab2welcome extends Fragment implements View.OnClickListener {
    AutoCompleteTextView myLocation;
    Button getMap;
//    public String[] languages={"Marina Bay Sands ","Singapore Flyer","Vivo City",
//            "Resorts World Sentosa","Buddha Tooth Relic Temple","Zoo"};

    List<String> languages= new ArrayList<>(Arrays.asList("Asian Civilisation Musuem","Changi Prison Chapel and Musuem","East Coast Park","Fort Canning Park","Gardens by the Bay","Haw Par Villa","Istana","Jurong Bird Park","Kranji War Memorial","Lau Pa Sat","National Musuem","Peranakan Musuem","Raffles Place","Universals Studio Singapore","Marina Bay Sands ","Singapore Flyer","Vivo City","Resorts World Sentosa","Buddha Tooth Relic Temple","Zoo"));



    MapView mMapView;
    private GoogleMap googleMap;
    private Marker marker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2welcome, container, false);

        myLocation = (AutoCompleteTextView) rootView.findViewById(R.id.ET_enterDest);
        registerViews(); //REGEX!!//


        //button
        getMap = (Button) rootView.findViewById(R.id.Button_GetDest);
        getMap.setOnClickListener(this);



        ///////////////
        //Autocorrect//
        ///////////////
        ArrayAdapter adapter = new
                ArrayAdapter(this.getActivity(),android.R.layout.simple_list_item_1,languages);

        myLocation.setAdapter(adapter);
        myLocation.setThreshold(1);
        ///////////////
        //Autocorrect//
        ///////////////



        ////////////
        //Map View//
        ////////////
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            //MapIntializer extend object
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                //googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sg = new LatLng(1.355213, 103.818718);
                marker = googleMap.addMarker(new MarkerOptions().position(sg).title("Singapore").snippet("An island city-state with a tropical climate"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sg).zoom(11).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        ////////////
        //Map View//
        ////////////

        return rootView;
    }


    public void onClick (View view){
        Geocoder geocoder;
        List<Address> addresses; //store the location in the list of addresses

        geocoder = new Geocoder(getActivity(), Locale.getDefault()); //context object thats why feed in the this

        switch (view.getId()) {
            case R.id.Button_GetDest:
                String inputDestination = myLocation.getText().toString().trim(); //get the text from edittext

                languages.add(inputDestination);

                ArrayAdapter adapter = new
                        ArrayAdapter(this.getActivity(),android.R.layout.simple_list_item_1,languages);

                myLocation.setAdapter(adapter);

                Log.i("test", inputDestination);
                if (checkValidation()){
                    submitForm();
                    try {
                        addresses = geocoder.getFromLocationName(inputDestination, 1);

                        // get the latitude and longitude from the 0-th elements of the List object
                        double latitude = addresses.get(0).getLatitude();
                        double longtitude = addresses.get(0).getLongitude();

                        // set up a new LatLng object
                        LatLng inputDes = new LatLng(latitude, longtitude);

                        //use the setPosition method of the marker object to move the marker
                        marker.setPosition(inputDes);

                        // use the moveCamera method of mMap to move the view
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(inputDes));

                        //similarly, set an appropriate zoom level
                        float zoomLevel = 18;
                        googleMap.moveCamera(CameraUpdateFactory.zoomTo(zoomLevel));

                        marker.setTitle(inputDestination);

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                else {
                    Toast.makeText(getActivity(), "Please Enter Location", Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    ////////////
    //Map View//
    ////////////
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    ////////////
    //Map View//
    ////////////



    /////////
    //Regex//
    /////////

    public void registerViews(){
        // TextWatcher would let us check validation error on the fly
        myLocation.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                ValidationRegex.hasText(myLocation);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        myLocation.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
            public void afterTextChanged(Editable s) {
                ValidationRegex.isLocation(myLocation, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
    }

    private void submitForm() {
        // Submit your form here. your form is valid
        Toast.makeText(getActivity(), "Fetching Location", Toast.LENGTH_LONG).show();
    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!ValidationRegex.hasText(myLocation)) ret = false;
        if (!ValidationRegex.isLocation(myLocation, true)) ret = false;


        return ret;
    }



    /////////
    //Regex//
    /////////

}
//Builder only when you need to feed an input
//Intent intent = new Intent(ACTION_VIEW,Uri.parse("http://www.google.com");


