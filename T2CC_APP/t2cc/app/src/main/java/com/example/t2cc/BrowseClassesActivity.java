package com.example.t2cc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.tasks.Tasks.whenAllSuccess;

public class BrowseClassesActivity extends BaseActivity implements
    FirestoreConnections.ClassesCollectionAccessors,
    FirestoreConnections.ClassRosterCollectionAccessors,
    FirestoreConnections.SubscriptionRequestsCollectionAccessors {

  final static String TAG = "T2CC:BrowseClasses";
  private List<ClassListInformation> availableClassesInfo;
  private Map<String, ClassListInformation> availableClassesInfoHash;
  private BrowseClassesAdapter mAdapter;
  private RecyclerView mRecyclerView;
  private ProgressBar mProgressBar;
  // firebase
  private CollectionReference mClassesRef;
  private CollectionReference mClassRosterRef;
  private CollectionReference mSubscriptionRequestsRef;

  // Calendar
  private Calendar mCal;
  private Date currentDate;
  private Integer currentYear;
  private String currentAcademicTerm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browseclass);

    // get recycle view
    mProgressBar = findViewById(R.id.browseClassProgressBar);
    mRecyclerView = findViewById(R.id.subscribe2ClassRecycleView);
    mRecyclerView.setVisibility(View.GONE);

    //Calendar
    mCal = Calendar.getInstance();
    // firebase
    mClassesRef = mFBDB.collection(mClassesCollection);
    mClassRosterRef = mFBDB.collection(mClassRosterCollection);
    mSubscriptionRequestsRef = mFBDB.collection(mSubscriptionRequestsCollection);
    findViewById(R.id.subscribe2ClassSubBodyLabel).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            populateAvailableClassesViewData();
          }
        });

    // list lookup info
    currentYear = mCal.get(Calendar.YEAR);
    currentDate = mCal.getTime();
    currentAcademicTerm = getCurrentTerm(currentDate, currentYear);

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

            } else {
              Log.w(TAG, "setUpAdapterOnCreate:failure");
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
    Task<List<Object>> populateClassList = whenAllSuccess(getAvailableClassesTask(),
        getUsersSubscribedClassesTask(mCurrentUserID),
        getUsersSubscriptionRequests(mCurrentUserID));
    populateClassList.addOnCompleteListener(new OnCompleteListener<List<Object>>() {
      @Override
      public void onComplete(@NonNull Task<List<Object>> task) {
        if (task.isSuccessful()) {
          Log.d(TAG, "populateAvailableClassesViewData:success");
          List<Object> docs = task.getResult();
          QuerySnapshot allClassesResult = (QuerySnapshot) docs.get(0);
          QuerySnapshot myClassesResult = (QuerySnapshot) docs.get(1);
          QuerySnapshot myRequestsResult = (QuerySnapshot) docs.get(2);

          for (QueryDocumentSnapshot classDocument : allClassesResult) {
            Map<String, Object> classInfo = classDocument.getData();
            String classID = classDocument.getId();
            //check if student subscribed
            Boolean isSubcribed = false;
            for (DocumentSnapshot d : myClassesResult.getDocuments()) {
              if (d.getId().equals(classID)) {
                isSubcribed = true;
                break;
              }
            }
            if (isSubcribed) {
              // we dont want to add subscribed classes to available class list
              continue;
            }
            //check is student requested
            Boolean hasBeenRequested = false;
            for (DocumentSnapshot d : myRequestsResult.getDocuments()) {
              if (d.getId().equals(classID)) {
                hasBeenRequested = true;
                break;
              }
            }

            String className = (String) classInfo.get(mClassesCollectionFieldTitle);
            String classNum = String.format("%s-%s",
                classInfo.get(mClassesCollectionFieldCourseNumber),
                classInfo.get(mClassesCollectionFieldSection));
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

  private Task<QuerySnapshot> getUsersSubscribedClassesTask(String userID) {
    return mClassRosterRef
        .whereArrayContains(mClassRosterCollectionFieldMember, userID).get();
  }

  private Task<QuerySnapshot> getUsersSubscriptionRequests(String userID) {
    return mSubscriptionRequestsRef
        .whereArrayContains(mSubscriptionRequestsCollectionRequests, userID)
        .get();
  }

  private Task<QuerySnapshot> getAvailableClassesTask() {
    return mClassesRef
        .whereEqualTo(mClassesCollectionFieldYear, currentYear.toString())
        .whereEqualTo(mClassesCollectionFieldTerm, currentAcademicTerm)
        .get();
  }

  private String getCurrentTerm(Date currentDate, Integer currentYear) {
    String currentAcademicTerm;
    Calendar tCal = Calendar.getInstance();
    tCal.set(currentYear, 0, 27);
    Date springStart = tCal.getTime();
    tCal.set(currentYear, 4, 19);
    Date springEnd = tCal.getTime();
    tCal.set(currentYear, 4, 26);
    Date summerStart = tCal.getTime();
    tCal.set(currentYear, 7, 4);
    Date summerEnd = tCal.getTime();
    tCal.set(currentYear, 7, 31);
    Date fallStart = tCal.getTime();
    tCal.set(currentYear, 11, 21);
    Date fallEnd = tCal.getTime();
    tCal.set(currentYear, 0, 2);
    Date winterStart = tCal.getTime();
    tCal.set(currentYear, 0, 22);
    Date winterEnd = tCal.getTime();
    if (currentDate.compareTo(springStart) >= 0 && currentDate.compareTo(springEnd) <= 0) {
      currentAcademicTerm = "Spring";
    } else if (currentDate.compareTo(summerStart) >= 0 && currentDate.compareTo(summerEnd) <= 0) {
      currentAcademicTerm = "Summer";
    } else if (currentDate.compareTo(fallStart) >= 0 && currentDate.compareTo(fallEnd) <= 0) {
      currentAcademicTerm = "Fall";
    } else if (currentDate.compareTo(winterStart) >= 0 && currentDate.compareTo(winterEnd) <= 0) {
      currentAcademicTerm = "Winter";
    } else {
      currentAcademicTerm = "Unavailable";
    }
    return currentAcademicTerm;
  }

  void handleSubscriptionToggle(BrowseClassesActivity bcaObject, String classID) {
    Log.d(bcaObject.TAG, "handleSubscriptionToggle");
      bcaObject.sendSubscriptionRequest(classID);
  }

  private void sendSubscriptionRequest(final String classID) {
    Log.d(TAG, "sendSubscriptionRequest");
    final DocumentReference classRequestRef = mSubscriptionRequestsRef
        .document(classID);
    mFBDB.runTransaction(new Transaction.Function<Void>() {
      @Nullable
      @Override
      public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
        DocumentSnapshot cReqDoc = transaction.get(classRequestRef);
        if (cReqDoc.exists()) {
          transaction.update(classRequestRef, mSubscriptionRequestsCollectionRequests,
              FieldValue.arrayUnion(mCurrentUserID));
        } else {
          Map<String, Object> req = new HashMap<>();
          req.put(mSubscriptionRequestsCollectionRequests, FieldValue.arrayUnion(mCurrentUserID));
          classRequestRef.set(req);
        }
        return null;
      }
    }).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
          Log.d(TAG, "subscriptionRequested:success: " + classID);
          availableClassesInfoHash.remove(classID);
          availableClassesInfo.clear();
          availableClassesInfo.addAll(availableClassesInfoHash.values());
          mAdapter.notifyDataSetChanged();
        } else {
          Log.w(TAG, "subscriptionRequested:failure: " + classID, task.getException());
        }
      }
    });
  }
}


