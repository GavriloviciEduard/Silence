package ro.keravnos.eddie.silence;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

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

        boolean ok = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if(Build.VERSION.SDK_INT >= 23)
        {
            if(!ok)
            {
                Permission();
            }
        }

        Intent serviceIntent = new Intent(this, Notifications.class);
        startService(serviceIntent);


        ViewPager viewPager = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ViewPagerAdapter v =new ViewPagerAdapter(getSupportFragmentManager());

        BottomNav  bot = new BottomNav();
        bot.create(v,bottomNavigationView, viewPager);
    }

    private void Permission()
    {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }
}
