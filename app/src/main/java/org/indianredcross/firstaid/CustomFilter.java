//Handling filtered results according to the search query entered by user.
package org.indianredcross.firstaid;

import android.widget.Filter;

import java.util.ArrayList;

import org.indianredcross.firstaid.Data.Pojo;

/**
 * Created by rishabh on 6/24/2016.
 */
public class CustomFilter extends Filter {
    CategoryAdapter adapter;
    ArrayList<Pojo> filterList;

    public CustomFilter(CategoryAdapter adapter, ArrayList<Pojo> filterList) {
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

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.Category= (ArrayList<Pojo>) results.values;
        adapter.notifyDataSetChanged();
    }
}
