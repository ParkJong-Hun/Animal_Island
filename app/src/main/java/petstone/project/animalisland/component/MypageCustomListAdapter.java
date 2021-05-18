package petstone.project.animalisland.component;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import petstone.project.animalisland.R;

public class MypageCustomListAdapter extends BaseAdapter {
    Context context;
    String[] strings = new String[]{"펫 프렌즈 신청", "유료 분양 권한 신청", "이벤트", "공지사항", "로그아웃"};
    int[] imageId = new int[]{R.drawable.document, R.drawable.document, R.drawable.document, R.drawable.document, R.drawable.password};

    public MypageCustomListAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.mypage_custom_list_component, parent, false);

        ImageView icon;
        TextView text;

        icon = (ImageView) convertView.findViewById(R.id.mypage_custom_list_icon);
        text = (TextView) convertView.findViewById(R.id.mypage_custom_list_text);

        icon.setImageResource(imageId[position]);
        icon.setColorFilter(Color.parseColor("#FF9696"));
        text.setText(strings[position]);

        return convertView;
    }
}

