package com.example.jeremy.lab2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.MyViewHolder> {

    private List<MyWifi> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, level;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.name);
            level = (TextView) view.findViewById(R.id.level);

        }
    }

    public WifiAdapter(List<MyWifi> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wifi_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyWifi movie = moviesList.get(position);
        holder.title.setText(movie.getName());
        holder.level.setText(""+movie.getLevel());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}

