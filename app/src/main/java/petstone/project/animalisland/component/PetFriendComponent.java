package petstone.project.animalisland.component;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.MypagePetfriendApplyActivity;
import petstone.project.animalisland.other.PetfriendFireAdapter;
import petstone.project.animalisland.other.PetfriendFireUser;
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
    private TextView userListSize;
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
    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference profileImagesRef;

    private String profileUri;

    // 내 uid
    private String mMyUid;
    //중복 여부
    private boolean isJungbok = false;
    //다이어로그
    private AlertDialog mAlertDialog;

    //파이어베이스 어댑터
    private PetfriendFireAdapter fireAdapter;







    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.petfriend_component, container, false);

         petfriend_search_view = view.findViewById(R.id.petfriend_search_view);
         userListSize = view.findViewById(R.id.petfriend_user_size);


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


        // 현재 유저 확인
        usercheck();
        // 중복 체크
        jungbokCheck();

        if(arrayList!=null)
            firebaseSearch();



        /*
        //파이어베이스에서 정보 읽어서 리사이클러뷰에 넣기
        FireAdapter();
        // 어댑터 클릭 이벤트
        fireAdapter.setOnItemClickListner(new PetfriendFireAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                PetfriendFireUser petfriendFireUser = documentSnapshot.toObject(PetfriendFireUser.class);
                // 클릭한유저 ID얻기
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Log.d("FireAdapterEvent" , "get position : " + position + "get ID : " + id );

                // 인텐트
                // 클릭한 유저 UID를 얻고 PetfriendUserSelect 클래스에 UID를 보냄
                Intent intent = new Intent(getContext(), PetfriendUserSelect.class);
                intent.putExtra("UID",id);
                getContext().startActivity(intent);
            }
        });

         */




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
                if(!isJungbok) {
                    Intent intent;
                    intent = new Intent(getContext(), MypagePetfriendApplyActivity.class);
                    startActivity(intent);
                }
                else{
                    sujungDialog();
                }

            }
        });

        return view;
    }

    // 이미 가입했을시 수정 다이어로그
    private void sujungDialog() {
        

            try {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("이미 펫프랜즈를 신청했습니다.\n신청한 내용을 수정하실건가요?");


                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "취소", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });
                builder.setPositiveButton("수정하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "수정완료", Toast.LENGTH_SHORT).show();

                        Intent intent;
                        intent = new Intent(getContext(), MypagePetfriendApplyActivity.class);
                        startActivity(intent);


                    }
                });

                mAlertDialog = builder.create();
                mAlertDialog.show();

            } catch (Exception e) {
                Log.e("dialog error", e.toString());
            }


        }
    // 검색 함수
    public void Search(String s) {

        

        //배열에서 검색
        ListSearch(s);
        //파이어베이스에서 검색
        //fireSearch(s);


    }
    // 펫프랜즈 콜렉션에 모든 문서 가져오기
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

                                profileUri = document.getData().get("profileImgUri").toString();

                                //리스트에 petfriend 컬렉션에서 모든 문서들의 데이터(닉네임,uid,비용,시간,자격증)를 가져와서 arrayList에 넣기
                                //String uid, String nickname, String address
                                arrayList.add(new PetfriendUser(
                                        document.getData().get("uid").toString()
                                        , document.getData().get("nickname").toString()
                                        , document.getData().get("address").toString()
                                        , null
                                        , document.getData().get("profileImgUri").toString()

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
    // 유저 확인
    private void usercheck() {
        //uid 확인
        user = FirebaseAuth.getInstance().getCurrentUser();
        mMyUid = user.getUid();
        Log.d("MyUid", "내 UID : "+ mMyUid.toString());
    }
    // 중복 확인
    void jungbokCheck(){

        db.collection("petfriend")
                .whereEqualTo("uid", mMyUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("jungbok", document.getId() + " => " + document.getData());
                                isJungbok = true;
                            }
                        } else {
                            Log.d("no,jungbok", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
   // 파이어스토어 어댑터
    void FireAdapter(){

        Query query = db.collection("petfriend");

        FirestoreRecyclerOptions<PetfriendFireUser> options = new FirestoreRecyclerOptions.Builder<PetfriendFireUser>()
                .setQuery(query,PetfriendFireUser.class)
                .build();

        fireAdapter = new PetfriendFireAdapter(options);
        user_rv.setAdapter(fireAdapter);

    }

    //리스트에서에서 검색
    private void ListSearch(String s) {

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

    //파이어베이스에서 검색

    private void fireSearch(String s){


        Query query;
        if (s.toString().isEmpty()) {
            query = db.collection("petfriend");



        }
        else {
            query = db.collection("petfriend")
                    .orderBy("nickname")
                    .startAt(s)
                    .endAt(s +"\uf8ff");


        }

        FirestoreRecyclerOptions<PetfriendFireUser> options = new FirestoreRecyclerOptions.Builder<PetfriendFireUser>()
                .setQuery(query, PetfriendFireUser.class)
                .build();


        fireAdapter.updateOptions(options);
        fireAdapter.notifyDataSetChanged();
        
    }


    // 다이어로그 오류 방지
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }

    }



    // 어댑터 실행
    /*
    @Override
    public void onStart() {
        super.onStart();
        fireAdapter.startListening();
    }

    // 어댑터 멈춤
    @Override
    public void onStop() {
        super.onStop();
        fireAdapter.stopListening();
    }

     */


}






