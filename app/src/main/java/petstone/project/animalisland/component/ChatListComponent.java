package petstone.project.animalisland.component;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.ChatActivity;
import petstone.project.animalisland.other.ChatAdapter;

public class ChatListComponent extends Fragment {

    ListView list;
    ChatAdapter listAdapter;

    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> updatedMessages = new ArrayList<String>();
    ArrayList<Integer> updatedCounts = new ArrayList<Integer>();
    ArrayList<Timestamp> updatedAts = new ArrayList<Timestamp>();
    ArrayList<String> uids = new ArrayList<String>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chatlist_component, container, false);

        list = rootView.findViewById(R.id.chat_lv1);

        //TODO: 파이어베이스 로드 비동기 해결하기
        loadData();
        listAdapter = new ChatAdapter(getContext(), names, updatedMessages, updatedCounts, updatedAts);
        list.setAdapter(listAdapter);
        Log.d("d", names + "\n" + updatedMessages + "\n" + updatedCounts + "\n" + updatedAts + "\n");


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("whoUID", uids);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public interface Callback {
        void success(String data);
    }

    public void loadData() {

    }
}