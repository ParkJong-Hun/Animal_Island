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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
                    String documentName = "";
                    if (mMyUid.compareTo(mUid) > 0) {
                        documentName = mMyUid + "_" + mUid;
                    } else {
                        documentName = mUid + "_" + mMyUid;
                    }
                    if (!documentName.isEmpty() || !documentName.equals("")) {
                        Map<String, Object> initData = new HashMap<>();
                        initData.put("uid", mMyUid);
                        initData.put("readed", 1);
                        initData.put("date", new Date());
                        initData.put("article",  mNickName + "님에게 손을 흔듭니다.");

                        String finalDocumentName = documentName;
                        db.collection("chats")
                                .document(documentName)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (!documentSnapshot.exists()) {
                                            Map<String, Object> data = new HashMap<>();
                                            if (mMyUid.compareTo(mUid) < 0) {
                                                data.put("uid", mMyUid);
                                                data.put("uid2", mUid);
                                            } else {
                                                data.put("uid", mUid);
                                                data.put("uid2", mMyUid);
                                            }

                                            db.collection("chats").document(finalDocumentName).set(data)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("d", "채팅 생성 성공");

                                                            db.collection("chats").document(finalDocumentName).collection("messages").document(new Date().toString() + mMyUid).set(initData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d("d", "채팅 초기 메시지 생성 성공");
                                                                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                                                    intent.putExtra("whoUID", mUid);
                                                                    intent.putExtra("chatName", finalDocumentName);
                                                                    startActivity(intent);
                                                                }
                                                            });
                                                        }
                                                    });
                                        } else {
                                            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                            intent.putExtra("whoUID", mUid);
                                            intent.putExtra("chatName", finalDocumentName);
                                            startActivity(intent);
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("w", "채팅 에러");
                                    }
                                });
                    }
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
