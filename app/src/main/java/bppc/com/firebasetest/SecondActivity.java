package bppc.com.firebasetest;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import bppc.com.firebasetest.Data.Pojoinst;

public class SecondActivity extends AppCompatActivity {

    Firebase ref;
    String category;
    TextView pressed;
    ImageView img;
    ListView listView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Firebase.getDefaultConfig().isPersistenceEnabled())
            Firebase.getDefaultConfig().setPersistenceEnabled(true);
        Firebase.setAndroidContext(this);
        category = getIntent().getStringExtra("category");
        String url = "https://project-2858820461191950748.firebaseio.com/" + category;
        ref = new Firebase(url);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pressed = (TextView) findViewById(R.id.textView);
        assert pressed != null;
        pressed.setText(category);
        listView = (ListView) findViewById(R.id.listView);
        FirebaseListAdapter<Pojoinst> adapter =
                new FirebaseListAdapter<Pojoinst>(
                        this,
                        Pojoinst.class,
                        android.R.layout.simple_list_item_1,
                        ref
                ) {
                    @Override
                    protected void populateView(View view, Pojoinst p, int i) {
                        if (!p.getStep().contains("https")) {

                            TextView text = (TextView) view.findViewById(android.R.id.text1);
                            text.setText(p.getStep());
                        }


                        //System.out.println(p.getValue());
                    }
                };

        listView.setAdapter(adapter);
        img = (ImageView) findViewById(R.id.batman);
        String s = "https://svbtleusercontent.com/tylerhayes_24609708604080_small.png";
        Glide.with(SecondActivity.this).load(s).into(img);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Second Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://bppc.com.firebasetest/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
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
                Uri.parse("android-app://bppc.com.firebasetest/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
