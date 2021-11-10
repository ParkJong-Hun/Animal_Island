package petstone.project.animalisland.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import petstone.project.animalisland.R;
import petstone.project.animalisland.other.ChatMessage;
import petstone.project.animalisland.other.ChatMessageAdapter;
import petstone.project.animalisland.other.VPAdapter;

//채팅
public class ChatActivity extends AppCompatActivity {
    String whoUID = ""; //상대방 uid

    String uid = "";

    String chatName = "";

    ImageView back;
    TextView whoText;
    Button button;
    TextView input;
    RecyclerView chatMessage;
    RecyclerView.Adapter adapter;

    String myNickname = "1";
    String whoNickname = "1";

    ArrayList<ChatMessage> messages = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        Intent intent = getIntent();
        whoUID = intent.getStringExtra("whoUID");
        chatName = intent.getStringExtra("chatName");

        Log.d("whoUID", whoUID);
        Log.d("chatName", chatName);

        back = findViewById(R.id.back);
        whoText = findViewById(R.id.chat_who);
        button = findViewById(R.id.chat_button);
        input = findViewById(R.id.chat_et);
        chatMessage = findViewById(R.id.chat_message);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        uid = auth.getUid();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        chatMessage.setLayoutManager(linearLayoutManager);

        db.collection("users")
                .document(whoUID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        whoText.setText(documentSnapshot.getString("nickname"));
                        whoNickname = documentSnapshot.getString("nickname");

                        db.collection("users")
                                .document(uid)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        myNickname = documentSnapshot.getString("nickname");

                                        reading();
                                    }
                                });
                    }
                });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!input.getText().equals("")) {

                    Map<String, Object> message = new HashMap<>();
                    message.put("uid", uid);
                    message.put("readed", 1);
                    message.put("date", new Date());
                    message.put("article", input.getText().toString());

                    db.collection("chats")
                            .document(chatName).collection("messages")
                            .document(new Date().toString() + uid)
                            .set(message)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("d", "메시지 전송 성공");
                                    input.setText("");
                                    reading();
                                }
                            });
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void updateMessage() {
        messages.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chats").document(chatName).collection("messages")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots
                        ) {
                            ChatMessage newMessage = new ChatMessage();
                            newMessage.setArticle(document.get("article").toString());
                            newMessage.setDate(document.getTimestamp("date").toDate());
                            String readed = String.valueOf(document.get("readed"));
                            newMessage.setReaded(Integer.parseInt(readed));
                            newMessage.setNickname(document.get("uid").toString());

                            messages.add(newMessage);
                        }
                        Log.d("message", messages.toString());
                        adapter = new ChatMessageAdapter(messages, whoUID, uid, myNickname, whoNickname);
                        chatMessage.setAdapter(adapter);
                        chatMessage.scrollToPosition(adapter.getItemCount());
                    }
                });
    }

    void reading() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chats").document(chatName).collection("messages")
                .whereEqualTo("uid", whoUID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots
                        ) {
                            document.getReference().update("readed", 0).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            });
                        }
                        Log.d("d", "읽기 성공");
                    }
                });
        updateMessage();
    }
}
