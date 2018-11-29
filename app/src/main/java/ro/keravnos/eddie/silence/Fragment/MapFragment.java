package ro.keravnos.eddie.silence.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
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

import ro.keravnos.eddie.silence.Model.CustomViewPager;
import ro.keravnos.eddie.silence.Model.MapTypeH;
import ro.keravnos.eddie.silence.R;



public class MapFragment extends Fragment
{
    public MapView mMapView;
    public GoogleMap googleMap;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    Marker last_location_marker=null;
    LocationManager mLocationManager;
    View rootView;
    boolean focused = false;
    Context BottomNavigation ;

    PlaceAutocompleteFragment mSearchPAF;

    MapTypeH M;


    public MapFragment()
    {

    }



    @SuppressLint("ValidFragment")
    public MapFragment( Context context, MapTypeH M)
    {

        this.BottomNavigation = context;
        this.M = M;

    }

    public boolean isGPSEnabled(Context mContext)
    {
        LocationManager lm = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void pop_up_adress(LatLng point) {



        View shadow = ((Activity) this.BottomNavigation).findViewById(R.id.bottom_navigation);
        shadow.setVisibility(View.GONE);

        View blank = rootView.findViewById(R.id.shadow);
        blank.setVisibility(View.INVISIBLE);

        /*CustomViewPager swipe = ((Activity) this.BottomNavigation).findViewById(R.id.viewpager);
        swipe.disableScroll(true);*/


        View win = rootView.findViewById(R.id.down);
        win.setVisibility(View.VISIBLE);







        TextView text = rootView.findViewById(R.id.textView_adress);

        Geocoder geoCoder = new Geocoder(getContext());
        List<Address> matches = null;
        SpannableStringBuilder sb = new SpannableStringBuilder();

        try
        {
            //PROBLEMATIC ARE NEVOIE MEREU NEVOIE DE NET
            matches = geoCoder.getFromLocation(point.latitude, point.longitude, 1);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if( matches!=null )
        {
            if(!matches.isEmpty())
            {
                String address = Objects.requireNonNull(matches).get(0).getAddressLine(0); //0 to obtain first possible address
                String city = matches.get(0).getLocality();
                //String state = matches.get(0).getAdminArea();
                String country = matches.get(0).getCountryName();
                //String postalCode = matches.get(0).getPostalCode();


                if(address!=null)
                {
                    sb.append(address).append(System.getProperty("line.separator"));
                }

                else if(city!=null)
                {

                    sb.append(city);
                }

                else if(country!=null)
                {
                    sb.append(" ").append(country);
                }

                if(sb.length()>0)
                {
                    // create a bold StyleSpan to be used on the SpannableStringBuilder
                    StyleSpan b = new StyleSpan(android.graphics.Typeface.BOLD); // Span to make text bold

                    // set only the name part of the SpannableStringBuilder to be bold --> 16, 16 + name.length()
                    if(address!=null)
                        sb.setSpan(b, 0, address.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold


                    else if(city!=null)
                        sb.setSpan(b, 0, city.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold


                    else if(country!=null)
                        sb.setSpan(b, 0, country.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold
                }

            }

            else
            {

                sb.append(point.toString());
            }

        }

        else
        {

            sb.append(point.toString());
        }



        text.setText(sb);


    }

    public void destroy_pop_up_adress()
    {
        View win =  rootView.findViewById(R.id.down);
        win.setVisibility(View.INVISIBLE);
        View shadow = ((Activity)this.BottomNavigation).findViewById(R.id.bottom_navigation);
        shadow.setVisibility(View.VISIBLE);

        View blank = rootView.findViewById(R.id.shadow);
        blank.setVisibility(View.VISIBLE);

        /*CustomViewPager swipe = ((Activity) this.BottomNavigation).findViewById(R.id.viewpager);
        swipe.disableScroll(false);*/

    }





    public void set_map()
    {

        Button clickButton = rootView.findViewById(R.id.buttonAD);
        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                v.startAnimation(buttonClick);
            }
        });


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
                if(isNetworkAvailable() && isGPSEnabled(getContext()))
                {
                    if(last_location_marker == null)
                    {
                        last_location_marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(place.getLatLng().latitude,place.getLatLng().longitude)).visible(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        CameraPosition camera = new CameraPosition.Builder().target(new LatLng(place.getLatLng().latitude,place.getLatLng().longitude)).zoom(17).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));

                        pop_up_adress(last_location_marker.getPosition());


                    }
                    else
                    {
                        last_location_marker.setPosition(new LatLng(place.getLatLng().latitude,place.getLatLng().longitude));
                        CameraPosition camera = new CameraPosition.Builder().target(new LatLng(place.getLatLng().latitude,place.getLatLng().longitude)).zoom(17).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));

                        pop_up_adress(last_location_marker.getPosition());


                    }
                }

                else
                {
                    if(!isNetworkAvailable())
                        Toast.makeText(getActivity(), "Internet and network is required", Toast.LENGTH_LONG).show();

                    else if(!isGPSEnabled(getContext()))
                        Toast.makeText(getActivity(), "Location services must be enabled", Toast.LENGTH_LONG).show();
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

                googleMap.setMyLocationEnabled(true);

                SharedPreferences s = getActivity().getSharedPreferences("ro.keravnos.eddie.silence", 0);
                boolean o = s.getBoolean("switchkeySETTINGS2",false);

                if(o == false)
                {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                else
                {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }


                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
                {

                    @Override
                    public void onMapLongClick(LatLng point)
                    {

                        if(isNetworkAvailable() && isGPSEnabled(getContext()))
                        {
                            if (point!=null)
                            {
                                if (last_location_marker != null)
                                {
                                    last_location_marker.setPosition(point);

                                    pop_up_adress(point);


                                }

                                else
                                {

                                    last_location_marker = googleMap.addMarker(new MarkerOptions().position
                                            (new LatLng(point.latitude
                                                    ,point.longitude))
                                            .draggable(true).visible(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                                    pop_up_adress(point);

                                }
                            }
                        }


                        else
                        {
                            if(!isNetworkAvailable())
                                Toast.makeText(getActivity(), "Internet and network is required", Toast.LENGTH_LONG).show();

                            else if(!isGPSEnabled(getContext()))
                                Toast.makeText(getActivity(), "Location services must be enabled", Toast.LENGTH_LONG).show();

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
                mLocationManager =  (LocationManager)Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
                //mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                LocationListener listener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location)
                    {
                        if(!focused)
                        {
                            focused = true;
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            LatLng myPosition = new LatLng(latitude, longitude);

                            CameraPosition cameraPosition = new CameraPosition.Builder().target(myPosition).zoom(17).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }

                        SharedPreferences s = getActivity().getSharedPreferences("ro.keravnos.eddie.silence", 0);
                        boolean o = s.getBoolean("switchkeySETTINGS2",false);

                        if(o == true && googleMap.getMapType() != GoogleMap.MAP_TYPE_HYBRID)
                        {
                            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        }

                        if(o == false && googleMap.getMapType() != GoogleMap.MAP_TYPE_NORMAL)
                        {
                            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        }

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,listener);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,listener);

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

        //View main = ((Activity) this.BottomNavigation).findViewById(R.id.activity_main);

        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        View shadow = rootView.findViewById(R.id.shadow);//#1F000000
        shadow.bringToFront();
        shadow.setVisibility(View.VISIBLE);

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
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), " Location Permission granted", Toast.LENGTH_LONG).show();
                    set_map();

                }

                else
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Location Permission denied", Toast.LENGTH_LONG).show();
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
