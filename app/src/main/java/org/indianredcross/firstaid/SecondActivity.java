//Displaying steps for various emergencies after user clicks on one.
package org.indianredcross.firstaid;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;

public class SecondActivity extends AppCompatActivity {

    Firebase ref;
    String category;
    static Context c;
    static DisplayMetrics metrics = new DisplayMetrics();

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onResume();
        try {
            if (!Firebase.getDefaultConfig().isPersistenceEnabled())
                Firebase.getDefaultConfig().setPersistenceEnabled(true);
            Firebase.setAndroidContext(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        category = getIntent().getStringExtra("category");
        setTitle(category);
        String url = "https://project-7104573469224225532.firebaseio.com/" + category;
        ref = new Firebase(url);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        listView = (ListView) findViewById(R.id.listView);
        recyclerView = (RecyclerView) findViewById(R.id.step_disp);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        FirebaseRecyclerAdapter<Boolean,StepHolder> adapter = new FirebaseRecyclerAdapter<Boolean, StepHolder>(
                Boolean.class, R.layout.step_layout, StepHolder.class, ref.child("img")){
            protected void populateViewHolder(StepHolder viewHold, Boolean model, final int position) {
                final String key = this.getRef(position).getKey();
                //System.out.println(key);
                final boolean val=model;
                final StepHolder sh=viewHold;
                ref.child("Steps").child(key).addValueEventListener(new ValueEventListener() {

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("text").getValue(String.class);
                        sh.setInst(name);
                        sh.setNum(key);
                        if(val) {
                            String iurl = dataSnapshot.child("url").getValue(String.class);
                            sh.setImg(category,key,iurl);
                            System.out.println(category+key);
                        }

                    }

                    public void onCancelled(FirebaseError firebaseError) { }
                });
                sh.setIsRecyclable(false);
            }
        };
        c= SecondActivity.this;
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerView.smoothScrollToPosition(0);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    public static class StepHolder extends RecyclerView.ViewHolder {
        View mView;

        public StepHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setInst(String name) {
            TextView field = (TextView) mView.findViewById(R.id.step_txt);
            field.setText(name);
        }

        public void setImg(String category,String num,String url) {
            ImageView img = (ImageView) mView.findViewById(R.id.step_url);
            double width=metrics.widthPixels/1.25;
            img.setMaxWidth((int)width);
//            try {
                File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                        + "/Android/data/"
                        +c.getPackageName()
                        + "/Files");
                File f=new File(mediaStorageDir.getPath()+File.separator +category+num+".png");
                Glide.with(c).
                        load(f).
                        asBitmap().
                        diskCacheStrategy(DiskCacheStrategy.NONE).
                        into(img);


        }
        public void setNum(String num)
        {
            TextView field = (TextView) mView.findViewById(R.id.step_num);
            field.setText(num);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction2 = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Second Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://org.indianredcross.firstaid/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction2);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Second Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://org.indianredcross.firstaid/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

}