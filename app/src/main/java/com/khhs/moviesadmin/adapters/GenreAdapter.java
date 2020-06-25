package com.khhs.moviesadmin.adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.khhs.moviesadmin.GenresFragment;
import com.khhs.moviesadmin.R;
import com.khhs.moviesadmin.models.Genre;
import com.khhs.moviesadmin.models.Movie;
import com.khhs.moviesadmin.models.Series;
import com.khhs.moviesadmin.ui.GenreFormBottomSheet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    GenresFragment genresFragment;
    ArrayList<Genre> genres;

    public GenreAdapter(GenresFragment genresFragment, ArrayList<Genre> genres) {
        this.genresFragment = genresFragment;
        this.genres = genres;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_item, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {

        holder.noTextView.setText("" + (position + 1));
        holder.nameTextView.setText(genres.get(position).getName());
    }
    @Override
    public int getItemCount() {
        return genres.size();
    }
    class GenreViewHolder extends RecyclerView.ViewHolder{

        TextView noTextView;
        TextView nameTextView;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);

            noTextView = itemView.findViewById(R.id.tv_no);
            nameTextView = itemView.findViewById(R.id.tv_genre_name);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(genresFragment.getContext());
                    String[] items = {"Edit", "Delete"};

                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final FirebaseFirestore db = FirebaseFirestore.getInstance();
                            final int position = getAdapterPosition();

                            switch (which) {
                                case 0:

                                    GenreFormBottomSheet bottomSheet = new GenreFormBottomSheet(genres.get(position));
                                    bottomSheet.show(genresFragment.getFragmentManager(), bottomSheet.getTag());
                                    break;

                                case 1:
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(genresFragment.getContext());

                                    builder1.setTitle("Are you sure you want to delete?");
                                    builder1.setPositiveButton(genresFragment.getResources().getString(R.string.alert_dialog_ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            db.collection(Genre.COLLECTION_NAME)
                                                    .document(genres.get(position).getId())
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // genre delete inside movies
                                                            db.collection(Movie.COLLECTION_NAME)
                                                                    .get()
                                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                                            for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                                                                                Movie movie = doc.toObject(Movie.class);

                                                                                if (movie.getGenreId().equals(genres.get(position).getId())) {

                                                                                    db.collection(Movie.COLLECTION_NAME)
                                                                                            .document(movie.getId())
                                                                                            .delete()
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {

                                                                                                }
                                                                                            })
                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                @Override
                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                    Toast.makeText(genresFragment.getContext(), "Genre Delete Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            });
                                                                                }
                                                                            }

                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(genresFragment.getContext(), "Genre Delete Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });

                                                            // genre delete inside series
                                                            db.collection(Series.COLLECTION_NAME)
                                                                    .get()
                                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                                            for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                                                                                Series series = doc.toObject(Series.class);
                                                                                if (series.getGenreId().equals(genres.get(position).getId())) {

                                                                                    db.collection(Series.COLLECTION_NAME)
                                                                                            .document(series.getId())
                                                                                            .delete()
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {

                                                                                                }
                                                                                            })
                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                @Override
                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                    Toast.makeText(genresFragment.getContext(), "Genre Delete Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            });
                                                                                }
                                                                            }

                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(genresFragment.getContext(), "Genre Delete Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });
                                        }
                                    });

                                    builder1.setNegativeButton(genresFragment.getResources().getString(R.string.alert_dialog_cancel), new DialogInterface.OnClickListener() {
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