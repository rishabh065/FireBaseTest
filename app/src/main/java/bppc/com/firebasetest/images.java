package bppc.com.firebasetest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class images extends Service {
    Context c;
    public images() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase ref=new Firebase("https://project-7104573469224225532.firebaseio.com/");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    final String category="https://project-7104573469224225532.firebaseio.com/"+data.getKey();
                    final String url="https://project-7104573469224225532.firebaseio.com/"+data.getKey()+"/img";
//                    System.out.println(url);
                    Firebase ref1=new Firebase(url);
                    ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot data : dataSnapshot.getChildren())
                            {

                                if(data.getValue().toString().equals("true"))
                                {
                                    System.out.println("yo");
                                    String imgurl=
                                            category+"/Steps/"+data.getKey();
                                    System.out.println(imgurl);
                                    Firebase ref3=new Firebase(imgurl);
                                    ref3.child("url").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            System.out.println(dataSnapshot.getValue());
                                            Glide.with(images.this)
                                                    .load(dataSnapshot.getValue())
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL);
//                                            try {
//                                                drawableFromUrl(dataSnapshot.getValue().toString());
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                            }
                                            Toast.makeText(images.this, dataSnapshot.getValue().toString(), Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });
                                }
                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
//    public static Drawable drawableFromUrl(String url) throws IOException {
//        Bitmap x;
//
//        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
//        connection.connect();
//        InputStream input = connection.getInputStream();
//
//        x = BitmapFactory.decodeStream(input);
//        return new BitmapDrawable(Resources.getSystem(),x);
//    }
}
