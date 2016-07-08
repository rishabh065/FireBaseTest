package ircs.com.firstaid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

import ircs.com.firstaid.Data.Pojo;
import ircs.com.firstaid.Data.PojoHolder;

/**
 * Created by rishabh on 6/16/2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<PojoHolder> implements Filterable {

    Context c;
    ArrayList<Pojo> Category,filterList;
    CustomFilter filter;
    boolean layout_single;

    public CategoryAdapter(Context c, ArrayList<Pojo> category,boolean layout_single) {
        this.c = c;
        Category = category;
        this.filterList = category;
        this.layout_single=layout_single;
    }

    @Override
    public PojoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layout_single ? R.layout.card_view_single : R.layout.card_view, parent, false);
        return new PojoHolder(itemView,c,Category);

    }

    @Override
    public void onBindViewHolder(PojoHolder holder, int position) {
        holder.setValue(Category.get(position).getValue());
        holder.setUrl(Category.get(position).getUrl());
//        System.out.println(Category.get(position).getUrl());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                System.out.println(pos);
        String s=  Category.get(pos).getValue();
        Intent intent=new Intent(c, SecondActivity.class);
        intent.putExtra("category",s);
        c.startActivity(intent);

            }
        });
    }
    public void setLayout_single(boolean var)
    {
        layout_single=var;
    }
    @Override
    public int getItemCount() {
//        System.out.println(Category.size());
        return Category.size();
    }


    @Override
    public Filter getFilter() {
            filter=new CustomFilter(this,filterList);
        return filter;
    }
}
