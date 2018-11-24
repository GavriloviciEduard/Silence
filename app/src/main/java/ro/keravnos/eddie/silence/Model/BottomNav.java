package ro.keravnos.eddie.silence.Model;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;


import ro.keravnos.eddie.silence.R;

public class BottomNav
{

    private MenuItem prevMenuItem;

    public  void create( ViewPagerAdapter v, final BottomNavigationView bottomNavigationView, final ViewPager viewPager  )
    {

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

        ViewPagerAdapter.setupViewPager(viewPager,v);

    }
}
