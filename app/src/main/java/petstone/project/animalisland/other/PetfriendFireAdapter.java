package petstone.project.animalisland.other;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

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

        
        // 레이팅바 가져오기
        String uid = model.getUid();
        getRating(holder.ratingBar,uid);

        // 래이팅바 점수로 바꾸기
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                holder.tv_rating.setText(rating+"점");
                Log.d("점수" , rating + uid);
            }
        });




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
            holder.iv_hasCarrer.setVisibility(View.INVISIBLE);
            holder.tv_carrer.setText(" 자격증 : X");
            holder.tv_carrer.setVisibility(View.GONE);
        }
        else {
            holder.iv_hasCarrer.setVisibility(View.VISIBLE);
            holder.tv_carrer.setText(" 자격증 : O");
            holder.tv_carrer.setVisibility(View.GONE);
        }

        holder.iv_profile.setImageURI(profileUri);

        Glide.with(context)
                .load(profileUri)
                .into(holder.iv_profile);



        // 활동 가져오기
        boolean mIsSancheck = model.isHwaldong_sancheck();
        boolean mIsDolBom = model.isHwaldong_dolbom();
        boolean mIsBeauty = model.isHwaldong_beauty();

        StringBuilder sb = new StringBuilder();
        if(mIsSancheck)
            sb.append("산책" + " ");
        if(mIsDolBom)
            sb.append("돌봄" + " ");
        if(mIsBeauty)
            sb.append("미용" + " ");
        String str = sb.toString();

        holder.tv_work.setText(" 활동 : "+ str);
        





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
        ImageView iv_hasCarrer;
        TextView tv_work;
        RatingBar ratingBar;
        TextView tv_rating;


        public UserViewHolder(@NonNull View itemView) {
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

    private String SplitDays(String days) {
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

    void getRating(RatingBar rb,String uid)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid)
                .collection("popularity")
                .whereNotEqualTo("uid", uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        //한 사람 추천당 0.3 +
                        //권한 없으면 80이 최대
                        rb.setRating(2.5f);
                        rb.setRating(rb.getRating() + 0.3f * value.getDocuments().size());
                        db.collection("users").document(uid)
                                .collection("report")
                                .whereNotEqualTo("uid", uid)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        //한 사람 신고당 0.3 -
                                        //권한 없으면 80이 최대
                                        rb.setRating(rb.getRating() - 0.3f * queryDocumentSnapshots.getDocuments().size());
                                        db.collection("users").document(uid)
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        if (rb.getRating() > 4.0f && !((boolean)documentSnapshot.get("sell_permission") || (boolean)documentSnapshot.get("is_petfriend"))) {
                                                            rb.setRating(4.0f);
                                                        } else if ((boolean)documentSnapshot.get("sell_permission") || (boolean)documentSnapshot.get("is_petfriend")) {
                                                            rb.setRating(rb.getRating() + 1.0f);
                                                        } else if (rb.getRating() > 5.0f) {
                                                            rb.setRating(5.0f);
                                                        } else if (rb.getRating() < 0) {
                                                            rb.setRating(0);
                                                        }
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }


    // 펫프렌즈컴포넌트에서 클릭 이벤트를 위한 인터페이스
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListner(OnItemClickListener listner){
        this.listener = listner;
    }
}
