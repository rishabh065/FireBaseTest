package ircs.com.firstaid.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
    Button b;
    TextView t;
    ArrayList<Contacts> contacts=new ArrayList<>();
    CustomAdapter adapter;
    ListView listView;
    SharedPreferences list_contents;
    private static final int PICK_CONTACT = 1234;
    private static final String SHARED_PREFS_NAME = "MY_SHARED_PREF";

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
        b= (Button) view.findViewById(R.id.contact_add);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                        fired=true;
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:112"));
                        startActivity(callIntent);

                }
            });
        }
        listView= (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        Button b= (Button) view.findViewById(R.id.contact_add);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contacts.size()>=3)
                    Toast.makeText(getActivity().getApplicationContext(), "You are allowed to set three favourites.",
                            Toast.LENGTH_LONG).show();
                else {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    getActivity().startActivityForResult(intent, PICK_CONTACT);
                }
            }
        });
        return view;
    }


    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        // String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            //if (TelephonyManager.CALL_STATE_RINGING == state) {
            // refresh ringing
            //Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            //  }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state && fired) {
                // active

                //Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and refresh call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                //Log.i(LOG_TAG, "IDLE");
                if (isPhoneCalling && fired) {

                    //Log.i(LOG_TAG, "restart app");

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
}

