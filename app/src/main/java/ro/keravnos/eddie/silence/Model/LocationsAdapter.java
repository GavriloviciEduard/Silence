package ro.keravnos.eddie.silence.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chootdev.csnackbar.Align;
import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Type;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ro.keravnos.eddie.silence.R;

import static com.chootdev.csnackbar.Snackbar.type;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.ViewHolder>
{

    private ArrayList<LatLng> Locations;

    private LatLng mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private View locationsView;
    private Context context;
    private Snackbar snackbar;
    private View rootView;


    public LocationsAdapter(ArrayList<LatLng> locations,Context context, View rootView)
    {
        this.Locations  = locations;
        this.context= context;
        this.rootView = rootView;

    }

    @NonNull
    @Override
    public LocationsAdapter.ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        locationsView = inflater.inflate(R.layout.location_item, parent, false);

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

        Geocoder geoCoder = new Geocoder(this.context);
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

        textView.setText(sb);
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

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        ViewHolder( View itemView )
        {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView =  itemView.findViewById(R.id.location_name);

        }
    }

    void deleteItem( int position )
    {
        mRecentlyDeletedItem = Locations.get(position);
        mRecentlyDeletedItemPosition = position;
        Locations.remove(position);
        notifyItemRemoved(position);
        update_array();
        showUndoSnackbar();
    }

    private void showUndoSnackbar( )
    {
        if(snackbar!=null)
            snackbar.dismiss();

        View view = rootView.findViewById(R.id.locations);
        snackbar = Snackbar.make(view, R.string.snack_bar_text, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> undoDelete());
        snackbar.show();

    }

    private void undoDelete()
    {
        Locations.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);

        update_array();
    }

    @SuppressLint("ApplySharedPref")
    private void update_array()
    {
        SharedPreferences saved_locations =context.getSharedPreferences("ro.keravnos.eddie.silence", 0);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor prefsEditor = saved_locations.edit();
        saved_locations.edit().remove("LOCArray").commit();

        Gson gson = new Gson();
        String json = gson.toJson(this.Locations);
        prefsEditor.putString("LOCArray", json);
        prefsEditor.commit();

    }
}
