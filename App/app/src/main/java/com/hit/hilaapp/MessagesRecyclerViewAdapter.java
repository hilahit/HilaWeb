package com.hit.hilaapp;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MessagesRecyclerViewAdapter
        extends FirebaseRecyclerAdapter<MessageModel, MessagesRecyclerViewAdapter.MessagesViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MessagesRecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<MessageModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessagesViewHolder holder, int position, @NonNull MessageModel message) {
        holder.messageTv.setText(message.getMessage());
        Log.d("alih", "onBindViewHolder: " + message.getMessage());

//        RelativeLayout.LayoutParams layoutParams =
//                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        if (message.getSenderName().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
//            holder.messageCv.setCardBackgroundColor(Color.parseColor("#d5e4f5"));
//        } else {
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
//            holder.messageCv.setCardBackgroundColor(Color.parseColor("#fdeac3"));
//        }
//
//        holder.messageCv.setLayoutParams(layoutParams);
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_message, parent, false);

        return new MessagesViewHolder(view);
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {

        private TextView messageTv;
        private CardView messageCv;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);

            messageTv = itemView.findViewById(R.id.message_tv);
            messageCv = itemView.findViewById(R.id.message_cv);
        }
    }
}
