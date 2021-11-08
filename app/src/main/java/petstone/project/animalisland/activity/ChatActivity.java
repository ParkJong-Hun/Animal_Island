package petstone.project.animalisland.activity;

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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import petstone.project.animalisland.R;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
      
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
                    }
                });

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!input.getText().equals("")) {

                    Map<String, Object> message = new HashMap<>();
                    message.put("uid", uid);
                    message.put("readed", 1);
                    message.put("date", new Date());
                    message.put("article", input.getText());

                    db.collection("chats")
                            .document(chatName).collection("messages")
                            .document(new Date().toString() + uid)
                            .set(message)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("d", "메시지 전송 성공");
                                    input.setText("");
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
}
