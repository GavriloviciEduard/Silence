package ro.keravnos.eddie.silence;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected( @NonNull MenuItem item ) {
            switch (item.getItemId()) {
                case R.id.locations:
                    mTextMessage.setText("CEVA1");

                    return true;
                case R.id.map:
                    mTextMessage.setText("CEVA2");

                    return true;
                case R.id.settings:
                    mTextMessage.setText("CEVA3");

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage =  findViewById(R.id.message);


        BottomNavigationView navigation =  findViewById(R.id.navigation);
        Integer indexItem = 1;
        navigation.getMenu().getItem(indexItem).setChecked(true);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
