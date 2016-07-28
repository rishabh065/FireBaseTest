//Driver class of the application
//Creates all the fragments
//Has on click listeners
//The UI of the application and basic functionality is governed by this activity.
package org.indianredcross.firstaid;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
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

import org.indianredcross.firstaid.Fragments.FiveFragment;
import org.indianredcross.firstaid.Fragments.FourFragment;
import org.indianredcross.firstaid.Fragments.OneFragment;
import org.indianredcross.firstaid.Fragments.ThreeFragment;
import org.indianredcross.firstaid.Fragments.TwoFragment;

import java.util.ArrayList;
import java.util.List;

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
    boolean fired=false;
    Intent callIntent;
    final private int call_code=1;
    final private int location_code=2;
    final private int PERMISSION_ALL=22;
    private final int contact_code=4;
    boolean here=false;
    SharedPreferences check;
    private static final String SHARED_PREFS_NAME = "EXECUTION_CHECK";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("First Aid");
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }
//    Adding fragments and checking for first run.
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "EMERGENCY");
        adapter.addFragment(new FiveFragment(), "INSTRUCTIONS");
        adapter.addFragment(new TwoFragment(), "CALL");
        adapter.addFragment(new ThreeFragment(), "HOSPITAL");
        adapter.addFragment(new FourFragment(), "ABOUT US");
        viewPager.setAdapter(adapter);
        check=getSharedPreferences(SHARED_PREFS_NAME,0);
        int num=check.getInt("Determine",1);
        if(num==1)
        {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(isOnline()) {
                    String[] PERMISSIONS = {Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    if (!hasPermissions(this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
                    }
                }
                else{
                    AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                    dialog.setTitle("No Internet Connection");
                    dialog.setMessage("You are required to enable internet on the first run.");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.dismiss();
                            finishAffinity();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });
                    dialog.show();
                }
            }
            else{
                if(isOnline()) {
                    Intent i = new Intent(this, images.class);
                    startService(i);
                    SharedPreferences.Editor editor;
                    editor= check.edit();
                    editor.putInt("Determine",2);
                    editor.commit();
                }
                else{
                    AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                    dialog.setTitle("No Internet Connection");
                    dialog.setMessage("You are required to enable internet on the first run.");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.dismiss();
                            finishAffinity();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });
                    dialog.show();
                }
            }
        }
    }
//    onClick listener for starting maps when finding hospitals
    public void start_map(View view) {
        canGetLocation = false;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isOnline()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setting Dialog Title
            alertDialog.setTitle("Network Unavailable");

            // Setting Dialog Message
            alertDialog.setMessage("Please connect your phone to the network.");

            // On pressing Settings button
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        } else if (!isGPSEnabled) {
            showSettingsAlert();
        } else canGetLocation = true;
        if (canGetLocation) {
            System.out.println("permission");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        location_code);
                System.out.println("permission2");
            }
            else {
                Toast.makeText(FirstActivity.this, "Starting Maps..", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, MapsActivity.class);
                startActivity(i);
            }

        } else Snackbar.make(view, "Enable GPS", Snackbar.LENGTH_LONG);
    }
//Placing call to contacts
    public void contact_call(View view) {
        View v;
        v = (View) view.getParent();
        RelativeLayout rl = (RelativeLayout) v.getParent();
        TextView tv = (TextView) rl.findViewById(R.id.contact_num);
        here=true;
        callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + tv.getText()));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    call_code);
        else{
            fired = true;
            startActivity(callIntent);
        }
    }
    public void call_emergency(View view)
    {
        callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:112"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    call_code);
        }

        else {
            fired=true;
            startActivity(callIntent);
        }
    }

//Handling permissions requests results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println(requestCode);
        switch (requestCode) {
            case call_code:
                System.out.println("inside1");
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED )
                {
                    if(here) {
                        fired = true;
                        here=false;
                        startActivity(callIntent);
                    }
                    else{
                        fired=true;
                        callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:112"));
                        startActivity(callIntent);
                    }
                }
                if(grantResults[0]==PackageManager.PERMISSION_DENIED)
                    not_granted("Please grant call permissions to make a call.");

            break;
            case location_code:
                System.out.println("inside2");
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED ) {
                    Toast.makeText(FirstActivity.this, "Starting Maps..", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(FirstActivity.this, MapsActivity.class);
                    startActivity(i);
                }
                if(grantResults[0]==PackageManager.PERMISSION_DENIED){
                    not_granted("Please grant location permissions to find nearby hospitals.");
                }
            break;
            case PERMISSION_ALL:
                System.out.println("inside3");
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if ( grantResults[permissions.length-1]==PackageManager.PERMISSION_GRANTED
                            && Settings.canDrawOverlays(this)) {
                        SharedPreferences.Editor editor;
                        editor= check.edit();
                        editor.putInt("Determine",2);
                        editor.commit();
                        // All Permissions Granted
                        Intent intent = new Intent(this, images.class);
                        startService(intent);
                    }
                    else {
                        // Permission Denied
                        Toast.makeText(FirstActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            break;
            case contact_code:
            {
                System.out.println("inside4");
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED ) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                }
                else {
                    not_granted("Please grant call permissions to call favourite contacts");
                }
            }
        }
    }
//    Setting up fragment adapter.
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
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(adapter.getItem(2).getTag());
        try {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        catch(Exception e){
            e.printStackTrace();
        }
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
//    Handling permissions denied in android M.
    public void not_granted(String message)    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle("Permission Unavailable");
        dialog.setMessage(message);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
//    Phone state listener to return back to app after placin g a call.
    private class PhoneCallListener extends PhoneStateListener {
        private boolean isPhoneCalling = false;
    private int count=0;
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (TelephonyManager.CALL_STATE_OFFHOOK == state && fired) {
                // active
//                System.out.println("frag1");
                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                count++;
                // run when class initial and refresh call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                //Log.i(LOG_TAG, "IDLE");
//                System.out.println("frag2");
                if (isPhoneCalling && fired && !here) {
                    if(count>2) {
                        // restart app
                        Intent i = new Intent(FirstActivity.this, FirstActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
//                        System.out.println(i);
                        startActivity(i);
                        fired = false;
                        isPhoneCalling = false;
                        count=0;
                    }
                }
                else if (isPhoneCalling && fired)
                {
                    // restart app
                    Intent i = new Intent(FirstActivity.this, FirstActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
//                    System.out.println("yo");
                    startActivity(i);
                    fired = false;
                    isPhoneCalling = false;
                    count=0;
                }
            }
        }
    }
//    Check if device has in internet access.
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
//    Checking for multiple permissions simultaneously.
    public  boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (int i=0;i<permissions.length;i++) {
                if(i==0)
                {
                    if(!Settings.canDrawOverlays(this))
                    {
                        startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
                    }
                }
                else{
                    if (ActivityCompat.checkSelfPermission(context, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
