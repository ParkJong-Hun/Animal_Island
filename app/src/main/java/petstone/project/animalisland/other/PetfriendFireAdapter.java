package petstone.project.animalisland.other;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import petstone.project.animalisland.R;

public class PetfriendFireAdapter extends FirestoreRecyclerAdapter<PetfriendFireUser,PetfriendFireAdapter.UserViewHolder > {

    private OnItemClickListener listener;
    private Context context;

    public PetfriendFireAdapter(@NonNull FirestoreRecyclerOptions<PetfriendFireUser> options, Context context) {
        super(options);
        this.context = context;
    }



    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull PetfriendFireUser model) {

        holder.tv_nickname.setText(model.getNickname());
        holder.tv_Address.setText(model.getAddress());


        Uri profileUri = Uri.parse(model.getProfileImgUri());
        //임시로 uid 닉네임 설정
        //holder.tv_nickname.setText(arrayList.get(position).getUid());
        holder.tv_nickname.setText(model.getNickname());
        holder.tv_Address.setText(model.getAddress());
        holder.tv_days.setText(" 요일 : "+SplitDays(model.getDays()));



        // 비용이 0일떄
        String s = model.getPay();
        s = s.replaceAll("\\p{Punct}", "");
        int money = Integer.parseInt(s);
        if(money == 0)
        {
            holder.tv_pay.setText(" 시급 : " + "무료");
        }
        else
        {
            holder.tv_pay.setText(" 시급 : "+model.getPay()+"원");
        }

        //holder.iv_profile.setImageURI(profileUri);

        // 이미지 이름이 없을경우
        String i = model.getCarrerImgName();
        if(i.length() == 0)
        {
            holder.tv_carrer.setText(" 자격증 : X");
        }
        else {
            holder.tv_carrer.setText(" 자격증 : O");
        }

        holder.iv_profile.setImageURI(profileUri);

        Glide.with(context)
                .load(profileUri)
                .into(holder.iv_profile);


        Log.d("profileUri",profileUri.toString());
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.petfriend_user_list_component,parent,false);

        return new UserViewHolder(view);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tv_nickname;
        TextView tv_Address;
        ImageView iv_profile;
        TextView tv_days;
        TextView tv_pay;
        TextView tv_carrer;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_nickname = itemView.findViewById(R.id.user_name);
            this.tv_Address = itemView.findViewById(R.id.user_info);
            this.iv_profile = itemView.findViewById(R.id.select_user_profile);
            this.tv_days = itemView.findViewById(R.id.petfriend_list_day_tv);
            this.tv_pay = itemView.findViewById(R.id.petfriend_list_tv_pay);
            this.tv_carrer = itemView.findViewById(R.id.petfrient_list_carrer_tv);

            // 클릭한 포지션값 얻기
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION && listener !=null){
                        listener.onItemClick(getSnapshots().getSnapshot(pos),pos);
                    }


                }
            });


        }
    }

    String SplitDays(String days) {
        StringBuilder sb = new StringBuilder();
        String s[] = days.split(" ");
        for (int i = 0; i < s.length; i++) {
            switch (s[i]) {
                case "월요일":
                    sb.append("월" + " ");
                    break;
                case "화요일":
                    sb.append("화" + " ");
                    break;
                case "수요일":
                    sb.append("수" + " ");
                    break;
                case "목요일":
                    sb.append("목" + " ");
                    break;
                case "금요일":
                    sb.append("금" + " ");
                    break;
                case "토요일":
                    sb.append("토" + " ");
                    break;
                case "일요일":
                    sb.append("일" + " ");
                    break;

            }
        }
        return sb.toString();
    }

    // 펫프렌즈컴포넌트에서 클릭 이벤트를 위한 인터페이스
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListner(OnItemClickListener listner){
        this.listener = listner;

    }
}
