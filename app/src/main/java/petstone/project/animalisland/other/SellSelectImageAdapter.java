package petstone.project.animalisland.other;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import petstone.project.animalisland.R;

public class SellSelectImageAdapter extends RecyclerView.Adapter<SellSelectImageAdapter.ViewHolder>{
    private ArrayList<Uri> mData = null ;
    private Context mContext = null ;

    public SellSelectImageAdapter(ArrayList<Uri> list, Context context) {
        mData = list ;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView sell_select_img;

        ViewHolder(View itemView) {
            super(itemView) ;

            sell_select_img = itemView.findViewById(R.id.sell_select_img);
        }
    }


    @Override
    public SellSelectImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.select_sell_rehome_img, parent, false) ;
        SellSelectImageAdapter.ViewHolder vh = new SellSelectImageAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(SellSelectImageAdapter.ViewHolder holder, int position) {
        Uri image_uri = mData.get(position) ;

        Glide.with(mContext)
                .load(image_uri)
                .into(holder.sell_select_img);
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }

}
