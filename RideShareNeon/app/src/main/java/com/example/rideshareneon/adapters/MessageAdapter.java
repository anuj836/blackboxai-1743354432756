package com.example.rideshareneon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rideshareneon.R;
import com.example.rideshareneon.models.Message;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messages;
    private String currentUserId;

    public MessageAdapter(List<Message> messages, String currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        
        // Set sender name
        String senderName = message.getSenderId().equals(currentUserId) ? "You" : "Driver";
        holder.tvSender.setText(senderName);
        
        // Handle image or text message
        if (message.getImageUrl() != null && !message.getImageUrl().isEmpty()) {
            holder.tvMessage.setVisibility(View.GONE);
            holder.ivImage.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                .load(message.getImageUrl())
                .into(holder.ivImage);
            
            holder.ivImage.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), ImageViewerActivity.class);
                intent.putExtra("image_url", message.getImageUrl());
                holder.itemView.getContext().startActivity(intent);
            });
        } else {
            holder.tvMessage.setVisibility(View.VISIBLE);
            holder.ivImage.setVisibility(View.GONE);
            holder.tvMessage.setText(message.getText());
        }
        
        // Format and set timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        holder.tvTimestamp.setText(sdf.format(message.getTimestamp()));
        
        // Align messages based on sender
        if (message.getSenderId().equals(currentUserId)) {
            holder.tvSender.setTextColor(holder.itemView.getContext()
                .getResources().getColor(R.color.neon_green));
            // Show read receipt for sender's messages
            if (message.isRead()) {
                holder.tvTimestamp.setText(holder.tvTimestamp.getText() + " ✓✓");
                holder.tvTimestamp.setTextColor(holder.itemView.getContext()
                    .getResources().getColor(R.color.neon_blue));
            } else {
                holder.tvTimestamp.setText(holder.tvTimestamp.getText() + " ✓");
                holder.tvTimestamp.setTextColor(holder.itemView.getContext()
                    .getResources().getColor(R.color.text_secondary));
            }
        } else {
            holder.tvSender.setTextColor(holder.itemView.getContext()
                .getResources().getColor(R.color.neon_blue));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvSender;
        TextView tvMessage;
        ImageView ivImage;
        TextView tvTimestamp;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSender = itemView.findViewById(R.id.tvSender);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }
}