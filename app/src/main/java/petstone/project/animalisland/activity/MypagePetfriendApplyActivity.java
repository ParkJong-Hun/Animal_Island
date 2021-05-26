package petstone.project.animalisland.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import petstone.project.animalisland.R;

public class MypagePetfriendApplyActivity extends AppCompatActivity {

    Button cancel, submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_petfriend_apply);

        cancel = findViewById(R.id.mypage_petfriend_apply_cancel);
        submit = findViewById(R.id.mypage_petfriend_apply_submit);

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
    }
}
