package com.example.aravind95.news;

import android.os.strictmode.ResourceMismatchViolation;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsCard> newsList;

    public NewsAdapter(List<NewsCard> newsList) {
        this.newsList = newsList;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView iv;
        TextView tv;

        public NewsViewHolder(View itemView) {
            super(itemView);
            this.cv = (CardView) itemView.findViewById(R.id.cardview);
            this.iv = (ImageView) itemView.findViewById(R.id.news_image);
            this.tv = (TextView) itemView.findViewById(R.id.news_title);
        }
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        NewsViewHolder nvh = new NewsViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Picasso
        .get()
        .load(newsList.get(position).getImageUrl())
        .placeholder(R.drawable.ic_launcher_foreground)
        .into(holder.iv);

        holder.tv.setText(newsList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
