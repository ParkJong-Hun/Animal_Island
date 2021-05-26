package petstone.project.animalisland.component;

import android.content.Intent;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.LoginActivity;
import petstone.project.animalisland.activity.MypageInfoEditActivity;
import petstone.project.animalisland.activity.MypagePetfriendApplyActivity;
import petstone.project.animalisland.activity.MypageSellApplyActivity;

public class MypageComponent extends Fragment {

    ListView list;
    MypageCustomListAdapter listAdapter;
    Button mypage_profile_edit, mypage_description_edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mypage_component, container, false);

        mypage_profile_edit = rootView.findViewById(R.id.mypage_profile_edit);
        mypage_description_edit = rootView.findViewById(R.id.mypage_description_edit);

        list = rootView.findViewById(R.id.mypage_list);
        listAdapter = new MypageCustomListAdapter(getContext());
        list.setAdapter(listAdapter);

        //프로필 편집 클릭 리스너
        mypage_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //파일 업로드
            }
        });

        mypage_description_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MypageInfoEditActivity.class);
                startActivityForResult(intent, 0 );
            }
        });

        //하단 리스트 클릭 리스너
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch(position) {
                    //펫프렌즈신청
                    case 0: {
                        intent = new Intent(getContext(), MypagePetfriendApplyActivity.class);
                        startActivity(intent);
                        break;
                    }
                    //유료 분양 권한 신청
                    case 1: {
                        intent = new Intent(getContext(), MypageSellApplyActivity.class);
                        startActivity(intent);
                        break;
                    }
                    //이벤트
                    case 2: {
                        //다이얼로그
                        break;
                    }
                    //공지사항
                    case 3: {
                        //다이얼로그
                        break;
                    }
                    //로그아웃
                    case 4: {
                        intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {//정보 수정 결과
                if(resultCode == -1) {
                    Toast.makeText(getContext(), "정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                } else if(resultCode == 0) {

                }
            }
            case 1: {//펫프렌즈 신청 결과
                if(resultCode == -1) {

                } else if(resultCode == 0) {

                }
            }
            case 2: {//유료분양 권한 신청 결과
                if(resultCode == -1) {

                } else if(resultCode == 0) {

                }
            }
        }
    }
}
