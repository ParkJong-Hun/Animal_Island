package petstone.project.animalisland.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.errorprone.annotations.Var;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import petstone.project.animalisland.R;
import petstone.project.animalisland.other.PetfriendUser;

//마이페이지 펫프렌즈 권한 신청
public class MypagePetfriendApplyActivity extends AppCompatActivity {

    //xml
    private TextView mJuso_tv, mSchedule_tv;
    private EditText minfo_edt;
    Button cancel, submit, schedule_btn;
    // 검색 삭제
    ImageView search_btn, petfriend_delete_iv;
    ImageView back, license1, license2, license3;
    Switch toggle;
    private CheckBox mSanchack_btn, mDolbom_btn, mBeauty_btn;

    private boolean isImgNull_1 = true, isImgNull_2 = true, isImgNull_3 = true;

    EditText mPay_edt;

    // 가입 데이터
    private String uid;
    private String mJuso, mInfo = "", mTime, mDay, mJob, mNickname, mDo, mCity, mRo, mDong;
    private String mOriginaAddress = "", mSchedule = "";
    private String mPay = "0";
    // 날짜를 담을 리스트
    private ArrayList<String> mDays = new ArrayList<>();
    private StringBuilder mDaySb = new StringBuilder();
    private String Days = "";

    // 다이어로그
    private AlertDialog mAlertDialog;

    // 데이터값  확인
    private boolean addressNull = true;
    private boolean scheduleNull = true;
    private boolean hwaldongNull = true;
    private StringBuilder sb = new StringBuilder();

    //파이어베이스 관련
    private FirebaseUser user;
    private FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference careerImagesRef;
    private FirebaseAuth auth;
    private StorageReference profileImagesRef;
    private StorageReference careerImagesDeletRef;
    private StorageReference storageReference;

    // 활동
    private boolean isSanChck = false;
    private boolean isDolbom = false;
    private boolean isBeauty = false;

    // 스토리지 관련
    private ArrayList<Uri> imgList = new ArrayList<>();
    private ArrayList<String> reImgName = new ArrayList<>();
    private ArrayList<Uri> storageList = new ArrayList<>();
    private String userCarrerImg;
    private StringBuilder uriSb = new StringBuilder();
    private String carrerImgUri = "";
    String fileName = "";
    Uri file;
    String profileUri = "";

    // 기존 가입 여부 확인
    private int newCreate;
    private boolean reEdit = false;

    // 기존 가입한 사람 데이터 불러옴
    private String reJuso, reNickName;

    // 자격증 토글 활성화 여부 확인
    private boolean setCarrer = false;

    //사진 관련
    private String[] imgNameArray = {"", "", ""};
    private String[] imgUriArray = {"", "", ""};


    //점수 가져오기
    private float mRating;
    private int mintPay;


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
        mPay_edt = findViewById(R.id.mypage_petfriend_pay_edt);
        // 행동 버튼
        mSanchack_btn = findViewById(R.id.petfriend_sanchack_btn);
        mDolbom_btn = findViewById(R.id.petfriend_dolbom_btn);
        mBeauty_btn = findViewById(R.id.petfriend_beauty_btn);

        schedule_btn = findViewById(R.id.schedule_btn);
        mSchedule_tv = findViewById(R.id.schedule_tv);
        storage = FirebaseStorage.getInstance();
        careerImagesRef = storage.getReference();
        petfriend_delete_iv = findViewById(R.id.petfriend_delete_iv);


        // 인텐트 가져옴
        Intent intent = getIntent();
        // 0 신규 1 수정
        newCreate = intent.getIntExtra("new", 2);

        //유저 정보 확인
        usercheck();
        //프로필 가져오기
        GetProfile();

        if (newCreate == 1) {
            firebaseSearch();
            Toast.makeText(getApplicationContext(), "기존 데이터 불러옴", Toast.LENGTH_SHORT).show();
        }

        //스토리지 주소 설정
        careerImagesRef = careerImagesRef.child("CarrerImg/" + uid + "_carrer" + "/");
        careerImagesDeletRef = careerImagesRef.child("CarrerImg/");


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
                
