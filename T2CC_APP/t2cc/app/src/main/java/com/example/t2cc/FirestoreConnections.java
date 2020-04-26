package com.example.t2cc;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

public class FirestoreConnections {
  public static final FirebaseFirestore mFBDB = FirebaseFirestore.getInstance();

  public abstract static class ClassesCollectionAccessors {
    public static final String mClassesCollection = "classes";
    public static final CollectionReference mClassesRef = mFBDB.collection(mClassesCollection);

    public static final String mClassesCollectionFieldCourseNumber = "course_number";
    public static final String mClassesCollectionFieldSection = "section";
    public static final String mClassesCollectionFieldTerm = "term";
    public static final String mClassesCollectionFieldTitle = "title";
    public static final String mClassesCollectionFieldYear = "year";


    public static Task<QuerySnapshot> getSpecificClassesInfoTask(List<String> classIDs) {
      return mClassesRef
          .whereIn(FieldPath.documentId(), classIDs)
          .get();
    }

    public static Task<QuerySnapshot> getAvailableClassesTask(String currentYear,
        String currentAcademicTerm) {
      return mClassesRef
          .whereEqualTo(ClassesCollectionAccessors.mClassesCollectionFieldYear,
              currentYear)
          .whereEqualTo(ClassesCollectionAccessors.mClassesCollectionFieldTerm, currentAcademicTerm)
          .get();
    }
  }

  public static class ClassRosterCollectionAccessors {
    public static final String mClassRosterCollection = "class_roster";
    public static final CollectionReference mClassRosterRef = mFBDB
        .collection(mClassRosterCollection);

    public static final String mClassRosterCollectionFieldStudents = "students";

    public static Task<QuerySnapshot> getUsersSubscribedClassesTask(String userID) {
      return mClassRosterRef
          .whereArrayContains(mClassRosterCollectionFieldStudents, userID).get();
    }
  }

  public abstract static class StudentCollectionAccessors {
    public static final String mStudentsCollection = "students";
    public static final CollectionReference mStudentsRef = mFBDB.collection(mStudentsCollection);

    public static final String mStudentCollectionFieldEmail = "email";
    public static final String mStudentCollectionFieldFirstName = "fname";
    public static final String mStudentCollectionFieldLastName = "lname";
  }

  public abstract static class SubscriptionRequestsCollectionAccessors {
    public static final String mSubscriptionRequestsCollection = "subscription_requests";
    public static final CollectionReference mSubscriptionRequestsRef =
        mFBDB.collection(mSubscriptionRequestsCollection);

    public static final String mSubscriptionRequestsCollectionFieldRequests = "requests";

    public static Task<QuerySnapshot> getUsersSubscriptionRequests(String userID) {
      return mSubscriptionRequestsRef
          .whereArrayContains(
              SubscriptionRequestsCollectionAccessors.mSubscriptionRequestsCollectionFieldRequests,
              userID)
          .get();
    }
  }

  public abstract static class MessagesCollectionAccessors {
    public static final String mMessagesCollection = "messages";
    public static final CollectionReference mMessagesRef = mFBDB.collection(mMessagesCollection);

    public static final String mMessagesCollectionFieldClassID = "class_id";
    public static final String mMessagesCollectionFieldTeacherID = "teacher_id";
    public static final String mMessagesCollectionFieldContent = "content";
    public static final String mMessagesCollectionFieldSentTime = "sent_time";

    public static Task<QuerySnapshot> getUsersMessagesTask(List<String> classIDs, Date cutOffDate) {
      Timestamp tsCutOffDate = new Timestamp(cutOffDate);
      Query.Direction messageSortDirection = Query.Direction.DESCENDING;
      return mMessagesRef
          .whereGreaterThanOrEqualTo(mMessagesCollectionFieldSentTime, tsCutOffDate)
          .whereIn(mMessagesCollectionFieldClassID, classIDs)
          .orderBy(mMessagesCollectionFieldSentTime, messageSortDirection)
          .get();
    }

    public static Task<QuerySnapshot> getMessagesForSpecificClass(String classID,
        Date cutOffDate) {
      Timestamp tsCutOffDate = new Timestamp(cutOffDate);
      Query.Direction messageSortDirection = Query.Direction.DESCENDING;
      return mMessagesRef
          .whereGreaterThanOrEqualTo(mMessagesCollectionFieldSentTime, tsCutOffDate)
          .whereEqualTo(mMessagesCollectionFieldClassID, classID)
          .orderBy(mMessagesCollectionFieldSentTime, messageSortDirection)
          .get();
    }
  }

  public abstract static class NotificationsCollectionAccessors {
    public static final String mNotificationsCollection = "notifications";
    public static final CollectionReference mNotificationsRef =
        mFBDB.collection(mNotificationsCollection);

    public static final String mNotificationsCollectionFieldToken = "notificationToken";


  }
}
