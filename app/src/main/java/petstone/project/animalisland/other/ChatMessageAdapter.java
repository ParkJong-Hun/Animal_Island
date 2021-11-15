package petstone.project.animalisland.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import petstone.project.animalisland.R;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    private ArrayList<ChatMessage> messages;
    private Context context;
    private String myUid, whoUid;
    private String myNickname, whoNickname;

    public ChatMessageAdapter(ArrayList<ChatMessage> messages, String myUid, String whoUid, String myNickname, String whoNickname) {
        this.messages = messages;
        this.myUid = myUid;
        this.whoUid = whoUid;
        this.myNickname = myNickname;
        this.whoNickname = whoNickname;
    }

    @Override
    public ChatMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.chat_message, parent, false) ;
        ChatMessageAdapter.ViewHolder viewHolder = new ChatMessageAdapter.ViewHolder(view) ;

        return viewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        ChatMessage thisMessage = messages.get(position);

        Date thisDate = thisMessage.getDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss");
        String dateString = dateFormat.format(thisDate);

        if (thisMessage.uid.equals(auth.getUid())) {
            holder.whoMessage.setVisibility(View.GONE);
            holder.myNickname.setText(myNickname);
            holder.myDate.setText(dateString);
            holder.myReaded.setText(String.valueOf(thisMessage.getReaded()).equals("0") ? "읽음" : "읽지 않음");
            holder.myText.setText(thisMessage.getArticle());
        } else {
            holder.myMessage.setVisibility(View.GONE);
            holder.whoNickname.setText(whoNickname);
            holder.whoDate.setText(dateString);
            holder.whoReaded.setText(String.valueOf(thisMessage.getReaded()).equals("0") ? "읽음" : "읽지 않음");
            holder.whoText.setText(thisMessage.getArticle());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout myMessage, whoMessage;
        TextView myText, whoText, myDate, whoDate, myNickname, whoNickname, myReaded, whoReaded;

        ViewHolder(View chatView) {
            super(chatView);
            myMessage = chatView.findViewById(R.id.chat_message_my);
            whoMessage = chatView.findViewById(R.id.chat_message_who);

            myText = chatView.findViewById(R.id.chat_message_my_text);
            myDate = chatView.findViewById(R.id.chat_message_my_createdAt);
            myNickname = chatView.findViewById(R.id.chat_message_my_name);
            myReaded = chatView.findViewById(R.id.chat_message_my_readed);

            whoText = chatView.findViewById(R.id.chat_message_who_text);
            whoDate = chatView.findViewById(R.id.chat_message_who_createdAt);
            whoNickname = chatView.findViewById(R.id.chat_message_who_name);
            whoReaded = chatView.findViewById(R.id.chat_message_who_readed);
        }
    }
}
