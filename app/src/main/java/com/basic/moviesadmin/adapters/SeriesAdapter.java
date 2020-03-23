package com.basic.moviesadmin.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.moviesadmin.R;
import com.basic.moviesadmin.SeriesFragment;
import com.basic.moviesadmin.models.Series;
import com.basic.moviesadmin.ui.SeriesBottomSheet;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder> {


    private SeriesFragment seriesFragment;
    private ArrayList<Series> series;

    public SeriesAdapter(SeriesFragment seriesFragment, ArrayList<Series> series) {
        this.seriesFragment = seriesFragment;
        this.series = series;
    }

    @NonNull
    @Override
    public SeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.series_item, parent, false);
        return new SeriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesViewHolder holder, int position) {

        Glide.with(seriesFragment.getContext())
                .load(series.get(position).getImageUrl())
                .into(holder.imageUrl);

        holder.noTextView.setText((position + 1) + "");
        holder.titleTextView.setText(series.get(position).getTitle());
        holder.genreNameTextView.setText(series.get(position).getGenreName());
        holder.epCountTextView.setText("Episodes " + series.get(position).getEpisodeCount());
    }

    @Override
    public int getItemCount() {
        return series.size();
    }

    class SeriesViewHolder extends RecyclerView.ViewHolder {

        ImageView imageUrl;
        TextView noTextView;
        TextView titleTextView;
        TextView genreNameTextView;
        TextView epCountTextView;

        public SeriesViewHolder(@NonNull View itemView) {
            super(itemView);

            imageUrl = itemView.findViewById(R.id.image_url);
            noTextView = itemView.findViewById(R.id.tv_series_no);
            titleTextView = itemView.findViewById(R.id.tv_series_title);
            genreNameTextView = itemView.findViewById(R.id.tv_genre_name);
            epCountTextView = itemView.findViewById(R.id.tv_epi_count);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(seriesFragment.getContext());
                    String [] items = {"Edit", "Delete"};

                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            int position = getAdapterPosition();

                            switch (which) {
                                case 0:
                                    SeriesBottomSheet bottomSheet = new SeriesBottomSheet(series.get(position));
                                    bottomSheet.show(seriesFragment.getFragmentManager(), "Edit Series");
                                    break;
                                case 1:
                                    db.collection(Series.COLLECTION_NAME)
                                            .document(series.get(position).getId())
                                            .delete();

                            }
                        }
                    });
                    builder.show();

                    return true;
                }
            });

        }
    }

}
