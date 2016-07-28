//Class for filtering the Instructions after the user enters a search string.
package org.indianredcross.firstaid.Data;

import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created by rishabh on 6/24/2016.
 */
public class InstructionFilter extends Filter {
    InstructionAdapter adapter;
    ArrayList<Pojo> filterList;
//Constructor
    public InstructionFilter(InstructionAdapter adapter, ArrayList<Pojo> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        if(constraint!=null && constraint.length()>0)
        {
            constraint=constraint.toString().toUpperCase();
            ArrayList<Pojo> filtered=new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
//                Filtering criteria. The query entered should be a substring of the data at this position in the arraylist.
                if(filterList.get(i).getValue().toUpperCase().contains(constraint))
                {
                    filtered.add(filterList.get(i));
                }
            }
            results.count=filtered.size();
            results.values=filtered;
        }
        else{
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }
//    Resetting the adapter with the filtered results.
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.Category= (ArrayList<Pojo>) results.values;
        adapter.notifyDataSetChanged();
    }
}
