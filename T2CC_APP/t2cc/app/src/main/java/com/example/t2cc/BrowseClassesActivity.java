package com.example.t2cc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.t2cc.FirestoreConnections.ClassRosterCollectionAccessors;
import com.example.t2cc.FirestoreConnections.ClassesCollectionAccessors;
import com.example.t2cc.FirestoreConnections.SubscriptionRequestsCollectionAccessors;
import com.example.t2cc.FirestoreConnections.TeacherCollectionAccessors;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import org.apache.commons.lang3.StringUtils;

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
              if (mAdapter.getItemCount() == 0) {
                mEmptyText.setVisibility(View.VISIBLE);
              } else {
                mAdapter.notifyDataSetChanged();
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
          final QuerySnapshot allClassesResult = (QuerySnapshot) docs.get(0);
          final QuerySnapshot myClassesResult = (QuerySnapshot) docs.get(1);
          final QuerySnapshot myRequestsResult = (QuerySnapshot) docs.get(2);

          final List<String> tl = new ArrayList<>();
          for (DocumentSnapshot doc : allClassesResult.getDocuments()) {
            tl.add((String) doc.get("teacher_id"));
          }
          final Map<String, Map<String, Object>> allTeachersInfoHash = new HashMap<>();
          TeacherCollectionAccessors.mTeachersRef
              .get()
              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                  if (task.isSuccessful()) {
                    QuerySnapshot allTeachersResult = task.getResult();
                    for (DocumentSnapshot teacherInfo : allTeachersResult) {
                      String teacherID = teacherInfo.getId();
                      if (tl.contains(teacherID)) {
                        allTeachersInfoHash.put(teacherID, teacherInfo.getData());
                      }
                    }

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
                      String classNum = String.format("%1$10s%2$10s",
                          classInfo
                              .get(ClassesCollectionAccessors.mClassesCollectionFieldCourseNumber),
                          classInfo.get(ClassesCollectionAccessors.mClassesCollectionFieldSection));
                      String teacherID =
                          (String) classInfo
                              .get(ClassesCollectionAccessors.mClassesCollectionFieldTeacherID);
                      Map<String, Object> teacherInfo = allTeachersInfoHash.get(teacherID);
                      String teacherName = String.format("%s %s",
                          StringUtils.capitalize((String) teacherInfo
                              .get(TeacherCollectionAccessors.mTeacherCollectionFieldFirstName)),
                          StringUtils.capitalize((String) teacherInfo
                              .get(TeacherCollectionAccessors.mTeacherCollectionFieldLastName)));
                      String requestStatus = hasBeenRequested ? "pending" : "none";
                      ClassListInformation cli = new ClassListInformation(
                          BrowseClassesActivity.this,
                          classID, className, classNum, teacherName, requestStatus);
                      availableClassesInfoHash.put(classID, cli);
                    }
                    availableClassesInfo.clear();
                    availableClassesInfo.addAll(availableClassesInfoHash.values());
                    if (mAdapter != null) {
                      mAdapter.notifyDataSetChanged();
                      if (mAdapter.getItemCount() > 0) {
                        mEmptyText.setVisibility(View.GONE);
                      }
                    }

                  } else {
                    Log.w(TAG, "Get Teacher info failed", task.getException());
                  }
                }
              });
        } else {
          Log.w(TAG, "populateAvailableClassesViewData:failure:", task.getException());
        }
      }
    });
    return populateClassList;
  }

  void handleSubscriptionToggle(final BrowseClassesActivity bcaObject, final String classID,
      final Switch mSubscribeSwitch, final TextView requestStatus) {
    Log.d(TAG, "handleSubscriptionToggle");
    whenAllSuccess(ClassRosterCollectionAccessors.getUsersSubscribedClassesTask(mCurrentUserID),
        SubscriptionRequestsCollectionAccessors.getUsersSubscriptionRequests(mCurrentUserID))
        .addOnCompleteListener(new OnCompleteListener<List<Object>>() {
          @Override
          public void onComplete(@NonNull Task<List<Object>> combinedTask) {
            if (combinedTask.isSuccessful()) {
              List<Object> combinedResult = combinedTask.getResult();
              QuerySnapshot subscribedClasses = (QuerySnapshot) combinedResult.get(0);
              QuerySnapshot subscriptionRequest = (QuerySnapshot) combinedResult.get(1);
              int currentlySubscribedClasses = subscribedClasses.getDocuments().size();
              int currentlyRequestedSubscriptions = subscriptionRequest.getDocuments().size();
              if ((currentlyRequestedSubscriptions + currentlySubscribedClasses) < 10) {
                bcaObject.sendSubscriptionRequest(classID);
              } else {
                mSubscribeSwitch.setChecked(false);
                requestStatus.requestFocus();
                requestStatus.setError("Subscription/Subscription Request Limit Reached!");
              }
            } else {
              Log.d(TAG, "Couldn't retrieve student subscribed classes; Allow subscription",
                  combinedTask.getException());
              bcaObject.sendSubscriptionRequest(classID);
            }
          }
        });
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


