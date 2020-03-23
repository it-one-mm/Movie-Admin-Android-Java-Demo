package com.basic.moviesadmin.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.basic.moviesadmin.R;
import com.basic.moviesadmin.models.Genre;
import com.basic.moviesadmin.models.Series;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;

public class SeriesBottomSheet extends BaseBottomSheet {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Genre> genres = new ArrayList<>();
    private int selectedGenrePosition = 0;

    Series series = null;

    public SeriesBottomSheet() {}

    public SeriesBottomSheet(Series series) {
        this.series = series;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.series_form, container, false);

        final TextView addSeries = view.findViewById(R.id.tv_add_series);
        final EditText titleEditText = view.findViewById(R.id.et_series_title);
        final EditText descEditText = view.findViewById(R.id.et_series_desc);
        final EditText epiCountEditText = view.findViewById(R.id.et_epi_count);
        final EditText imageUrlEditText = view.findViewById(R.id.et_image_url);
        final Spinner genresSpinner = view.findViewById(R.id.sp_genres);
        final Button saveButton = view.findViewById(R.id.btn_series_save);

        if (series != null) {
            addSeries.setText("Edit Series");
            titleEditText.setText(series.getTitle());
            descEditText.setText(series.getDesc());
//            epiCountEditText.setText(series.getEpisodeCount());
            imageUrlEditText.setText(series.getImageUrl());
        }

//        epiCountEditText.setText("0");

        db.collection(Genre.COLLECTION_NAME)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        ArrayList<String> genreNames = new ArrayList<>();

                        int editPosition = 0;
                        int index = 0;

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Genre genre = doc.toObject(Genre.class);
                            genres.add(genre);
                            genreNames.add(genre.getName());
                        }

                        if (series != null) {
                            if (series.getGenreId().equals(genres.get(selectedGenrePosition).getId())) {
                                editPosition = index;
                            }
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, genreNames);
                        genresSpinner.setAdapter(adapter);

                        if (series != null) {
                            genresSpinner.setSelection(editPosition);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        genresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedGenrePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String desc = descEditText.getText().toString();
                int epiCount = Integer.parseInt(epiCountEditText.getText().toString());
                String imageUrl = imageUrlEditText.getText().toString();

                if (title.trim().equals("") || imageUrl.trim().equals("")) {
                    Toast toast = Toast.makeText(view.getContext(), "Title is required! \n ImageUrl Is Required!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    return;
                }

                String id ;

                if (series != null) {
                    id = series.getId();
                }else {
                    id = UUID.randomUUID().toString();
                }

                final Series series = new Series(id, title, desc, epiCount, imageUrl,
                        genres.get(selectedGenrePosition).getId(),
                        genres.get(selectedGenrePosition).getName());

                db.collection(Series.COLLECTION_NAME)
                        .document(id)
                        .set(series)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(view.getContext(), "Save Success", Toast.LENGTH_SHORT).show();

                                if (series != null) {
                                    dismiss();
                                    return;
                                }

                                titleEditText.setText("");
                                descEditText.setText("");
                                epiCountEditText.setText("");
                                imageUrlEditText.setText("");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(view.getContext(), "Save Failed", Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });

                return view;
    }
}
