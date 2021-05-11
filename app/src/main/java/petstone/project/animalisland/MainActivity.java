package petstone.project.animalisland;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

//메인
public class MainActivity extends AppCompatActivity {

    //선언부

    //화면 초기화
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    //하단 탭 선택시 표시되는 창
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.bottom_home:
            {
                //home.xml로 이동
                return true;
            }
            case R.id.bottom_petfriend:
            {
                //petfreind.xml로 이동
                return true;
            }
            case R.id.bottom_chat:
            {
                //chat.xml로 이동
                return true;
            }
            case R.id.bottom_mypage:
            {
                //mypage.xml로 이동
                return true;
            }
        }
        return false;
    }

}