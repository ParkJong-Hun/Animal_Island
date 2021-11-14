package petstone.project.animalisland.component;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.RehomeFreeSubmitActivity;
import petstone.project.animalisland.other.FreeRecycleAdapter;
import petstone.project.animalisland.other.FreeRehomeList;

public class FreeRehomeComponent extends Fragment {
    FloatingActionButton free_submit;
    RecyclerView recyclerView = null ;
    FreeRecycleAdapter frAdapter = null ;
    ArrayList<FreeRehomeList> mList = new ArrayList<FreeRehomeList>();

    FirebaseFirestore db;
    String s_animal_type, s_birth, s_local, s_date, s_did ;
    Drawable sex ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.free_rehome, container, false);

        db = FirebaseFirestore.getInstance();

        free_submit = view.findViewById(R.id.free_submit);
        recyclerView = view.findViewById(R.id.free_recycler);
        frAdapter = new FreeRecycleAdapter(mList);
        recyclerView.setAdapter(frAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        frAdapter.notifyDataSetChanged();

        db.collection("sale_posts")
                .whereEqualTo("is_sell", "0")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            s_animal_type = "[" + document.getData().get("animal_type").toString() + "]";
                            s_date = "작성날짜 : " + document.getData().get("date").toString();
                            s_birth = "생년월일 : " + document.getData().get("birth").toString();
                            s_did = document.getData().get("document_id").toString();

                            if ((document.getData().get("sex").toString()).equals("암컷")){
                                sex = getResources().getDrawable(R.drawable.female);
                            }
                            else if((document.getData().get("sex").toString()).equals("수컷")){
                                sex = getResources().getDrawable(R.drawable.male);
                            }

                            addItem(getResources().getDrawable(R.drawable.image,null),
                                    sex,
                                    s_animal_type,
                                    document.getData().get("animal_breed").toString(),
                                    s_birth,
                                    "지역 : ",
                                    s_date,
                                    s_did) ;

                            frAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


        //무료 분양 게시글 등록버튼
        free_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RehomeFreeSubmitActivity.class);
                startActivityForResult(intent, 0 );
            }
        });

        return view;
    }

    public void addItem(Drawable main, Drawable gender, String type, String breed, String birth, String local, String date, String did) {
        FreeRehomeList item = new FreeRehomeList();

        item.setImg(main);
        item.setGenderImg(gender);
        item.setType(type);
        item.setBreed(breed);
        item.setBirth(birth);
        item.setLocal(local);
        item.setDate(date);
        item.setDid(did);

        mList.add(item);
    }

}
