package bppc.com.firebasetest.Data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import bppc.com.firebasetest.ItemClickListener;
import bppc.com.firebasetest.R;

/**
 * Created by rishabh on 6/12/2016.
 */
public class PojoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    View view;
    Context c;
    ArrayList<Pojo> category=new ArrayList<>();
    ItemClickListener itemClickListener;
    public PojoHolder(View itemView, Context c, ArrayList<Pojo> category ) {
        super(itemView);
        view = itemView;
        this.c = c;
        view.setOnClickListener(this);
        this.category=category;
    }

    public void setValue(String name)
    {
        TextView field = (TextView) view.findViewById(R.id.category);
        field.setText(name);
    }
    public void setUrl(String url)
    {
        ImageView img=(ImageView)view.findViewById(R.id.imgView);
        //url="http://".concat(url);
        //System.out.println(url);
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        Glide.with(c).load(url)
                .override(metrics.widthPixels/3,metrics.widthPixels/3)
                .diskCacheStrategy( DiskCacheStrategy.RESULT )
                .into(img);


    }


    @Override
    public void onClick(View v) {
//        int position= getAdapterPosition();
//        String s=  this.category.get(position).getValue();
//        Intent intent=new Intent(c, SecondActivity.class);
//        intent.putExtra("category",s);
//        c.startActivity(intent);
        this.itemClickListener.onItemClick(v,getAdapterPosition());
    }
    public void setItemClickListener(ItemClickListener listener)
    {
        this.itemClickListener=listener;
    }


}