                infoCheck();
                // 주소가 빈칸이면 다이어로그 안나옴
                if (!addressNull && !scheduleNull && !hwaldongNull) {
                    PetfriendDialog();
                } else
                    Toast.makeText(getApplicationContext(), "주소, 스케줄, 활동을 확인해 주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
        });


        mSanchack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSanChck == false) {
                    isSanChck = true;
                    hwaldongNull = false;
                    Log.d("산책 채크박스", isSanChck + "");

                } else {
                    isSanChck = false;
                    hwaldongNull = true;
                    Log.d("산책 채크박스", isSanChck + "");
                }

            }
        });
        mDolbom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isDolbom == false) {
                    isDolbom = true;
                    hwaldongNull = false;
                    Log.d("산책 채크박스", isDolbom + "");

                } else {
                    isDolbom = false;
                    hwaldongNull = true;
                    Log.d("산책 채크박스", isDolbom + "");
                }


            }
        });

        mBeauty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isBeauty == false) {
                    isBeauty = true;
                    hwaldongNull = false;
                    Log.d("산책 채크박스", isBeauty + "");

                } else {
                    isBeauty = false;
                    hwaldongNull = true;
                    Log.d("산책 채크박스", isBeauty + "");
                }


            }
        });


        // 이미지뷰 이벤트
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.mypage_petfriend_apply_license_image1:
                        if (isImgNull_1 == true)
                            GetImg(20);
                        break;
                    case R.id.mypage_petfriend_apply_license_image2:
                        if (isImgNull_2 == true)
                            GetImg(21);
                        break;
                    case R.id.mypage_petfriend_apply_license_image3:
                        if (isImgNull_3 == true)
                            GetImg(22);
                        break;
                }
            }
        };

        View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                switch (v.getId()) {
                    case R.id.mypage_petfriend_apply_license_image1:
                        if (isImgNull_1 == false) {
                            ImgDeleteDialog(1);
                            //Toast.makeText(getApplicationContext(), "롱클릭 삭제", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.mypage_petfriend_apply_license_image2:
                        if (isImgNull_2 == false) {
                            ImgDeleteDialog(2);
                            //Toast.makeText(getApplicationContext(), "롱클릭 삭제", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.mypage_petfriend_apply_license_image3:
                        if (isImgNull_3 == false) {
                            //Toast.makeText(getApplicationContext(), "롱클릭 삭제", Toast.LENGTH_SHORT).show();
                            ImgDeleteDialog(3);
                        }
                        break;
                }
                return true;
            }
        };


        // 이미지 클릭 이벤트
        license1.setOnClickListener(listener);
        license2.setOnClickListener(listener);
        license3.setOnClickListener(listener);

        license1.setOnLongClickListener(onLongClickListener);
        license2.setOnLongClickListener(onLongClickListener);
        license3.setOnLongClickListener(onLongClickListener);

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
                    petfriend_delete_iv.setVisibility(View.VISIBLE);
                    setCarrer = true;
                } else {
                    license1.setVisibility(View.GONE);
                    license2.setVisibility(View.GONE);
                    license3.setVisibility(View.GONE);
                    petfriend_delete_iv.setVisibility(View.GONE);
                    setCarrer = false;

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


        // 이미지 전체 삭제
        petfriend_delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                for (int i = 0; i < imgNameArray.length; i++) {
                    fileName = (uid + "_" + i);
                    imgNameArray[i] = "";
                    careerImagesRef.child(fileName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("이미지 삭제", "삭제");
                            Toast.makeText(getApplicationContext(), "이미지 전체 삭제", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("삭제못함", "삭제못함");
                        }
                    });

                }
                 */

                AllImgDeleteDialog();


            }
        });


    }


    // 갤러리에서 이미지 찾기
    private void GetImg(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, requestCode);

    }

    // 이미지 업로드 및 삭제
    private void ImgUpload() {

        try {
            // 삭제
            for (int i = 0; i < imgNameArray.length; i++) {
                if (imgUriArray[i].equals("")) {
                    fileName = (uid + "_" + i);
                    careerImagesRef.child(fileName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("이미지 삭제", "삭제");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("삭제못함", "삭제못함");
                        }
                    });
                }
            }

            //업로드 
            for (int i = 0; i < imgNameArray.length; i++) {
                if (!imgUriArray[i].equals("")) {

                    fileName = imgNameArray[i];
                    uriSb.append(fileName);
                    Uri uri = Uri.parse((imgUriArray[i]));
                    UploadTask uploadTask = careerImagesRef.child(fileName).putFile(uri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("이미지 업로드 성공", taskSnapshot.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("이미지 업로드 실패", e.toString());
                        }
                    });
                    if (i == (imgNameArray.length - 1))
                        break;
                }
                uriSb.append(" ");
            }
        } catch (Exception e) {
            Log.d("업로드에러", e.toString());
        }

    }

    // 스토리지에서 이미지 갯수 찾기
    private void StorageImgSearch() {

        //폴더안의 모든 이미지 읽어옴
        careerImagesRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {

                    @Override
                    public void onSuccess(ListResult listResult) {

                        for (StorageReference item : listResult.getItems()) {

                            item.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {

                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    if (task.isSuccessful()) {
                                        storageList.add(task.getResult());
                                        StorageImgDelete(storageList.size());
                                        Log.d("폴더안에 모든 이미지 ", storageList.size() + "");
                                    }
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("fail", e.toString());
            }
        });


    }

    // 스토리지 이미지 삭제
    private void StorageImgDelete(int index) {

        int num = index - 1;
        String fName = (uid + "_" + num).toString();
        careerImagesRef.child(fName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("스토리지 삭제", "삭제");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("스토리지 삭제못함", "삭제못함");
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

        } catch (Exception e) {
            Log.e("error", e.toString());
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

                    Log.d("검사 시작", "시작");
                    if(addressNull || scheduleNull || hwaldongNull)
                    {
                        Toast.makeText(getApplicationContext(), "주소, 스케줄, 활동을 추가해 주세요.", Toast.LENGTH_SHORT).show();
                        Log.d("검사 끝", "실패");
                        return;
                    }


                    //날짜 돈 변환
                    //getDays();
                    //getPay();

                    //주소입력 검사
                    /*
                    if (addressNull) {
                        Toast.makeText(getApplicationContext(), "주소를 추가해 주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mSchedule.length() < 3 || Days.length() < 2) {
                        Toast.makeText(getApplicationContext(), "스케줄을 추가해 주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!isSanChck & !isDolbom & !isBeauty) {
                        Toast.makeText(getApplicationContext(), "활동을 골라 주세요.", Toast.LENGTH_SHORT).show();
                        return;

                    }

                     */



                    //날짜 돈 변환
                    getDays();
                    getPay();

                    //토글 활성화시 이미지 업로드
                    if (setCarrer)
                        ImgUpload();
                    // 비활성화시 모든 이미지 삭제
                    else
                        StorageImgSearch();

                    carrerImgUri = uriSb.toString();
                    if (carrerImgUri.length() < 5) {
                        carrerImgUri = "";
                    }



                    // 데이터삽입 uid 닉네임 비용 경력 시간 비용 등등
                    //String uid, String nickname, String originalAddress, String do_address, String gu_address, String ro_address, String dong_address
                    PetfriendUser petfriendUser = new PetfriendUser(
                            uid
                            , mNickname
                            , mJuso
                            , carrerImgUri
                            , profileUri
                            , Days
                            , mInfo
                            , mSchedule
                            , mOriginaAddress
                            , mPay
                            , isSanChck
                            , isDolbom
                            , isBeauty
                            , true
                            , mintPay);


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

    //이미지 삭제 확인 얼트 다이어로그
    void ImgDeleteDialog(int i) {

        Log.d("다이어로그", "다이어로그");

        try {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("이미지를 삭제 하실건가요?");


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


                    switch (i) {
                        case 1:
                            imgNameArray[0] = "";
                            imgUriArray[0] = "";
                            license1.setImageResource(0);
                            license1.setImageResource(R.drawable.image);
                            isImgNull_1 = true;
                            break;
                        case 2:
                            imgNameArray[1] = "";
                            imgUriArray[1] = "";
                            license2.setImageResource(0);
                            license2.setImageResource(R.drawable.image);
                            isImgNull_2 = true;
                            break;
                        case 3:
                            imgNameArray[2] = "";
                            imgUriArray[2] = "";
                            license3.setImageResource(0);
                            license3.setImageResource(R.drawable.image);
                            isImgNull_3 = true;
                            break;

                    }
                    dialog.dismiss();
                }
            });

            mAlertDialog = builder.create();
            mAlertDialog.show();

        } catch (Exception e) {
            Log.e("dialog error", e.toString());
        }


    }

    //전체 이미지 삭제 확인 얼트 다이어로그
    void AllImgDeleteDialog() {

        Log.d("다이어로그", "다이어로그");


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("모든 이미지를 삭제 하실건가요?");


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


                for (int i = 0; i < 3; i++) {

                    if (i == 0) {
                        imgNameArray[0] = "";
                        imgUriArray[0] = "";
                        license1.setImageResource(0);
                        license1.setImageResource(R.drawable.image);
                        isImgNull_1 = true;
                    } else if (i == 1) {
                        imgNameArray[1] = "";
                        imgUriArray[1] = "";
                        license2.setImageResource(0);
                        license2.setImageResource(R.drawable.image);
                        isImgNull_2 = true;
                    } else {
                        imgNameArray[2] = "";
                        imgUriArray[2] = "";
                        license3.setImageResource(0);
                        license3.setImageResource(R.drawable.image);
                        isImgNull_3 = true;
                    }

                }

                dialog.dismiss();
            }
        });

        mAlertDialog = builder.create();
        mAlertDialog.show();

    }


    // 돈
    private void getPay() {

        mPay = mPay_edt.getText().toString();
        //Toast.makeText(getApplicationContext(), mPay + "", Toast.LENGTH_SHORT).show();
        if (mPay.length() != 0) {
            if (Integer.parseInt(mPay) <= 0 || mPay.length() == 0) {
                mPay = "0";
                mintPay = Integer.parseInt(mPay);
            } else {
                mintPay = Integer.parseInt(mPay);
                mPay = getFormatDEC(mPay);
            }
        } else {
            mPay = "0";
        }

    }

    // 돈 천에자리에서 , 넣기
    private String getFormatDEC(String number) {

        DecimalFormat dec = new DecimalFormat("##,###,###");
        if (!number.trim().equals("")) {
            number = dec.format(Long.valueOf(number));
        }
        return number;
    }


    // 신청 내용 확인 메소드
    private void infoCheck() {
        AddressCheck();
        mInfo = minfo_edt.getText().toString();
    }

    // 스케줄 가져오기
    private void ScheduleCheck(Intent intent, String day) {
        ArrayList<Integer> mlist = new ArrayList<>();
        String str;

        mDays.add(day);

        sb.append(day + " : ");
        mlist = intent.getIntegerArrayListExtra(day);

        try {
            for (int i = 0; i < mlist.size(); i++) {
                if ((i + 1) % 2 == 1)
                    sb.append(mlist.get(i) + "시" + "-");
                if ((i + 1) % 2 == 0)
                    sb.append(mlist.get(i) + "시" + " ");
            }

            sb.append("\n");
            str = sb.toString();
            mSchedule_tv.setText(str);
            mSchedule = str;
            scheduleNull = false;
            Log.d("날짜 데이터 확인", mDays.size() + "");
        } catch (Exception e) {
            Log.e("error", e.toString());
        }

    }

    // 날짜 가져오기
    private void getDays() {

        for (int i = 0; i < mDays.size(); i++) {
            Days += mDays.get(i) + " ";
        }

        Log.d("활성화된 날", Days);

    }

    // 프로필 이미지 가져오기
    private void GetProfile() {
        db.collection("users")
                .document(uid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                try {
                    profileUri = documentSnapshot.get("image").toString();
                    Log.d("profile", profileUri);
                } catch (Exception e) {
                    Log.d("프로필이미지 없음", profileUri.toString());
                }


            }
        });

    }

    // 주소 가져오기
    private void AddressCheck() {
        //addressNull
        //!mJuso_tv.getText().toString().equals("juso")
        //addressNull ||reEdit
        if (mJuso_tv.getText().toString().length() > 0) {
            // 오리지날 주소
            mJuso = mJuso_tv.getText().toString();
            mOriginaAddress = mJuso;

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
            mJuso = mDo + " " + mCity;

            addressNull = false;
        }
        //Toast.makeText(getApplicationContext(), "현재주소 : " + mJuso, Toast.LENGTH_SHORT).show();
    }

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
        else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                mDays.clear();
                Days = "";
                ArrayList<String> day = data.getStringArrayListExtra("time");
                for (int i = 0; i < day.size(); i++) {
                    switch (day.get(i)) {
                        case "월요일":
                            Log.d(day.get(i), day.get(i).toString());
                            ScheduleCheck(data, day.get(i));
                            break;
                        case "화요일":
                            Log.d(day.get(i), day.get(i).toString());
                            ScheduleCheck(data, day.get(i));
                            break;
                        case "수요일":
                            Log.d(day.get(i), day.get(i).toString());
                            ScheduleCheck(data, day.get(i));
                            break;
                        case "목요일":
                            Log.d(day.get(i), day.get(i).toString());
                            ScheduleCheck(data, day.get(i));
                            break;
                        case "금요일":
                            Log.d(day.get(i), day.get(i).toString());
                            ScheduleCheck(data, day.get(i));
                            break;
                        case "토요일":
                            Log.d(day.get(i), day.get(i).toString());
                            ScheduleCheck(data, day.get(i));
                            break;
                        case "일요일":
                            Log.d(day.get(i), day.get(i).toString());
                            ScheduleCheck(data, day.get(i));
                            break;
                    }
                }
            }

        } else if (requestCode == 20) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
            } else {


                imgNameArray[0] = "";

                Uri selectImg = data.getData();
                //imgList.add(selectImg);
                license1.setImageURI(selectImg);
                isImgNull_1 = false;
                Log.d("img", selectImg.toString());

                fileName = (uid + "_" + 0).toString();
                imgNameArray[0] = fileName;
                imgUriArray[0] = selectImg.toString();

            }
        } else if (requestCode == 21) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
            } else {


                Uri selectImg = data.getData();
                //imgList.add(selectImg);
                license2.setImageURI(selectImg);
                isImgNull_2 = false;

                fileName = (uid + "_" + 1).toString();
                imgNameArray[1] = fileName;
                imgUriArray[1] = selectImg.toString();

            }
        } else if (requestCode == 22) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
            } else {

                try {

                    Uri selectImg = data.getData();
                    //imgList.add(selectImg);
                    license3.setImageURI(selectImg);
                    isImgNull_3 = false;

                    fileName = (uid + "_" + 2).toString();
                    imgNameArray[2] = fileName;
                    imgUriArray[2] = selectImg.toString();
                } catch (Exception e) {
                    Log.d("배열 오류", e.toString());
                }
            }
        }
    }

    private void firebaseSearch() {

        addressNull = false;
        reEdit = true;


        db.collection("petfriend").whereEqualTo("uid", uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d("펫프렌즈 수정 : 연동성공", "\n가져온 UID : " + doc.getId() + " \n가져온 데이터 : " + doc.getData() + "\n");

                    reJuso = doc.getData().get("address").toString();
                    reNickName = doc.getData().get("nickname").toString();
                    Days = doc.getData().get("days").toString();
                    profileUri = doc.getData().get("profileImgUri").toString();
                    mOriginaAddress = doc.getData().get("originalAddress").toString();
                    mInfo = doc.getData().get("info").toString();
                    carrerImgUri = doc.getData().get("carrerImgName").toString();
                    mPay = doc.getData().get("pay").toString();
                    mSchedule = doc.getData().get("schedule").toString();

                    // 돈가져옴
                    //sigungu = sigungu.replaceAll("\\p{Punct}", "");
                    mPay = mPay.replaceAll("\\p{Punct}", "");
                    mPay_edt.setText(mPay);


                    // 활동 가져옴
                    isSanChck = doc.getBoolean("hwaldong_sancheck");
                    isDolbom = doc.getBoolean("hwaldong_dolbom");
                    isBeauty = doc.getBoolean("hwaldong_beauty");

                    if(isSanChck || isDolbom || isBeauty) {
                        if (isSanChck)
                            mSanchack_btn.setChecked(isSanChck);
                        if (isDolbom)
                            mDolbom_btn.setChecked(isDolbom);
                        if (isBeauty)
                            mBeauty_btn.setChecked(isBeauty);
                    }

                    //hwaldongNull = false;
                    //addressNull = false;
                    //scheduleNull = false;

                    // 스케줄 널값
                    try {
                        if (mSchedule.length() > 2)
                            scheduleNull = false;
                        if(reJuso.length() > 2)
                            addressNull = false;
                        if(isSanChck || isDolbom || isBeauty)
                            hwaldongNull = false;

                    }catch (Exception e)
                    {

                    }
                    Log.d("데이터 확인",scheduleNull + ":" +  addressNull +":"+hwaldongNull);


                    //storageReference = storage.getReference();
                    //storageReference = storageReference.child("CarrerImg/" + uid + "_carrer" + "/");


                    // 커리어 이미지 여부 확인

                    try {


                        if (carrerImgUri.length() != 0) {

                            storageReference = storage.getReference();
                            storageReference = storageReference.child("CarrerImg/" + uid + "_carrer" + "/");


                            reImgName.clear();
                            toggle.setChecked(true);
                            String imgName = doc.getData().get("carrerImgName").toString();
                            // 이미지 이름 넣기
                            String[] str = imgName.split(" ");
                            for (int i = 0; i < str.length; i++) {
                                imgNameArray[i] = str[i];
                            }


                            //imgNameArray
                            for (int i = 0; i < imgNameArray.length; i++) {
                                Log.d("imgNameArray", imgNameArray[i] + "");
                                if (imgNameArray[i].length() != 0) {
                                    //StorageReference sb = storageReference.child(imgNameArray[i]);
                                    switch (i) {
                                        case 0:
                                            if (imgNameArray[i].contains(uid + "_" + 0)) {
                                                imgNameArray[0] = uid + "_" + 0;
                                                StorageReference sb = storageReference.child(imgNameArray[0]);
                                                isImgNull_1 = false;
                                                LoadImg(0, sb);
                                            }
                                            break;
                                        case 1:
                                            if (imgNameArray[i].contains(uid + "_" + 1)) {
                                                imgNameArray[1] = uid + "_" + 1;
                                                StorageReference sb = storageReference.child(imgNameArray[1]);
                                                isImgNull_2 = false;
                                                LoadImg(1, sb);
                                            }
                                            break;
                                        case 2:
                                            if (imgNameArray[i].contains(uid + "_" + 2)) {
                                                imgNameArray[2] = uid + "_" + 2;
                                                StorageReference sb = storageReference.child(imgNameArray[2]);
                                                isImgNull_3 = false;
                                                LoadImg(2, sb);
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }


                            for (int i = 0; i < imgNameArray.length; i++) {

                                if (imgName.length() != 0) {
                                    //LoadImg(i);
                                } else {
                                    switch (i) {
                                        case 0:
                                            license1.setImageResource(R.drawable.image);
                                        case 1:
                                            license2.setImageResource(R.drawable.image);
                                        case 2:
                                            license3.setImageResource(R.drawable.image);
                                    }

                                }

                            }


                            //uri
                            //https://firebasestorage.googleapis.com/v0/b/animal-island-512cc.appspot.com/o/CarrerImg%2FDfLWml3R9McBFSQIOLXjpNmPMCJ2_carrer%2FDfLWml3R9McBFSQIOLXjpNmPMCJ2_0?alt=media&token=959c285d-581c-4d97-ab95-e71b8d9da4cb

                        } else {
                            toggle.setChecked(false);
                            petfriend_delete_iv.setVisibility(View.GONE);
                        }
                    }catch (Exception e)
                    {
                        Log.d("이미지 로딩 실패", "실패");
                    }


                    Log.d("reJuso", reJuso);
                    //주소
                    mJuso = reJuso;
                    mJuso_tv.setText(mOriginaAddress);
                    minfo_edt.setText(mInfo);
                    mSchedule_tv.setText(mSchedule);

                }


            }
        });


    }

    // 수정 이미지 불러오기
    private void LoadImg(int i, StorageReference sb) {

        try {

            sb.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    if (i == 0) {
                        Glide.with(getApplicationContext())
                                .load(uri)
                                .into(license1);
                        imgUriArray[0] = uri.toString();
                    } else if (i == 1) {
                        Glide.with(getApplicationContext())
                                .load(uri)
                                .into(license2);
                        imgUriArray[1] = uri.toString();
                    } else {
                        Glide.with(getApplicationContext())
                                .load(uri)
                                .into(license3);
                        imgUriArray[2] = uri.toString();
                    }

                }
            });
        } catch (Exception e) {
            Log.d("이미지 로딩 에러", e.toString());
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