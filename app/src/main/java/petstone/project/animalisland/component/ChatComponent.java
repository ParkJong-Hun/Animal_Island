package petstone.project.animalisland.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import petstone.project.animalisland.R;
import petstone.project.animalisland.other.ChatAdapter;

public class ChatComponent extends Fragment {
    ListView c_ListView;
    ChatAdapter c_Adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chatlist_component, container, false);
        return rootView;
    }
}
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View chat_view = inflater.inflate(R.layout.chatlist_component, container, false);

        c_Adapter = new ChatComponent();
        c_ListView.setAdapter(c_Adapter);

        // 커스텀 어댑터 생성
        c_Adapter = new ChatComponent();

        // Xml에서 추가한 ListView 연결
        c_ListView = (ListView) c_ListView.findViewById(R.id.chat_lv1);

        // ListView에 어댑터 연결
        c_ListView.setAdapter(c_Adapter);
        c_ArrayList = new ArrayList<>();

        c_ArrayList.add(Integer.parseInt("펫 프렌즈 보고 연락드려요", 0));
        c_ArrayList.add(Integer.parseInt("날짜와 시간, 장소 알려주세요", 1));
        c_ArrayList.add(Integer.parseInt("25일 성북구청 가능할까요?", 0));
        c_ArrayList.add(Integer.parseInt("네 가능합니다 시간 알려주시겠어요?", 1));
        c_ArrayList.add(Integer.parseInt("11시에서 3시정도 괜찮으신가요?", 0));
        c_ArrayList.add(Integer.parseInt("네 가능합니다. 25일에 다시 연락 바랄게요", 1));
        c_ArrayList.add(Integer.parseInt("2021/05/25", 2));
        c_ArrayList.add(Integer.parseInt("11시에 잘 부탁드릴게요", 0));
        c_ArrayList.add(Integer.parseInt("네 알겠습니다", 0));
        c_ArrayList.add(Integer.parseInt("감사합니다~", 1));

        return chat_view;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chatlist_component, container, false);
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
    } */