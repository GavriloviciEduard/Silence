package ro.keravnos.eddie.silence.Fragment;


import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


import ro.keravnos.eddie.silence.Model.Notifications;
import ro.keravnos.eddie.silence.R;



public class SettingsFragment extends Fragment
{

    public SettingsFragment()
    {

    }

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        return  inflater.inflate(R.layout.fragment_settings,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        if(view != null)
        {
            final Switch sw = view.findViewById(R.id.notifications_switch);

            //LAST STATE OF SWITCH
            SharedPreferences settings = getActivity().getSharedPreferences("ro.keravnos.eddie.silence", 0);//SETTINGS SWITCH
            boolean switchStateSET = settings.getBoolean("switchkeySETTINGS", false);//SETTINGS SWITCH


            if (switchStateSET)//SETTINGS SWITCH
            {
                sw.setChecked(true);
                Notifications notf = new Notifications(getActivity().getApplicationContext());
                notf.createNotificationChannel();
                notf.create();
            }

            //LAST STATE OF SWITCH

            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()//LISTENER FOR SETTINGS SWITCH
            {



                @Override
                public void onCheckedChanged( CompoundButton buttonView, boolean isChecked)
                {
                    if(sw.isChecked())
                    {
                        Notifications notf = new Notifications(getActivity().getApplicationContext());
                        notf.createNotificationChannel();
                        notf.create();

                    }

                    else
                    {
                        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.cancelAll();
                    }

                    SharedPreferences settings = getActivity().getSharedPreferences("ro.keravnos.eddie.silence", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("switchkeySETTINGS", isChecked);
                    editor.commit();


                }

            });

        }
    }




}
