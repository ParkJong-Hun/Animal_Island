package petstone.project.animalisland.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import petstone.project.animalisland.R;

public class MypageNewsDialog extends Dialog {
    private Context context;

    RecyclerView news_recycler;
    Button close;
    NewsAdapter n_adapter ;
    ArrayList<NewsList> mList = new ArrayList<NewsList>();
    FirebaseFirestore db;

    public MypageNewsDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_news_component);

        news_recycler = findViewById(R.id.news_recycler);
        close = findViewById(R.id.mypage_news_button);

        db = FirebaseFirestore.getInstance();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        news_recycler.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        news_recycler.addItemDecoration(new CirclePagerIndicatorDecoration());
        n_adapter = new NewsAdapter(mList);
        news_recycler.setAdapter(n_adapter);

        /*
        db.collection("mypage_news")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot document : value) {
                            addItem(document.getData().get("news_title").toString(),
                                    document.getData().get("news_article").toString()) ;
                        }
                    }
                });
         */


        addItem("서비스 이용 일시 중단 안내", "정보 보호 관리 체계 인증 및 서버 안정화를 위한 서버 증설 작업을 실시합니다. \n작업 시간 동안에는 ANIMAL ISLAND의 모든 서비스 이용이 전면 중단되오니 이용에 참고 부탁드립니다. \n\n중단 일시\n2021년 12월 15일 수요일 00:00~06:00(6시간)");
        addItem("유료 분양 권한 신청 안내", "동물보호법 개정에 따라 각 지자체에서 동물 판매업으로 허가를 받지 않은 업체나 가정에서 반려동물을 유료로 분양하면 모두 불법입니다. \n따라서 저희 ANIMAL ISLAND는 유료 분양 권한 신청을 통해 유료 분양이 가능한지를 심사하고 유료 분양이 가능한 기준이 된다면 게시글을 작성할 수 있는 권한을 얻을 수 있습니다.");
        addItem("분양 후 주의사항", "반려동물은 적절한 보호와 보살핌이 필요합니다.\n→전염병 예방을 위해 정기적으로 예방접종 및 구충 필요\n\n질병 발생 시 판매처에 즉시 연락해야 합니다.\n→반려동물의 건강에 이상이 생겼을 경우 계약 내용을 확인하여, 즉시 판매처에 연락하고 적절한 치료를 받도록 의뢰 또는 협의");

        snapHelper.attachToRecyclerView(news_recycler);

    }

    public void addItem(String title, String content){
        NewsList item = new NewsList();

        item.setTitle(title);
        item.setContent(content);

        mList.add(item);
    }
}