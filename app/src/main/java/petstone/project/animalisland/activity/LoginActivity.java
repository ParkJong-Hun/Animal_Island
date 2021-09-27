package petstone.project.animalisland.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import petstone.project.animalisland.R;

//메인
public class LoginActivity extends AppCompatActivity {

    //선언부
    //파이어베이스 인증 인스턴스
    private FirebaseAuth auth;

    Button login_button;
    Button user_create_button;

    //화면 초기화
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //파이어베이스 인증 인스턴스 초기화
        auth = FirebaseAuth.getInstance();

        //바인딩
        login_button = findViewById(R.id.login_button);
        user_create_button = findViewById(R.id.user_create_button);

        //로그인 버튼
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) {
                    //로그인 정보 일치
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    //로그인 정보 불일치
                    Toast.makeText(getApplicationContext(), "아이디 혹은 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
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
            //로그인 안되어있으면 reload
            reload();
        }
    }

    //새로고침
    private void reload() {
        //새로고침하는 무언가 작성
    }
}