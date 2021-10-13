package petstone.project.animalisland.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import petstone.project.animalisland.R;

//회원가입
public class RegisterActivity extends AppCompatActivity {
    //선언부
    FirebaseAuth auth;
    FirebaseFirestore db;

    //회원가입 버튼
    Button btn_register;

    //이름
    EditText et_name;
    String name = "";

    //아이디
    EditText et_id;
    String id = "";
    //아이디 중복확인
    Button validateButton;
    Boolean validate_id = false;

    //패스워드
    EditText et_pw;
    String password = "";
    //패스워드 중복확인 입력
    EditText et_pwck;
    String password_check = "";

    //닉네임
    EditText et_nickname;
    String nickname = "";
    //닉네임 중복확인
    Button validateButton2;
    Boolean validate_nickname = false;

    //성별
    //EditText et_sex;
    RadioGroup radio_sex;
    Sex sex = Sex.male;
    enum Sex {
        male, female, interSex
    }

    //나이
    EditText et_age;
    Date birth = new Date();
    //생일 calendar
    Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);

    //이메일
    EditText et_mail;
    String email = "";

    //UID
    String uid = "";

    //화면 초기화
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_create);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btn_register = findViewById(R.id.btn_register);
        et_id = findViewById(R.id.et_id);
        validateButton = findViewById(R.id.validateButton);
        et_pw = findViewById(R.id.et_pw);
        et_pwck = findViewById(R.id.et_pwck);
        et_name = findViewById(R.id.et_name);
        validateButton2 = findViewById(R.id.validateButton2);
        // et_sex = findViewById(R.id.et_sex);
        radio_sex = findViewById(R.id.radio_sex);
        et_age = findViewById(R.id.et_age);
        et_mail = findViewById(R.id.et_mail);
        et_nickname = findViewById(R.id.et_nickname);


        //아이디 중복 확인 버튼 클릭 리스너
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //입력한 데이터 가져오기
                id = et_id.getText().toString();

                //파이어베이스에서 회원 컬렉션에서 입력한 id와 일치하는 필드를 갖는 문서가 있는지 질의
                final CollectionReference usersRef = db.collection("users");
                Query query = usersRef.whereEqualTo("id", id);
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().isEmpty() && !(id.isEmpty()) && id.length() >= 6) {
                            //없으면 참으로 변경
                            validate_id = true;
                            et_id.setTextColor(Color.GREEN);
                            Toast.makeText(RegisterActivity.this, "사용 가능한 아이디입니다.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (id.isEmpty()) {
                            //비면 거짓으로 변경
                            Log.d("fail", queryDocumentSnapshots.getDocuments().toString());
                            validate_id = false;
                            et_id.setTextColor(Color.RED);
                            Toast.makeText(RegisterActivity.this, "아이디를 정확히 입력해주세요.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (id.length() < 6) {
                            //길이가 작으면 거짓으로 변경
                            validate_id = false;
                            et_id.setTextColor(Color.RED);
                            Toast.makeText(RegisterActivity.this, "아이디를 6자 이상 입력해주세요.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //있으면 거짓으로 변경
                            Log.d("fail", queryDocumentSnapshots.getDocuments().toString());
                            validate_id = false;
                            et_id.setTextColor(Color.RED);
                            Toast.makeText(RegisterActivity.this, "이미 동일한 아이디가 존재합니다.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //닉네임 중복 확인 버튼 클릭 리스너
        validateButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //입력한 데이터 가져오기
                nickname = et_nickname.getText().toString();

                //파이어베이스에서 회원 컬렉션에서 입력한 id와 일치하는 필드를 갖는 문서가 있는지 질의
                final CollectionReference usersRef = db.collection("users");
                Query query = usersRef.whereEqualTo("nickname", nickname);
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().isEmpty() && !(nickname.isEmpty())) {
                            //없으면 참으로 변경
                            validate_nickname = true;
                            et_nickname.setTextColor(Color.GREEN);
                            Toast.makeText(RegisterActivity.this, "사용 가능한 닉네임입니다.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (nickname.isEmpty()) {
                            //비면 거짓으로 변경
                            validate_nickname = false;
                            et_nickname.setTextColor(Color.RED);
                            Toast.makeText(RegisterActivity.this, "닉네임을 정확히 입력해주세요.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //있으면 거짓으로 변경
                            Log.d("fail", queryDocumentSnapshots.getDocuments().toString());
                            validate_nickname = false;
                            et_nickname.setTextColor(Color.RED);
                            Toast.makeText(RegisterActivity.this, "이미 동일한 닉네임이 존재합니다.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //회원가입 버튼 클릭 리스너
        btn_register.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                //데이터 가져오기
                try {
                    name = et_name.getText().toString();
                    id = et_id.getText().toString();
                    password = et_pw.getText().toString();
                    password_check = et_pwck.getText().toString();
                    nickname = et_nickname.getText().toString();
                    birth = c.getTime();
                    email = et_mail.getText().toString();
                    if (radio_sex.getCheckedRadioButtonId() == 1) {
                        sex = Sex.female;
                    } else if (radio_sex.getCheckedRadioButtonId() == 2) {
                        sex = Sex.male;
                    } else {
                        sex = Sex.interSex;
                    }
                    //로그인 정보 일치
                    if (password.equals(password_check) && email.contains("@") && validate_id && validate_nickname && (password.length() >= 8) && (password.length() <= 20) && (id.length() >= 6)) {
                        //로그인 정보 일치 O
                        //회원 생성
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            //회원가입 성공
                                            Log.d("Success", "createUserWithEmail:success");
                                            
                                            //회원가입을 위해 UID 저장하기
                                            uid = auth.getCurrentUser().getUid();
                                            
                                            //여러 정보를 담기 위한 유저 객체 생성
                                            Map<String, Object> user = new HashMap<>();
                                            user.put("name", name);
                                            user.put("id", id);
                                            user.put("password", password);
                                            user.put("nickname", nickname);
                                            user.put("sex", sex.toString());
                                            user.put("birth", birth);
                                            user.put("email", email);
                                            user.put("uid", uid);
                                            user.put("address", null);
                                            user.put("sell_permission", false);
                                            user.put("is_petfriend", false);
                                            user.put("image", null);

                                            Map<String, Object> popularity = new HashMap<>();

                                            //데이터베이스에 추가
                                            db.collection("users").document(uid)
                                                    .set(user)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            //데이터 베이스 저장 성공
                                                            Log.d("Success", "회원 데이터베이스 저장 성공");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            //데이터 베이스 저장 실패
                                                            Log.d("Fail", "회원 데이터베이스 저장 실패");
                                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                            user.delete()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()) {
                                                                                Log.d(null, "로그인 정보 삭제");
                                                                            }
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Log.w(null, "로그인 정보 삭제");
                                                                        }
                                                                    });
                                                        }
                                                    });

                                            Map<String, String> whoLoveYou = new HashMap<>();
                                            whoLoveYou.put("uid", uid);

                                            db.collection("users").document(uid).collection("popularity").document(uid)
                                                    .set(whoLoveYou)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("Success", "호감도 컬렉션 생성 성공");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w("fail", "호감도 컬렉션 생성 실패");
                                                        }
                                                    });

                                            //로그인 화면으로 이동
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        } else {
                                            //회원가입 실패
                                            Log.w("Fail", "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(RegisterActivity.this, "이미 동일한 아이디가 존재합니다.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        //로그인 정보 일치 X
                        if (password.length() < 8 || password.length() > 20) {
                            Toast.makeText(RegisterActivity.this, "비밀번호를 8자 이상 20자 이하 입력해주세요.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (email.contains("@")) {
                            Toast.makeText(RegisterActivity.this, "이메일 형식을 확인해주세요.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "내용을 정확히 입력해주세요.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        Log.d("incorrectFail", "ID: " + id + "\nPassword: " + password + "\nBirthday: " + birth.toString() + "\nSex: " +  sex + "\nNickname: " +  nickname + "\nEmail: " + email);
                    }
                } catch (Exception e) {
                    //무언가 하나라도 빈칸이면 회원가입 실패
                    Toast.makeText(RegisterActivity.this, "내용을 정확히 입력해주세요.",
                            Toast.LENGTH_SHORT).show();
                    Log.d("exceptionFail", "ID: " + id + "\nPassword: " + password + "\nBirthday: " + birth.toString() + "\nSex: " +  sex + "\nNickname: " +  nickname + "\nEmail: " + email);
                }
            }
        });

        DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth ,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                et_age.setText(year + "년 " + (month+1) + "월 " + dayOfMonth + "일");
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month + 1);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        et_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    datePickerDialog.show();
            }
        });

    }
}

