package com.hit.hilaapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final public class ChatFragment extends Fragment {


    public interface ChatListener {
        void onContentReceived();
    }

    private ChatListener listener;
    private RecyclerView mMessagesRecyclerView;
    private MessagesRecyclerViewAdapter mMessagesAdapter;

    private DatabaseReference mRootDbReference;
    private DatabaseReference mUserChatsReference;

    private EditText userInputET;
    private TextView contactNameTV;

    private String mContactName;
    private int mContactID;
    private String mChatId;
    private int messageCount = 0;

    public static ChatFragment newInstance(String contactName, int contactID) {
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ContactListFragment.CONTACT_NAME, contactName);
        bundle.putInt(ContactListFragment.CONTACT_ID, contactID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ChatListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ChatListener interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initViews(view);

        mContactName = requireArguments().getString(ContactListFragment.CONTACT_NAME, "n/a");
        mContactID = requireArguments().getInt(ContactListFragment.CONTACT_ID);

        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mChatId = userID + "_" + mContactID;

        mRootDbReference = FirebaseDatabase.getInstance().getReference();
        mUserChatsReference = mRootDbReference.child("Chats").child(mChatId);

        FirebaseRecyclerOptions<MessageModel> options = new FirebaseRecyclerOptions.Builder<MessageModel>()
                .setLifecycleOwner(this)
                .setQuery(mUserChatsReference, MessageModel.class)
                .build();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        linearLayoutManager.setStackFromEnd(true);
        mMessagesRecyclerView.setLayoutManager(linearLayoutManager);
        mMessagesAdapter = new MessagesRecyclerViewAdapter(options);

        mMessagesRecyclerView.setAdapter(mMessagesAdapter);

        // get all messages
//        mUserChatsReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
//                if (task.isSuccessful()) {
//                    Log.d("alih", "onComplete: " + task.getResult());
////                    String chatID = task.getResult().getValue(String.class);
////                    Log.d("hila", "onComplete: " + chatID);
//                    loadMessages();
//                }
//                else {
//                    Log.d("TAG", "onComplete: failed " + Objects.requireNonNull(task.getException()).toString());
//                }
//            }
//        });

        mUserChatsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable String previousChildName) {
                messageCount++;
                Log.d("alih", "onChildAdded: message count: " + messageCount);

            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("alih", "onChildChanged: " + snapshot.getValue());
                Log.d("alih", "onChildChanged: previousChildName: " + previousChildName);
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                Log.d("alih", "onChildRemoved: " + snapshot.getValue());
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, String previousChildName) {
                Log.d("alih", "onChildMoved: " + snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("alih", "onCancelled: " + error.getMessage());
            }
        });
    }

    private void initViews(View view) {

        mMessagesRecyclerView = view.findViewById(R.id.chat_recycler_view);
        mMessagesRecyclerView.hasFixedSize();
        contactNameTV = view.findViewById(R.id.contact_name_tv);
        userInputET = view.findViewById(R.id.chat_input_text_et);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMessagesAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        mMessagesAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("alih", "onDestroy: chat fragment destroyed");
    }

    private void loadMessages() {


    }
}
