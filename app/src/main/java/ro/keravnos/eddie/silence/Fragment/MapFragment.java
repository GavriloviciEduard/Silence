package ro.keravnos.eddie.silence.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import ro.keravnos.eddie.silence.R;
import static android.content.Context.LOCATION_SERVICE;



public class MapFragment extends Fragment
{
    public MapView mMapView;
    public GoogleMap googleMap;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    Marker last_location_marker=null;
    LocationManager mLocationManager;
    View rootView;

    PlaceAutocompleteFragment mSearchPAF;

    private Location getLastKnownLocation()
    {


        mLocationManager = (LocationManager)Objects.requireNonNull(getContext()).getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers)
        {
            @SuppressLint("MissingPermission") Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }






    public MapFragment()
    {

    }

    public void pop_up_adress(LatLng point) {

        View win = rootView.findViewById(R.id.down);
        win.setVisibility(View.VISIBLE);

        TextView text = rootView.findViewById(R.id.textView_adress);
        String adress ;

        Geocoder geoCoder = new Geocoder(getContext());
        List<Address> matches = null;

        try
        {
            matches = geoCoder.getFromLocation(point.latitude, point.longitude, 1);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Address bestMatch = (Objects.requireNonNull(matches).isEmpty() ? null : matches.get(0));
        adress = Objects.requireNonNull(bestMatch).toString();
        text.setText(adress);


    }

    public void destroy_pop_up_adress()
    {
        View win =  rootView.findViewById(R.id.down);
        win.setVisibility(View.INVISIBLE);
    }



    public void set_map()
    {


        try
        {
            MapsInitializer.initialize(Objects.requireNonNull(getActivity()).getApplicationContext());
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }


        mSearchPAF = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        mSearchPAF.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place)
            {
                if(last_location_marker == null)
                {
                    last_location_marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(place.getLatLng().latitude,place.getLatLng().longitude)).visible(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    CameraPosition camera = new CameraPosition.Builder().target(new LatLng(place.getLatLng().latitude,place.getLatLng().longitude)).zoom(17).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
                }
                else
                {
                    last_location_marker.setPosition(new LatLng(place.getLatLng().latitude,place.getLatLng().longitude));
                    CameraPosition camera = new CameraPosition.Builder().target(new LatLng(place.getLatLng().latitude,place.getLatLng().longitude)).zoom(17).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
                }

            }

            @Override
            public void onError(Status status)
            {

            }
        });

        mMapView.getMapAsync(new OnMapReadyCallback()
        {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady( final GoogleMap mMap )
            {

                googleMap = mMap;

                //googleMap.setMapType(2);

                googleMap.setMyLocationEnabled(true);



                Location location = getLastKnownLocation();


                if(location != null)
                {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng myPosition = new LatLng(latitude, longitude);


                    CameraPosition cameraPosition = new CameraPosition.Builder().target(myPosition).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }



                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
                {

                    @Override
                    public void onMapLongClick(LatLng point)
                    {
                        if (last_location_marker != null)
                        {
                            last_location_marker.setPosition(point);

                            Toast.makeText(getContext(),"AT:-> " + point.toString(), Toast.LENGTH_LONG).show();
                            pop_up_adress( point);
                        }

                        else
                        {

                            last_location_marker = googleMap.addMarker(new MarkerOptions().position
                                    (new LatLng(point.latitude
                                    ,point.longitude))
                                    .draggable(true).visible(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                            Toast.makeText(getContext(),"AT:-> " + point.toString(), Toast.LENGTH_LONG).show();
                            pop_up_adress(point);

                        }

                    }
                });


                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
                {
                    @Override
                    public void onMapClick( LatLng latLng )
                    {
                            googleMap.clear();
                            last_location_marker = null;
                            destroy_pop_up_adress();
                    }
                });

            }
        });






        View locationButton = ((View)mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        // and next place it, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
        locationButton.getLayoutParams();
        // position on right bottom
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.setMargins(0, 0, 30, 30);



    }

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {

        rootView = inflater.inflate(R.layout.fragment_map, container, false);


        mMapView = rootView.findViewById(R.id.mapView);

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();



        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (shouldShowRequestPermissionRationale( android.Manifest.permission.ACCESS_FINE_LOCATION))
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }

            else
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }

        else
            set_map();





        return rootView;
    }


    @Override
    public void onResume()
    {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onRequestPermissionsResult( int requestCode,
                                            @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION :
                {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), " Location Permission granted", Toast.LENGTH_SHORT).show();
                    set_map();

                }

                else
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Location Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mSearchPAF.onActivityResult(requestCode, resultCode, data);
    }
}
