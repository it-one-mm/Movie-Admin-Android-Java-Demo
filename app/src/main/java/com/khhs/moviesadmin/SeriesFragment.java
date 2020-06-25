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

//import com.basic.moviesadmin.adapters.SeriesAdapter;
import com.khhs.moviesadmin.adapters.SeriesAdapter;
import com.khhs.moviesadmin.models.Series;
import com.khhs.moviesadmin.ui.SeriesBottomSheet;
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
public class SeriesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SeriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context context = container.getContext();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_series, container, false);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_create_series);
        recyclerView = view.findViewById(R.id.recycler_view);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesBottomSheet bottomSheet = new SeriesBottomSheet();
                bottomSheet.show(getFragmentManager(),bottomSheet.getTag());
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        db.collection(Series.COLLECTION_NAME)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Toast.makeText(context, "Listen Failed", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ArrayList<Series> seriesCollection = new ArrayList<>();
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                            Series series = doc.toObject(Series.class);
                            seriesCollection.add(series);

                        }
                        SeriesAdapter adapter = new SeriesAdapter(SeriesFragment.this, seriesCollection);
                        recyclerView.setAdapter(adapter);
                    }
                });

        return  view;
    }
}
