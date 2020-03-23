package com.basic.moviesadmin.ui;

import android.os.Bundle;
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
import com.basic.moviesadmin.models.Episode;
import com.basic.moviesadmin.models.Series;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;

public class EpisodeFormBottomSheet extends  BaseBottomSheet{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Series> series1 = new ArrayList<>();
    private int selectedSeriesPosition = 0;

    Episode episode = null;

    public EpisodeFormBottomSheet() {}
    public EpisodeFormBottomSheet(Episode episode) {
        this.episode = episode;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.episode_form, container, false);

        TextView addEpisode = view.findViewById(R.id.tv_form_title);
        final EditText epiNameEditText = view.findViewById(R.id.et_ep_name);
        final EditText epiVideoLink = view.findViewById(R.id.et_ep_video_link);
        final Button btnSave = view.findViewById(R.id.btn_epi_save);
        final Spinner seriesSpinner = view.findViewById(R.id.sp_series);

        if (episode != null) {
            addEpisode.setText("Edit Episode");
        }

        db.collection(Series.COLLECTION_NAME)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        ArrayList<String> seriesName = new ArrayList<>();
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                            Series series = doc.toObject(Series.class);
                            seriesName.add(series.getTitle());
                            series1.add(series);

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, seriesName);
                        seriesSpinner.setAdapter(adapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        seriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedSeriesPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String epiName = epiNameEditText.getText().toString();
                String videoLink = epiVideoLink.getText().toString();

                String epiId;

                if (EpisodeFormBottomSheet.this.episode != null) {

                    epiId = EpisodeFormBottomSheet.this.episode.getId();
                }else {
                    epiId = UUID.randomUUID().toString();
                }

                final Episode episode = new Episode(epiId, epiName, videoLink,
                        series1.get(selectedSeriesPosition).getId(),
                        series1.get(selectedSeriesPosition).getTitle(),
                        series1.get(selectedSeriesPosition).getEpisodeCount());

                db.collection(Episode.COLLECTION_NAME)
                        .document(epiId)
                        .set(episode)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(getContext(), "save success", Toast.LENGTH_SHORT).show();

                                if (episode != null) {
                                    dismiss();
                                    return;
                                }

                                epiNameEditText.setText("");
                                epiVideoLink.setText("");
                                seriesSpinner.setSelection(0);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });

        return view;
    }
}
