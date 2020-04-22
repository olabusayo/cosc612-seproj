package com.example.t2cc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
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
  // firebase
  private FirebaseAuth mAuth;
 // private FirebaseFirestore mFBDB;
  private FirebaseUser mCurrentUser;
  private String mCurrentUserID;
  private CollectionReference mClassesRef;
  private CollectionReference mClassRosterRef;
  private CollectionReference mSubscriptionRequestsRef;

  // Calendar
  private Calendar mCal;
  private Date currentDate;
  private Integer currentYear;
  private String currentAcademicTerm;
  private TextView mAvailableClassesText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browseclass);

    //Calendar
    mCal = Calendar.getInstance();
    // firebase
    mAuth = FirebaseAuth.getInstance();
    mClassesRef = mFBDB.collection(mClassesCollection);
    mClassRosterRef = mFBDB.collection(mClassRosterCollection);
    mSubscriptionRequestsRef = mFBDB.collection(mSubscriptionRequestsCollection);
    mCurrentUser = mAuth.getCurrentUser();
    mCurrentUserID = mCurrentUser.getUid();
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
            } else {
              Log.w(TAG, "setUpAdapterOnCreate:failure");
            }
          }
        });

    // get recycle view
    mRecyclerView = findViewById(R.id.subscribe2ClassRecycleView);
  }

  void handleSubscriptionToggle(BrowseClassesActivity bcaObject, String classID,
      Boolean isSubscriptionRequest) {
    Log.d(bcaObject.TAG, "handleSubscriptionToggle");
    if (isSubscriptionRequest) {
      bcaObject.sendSubscriptionRequest(classID);
    } else {
      bcaObject.unsubscribeFromClass(classID);
    }
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
          List<ClassListInformation> tempList = new ArrayList<>();
          Log.d(TAG, "populateAvailableClassesData:success");
          List<Object> docs = task.getResult();
          QuerySnapshot allClassesResult = (QuerySnapshot) docs.get(0);
          QuerySnapshot myClassesResult = (QuerySnapshot) docs.get(1);
          QuerySnapshot myRequestsResult = (QuerySnapshot) docs.get(2);

          for (QueryDocumentSnapshot document : allClassesResult) {
            String classID = document.getId();
            //check if student subscribed
            Boolean subscriptionStatus = false;
            myClassesResult.getDocuments().contains(classID);
            for (DocumentSnapshot d : myClassesResult.getDocuments()) {
              if (d.getId().equals(classID)) {
                subscriptionStatus = true;
                break;
              }
            }
            //check is student requested
            Boolean hasBeenRequested = false;
            for (DocumentSnapshot d : myRequestsResult.getDocuments()) {
              if (d.getId().equals(classID)) {
                hasBeenRequested = true;
                break;
              }
            }
            String requestStatus = hasBeenRequested ? "pending" : "none";
            ClassListInformation cli = new ClassListInformation(BrowseClassesActivity.this,
                document,
                subscriptionStatus, requestStatus);
            if (availableClassesInfoHash.isEmpty() || !availableClassesInfoHash.containsKey(
                classID)) {
              availableClassesInfoHash.put(classID, cli);
            } else {
              availableClassesInfoHash.put(classID, cli);
            }
          }
          availableClassesInfo.clear();
          availableClassesInfo.addAll(availableClassesInfoHash.values());
          if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
          }
        } else {
          Log.w(TAG, "populateAvailableClassesViewData:failure");
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
          populateAvailableClassesViewData();
        } else {
          Log.w(TAG, "subscriptionRequested:failure: " + classID, task.getException());
        }
      }
    });
  }

  private void unsubscribeFromClass(final String classID) {
    Log.d(TAG, "unsubscribeFromClass");
    final DocumentReference classRosterRef = mClassRosterRef
        .document(classID);
    mFBDB.runTransaction(new Transaction.Function<Void>() {
      @Nullable
      @Override
      public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
        DocumentSnapshot rosterClassDoc = transaction.get(classRosterRef);
        if (rosterClassDoc.exists()) {
          transaction.update(classRosterRef, mClassRosterCollectionFieldMember,
              FieldValue.arrayRemove(mCurrentUserID));
        } else {
          Log.w(TAG, "Attempted to unsubscribe from class with no roster.");
        }
        return null;
      }
    }).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
          Log.d(TAG, "unsubscribeFromClass:success: " + classID);
          populateAvailableClassesViewData();
        } else {
          Log.w(TAG, "unsubscribeFromClass:failure: " + classID, task.getException());
        }
      }
    });
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
}

// define data
class ClassListInformation implements FirestoreConnections.ClassesCollectionAccessors {
  String className;
  String classNumber;
  Boolean subscription;
  String status;
  String classID;
  BrowseClassesActivity bcaObject;

  ClassListInformation(BrowseClassesActivity bcaObject, String classID, String classNumber,
      String className,
      Boolean subscription, String status) {
    this.classID = classID;
    this.className = className;
    this.classNumber = classNumber;
    this.subscription = subscription;
    this.status = status;
    this.bcaObject = bcaObject;
  }

  ClassListInformation(BrowseClassesActivity browseClassesActivity,
      QueryDocumentSnapshot classDocument, Boolean subscriptionStatus,
      String requestStatus) {
    String cNum, cName;
    Map<String, Object> classInfo = classDocument.getData();
    cNum = String.format("%s-%s", classInfo.get(mClassesCollectionFieldCourseNumber),
        classInfo.get(mClassesCollectionFieldSection));
    cName = (String) classInfo.get(mClassesCollectionFieldTitle);

    this.className = cName;
    this.classNumber = cNum;
    this.subscription = subscriptionStatus;
    this.status = requestStatus;
    this.classID = classDocument.getId();
    this.bcaObject = browseClassesActivity;
  }
}


