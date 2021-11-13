package petstone.project.animalisland.other;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.rpc.context.AttributeContext;

import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.net.URL;
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
        ImageView profile = convertView.findViewById(R.id.chat_user_img);

        nickname.setText(lists.get(position).getWhoName());
        updatedMessage.setText(lists.get(position).getUpdatedMessage());
        Date date = lists.get(position).getUpdatedDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateStr = dateFormat.format(date);
        updatedAt.setText(dateStr);
        updatedCount.setText(lists.get(position).getNewCount().toString());

        if(updatedCount.getText().equals("0")) {
            updatedCount.setVisibility(View.GONE);
        } else {
            updatedCount.setVisibility(View.VISIBLE);
        }

        StorageReference ref = FirebaseStorage.getInstance().getReference("profileImages/" + lists.get(position).getUid() + ".jpg");

        Glide.with(convertView.getContext())
                .load(ref)
                .placeholder(R.drawable.mypage_colored)
                .fallback(R.drawable.mypage_colored)
                .error(R.drawable.mypage_colored)
                .centerCrop()
                .into(profile);
        return convertView;
    }

}

