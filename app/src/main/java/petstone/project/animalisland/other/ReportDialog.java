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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import petstone.project.animalisland.R;

public class ReportDialog extends Dialog {

    TextView text;
    Button ok, cancel;
    EditText field;

    String nickname = "";
    String uid = "";

    public ReportDialog(@NonNull Context context, String nickname, String uid) {
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

        setContentView(R.layout.report_dialog);

        text = findViewById(R.id.report_dialog_text);
        ok = findViewById(R.id.report_dialog_ok);
        cancel = findViewById(R.id.report_dialog_cancel);
        field = findViewById(R.id.report_dialog_field);

        text.setText(nickname + "님을 다른 유저에게 신고하시겠습니까?\n(신뢰도가 감소합니다.\n경우에 따라 해당 사용자가 영구 정지될 수 있습니다.)");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uid != null) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    db.collection("users").document(uid).collection("report").document(auth.getUid())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(!documentSnapshot.exists()) {
                                HashMap<String, Object> data = new HashMap<>();
                                data.put("uid", auth.getUid());

                                FirebaseFirestore.getInstance().collection("users").document(uid).collection("report")
                                        .document(auth.getUid())
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("d", "호감도 감소 성공");
                                                Toast.makeText(getContext(), "호감도 감소가 성공했습니다.", Toast.LENGTH_SHORT).show();

                                                HashMap<String,Object> reportData = new HashMap<>();
                                                reportData.put("reportReason", field.getText().toString());
                                                reportData.put("whoReport", auth.getUid());

                                                db.collection("report").document(uid)
                                                        .set(reportData)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("d", "신고 성공");
                                                                dismiss();
                                                            }
                                                        });
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
