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

public class MypageNewsDialog extends Dialog {
    private Context context;

    RecyclerView news_recycler;
    Button close;
    NewsAdapter n_adapter ;
    ArrayList<NewsList> mList = new ArrayList<NewsList>();

    public MypageNewsDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_news_component);

        final Dialog dlg = new Dialog(context);

        news_recycler = findViewById(R.id.news_recycler);
        close = findViewById(R.id.mypage_news_button);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        news_recycler.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        news_recycler.addItemDecoration(new CirclePagerIndicatorDecoration());
        n_adapter = new NewsAdapter(mList);
        news_recycler.setAdapter(n_adapter);

        addItem("이건 제목이지롱", "이건 내용이지롱");
        addItem("나 너무 졸려용", "오류 그_만");
        addItem("헤헤헤헤하하히", "아아아아ㅏ아아아ㅏ 자고싶어여");

        snapHelper.attachToRecyclerView(news_recycler);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }

    public void addItem(String title, String context){
        NewsList item = new NewsList();

        item.setTitle(title);
        item.setContext(context);

        mList.add(item);
    }
}