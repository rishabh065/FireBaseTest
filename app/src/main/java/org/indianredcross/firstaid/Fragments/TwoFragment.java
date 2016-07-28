//Fragment for making calls to emergency number and setting favourite contacts.
package org.indianredcross.firstaid.Fragments;

import android.Manifest;
import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.indianredcross.firstaid.Data.Contacts;
import org.indianredcross.firstaid.Data.CustomAdapter;
import org.indianredcross.firstaid.R;

import java.util.ArrayList;

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
//        PhoneCallListener phoneListener = new PhoneCallListener();
//        TelephonyManager telephonyManager = (TelephonyManager) getActivity().
//                getSystemService(Context.TELEPHONY_SERVICE);
//        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        list_contents = getActivity().getSharedPreferences(SHARED_PREFS_NAME, 0);
        int num=list_contents.getInt("Number",0);
//        Filling the favourites list view from shared preferences.
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
        listView= (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
//        Listener on favourite contacts call and asking for permissions in android M.
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
//    asking for in app permissions in marshmallow
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
//            call permission
            case call_code:
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED ) {
                        fired = true;
                        startActivity(callIntent);
                }
                else {
                    not_granted("Please grant call permissions to make calls.");
                }
            }
//            Permission to access contacts
            case contact_code:
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED ) {
                    getActivity().startActivityForResult(intent, PICK_CONTACT);
                }
                else {
                    not_granted("Please grant location permissions to find nearby hospitals.");
                }
            }
//            phone state access permission
            case state_code:
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED )
                {
                    other=true;
                    startActivity(callIntent);
                }
                else not_granted("Please grant state permissions to make calls.");
            }
        }
    }

//    Returning to the app after placing a call by placing a listener.

//  Activity call back after the user selects a contact from the contact list.
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
//                        Fetching the name and data of selected contact.
                        while (phones.moveToNext()) {
                            number = phones.getString(phones.getColumnIndex
                                    (ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[-() ]", "");
                            name = phones.getString(phones.getColumnIndex
                                    (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        }
                        phones.close();
                        Toast.makeText(getActivity().getApplicationContext(), "Contact Added", Toast.LENGTH_SHORT).show();
//                        Adding data to list view and notifying the adapter.
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
//    When the fragment calls onSTop all the favourites selected by the user are committed to shared
//    preferences so that  they can be retrieved when the fragment resumes.
    @Override
    public void onStop() {
//        System.out.println("OnStop");
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
//    If user denies permission in Android M, a suitable message iis displayed.
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

