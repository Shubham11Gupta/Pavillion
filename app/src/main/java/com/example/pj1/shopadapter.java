package com.example.pj1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

 public class shopadapter extends RecyclerView.Adapter<shopadapter.ViewHolder> {
    ArrayList<shopmodel> list;
    ArrayList<shopmodel> listfull;
    Context context;
    uid uid;
    public shopadapter(ArrayList<shopmodel> list, Context context,uid uid) {
        this.list = list;
        this.context = context;
        this.uid=uid;
        listfull=new ArrayList<>(list);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.shopdisplay,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        shopmodel shopmodel=list.get(position);
        Picasso.get().load(shopmodel.getImage2()).placeholder(R.drawable.ic_add_image).into(holder.img1);
        holder.name.setText(shopmodel.getName());
        holder.cat.setText(shopmodel.getCategory());
    }
    public interface uid{
        void uid(shopmodel shopmodel);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img1;
        TextView name,cat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img1=itemView.findViewById(R.id.shopimage1);
            name=itemView.findViewById(R.id.shopname);
            cat=itemView.findViewById(R.id.shopcategory);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uid.uid(list.get(getAdapterPosition()));
                }
            });
        }
    }
}
