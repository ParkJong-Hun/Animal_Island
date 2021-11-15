package petstone.project.animalisland.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

import petstone.project.animalisland.R;

public class SelectFreeRehomeActivity extends AppCompatActivity {

    ImageView back;
    TextView select_animal_type, select_breed, select_local, select_title, select_age, select_gender, select_neuter, select_inoculation, select_content;

    TextView name;
    String uid;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_free_rehome);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String document_id = intent.getStringExtra("document_id");

        back = findViewById(R.id.back);
        select_animal_type = findViewById(R.id.select_animal_type);
        select_breed = findViewById(R.id.select_breed);
        select_local = findViewById(R.id.select_local);
        select_title = findViewById(R.id.select_title);
        select_age = findViewById(R.id.select_age);
        select_gender = findViewById(R.id.select_gender);
        select_neuter = findViewById(R.id.select_neuter);
        select_inoculation = findViewById(R.id.select_inoculation);
        select_content = findViewById(R.id.select_content);

        name = findViewById(R.id.name);

        //게시글 보여주기
        db.collection("sale_posts")
                .whereEqualTo("document_id", document_id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d("success", "데이터베이스 로드 성공");
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            select_animal_type.setText("[" + document.getData().get("animal_type").toString() + "]");
                            select_breed.setText(document.getData().get("animal_breed").toString());
                            select_title.setText(document.getData().get("title").toString());
                            select_age.setText("생년월일\n" + document.getData().get("birth").toString());
                            select_gender.setText("성별\n" + document.getData().get("sex").toString());
                            select_neuter.setText("중성화\n" + document.getData().get("is_neutralized").toString());
                            select_inoculation.setText("접종\n" + document.getData().get("is_inoculated").toString());
                            select_content.setText(document.getData().get("article").toString());

                            uid = document.getData().get("uid").toString();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        //사용자 정보 보여주기
        db.collection("users")
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d("success", "데이터베이스 로드 성공");
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            name.setText(document.getData().get("nickname").toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
