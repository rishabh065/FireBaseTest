package bppc.com.firebasetest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import bppc.com.firebasetest.Data.Pojo;
import bppc.com.firebasetest.Data.PojoHolder;

/**
 * Created by rishabh on 6/16/2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<PojoHolder>  {

    Context c;
    ArrayList<Pojo> Category;

    public CategoryAdapter(Context c, ArrayList<Pojo> category) {

        this.c = c;
        this.Category = category;

    }

    @Override
    public PojoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        return new PojoHolder(itemView,c,Category);

    }

    @Override
    public void onBindViewHolder(PojoHolder holder, int position) {
        holder.setValue(Category.get(position).getValue());
        holder.setUrl(Category.get(position).getUrl());
//        System.out.println(Category.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
//        System.out.println(Category.size());
        return Category.size();
    }




}
