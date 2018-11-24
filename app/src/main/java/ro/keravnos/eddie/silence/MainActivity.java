package ro.keravnos.eddie.silence;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import ro.keravnos.eddie.silence.Model.BottomNav;
import ro.keravnos.eddie.silence.Model.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity
{
    BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ViewPagerAdapter v =new ViewPagerAdapter(getSupportFragmentManager());

        BottomNav  bot = new BottomNav();
        bot.create(v,bottomNavigationView,viewPager);

    }


}
