package petstone.project.animalisland.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import petstone.project.animalisland.R;

//마이페이지 정보 수정
public class MypageInfoEditActivity extends AppCompatActivity {

    Button cancel, edit;
    TextView name_tv;
    TextView id_tv;
    TextView birthday_tv;
    TextView sex_tv;
    TextView nickname_tv;
    EditText password, password_check;
    EditText email;
    TextView password_checked;
    boolean password_checked_switch = false;//비밀번호 일치하는지 판별
    boolean nickname_checked_switch = true;
    ImageView back;
    FirebaseAuth auth;
    FirebaseFirestore db;
    Button validate;
    TextView address;
    Button address_button;
    String beforeNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_info_edit);

        name_tv = findViewById(R.id.mypage_info_edit_name_form);
        id_tv = findViewById(R.id.mypage_info_edit_id_form);
        birthday_tv = findViewById(R.id.mypage_info_edit_age_form);
        sex_tv = findViewById(R.id.mypage_info_edit_sex_form);
        nickname_tv = findViewById(R.id.mypage_info_edit_nickname_form);
        back = findViewById(R.id.back_mypage_edit_info);
        cancel = findViewById(R.id.mypage_info_edit_cancel);
        edit = findViewById(R.id.mypage_info_edit_submit);
        password = findViewById(R.id.mypage_info_edit_password_form);
        password_check = findViewById(R.id.mypage_info_edit_password_check_form);
        password_checked = findViewById(R.id.mypage_info_edit_password_checked);
        email = findViewById(R.id.mypage_info_edit_email_form);
        validate = findViewById(R.id.mypage_info_edit_validateButton);
        address = findViewById(R.id.mypage_info_edit_address_text);
        address_button = findViewById(R.id.mypage_info_edit_address_button);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        db.collection("users")
                .whereEqualTo("uid", auth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d("success", "데이터베이스 로드 성공");
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            name_tv.setText(document.get("name").toString());
                            id_tv.setText(document.get("id").toString());
                            Timestamp birthday_timestamp = (Timestamp)document.get("birth");
                            Date birthday = birthday_timestamp.toDate();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년MM월dd일");
                            String birthday_str = dateFormat.format(birthday);
                            birthday_tv.setText(birthday_str);
                            email.setText(document.get("email").toString());
                            sex_tv.setText(document.get("sex").toString());
                            nickname_tv.setText(document.get("nickname").toString());
                            beforeNickname = document.get("nickname").toString();
                            if (document.get("address") != null) {
                                address.setText(document.get("address").toString());
                            } else {
                                address.setText("주소");
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("fail", "해당 데이터베이스가 존재하지 않습니다.");
                    }
                });


        //비밀번호 확인
        password_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                password_checked.setVisibility(View.INVISIBLE);
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(password.getText().toString().equals(password_check.getText().toString())&&!(password.getText().toString().equals(""))&&password.getText().length() >= 8) {
                    password_checked.setText("일치함");
                    password_checked.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    password_checked.setVisibility(View.VISIBLE);
                    password_checked_switch = true;
                } else if(!(password_check.getText().toString().equals(""))){
                    password_checked.setText("일치하지 않음");
                    password_checked.setTextColor(0xFFF44336);
                    password_checked.setVisibility(View.VISIBLE);
                    password_checked_switch = false;
                } else if(password_check.getText().toString().equals("")){
                    password_checked.setVisibility(View.INVISIBLE);
                    password_checked_switch = false;
                }
            }
        });

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users")
                        .whereEqualTo("nickname", nickname_tv.getText().toString())
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() > 0) {
                            if(nickname_tv.getText().toString().equals(beforeNickname)) {
                                nickname_checked_switch = true;
                                nickname_tv.setTextColor(Color.GREEN);
                                Toast.makeText(getApplicationContext(), "닉네임을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                nickname_checked_switch = false;
                                nickname_tv.setTextColor(Color.RED);
                                Toast.makeText(getApplicationContext(), "닉네임을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            nickname_checked_switch = true;
                            nickname_tv.setTextColor(Color.GREEN);
                            Toast.makeText(getApplicationContext(), "닉네임을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        
        //취소버튼
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        //수정버튼
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password_checked_switch && !(email.getText().toString().equals("")) && !(nickname_tv.getText().toString().equals("")) && nickname_checked_switch && !address.getText().equals("주소")) {
                    //이메일 형식 체크
                    String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(email.getText().toString());
                    if(m.matches()){
                        db.collection("users")
                                .document(auth.getUid())
                                .update("nickname", nickname_tv.getText().toString());
                        db.collection("users")
                                .document(auth.getUid())
                                .update("password", password.getText().toString());
                        db.collection("users")
                                .document(auth.getUid())
                                .update("address", address.getText().toString());
                        db.collection("petfriend")
                                .document(auth.getUid())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()) {
                                            documentSnapshot.getReference().update("nickname", nickname_tv.getText().toString())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("success", "펫프렌즈 닉네임 변경 성공");
                                                        }
                                                    });
                                        }
                                    }
                                });
                        auth.getCurrentUser().updatePassword(password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("success", "비밀번호 변경  성공");
                                } else {
                                    Log.d("fail", "비밀번호 변경 실패");
                                }
                            }
                        });
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "이메일이 옳바른 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "입력 정보가 정확하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //주소 검색
        address_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebviewActivity.class);
                startActivityForResult(intent, 0);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //웹 인텐트
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("data");
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                address.setText(result);
            }
        }
    }
}
