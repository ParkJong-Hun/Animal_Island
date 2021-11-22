package petstone.project.animalisland.other;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.SelectFreeRehomeActivity;

public class FreeRecycleAdapter extends RecyclerView.Adapter<FreeRecycleAdapter.ViewHolder> {

    private ArrayList<FreeRehomeList> mData = null ;
    private Context context;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public FreeRecycleAdapter(ArrayList<FreeRehomeList> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public FreeRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.free_rehome_list, parent, false) ;
        FreeRecycleAdapter.ViewHolder vh = new FreeRecycleAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(FreeRecycleAdapter.ViewHolder holder, int position) {

        FreeRehomeList item = mData.get(position) ;

        Glide.with(context)
                .load(item.getImg())
                .into(holder.main_img);

        Glide.with(context)
                .load(item.getGender())
                .into(holder.gender);

        holder.type.setText(item.getType());
        holder.breed.setText(item.getBreed()) ;
        holder.birth.setText(item.getBirth()) ;
        holder.local.setText(item.getLocal()) ;
        holder.date.setText(item.getDate()) ;
        holder.did.setText(item.getDid());

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public void  filterList(ArrayList<FreeRehomeList> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView main_img, gender ;
        TextView type, breed, birth, local, date, did ;

        ViewHolder(View itemView) {
            super(itemView) ;

            main_img = itemView.findViewById(R.id.main_img) ;
            gender = itemView.findViewById(R.id.free_gender) ;
            type = itemView.findViewById(R.id.free_animal_type);
            breed = itemView.findViewById(R.id.free_breed) ;
            birth = itemView.findViewById(R.id.free_birth);
            local = itemView.findViewById(R.id.free_local) ;
            date = itemView.findViewById(R.id.free_date) ;
            did = itemView.findViewById(R.id.document_id);


            itemView.setClickable(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(context, SelectFreeRehomeActivity.class);
                        intent.putExtra("document_id", did.getText().toString());
                        context.startActivity(intent);
                    }
                }
            });

        }
    }
}
