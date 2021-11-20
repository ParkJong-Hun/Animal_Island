package petstone.project.animalisland.component;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.RehomeFreeSubmitActivity;
import petstone.project.animalisland.other.FreeRecycleAdapter;
import petstone.project.animalisland.other.FreeRehomeList;
import petstone.project.animalisland.other.PetfriendUser;
import petstone.project.animalisland.other.PetfriendUserList_CustomAdapter;

public class FreeRehomeComponent extends Fragment {
    FloatingActionButton free_submit;
    RecyclerView recyclerView;
    FreeRecycleAdapter frAdapter ;
    ArrayList<FreeRehomeList> mList = new ArrayList<FreeRehomeList>();
    ArrayList<FreeRehomeList> FilterList = new ArrayList<FreeRehomeList>();

    SearchView free_search;

    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference ImgRef, main_img;
    String s_animal_type, s_birth, s_local, s_date, s_did ;
    Drawable sex ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.free_rehome, container, false);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        ImgRef = storage.getReference("PostImg");

        free_submit = view.findViewById(R.id.free_submit);

        recyclerView = view.findViewById(R.id.free_recycler);
        free_search = view.findViewById(R.id.free_search_view);


        free_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                Search(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Search(s);
                return false;
            }
        });

        db.collection("sale_posts")
                .whereEqualTo("is_sell", "0")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot document : value) {
                            s_animal_type = "[" + document.getData().get("animal_type").toString() + "]";
                            s_date = "작성날짜 : " + document.getData().get("date").toString();
                            s_birth = "생년월일 : " + document.getData().get("birth").toString();
                            s_did = document.getData().get("document_id").toString();
                            s_local = "지역 : " + document.getData().get("district").toString();

                            if ((document.getData().get("sex").toString()).equals("암컷")){
                                sex = getResources().getDrawable(R.drawable.female);
                            }
                            else if((document.getData().get("sex").toString()).equals("수컷")){
                                sex = getResources().getDrawable(R.drawable.male);
                            }

                            StorageReference postImgRef = ImgRef.child(s_did);
                            main_img = postImgRef.child("img1");

                            recyclerView.removeAllViewsInLayout();

                            addItem(main_img,
                                    sex,
                                    s_animal_type,
                                    document.getData().get("animal_breed").toString(),
                                    s_birth,
                                    s_local,
                                    s_date,
                                    s_did) ;

                            frAdapter = new FreeRecycleAdapter(mList);
                            recyclerView.setAdapter(frAdapter);

                            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

                        }
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

    public void addItem(StorageReference main, Drawable gender, String type, String breed, String birth, String local, String date, String did) {
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


    public void Search(String text){
        String s_text = text;
        FilterList.clear();

        for(int i=0; i<mList.size(); i++) {
            if (mList.get(i).getLocal().toLowerCase().contains(s_text.toLowerCase())) {
                FilterList.add(mList.get(i));
            }
            else if(mList.get(i).getType().toLowerCase().contains(s_text.toLowerCase())){
                FilterList.add(mList.get(i));
            }
            else if(mList.get(i).getBreed().toLowerCase().contains(s_text.toLowerCase())){
                FilterList.add(mList.get(i));
            }
        }
        frAdapter.filterList(FilterList);
    }

}
