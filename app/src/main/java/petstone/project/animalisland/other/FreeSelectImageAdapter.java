package petstone.project.animalisland.other;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.FullImageActivity;

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


            // 11월 24일 추가
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Uri image_uri = mData.get(pos) ;
                    Log.d("uri", image_uri.toString());
                    Intent intent = new Intent(mContext, FullImageActivity.class);
                    intent.putExtra("fullimguri",image_uri.toString());
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    /*
                    Uri image_uri = mData.get(pos) ;;

                    Intent intent = new Intent(mContext, FullImageActivity.class);
                    intent.putExtra("fullimguri",image_uri);
                    mContext.startActivity(intent);

                     */
                }
            });
            // 여기까지
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
