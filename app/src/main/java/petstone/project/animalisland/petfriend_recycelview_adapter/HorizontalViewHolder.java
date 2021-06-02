package petstone.project.animalisland.petfriend_recycelview_adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import petstone.project.animalisland.R;

public class HorizontalViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;


    public HorizontalViewHolder(View itemView) {
        super(itemView);


        textView = (TextView) itemView.findViewById(R.id.search_item);


    }
}
