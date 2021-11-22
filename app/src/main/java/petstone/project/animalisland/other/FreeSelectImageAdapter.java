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

public class FreeSelectImageAdapter extends RecyclerView.Adapter<FreeSelectImageAdapter.ViewHolder>{
    private ArrayList<Uri> mData = null ;
    private Context mContext = null ;

    public FreeSelectImageAdapter(ArrayList<Uri> list, Context context) {
        mData = list ;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView free_select_img;

        ViewHolder(View itemView) {
            super(itemView) ;

            free_select_img = itemView.findViewById(R.id.free_select_img);
        }
    }


    @Override
    public FreeSelectImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.select_free_rehome_img, parent, false) ;
        FreeSelectImageAdapter.ViewHolder vh = new FreeSelectImageAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(FreeSelectImageAdapter.ViewHolder holder, int position) {
        Uri image_uri = mData.get(position) ;

        Glide.with(mContext)
                .load(image_uri)
                .into(holder.free_select_img);
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }

}
