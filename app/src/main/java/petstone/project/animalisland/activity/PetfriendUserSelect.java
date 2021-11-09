package petstone.project.animalisland.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import petstone.project.animalisland.R;

public class PetfriendUserSelect extends AppCompatActivity {

    ImageView back;
    Button chat_btn;
    private FirebaseFirestore db;
    private FirebaseUser user;
    // 현재 유저 아이디
    private String mMyUid;
    //파이어베이스에서 가져올 데이터들
    private String mUid, mNickName, mAddress;
    //Xml
    private TextView mUserName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petfriend_user_select);

        db = FirebaseFirestore.getInstance();



        Intent intent = getIntent();
        mUid = intent.getStringExtra("UID");

        Log.d(" mUid " ,"가져온 UID : " + mUid);

        chat_btn = findViewById(R.id.petfriend_start_chating_button);
        back = findViewById(R.id.petfriend_user_select_back);
        mUserName = findViewById(R.id.user_name);


        firebaseSearch();
        usercheck();




        // 챗팅버튼 클릭시 챗팅 화면
        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 같은 사용자에게 채팅 못함
                //
                if(mMyUid.equals(mUid))
                {
                    Toast.makeText(getApplicationContext(),"나에게 챗팅 못함",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    finish();
                }
            }
        });

        // 돌아가기 버튼
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void usercheck() {
        //uid 확인
        user = FirebaseAuth.getInstance().getCurrentUser();
        mMyUid = user.getUid();
        Log.d("MyUid", "내 UID : "+ mMyUid.toString());
    }

    // 파이어베이스 검색
    private void firebaseSearch() {

        // 팻프렌즈 콜렉션에 muid와 같은 이름의 문서를 가져옴
        DocumentReference docRef = db.collection("petfriend").document(mUid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("petfriendUser", "DocumentSnapshot data: " + document.getData());

                        mNickName = document.getData().get("nickname").toString();
                        mAddress = document.getData().get("address").toString();

                        Log.d("mNickName", mNickName);
                        mUserName.setText(mNickName);

                    } else {
                        Log.d("petfriendUser", "No such document");
                    }
                } else {
                    Log.d("petfriendUser", "get failed with ", task.getException());
                }
            }
        });
    }
}
