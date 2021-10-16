package petstone.project.animalisland.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import petstone.project.animalisland.R;

public class MypageEventDialog extends Dialog {
    private Context context;

    RecyclerView event_recycler;
    Button close;
    EventAdapter e_adapter ;
    ArrayList<EventList> mList = new ArrayList<EventList>();

    public MypageEventDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_event_component);

        final Dialog dlg = new Dialog(context);

        event_recycler = findViewById(R.id.event_recycler);
        close = findViewById(R.id.mypage_event_button);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        event_recycler.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        event_recycler.addItemDecoration(new CirclePagerIndicatorDecoration());
        e_adapter = new EventAdapter(mList);
        event_recycler.setAdapter(e_adapter);

        addItem("이건 제목이지롱", "이건 내용이지롱");
        addItem("안녕안녕", "ㅎㅎ히히히");

        snapHelper.attachToRecyclerView(event_recycler);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.cancel();
            }
        });
    }

    public void addItem(String title, String context){
        EventList item = new EventList();

        item.setTitle(title);
        item.setContext(context);

        mList.add(item);
    }
}