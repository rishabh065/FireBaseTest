package ircs.com.firstaid.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ircs.com.firstaid.Data.Contacts;
import ircs.com.firstaid.Data.CustomAdapter;
import ircs.com.firstaid.R;

/**
 * Created by Rishabh on 03-07-2016.
 */


public class TwoFragment extends Fragment {
    private boolean fired=false;
    Button contact_add;
    ArrayList<Contacts> contacts=new ArrayList<>();
    CustomAdapter adapter;
    ListView listView;
    SharedPreferences list_contents;
    private static final int PICK_CONTACT = 1234;
    private static final String SHARED_PREFS_NAME = "MY_SHARED_PREF";
    private final int call_code=1;
    private final int contact_code=4;
    private final int state_code=5;
    Intent callIntent;
    boolean other=false;
    Intent intent;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().
                getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        list_contents = getActivity().getSharedPreferences(SHARED_PREFS_NAME, 0);
        int num=list_contents.getInt("Number",0);
        for(int i=1;i<=num;i++)
        {
            String name=list_contents.getString("name"+i,null);
            String number=list_contents.getString("num"+i,null);
            Contacts c=new Contacts();
            c.setNumber(number);
            c.setName(name);
            contacts.add(c);
        }
        adapter=new CustomAdapter(getActivity(),contacts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        FloatingActionButton fab = (FloatingActionButton)view. findViewById(R.id.fab);
        contact_add= (Button) view.findViewById(R.id.contact_add);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:112"));
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CALL_PHONE},
                                call_code);
                    }
                    else startActivity(callIntent);
//                    if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
//                            == PackageManager.PERMISSION_GRANTED && other ){
//                        startActivity(callIntent);
//                    }

                }
            });
        }
        listView= (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        contact_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                if(contacts.size()>=3)
                    Toast.makeText(getActivity().getApplicationContext(), "You are allowed to set three favourites.",
                            Toast.LENGTH_LONG).show();
                else {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                            != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_CONTACTS},
                                contact_code);
                    }
                    else getActivity().startActivityForResult(intent, PICK_CONTACT);
                    }
                }
            });
        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case call_code:
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED ) {
//                    if (other) {
                        fired = true;
                        startActivity(callIntent);
//                    }
//                    else phone_state();
                }
            }
            case contact_code:
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED ) {
                    getActivity().startActivityForResult(intent, PICK_CONTACT);
                }
                else {
                    not_granted("Please grant location permissions to find nearby hospitals");
                }
            }
            case state_code:
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED )
                {
                    other=true;
                    startActivity(callIntent);
                }
                else not_granted("Please grant state permissions to make calls");
            }
        }
    }


    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        // String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_OFFHOOK == state && fired) {
                // active
                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and refresh call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                //Log.i(LOG_TAG, "IDLE");
                if (isPhoneCalling && fired) {
                    // restart app
                    Intent i = getActivity().getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getActivity().getBaseContext().getPackageName());
                    getActivity().finish();
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    fired=false;
                    isPhoneCalling = false;
                }

            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT) {
                if (resultCode == getActivity().RESULT_OK) {
                    Uri contactData = data.getData();
                    String number = "";
                    String name = "";
                    Cursor cursor = getActivity().getContentResolver().query(contactData, null, null, null, null);
                    cursor.moveToFirst();
                    String hasPhone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    if (hasPhone.equals("1")) {
                        Cursor phones = getActivity().getContentResolver().query
                                (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                + " = " + contactId, null, null);
                        while (phones.moveToNext()) {
                            number = phones.getString(phones.getColumnIndex
                                    (ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[-() ]", "");
                            name = phones.getString(phones.getColumnIndex
                                    (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        }
                        phones.close();
                        Toast.makeText(getActivity().getApplicationContext(), "Contact Added", Toast.LENGTH_SHORT).show();
                        Contacts c = new Contacts();
                        c.setName(name);
                        c.setNumber(number);
                        contacts.add(c);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "This contact has no phone number", Toast.LENGTH_SHORT)
                                .show();
                    }
                    cursor.close();
                }
            }
    }

    @Override
    public void onStop() {
        System.out.println("OnStop");
        SharedPreferences.Editor editor;
        for(int i=1;i<=contacts.size();i++)
        {
            editor= list_contents.edit();
            editor.putString("name"+i,contacts.get(i-1).getName());
            editor.commit();
            editor.putString("num"+i,contacts.get(i-1).getNumber());
            editor.commit();
        }
        editor= list_contents.edit();
        editor.putInt("Number",contacts.size());
        editor.commit();
        super.onStop();
    }
    public void not_granted(String message)    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle("Permission Unavailable");
        dialog.setMessage(message);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void phone_state()
    {
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED)
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_PHONE_STATE},
                state_code);
    }
}

