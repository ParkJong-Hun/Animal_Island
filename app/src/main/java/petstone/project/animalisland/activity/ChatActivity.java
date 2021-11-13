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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
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
    LinearLayoutManager linearLayoutManager;
    ListenerRegistration registration;

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

        linearLayoutManager = new LinearLayoutManager(this);
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
                                        updateMessage();
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
                                    updateMessage();
                                }
                            });
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration.remove();
                finish();
            }
        });
    }

    void updateMessage() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        registration = db.collection("chats").document(chatName).collection("messages")
                .orderBy("date")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        messages.clear();
                        for (DocumentSnapshot document : value
                        ) {
                            ChatMessage newMessage = new ChatMessage();
                            newMessage.setArticle(document.get("article").toString());
                            newMessage.setDate(document.getTimestamp("date").toDate());
                            String readed = String.valueOf(document.get("readed"));
                            newMessage.setReaded(Integer.parseInt(readed));
                            newMessage.setNickname(document.get("uid").toString());
                            if(newMessage.getReaded() == 1 && newMessage.getUid().equals(whoUID)) {
                                document.getReference().update("readed", 0).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });
                            }
                            messages.add(newMessage);
                        }
                        adapter = new ChatMessageAdapter(messages, whoUID, uid, myNickname, whoNickname);
                        chatMessage.setAdapter(adapter);
                        linearLayoutManager.scrollToPosition(adapter.getItemCount() - 1);
                    }
                });
    }
}
