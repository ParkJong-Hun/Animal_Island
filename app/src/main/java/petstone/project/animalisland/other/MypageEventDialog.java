package petstone.project.animalisland.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

        event_recycler = findViewById(R.id.event_recycler);
        close = findViewById(R.id.mypage_event_button);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        event_recycler.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        event_recycler.addItemDecoration(new CirclePagerIndicatorDecoration());
        e_adapter = new EventAdapter(mList);
        event_recycler.setAdapter(e_adapter);

        /*이벤트 DB
        db.collection("mypage_event")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot document : value) {
                            addItem(document.getData().get("events_title").toString(),
                                    document.getData().get("events_article").toString()) ;
                        }
                    }
                });
         */

        addItem("♡펫 프렌즈 이용 고객님♡", "저희 펫 프렌즈를 이용해주셔서 대단히 감사합니다♡\n저희를 믿고 맡겨주신 그 마음 보답해드리고자 ANIMAL ISLAND 팀이 이벤트를 준비했습니다♡\n\n10월 31일 하루 !! 펫 프렌즈 이용 고객님께 !!\n펫 프렌즈 이용 금액 30%를 지원해드립니다 !!\n많은 이용과 관심 부탁드립니다♡");
        addItem("유기·유실동물 입양비 지원", "지자체 동물보호센터에서 보호하고 있는 유기·유실동물을 반려의 목적으로 입양할 경우 동물 등록비 및 중성화 수술비 등을 지원 받을 수 있습니다.\n\n\n아래의 링크를 참고해주세요. \nhttps://www.animal.go.kr");

        snapHelper.attachToRecyclerView(event_recycler);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void addItem(String title, String content){
        EventList item = new EventList();

        item.setTitle(title);
        item.setContent(content);

        mList.add(item);
    }
}