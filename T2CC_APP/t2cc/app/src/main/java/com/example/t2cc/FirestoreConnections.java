package com.example.t2cc;

interface FirestoreConnections {
  interface ClassesCollectionAccessors {
    final static String mClassesCollection = "classes";
    final static String mClassesCollectionFieldCourseNumber = "course_number";
    final static String mClassesCollectionFieldSection = "section";
    final static String mClassesCollectionFieldTerm = "term";
    final static String mClassesCollectionFieldTitle = "title";
    final static String mClassesCollectionFieldYear = "year";
  }

  interface ClassRosterCollectionAccessors {
    final static String mClassRosterCollection = "class_roster";
    final static String mClassRosterCollectionFieldMember = "students";
  }

  interface StudentCollectionAccessors {
    final static String mStudentsCollection = "students";
    final static String mStudentCollectionEmail = "email";
    final static String mStudentCollectionFieldFirstName = "fname";
    final static String mStudentCollectionFieldLastName = "lname";
  }

  interface SubscriptionRequestsCollectionAccessors {
    final static String mSubscriptionRequestsCollection = "subscription_requests";
    final static String mSubscriptionRequestsCollectionRequests= "requests";
  }
}
