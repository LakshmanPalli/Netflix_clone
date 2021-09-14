package com.senku.netflix_clone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.senku.netflix_clone.MainScreens.MovieDetails;
import com.senku.netflix_clone.Model.CategoryItemList;
import com.senku.netflix_clone.R;

import java.util.List;
//we use this class to adapt(fetch the image urls) the content into all other recycler view movieDetails
public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder> {
    Context context;
    List<CategoryItemList> categoryItemList;
    List<CategoryItemList> moviesListAll;

    public ItemRecyclerAdapter(Context context, List<CategoryItemList> categoryItemList, List<CategoryItemList> moviesListAll) {
        this.context = context;
        this.categoryItemList = categoryItemList;
        this.moviesListAll = moviesListAll;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //we will send the image layout
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.category_row_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) { //takes action like clicks
        Glide.with(context).load(categoryItemList.get(position).getImageUrl()).into(holder.itemImage);//glide is used to fetch image from image url
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MovieDetails.class);
                i.putExtra("movieId",categoryItemList.get(position).getId());
                i.putExtra("movieName",categoryItemList.get(position).getMovieName());
                i.putExtra("movieImageUrl",categoryItemList.get(position).getImageUrl());
                i.putExtra("movieName",categoryItemList.get(position).getFileUrl());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }

    public static final class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage=itemView.findViewById(R.id.itemimage);
        }
    }
}
