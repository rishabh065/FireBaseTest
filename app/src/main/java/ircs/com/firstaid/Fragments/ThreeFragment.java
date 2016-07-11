package ircs.com.firstaid.Fragments;

/**
 * Created by Tushar on 03-07-2016.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ircs.com.firstaid.MapsActivity;
import ircs.com.firstaid.R;


public class ThreeFragment extends Fragment{
    private final String TAG = getClass().getSimpleName();

    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        FloatingActionButton fab = (FloatingActionButton)view. findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), MapsActivity.class);
                getActivity().startActivity(i);
            }
        });
        return view;
    }
}