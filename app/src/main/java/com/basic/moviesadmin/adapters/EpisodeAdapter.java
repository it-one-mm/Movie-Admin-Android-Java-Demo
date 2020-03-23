package com.basic.moviesadmin.adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.moviesadmin.EpisodeFragment;
import com.basic.moviesadmin.R;
import com.basic.moviesadmin.models.Episode;
import com.basic.moviesadmin.models.Movie;
import com.basic.moviesadmin.ui.EpisodeFormBottomSheet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {

   EpisodeFragment episodeFragment;
    ArrayList<Episode> episodes;

    public EpisodeAdapter(EpisodeFragment episodeFragment , ArrayList<Episode> episodes) {
        this.episodeFragment = episodeFragment;
        this.episodes = episodes;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.episode_item, parent, false);
        return new EpisodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {

//        holder.epiCount.setText(episodes.get(position).getEpisodeCount());
        holder.epiNameTV.setText("Episode name: " + episodes.get(position).getName());
        holder.seriesNameTextView.setText("Series name: " + episodes.get(position).getSeriesName());
        holder.noTextView.setText("No. " + (position + 1) );

    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    class EpisodeViewHolder extends RecyclerView.ViewHolder {

//        final TextView epiCount;
        final TextView epiNameTV;
        final TextView seriesNameTextView;
        final TextView noTextView;

        public EpisodeViewHolder(@NonNull View itemView) {
            super(itemView);

//            epiCount = itemView.findViewById(R.id.tv_epi_count);
            epiNameTV = itemView.findViewById(R.id.tv_epi_name);
            seriesNameTextView = itemView.findViewById(R.id.tv_epi_series);
            noTextView = itemView.findViewById(R.id.tv_epi_no);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                     AlertDialog.Builder builder = new AlertDialog.Builder(episodeFragment.getContext());
                     String[] items = {"Edit", "Delete"};

                     builder.setItems(items, new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {

                             final FirebaseFirestore db = FirebaseFirestore.getInstance();

                             final int position = getAdapterPosition();


                             switch (which) {
                                 case 0:
                                     EpisodeFormBottomSheet bottomSheet = new EpisodeFormBottomSheet(episodes.get(position));
                                     bottomSheet.show(episodeFragment.getFragmentManager(),"Edit Episode");

                                     break;
                                 case 1:
                                     AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(episodeFragment.getContext());

                                     builder1.setTitle("Are you sure you want to delete?");

                                     builder1.setPositiveButton(episodeFragment.getResources().getString(R.string.alert_dialog_ok), new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             db.collection(Episode.COLLECTION_NAME)
                                                     .document(episodes.get(position).getId())
                                                     .delete()
                                                     .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                         @Override
                                                         public void onSuccess(Void aVoid) {
                                                             Toast.makeText(episodeFragment.getContext(), "Episode Delete Success!", Toast.LENGTH_SHORT).show();
                                                         }
                                                     })
                                                     .addOnFailureListener(new OnFailureListener() {
                                                         @Override
                                                         public void onFailure(@NonNull Exception e) {
                                                             Toast.makeText(episodeFragment.getContext(), "Episode Delete Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                         }
                                                     });
                                         }
                                     });

                                     builder1.setNegativeButton(episodeFragment.getResources().getString(R.string.alert_dialog_cancel), new DialogInterface.OnClickListener() {
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
