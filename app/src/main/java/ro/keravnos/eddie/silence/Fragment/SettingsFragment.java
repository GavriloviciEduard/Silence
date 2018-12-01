package ro.keravnos.eddie.silence.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


import java.util.EventListener;
import java.util.Objects;

import ro.keravnos.eddie.silence.Model.BackgroundService;
import ro.keravnos.eddie.silence.Model.Notifications;
import ro.keravnos.eddie.silence.R;

import ro.keravnos.eddie.silence.Model.MapTypeH;


public class SettingsFragment extends Fragment
{
    MapTypeH M;

    boolean notifStarted = false;
    boolean backStarted = false;

    public SettingsFragment()
    {

    }

    @SuppressLint("ValidFragment")
    public SettingsFragment( MapTypeH M)
    {
        this.M = M;
    }

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        return  inflater.inflate(R.layout.fragment_settings,container,false);

    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        if(view != null)
        {

            final Switch sw = view.findViewById(R.id.notifications_switch);

            //LAST STATE OF SWITCH
            SharedPreferences settings = Objects.requireNonNull(getActivity()).getSharedPreferences("ro.keravnos.eddie.silence", 0);//SETTINGS SWITCH
            boolean switchStateSET = settings.getBoolean("switchkeySETTINGS", false);//SETTINGS SWITCH
            boolean switchStateSET3 = settings.getBoolean("switchBackground",false);


            if (switchStateSET && switchStateSET3)//SETTINGS SWITCH
            {
                sw.setChecked(true);
                Notifications notf = new Notifications(getActivity().getApplicationContext());
                notf.createNotificationChannel();
                notf.create();
            }

            //LAST STATE OF SWITCH

            //LISTENER FOR SETTINGS SWITCH
            sw.setOnCheckedChangeListener(( buttonView, isChecked ) -> {
                if(sw.isChecked())
                {
                    //Notifications notf = new Notifications(getActivity().getApplicationContext());
                    //notf.createNotificationChannel();
                    //notf.create();
                    if(!notifStarted)
                    {
                        Intent notification = new Intent(getActivity(),Notifications.class);
                        getActivity().startService(notification);
                        notifStarted = true;
                    }
                }

                else
                {
                    NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.cancelAll();

                    if(notifStarted)
                    {
                        Intent notification = new Intent(getActivity(),Notifications.class);
                        getActivity().stopService(notification);
                        notifStarted = false;
                    }
                }

                SharedPreferences settings1 = getActivity().getSharedPreferences("ro.keravnos.eddie.silence", 0);
                SharedPreferences.Editor editor = settings1.edit();
                editor.putBoolean("switchkeySETTINGS", isChecked);
                editor.commit();


            });

            final Switch sw2 = view.findViewById(R.id.maptype);
            SharedPreferences settings2 = getActivity().getSharedPreferences("ro.keravnos.eddie.silence", 0);
            boolean switchStateSET2 = settings2.getBoolean("switchkeySETTINGS2", false);

            if (switchStateSET2)//SETTINGS SWITCH
            {
                sw2.setChecked(true);
                Notifications notf = new Notifications(getActivity().getApplicationContext());
                notf.createNotificationChannel();
                notf.create();
            }


            sw2.setOnCheckedChangeListener(( buttonView, isChecked ) -> {
                M.setType(sw2.isChecked());

                SharedPreferences settings12 = getActivity().getSharedPreferences("ro.keravnos.eddie.silence", 0);
                SharedPreferences.Editor editor = settings12.edit();
                editor.putBoolean("switchkeySETTINGS2", isChecked);
                editor.commit();
            });

            final Switch sw3 = view.findViewById(R.id.background);


            if(switchStateSET3)
            {
                sw3.setChecked(true);
            }
            else
            {
                sw3.setChecked(false);
                sw.setChecked(false);
                sw.setEnabled(false);
            }

            sw3.setOnCheckedChangeListener(( buttonView, isChecked ) -> {
                if(sw3.isChecked())
                {
                    sw.setEnabled(true);

                    if(!backStarted)
                    {
                        Intent background = new Intent(getActivity(),BackgroundService.class);
                        getActivity().startService(background);
                        backStarted = true;
                    }
                }
                else
                {
                    sw.setChecked(false);
                    sw.setEnabled(false);

                    if(backStarted)
                    {
                        Intent background = new Intent(getActivity(),BackgroundService.class);
                        getActivity().stopService(background);
                        backStarted = false;
                    }
                }

                SharedPreferences s = getActivity().getSharedPreferences("ro.keravnos.eddie.silence", 0);
                SharedPreferences.Editor editor = s.edit();
                editor.putBoolean("switchBackground",isChecked);
                editor.commit();
            });

        }
    }




}
