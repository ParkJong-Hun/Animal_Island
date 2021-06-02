package petstone.project.animalisland.petfriend_recycelview_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import petstone.project.animalisland.R;

public class PetfriendRecycleAdapter extends RecyclerView.Adapter<PetfriendRecycleAdapter.PetfriendViewHolder> {

    private ArrayList<PetfriendSearchData> search_dataset;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class PetfriendViewHolder extends RecyclerView.ViewHolder{

        public TextView PetfriendSearchTextView;

        public PetfriendViewHolder(@NonNull View view) {
            super(view);

            PetfriendSearchTextView = view.findViewById(R.id.search_item);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PetfriendRecycleAdapter(ArrayList<PetfriendSearchData> list)
    {
        search_dataset = list;
    }


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public PetfriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.petfriend_search_item,parent,false);
        PetfriendViewHolder pvh = new PetfriendViewHolder(view);
        return pvh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull PetfriendViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.PetfriendSearchTextView.setText(search_dataset.get(position).getText());
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return search_dataset.size();
    }

}
