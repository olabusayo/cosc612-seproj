package com.example.t2cc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.t2cc.FirestoreConnections.ClassRosterCollectionAccessors;
import static com.example.t2cc.FirestoreConnections.ClassesCollectionAccessors;
import static com.example.t2cc.FirestoreConnections.MessagesCollectionAccessors;

public class MessageActivity extends BaseActivity {
  final static String TAG = "T2CC:MessagesActivity";
  MessageAdapter mAdapter;
  RecyclerView mRecyclerView;
  SimpleDateFormat messageDateFormat;
  private List<MessageData> messagesInfo;
  private Map<String, List<MessageData>> messagesInfoHash;

  ProgressBar mProgressBar;
  TextView mEmptyText;

  static void handleSpecificClassMessageClick(MessageActivity mcaObject, String classID,
      String className) {
    Log.d(TAG, "handleSpecificClassMessageClick");
    mcaObject.populateMessagesForIndividualClass(classID, className);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_message);

    messagesInfoHash = new HashMap<>();
    messagesInfo = new ArrayList<>();
    messageDateFormat = new SimpleDateFormat("MMMM dd");

    // get recycle view
    mRecyclerView = findViewById(R.id.messageRecycleView);

    //Set Visibibilty
    mEmptyText = findViewById(R.id.messageEmptyText);
    mProgressBar = findViewById(R.id.messageProgressBar);
    mEmptyText.setVisibility(View.GONE);
    mRecyclerView.setVisibility(View.GONE);


