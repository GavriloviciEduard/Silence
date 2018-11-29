package ro.keravnos.eddie.silence.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import ro.keravnos.eddie.silence.Model.LocationsAdapter;
import ro.keravnos.eddie.silence.R;

public class LocationsFragment extends Fragment
{
    ArrayList<LatLng> Locations;
    View rootView;
    RecyclerView recyclerView;
    LocationsAdapter LAdapter;


    public LocationsFragment()
    {
        this.Locations = new ArrayList<>();
    }

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_locations,container,false);

        recyclerView =rootView.findViewById(R.id.locations);
        LAdapter = new LocationsAdapter(Locations);
        recyclerView.setAdapter(LAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    public void add_location(LatLng location)
    {
        if(!this.Locations.contains(location))//no dubs
        {
            this.Locations.add(location);
            LAdapter.notifyItemInserted(LAdapter.getItemCount());
        }


    }

    public void delete_location()
    {

    }



}