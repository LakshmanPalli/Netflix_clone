package com.senku.netflix_clone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.senku.netflix_clone.Model.AllCategory;
import com.senku.netflix_clone.Model.CategoryItemList;
import com.senku.netflix_clone.R;

import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>
{
    Context context;
    List<AllCategory> allCategoryList;

    public MainRecyclerAdapter(Context context, List<AllCategory> allCategoryList) {
        this.context = context;
        this.allCategoryList = allCategoryList;
    }

    @NonNull
    @Override
    public MainRecyclerAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //we will send the image layout
        return new MainRecyclerAdapter.MainViewHolder(LayoutInflater.from(context).inflate(R.layout.mainrecyclerlayout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
         holder.categoryName.setText(allCategoryList.get(position).getCategoryTitle());
         setItemRecycler(holder.itemRecycler,allCategoryList.get(position).getCategoryItemList());
         
    }

    @Override
    public int getItemCount() {
        return allCategoryList.size();
    }

    public static final class MainViewHolder extends RecyclerView.ViewHolder{
        TextView categoryName;
        RecyclerView itemRecycler;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName=itemView.findViewById(R.id.item_category);
            itemRecycler=itemView.findViewById(R.id.mainScreenRecycleView);
        }
    }
    public void setItemRecycler(RecyclerView recyclerView, List<CategoryItemList> categoryItem){
        ItemRecyclerAdapter itemRecyclerAdapter=new ItemRecyclerAdapter(context,categoryItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(itemRecyclerAdapter);
    }
}
