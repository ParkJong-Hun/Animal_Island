package petstone.project.animalisland.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import petstone.project.animalisland.R;
import petstone.project.animalisland.other.VPAdapter;

//채팅
public class ChatActivity extends AppCompatActivity {
    String whoUID = ""; //상대방 uid
    //TODO: 상대방 UID를 이전 화면에서 가져오기

    ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
      
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
