package bppc.com.firebasetest;

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

import bppc.com.firebasetest.Data.Pojoinst;

public class SecondActivity extends AppCompatActivity {

    Firebase ref;
    String category;
    TextView pressed;
    ImageView img;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!Firebase.getDefaultConfig().isPersistenceEnabled())
            Firebase.getDefaultConfig().setPersistenceEnabled(true);
        Firebase.setAndroidContext(this);
        category=getIntent().getStringExtra("category");
        String url="https://project-2858820461191950748.firebaseio.com/"+category;
        ref=new Firebase(url);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pressed= (TextView) findViewById(R.id.textView);
        assert pressed != null;
        pressed.setText(category);
        listView= (ListView) findViewById(R.id.listView);
        FirebaseListAdapter<Pojoinst> adapter =
                new FirebaseListAdapter<Pojoinst>(
                        this,
                        Pojoinst.class,
                        android.R.layout.simple_list_item_1,
                        ref
                ){
                    @Override
                    protected void populateView (View view, Pojoinst p, int i){
                        if(!p.getStep().contains("https")){

                            TextView text = (TextView) view.findViewById(android.R.id.text1);
                            text.setText(p.getStep());
                        }


                        //System.out.println(p.getValue());
                    }
                } ;

        listView.setAdapter(adapter);
        img= (ImageView) findViewById(R.id.batman);
        String s="https://svbtleusercontent.com/tylerhayes_24609708604080_small.png";
        Glide.with(SecondActivity.this).load(s).into(img);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

    }

}
