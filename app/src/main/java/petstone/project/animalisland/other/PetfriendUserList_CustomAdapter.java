package petstone.project.animalisland.other;

import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.MypagePetfriendApplyActivity;
import petstone.project.animalisland.activity.PetfriendUserSelect;


public class PetfriendUserList_CustomAdapter extends RecyclerView.Adapter<PetfriendUserList_CustomAdapter.CustomViewholder> {
    private ArrayList<PetfriendUser> arrayList;
    private Context context;

    public PetfriendUserList_CustomAdapter(ArrayList<PetfriendUser> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public CustomViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 컨텍스트 가져오기
        context = parent.getContext();
        
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.petfriend_user_list_component,parent,false);
        CustomViewholder holder = new CustomViewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewholder holder, int position) {
        //임시로 uid 닉네임 설정
        //holder.tv_nickname.setText(arrayList.get(position).getUid());
        holder.tv_nickname.setText(arrayList.get(position).getNickname());
        holder.tv_Address.setText(arrayList.get(position).getAddress());

    }

    @Override
    public int getItemCount() {
        // 조건 ? 참 : 거짓 삼항연사자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewholder extends RecyclerView.ViewHolder {

        TextView tv_nickname;
        TextView tv_Address;

        public CustomViewholder(@NonNull View itemView)
        {
            super(itemView);
            this.tv_nickname = itemView.findViewById(R.id.user_name);
            this.tv_Address = itemView.findViewById(R.id.user_info);


            // 리사이클러뷰 클릭 이벤트
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 클릭된 포지션 가져오기
                    int pos = getAdapterPosition();
                    PetfriendUser petfriendUser = arrayList.get(pos);
                    String getListUid = petfriendUser.getUid();

                    Log.d("listData","UID :"+petfriendUser.getUid() +"\n"+"닉네임 : "+ petfriendUser.getNickname());



                    Intent intent = new Intent(context, PetfriendUserSelect.class);
                    intent.putExtra("UID",getListUid);
                    context.startActivity(intent);

                }
            });


        }
    }
}