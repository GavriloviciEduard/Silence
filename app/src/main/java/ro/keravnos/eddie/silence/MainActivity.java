package ro.keravnos.eddie.silence;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import ro.keravnos.eddie.silence.Model.BottomNav;
import ro.keravnos.eddie.silence.Model.ViewPagerAdapter;
import ro.keravnos.eddie.silence.Model.Notifications;

public class MainActivity extends AppCompatActivity
{
    public static final String CHANNEL_ID = "0" ;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent serviceIntent = new Intent(this, Notifications.class);
        startService(serviceIntent);

        ViewPager viewPager = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ViewPagerAdapter v =new ViewPagerAdapter(getSupportFragmentManager());

        BottomNav  bot = new BottomNav();
        bot.create(v,bottomNavigationView, viewPager);
    }

}
