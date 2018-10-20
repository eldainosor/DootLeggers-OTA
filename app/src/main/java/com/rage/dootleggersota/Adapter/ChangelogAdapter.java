package com.rage.dootleggersota.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rage.dootleggersota.Modal.ChangelogModal;
import com.rage.dootleggersota.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChangelogAdapter extends RecyclerView.Adapter<ChangelogAdapter.ViewHolder> {

    private ArrayList <ChangelogModal> list;
    private Context context;

    public ChangelogAdapter(ArrayList<ChangelogModal> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_changelog, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ChangelogModal item = list.get(i);

        viewHolder.title.setText(item.getTitle());
        viewHolder.description.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textViewChangelogTitle);
            description = itemView.findViewById(R.id.textViewChangelogDescription);

        }
    }

}
