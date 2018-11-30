package ro.keravnos.eddie.silence.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Objects;

import ro.keravnos.eddie.silence.Model.LocationsAdapter;
import ro.keravnos.eddie.silence.Model.SwipeToDeleteCallback;
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
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences saved_locations = Objects.requireNonNull(getActivity()).getSharedPreferences("ro.keravnos.eddie.silence", 0);
        Gson gson = new Gson();
        String json = saved_locations.getString("LOCArray", "");

        if(json!=null && !json.isEmpty())
        {
            this.Locations = gson.fromJson(json, new TypeToken<ArrayList<LatLng>>(){}.getType());
        }


        recyclerView =rootView.findViewById(R.id.locations);
        LAdapter = new LocationsAdapter(Locations,getContext(),rootView);
        recyclerView.setAdapter(LAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(LAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_locations,container,false);


        return rootView;
    }

    public void add_location(LatLng location)
    {
        if(!this.Locations.contains(location))//no dubs
        {
            this.Locations.add(location);
            LAdapter.notifyItemInserted(LAdapter.getItemCount());
            save_array();
        }


    }

    @SuppressLint("ApplySharedPref")
    public void save_array()
    {
        SharedPreferences saved_locations = Objects.requireNonNull(getActivity()).getSharedPreferences("ro.keravnos.eddie.silence", 0);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor prefsEditor = saved_locations.edit();
        saved_locations.edit().clear().commit();

        Gson gson = new Gson();
        String json = gson.toJson(this.Locations);
        prefsEditor.putString("LOCArray", json);
        prefsEditor.commit();

    }

}