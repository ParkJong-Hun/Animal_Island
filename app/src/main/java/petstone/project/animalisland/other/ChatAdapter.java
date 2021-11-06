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

public class ChatAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> updatedMessages = new ArrayList<>();
    ArrayList<Integer> updatedCounts = new ArrayList<Integer>();
    ArrayList<Timestamp> updatedAts = new ArrayList<Timestamp>();

    public ChatAdapter(Context context, ArrayList<String> names, ArrayList<String> updatedMessages, ArrayList<Integer> updatedCounts, ArrayList<Timestamp> updatedAts) {
        this.context = context;
        this.names = names;
        this.updatedMessages = updatedMessages;
        this.updatedCounts = updatedCounts;
        this.updatedAts = updatedAts;
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
        Date date = updatedAts.get(position).toDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateStr = dateFormat.format(date);
        updatedAt.setText(dateStr);
        updatedCount.setText(updatedCounts.get(position));

        return convertView;
    }
}

