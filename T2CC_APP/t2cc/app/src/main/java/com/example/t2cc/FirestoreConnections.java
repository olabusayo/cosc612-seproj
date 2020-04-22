package com.example.t2cc;

interface FirestoreConnections {
  interface ClassesCollectionAccessors {
    String mClassesCollection = "classes";
    String mClassesCollectionFieldCourseNumber = "course_number";
    String mClassesCollectionFieldSection = "section";
    String mClassesCollectionFieldTerm = "term";
    String mClassesCollectionFieldTitle = "title";
    String mClassesCollectionFieldYear = "year";
  }

  interface ClassRosterCollectionAccessors {
    String mClassRosterCollection = "class_roster";
    String mClassRosterCollectionFieldMember = "students";
  }

  interface StudentCollectionAccessors {
    String mStudentsCollection = "students";
    String mStudentCollectionEmail = "email";
    String mStudentCollectionFieldFirstName = "fname";
    String mStudentCollectionFieldLastName = "lname";
  }

  interface SubscriptionRequestsCollectionAccessors {
    String mSubscriptionRequestsCollection = "subscription_requests";
    String mSubscriptionRequestsCollectionRequests = "requests";
  }
}
