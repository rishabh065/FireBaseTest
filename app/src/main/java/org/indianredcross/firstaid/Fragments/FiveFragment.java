//Fragment for displaying basic instructions in first aid.
package org.indianredcross.firstaid.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.indianredcross.firstaid.Data.InstructionAdapter;
import org.indianredcross.firstaid.Data.Pojo;
import org.indianredcross.firstaid.R;
import org.indianredcross.firstaid.images;

import java.util.ArrayList;

public class FiveFragment extends Fragment{
    private final String TAG = getClass().getSimpleName();
    ArrayList<Pojo> Category = new ArrayList<>();
    Firebase ref, ref1;
    private GoogleApiClient client;
    RecyclerView recyclerView;
    InstructionAdapter adapter1;
    RecyclerView.LayoutManager mLayoutManager;
    SearchView sv;
    MenuItem searchItem;
    boolean layout_single=false;

    MenuItem layout_changer;
    public FiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        adapter1 = new InstructionAdapter(getActivity(),Category,layout_single);
        super.onCreate(savedInstanceState);
        super.onResume();
        try {
            if (!Firebase.getDefaultConfig().isPersistenceEnabled()) {
                Firebase.getDefaultConfig().setPersistenceEnabled(true);
            }
            Firebase.setAndroidContext(getActivity());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ref = new Firebase("https://project-7104573469224225532.firebaseio.com/Instructions");
        ref.keepSynced(true);

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Category.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    final Pojo pojo = new Pojo();
                    if(data.getKey().equals("zzzzzz"))
                        continue;
                    pojo.setValue(data.getKey());
                    String string = "https://project-7104573469224225532.firebaseio.com/Instructions/" + data.getKey() + "/url";
//                    System.out.println(string);
                    ref1 = new Firebase(string);
                    ref1.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Pojo pojo1 = new Pojo();
                            pojo1.setValue(pojo.getValue());
                            //System.out.println(pojo1.getValue());
                            pojo1.setUrl((String) dataSnapshot.getValue());
//                            System.out.println(pojo1.getUrl());
                            Category.add(pojo1);
                            try {
                                recyclerView.setAdapter(adapter1);
                                adapter1.notifyDataSetChanged();
                            }
                            catch(Exception e){
                                e.printStackTrace();
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
        client = new GoogleApiClient.Builder(getActivity()).addApi(AppIndex.API).build();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_one, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = (layout_single ? new GridLayoutManager(getActivity(), 1) : new GridLayoutManager(getActivity(), 2));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter1);
        // Inflate the layout for this fragment
        return view;
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater Inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Inflater.inflate(R.menu.menu_first, menu);
        layout_changer=menu.findItem(R.id.layout_select);
        layout_changer.setIcon(layout_single ? R.drawable.grid : R.drawable.list);

        searchItem = menu.findItem(R.id.action_search);
        sv =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        sv.setQueryHint("Search...");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter1.getFilter().filter(query);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.layout_select) {
            layout_single = !layout_single;
            getActivity().supportInvalidateOptionsMenu();
            adapter1.setLayout_single(layout_single);
            recyclerView.setLayoutManager(layout_single ? new GridLayoutManager(getActivity(), 1) : new GridLayoutManager(getActivity(), 2));
            adapter1.notifyDataSetChanged();
            recyclerView.setAdapter(adapter1);
        }
        if(id==R.id.refresh)
        {
            Intent intent=new Intent(getActivity(),images.class);
            getActivity().startService(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}