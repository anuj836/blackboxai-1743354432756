package com.example.rideshareneon;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rideshareneon.adapters.MessageAdapter;
import com.example.rideshareneon.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String confirmationId;
    private String userId;
    private MessageAdapter adapter;
    private List<Message> messages = new ArrayList<>();
    private TextView tvTypingStatus;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        confirmationId = getIntent().getStringExtra("confirmationId");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        tvTypingStatus = findViewById(R.id.tvTypingStatus);

        RecyclerView rvMessages = findViewById(R.id.rvMessages);
        EditText etMessage = findViewById(R.id.etMessage);
        
        adapter = new MessageAdapter(messages, userId);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(adapter);

        // Emoji button
        ImageButton btnEmoji = findViewById(R.id.btnEmoji);
        btnEmoji.setOnClickListener(v -> {
            // TODO: Implement emoji picker dialog
            // For now, just insert a smiley emoji
            EditText etMessage = findViewById(R.id.etMessage);
            int cursorPos = etMessage.getSelectionStart();
            etMessage.getText().insert(cursorPos, "😊");
        });

        // Send message button
        findViewById(R.id.btnSend).setOnClickListener(v -> {
            String messageText = etMessage.getText().toString().trim();
            if (!messageText.isEmpty()) {
                sendMessage(messageText);
                etMessage.setText("");
            }
        });

        // Typing indicator
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTypingStatus(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    updateTypingStatus(false);
                }
            }
        });

        loadMessages();
        setupTypingListener();
    }

    private void sendMessage(String text) {
        Message message = new Message(
            userId,
            text,
            new Date().getTime()
        );
        
        mDatabase.child("chats").child(confirmationId).push().setValue(message)
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
            });
    }

    private void loadMessages() {
        mDatabase.child("chats").child(confirmationId).addValueEventListener(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    messages.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Message message = snapshot.getValue(Message.class);
                        if (message != null) {
                            messages.add(message);
                            // Mark messages as read if they're from the other user
                            if (!message.getSenderId().equals(userId) && !message.isRead()) {
                                snapshot.getRef().child("isRead").setValue(true);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (!messages.isEmpty()) {
                        ((RecyclerView) findViewById(R.id.rvMessages))
                            .scrollToPosition(messages.size() - 1);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ChatActivity.this, 
                        "Failed to load messages", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void updateTypingStatus(boolean isTyping) {
        Map<String, Object> typingUpdate = new HashMap<>();
        typingUpdate.put("users/" + userId + "/isTyping", isTyping);
        mDatabase.updateChildren(typingUpdate);
    }

    private void setupTypingListener() {
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.getKey().equals(userId) && 
                        snapshot.child("isTyping").getValue(Boolean.class)) {
                        tvTypingStatus.setText("Driver is typing...");
                        tvTypingStatus.setVisibility(View.VISIBLE);
                        return;
                    }
                }
                tvTypingStatus.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateTypingStatus(false);
    }
}