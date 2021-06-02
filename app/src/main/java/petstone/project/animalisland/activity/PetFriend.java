package petstone.project.animalisland.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import petstone.project.animalisland.R;
import petstone.project.animalisland.petfriend_recycelview_adapter.HorizontalData;
import petstone.project.animalisland.component.PetfriendCustomAdapter;
import petstone.project.animalisland.petfriend_recycelview_adapter.PetfriendSearchHorizontalAdapter;

public class PetFriend extends Fragment {
    ListView listView;
    PetfriendCustomAdapter adapter;
    FloatingActionButton petfriend_submit;
    SearchView petfriend_search_view;
    RecyclerView p_recyclerView;
    private LinearLayoutManager mLayoutManager;
    PetfriendSearchHorizontalAdapter pfs_adapter;






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.petfriend_component, container, false);

        //데이터 바인딩
        petfriend_submit = view.findViewById(R.id.petfriend_submit);
        petfriend_search_view = view.findViewById(R.id.petfriend_search_view);
        // 리사이클뷰 바인딩
        p_recyclerView = view.findViewById(R.id.search_result);

        //init layoutmanager
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL

        //setLayoutManager
        p_recyclerView.setLayoutManager(mLayoutManager);

        //init aapter
        pfs_adapter = new PetfriendSearchHorizontalAdapter();

        //init data
        ArrayList<HorizontalData> data = new ArrayList<>();

        /*
                int i = 0;
        while (i < MAX_ITEM_COUNT) {
            data.add(new HorizontalData(R.mipmap.ic_launcher, i+"번째 데이터"));
            i++;
        }
         */

        // set Data
        pfs_adapter.setData(data);

        // set Adapter
        p_recyclerView.setAdapter(pfs_adapter);






        //유저리스트 리스트뷰
        listView = view.findViewById(R.id.petfriend_listview);
        adapter = new PetfriendCustomAdapter(getContext());
        listView.setAdapter(adapter);

        
        //검색 기록 리사이클뷰 호라이즌
        /*LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);*/

        //유저 클릭시 유저 정보 뛰움
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                intent = new Intent(getContext(), PetfriendUserSelect.class);
                startActivity(intent);

            }
        });
        
        // floating 버튼 클릭시, 조건문으로 펫프랜즈 권한확인후 버튼 활성화 or 권한없으면 비활성화 하거나 신청 화면으로 연결
        petfriend_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getContext(), PetfriendUserApplyActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }


}

