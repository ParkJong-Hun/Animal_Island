package petstone.project.animalisland;

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

import petstone.project.animalisland.activity.RehomeFreeSubmitActivity;
import petstone.project.animalisland.activity.RehomeSellSubmitActivity;

public class Sell_Rehome extends Fragment {

    FloatingActionButton sell_submit;
    RecyclerView recyclerView = null ;
    SellRecycleAdapter srAdapter = null ;
    ArrayList<SellRehomeList> mList = new ArrayList<SellRehomeList>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sell_rehome, container, false);
        sell_submit = view.findViewById(R.id.sell_submit);
        recyclerView = view.findViewById(R.id.sell_recycler);
        srAdapter = new SellRecycleAdapter(mList);
        recyclerView.setAdapter(srAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        addItem(getResources().getDrawable(R.drawable.image,null), getResources().getDrawable(R.drawable.female,null), "[동물종류] 품종", "나이", "지역", "날짜", "분양가격" ,"닉네임") ;


        srAdapter.notifyDataSetChanged() ;

        sell_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RehomeSellSubmitActivity.class);
                startActivityForResult(intent, 0 );
            }
        });

        return view;
    }

    public void addItem(Drawable main, Drawable gender, String breed, String age, String local, String date, String price, String nickname) {
        SellRehomeList item = new SellRehomeList();

        item.setImg(main);
        item.setGenderImg(gender);
        item.setBreed(breed);
        item.setAge(age);
        item.setLocal(local);
        item.setDate(date);
        item.setPrice(price);
        item.setNickname(nickname);

        mList.add(item);
    }
}
