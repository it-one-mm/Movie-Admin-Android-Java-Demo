package com.khhs.moviesadmin.adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.khhs.moviesadmin.R;
import com.khhs.moviesadmin.SeriesFragment;
import com.khhs.moviesadmin.models.Episode;
import com.khhs.moviesadmin.models.Series;
import com.khhs.moviesadmin.ui.SeriesBottomSheet;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

                            final FirebaseFirestore db = FirebaseFirestore.getInstance();
                            final int position = getAdapterPosition();

                            switch (which) {
                                case 0:
                                    SeriesBottomSheet bottomSheet = new SeriesBottomSheet(series.get(position));
                                    bottomSheet.show(seriesFragment.getFragmentManager(), "Edit Series");
                                    break;
                                case 1:
                                    AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(seriesFragment.getContext());

                                    builder1.setTitle("Are you sure you want to delete?");

                                    builder1.setPositiveButton(seriesFragment.getResources().getString(R.string.alert_dialog_ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            db.collection(Series.COLLECTION_NAME)
                                                    .document(series.get(position).getId())
                                                    .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // series delete inside episode
                                                    db.collection(Episode.COLLECTION_NAME)
                                                            .get()
                                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                                    for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                                                                        Episode episode = doc.toObject(Episode.class);

                                                                        if (episode.getSeriesId().equals(series.get(position).getId())) {

                                                                            db.collection(Episode.COLLECTION_NAME)
                                                                                    .document(episode.getId())
                                                                                    .delete()
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
                                                                                            Toast.makeText(seriesFragment.getContext(), "Series Delete Success!", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Toast.makeText(seriesFragment.getContext(), "Series Delete Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }

                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(seriesFragment.getContext(), "Series Delete Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(seriesFragment.getContext(), "Series Delete Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });

                                    builder1.setNegativeButton(seriesFragment.getResources().getString(R.string.alert_dialog_cancel), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    builder1.show();



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
