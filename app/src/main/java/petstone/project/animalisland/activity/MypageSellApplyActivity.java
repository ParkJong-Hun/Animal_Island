package petstone.project.animalisland.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import petstone.project.animalisland.R;

//마이페이지 유료 분양 권한 신청
public class MypageSellApplyActivity extends AppCompatActivity {

    Button cancel, submit;
    ImageView back;
    ImageView image1, image2, image3;
    EditText article;

    FirebaseStorage storage;
    StorageReference imageRef;
    Uri file;

    FirebaseFirestore db;
    FirebaseAuth auth;
    StorageReference thisFileRef;

    Boolean imageCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_sell_apply);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        cancel = findViewById(R.id.mypage_sell_apply_cancel);
        submit = findViewById(R.id.mypage_sell_apply_submit);
        back = findViewById(R.id.back_mypage_sell_apply);
        article = findViewById(R.id.mypage_sell_apply_article);
        image1 = findViewById(R.id.mypage_sell_apply_license_image1);
        image2 = findViewById(R.id.mypage_sell_apply_license_image2);
        image3 = findViewById(R.id.mypage_sell_apply_license_image3);

        storage = FirebaseStorage.getInstance();
        imageRef = storage.getReference("sellApplyImages");

        imageCheck = false;

        HashMap<String, Object> data = new HashMap<>();
        data.put("uid", auth.getUid());
        data.put("image1", null);
        data.put("image2", null);
        data.put("image3", null);

        db.collection("sell_apply")
                .document(auth.getUid())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("d", "생성됨");
                    }
                });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1 );
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2 );
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 3 );
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("sell_apply")
                        .document(auth.getUid())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("d", "신청 취소");
                            }
                        });
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(article.getText() != null && imageCheck) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "내용을 작성해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //뒤로가기
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("sell_apply")
                        .document(auth.getUid())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("d", "신청 취소");
                            }
                        });
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    void uploadFile(StorageReference ref, ImageView view) {
        Glide.with(getApplicationContext())
                .load(ref)
                .signature(new ObjectKey(System.currentTimeMillis()))
                .into(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getData() != null) {
            file = data.getData();
            if (!file.equals(null)) {
                switch (requestCode) {
                    case 1:
                    thisFileRef = imageRef.child("/" + auth.getCurrentUser().getUid() + "_1.jpg");
                    UploadTask uploadTask = thisFileRef.putFile(file);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploadFile(thisFileRef, image1);
                            Toast.makeText(getApplicationContext(), "사진이 업로드되었습니다.", Toast.LENGTH_LONG).show();
                            thisFileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    db.collection("sellApply")
                                            .document(auth.getUid())
                                            .update("image1", uri.toString())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("success", "db 업데이트 성공");
                                                    imageCheck = true;
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
                            Toast.makeText(getApplicationContext(), "사진 업로드가 성공적으로 완료되지 못했습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                    case 2:
                        thisFileRef = imageRef.child("/" + auth.getCurrentUser().getUid() + "_2.jpg");
                        UploadTask uploadTask2 = thisFileRef.putFile(file);
                        uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                uploadFile(thisFileRef, image2);
                                Toast.makeText(getApplicationContext(), "사진이 업로드되었습니다.", Toast.LENGTH_LONG).show();
                                thisFileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        db.collection("sellApply")
                                                .document(auth.getUid())
                                                .update("image2", uri.toString())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("success", "db 업데이트 성공");
                                                        imageCheck = true;
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
                                Toast.makeText(getApplicationContext(), "사진 업로드가 성공적으로 완료되지 못했습니다.", Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case 3:
                        thisFileRef = imageRef.child("/" + auth.getCurrentUser().getUid() + "_3.jpg");
                        UploadTask uploadTask3 = thisFileRef.putFile(file);
                        uploadTask3.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                uploadFile(thisFileRef, image3);
                                Toast.makeText(getApplicationContext(), "사진이 업로드되었습니다.", Toast.LENGTH_LONG).show();
                                thisFileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        db.collection("sellApply")
                                                .document(auth.getUid())
                                                .update("image3", uri.toString())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("success", "db 업데이트 성공");
                                                        imageCheck = true;
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
                                Toast.makeText(getApplicationContext(), "사진 업로드가 성공적으로 완료되지 못했습니다.", Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "파일을 감지하지 못했습니다.", Toast.LENGTH_LONG).show();
        }
    }
}