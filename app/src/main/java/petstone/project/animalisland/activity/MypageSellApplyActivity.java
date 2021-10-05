package petstone.project.animalisland.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import petstone.project.animalisland.R;

//마이페이지 유료 분양 권한 신청
public class MypageSellApplyActivity extends AppCompatActivity {

    Button cancel, submit;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_sell_apply);

        cancel = findViewById(R.id.mypage_sell_apply_cancel);
        submit = findViewById(R.id.mypage_sell_apply_submit);
        back = findViewById(R.id.back_mypage_sell_apply);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        //뒤로가기
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}