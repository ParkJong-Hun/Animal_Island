package petstone.project.animalisland.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import petstone.project.animalisland.R;
import petstone.project.animalisland.component.ChatListComponent;
import petstone.project.animalisland.component.MypageComponent;
import petstone.project.animalisland.component.PetFriendComponent;
import petstone.project.animalisland.component.RehomeComponent;

//메인
public class MainActivity extends AppCompatActivity {

    //선언부
    BottomNavigationView bottomNavi;
    RehomeComponent homeFrag;
    PetFriendComponent petFrag;
    ChatListComponent chatFrag;
    MypageComponent myFrag;
    String previousActivity = "";
    Boolean snackBarOff = false;

    //화면 초기화
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Intent intent = getIntent();
        try {
            previousActivity = intent.getStringExtra("activity");
            if (previousActivity.equals("login")) {
                Toast.makeText(this, "로그인이 성공적으로 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }

        bottomNavi = findViewById(R.id.bottom_tab);
        chatFrag = new ChatListComponent();
        homeFrag = new RehomeComponent();
        petFrag = new PetFriendComponent();
        myFrag = new MypageComponent();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,homeFrag).commitAllowingStateLoss();

        bottomNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, homeFrag).commitAllowingStateLoss();
                        return true;
                    case R.id.bottom_petfriend:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, petFrag).commitAllowingStateLoss();
                        return true;
                    case R.id.bottom_chat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, chatFrag).commitAllowingStateLoss();
                        StringBuffer buffer = new StringBuffer();
                        String data = null;
                        FileInputStream fis = null;
                        try {
                            fis = openFileInput("chatCheck.txt");
                            BufferedReader iReader = new BufferedReader(new InputStreamReader((fis)));

                            data = iReader.readLine();
                            if(data != null)
                            {
                                snackBarOff = true;
                            }
                            iReader.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String path = Environment.getExternalStorageDirectory() + "/chatCheck.txt";

                        try {
                            BufferedReader eReader = new BufferedReader(new FileReader(path));
                            data = eReader.readLine();
                            if(data != null)
                            {
                                snackBarOff = true;
                            }
                            eReader.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (!snackBarOff) {
                            Snackbar.make(findViewById(R.id.main_frame), "채팅 목록을 길게 누르면 채팅 삭제, 추천, 신고를 할 수 있습니다.", Snackbar.LENGTH_INDEFINITE).setAction("닫기", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FileOutputStream fos = null;
                                    try {
                                        fos = openFileOutput("chatCheck.txt", Context.MODE_PRIVATE);
                                        fos.write(1);
                                        fos.close();;
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).show();
                        }
                        return true;
                    case R.id.bottom_mypage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, myFrag).commitAllowingStateLoss();
                        return true;
                }
                return false;
            }
        });
    }
}