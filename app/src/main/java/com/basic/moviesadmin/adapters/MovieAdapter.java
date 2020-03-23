package com.basic.moviesadmin.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.moviesadmin.MoviesFragment;
import com.basic.moviesadmin.R;
import com.basic.moviesadmin.models.Movie;
import com.basic.moviesadmin.ui.MovieBottomSheet;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHOlder> {

    private MoviesFragment moviesFragment;
    private ArrayList<Movie> movies;

    public MovieAdapter(MoviesFragment moviesFragment, ArrayList<Movie> movies) {
        this.moviesFragment = moviesFragment;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_item, parent, false);

        return new MovieViewHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHOlder holder, int position) {

        Glide.with(moviesFragment.getContext())
                .load(movies.get(position)
                        .getImageUrl())
                .into(holder.urlImageView);
        holder.noTextView.setText(position + 1 + "");
        holder.titleTextView.setText(movies.get(position).getTitle());
        holder.genreTextView.setText(movies.get(position).getGenreName());

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHOlder extends RecyclerView.ViewHolder{

        ImageView urlImageView;
        TextView noTextView;
        TextView titleTextView;
        TextView genreTextView;

        public MovieViewHOlder(@NonNull View itemView) {
            super(itemView);

            genreTextView = itemView.findViewById(R.id.tv_genre_item);
            urlImageView = itemView.findViewById(R.id.image_url);
            noTextView = itemView.findViewById(R.id.tv_movie_no);
            titleTextView = itemView.findViewById(R.id.tv_movie_title);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(moviesFragment.getContext());
                    String[] items = {"Edit", "Delete"};

                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            int position = getAdapterPosition();

                            switch (which) {
                                case 0:
                                    MovieBottomSheet bottomSheet = new MovieBottomSheet(movies.get(position));
                                    bottomSheet.show(moviesFragment.getFragmentManager(), "Edit Movie");
                                    break;
                                case 1:
                                    db.collection(Movie.COLLECTION_NAME)
                                            .document(movies.get(position).getId())
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