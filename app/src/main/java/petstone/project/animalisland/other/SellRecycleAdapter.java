package petstone.project.animalisland.other;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.SelectSellRehomeActivity;

public class SellRecycleAdapter extends RecyclerView.Adapter<SellRecycleAdapter.ViewHolder> {

    private ArrayList<SellRehomeList> mData = null ;
    private Context s_context;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public SellRecycleAdapter(ArrayList<SellRehomeList> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public SellRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        s_context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) s_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.sell_rehome_list, parent, false) ;
        SellRecycleAdapter.ViewHolder vh = new SellRecycleAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(SellRecycleAdapter.ViewHolder holder, int position) {

        SellRehomeList item = mData.get(position) ;

        holder.main_img.setImageDrawable(item.getImg()) ;
        holder.gender.setImageDrawable(item.getGender()) ;
        holder.type.setText(item.getType());
        holder.breed.setText(item.getBreed()) ;
        holder.birth.setText(item.getBirth()) ;
        holder.local.setText(item.getLocal()) ;
        holder.date.setText(item.getDate()) ;
        holder.price.setText(item.getPrice());
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView main_img, gender ;
        TextView type, breed, birth, local, date, price ;

        ViewHolder(View itemView) {
            super(itemView) ;

            main_img = itemView.findViewById(R.id.main_img) ;
            gender = itemView.findViewById(R.id.sell_gender) ;
            type = itemView.findViewById(R.id.sell_animal_type);
            breed = itemView.findViewById(R.id.sell_breed) ;
            birth = itemView.findViewById(R.id.sell_birth);
            local = itemView.findViewById(R.id.sell_local) ;
            date = itemView.findViewById(R.id.sell_date) ;
            price = itemView.findViewById(R.id.sell_price);

            itemView.setClickable(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(s_context, SelectSellRehomeActivity.class);
                        s_context.startActivity(intent);
                    }
                }
            });

        }
    }
}
