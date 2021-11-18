package petstone.project.animalisland.component;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.MypagePetfriendApplyActivity;
import petstone.project.animalisland.activity.PetfriendUserSelect;
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
    // xml
    private FloatingActionButton petfriend_submit;
    private SearchView petfriend_search_view;
    private RecyclerView search_recyclerView;
    private RecyclerView list_recyclerView;
    private TextView userListSize;
    private LinearLayoutManager mLayoutManager;
    PetfriendRecycleAdapter pfs_adapter;
    private Button myPetfriend_btn;
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

    // 날짜 나누기
    private String Days = "";

    // 스피너
    private Spinner mHwaldong_sp;
    private Spinner mOrderbyMoney_sp;
    // 스피너 어뎁터
    private ArrayAdapter hwaldongAdapter;
    private ArrayAdapter moneyAdapter;
    // 스피너 리스트
    private String[] mHwaldong_list = {"활동", "산책", "돌봄", "미용"};
    private String[] mMoney_list = {"비용", "저가순", "고가순"};

    //파이어베이스 쿼리문
    Query query;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.petfriend_component, container, false);


        //데이터 바인딩
        petfriend_submit = view.findViewById(R.id.petfriend_submit);
        petfriend_search_view = view.findViewById(R.id.petfriend_search_view);
        user_rv = view.findViewById(R.id.petfriend_rv);
        userListSize = view.findViewById(R.id.petfriend_user_size);
        mHwaldong_sp = view.findViewById(R.id.hwaldong_filter);
        mOrderbyMoney_sp = view.findViewById(R.id.money_filter);
        myPetfriend_btn = view.findViewById(R.id.myPetfriend_btn);


        //유저 리스트 리사이클러뷰 레이아웃 설정
        user_rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        user_rv.setLayoutManager(layoutManager);
        // 유저 리스트 리사이클러 뷰 어댑터, 리스트 설정
        arrayList = new ArrayList<>(); // pertfrienduser 객체를 담을 어레이 리스트(어댑터쪽)
        user_adapter = new PetfriendUserList_CustomAdapter(arrayList, getContext());

        //user_rv.setAdapter(user_adapter);

        //파이어베이스 인스턴스 초기화
        db = FirebaseFirestore.getInstance();

        //스피너 어댑터
        hwaldongAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, mHwaldong_list);
        moneyAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, mMoney_list);

        // 어댑터 연결
        mHwaldong_sp.setAdapter(hwaldongAdapter);
        mOrderbyMoney_sp.setAdapter(moneyAdapter);


        // 리사이클뷰 바인딩
        search_recyclerView = view.findViewById(R.id.search_result);
        //리사이클 뷰에 들어갈 리스트뷰
        search_list = new ArrayList<PetfriendSearchData>();
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
        //if(arrayList!=null)
        //firebaseSearch();


        jungbokCheck();
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
                Log.d("FireAdapterEvent", "get position : " + position + "get ID : " + id);

                // 인텐트
                // 클릭한 유저 UID를 얻고 PetfriendUserSelect 클래스에 UID를 보냄
                Intent intent = new Intent(getContext(), PetfriendUserSelect.class);
                intent.putExtra("UID", id);
                getContext().startActivity(intent);
                //startActivityForResult(intent, 11 );
            }
        });


        mHwaldong_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String m = mHwaldong_sp.getItemAtPosition(position).toString();
                switch (m) {
                    case "산책":
                        HwaldongFilter(m);
                        break;
                    case "돌봄":
                        HwaldongFilter(m);
                        break;
                    case "미용":
                        HwaldongFilter(m);
                        break;

                    default:
                        HwaldongFilter(m);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getContext(), "선택 : " + null, Toast.LENGTH_SHORT).show();
            }
        });






        mOrderbyMoney_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"활동선택 : "+mOrderbyMoney_sp.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                String m = mOrderbyMoney_sp.getItemAtPosition(position).toString();
                switch (m)
                {
                    case "비용": MoneyOrderBy(m);break;
                    case "저가순": MoneyOrderBy(m);break;
                    case "고가순": MoneyOrderBy(m);break;

                    default: break;

                }
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        // 중복 체크
        //jungbokCheck();


        myPetfriend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PetfriendUserSelect.class);
                intent.putExtra("UID", mMyUid);
                getContext().startActivity(intent);
            }
        });


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
                if (!isJungbok) {
                    Intent intent;
                    intent = new Intent(getContext(), MypagePetfriendApplyActivity.class);
                    intent.putExtra("new", 0);
                    startActivity(intent);
                } else {
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
                    Intent intent;
                    intent = new Intent(getContext(), MypagePetfriendApplyActivity.class);
                    intent.putExtra("new", 1);
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
                                Log.d("연동성공", "\n가져온 UID : " + document.getId() + " \n가져온 데이터 : " + document.getData() + "\n");

                                profileUri = document.getData().get("profileImgUri").toString();
                                Days = document.getData().get("days").toString();


                                //리스트에 petfriend 컬렉션에서 모든 문서들의 데이터(닉네임,uid,비용,시간,자격증)를 가져와서 arrayList에 넣기
                                //String uid, String nickname, String address
                                //uid 닉네임 주소 자격증 프로필주소
                                arrayList.add(new PetfriendUser(
                                        document.getData().get("uid").toString()
                                        , document.getData().get("nickname").toString()
                                        , document.getData().get("address").toString()
                                        , document.getData().get("carrerImgName").toString()
                                        , document.getData().get("profileImgUri").toString()
                                        , SplitDays(Days)
                                        , null
                                        , null
                                        , null
                                        , document.getData().get("pay").toString()
                                        , document.getBoolean("hwaldong_sancheck")
                                        , document.getBoolean("hwaldong_dolbom")
                                        , document.getBoolean("hwaldong_beauty")
                                        , document.getBoolean("petfriend")
                                ));

                                //어댑터 새로고침
                                user_adapter.notifyDataSetChanged();


                            }
                        } else {
                            Log.d("연동 실패", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    // 필터
    void HwaldongFilter(String s) {

        String filter = "";

        switch (s) {
            case "산책":
                filter = "hwaldong_sancheck";
                query = db.collection("petfriend").whereEqualTo(filter, true);
                break;
            case "돌봄":
                filter = "hwaldong_dolbom";
                query = db.collection("petfriend").whereEqualTo(filter, true);
                break;
            case "미용":
                filter = "hwaldong_beauty";
                query = db.collection("petfriend").whereEqualTo(filter, true);
                break;

            default:
                query = db.collection("petfriend");
                break;
        }

        FireAdapterFilter();





        /*
        ArrayList<PetfriendUser> mArrayList = new ArrayList<>();
        if(s.equals("산책")) {

            for (int i = 0; i < arrayList.size(); i++) {
                boolean isSancheck = arrayList.get(i).isHwaldong_sancheck();

                if (isSancheck) {
                    mArrayList.add(arrayList.get(i));
                }
            }
            PetfriendUserList_CustomAdapter adapter = new PetfriendUserList_CustomAdapter(mArrayList, getContext());
            user_rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else if(s.equals("돌봄"))
        {
            for (int i = 0; i < arrayList.size(); i++) {
                boolean isDolbom = arrayList.get(i).isHwaldong_dolbom();

                if (isDolbom) {
                    mArrayList.add(arrayList.get(i));
                }
            }
            PetfriendUserList_CustomAdapter adapter = new PetfriendUserList_CustomAdapter(mArrayList, getContext());
            user_rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else if(s.equals("미용"))
        {
            for (int i = 0; i < arrayList.size(); i++) {
                boolean isBeauty = arrayList.get(i).isHwaldong_beauty();

                if (isBeauty) {
                    mArrayList.add(arrayList.get(i));
                }
            }
            PetfriendUserList_CustomAdapter adapter = new PetfriendUserList_CustomAdapter(mArrayList, getContext());
            user_rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
        else{

            firebaseSearch();
            user_adapter.notifyDataSetChanged();
            user_rv.setAdapter(user_adapter);

        }
         */

    }

    void FireAdapterFilter() {

        FirestoreRecyclerOptions<PetfriendFireUser> options = new FirestoreRecyclerOptions.Builder<PetfriendFireUser>()
                .setQuery(query, PetfriendFireUser.class)
                .build();

        fireAdapter.updateOptions(options);
        fireAdapter.notifyDataSetChanged();
        //user_rv.setAdapter(fireAdapter);
        Log.d("필터 작동 ", query + "");

    }

    // 비용별 오더바이
    void MoneyOrderBy(String s) {

        if (s.equals("저가순")) {
            //오름차순
            query.orderBy("pay");
            Log.d("오름차순 작동" , "오름차순");

        } else if (s.equals("고가순")) {
            //내림차순
            query.orderBy("pay", Query.Direction.DESCENDING);
            Log.d("내림차순 작동" , "내림차순");


        } else {
            query = db.collection("petfriend");
        }

        FireAdapterFilter();

    }

    // 유저 확인
    private void usercheck() {
        //uid 확인
        user = FirebaseAuth.getInstance().getCurrentUser();
        mMyUid = user.getUid();
        Log.d("MyUid", "내 UID : " + mMyUid.toString());
    }

    // 중복 확인
    void jungbokCheck() {


        db.collection("petfriend")
                .whereEqualTo("uid", mMyUid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("error", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d("add", "New city: " + dc.getDocument().getData());
                                    myPetfriend_btn.setVisibility(View.VISIBLE);
                                    isJungbok = true;
                                    break;
                                case MODIFIED:
                                    Log.d("sujung", "Modified city: " + dc.getDocument().getData());
                                    myPetfriend_btn.setVisibility(View.VISIBLE);
                                    isJungbok = true;
                                    break;
                                case REMOVED:
                                    Log.d("remove", "Removed city: " + dc.getDocument().getData());
                                    myPetfriend_btn.setVisibility(View.GONE);
                                    isJungbok = false;
                                    break;
                            }
                        }

                    }
                });


    }

    // 파이어스토어 어댑터
    void FireAdapter() {

        query = db.collection("petfriend");

        FirestoreRecyclerOptions<PetfriendFireUser> options = new FirestoreRecyclerOptions.Builder<PetfriendFireUser>()
                .setQuery(query, PetfriendFireUser.class)
                .build();

        fireAdapter = new PetfriendFireAdapter(options, getContext());

        Log.d("어댑터에 파이어베이스 어댑터 넣기", "연결");
        user_adapter = fireAdapter;
        user_rv.setAdapter(fireAdapter);

        /*
        Query query1 = db.collection("petfriend").whereEqualTo("uid",mMyUid);

        FirestoreRecyclerOptions<PetfriendFireUser> options1 = new FirestoreRecyclerOptions.Builder<PetfriendFireUser>()
                .setQuery(query1,PetfriendFireUser.class)
                .build();

        PetfriendFireAdapter  fa = new PetfriendFireAdapter(options1);

        Log.d("파이어 어댑터 사이즈",fa.getItemCount() + " ");

         */


    }

    String SplitDays(String days) {
        StringBuilder sb = new StringBuilder();
        String s[] = days.split(" ");
        for (int i = 0; i < s.length; i++) {
            switch (s[i]) {
                case "월요일":
                    sb.append("월" + " ");
                    break;
                case "화요일":
                    sb.append("화" + " ");
                    break;
                case "수요일":
                    sb.append("수" + " ");
                    break;
                case "목요일":
                    sb.append("목" + " ");
                    break;
                case "금요일":
                    sb.append("금" + " ");
                    break;
                case "토요일":
                    sb.append("토" + " ");
                    break;
                case "일요일":
                    sb.append("일" + " ");
                    break;

            }
        }
        return sb.toString();
    }

    //리스트에서에서 검색
    private void ListSearch(String s) {

        firebaseSearch();

        ArrayList<PetfriendUser> mArrayList = new ArrayList<>();

        for (int i = 0; i < arrayList.size(); i++) {
            String searchNickName = arrayList.get(i).getNickname();
            String searchAddress = arrayList.get(i).getAddress();

            if (searchNickName.toLowerCase().contains(s.toLowerCase()) || searchAddress.toLowerCase().contains(s.toLowerCase())) {
                mArrayList.add(arrayList.get(i));
            }
        }

        PetfriendUserList_CustomAdapter adapter = new PetfriendUserList_CustomAdapter(mArrayList, getContext());
        user_rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (s.isEmpty()) {

            //firebaseSearch();
            //user_adapter.notifyDataSetChanged();
            //user_rv.setAdapter(user_adapter);
            fireAdapter.notifyDataSetChanged();
            user_rv.setAdapter(fireAdapter);

        }

    }

    //파이어베이스에서 검색

    /*private void fireSearch(String s){


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
        
    }*/


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
    @Override
    public void onStart() {
        super.onStart();
        Log.d("파이어베이스 리스닝", "시작");
        fireAdapter.startListening();
    }

    // 어댑터 멈춤
    @Override
    public void onStop() {
        super.onStop();
        Log.d("파이어베이스 리스닝", "끝");
        fireAdapter.stopListening();
    }


}






