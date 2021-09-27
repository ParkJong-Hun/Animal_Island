package petstone.project.animalisland.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import petstone.project.animalisland.R;

//마이페이지 펫프렌즈 권한 신청
public class MypagePetfriendApplyActivity extends AppCompatActivity {

    Button cancel, submit;
    ImageView back, license1, license2, license3;
    Switch toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_petfriend_apply);

        cancel = findViewById(R.id.mypage_petfriend_apply_cancel);
        submit = findViewById(R.id.mypage_petfriend_apply_submit);
        back = findViewById(R.id.back_mypage_petfriend_apply);
        toggle = findViewById(R.id.mypage_petfriend_apply_toggleButton);
        license1 = findViewById(R.id.mypage_petfriend_apply_license_image1);
        license2 = findViewById(R.id.mypage_petfriend_apply_license_image2);
        license3 = findViewById(R.id.mypage_petfriend_apply_license_image3);
        license1.setVisibility(View.GONE);
        license2.setVisibility(View.GONE);
        license3.setVisibility(View.GONE);

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

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    license1.setVisibility(View.VISIBLE);
                    license2.setVisibility(View.VISIBLE);
                    license3.setVisibility(View.VISIBLE);
                } else {
                    license1.setVisibility(View.GONE);
                    license2.setVisibility(View.GONE);
                    license3.setVisibility(View.GONE);

                }
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
}
