package petstone.project.animalisland.component;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.Session2Command;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.LoginActivity;
import petstone.project.animalisland.activity.MypageInfoEditActivity;
import petstone.project.animalisland.activity.MypagePetfriendApplyActivity;
import petstone.project.animalisland.activity.MypageSellApplyActivity;
import petstone.project.animalisland.other.MypageCustomListAdapter;
import petstone.project.animalisland.other.MypageEventDialog;
import petstone.project.animalisland.other.MypageNewsDialog;

public class MypageComponent extends Fragment {
    FirebaseAuth auth;
    FirebaseFirestore db;

    ListView list;
    MypageCustomListAdapter listAdapter;
    Button mypage_profile_edit, mypage_description_edit;

    TextView nickname, description;
    ImageView profile_image;
    RatingBar ratingBar;
    TextView sell, petFriend;

    FirebaseStorage storage;
    StorageReference profileImagesRef;
    Uri file;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mypage_component, container, false);

        mypage_profile_edit = rootView.findViewById(R.id.mypage_profile_edit);
        mypage_description_edit = rootView.findViewById(R.id.mypage_description_edit);
        nickname = rootView.findViewById(R.id.mypage_nickname);
        description = rootView.findViewById(R.id.mypage_descripiton);
        profile_image = rootView.findViewById(R.id.mypage_profile_image);
        ratingBar = rootView.findViewById(R.id.ratingBar);
        sell = rootView.findViewById(R.id.mypage_sell_tv);
        petFriend = rootView.findViewById(R.id.mypage_petfriend_tv);

        list = rootView.findViewById(R.id.mypage_list);
        listAdapter = new MypageCustomListAdapter(getContext());
        list.setAdapter(listAdapter);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        profileImagesRef = storage.getReference("profileImages");

        db.collection("users")
                .whereEqualTo("uid", auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            //나이 반환
                            Timestamp timestamp_birth = (Timestamp)document.get("birth");
                            Date date_birth = timestamp_birth.toDate();
                            LocalDate local_birth = date_birth.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate();
                            int age = Period.between(local_birth, LocalDate.now()).getYears();
                            
                            String description_str = "성명 : " + document.get("name") + "(";
                            switch (document.get("sex").toString()) {
                                case "male":
                                    description_str += "男)\n나이 : ";
                                    break;
                                case "female":
                                    description_str += "女)\n나이 : ";
                                    break;
                                default:
                                    description_str += "中)\n나이 : ";
                                    break;
                            }
                            description_str += age + "살\n지역 : ";
                            try {
                                description_str += document.get("address").toString();
                            } catch (Exception e) {
                                description_str += "미작성됨";
                            }

                            description.setText(description_str);

                            if ((boolean)document.get("sell_permission")) {
                                sell.setText("유료 분양\n가능");
                            } else {
                                sell.setText("유료 분양\n불가능");
                            }

                            if ((boolean)document.get("is_petfriend")) {
                                petFriend.setText("펫 프렌즈\n회원님");
                            } else {
                                petFriend.setText("일반\n회원님");
                            }

                            db.collection("users").document(auth.getUid())
                                    .collection("popularity")
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (DocumentSnapshot document :
                                                    queryDocumentSnapshots) {
                                                //한 사람 추천당 0.1 +
                                                ratingBar.setRating(ratingBar.getRating() + 0.1f);
                                                //권한 없으면 80이 최대
                                                if (ratingBar.getRating() > 4.0 && !((boolean)document.get("sell_permission") || (boolean)document.get("is_petfriend"))) {
                                                    ratingBar.setRating(4.0f);
                                                }
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("fail", "현재 생성된 하위 컬렉션이 없는 에러이거나, 진짜로 아무도 안함.");
                                        }
                                    });


                            nickname.setText(document.get("nickname").toString());

                            try {
                                URL url = new URL((String) document.get("image"));
                                Uri uri = Uri.parse(url.toURI().toString());
                                Glide.with(getContext())
                                        .load(uri)
                                        .into(profile_image);
                            } catch (Exception e) {
                            }
                        }
                    }
                });

        //프로필 편집 클릭 리스너
        mypage_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //파일 업로드
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 200 );
            }
        });

        mypage_description_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MypageInfoEditActivity.class);
                startActivityForResult(intent, 0 );
            }
        });

        //하단 리스트 클릭 리스너
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch(position) {
                    //펫프렌즈신청
                    case 0: {
                        intent = new Intent(getContext(), MypagePetfriendApplyActivity.class);
                        startActivity(intent);
                        break;
                    }
                    //유료 분양 권한 신청
                    case 1: {
                        intent = new Intent(getContext(), MypageSellApplyActivity.class);
                        startActivity(intent);
                        break;
                    }
                    //이벤트
                    case 2: {
                        MypageEventDialog dlg = new MypageEventDialog(getContext());
                        dlg.addArticle("아아아아 내용");
                        dlg.show();
                        break;
                    }
                    //공지사항
                    case 3: {
                        MypageNewsDialog dlg = new MypageNewsDialog(getContext());
                        dlg.addArticle("아아아아 내용");
                        dlg.show();
                        break;
                    }
                    //로그아웃
                    case 4: {
                        auth.signOut();

                        intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {//정보 수정 결과
                if (resultCode == -1) {
                    Toast.makeText(getContext(), "정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                } else if (resultCode == 0) {

                }
                break;
            }
            case 1: {//펫프렌즈 신청 결과
                if (resultCode == -1) {

                } else if (resultCode == 0) {

                }
                break;
            }
            case 2: {//유료분양 권한 신청 결과
                if (resultCode == -1) {

                } else if (resultCode == 0) {

                }
                break;
            }
            case 200: {//업로드 버튼 클릭시
                if (data != null && data.getData() != null) {
                    file = data.getData();
                    StorageReference thisFileRef = profileImagesRef.child("/" + auth.getCurrentUser().getUid() + ".jpg");
                    if (!file.equals(null)) {
                        UploadTask uploadTask = thisFileRef.putFile(file);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                updateFile();
                                Toast.makeText(getContext(), "프로필 변경이 성공적으로 완료되었습니다.", Toast.LENGTH_LONG).show();
                                thisFileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        db.collection("users")
                                                .document(auth.getUid())
                                                .update("image", uri.toString())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("success", "db 업데이트 성공");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("fail", "db 업데이트 실패");
                                                    }
                                                });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "프로필 변경이 성공적으로 완료되지 못했습니다.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "파일을 감지하지 못했습니다.", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
    protected void updateFile() {
        Glide.with(getContext())
                .load(profileImagesRef.child("/"+auth.getCurrentUser().getUid()+".jpg"))
                .signature(new ObjectKey(System.currentTimeMillis()))
                .into(profile_image);
    }
}
