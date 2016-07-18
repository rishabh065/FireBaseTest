package ircs.com.firstaid;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class images extends Service {
    ProgressDialog dialog;
    ArrayList<String> url_list=new ArrayList<>();
    public images() {

    }

    @Override
    public void onCreate() {

        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase ref=new Firebase("https://project-7104573469224225532.firebaseio.com/");
        final Context c=images.this;
        dialog = new ProgressDialog(c);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);
        dialog.setCancelable(false);
        dialog.setMessage("Fetching Images..");
        dialog.isIndeterminate();
        dialog.show();
        ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    final String category=data.getKey();
                    final String categoryurl="https://project-7104573469224225532.firebaseio.com/"+data.getKey();
                    final String url="https://project-7104573469224225532.firebaseio.com/"+data.getKey()+"/img";
                    Firebase ref1=new Firebase(url);
                    ref1.addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for(DataSnapshot data : dataSnapshot.getChildren())
                            {

                                if(data.getValue().toString().equals("true"))
                                {
                                    final String num=data.getKey();
                                    String imgurl=categoryurl+"/Steps/"+data.getKey();
                                    System.out.println(imgurl);
                                    Firebase ref3=new Firebase(imgurl);
                                    ref3.child("url").addListenerForSingleValueEvent(new ValueEventListener()
                                    {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            System.out.println(dataSnapshot.getValue());
                                            images.this.url_list.add(dataSnapshot.getValue().toString());
                                            Glide
                                                    .with(getApplicationContext())
                                                    .load(dataSnapshot.getValue())
                                                    .asBitmap()
                                                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL) {
                                                        @Override
                                                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                                            storeImage(resource,category+num,category);
                                                        }
                                                    });
//                                            Toast.makeText(images.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {}
                                    });

                                }
                            }
                            System.out.println("mysizeisyo"+url_list.size());
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {}
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        System.out.println("mysizeis"+url_list.size());

        //Toast.makeText(images.this, "stop", Toast.LENGTH_SHORT).show();

    }
    private void storeImage(Bitmap image,String name,String cate) {
        File pictureFile = getOutputMediaFile(name);
        System.out.println(cate);
        if(cate.equals("zzzzzz"))
            stopService();
        if (pictureFile == null) {
            Log.d("file",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("file", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("file", "Error accessing file: " + e.getMessage());
        }
    }
    private  File getOutputMediaFile(String name){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        System.out.println("Hello1");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        System.out.println("Hello");
        // Create a media file name
        File mediaFile;
        String mImageName=name+".png";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        System.out.println(mediaFile.getPath());
        return mediaFile;
    }
    public void stopService()
    {
        dialog.dismiss();
        stopSelf();
    }


    @Override
        public IBinder onBind (Intent intent){
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }

}
