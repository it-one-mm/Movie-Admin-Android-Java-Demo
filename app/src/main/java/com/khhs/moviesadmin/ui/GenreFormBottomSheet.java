package com.khhs.moviesadmin.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.khhs.moviesadmin.R;
import com.khhs.moviesadmin.models.Genre;
import com.khhs.moviesadmin.models.Movie;
import com.khhs.moviesadmin.models.Series;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

public class GenreFormBottomSheet extends BaseBottomSheet {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Genre genre = null;

    public GenreFormBottomSheet() {
    }

    public GenreFormBottomSheet(Genre genre) {
        this.genre = genre;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.genre_form, container, false);

        final TextView titleTextView = view.findViewById(R.id.tv_title);
        final EditText nameEditText = view.findViewById(R.id.rt_genre_name);
        final Button saveButton = view.findViewById(R.id.btn_save);

        if (genre != null) {
            titleTextView.setText("Edit Genre");
            nameEditText.setText(genre.getName());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!nameEditText.getText().toString().trim().equals(""))
                {
                    final String name = nameEditText.getText().toString().trim();

                    db.collection(Genre.COLLECTION_NAME)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    boolean isGenreExist = false;

                                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                        Genre genre = doc.toObject(Genre.class);
                                        if (genre.getName().toLowerCase().equals(name.toLowerCase())) {
                                            isGenreExist = true;
                                        }
                                    }

                                    if (isGenreExist) {
                                        Toast.makeText(getContext(), "Genre is Already exist!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    String id;

                                    if (genre != null) {
                                        id = genre.getId();
                                    } else {
                                        id = UUID.randomUUID().toString();
                                    }

                                    db.collection("genres").document(id)
                                            .set(new Genre(id, name))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    if (genre != null) {

                                                        // genre update inside movies
                                                        db.collection(Movie.COLLECTION_NAME)
                                                                .get()
                                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                                                                            Movie movie = doc.toObject(Movie.class);

                                                                            if (movie.getGenreId().equals(genre.getId())) {

                                                                                movie.setGenreName(name);

                                                                                db.collection(Movie.COLLECTION_NAME)
                                                                                        .document(movie.getId())
                                                                                        .set(movie)
                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {

                                                                                            }
                                                                                        })
                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                Toast.makeText(view.getContext(), "Genre Update Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });
                                                                            }
                                                                        }

                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(view.getContext(), "Genre Update Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                        // genre update inside series
                                                        db.collection(Series.COLLECTION_NAME)
                                                                .get()
                                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                                                                            Series series = doc.toObject(Series.class);
                                                                            if (series.getGenreId().equals(genre.getId())) {

                                                                                series.setGenreName(name);

                                                                                db.collection(Series.COLLECTION_NAME)
                                                                                        .document(series.getId())
                                                                                        .set(series)
                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {

                                                                                            }
                                                                                        })
                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                Toast.makeText(view.getContext(), "Genre Update Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });
                                                                            }
                                                                        }

                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(view.getContext(), "Genre Update Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                        dismiss();

                                                    } else {
                                                        nameEditText.setText("");
                                                    }
                                                    Toast.makeText(getContext(), "Save Success!", Toast.LENGTH_SHORT).show();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Save Fail", Toast.LENGTH_SHORT).show();
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

            }
        });

        return view;
    }
}