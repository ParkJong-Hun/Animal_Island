package petstone.project.animalisland;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FreeRecycleAdapter extends RecyclerView.Adapter<FreeRecycleAdapter.ViewHolder> {

    private ArrayList<FreeRehomeList> mData = null ;
    private Context context;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    FreeRecycleAdapter(ArrayList<FreeRehomeList> list) {
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

        holder.main_img.setImageDrawable(item.getImg()) ;
        holder.gender.setImageDrawable(item.getGender()) ;
        holder.breed.setText(item.getBreed()) ;
        holder.age.setText(item.getAge()) ;
        holder.local.setText(item.getLocal()) ;
        holder.date.setText(item.getDate()) ;
        holder.nickname.setText(item.getNickname()) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView main_img, gender ;
        TextView breed, age, local, date, nickname ;

        ViewHolder(View itemView) {
            super(itemView) ;

            main_img = itemView.findViewById(R.id.main_img) ;
            gender = itemView.findViewById(R.id.free_gender) ;
            breed = itemView.findViewById(R.id.free_breed) ;
            age = itemView.findViewById(R.id.free_age) ;
            local = itemView.findViewById(R.id.free_local) ;
            date = itemView.findViewById(R.id.free_date) ;
            nickname = itemView.findViewById(R.id.free_nickname) ;

            itemView.setClickable(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(context, Select_Free_Rehome.class);
                        context.startActivity(intent);
                    }
                }
            });

        }
    }
}
