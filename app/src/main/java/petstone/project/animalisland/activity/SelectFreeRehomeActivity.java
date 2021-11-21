package petstone.project.animalisland.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import petstone.project.animalisland.R;
import petstone.project.animalisland.other.FreeRecycleAdapter;
import petstone.project.animalisland.other.FreeSelectImageAdapter;

public class SelectFreeRehomeActivity extends AppCompatActivity {

    RecyclerView select_img;
    FreeSelectImageAdapter adapter;
    ArrayList<Uri> storageList = new ArrayList<>();

    ImageView back, profile, delete;
    Button chat;
    TextView select_animal_type, select_breed, select_local, select_title, select_age, select_gender, select_neuter, select_inoculation, select_content;
    TextView name;
    RatingBar rating;
    String document_id;

    String uid, mMyUid;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference ImgRef, postImgRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_free_rehome);

        Intent intent = getIntent();
        document_id = intent.getStringExtra("document_id");

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        ImgRef = storage.getReference("PostImg");

        back = findViewById(R.id.back);
        profile = findViewById(R.id.profile);
        delete = findViewById(R.id.delete);

        chat = findViewById(R.id.btn_chat);

        select_img = findViewById(R.id.select_img);
        select_animal_type = findViewById(R.id.select_animal_type);
        select_breed = findViewById(R.id.select_breed);
        select_local = findViewById(R.id.select_local);
        select_title = findViewById(R.id.select_title);
        select_age = findViewById(R.id.select_age);
        select_gender = findViewById(R.id.select_gender);
        select_neuter = findViewById(R.id.select_neuter);
        select_inoculation = findViewById(R.id.select_inoculation);
        select_content = findViewById(R.id.select_content);

        name = findViewById(R.id.name);
        rating = findViewById(R.id.rating);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mMyUid = user.getUid();

        //게시글 보여주기
        db.collection("sale_posts")
                .whereEqualTo("document_id", document_id)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot document : value) {
                            select_animal_type.setText("[" + document.getData().get("animal_type").toString() + "]");
                            select_breed.setText(document.getData().get("animal_breed").toString());
                            select_local.setText(document.getData().get("district").toString());
                            select_title.setText(document.getData().get("title").toString());
                            select_age.setText("생년월일\n" + document.getData().get("birth").toString());
                            select_gender.setText("성별\n" + document.getData().get("sex").toString());
                            select_neuter.setText("중성화\n" + document.getData().get("is_neutralized").toString());
                            select_inoculation.setText("접종\n" + document.getData().get("is_inoculated").toString());
                            select_content.setText(document.getData().get("article").toString());

                            uid = document.getData().get("uid").toString();

                            //현재 사용자 uid와 게시글 uid 같을 경우 delete 활성화 & 채팅 버튼 없애기
                            if (mMyUid.equals(uid)) {
                                delete.setVisibility(View.VISIBLE);
                                chat.setVisibility(View.INVISIBLE);
                            } else
                                delete.setVisibility(View.INVISIBLE);

                            //사용자 정보 보여주기
                            db.collection("users")
                                    .whereEqualTo("uid", uid)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            for (DocumentSnapshot document : value) {
                                                name.setText(document.getData().get("nickname").toString());

                                                try {
                                                    URL url = new URL((String) document.get("image"));
                                                    Uri uri = Uri.parse(url.toURI().toString());
                                                    Glide.with(getApplicationContext())
                                                            .load(uri)
                                                            .into(profile);
                                                } catch (Exception e) {
                                                }
                                            }
                                        }
                                    });

                            db.collection("users").document(uid)
                                    .collection("popularity")
                                    .whereNotEqualTo("uid", uid)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            //한 사람 추천당 0.3 +
                                            //권한 없으면 80이 최대
                                            rating.setRating(2.5f);
                                            rating.setRating(rating.getRating() + 0.3f * value.getDocuments().size());
                                            db.collection("users").document(uid)
                                                    .collection("report")
                                                    .whereNotEqualTo("uid", uid)
                                                    .get()
                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                            //한 사람 신고당 0.3 -
                                                            //권한 없으면 80이 최대
                                                            rating.setRating(rating.getRating() - 0.3f * queryDocumentSnapshots.getDocuments().size());
                                                            db.collection("users").document(uid)
                                                                    .get()
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                            if (rating.getRating() > 4.0f && !((boolean) documentSnapshot.get("sell_permission") || (boolean) documentSnapshot.get("is_petfriend"))) {
                                                                                rating.setRating(4.0f);
                                                                            } else if ((boolean) documentSnapshot.get("sell_permission") || (boolean) documentSnapshot.get("is_petfriend")) {
                                                                                rating.setRating(rating.getRating() + 1.0f);
                                                                            } else if (rating.getRating() > 5.0f) {
                                                                                rating.setRating(5.0f);
                                                                            } else if (rating.getRating() < 0) {
                                                                                rating.setRating(0);
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    });
                                        }
                                    });
                        }
                    }
                });


        //스토리지에서 이미지 가져와서 리사이클러뷰에 보여주기
        postImgRef = ImgRef.child(document_id);
        postImgRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            item.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        storageList.add(task.getResult());

                                        adapter = new FreeSelectImageAdapter(storageList, getApplicationContext());
                                        select_img.setAdapter(adapter);
                                        select_img.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
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

        //게시글 삭제
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void PostDelete(){
        db.collection("sale_posts")
                .document(document_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //게시글 삭제되면 이미지 스토리지도 삭제
                        for(int i=1; i<=storageList.size(); i++){
                            postImgRef.child("img"+i).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    void showDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(SelectFreeRehomeActivity.this).setMessage("게시글 삭제하시겠습니까?").setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PostDelete();
                finish();
            }
        }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
}
