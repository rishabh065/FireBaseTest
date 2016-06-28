package bppc.com.firebasetest;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by rishabh on 6/29/2016.
 */
public class LoadImages extends AsyncTask<Void,Void,Void>
{
    Context c;

    public LoadImages(Context c) {
        this.c = c;
    }

    public void saveToDisk(String path,String category,String step_num)
    {
        int file_length=0;
        try {
            URL url=new URL(path);
            URLConnection urlConnection=url.openConnection();
            urlConnection.connect();
            file_length=urlConnection.getContentLength();
            ContextWrapper cw = new ContextWrapper(c);
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File mypath=new File(directory,category+step_num+".jpg");
            InputStream inputStream= new BufferedInputStream(url.openStream(),8192);
            byte[] data=new byte[1024];
            int count=0;
            OutputStream outputStream=new FileOutputStream(mypath);
            while ((count=inputStream.read(data))!=-1)
            {
                outputStream.write(data,0,count);
            }
            inputStream.close();
            outputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected Void doInBackground(Void... params) {
        Firebase ref=new Firebase("https://project-7104573469224225532.firebaseio.com/");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    final String tapped=data.getKey();
                    final String category="https://project-7104573469224225532.firebaseio.com/"+data.getKey();
                    final String url="https://project-7104573469224225532.firebaseio.com/"+data.getKey()+"/img";
                    Firebase ref1=new Firebase(url);
                    ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            for(DataSnapshot data : dataSnapshot.getChildren())
                            {

                                if(data.getValue().toString().equals("true"))
                                {
                                    final String step=data.getValue().toString();
                                    String imgurl=category+"/Steps/"+data.getKey();
                                    Firebase ref3=new Firebase(imgurl);
                                    ref3.child("url").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            saveToDisk(dataSnapshot.getValue().toString(),tapped,step);
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
        return null;
    }
}

