package petstone.project.animalisland.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import petstone.project.animalisland.R;


public class ChatActivity extends Activity {
    ListView c_ListView;
    ChatComponent c_Adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chatlist_component, container, false);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        // 커스텀 어댑터 생성
        c_Adapter = new ChatComponent();

        // Xml에서 추가한 ListView 연결
        c_ListView = (ListView) findViewById(R.id.chat_lv1);

        // ListView에 어댑터 연결
        c_ListView.setAdapter(c_Adapter);

        c_Adapter.add("펫 프렌즈 보고 연락드려요", 0);
        c_Adapter.add("날짜와 시간, 장소 알려주세요", 1);
        c_Adapter.add("25일 성북구청 가능할까요?", 0);
        c_Adapter.add("네 가능합니다 시간 알려주시겠어요?", 1);
        c_Adapter.add("11시에서 3시정도 괜찮으신가요?", 0);
        c_Adapter.add("네 가능합니다. 25일에 다시 연락 바랄게요", 1);
        c_Adapter.add("2021/05/25", 2);
        c_Adapter.add("11시에 잘 부탁드릴게요", 0);
        c_Adapter.add("네 알겠습니다", 0);
        c_Adapter.add("감사합니다~", 1);

    }

}