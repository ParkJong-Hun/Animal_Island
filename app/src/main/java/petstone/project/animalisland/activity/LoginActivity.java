package petstone.project.animalisland.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import petstone.project.animalisland.R;

//메인
public class LoginActivity extends AppCompatActivity {

    //선언부
    Button login_button;
    Button user_create_button;

    //화면 초기화
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        //바인딩
        login_button = findViewById(R.id.login_button);
        user_create_button = findViewById(R.id.user_create_button);

        //로그인 버튼
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
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
}