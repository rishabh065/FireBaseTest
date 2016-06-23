package bppc.com.firebasetest;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;

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
//        listView = (ListView) findViewById(R.id.listView);
        recyclerView = (RecyclerView) findViewById(R.id.step_disp);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
//        FirebaseListAdapter<Pojoinst> adapter =
//                new FirebaseListAdapter<Pojoinst>(
//                        this,
//                        Pojoinst.class,
//                        android.R.layout.simple_list_item_1,
//                        ref
//                ) {
//                    @Override
//                    protected void populateView(View view, Pojoinst p, int i) {
//                        if (!p.getStep().contains("https")) {
//
//                            TextView text = (TextView) view.findViewById(android.R.id.text1);
//                            text.setText(p.getStep());
//                        }
//                        else {
//
//                            TextView text = (TextView) view.findViewById(android.R.id.text1);
//                            text.setText("");
//                        }
//
//
//                        //System.out.println(p.getValue());
//                    }
//                };
        FirebaseRecyclerAdapter<Boolean,StepHolder> adapter = new FirebaseRecyclerAdapter<Boolean, StepHolder>(
                Boolean.class, android.R.layout.two_line_list_item, StepHolder.class, ref.child("img")){
            protected void populateViewHolder(StepHolder viewHold, Boolean model, int position) {
                String key = this.getRef(position).getKey();
                System.out.println(key);
                final boolean val=model;
                final StepHolder sh=viewHold;
                ref.child("Steps").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("text").getValue(String.class);
                        sh.setInst(name);
                        if(val) {
                            String iurl = dataSnapshot.child("url").getValue(String.class);
                            sh.setUrl(iurl);
                        }

                    }

                    public void onCancelled(FirebaseError firebaseError) { }
                });
            }
        };

        recyclerView.setAdapter(adapter);
//        img = (ImageView) findViewById(R.id.batman);
//        String s = "https://svbtleusercontent.com/tylerhayes_24609708604080_small.png";
//        Glide.with(SecondActivity.this).load(s).into(img);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

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
            TextView field = (TextView) mView.findViewById(android.R.id.text1);
            field.setText(name);
        }

        public void setUrl(String text) {
            TextView field = (TextView) mView.findViewById(android.R.id.text2);
            field.setText(text);
        }
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
