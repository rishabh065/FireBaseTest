//Launcher activity of the application.
//Displays splash and disclaimer
//Checks for first run of the application
package org.indianredcross.firstaid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
    private static int SPLASH_TIME_OUT = 2000;
    SharedPreferences check;

    private static final String SHARED_PREFS_NAME = "EXECUTION_CHECK";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        TextView t= (TextView) findViewById(R.id.splash_text);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/league-spartan.bold.ttf");
        t.setTypeface(custom_font);
        check=getSharedPreferences(SHARED_PREFS_NAME,0);
        int first=check.getInt("Determine",0);
//       On first run.
        if(first==0)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            View v=getLayoutInflater().inflate(R.layout.copyright,null);
            TextView tv= (TextView) v.findViewById(R.id.disclaimer_text);
            tv.setText("The first aid instructions and information enclosed in the application is for the Indian market only. \n" +
                    "The health-related information in this application is meant for basic informational purposes only. The information is not intended as a substitute for professional medical advice, emergency treatment or formal first-aid training. Don't use this information to diagnose or develop a treatment plan for a health problem or disease without consulting a qualified health care provider. If you're in a life-threatening or emergency medical situation, always seek medical assistance immediately. \n" +
                    "Due to the nature of software applications, St John Ambulance India and the Indian Red Cross Society cannot guarantee the information in this application will be constantly available, or available at all; or all the information in this application is complete, true, accurate, up-to-date, or non-misleading. \n" +
                    "All care has been taken by Indian Red Cross Society and St John Ambulance India with the preparation of the information, but the organisations take no responsibility for its use by other parties or individuals.\n");
            dialog.setView(v);
            dialog.setCancelable(false);
            dialog.setTitle("DISCLAIMER");
//            If user agrees to disclaimer launch activity.
            dialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    dialog.dismiss();
                    SharedPreferences.Editor editor;
                    editor= check.edit();
                    editor.putInt("Determine",1);
                    editor.commit();
                    Intent i = new Intent(MainActivity.this, FirstActivity.class);
                    startActivity(i);
                }
            });
//            Terminate the application if the user disagrees.
            dialog.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    dialog.dismiss();
                    SharedPreferences.Editor editor;
                    editor= check.edit();
                    editor.putInt("Determine",0);
                    editor.commit();
                    finishAffinity();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });
            AlertDialog dialog12 = dialog.create();
            dialog12.show();
        }
//        On runs when the user has agreed to the disclaimer.
        else if(first==2||first==1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your main activity
                    Intent i = new Intent(MainActivity.this, FirstActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
