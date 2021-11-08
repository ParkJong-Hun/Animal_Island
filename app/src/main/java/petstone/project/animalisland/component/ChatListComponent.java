package petstone.project.animalisland.component;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.ChatActivity;
import petstone.project.animalisland.other.ChatAdapter;

public class ChatListComponent extends Fragment {

    ListView list;
    ChatAdapter listAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chatlist_component, container, false);

        list = rootView.findViewById(R.id.chat_lv1);
        ArrayList<String> uids = new ArrayList<String>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        db.collection("chats")
                .whereEqualTo("uid", auth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<String> names = new ArrayList<String>();
                        ArrayList<String> updatedMessages = new ArrayList<String>();
                        ArrayList<Integer> updatedCounts = new ArrayList<Integer>();
                        ArrayList<Timestamp> updatedAts = new ArrayList<Timestamp>();

                        for (DocumentSnapshot document : queryDocumentSnapshots
                             ) {
                            if (!document.getString("uid").equals(auth.getUid()))
                                db.collection("users")
                                        .document(document.getString("uid"))
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                names.add(documentSnapshot.getString("nickname"));
                                                uids.add(documentSnapshot.getString("uid"));
                                            }
                                        });
                            else {
                                db.collection("users")
                                        .document(document.getString("uid2"))
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                names.add(documentSnapshot.getString("nickname"));
                                                uids.add(documentSnapshot.getString("uid"));
                                            }
                                        });
                            }
                            document.getReference().collection("messages")
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                                            updatedMessages.add(documents.get(documents.size() - 1).getString("message"));
                                            updatedAts.add(documents.get(documents.size() - 1).getTimestamp("date"));
                                        }
                                    });
                            document.getReference().collection("messages")
                                    .whereEqualTo("readed", 1)
                                    .whereNotEqualTo("uid", auth.getUid())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            updatedCounts.add(queryDocumentSnapshots.getDocuments().size());
                                        }
                                    });
                        }
                        //TODO: 아마도 데이터 동기화 안될듯
                        listAdapter = new ChatAdapter(getContext(), names, updatedMessages, updatedCounts, updatedAts);
                        list.setAdapter(listAdapter);
                    }
                });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                //TODO: 상대방 uid 보내기
                intent.putExtra("whoUID", uids);
                startActivity(intent);
            }
        });

        return rootView;
    }
}