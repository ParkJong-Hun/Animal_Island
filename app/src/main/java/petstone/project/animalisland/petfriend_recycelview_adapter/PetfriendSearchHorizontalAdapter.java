package petstone.project.animalisland.petfriend_recycelview_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import petstone.project.animalisland.R;

public class PetfriendSearchHorizontalAdapter extends RecyclerView.Adapter<HorizontalViewHolder> {

    ArrayList<HorizontalData> HorizontalDatas;
    public void setData(ArrayList<HorizontalData> list){
        HorizontalDatas = list;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 사용할 아이템 뷰를 생성한다.

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.petfriend_search_item,parent,false);

        HorizontalViewHolder holder = new HorizontalViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() { return HorizontalDatas.size(); }

}

