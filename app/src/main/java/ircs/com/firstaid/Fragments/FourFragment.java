package ircs.com.firstaid.Fragments;

/**
 * Created by rishabh on 7/15/2016.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ircs.com.firstaid.R;


public class FourFragment extends Fragment{
    private final String TAG = getClass().getSimpleName();
    public FourFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_four, container, false);
        return view;
    }
}
