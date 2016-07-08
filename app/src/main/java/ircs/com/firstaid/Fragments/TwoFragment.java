package ircs.com.firstaid.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ircs.com.firstaid.GPSTracker;
import ircs.com.firstaid.R;

/**
 * Created by Rishabh on 03-07-2016.
 */


public class TwoFragment extends Fragment {
    GPSTracker gps;
    private boolean fired=false;
    Button b;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().
                getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        FloatingActionButton fab = (FloatingActionButton)view. findViewById(R.id.fab);
        b= (Button) view.findViewById(R.id.contact_add);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // create class object
                    gps = new GPSTracker(getActivity());

                    // check if GPS enabled
                    if(gps.canGetLocation()){
                        fired=true;
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:112"));
                        startActivity(callIntent);
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        // \n is for new line
                        Toast.makeText(getActivity().getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    }
                    else{
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }

                }
            });
        }
        return  view;
    }


    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        // String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            //if (TelephonyManager.CALL_STATE_RINGING == state) {
            // refresh ringing
            //Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            //  }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state && fired) {
                // active

                //Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and refresh call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                //Log.i(LOG_TAG, "IDLE");
                if (isPhoneCalling && fired) {

                    //Log.i(LOG_TAG, "restart app");

                    // restart app
                    Intent i = getActivity().getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getActivity().getBaseContext().getPackageName());
                    getActivity().finish();
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    fired=false;
                    isPhoneCalling = false;
                }

            }
        }
    }

}