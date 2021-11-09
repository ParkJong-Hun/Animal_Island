package petstone.project.animalisland.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import petstone.project.animalisland.R;
import petstone.project.animalisland.other.PetfriendUser;

//마이페이지 펫프렌즈 권한 신청
public class MypagePetfriendApplyActivity extends AppCompatActivity {

    private TextView mJuso_tv;
    private EditText minfo_edt;
    Button cancel, submit, search_btn;
    ImageView back, license1, license2, license3;
    Switch toggle;

    private String uid;
    private String mJuso, mInfo, mTime, mDay, mJob, mNickname, mDo, mCity, mRo, mDong;

    private FirebaseUser user;
    private FirebaseFirestore db;

    private AlertDialog mAlertDialog;

    private boolean addressNull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_petfriend_apply);
        cancel = findViewById(R.id.mypage_petfriend_apply_cancel);
        submit = findViewById(R.id.mypage_petfriend_apply_submit);
        search_btn = findViewById(R.id.search_btn); // 검색버튼
        mJuso_tv = findViewById(R.id.mypage_petfriend_apply_address_city_tv); // 주소 텍스트뷰
        minfo_edt = findViewById(R.id.mypage_petfriend_user_info_edt);
        back = findViewById(R.id.back_mypage_petfriend_apply);
        toggle = findViewById(R.id.mypage_petfriend_apply_toggleButton);
        license1 = findViewById(R.id.mypage_petfriend_apply_license_image1);
        license2 = findViewById(R.id.mypage_petfriend_apply_license_image2);
        license3 = findViewById(R.id.mypage_petfriend_apply_license_image3);
        license1.setVisibility(View.GONE);
        license2.setVisibility(View.GONE);
        license3.setVisibility(View.GONE);


        //유저 정보 확인
        usercheck();

        //취소 버튼
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // 확인버튼
        submit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                setResult(RESULT_OK);

                //내용 확인 메소드
                infoCheck();

                //가입 확인 하기전 확인 다이어로그
                PetfriendDialog();


                // 데이터삽입 uid 닉네임 비용 경력 시간 비용 등등
                //PetfriendUser petfriendUser = new PetfriendUser(uid,mNickname);

                //업로드 메소드
                //uploader(petfriendUser);

                //finish();
            }
        });

        // 검색 버튼 누를시 웹뷰엑티비티 인텐드
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebviewActivity.class);
                startActivityForResult(intent, 0);
            }
        });


        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    license1.setVisibility(View.VISIBLE);
                    license2.setVisibility(View.VISIBLE);
                    license3.setVisibility(View.VISIBLE);
                } else {
                    license1.setVisibility(View.GONE);
                    license2.setVisibility(View.GONE);
                    license3.setVisibility(View.GONE);

                }
            }
        });

        //뒤로가기
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    // 유저정보 확인
    private void usercheck() {
        //uid 확인
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        Log.d("uid", uid.toString());
        // 유저 정보 가져오기
        download(uid);
    }

    // 파이에베이스 에서 유저 정보 다운로드
    private void download(String muid) {
        try {
            //파이어베이스 초기화
            db = FirebaseFirestore.getInstance();
            // 콜렉션 user에   uid와 같은값 갖고있는 문서를 불러옴
            // 닉네임을 가져옴
            db.collection("users")
                    .whereEqualTo("uid", muid)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                Log.d("nickname", doc.getString("nickname"));
                                mNickname = doc.getString("nickname");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //파이어베이스 에 업로드
    private void uploader(PetfriendUser petfriendUser) {
        //파이어베이스 초기화
        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("petfriend").document(user.getUid());


        // 문서 이름 = 유저 UID 파이어베이스 업로드
        db.collection("petfriend").document(user.getUid()).set(petfriendUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("uploader", "UID create post :" + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("uploader", e.toString());
            }
        });



        /* 문서 이름 = 랜덤 파이어베이스 업로드
        db.collection("petfriend").add(petfriendUser)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(uploader,"UID create post :" + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("uploader", e.toString());
                    }
                });
         */


    }

    //신청 확인 얼트 다이어로그
    void PetfriendDialog() {

        try {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("펫프렌지 확인 메세지").setTitle("가입할건가여?");


            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            });
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "신청완료", Toast.LENGTH_SHORT).show();

                    // 데이터삽입 uid 닉네임 비용 경력 시간 비용 등등
                    //String uid, String nickname, String originalAddress, String do_address, String gu_address, String ro_address, String dong_address
                    PetfriendUser petfriendUser = new PetfriendUser(
                            uid
                            , mNickname
                            , mJuso
                    );

                    //업로드 메소드
                    uploader(petfriendUser);

                    db = FirebaseFirestore.getInstance();

                    finish();

                }
            });

            mAlertDialog = builder.create();
            mAlertDialog.show();

        } catch (Exception e) {
            Log.e("dialog error", e.toString());
        }


    }

    // 신청 내용 확인 메소드
    private void infoCheck() {

        AddressCheck();
        ScheduleCheck();

    }



    private void ScheduleCheck()
    {

    }


    private void AddressCheck()
    {
        if (!mJuso_tv.getText().toString().equals("juso")) {
            // 오리지날 주소
            mJuso = mJuso_tv.getText().toString();

            // 숫자와 특수문자를 제거할 주소
            String sigungu = mJuso;

            // 숫자 제거
            sigungu = mJuso.replaceAll("\\d", "");
            // 특수 문자 제거
            sigungu = sigungu.replaceAll("\\p{Punct}", "");
            // 주소를 시 군 구로 짤라서 배열에 넣기


            String[] str_arr = sigungu.split(" ");
            Log.d("str_arr", String.valueOf(str_arr.length));

            // 배열안 데이터 확인

            for (int i = 0; i < str_arr.length; i++) {
                Log.d("juso", String.valueOf(i + " : ") + str_arr[i]);
            }



            // 도 , 시
            mDo = str_arr[1];
            Log.d("mDo", str_arr[1]);
            // 구
            mCity = str_arr[2];
            Log.d("mCity", str_arr[2]);
            // 로
            mRo = str_arr[3];
            Log.d("mRo", str_arr[3]);
            // 동
            mDong = str_arr[5];
            Log.d("mDong", str_arr[5]);

            // 최종주소(시 + 구)
            mJuso = mDo+" "+mCity;

            addressNull = false;
        }

        mInfo = minfo_edt.getText().toString();
        Toast.makeText(getApplicationContext(), "현재주소 : " + mJuso, Toast.LENGTH_SHORT).show();
    }


    //웹 인텐트 데이터 확인
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("data");
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                mJuso_tv.setText(result);
            }
        }
    }

    // WindowLeaked 에러 해결
/*
    @Override
    protected void onStop() {
        super.onStop();
        if(mAlertDialog != null)
        {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }
 */

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }

    }


}