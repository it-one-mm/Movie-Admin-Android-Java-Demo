package com.basic.moviesadmin.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.moviesadmin.EpisodeFragment;
import com.basic.moviesadmin.GenresFragment;
import com.basic.moviesadmin.R;
import com.basic.moviesadmin.models.Episodes;
import com.basic.moviesadmin.models.Genre;
import com.basic.moviesadmin.models.Series;
import com.basic.moviesadmin.ui.EpisodeFormBottomSheet;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {

   EpisodeFragment episodeFragment;
    ArrayList<Episodes> episodes;

    public EpisodeAdapter(EpisodeFragment episodeFragment , ArrayList<Episodes> episodes) {
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

                             FirebaseFirestore db = FirebaseFirestore.getInstance();

                             int position = getAdapterPosition();


                             switch (which) {
                                 case 0:
                                     EpisodeFormBottomSheet bottomSheet = new EpisodeFormBottomSheet(episodes.get(position));
                                     bottomSheet.show(episodeFragment.getFragmentManager(),"Edit Episode");

                                     break;
                                 case 1:

                                     db.collection(Episodes.COLLECTION_NAME)
                                             .document(episodes.get(position).getId())
                                             .delete();

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
