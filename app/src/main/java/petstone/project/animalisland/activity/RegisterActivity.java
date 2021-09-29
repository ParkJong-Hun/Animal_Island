package petstone.project.animalisland.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

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
    EditText et_name;
    String nickname = "";
    //닉네임 중복확인
    Button validateButton2;
    Boolean validate_nickname = false;

    //성별
    EditText et_sex;
    Sex sex = Sex.male;
    enum Sex {
        male, female
    }

    //나이
    EditText et_age;
    int age = 0;

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
        et_sex = findViewById(R.id.et_sex);
        et_age = findViewById(R.id.et_age);
        et_mail = findViewById(R.id.et_mail);

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
                        if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                            //없으면 참으로 변경
                            validate_id = true;
                            Toast.makeText(RegisterActivity.this, "사용 가능한 아이디입니다.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //있으면 거짓으로 변경
                            Log.d("fail", queryDocumentSnapshots.getDocuments().toString());
                            validate_id = false;
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
                nickname = et_name.getText().toString();

                //파이어베이스에서 회원 컬렉션에서 입력한 id와 일치하는 필드를 갖는 문서가 있는지 질의
                final CollectionReference usersRef = db.collection("users");
                Query query = usersRef.whereEqualTo("nickname", nickname);
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                            //없으면 참으로 변경
                            validate_nickname = true;
                            Toast.makeText(RegisterActivity.this, "사용 가능한 닉네임입니다.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //있으면 거짓으로 변경
                            Log.d("fail", queryDocumentSnapshots.getDocuments().toString());
                            validate_nickname = false;
                            Toast.makeText(RegisterActivity.this, "이미 동일한 닉네임이 존재합니다.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //회원가입 버튼 클릭 리스너
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 가져오기
                try {
                    id = et_id.getText().toString();
                    password = et_pw.getText().toString();
                    password_check = et_pwck.getText().toString();
                    nickname = et_name.getText().toString();
                    //TODO: 성별 아직 안함
                    //sex = ;
                    age = Integer.parseInt(et_age.getText().toString());
                    email = et_mail.getText().toString();
                    //로그인 정보 일치
                    if (password.equals(password_check) && email.contains("@") && validate_id && validate_nickname) {
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
                                            user.put("id", id);
                                            user.put("password", password);
                                            user.put("nickname", nickname);
                                            user.put("sex", Sex.male);
                                            user.put("age", age);
                                            user.put("email", email);
                                            user.put("uid", uid);
                                            user.put("name", null);
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
                        Toast.makeText(RegisterActivity.this, "내용을 정확히 입력해주세요.",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    //무언가 하나라도 빈칸이면 회원가입 실패
                    Toast.makeText(RegisterActivity.this, "내용을 정확히 입력해주세요.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
