package petstone.project.animalisland.component;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.MypagePetfriendApplyActivity;
import petstone.project.animalisland.other.PetfriendUser;
import petstone.project.animalisland.other.PetfriendUserList_CustomAdapter;
import petstone.project.animalisland.other.petfriend_recycelview_adapter.PetfriendRecycleAdapter;
import petstone.project.animalisland.other.petfriend_recycelview_adapter.PetfriendSearchData;
import petstone.project.animalisland.other.petfriend_recycelview_adapter.RecyclerDecoration;


public class PetFriendComponent extends Fragment {
    //private ListView listView;
    //private PetfriendCustomAdapter adapter;
    private FloatingActionButton petfriend_submit;
    private SearchView petfriend_search_view;
    private RecyclerView search_recyclerView;
    private RecyclerView list_recyclerView;
    private LinearLayoutManager mLayoutManager;
    PetfriendRecycleAdapter pfs_adapter;
    ArrayList<PetfriendSearchData> search_list = null;

    //유저리스트 리사이클러뷰
    private ArrayList<PetfriendUser> arrayList;
    private RecyclerView user_rv;
    private RecyclerView.Adapter user_adapter;
    private RecyclerView.LayoutManager layoutManager;

    //파이어 베이스
    private FirebaseFirestore db;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.petfriend_component, container, false);

         petfriend_search_view = view.findViewById(R.id.petfriend_search_view);


        //리사이클 뷰에 들어갈 리스트뷰
        search_list = new ArrayList<PetfriendSearchData>();

        //리스트뷰에 예시 데이터넣기
        search_list.add(new PetfriendSearchData(new String("서울")));
        search_list.add(new PetfriendSearchData(new String("강북")));
        search_list.add(new PetfriendSearchData(new String("강서")));

        //유저 리스트 리사이클러뷰 바인딩
        user_rv = view.findViewById(R.id.petfriend_rv);

        //유저 리스트 리사이클러뷰 레이아웃 설정
        user_rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        user_rv.setLayoutManager(layoutManager);
        // 유저 리스트 리사이클러 뷰 어댑터, 리스트 설정
        arrayList = new ArrayList<>(); // pertfrienduser 객체를 담을 어레이 리스트(어댑터쪽)
        user_adapter = new PetfriendUserList_CustomAdapter(arrayList, getContext());
        user_rv.setAdapter(user_adapter);

        //파이어베이스 인스턴스 초기화
        db = FirebaseFirestore.getInstance();




        //데이터 바인딩
        petfriend_submit = view.findViewById(R.id.petfriend_submit);
        petfriend_search_view = view.findViewById(R.id.petfriend_search_view);
        // 리사이클뷰 바인딩
        search_recyclerView = view.findViewById(R.id.search_result);

        //init layoutmanager
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        //리사이클뷰 간격 숫자가 커질수록 간격이 넓어짐
        search_recyclerView.addItemDecoration(new RecyclerDecoration(10));
        //리사이클 뷰의 레이아웃 크기가 변경되는걸 막음
        search_recyclerView.setHasFixedSize(true);
        //setLayoutManager
        search_recyclerView.setLayoutManager(mLayoutManager);
        //init aapter
        pfs_adapter = new PetfriendRecycleAdapter(search_list);
        // set Data
        pfs_adapter = new PetfriendRecycleAdapter(search_list);
        // set Adapter
        search_recyclerView.setAdapter(pfs_adapter);


        search_recyclerView.addItemDecoration(new RecyclerDecoration(10));
        pfs_adapter.notifyDataSetChanged();

        if(arrayList!=null)
            firebaseSearch();




        petfriend_search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
               Search(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
               Search(s);
                return true;
            }
        });




        //검색 기록 리사이클뷰 호라이즌
        /*LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer); */


        // floating 버튼 클릭시, 조건문으로 펫프랜즈 권한확인후 버튼 활성화 or 권한없으면 비활성화 하거나 신청 화면으로 연결
        petfriend_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getContext(), MypagePetfriendApplyActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }

    public void Search(String s) {

        ArrayList<PetfriendUser> mArrayList = new ArrayList<>();


        for(int i =0; i< arrayList.size(); i++)
        {
            String searchNickName = arrayList.get(i).getNickname();
            String searchAddress = arrayList.get(i).getAddress();

            if(searchNickName.toLowerCase().contains(s.toLowerCase()) || searchAddress.toLowerCase().contains(s.toLowerCase()))
            {
                mArrayList.add(arrayList.get(i));
            }
        }

        PetfriendUserList_CustomAdapter adapter = new PetfriendUserList_CustomAdapter(mArrayList, getContext());
        user_rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (s.isEmpty()) {

            firebaseSearch();
            user_adapter.notifyDataSetChanged();
            user_rv.setAdapter(user_adapter);

        }


    }


    void firebaseSearch() {

        db.collection("petfriend")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //해당컬렉션에 모든 문서를 가져옴

                        arrayList.clear(); // 기존 배열 초기화 예방차원
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("firebaseSearch", document.getId() + " => " + document.getData() + "\n");
                                //리스트에 petfriend 컬렉션에서 모든 문서들의 데이터(닉네임,uid,비용,시간,자격증)를 가져와서 arrayList에 넣기
                                //String uid, String nickname, String address
                                arrayList.add(new PetfriendUser(
                                        document.getData().get("uid").toString()
                                        , document.getData().get("nickname").toString()
                                        , document.getData().get("address").toString()

                                ));
                                //어댑터 새로고침
                                user_adapter.notifyDataSetChanged();

                            }
                        } else {
                            Log.d("firebaseSearch", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

}




