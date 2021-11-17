package petstone.project.animalisland.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import petstone.project.animalisland.R;

public class PetfriendUserSelect extends AppCompatActivity {

    // 파이어베이스 관련
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    // 현재 유저 아이디
    private String mMyUid;

    //파이어베이스에서 가져올 데이터들
    private String mUid, mNickName, mAddress, mInfo, mDays, mSchedule, mCarrerImgUri="",mPay;

    //Xml
    private TextView mUserName;
    private ImageView minfoImg1,minfoImg2,minfoImg3, mUserProfie;
    private String profileUri;
    private ImageView back;
    private Button chat_btn;
    private TextView mInfo_tv;
    private TextView mUserInfo_tv;
    private ImageView mSujung_iv,mDelete_iv;
    private TextView mCarrer_tv;
    private AlertDialog mAlertDialog;
    private TextView mSchedule_tv;

    //uri담을 리스트
    ArrayList<Integer>uriList = new ArrayList<>();
    int mCount;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petfriend_user_select);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        Intent intent = getIntent();
        mUid = intent.getStringExtra("UID");

        storageReference = storageReference.child("CarrerImg/"+mUid+"_carrer"+"/");

        Log.d(" mUid " ,"가져온 UID : " + mUid);

        chat_btn = findViewById(R.id.petfriend_start_chating_button);
        back = findViewById(R.id.petfriend_user_select_back);
        mUserName = findViewById(R.id.user_name);
        minfoImg1 = findViewById(R.id.user_info_image1);
        minfoImg2 = findViewById(R.id.user_info_image2);
        minfoImg3 = findViewById(R.id.user_info_image3);
        mUserProfie = findViewById(R.id.select_user_profile);
        mInfo_tv = findViewById(R.id.petfriend_career_text);
        mUserInfo_tv = findViewById(R.id.user_info);
        mSujung_iv = findViewById(R.id.edit_iv);
        mDelete_iv = findViewById(R.id.delete_iv);
        mCarrer_tv = findViewById(R.id.carrer_tv);
        mSchedule_tv = findViewById(R.id.user_schedule_tv);




            firebaseSearch();
            usercheck();
            //imgSearch();
            btnChange();




        // 챗팅버튼 클릭시 챗팅 화면
        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 같은 사용자에게 채팅 못함
                //
                if(mMyUid.equals(mUid))
                {
                    //Toast.makeText(getApplicationContext(),"나에게 챗팅 못함",Toast.LENGTH_SHORT).show();
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

        mSujung_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sujungDialog();

            }
        });
        mDelete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               deleteDialog();
            }
        });

    }

    // 폴더안의 모든 이미지 불러옴
    private void imgSearch() {


        //폴더안의 모든 이미지 읽어옴
        storageReference.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    int count = 0;
                    @Override
                    public void onSuccess(ListResult listResult) {

                        for (StorageReference item : listResult.getItems()){

                            item.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {

                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    if(task.isSuccessful())
                                    {

                                        switch (count) {
                                            case 0:setImg(task.getResult(),count); break;
                                            case 1:setImg(task.getResult(),count); break;
                                            case 2:setImg(task.getResult(),count); break;

                                        }
                                        count++;
                                        mCount = count;
                                        CarrerImgChange(mCount);

                                            Log.d("count", count+"");
                                            String s[] = mCarrerImgUri.split(" , ");
                                            Log.d("s", s.length+"");

                                    }
                                    else{
                                        Log.d("fail", "onfail");

                                    }

                                }

                            });
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CarrerImgChange(0);
                Log.d("fail",e.toString());
            }
        });



/*
            String s[] = mCarrerImgUri.split(" , ");

            for (int i = 0; i < s.length; i++) {

                Log.d("s" , s[i]);

                Uri uri = Uri.parse(s[i]);
                switch (i) {
                    case 0:
                        setImg(uri,i);
                        break;
                    case 1:
                        setImg(uri,i);
                        break;
                    case 2:
                        setImg(uri,i);
                        break;
                }

            }

 */





    }

    private void setImg(Uri uri,int i) {

        if(i ==0)
        {
            Glide.with(getApplicationContext())
                    .load(uri)
                    .into(minfoImg1);
            Log.d("setImg1" , uri.toString());

        }
        else if(i ==1)
        {
            Glide.with(getApplicationContext())
                    .load(uri)
                    .into(minfoImg2);
            Log.d("setImg2" , uri.toString());

        }
        else if(i ==2)
        {
            Glide.with(getApplicationContext())
                    .load(uri)
                    .into(minfoImg3);
            Log.d("setImg3" , uri.toString());

        }

        else return;

    }

    private void sujungDialog() {


        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("내용을 수정하실건가요?");

            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            });
            builder.setPositiveButton("수정하기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            mAlertDialog = builder.create();
            mAlertDialog.show();

        } catch (Exception e) {
            Log.e("dialog error", e.toString());
        }


    }
    private void deleteDialog() {


        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("삭제하실건가요?");

            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            });
            builder.setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    db.collection("petfriend").document(mMyUid)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Delete", mMyUid);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Delete", "Error deleting document", e);
                                }
                            });

                }
            });

            mAlertDialog = builder.create();
            mAlertDialog.show();

        } catch (Exception e) {
            Log.e("dialog error", e.toString());
        }


    }

    private void btnChange() {

        if(mMyUid.equals(mUid))
        {
            mSujung_iv.setVisibility(View.VISIBLE);
            mDelete_iv.setVisibility(View.VISIBLE);
            chat_btn.setBackgroundColor(Color.GRAY);
            chat_btn.setText("자신한테는 채팅을 할 수 없습니다");
        }


    }

    private void CarrerImgChange(int i)
    {
        if (i == 0) {
            Log.d("mCount", mCount+"");
            minfoImg1.setVisibility(View.GONE);
            minfoImg2.setVisibility(View.GONE);
            this.minfoImg3.setVisibility(View.GONE);
            this.mCarrer_tv.setVisibility(View.VISIBLE);
        } else {
            Log.d("mCount", mCount+"");
            minfoImg1.setVisibility(View.VISIBLE);
            minfoImg2.setVisibility(View.VISIBLE);
            minfoImg3.setVisibility(View.VISIBLE);
            mCarrer_tv.setVisibility(View.GONE);}

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
                        profileUri = document.getData().get("profileImgUri").toString();
                        mDays = document.getData().get("days").toString();
                        mInfo = document.getData().get("info").toString();
                        mSchedule = document.getData().get("schedule").toString();
                        mCarrerImgUri = document.getData().get("carrerImgName").toString();
                        mPay = document.getData().get("pay").toString();


                        if(mCarrerImgUri.length()!=0)
                            imgSearch();
                        else
                            CarrerImgChange(0);


                        Log.d("mCarrerImgUri", mCarrerImgUri.length()+"");
                        //이름
                        mUserName.setText(mNickName);
                        mInfo_tv.setText(mInfo);
                        mUserInfo_tv.setText("요일 : " + mDays +"\n"+"비용 : "+mPay+"\n");
                        mSchedule_tv.setText(mSchedule);

                        //프로필이미지
                        Glide.with(getApplicationContext())
                                .load(Uri.parse(profileUri))
                                .into(mUserProfie);


                    } else {
                        Log.d("petfriendUser", "No such document");
                    }
                } else {
                    Log.d("petfriendUser", "get failed with ", task.getException());
                }
            }
        });


    }


    // 다이어로그 오류 방지
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }

    }

}
