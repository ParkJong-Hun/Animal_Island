package petstone.project.animalisland.other;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.PetfriendUserSelect;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


public class PetfriendUserList_CustomAdapter extends RecyclerView.Adapter<PetfriendUserList_CustomAdapter.CustomViewholder> {
    private ArrayList<PetfriendUser> arrayList;
    private Context context;
    private float mrating;
    private RatingBar rb;

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

        Uri profileUri = Uri.parse(arrayList.get(position).getProfileImgUri());
        //임시로 uid 닉네임 설정
        //holder.tv_nickname.setText(arrayList.get(position).getUid());
        holder.tv_nickname.setText(arrayList.get(position).getNickname());
        holder.tv_Address.setText(arrayList.get(position).getAddress());
        holder.tv_days.setText(" 요일 : "+arrayList.get(position).getDays());

        // 레이팅바 가져오기
        String uid = arrayList.get(position).getUid();
        getRating(holder.ratingBar,uid);




        // 비용이 0일떄
        String s = arrayList.get(position).getPay();
        s = s.replaceAll("\\p{Punct}", "");
        int money = Integer.parseInt(s);
        if(money == 0)
        {
            holder.tv_pay.setText(" 시급 : " + "무료");
        }
        else
        {
            holder.tv_pay.setText(" 시급 : "+arrayList.get(position).getPay()+"원");
        }
        
        //holder.iv_profile.setImageURI(profileUri);

        // 이미지 이름이 없을경우
        String i = arrayList.get(position).getCarrerImgName();
        if(i.length() == 0)
        {
            holder.iv_hasCarrer.setVisibility(View.INVISIBLE);
            holder.tv_carrer.setText(" 자격증 : X");
        }
        else {
            holder.iv_hasCarrer.setVisibility(View.VISIBLE);
            holder.tv_carrer.setText(" 자격증 : O");
        }

        Glide.with(context)
                .load(profileUri)
                .into(holder.iv_profile);

        Log.d("profileUri",profileUri.toString());


        // 활동 가져오기
        boolean mIsSancheck = arrayList.get(position).isHwaldong_sancheck();
        boolean mIsDolBom = arrayList.get(position).isHwaldong_dolbom();
        boolean mIsBeauty = arrayList.get(position).isHwaldong_beauty();

        StringBuilder sb = new StringBuilder();
        if(mIsSancheck)
            sb.append("산책" + " ");
        if(mIsDolBom)
            sb.append("돌봄" + " ");
        if(mIsBeauty)
            sb.append("미용" + " ");
        String str = sb.toString();

        holder.tv_work.setText(" 활동 : " + str);

        // 래이팅바 점수로 바꾸기
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                holder.tv_rating.setText(rating+"점");
                Log.d("점수" , uid +" : "+rating + "점");
            }
        });







    }

    @Override
    public int getItemCount() {
        // 조건 ? 참 : 거짓 삼항연사자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewholder extends RecyclerView.ViewHolder {

        TextView tv_nickname;
        TextView tv_Address;
        ImageView iv_profile;
        TextView tv_days;
        TextView tv_pay;
        TextView tv_carrer;
        ImageView iv_hasCarrer;
        TextView tv_work;
        RatingBar ratingBar;
        TextView tv_rating;

        public CustomViewholder(@NonNull View itemView)
        {
            super(itemView);
            this.tv_nickname = itemView.findViewById(R.id.user_name);
            this.tv_Address = itemView.findViewById(R.id.user_info);
            this.iv_profile = itemView.findViewById(R.id.select_user_profile);
            this.tv_days = itemView.findViewById(R.id.petfriend_list_day_tv);
            this.tv_pay = itemView.findViewById(R.id.petfriend_list_tv_pay);
            this.tv_carrer = itemView.findViewById(R.id.petfrient_list_carrer_tv);
            this.iv_hasCarrer = itemView.findViewById(R.id.hasCarrer_Iv);
            this.tv_work = itemView.findViewById(R.id.petfrient_list_info_tv);
            this.ratingBar = itemView.findViewById(R.id.ratingBar2);
            this.tv_rating = itemView.findViewById(R.id.rating_tv);



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

    void getRating(RatingBar rb,String uid)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("rating").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String s = documentSnapshot.getData().get("rating").toString();
                float f = Float.parseFloat(s);

                rb.setRating(f);
            }
        });

    }




}