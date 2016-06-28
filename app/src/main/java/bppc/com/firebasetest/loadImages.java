package bppc.com.firebasetest;

import android.content.Context;
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
import java.util.ArrayList;

import bppc.com.firebasetest.Data.Step_Image;

/**
 * Created by rishabh on 6/29/2016.
 */
public class LoadImages extends AsyncTask<Void,Void,String>
{
    Context c;
    final ArrayList<Step_Image> arrayList= new ArrayList<>();


    public LoadImages(Context c) {
        this.c = c;
    }
    public void downloadImg()
    {
       for (int i=0;i<arrayList.size();i++)
        {
            try {
                URL url = new URL(arrayList.get(i).getUrl());
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                System.out.println("continue");
                File directory = c.getDir("images", Context.MODE_PRIVATE);

                if (directory.exists()) {
                    System.out.println(directory.getPath());
                }
                File myPath = new File(directory, arrayList.get(i).getName() + ".jpg");
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                byte[] data = new byte[1024];
                int count = 0;
                OutputStream outputStream = new FileOutputStream(myPath);
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                }
                inputStream.close();
                outputStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        if (!Firebase.getDefaultConfig().isPersistenceEnabled())
            Firebase.getDefaultConfig().setPersistenceEnabled(true);
        Firebase.setAndroidContext(c);

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
                                            Step_Image si=new Step_Image();
                                            si.setUrl(dataSnapshot.getValue().toString());
                                            si.setName(category+step);
                                            arrayList.add(si);
                                            System.out.println(si.getUrl());
                                            System.out.println(si.getName());
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
        downloadImg();
        return "Image Loaded";
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println(result);
    }
}

