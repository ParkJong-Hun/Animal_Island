package petstone.project.animalisland.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import petstone.project.animalisland.R;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    private ArrayList<ChatMessage> messages = null ;
    private Context context;

    public ChatMessageAdapter(ArrayList<ChatMessage> messages) {
        this.messages = messages ;
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

    }

    @Override
    public int getItemCount() {
        return messages.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout myMessage, whoMessage;

        ViewHolder(View itemView) {
            super(itemView);
            myMessage.findViewById(R.id.chat_message_my);
            whoMessage.findViewById(R.id.chat_message_who);
        }
    }
}
