package petstone.project.animalisland.activity;

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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import petstone.project.animalisland.R;
import petstone.project.animalisland.other.PetfriendUser;

//마이페이지 펫프렌즈 권한 신청
public class MypagePetfriendApplyActivity extends AppCompatActivity {

    private TextView mJuso_tv;
    private EditText minfo_edt;
    Button cancel, submit, search_btn;
    ImageView back, license1, license2, license3;
    Switch toggle;

    private FirebaseUser user;

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

        //파이어베이스 선언



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

                //uid 확인
                user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                Log.d("uid",uid.toString());

                PetfriendUser petfriendUser = new PetfriendUser(uid);
                
                //업로드 메소드
                uploader(petfriendUser);

                finish();
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
                if(isChecked) {
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

    //웹 인텐트 데이터 확인
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode==RESULT_OK)
            {
                String result = data.getStringExtra("data");
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                mJuso_tv.setText(result);
            }
        }
    }

    private void uploader(PetfriendUser petfriendUser)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("petfriend").document(user.getUid());



        // 문서 이름 유저 UID 파이어베이스 업로드
        db.collection("petfriend").document(user.getUid()).set(petfriendUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("uploader","UID create post :" + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("uploader", e.toString());
            }
        });



        /* 문서 이름 랜덤 파이어베이스 업로드
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
    
}
