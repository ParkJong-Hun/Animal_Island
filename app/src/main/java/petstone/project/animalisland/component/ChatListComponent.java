package petstone.project.animalisland.component;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.ChatActivity;
import petstone.project.animalisland.other.ChatListAdapter;
import petstone.project.animalisland.other.ChatList;

public class ChatListComponent extends Fragment {

    ListView listView;
    ChatListAdapter listAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    ArrayList<ChatList> lists = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chatlist_component, container, false);

        listView = rootView.findViewById(R.id.chat_lv1);

        db.collection("chats")
                .whereEqualTo("uid", auth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        CountDownLatch fullLatch = new CountDownLatch(queryDocumentSnapshots.size());
                        for (DocumentSnapshot document : queryDocumentSnapshots
                        ) {
                            ChatList list = new ChatList();
                            CountDownLatch latch = new CountDownLatch(3);

                            if (!document.getString("uid").equals(auth.getUid()))
                                db.collection("users")
                                        .document(document.getString("uid"))
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                list.setWhoName(documentSnapshot.getString("nickname"));
                                                list.setUid(documentSnapshot.getString("uid"));
                                                latch.countDown();
                                            }
                                        });
                            else {
                                db.collection("users")
                                        .document(document.getString("uid2"))
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                list.setWhoName(documentSnapshot.getString("nickname"));
                                                list.setUid(documentSnapshot.getString("uid"));
                                                latch.countDown();
                                            }
                                        });
                            }
                            document.getReference().collection("messages")
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                                            list.setUpdatedMessage(documents.get(documents.size() - 1).getString("message"));
                                            list.setUpdatedDate(documents.get(documents.size() - 1).getTimestamp("date").toDate());
                                            latch.countDown();
                                        }
                                    });
                            document.getReference().collection("messages")
                                    .whereNotEqualTo("uid", auth.getUid())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                                if(document.getLong("readed") == 1) {
                                                    list.setNewCount(list.getNewCount() + 1);
                                                }
                                            }
                                            latch.countDown();
                                        }
                                    });

                            try {
                                latch.await();
                                fullLatch.countDown();
                                lists.add(list);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            fullLatch.await();
                            Log.d("", lists.toString());
                            listAdapter = new ChatListAdapter(getContext(), lists);
                            listView.setAdapter(listAdapter);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });


        //채팅 입장
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("whoUID", lists.get(position).getUid());
                startActivity(intent);
            }
        });

        //채팅 리스트 길게 누르기(채팅 삭제 및 신뢰도 증가 다이얼로그 표시)
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        return rootView;
    }
}