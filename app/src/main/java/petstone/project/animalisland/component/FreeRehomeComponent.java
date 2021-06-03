package petstone.project.animalisland.component;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import petstone.project.animalisland.R;
import petstone.project.animalisland.activity.RehomeFreeSubmitActivity;
import petstone.project.animalisland.other.FreeRecycleAdapter;
import petstone.project.animalisland.other.FreeRehomeList;

public class FreeRehomeComponent extends Fragment {
    FloatingActionButton free_submit;
    RecyclerView recyclerView = null ;
    FreeRecycleAdapter frAdapter = null ;
    ArrayList<FreeRehomeList> mList = new ArrayList<FreeRehomeList>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.free_rehome, container, false);
        
        free_submit = view.findViewById(R.id.free_submit);
        recyclerView = view.findViewById(R.id.free_recycler);
        frAdapter = new FreeRecycleAdapter(mList);
        recyclerView.setAdapter(frAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        addItem(getResources().getDrawable(R.drawable.image,null), getResources().getDrawable(R.drawable.female,null), "[동물종류] 품종", "나이", "지역", "날짜", "닉네임") ;

        frAdapter.notifyDataSetChanged() ;


        free_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RehomeFreeSubmitActivity.class);
                startActivityForResult(intent, 0 );
            }
        });

        return view;
    }

    public void addItem(Drawable main, Drawable gender, String breed, String age, String local, String date, String nickname) {
        FreeRehomeList item = new FreeRehomeList();

        item.setImg(main);
        item.setGenderImg(gender);
        item.setBreed(breed);
        item.setAge(age);
        item.setLocal(local);
        item.setDate(date);
        item.setNickname(nickname);

        mList.add(item);
    }
}
