package com.basic.moviesadmin;

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

import com.basic.moviesadmin.adapters.EpisodeAdapter;
import com.basic.moviesadmin.models.Episodes;
import com.basic.moviesadmin.ui.EpisodeFormBottomSheet;
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
public class EpisodeFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;

    public EpisodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Context context = container.getContext();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_episode, container, false);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_create_episode);
        recyclerView = view.findViewById(R.id.recycler_view);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EpisodeFormBottomSheet episodeFormBottomSheet = new EpisodeFormBottomSheet();
                episodeFormBottomSheet.show(getFragmentManager(), episodeFormBottomSheet.getTag());

            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        db.collection(Episodes.COLLECTION_NAME)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Toast.makeText(context, "Listen failed", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ArrayList<Episodes> episodes = new ArrayList<>();
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                            Episodes episodes1 = doc.toObject(Episodes.class);
                            episodes.add(episodes1);

                        }

                        EpisodeAdapter adapter = new EpisodeAdapter(EpisodeFragment.this, episodes);
                        recyclerView.setAdapter(adapter);


                    }
                });

        return view;
    }
}
