package petstone.project.animalisland.component;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.RehomeSellSubmitActivity;
import petstone.project.animalisland.other.SellRecycleAdapter;
import petstone.project.animalisland.other.SellRehomeList;

public class SellRehomeComponent extends Fragment {

    FirebaseAuth auth;
    FirebaseFirestore db;

    FloatingActionButton sell_submit;
    RecyclerView recyclerView = null ;
    SellRecycleAdapter srAdapter = null ;
    ArrayList<SellRehomeList> mList = new ArrayList<SellRehomeList>();

    String s_animal_type, s_birth, s_local, s_date, s_price, s_did ;
    Drawable sex ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sell_rehome, container, false);
        sell_submit = view.findViewById(R.id.sell_submit);
        recyclerView = view.findViewById(R.id.sell_recycler);
        srAdapter = new SellRecycleAdapter(mList);
        recyclerView.setAdapter(srAdapter);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        db.collection("sale_posts")
                .whereNotEqualTo("is_sell", "0")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d("success", "데이터베이스 로드 성공");
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            s_animal_type = "[" + document.getData().get("animal_type").toString() + "]";
                            s_date = "작성날짜 : " + document.getData().get("date").toString();
                            s_price = document.getData().get("is_sell").toString() + "원";
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
                                    s_price,
                                    s_did) ;

                            srAdapter.notifyDataSetChanged() ;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("fail", "해당 데이터베이스가 존재하지 않습니다.");
                    }
                });

        //유료분양 회원만 게시물 작성할 수 있도록
        db.collection("users")
                .whereEqualTo("uid", auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                            if ((boolean)document.get("sell_permission")) {
                                sell_submit.setVisibility(View.VISIBLE);
                            } else {
                                sell_submit.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });

        sell_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RehomeSellSubmitActivity.class);
                startActivityForResult(intent, 0 );
            }
        });

        return view;
    }

    public void addItem(Drawable main, Drawable gender, String type, String breed, String birth, String local, String date, String price, String did) {
        SellRehomeList item = new SellRehomeList();

        item.setImg(main);
        item.setGenderImg(gender);
        item.setType(type);
        item.setBreed(breed);
        item.setBirth(birth);
        item.setLocal(local);
        item.setDate(date);
        item.setPrice(price);
        item.setDid(did);

        mList.add(item);
    }
}
