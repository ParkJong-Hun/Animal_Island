package petstone.project.animalisland.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<< HEAD:app/src/main/java/petstone/project/animalisland/RegisterActivity.java
import petstone.project.animalisland.activity.MainActivity;
=======
import petstone.project.animalisland.R;
>>>>>>> 606206004206bda025175208b1ab87dd2811ed38:app/src/main/java/petstone/project/animalisland/activity/RegisterActivity.java

public class RegisterActivity extends AppCompatActivity {
    //선언부
    Button btn_register;

    //화면 초기화
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_create);

        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
