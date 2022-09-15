package com.example.journalapp.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journalapp.R;
import com.example.journalapp.model.Journal;

import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.viewHolder> {

    private Context context;
    private List<Journal> journalList;

    public JournalAdapter(Context context, List<Journal> journalList) {
        this.context = context;
        this.journalList = journalList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_journal,viewGroup,false);
        return new viewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        String imageUrl;
        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(journalList
                .get(position)
                .getTimeAdd()
                .getSeconds()*1000);
        holder.title.setText(journalList.get(position).getTitle());
        holder.thoughts.setText(journalList.get(position).getThoughts());
        holder.name.setText(journalList.get(position).getUserName());
        imageUrl = journalList.get(position).getImageUri();
        holder.dateAdded.setText(timeAgo);

        Glide.with(context).load(imageUrl).fitCenter().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        public TextView title,thoughts,dateAdded,name;
        public ImageView image,shareButton;
        public String userId,username;


        public viewHolder(@NonNull View itemView,Context mcontex) {
            super(itemView);

            context = mcontex;
            title = itemView.findViewById(R.id.journal_title_list);
            thoughts = itemView.findViewById(R.id.journal_list_thoughts);
            dateAdded = itemView.findViewById(R.id.journal_timestamp_list);
            image = itemView.findViewById(R.id.jornal_image_list);
            name = itemView.findViewById(R.id.journal_row_username);
            shareButton = itemView.findViewById(R.id.journal_row_share_btn);

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });
        }
    }
}
