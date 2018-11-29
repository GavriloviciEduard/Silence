package ro.keravnos.eddie.silence.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import ro.keravnos.eddie.silence.R;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.ViewHolder>
{

    private ArrayList<LatLng> Locations;
    private int index =1;

    public  LocationsAdapter()
    {}

    public LocationsAdapter(ArrayList<LatLng> locations)
    {
        this.Locations  = locations;
    }

    @NonNull
    @Override
    public LocationsAdapter.ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View locationsView = inflater.inflate(R.layout.location_item, parent, false);

        // Return a new holder instance

        return new ViewHolder(locationsView);
    }

    // Involves populating data into the item through holder
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder( @NonNull LocationsAdapter.ViewHolder viewHolder, int position)
    {
        // Get the data model based on position
        LatLng point = Locations.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(String.valueOf(index) +point.toString());
        index++;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount()
    {
        return Locations.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView nameTextView;
        //Button deleteButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        ViewHolder( View itemView )
        {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView =  itemView.findViewById(R.id.location_name);
            //deleteButton =  itemView.findViewById(R.id.delete_button);
        }
    }
}
