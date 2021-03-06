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
import com.google.android.gms.tasks.OnSuccessListener;
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


import java.lang.reflect.Array;
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

    //??????????????? ??????????????????
    private ArrayList<PetfriendUser> arrayList;
    private RecyclerView user_rv;
    private RecyclerView.Adapter user_adapter;
    private RecyclerView.LayoutManager layoutManager;

    //????????? ?????????
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference profileImagesRef;

    private String profileUri;

    // ??? uid
    private String mMyUid;

    //?????? ??????
    private boolean isJungbok = false;

    //???????????????
    private AlertDialog mAlertDialog;

    //?????????????????? ?????????
    private PetfriendFireAdapter fireAdapter;

    // ?????? ?????????
    private String Days = "";

    // ?????????
    private Spinner mHwaldong_sp;
    private Spinner mOrderbyMoney_sp;
    // ????????? ?????????
    private ArrayAdapter hwaldongAdapter;
    private ArrayAdapter moneyAdapter;
    // ????????? ?????????
    private String[] mHwaldong_list = {"??????", "??????", "??????", "??????"};
    private String[] mMoney_list = {"??????", "?????????", "?????????"};
    // ??????
    private String mFilter = "";

    //?????? ?????????
    String mNickName = "";
    float mRating;

    //?????????????????? ?????????
    Query query;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.petfriend_component, container, false);


        //????????? ?????????
        petfriend_submit = view.findViewById(R.id.petfriend_submit);
        petfriend_search_view = view.findViewById(R.id.petfriend_search_view);
        user_rv = view.findViewById(R.id.petfriend_rv);
        userListSize = view.findViewById(R.id.petfriend_user_size);
        mHwaldong_sp = view.findViewById(R.id.hwaldong_filter);
        mOrderbyMoney_sp = view.findViewById(R.id.money_filter);
        myPetfriend_btn = view.findViewById(R.id.myPetfriend_btn);


        //?????? ????????? ?????????????????? ???????????? ??????
        user_rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        user_rv.setLayoutManager(layoutManager);
        // ?????? ????????? ??????????????? ??? ?????????, ????????? ??????
        arrayList = new ArrayList<>(); // pertfrienduser ????????? ?????? ????????? ?????????(????????????)
        user_adapter = new PetfriendUserList_CustomAdapter(arrayList, getContext());

        //user_rv.setAdapter(user_adapter);

        //?????????????????? ???????????? ?????????
        db = FirebaseFirestore.getInstance();

        //????????? ?????????
        hwaldongAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, mHwaldong_list);
        moneyAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, mMoney_list);

        // ????????? ??????
        mHwaldong_sp.setAdapter(hwaldongAdapter);
        mOrderbyMoney_sp.setAdapter(moneyAdapter);


        // ??????????????? ?????????
        search_recyclerView = view.findViewById(R.id.search_result);
        //???????????? ?????? ????????? ????????????
        search_list = new ArrayList<PetfriendSearchData>();
        //init layoutmanager
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // ???????????? VERTICAL
        //??????????????? ?????? ????????? ???????????? ????????? ?????????
        search_recyclerView.addItemDecoration(new RecyclerDecoration(10));
        //???????????? ?????? ???????????? ????????? ??????????????? ??????
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


        // ?????? ?????? ??????
        usercheck();
        if (arrayList != null)
            //firebaseSearch();


        //????????????
        jungbokCheck();
        //???????????????????????? ?????? ????????? ????????????????????? ??????
        FireAdapter();
        TotalUser();
        //TotalFireUser();
        FirebaseDataChange();
        // ????????? ?????? ?????????
        fireAdapter.setOnItemClickListner(new PetfriendFireAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                PetfriendFireUser petfriendFireUser = documentSnapshot.toObject(PetfriendFireUser.class);
                // ??????????????? ID??????
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Log.d("FireAdapterEvent", "get position : " + position + "get ID : " + id);

                // ?????????
                // ????????? ?????? UID??? ?????? PetfriendUserSelect ???????????? UID??? ??????
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
                    case "??????":
                        HwaldongFilter(m);
                        break;
                    case "??????":
                        HwaldongFilter(m);
                        break;
                    case "??????":
                        HwaldongFilter(m);
                        break;
                    default:
                        HwaldongFilter(m);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getContext(), "?????? : " + null, Toast.LENGTH_SHORT).show();
            }
        });

        mOrderbyMoney_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String m = mOrderbyMoney_sp.getItemAtPosition(position).toString();
                Log.d("????????? ????????? ???", position + "\n" + view + "\n" + id);
                switch (m) {
                    case "?????????":
                        MoneyOrderBy(m);
                        break;
                    case "?????????":
                        MoneyOrderBy(m);
                        break;
                    default:
                        MoneyOrderBy(m);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // ?????? ???????
        myPetfriend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PetfriendUserSelect.class);
                intent.putExtra("UID", mMyUid);
                getContext().startActivity(intent);
            }
        });

        // ?????? ?????????
        petfriend_search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                //Search(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Search(s);
                return false;
            }
        });


        //?????? ?????? ??????????????? ????????????
        /*LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer); */


        // floating ?????? ?????????, ??????????????? ???????????? ??????????????? ?????? ????????? or ??????????????? ???????????? ????????? ?????? ???????????? ??????
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

    // ?????? ??????????????? ?????? ???????????????
    private void sujungDialog() {
        try {

            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("?????? ??????????????? ??????????????????.\n????????? ????????? ??????????????????????");


            builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getContext(), "??????", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            });
            builder.setPositiveButton("????????????", new DialogInterface.OnClickListener() {
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

    // ?????? ??????
    public void Search(String s) {

        //firebaseSearch();
        //???????????? ??????
        ListSearch(s);

    }

    // ??????????????????
    // ???????????? ???????????? ?????? ?????? ????????????
    void firebaseSearch() {


        /*
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //?????????????????? ?????? ????????? ?????????
                        arrayList.clear(); // ?????? ?????? ????????? ????????????
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("firesearch : ????????????", "\n????????? UID : " + document.getId() + " \n????????? ????????? : " + document.getData() + "\n");
                                profileUri = document.getData().get("profileImgUri").toString();
                                Days = document.getData().get("days").toString();
                                //???????????? petfriend ??????????????? ?????? ???????????? ?????????(?????????,uid,??????,??????,?????????)??? ???????????? arrayList??? ??????
                                //String uid, String nickname, String address
                                //uid ????????? ?????? ????????? ???????????????
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
                                //????????? ????????????
                                user_adapter.notifyDataSetChanged();
                                int i = user_adapter.getItemCount();
                                userListSize.setText(i + "???");
                            }
                        } else {
                            Log.d("?????? ??????", "Error getting documents: ", task.getException());
                        }
                    }
                });
         */

        db.collection("petfriend").whereEqualTo("petfriend", true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d("?????????????????? : ????????????", "\n????????? UID : " + doc.getId() + " \n????????? ????????? : " + doc.getData() + "\n");

                    profileUri = doc.getData().get("profileImgUri").toString();
                    Days = doc.getData().get("days").toString();

                    //???????????? petfriend ??????????????? ?????? ???????????? ?????????(?????????,uid,??????,??????,?????????)??? ???????????? arrayList??? ??????
                    //String uid, String nickname, String address
                    //uid ????????? ?????? ????????? ???????????????
                    //SplitDays(Days)
                    arrayList.add(new PetfriendUser(
                            doc.getData().get("uid").toString()
                            , doc.getData().get("nickname").toString()
                            , doc.getData().get("address").toString()
                            , doc.getData().get("carrerImgName").toString()
                            , doc.getData().get("profileImgUri").toString()
                            , SplitDays(Days)
                            , null
                            , null
                            , null
                            , doc.getData().get("pay").toString()
                            , doc.getBoolean("hwaldong_sancheck")
                            , doc.getBoolean("hwaldong_dolbom")
                            , doc.getBoolean("hwaldong_beauty")
                            , doc.getBoolean("petfriend")
                            , Integer.parseInt(doc.getData().get("payint").toString())
                    ));

                    //????????? ????????????
                    user_adapter.notifyDataSetChanged();
                    int i = user_adapter.getItemCount();
                    userListSize.setText(i + "???");

                }
            }
        });


    }


    // ??????
    void HwaldongFilter(String s) {

        String filter = "";

        switch (s) {
            case "??????":
                Log.d("?????? ", query.toString());
                filter = "hwaldong_sancheck";
                mOrderbyMoney_sp.setSelection(0);
                query = db.collection("petfriend").whereEqualTo(filter, true);
                break;
            case "??????":
                Log.d("?????? ", query.toString());
                filter = "hwaldong_dolbom";
                mOrderbyMoney_sp.setSelection(0);
                query = db.collection("petfriend").whereEqualTo(filter, true);
                break;
            case "??????":
                Log.d("?????? ", query.toString());
                filter = "hwaldong_beauty";
                mOrderbyMoney_sp.setSelection(0);
                query = db.collection("petfriend").whereEqualTo(filter, true);
                break;

            default:
                filter = "??????";
                query = db.collection("petfriend");
                mOrderbyMoney_sp.setSelection(0);
                break;
        }

        mFilter = filter;
        FireAdapterFilter();


    }

    // ??????????????? ?????? ??????
    private void FireAdapterFilter() {

        FirestoreRecyclerOptions<PetfriendFireUser> options = new FirestoreRecyclerOptions.Builder<PetfriendFireUser>()
                .setQuery(query, PetfriendFireUser.class)
                .build();

        fireAdapter.updateOptions(options);
        fireAdapter.notifyDataSetChanged();
        user_adapter = fireAdapter;
        //user_rv.setAdapter(fireAdapter);
        int i = user_adapter.getItemCount();
        Log.d("?????? ?????? ", query + "" + i);
        userListSize.setText(i + "???");

    }

    // ??? ?????? ????????? ????????????
    void MoneyOrderBy(String s) {

        if (s.equals("?????????")) {
            Log.d("????????? ", query.toString());
            query = getHwadongQuery("??????");
            Log.d("???????????? ??????", "????????????");

        } else if (s.equals("?????????")) {
            Log.d("????????? ", query.toString());
            query = getHwadongQuery("??????");
            Log.d("???????????? ??????", "????????????");
        } else {
            Log.d("????????? ", query.toString());
            query = getHwadongQuery("??????");

        }

        FireAdapterFilter();

    }

    // ?????? ??????
    private void usercheck() {
        //uid ??????
        user = FirebaseAuth.getInstance().getCurrentUser();
        mMyUid = user.getUid();
        Log.d("MyUid", "??? UID : " + mMyUid.toString());
    }

    // ?????? ?????? ????????????
    private Query getHwadongQuery(String bangsick) {
        Query mQyery;

        if (bangsick.equals("??????")) {

            if (mFilter.length() > 5) {
                mQyery = db.collection("petfriend").whereEqualTo(mFilter, true).orderBy("payint");
            } else {
                mQyery = db.collection("petfriend").orderBy("payint");
            }

        } else if (bangsick.equals("??????")) {

            if (mFilter.length() > 5) {
                mQyery = db.collection("petfriend").whereEqualTo(mFilter, true).orderBy("payint", Query.Direction.DESCENDING);
            } else {
                mQyery = db.collection("petfriend").orderBy("payint", Query.Direction.DESCENDING);
            }

        } else {

            if (mFilter.length() > 5) {
                mQyery = db.collection("petfriend").whereEqualTo(mFilter, true);
            } else {
                mQyery = db.collection("petfriend");
            }


        }


        return mQyery;
    }

    // ?????? ??????
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
                                    Log.d("add", mMyUid + dc.getDocument().getData());
                                    myPetfriend_btn.setVisibility(View.VISIBLE);
                                    isJungbok = true;
                                    break;
                                case MODIFIED:
                                    Log.d("sujung", mMyUid + dc.getDocument().getData());
                                    myPetfriend_btn.setVisibility(View.VISIBLE);
                                    isJungbok = true;
                                    break;
                                case REMOVED:
                                    Log.d("remove", mMyUid + dc.getDocument().getData());
                                    myPetfriend_btn.setVisibility(View.GONE);
                                    isJungbok = false;
                                    int i = fireAdapter.getItemCount();
                                    userListSize.setText(i + "???");
                                    break;
                            }
                        }

                    }
                });


    }

    // ?????????????????? ?????????
    void FireAdapter() {

        query = db.collection("petfriend");

        FirestoreRecyclerOptions<PetfriendFireUser> options = new FirestoreRecyclerOptions.Builder<PetfriendFireUser>()
                .setQuery(query, PetfriendFireUser.class)
                .build();

        fireAdapter = new PetfriendFireAdapter(options, getContext());

        Log.d("???????????? ?????????????????? ????????? ??????", "??????");
        user_adapter.notifyDataSetChanged();
        fireAdapter.notifyDataSetChanged();
        user_adapter = fireAdapter;
        user_rv.setAdapter(fireAdapter);
        TotalUser();


    }


    private String SplitDays(String days) {
        StringBuilder sb = new StringBuilder();
        String s[] = days.split(" ");
        for (int i = 0; i < s.length; i++) {
            switch (s[i]) {
                case "?????????":
                    sb.append("???" + " ");
                    break;
                case "?????????":
                    sb.append("???" + " ");
                    break;
                case "?????????":
                    sb.append("???" + " ");
                    break;
                case "?????????":
                    sb.append("???" + " ");
                    break;
                case "?????????":
                    sb.append("???" + " ");
                    break;
                case "?????????":
                    sb.append("???" + " ");
                    break;
                case "?????????":
                    sb.append("???" + " ");
                    break;

            }
        }
        return sb.toString();
    }

    //????????????????????? ??????
    private void ListSearch(String s) {


        ArrayList<PetfriendUser> mArrayList = new ArrayList<>();
        firebaseSearch();

        for (int i = 0; i < arrayList.size(); i++) {

            String searchNickName = arrayList.get(i).getNickname();
            String searchAddress = arrayList.get(i).getAddress();

            if (searchNickName.toLowerCase().contains(s.toLowerCase()) || searchAddress.toLowerCase().contains(s.toLowerCase())) {
                mArrayList.add(arrayList.get(i));
                Log.d("mArrayList.size", mArrayList.size() + "");

            }
        }



        // ?????? ?????? ??????
        arrayList.clear();
        // ????????? ????????? ??????
        PetfriendUserList_CustomAdapter adapter = new PetfriendUserList_CustomAdapter(mArrayList, getContext());
        // ????????? ????????????
        adapter.notifyDataSetChanged();
        // ????????? ??????
        user_adapter = adapter;
        user_adapter.notifyDataSetChanged();
        user_rv.setAdapter(user_adapter);
        // ????????? ?????????
        //int i = user_adapter.getItemCount();
        //userListSize.setText(i + "???");

        // ???????????? ????????????
        if (s.isEmpty()) {
            fireAdapter.notifyDataSetChanged();
            Log.d("fireAdapter Reset", fireAdapter.getItemCount()+ "");
            user_rv.setAdapter(fireAdapter);
            TotalUser();

        }

    }


    // ?????? ??????
    private void TotalUser() {

        //fireAdapter.onDataChanged();
        user_adapter.notifyDataSetChanged();


        user_adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                int totalNumberOfItems = user_adapter.getItemCount();
                userListSize.setText(String.valueOf(totalNumberOfItems) + "???");
                Log.d("????????? : ", String.valueOf(totalNumberOfItems) + "???");
            }

        });



    }

    private void FirebaseDataChange()
    {
        db.collection("petfriend")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("?????????", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case REMOVED:
                                    query = db.collection("petfriend");
                                    FirestoreRecyclerOptions<PetfriendFireUser> options = new FirestoreRecyclerOptions.Builder<PetfriendFireUser>()
                                            .setQuery(query, PetfriendFireUser.class)
                                            .build();
                                    fireAdapter.updateOptions(options);
                                    user_adapter = fireAdapter;
                                    TotalUser();
                                    break;
                            }
                        }

                    }
                });
    }

    // ?????? ??????
    private void TotalFireUser() {

        //fireAdapter.notifyDataSetChanged();


        fireAdapter.notifyDataSetChanged();

        fireAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                int totalNumberOfItems = fireAdapter.getItemCount();
                userListSize.setText(String.valueOf(totalNumberOfItems) + "???");
                Log.d("????????? : ", String.valueOf(totalNumberOfItems) + "???");
            }

        });



    }


    // ??????????????? ?????? ??????
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }

    }


    // ????????? ??????
    @Override
    public void onStart() {
        super.onStart();
        Log.d("?????????????????? ?????????", "??????");
        fireAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        petfriend_search_view.setQuery("",false);
        fireAdapter.stopListening();
    }
}





