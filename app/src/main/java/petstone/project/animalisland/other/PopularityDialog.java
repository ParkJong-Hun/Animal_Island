package petstone.project.animalisland.other;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import petstone.project.animalisland.R;

public class PopularityDialog extends Dialog {

    TextView text;
    Button ok, cancel;

    String nickname = "";
    String uid = "";

    public PopularityDialog(@NonNull Context context, String nickname, String uid) {
        super(context);
        this.nickname = nickname;
        this.uid = uid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //꼭짓점 잘리는 거 방지
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.popularity_dialog);

        text = findViewById(R.id.popularity_dialog_text);
        ok = findViewById(R.id.popularity_dialog_ok);
        cancel = findViewById(R.id.popularity_dialog_cancel);

        text.setText(nickname + "님을 다른 유저에게 추천하시겠습니까?\n(호감도 증가합니다.)");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uid != null) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    db.collection("users").document(uid).collection("popularity").document(auth.getUid())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(!documentSnapshot.exists()) {
                                HashMap<String, Object> data = new HashMap<>();
                                data.put("uid", auth.getUid());

                                FirebaseFirestore.getInstance().collection("users").document(uid).collection("popularity")
                                        .document(auth.getUid())
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("d", "호감도 증가 성공");
                                                Toast.makeText(getContext(), "호감도 증가가 성공했습니다.", Toast.LENGTH_SHORT).show();
                                                dismiss();
                                            }
                                        });
                            }
                        }
                    });
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
