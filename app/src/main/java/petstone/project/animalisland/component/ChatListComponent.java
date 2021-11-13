package petstone.project.animalisland.component;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.ChatActivity;
import petstone.project.animalisland.other.ChatListAdapter;
import petstone.project.animalisland.other.ChatList;
import petstone.project.animalisland.other.PopularityDialog;

public class ChatListComponent extends Fragment {

    ListView listView;
    ChatListAdapter listAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    ArrayList<ChatList> lists = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chatlist_component, container, false);

        listView = rootView.findViewById(R.id.chat_lv1);
        lists.clear();

        //채팅 입장
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("", lists.toString());
                String whoUID = lists.get(position).getUid();
                String documentName = "";

                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("whoUID", whoUID);

                if(whoUID.compareTo(auth.getUid()) > 0) {
                    documentName = whoUID + "_" + auth.getUid();
                } else {
                    documentName = auth.getUid() + "_" + whoUID;
                }

                intent.putExtra("chatName", documentName);
                startActivity(intent);
            }
        });

        //채팅 리스트 길게 누르기(채팅 삭제 및 신뢰도 증가 다이얼로그 표시)
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu menu = new PopupMenu(getContext(), view);
                PopularityDialog dialog = new PopularityDialog(getContext(), lists.get(position).getWhoName(), lists.get(position).getUid());
                menu.getMenuInflater().inflate(R.menu.chat_list_menu, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.chat_list_menu_delete:
                                String whoUID = lists.get(position).getUid();
                                String documentName = "";
                                if(whoUID.compareTo(auth.getUid()) > 0) {
                                    documentName = whoUID + "_" + auth.getUid();
                                } else {
                                    documentName = auth.getUid() + "_" + whoUID;
                                }
                                HashMap<String, Object> data = new HashMap<>();
                                data.put("uid", auth.getUid());
                                data.put("date", new Date());
                                data.put("article", "정중히 인사하고 퇴장합니다.\n채팅이 종료됩니다.");
                                data.put("readed", 1);

                                String finalDocumentName = documentName;
                                db.collection("chats").document(documentName).collection("messages")
                                        .document(new Date() + auth.getUid())
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                db.collection("chats").document(finalDocumentName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("d", "채팅 삭제 완료");
                                                        listAdapter.notifyDataSetChanged();
                                                    }
                                                });
                                            }
                                        });
                                break;
                            case R.id.chat_list_menu_recommend:
                                dialog.show();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                menu.show();

                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db.collection("chats")
                .whereEqualTo("uid", auth.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        lists.clear();
                        Log.d("", "초기화됨");

                        for (DocumentSnapshot doc : value) {
                            ChatList newList = new ChatList();
                            newList.setUid(doc.getString("uid2"));
                            lists.add(newList);
                        }
                        listAdapter = new ChatListAdapter(getContext(), lists);
                        listView.setAdapter(listAdapter);

                        db.collection("chats")
                                .whereEqualTo("uid2", auth.getUid())
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                        for (DocumentSnapshot doc : value) {
                                            ChatList newList = new ChatList();
                                            newList.setUid(doc.getString("uid"));
                                            lists.add(newList);
                                        }
                                        listAdapter.notifyDataSetChanged();

                                        for (ChatList list : lists) {
                                            db.collection("users")
                                                    .whereEqualTo("uid", list.getUid())
                                                    .get()
                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                            for (DocumentSnapshot doc:queryDocumentSnapshots) {
                                                                list.setWhoName(doc.getString("nickname"));
                                                                listAdapter.notifyDataSetChanged();
                                                                break;
                                                            }
                                                        }
                                                    });

                                            db.collection("chats")
                                                    .whereEqualTo("uid", list.getUid())
                                                    .get()
                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                                                doc.getReference().collection("messages")
                                                                        .whereNotEqualTo("uid", auth.getUid())
                                                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                                                list.setNewCount(0);
                                                                                for (DocumentSnapshot doc:value) {
                                                                                    if (doc.getLong("readed") == 1) {
                                                                                        list.setNewCount(list.getNewCount() + 1);
                                                                                    }
                                                                                    list.setUpdatedDate(doc.getDate("date"));
                                                                                    list.setUpdatedMessage(doc.getString("article"));
                                                                                }
                                                                                listAdapter.notifyDataSetChanged();
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });

                                            db.collection("chats")
                                                    .whereEqualTo("uid2", list.getUid())
                                                    .get()
                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                                                doc.getReference().collection("messages")
                                                                        .whereNotEqualTo("uid", auth.getUid())
                                                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                                                list.setNewCount(0);
                                                                                for (DocumentSnapshot doc:value) {
                                                                                    if (doc.getLong("readed") == 1) {
                                                                                        list.setNewCount(list.getNewCount() + 1);
                                                                                    }
                                                                                    list.setUpdatedDate(doc.getDate("date"));
                                                                                    list.setUpdatedMessage(doc.getString("article"));
                                                                                }
                                                                                listAdapter.notifyDataSetChanged();
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                });
    }
}