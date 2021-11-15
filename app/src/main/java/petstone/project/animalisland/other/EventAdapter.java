package petstone.project.animalisland.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import petstone.project.animalisland.R;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private ArrayList<EventList> mData = null ;
    private Context context;

    public EventAdapter(ArrayList<EventList> list) {
        mData = list ;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.mypage_event_recycler_text, parent, false) ;
        ViewHolder vh = new ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        EventList item = mData.get(position) ;

        holder.title.setText(item.getTitle());
        holder.content.setText(item.getContent()) ;

    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, content ;

        ViewHolder(View itemView) {
            super(itemView) ;

            title = itemView.findViewById(R.id.event_title) ;
            content = itemView.findViewById(R.id.event_content) ;


        }
    }
}
