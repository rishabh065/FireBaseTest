package bppc.com.firebasetest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import bppc.com.firebasetest.Data.Pojo;

public class FirstActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    ArrayList<Pojo> Category = new ArrayList<>();
    Firebase ref, ref1;
    private GoogleApiClient client;
    RecyclerView recyclerView;
    CategoryAdapter adapter1;
    RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onResume();
        if (savedInstanceState == null)
        {
            if (!Firebase.getDefaultConfig().isPersistenceEnabled())
                Firebase.getDefaultConfig().setPersistenceEnabled(true);
            Firebase.setAndroidContext(this);
            setContentView(R.layout.activity_first);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            ref = new Firebase("https://project-2858820461191950748.firebaseio.com/");
            ref.keepSynced(true);
            adapter1 = new CategoryAdapter(this, Category);
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mLayoutManager = new GridLayoutManager(FirstActivity.this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            ref.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Category.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        final Pojo pojo = new Pojo();
                        pojo.setValue(data.getKey());

                        String string = "https://project-2858820461191950748.firebaseio.com/" + data.getKey() + "/url";
//                    System.out.println(string);
                        ref1 = new Firebase(string);
                        ref1.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Pojo pojo1 = new Pojo();
                                pojo1.setValue(pojo.getValue());
                                System.out.println(pojo1.getValue());
                                pojo1.setUrl((String) dataSnapshot.getValue());
//                            System.out.println(pojo1.getUrl());
                                Category.add(pojo1);
                                adapter1.notifyDataSetChanged();
                                recyclerView.setAdapter(adapter1);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }

                        });
//                    System.out.println(iurl);
//                    String s="https://svbtleusercontent.com/tylerhayes_24609708604080_small.png";
//                    pojo.setUrl(iurl);
//                    System.out.println(Category.size());

//                    Category.add(pojo);
                    }
                /*adapter1.notifyDataSetChanged();
                recyclerView.setAdapter(adapter1);*/

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

//Bhuvan
            PhoneCallListener phoneListener = new PhoneCallListener();
            TelephonyManager telephonyManager = (TelephonyManager) this
                    .getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            assert fab != null;
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:112"));
                    startActivity(callIntent);
                }
            });


            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        }
    }

//    private void getUpdates(DataSnapshot ds ){
////        System.out.println("awesome");
//        Category.clear();
//        for (DataSnapshot data : ds.getChildren()) {
////            Pojo pojo = new Pojo();
//            Category.add(data.getKey());
//            System.out.println(data.getKey());
//
//        }
//        if(Category.size()>0)
//        {
//            adapter1.notifyDataSetChanged();
//            recyclerView.setAdapter(adapter1);
//        }
//        else Toast.makeText(this,"No Data", Toast.LENGTH_LONG).show();
//    }
//    public void refresh()
//    {
//        ref.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                getUpdates(dataSnapshot);
//                System.out.println("awesome");
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                getUpdates(dataSnapshot);
//                System.out.println("awesome2");
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                getUpdates(dataSnapshot);
//                System.out.println("awesome3");
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                getUpdates(dataSnapshot);
//                System.out.println("awesome4");
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//      }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_first, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "First Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://bppc.com.firebasetest/http/host/path")
//        );
//
//        AppIndex.AppIndexApi.start(client, viewAction);
//
//
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "First Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://bppc.com.firebasetest/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }


        private class PhoneCallListener extends PhoneStateListener {

            private boolean isPhoneCalling = false;

            // String LOG_TAG = "LOGGING 123";

            @Override
            public void onCallStateChanged(int state, String incomingNumber) {

                //if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                //Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
                //  }

                if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                    // active
                    finish();
                    //Log.i(LOG_TAG, "OFFHOOK");

                    isPhoneCalling = true;
                }

                if (TelephonyManager.CALL_STATE_IDLE == state) {
                    // run when class initial and phone call ended,
                    // need detect flag from CALL_STATE_OFFHOOK
                    //Log.i(LOG_TAG, "IDLE");

                    if (isPhoneCalling) {

                        //Log.i(LOG_TAG, "restart app");

                        // restart app
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(
                                        getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                        isPhoneCalling = false;
                    }

                }
            }
        }
    }