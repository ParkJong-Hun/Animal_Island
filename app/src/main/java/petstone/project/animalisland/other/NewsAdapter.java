package petstone.project.animalisland.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import petstone.project.animalisland.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private ArrayList<NewsList> mData = null ;
    private Context context;

    public NewsAdapter(ArrayList<NewsList> list) {
        mData = list ;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.mypage_news_recycler_text, parent, false) ;
        NewsAdapter.ViewHolder vh = new NewsAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {

        NewsList item = mData.get(position) ;

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

            title = itemView.findViewById(R.id.news_title) ;
            content = itemView.findViewById(R.id.news_content) ;


        }
    }
}
