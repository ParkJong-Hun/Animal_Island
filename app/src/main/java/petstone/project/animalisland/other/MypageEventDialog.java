package petstone.project.animalisland.other;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import petstone.project.animalisland.R;

public class MypageEventDialog {
    private Context context;
    String article;

    public MypageEventDialog(Context context) {
        this.context = context;
    }

    public void addArticle(String article) {
        this.article = article;
    }

    public void show() {
        final Dialog dlg = new Dialog(context);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.mypage_event_component);
        dlg.show();
        TextView article_tv = dlg.findViewById(R.id.mypage_event_article);
        article_tv.setText(article);
        Button close = dlg.findViewById(R.id.mypage_event_button);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
    }
}