    Intent singleClassMessages = getIntent();
    if (singleClassMessages.hasExtra("CLASS_ID") && singleClassMessages.hasExtra("CLASS_NAME")) {
      //check if this is from my classes view message click
      String classID = singleClassMessages.getStringExtra("CLASS_ID");
      String className = singleClassMessages.getStringExtra("CLASS_NAME");
      populateMessagesForIndividualClass(classID, className).addOnCompleteListener(
          new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
              if (task.isSuccessful()) {
                Log.d(TAG, "setUpAdapterOnCreate:success");
                setupMessagesAdapter(messagesInfo);
              } else {
                Log.w(TAG, "setUpAdapterOnCreate:failure");
              }
            }
          });
    } else {
      populateMessagesViewData().addOnCompleteListener(
          new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
              if (task.isSuccessful()) {
                Log.d(TAG, "setUpAdapterOnCreate:success");
                setupMessagesAdapter(messagesInfo);
              } else {
                Log.w(TAG, "setUpAdapterOnCreate:failure");
              }
            }
          });
    }
  }

  private void setupMessagesAdapter(List<MessageData> messagesInfo) {
    mAdapter = new MessageAdapter(messagesInfo, getApplication());
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
  }

  private Task<QuerySnapshot> populateMessagesViewData() {
    final Map<String, Map<String, Object>> classesInfo = new HashMap<>();
    Task<QuerySnapshot> gatherUsersMessages =
        ClassRosterCollectionAccessors.getUsersSubscribedClassesTask(mCurrentUserID)
            .continueWithTask(
                new Continuation<QuerySnapshot, Task<QuerySnapshot>>() {
                  @Override
                  public Task<QuerySnapshot> then(
                      @NonNull Task<QuerySnapshot> subscribedClassesTask) {
                    if (subscribedClassesTask.isSuccessful()) {
                      QuerySnapshot myClassesResult = subscribedClassesTask.getResult();
                      List<String> subscribedClassIDs = new ArrayList<>();
                      for (DocumentSnapshot cDoc : myClassesResult.getDocuments()) {
                        subscribedClassIDs.add(cDoc.getId());
                      }
                      if (!subscribedClassIDs.isEmpty()) {
                        return ClassesCollectionAccessors
                            .getSpecificClassesInfoTask(subscribedClassIDs);
                      } else {
                        Log.d(TAG, "Couldn't retrieve user's subscribed classes",
                            subscribedClassesTask.getException());
                      }
                    } else {
                      Log.d(TAG, "Couldn't retrieve user's subscribed classes",
                          subscribedClassesTask.getException());
                    }
                    return null;
                  }
                }).continueWithTask(new Continuation<QuerySnapshot, Task<QuerySnapshot>>() {
          @Override
          public Task<QuerySnapshot> then(
              @NonNull Task<QuerySnapshot> classesInfoTask) throws Exception {
            if (classesInfoTask.isSuccessful()) {
              List<String> classIDs = new ArrayList<>();
              QuerySnapshot myClassesResult = classesInfoTask.getResult();
              for (QueryDocumentSnapshot classDocument : myClassesResult) {
                Map<String, Object> classInfo = classDocument.getData();
                String classID = classDocument.getId();
                classesInfo.put(classID, classInfo);
                classIDs.add(classID);
              }
              if (!classIDs.isEmpty()) {
                return MessagesCollectionAccessors.getUsersMessagesTask(classIDs,
                    Utilities.getMessagesCutOffDate());
              } else {
                Log.d(TAG, "No classes found for:" + mCurrentUserID);
              }
            } else {
              Log.w(TAG, "Unable to retrieve class information",
                  classesInfoTask.getException());
            }
            return null;
          }
        }).continueWithTask(new Continuation<QuerySnapshot, Task<QuerySnapshot>>() {
          @Override
          public Task<QuerySnapshot> then(
              @NonNull Task<QuerySnapshot> classMessagesTask) throws Exception {
            if (classMessagesTask.isSuccessful()) {
              Log.d(TAG, "populateMessagesViewData:success");
              QuerySnapshot messagesResult = (QuerySnapshot) classMessagesTask.getResult();
              for (QueryDocumentSnapshot messageDocument : messagesResult) {
                Map<String, Object> messageInfo = messageDocument.getData();
                String classID =
                    (String) messageInfo
                        .get(MessagesCollectionAccessors.mMessagesCollectionFieldClassID);
                Map<String, Object> classInfo = classesInfo.get(classID);

                String className = (String) classInfo.get(
                    ClassesCollectionAccessors.mClassesCollectionFieldTitle);
                String messageContents = (String)
                    messageInfo.get(MessagesCollectionAccessors.mMessagesCollectionFieldContent);
                Timestamp sentTimeStamp =
                    (Timestamp) messageInfo
                        .get(MessagesCollectionAccessors.mMessagesCollectionFieldSentTime);
                String sentFormattedDate = messageDateFormat.format(sentTimeStamp.toDate());
                MessageData md = new MessageData(className,
                    sentFormattedDate, messageContents);
                if (!messagesInfoHash.containsKey(classID)) {
                  messagesInfoHash.put(classID, new ArrayList<MessageData>());
                }
                messagesInfoHash.get(classID).add(md);
              }
              messagesInfo.clear();
              for (List<MessageData> lmd : messagesInfoHash.values()) {
                messagesInfo.addAll(lmd);
              }
              if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
              }
            } else {
              Log.w(TAG, "populateMessagesViewData:failure:", classMessagesTask.getException());
            }
            return classMessagesTask;
          }
        });
    return gatherUsersMessages;
  }

  private Task<QuerySnapshot> populateMessagesForIndividualClass(String classID,
      final String className) {

    Task<QuerySnapshot> gatherClassMessages =
        MessagesCollectionAccessors.getMessagesForSpecificClass(classID,
            Utilities.getMessagesCutOffDate()).continueWithTask(
            new Continuation<QuerySnapshot, Task<QuerySnapshot>>() {
              @Override
              public Task<QuerySnapshot> then(
                  @NonNull Task<QuerySnapshot> classMessagesTask) throws Exception {
                if (classMessagesTask.isSuccessful()) {
                  Log.d(TAG, "populateMessagesViewData:success");
                  QuerySnapshot messagesResult = (QuerySnapshot) classMessagesTask.getResult();
                  for (QueryDocumentSnapshot messageDocument : messagesResult) {
                    Map<String, Object> messageInfo = messageDocument.getData();
                    String classID =
                        (String) messageInfo
                            .get(MessagesCollectionAccessors.mMessagesCollectionFieldClassID);

                    String messageContents = (String)
                        messageInfo
                            .get(MessagesCollectionAccessors.mMessagesCollectionFieldContent);
                    Timestamp sentTimeStamp =
                        (Timestamp) messageInfo
                            .get(MessagesCollectionAccessors.mMessagesCollectionFieldSentTime);
                    String sentFormattedDate = messageDateFormat.format(sentTimeStamp.toDate());
                    MessageData md = new MessageData(className,
                        sentFormattedDate, messageContents);
                    if (!messagesInfoHash.containsKey(classID)) {
                      messagesInfoHash.put(classID, new ArrayList<MessageData>());
                    }
                    messagesInfoHash.get(classID).add(md);
                  }
                  messagesInfo.clear();
                  for (List<MessageData> lmd : messagesInfoHash.values()) {
                    messagesInfo.addAll(lmd);
                  }
                  if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                  }
                } else {
                  Log.w(TAG, "populateMessagesViewData:failure:", classMessagesTask.getException());
                }
                return classMessagesTask;
              }
            });
    return gatherClassMessages;
  }
}

// define data
class MessageData {
  String classNameLabel;
  String dateLabel;
  String messageTextField;

  MessageData(String classNameLabel, String dateLabel,
      String messageTextField) {
    this.classNameLabel = classNameLabel;
    this.dateLabel = dateLabel;
    this.messageTextField = messageTextField;
  }
}
