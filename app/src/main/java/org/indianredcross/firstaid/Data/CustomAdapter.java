//Custom adapter for ListView which extends BaseAdapter and handles on click functions
package org.indianredcross.firstaid.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.indianredcross.firstaid.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter{
    ArrayList<Contacts> contacts;
    Context context;
    private static LayoutInflater inflater=null;
    public CustomAdapter(Context c, ArrayList<Contacts> contacts) {
        context=c;
        this.contacts=contacts;
//        Getting layout inflater
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
//    Getters in the adapter.
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv1;
        TextView tv2;
        ImageView iv;
    }
    @Override
//    Filling an entry with data
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.contact_layout, null);
        holder.tv1=(TextView) rowView.findViewById(R.id.contact_name);
        holder.tv2=(TextView) rowView.findViewById(R.id.contact_num);
        holder.iv= (ImageView) rowView.findViewById(R.id.contact_del);
        holder.iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                contacts.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.tv1.setText(contacts.get(position).getName());
        holder.tv2.setText(contacts.get(position).getNumber());
        return rowView;
    }

}