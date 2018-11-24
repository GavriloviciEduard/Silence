package ro.keravnos.eddie.silence;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.util.Set;

import ro.keravnos.eddie.silence.Fragment.SettingsFragment;
import ro.keravnos.eddie.silence.Fragment.MapFragment;
import ro.keravnos.eddie.silence.Fragment.LocationsFragment;

public class MainActivity extends AppCompatActivity
{
    BottomNavigationView bottomNavigationView;

    private ViewPager viewPager;

    LocationsFragment locationsFragment;
    MapFragment mapFragment;
    SettingsFragment settingsFragment;
    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        viewPager = findViewById(R.id.viewpager);
        //BottomNavigationView navigation =  findViewById(R.id.bottom_navigation);
        //Integer indexItem = 1;
        //navigation.getMenu().getItem(indexItem).setChecked(true);

       bottomNavigationView = findViewById(R.id.bottom_navigation);
       bottomNavigationView.getMenu().getItem(1).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.locations:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.map:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.settings:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1)
            {

            }

            @Override
            public void onPageSelected(int i)
            {
                if(prevMenuItem != null)
                {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(i).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i)
            {

            }
        });
        setupViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        locationsFragment = new LocationsFragment();
        mapFragment = new MapFragment();
        settingsFragment = new SettingsFragment();
        adapter.addFragment(locationsFragment);
        adapter.addFragment(mapFragment);
        adapter.addFragment(settingsFragment);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }
}
