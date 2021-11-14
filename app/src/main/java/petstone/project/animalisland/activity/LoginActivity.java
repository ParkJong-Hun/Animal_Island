package petstone.project.animalisland.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import petstone.project.animalisland.R;

//로그인
public class LoginActivity extends AppCompatActivity {

    //선언부
    //파이어베이스 인증 인스턴스
    private FirebaseAuth auth;
    FirebaseFirestore db;

    Button login_button;
    Button user_create_button;
    EditText id_editText;
    EditText password_editText;

    String id = "";
    String password = "";
    String email = "";

    //화면 초기화
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //파이어베이스 인증 인스턴스 초기화
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //바인딩
        login_button = findViewById(R.id.login_button);
        user_create_button = findViewById(R.id.user_create_button);
        id_editText = findViewById(R.id.login_id_et);
        password_editText = findViewById(R.id.login_pw_et);

        //로그인 버튼
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = id_editText.getText().toString();
                password = password_editText.getText().toString();
                try {
                    db.collection("users")
                            .whereEqualTo("id", id)
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                Log.d("success", doc.getString("email"));
                                email = doc.getString("email");
                            }
                        }
                    });

                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //로그인 정보 일치
                                        Log.d("Success", "이메일로 로그인:success");
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("activity", "login");
                                        startActivity(intent);
                                    } else {
                                        //로그인 정보 불일치
                                        Log.w("Fail", "이메일로 로그인:failure", task.getException());
                                        Toast.makeText(getApplicationContext(), "아이디 혹은 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 모두 작성해주십시오.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //회원가입 버튼
        user_create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    //안드로이드 사이클 create -> start일 때
    @Override
    public void onStart() {
        super.onStart();
        //현재 로그인되어 있는지(사용자 이름이 비어있는지) 체크
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null) {
            Log.d("Success", "자동 로그인됨: " + currentUser.getEmail());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("activity", "login");
            startActivity(intent);
        }
    }
}