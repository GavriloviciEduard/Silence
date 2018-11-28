package ro.keravnos.eddie.silence.Model;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import ro.keravnos.eddie.silence.Fragment.*;

public class ViewPagerAdapter extends FragmentPagerAdapter
{
    private final List<Fragment> mFragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager)
    {
        super(manager);
    }

    @Override
    public Fragment getItem(int i)
    {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount()
    {
        return mFragmentList.size();
    }

    private void addFragment( Fragment fragment )
    {
        mFragmentList.add(fragment);
    }

    static void setupViewPager( Context context, ViewPager viewPager, ViewPagerAdapter adpt )
    {
        LocationsFragment locationsFragment = new LocationsFragment();
        MapFragment mapFragment = new MapFragment(context);
        SettingsFragment settingsFragment = new SettingsFragment();
        adpt.addFragment(locationsFragment);
        adpt.addFragment(mapFragment);
        adpt.addFragment(settingsFragment);
        viewPager.setAdapter(adpt);
        viewPager.setCurrentItem(1);
    }
}

