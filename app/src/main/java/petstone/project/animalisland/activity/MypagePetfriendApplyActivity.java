package petstone.project.animalisland.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

import petstone.project.animalisland.R;
import petstone.project.animalisland.other.PetfriendUser;

//마이페이지 펫프렌즈 권한 신청
public class MypagePetfriendApplyActivity extends AppCompatActivity {

    private TextView mJuso_tv, mSchedule_tv;
    private EditText minfo_edt;
    Button cancel, submit, search_btn, schedule_btn;
    ImageView back, license1, license2, license3;
    Switch toggle;

    private String uid;
    private String mJuso, mInfo, mTime, mDay, mJob, mNickname, mDo, mCity, mRo, mDong;

    private FirebaseUser user;
    private FirebaseFirestore db;

    private AlertDialog mAlertDialog;

    private boolean addressNull = true;
    private StringBuilder sb = new StringBuilder();

    FirebaseStorage storage;
    StorageReference careerImagesRef;
    private FirebaseAuth auth;
    private StorageReference profileImagesRef;
    private ArrayList<Uri> imgList = new ArrayList<>();
    private ArrayList<Uri> StorageUri = new ArrayList<>();
    private StringBuilder uriSb = new StringBuilder();
    String fileName;
    Uri file;
    String profileUri;

    private boolean setCarrer=false;
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

        schedule_btn = findViewById(R.id.schedule_btn);
        mSchedule_tv = findViewById(R.id.schedule_tv);
        storage = FirebaseStorage.getInstance();
        careerImagesRef = storage.getReference();




        //유저 정보 확인
        usercheck();
        //프로필 가져오기
        GetProfile();
        //스토리지 주소 설정
        careerImagesRef = careerImagesRef.child("CarrerImg/"+uid+"_carrer"+"/");
        //스토리지 이미지 불러오기
        //StorageImgSearch();
        //스토리지 이미지 삭제
        //StorageDelete();

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
                // 주소가 빈칸이면 다이어로그 안나옴
                if(!addressNull)
                //가입 확인 하기전 확인 다이어로그
                PetfriendDialog();
                else return;



                // 데이터삽입 uid 닉네임 비용 경력 시간 비용 등등
                //PetfriendUser petfriendUser = new PetfriendUser(uid,mNickname);

                //업로드 메소드
                //uploader(petfriendUser);

