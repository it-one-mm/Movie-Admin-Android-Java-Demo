package com.khhs.moviesadmin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.khhs.moviesadmin.adapters.MovieAdapter;
import com.khhs.moviesadmin.models.Movie;
import com.khhs.moviesadmin.ui.MovieBottomSheet;
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
public class MoviesFragment extends Fragment {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         final Context context = container.getContext();
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        FloatingActionButton createGenreFAB = view.findViewById(R.id.fab_create_movie);
        recyclerView = view.findViewById(R.id.recycler_view);

        createGenreFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieBottomSheet bottomSheet = new MovieBottomSheet();
                bottomSheet.show(getFragmentManager(),bottomSheet.getTag());
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        db.collection(Movie.COLLECTION_NAME)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Toast.makeText(context, "Save failed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ArrayList<Movie> movies = new ArrayList<>();
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                            Movie movie = doc.toObject(Movie.class);
                            movies.add(movie);
                        }
                        MovieAdapter adapter = new MovieAdapter(MoviesFragment.this, movies);
                        recyclerView.setAdapter(adapter);
                    }
                });

        return view;
    }
}
