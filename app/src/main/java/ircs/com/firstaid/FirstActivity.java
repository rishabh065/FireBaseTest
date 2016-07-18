package ircs.com.firstaid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ircs.com.firstaid.Fragments.FourFragment;
import ircs.com.firstaid.Fragments.OneFragment;
import ircs.com.firstaid.Fragments.ThreeFragment;
import ircs.com.firstaid.Fragments.TwoFragment;

public class FirstActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    ViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static final int PICK_CONTACT = 1234;
    private LocationManager locationManager;
    boolean canGetLocation=false;
    boolean fired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_first);


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
           // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
              getSupportActionBar().setTitle("First Aid");
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "EMERGENCY");
        adapter.addFragment(new TwoFragment(), "CALL");
        adapter.addFragment(new ThreeFragment(), "HOSPITAL");
        adapter.addFragment(new FourFragment(),"ABOUT US");
        viewPager.setAdapter(adapter);
    }
    public void start_map(View view)
    {
        canGetLocation = false;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LOCATION_SERVICE);
        // getting network status
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled) {
            showSettingsAlert();
        }
        else canGetLocation = true;
        if (canGetLocation) {
            Toast.makeText(FirstActivity.this, "Starting Maps..", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, MapsActivity.class);
            startActivity(i);
        }
        else Snackbar.make(view,"Enable GPS",Snackbar.LENGTH_LONG);
    }
    public void contact_call(View view)
    {
        View v;
        v= (View) view.getParent();
        RelativeLayout rl= (RelativeLayout) v.getParent();
        TextView tv= (TextView) rl.findViewById(R.id.contact_num);
        fired=true;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+tv.getText()));
        startActivity(callIntent);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(adapter.getItem(1).getTag());
        try {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        catch(Exception e){}
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
        // Showing Alert Message
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
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    finish();
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    fired=false;
                    isPhoneCalling = false;
                }

            }
        }
    }
}