                //finish();
            }
        });

        // 이미지뷰 이벤트
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.mypage_petfriend_apply_license_image1:
                        GetImg(20);
                        break;
                    case R.id.mypage_petfriend_apply_license_image2:
                        GetImg(21);
                        break;
                    case  R.id.mypage_petfriend_apply_license_image3:
                        GetImg(22);
                        break;
                }
            }
        };

        // 이미지 클릭 이벤트
        license1.setOnClickListener(listener);
        license2.setOnClickListener(listener);
        license3.setOnClickListener(listener);
        
        // 검색 버튼 누를시 웹뷰엑티비티 인텐드
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebviewActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        // 스케줄엑티비티 인텐트
        schedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.setLength(0);
                Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        // 이미지 넣기
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    license1.setVisibility(View.VISIBLE);
                    license2.setVisibility(View.VISIBLE);
                    license3.setVisibility(View.VISIBLE);
                    setCarrer=true;
                } else {
                    license1.setVisibility(View.GONE);
                    license2.setVisibility(View.GONE);
                    license3.setVisibility(View.GONE);
                    setCarrer=false;

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


    // 갤러리에서 이미지 찾기
    private void GetImg(int requestCode)
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, requestCode );

    }

    // 이미지 업로드
    private  void ImgUpload() {

        //imgList.size()
        //imgArrayUri.length

        for(int i = 0; i < imgList.size(); i++)
        {

            if(!imgList.get(i).equals(null))
            {
                Date date = new Date();
                fileName = uid + "_"+date.toString();
                uriSb.append(fileName + ")");
                UploadTask uploadTask = careerImagesRef.child(fileName).putFile(imgList.get(i));
                Log.d("careerImagesRef", careerImagesRef.toString());

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        // Handle unsuccessful uploads
                        Log.d("onFailure", exception.toString());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        Log.d("ImgUpload", taskSnapshot.toString());
                    }
                });
            }

        }

    }


    private void StorageImgSearch()
    {
        //폴더안의 모든 이미지 읽어옴
        careerImagesRef.listAll()
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
                                        StorageUri.add(task.getResult());

                                        Log.d("StorageUri", StorageUri.get(count).toString());
                                        count++;
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
                Log.d("fail",e.toString());
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
        try {
            db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.collection("petfriend").document(user.getUid());

            //petfriendUser.setCarrerImgUri(uriSb.toString());



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

        }catch (Exception e)
        {
            Log.e("error",e.toString());
        }



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

        Log.d("다이어로그", "다이어로그");

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

                    Log.d("주소", "주소");
                    //주소입력 검사
                    if(addressNull)
                        return;
                    if (setCarrer)
                        ImgUpload();



                        // 데이터삽입 uid 닉네임 비용 경력 시간 비용 등등
                        //String uid, String nickname, String originalAddress, String do_address, String gu_address, String ro_address, String dong_address
                        PetfriendUser petfriendUser = new PetfriendUser(
                                uid
                                , mNickname
                                , mJuso
                                , uriSb.toString()
                                , profileUri
                        );


                        Log.d("업로드", "업로드");


                    //업로드
                    uploader(petfriendUser);

                    db = FirebaseFirestore.getInstance();

                    Toast.makeText(getApplicationContext(), "신청완료", Toast.LENGTH_SHORT).show();
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
        //StorageImgSearch();

    }



    // 스케줄 가져오기
    private void ScheduleCheck(Intent intent,String day) {
        ArrayList<Integer> mlist = new ArrayList<>();
        String str;

        sb.append(day+" : ");
        mlist = intent.getIntegerArrayListExtra(day);

        try {
            for (int i = 0; i < mlist.size(); i++) {
                if ((i + 1) % 2 == 1)
                    sb.append(mlist.get(i)+"시" + "-");
                if ((i + 1) % 2 == 0)
                    sb.append(mlist.get(i)+"시" + " ");
            }

            sb.append("\n");
            str = sb.toString();
            mSchedule_tv.setText(str);
        }catch (Exception e)
        {
            Log.e("error", e.toString());
        }

    }

    private void GetProfile()
    {
        db.collection("users")
                .document(uid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                profileUri = documentSnapshot.get("image").toString();
                Log.d("profile", profileUri);

            }
        });

    }

    // 주소 가져오기
    private void AddressCheck() {
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

    //

    // 인텐트 데이터 확인
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //웹 인텐트
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("data");
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                mJuso_tv.setText(result);
            }
        }
        //스케줄 인텐트
        else if(requestCode == 1)
        {
            if (resultCode == RESULT_OK) {
                ArrayList<String> day = data.getStringArrayListExtra("time");
                for (int i=0; i<day.size(); i++)
                {
                    switch (day.get(i)){
                        case "월요일":Log.d(day.get(i), day.get(i).toString());ScheduleCheck(data,day.get(i));break;
                        case "화요일":Log.d(day.get(i), day.get(i).toString());ScheduleCheck(data,day.get(i));break;
                        case "수요일":Log.d(day.get(i), day.get(i).toString());ScheduleCheck(data,day.get(i));break;
                        case "목요일":Log.d(day.get(i), day.get(i).toString());ScheduleCheck(data,day.get(i));break;
                        case "금요일":Log.d(day.get(i), day.get(i).toString());ScheduleCheck(data,day.get(i));break;
                        case "토요일":Log.d(day.get(i), day.get(i).toString());ScheduleCheck(data,day.get(i));break;
                        case "일요일":Log.d(day.get(i), day.get(i).toString());ScheduleCheck(data,day.get(i));break;
                    }
                }
            }

        }
        else if(requestCode == 20){
            if(data == null){
                Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
            }
            else{

                Uri selectImg = data.getData();
                imgList.add(selectImg);
                license1.setImageURI(selectImg);

            }
        }
        else if(requestCode == 21){
            if(data == null){
                Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
            }
            else{

                Uri selectImg = data.getData();
                imgList.add(selectImg);
                license2.setImageURI(selectImg);

            }
        }
        else if(requestCode == 22){
            if(data == null){
                Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
            }
            else{

                Uri selectImg = data.getData();
                imgList.add(selectImg);
                license3.setImageURI(selectImg);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }

    }


}