package petstone.project.animalisland.other;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import petstone.project.animalisland.R;

public class ChatImsiAdapter extends BaseAdapter {
    Context context;
    String[] strings = new String[]{"홍길동"};

    public ChatImsiAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        return strings.length;
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
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.chat_list, parent, false);

        return convertView;
    }
}

