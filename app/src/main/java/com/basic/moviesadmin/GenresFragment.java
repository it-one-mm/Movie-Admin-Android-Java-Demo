package com.basic.moviesadmin;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.basic.moviesadmin.adapters.GenreAdapter;
import com.basic.moviesadmin.models.Genre;
import com.basic.moviesadmin.ui.GenreFormBottomSheet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GenresFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Genre> genres;

    public GenresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Context context = container.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_genres, container, false);

        FloatingActionButton createGenreFAB = view.findViewById(R.id.fab_create_genre);
        final RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        final EditText searchEditText = view.findViewById(R.id.et_search);

        genres = new ArrayList<>();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = s.toString().toLowerCase().trim();

                if (count > 0) {

                    ArrayList<Genre> filterGenres = new ArrayList<>();

                    for(Genre genre: genres) {
                        if (genre.getName().toLowerCase().contains(search)) {
                            filterGenres.add(genre);
                        }
                    }

                    GenreAdapter adapter = new GenreAdapter(GenresFragment.this, filterGenres);
                    recyclerView.setAdapter(adapter);
                }else {

                    GenreAdapter adapter = new GenreAdapter(GenresFragment.this, genres);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        createGenreFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GenreFormBottomSheet bottomSheet = new GenreFormBottomSheet();
                bottomSheet.show(getFragmentManager(), bottomSheet.getTag());
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        db.collection(Genre.COLLECTION_NAME)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Toast.makeText(context, "listener failed", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        genres.clear();

                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {

                            Genre genre = doc.toObject(Genre.class);
                            genres.add(genre);
                        }

                        GenreAdapter adapter = new GenreAdapter(GenresFragment.this, genres);
                        recyclerView.setAdapter(adapter);
                    }
                });

        return view;
    }
}
