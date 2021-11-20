package petstone.project.animalisland.other;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import petstone.project.animalisland.R;

public class PetfriendFireAdapter extends FirestoreRecyclerAdapter<PetfriendFireUser,PetfriendFireAdapter.UserViewHolder > {

    private OnItemClickListener listener;

    public PetfriendFireAdapter(@NonNull FirestoreRecyclerOptions<PetfriendFireUser> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull PetfriendFireUser model) {

        holder.tv_nickname.setText(model.getNickname());
        holder.tv_Address.setText(model.getAddress());

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


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_nickname = itemView.findViewById(R.id.user_name);
            this.tv_Address = itemView.findViewById(R.id.user_info);

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

    // 펫프렌즈컴포넌트에서 클릭 이벤트를 위한 인터페이스
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListner(OnItemClickListener listner){
        this.listener = listner;

    }
}
