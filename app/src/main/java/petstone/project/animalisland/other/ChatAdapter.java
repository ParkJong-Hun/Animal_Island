package petstone.project.animalisland.other;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import petstone.project.animalisland.R;

public class ChatAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> updatedMessages = new ArrayList<>();
    ArrayList<Integer> updatedCounts = new ArrayList<Integer>();
    ArrayList<Integer> updatedAts = new ArrayList<Integer>();

    public ChatAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        return names.size();
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

        TextView nickname = convertView.findViewById(R.id.chat_nickname);
        TextView updatedMessage = convertView.findViewById(R.id.chat_updated_message);
        TextView updatedCount = convertView.findViewById(R.id.chat_updated_count);
        TextView updatedAt = convertView.findViewById(R.id.chat_updatedAt);

        nickname.setText(names.get(position));
        updatedMessage.setText(updatedMessages.get(position));
        updatedAt.setText(updatedAts.get(position));
        updatedCount.setText(updatedCounts.get(position));

        return convertView;
    }
}

