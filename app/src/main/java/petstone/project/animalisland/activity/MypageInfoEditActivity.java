package petstone.project.animalisland.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import petstone.project.animalisland.R;

public class MypageInfoEditActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_info_edit);// 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
    }
}
