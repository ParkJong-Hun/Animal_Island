package petstone.project.animalisland.other;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.rpc.context.AttributeContext;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import petstone.project.animalisland.R;

public class ChatListAdapter extends BaseAdapter {
    Context context;
    ArrayList<ChatList> lists = new ArrayList<>();

    public ChatListAdapter(Context context, ArrayList<ChatList> lists) {
        this.context = context;
        this.lists = lists;
    }
    @Override
    public int getCount() {
        return lists!=null || !lists.isEmpty() ? lists.size() : 0;
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

        nickname.setText(lists.get(position).getWhoName());
        updatedMessage.setText(lists.get(position).getUpdatedMessage());
        Date date = lists.get(position).getUpdatedDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateStr = dateFormat.format(date);
        updatedAt.setText(dateStr);
        updatedCount.setText(lists.get(position).getNewCount().toString());

        return convertView;
    }
}

