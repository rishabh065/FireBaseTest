//Fragment for finding nearby hospitals.
package org.indianredcross.firstaid.Fragments;

/**
 * Created by RIshabh on 03-07-2016.
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.indianredcross.firstaid.R;


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
        final View view = inflater.inflate(R.layout.fragment_three, container, false);
        return view;
    }
}