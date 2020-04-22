package com.example.t2cc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.t2cc.FirestoreConnections.ClassRosterCollectionAccessors;
import com.example.t2cc.FirestoreConnections.ClassesCollectionAccessors;
import com.example.t2cc.FirestoreConnections.SubscriptionRequestsCollectionAccessors;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.google.android.gms.tasks.Tasks.whenAllSuccess;

public class BrowseClassesActivity extends BaseActivity {

  final static String TAG = "T2CC:BrowseClasses";
  private List<ClassListInformation> availableClassesInfo;
  private Map<String, ClassListInformation> availableClassesInfoHash;
  private BrowseClassesAdapter mAdapter;
  private RecyclerView mRecyclerView;
  private ProgressBar mProgressBar;
  private TextView mEmptyText;

  // Calendar
  private Calendar mCal;
  private Integer currentYear;
  private String currentAcademicTerm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browseclass);

    // get recycle view
    mProgressBar = findViewById(R.id.browseClassProgressBar);
    mEmptyText = findViewById(R.id.browseClassEmptyText);
    mRecyclerView = findViewById(R.id.subscribe2ClassRecycleView);
    mRecyclerView.setVisibility(View.GONE);
    mEmptyText.setVisibility(View.GONE);

    findViewById(R.id.subscribe2ClassSubBodyLabel).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            populateAvailableClassesViewData();
          }
        });

    mCal = Calendar.getInstance();
    currentYear = mCal.get(Calendar.YEAR);
    currentAcademicTerm = Utilities.getCurrentTerm(mCal.getTime(), currentYear);

    availableClassesInfoHash = new HashMap<>();
    availableClassesInfo = new ArrayList<>();

    populateAvailableClassesViewData().addOnCompleteListener(
        new OnCompleteListener<List<Object>>() {
          @Override
          public void onComplete(@NonNull Task<List<Object>> task) {
            if (task.isSuccessful()) {
              Log.d(TAG, "setUpAdapterOnCreate:success");
              setupBrowseClassesAdapter(availableClassesInfo);
              mProgressBar.setVisibility(View.GONE);
              mRecyclerView.setVisibility(View.VISIBLE);
              if(mAdapter.getItemCount() == 0){
                mEmptyText.setVisibility(View.VISIBLE);
              }

            } else {
              Log.w(TAG, "setUpAdapterOnCreate:failure");
              mProgressBar.setVisibility(View.GONE);
              mEmptyText.setVisibility(View.VISIBLE);
            }
          }
        });

  }

  private void setupBrowseClassesAdapter(List<ClassListInformation> availableClassesInfo) {
    mAdapter = new BrowseClassesAdapter(availableClassesInfo, getApplication());
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(BrowseClassesActivity.this));
  }

  private Task<List<Object>> populateAvailableClassesViewData() {
    Task<List<Object>> populateClassList =
        whenAllSuccess(
            ClassesCollectionAccessors.getAvailableClassesTask(currentYear.toString(),
                currentAcademicTerm),
            ClassRosterCollectionAccessors.getUsersSubscribedClassesTask(mCurrentUserID),
            SubscriptionRequestsCollectionAccessors.getUsersSubscriptionRequests(mCurrentUserID));
    populateClassList.addOnCompleteListener(new OnCompleteListener<List<Object>>() {
      @Override
      public void onComplete(@NonNull Task<List<Object>> task) {
        List<Object> docs;
        if (task.isSuccessful() && ((docs = task.getResult()) != null && !docs.isEmpty())) {
          Log.d(TAG, "populateAvailableClassesViewData:success");
          QuerySnapshot allClassesResult = (QuerySnapshot) docs.get(0);
          QuerySnapshot myClassesResult = (QuerySnapshot) docs.get(1);
          QuerySnapshot myRequestsResult = (QuerySnapshot) docs.get(2);

          for (QueryDocumentSnapshot classDocument : allClassesResult) {
            Map<String, Object> classInfo = classDocument.getData();
            String classID = classDocument.getId();
            //check if student subscribed
            boolean isSubscribed = false;
            for (DocumentSnapshot d : myClassesResult.getDocuments()) {
              if (d.getId().equals(classID)) {
                isSubscribed = true;
                break;
              }
            }
            if (isSubscribed) {
              // we dont want to add subscribed classes to available class list
              continue;
            }
            //check is student requested
            boolean hasBeenRequested = false;
            for (DocumentSnapshot d : myRequestsResult.getDocuments()) {
              if (d.getId().equals(classID)) {
                hasBeenRequested = true;
                break;
              }
            }

            String className = (String) classInfo.get(
                ClassesCollectionAccessors.mClassesCollectionFieldTitle);
            String classNum = String.format("%s-%s",
                classInfo.get(ClassesCollectionAccessors.mClassesCollectionFieldCourseNumber),
                classInfo.get(ClassesCollectionAccessors.mClassesCollectionFieldSection));
            String requestStatus = hasBeenRequested ? "pending" : "none";
            ClassListInformation cli = new ClassListInformation(BrowseClassesActivity.this,
                classID, className, classNum, requestStatus);
            availableClassesInfoHash.put(classID, cli);
          }
          availableClassesInfo.clear();
          availableClassesInfo.addAll(availableClassesInfoHash.values());
          if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
          }
        } else {
          Log.w(TAG, "populateAvailableClassesViewData:failure:", task.getException());
        }
      }
    });
    return populateClassList;
  }

  void handleSubscriptionToggle(BrowseClassesActivity bcaObject, String classID) {
    Log.d(TAG, "handleSubscriptionToggle");
    bcaObject.sendSubscriptionRequest(classID);
  }

  private void sendSubscriptionRequest(final String classID) {
    Log.d(TAG, "sendSubscriptionRequest");
    final DocumentReference classRequestRef = SubscriptionRequestsCollectionAccessors.mSubscriptionRequestsRef
        .document(classID);
    mFBDB.runTransaction(new Transaction.Function<Void>() {
      @Nullable
      @Override
      public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
        DocumentSnapshot cReqDoc = transaction.get(classRequestRef);
        if (cReqDoc.exists()) {
          transaction.update(classRequestRef,
              SubscriptionRequestsCollectionAccessors.mSubscriptionRequestsCollectionFieldRequests,
              FieldValue.arrayUnion(mCurrentUserID));
        } else {
          Map<String, Object> req = new HashMap<>();
          req.put(
              SubscriptionRequestsCollectionAccessors.mSubscriptionRequestsCollectionFieldRequests,
              FieldValue.arrayUnion(mCurrentUserID));
          classRequestRef.set(req);
        }
        return null;
      }
    }).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
          Log.d(TAG, "subscriptionRequested:success: " + classID);
          Objects.requireNonNull(availableClassesInfoHash.get(classID)).toggleRequestStatus();
          mAdapter.notifyDataSetChanged();
        } else {
          Log.w(TAG, "subscriptionRequested:failure: " + classID, task.getException());
        }
      }
    });
  }
}


