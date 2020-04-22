const functions = require('firebase-functions');
const admin = require('firebase-admin');
var t2cc = admin.initializeApp({
    credential: admin.credential.applicationDefault(),
    databaseURL: "https://cosc612-student-andriod.firebaseio.com"
});
var mAuth = t2cc.auth();
var mFBDB = t2cc.firestore();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

/*******************FIRESTORE INFO*******************************/
/**********Classes**********/
const mClassesCollection = "classes";
const mClassesCollectionFieldCourseNumber = "course_number";
const mClassesCollectionFieldSection = "section";
const mClassesCollectionFieldTerm = "term";
const mClassesCollectionFieldTitle = "title";
const mClassesCollectionFieldYear = "year";
/**********ClassRoster**********/
const mClassRosterCollection = "class_roster";
const mClassRosterCollectionFieldMember = "students";
/**********Student**********/
const mStudentsCollection = "students";
const mStudentCollectionEmail = "email";
const mStudentCollectionFieldFirstName = "fname";
const mStudentCollectionFieldLastName = "lname";
/**********SubscriptionRequests**********/
const mSubscriptionRequestsCollection = "subscription_requests";
const mSubscriptionRequestsCollectionRequests = "requests";
/*******************FIRESTORE INFO*******************************/

// FIXME TESTING ONLY
/* This automatically approves all requests. MUST be updated with approval/denial info so
it can
 */

exports.handleSubscriptions = functions.firestore
    .document(mSubscriptionRequestsCollection + '/' + '{classID}')
    .onUpdate((change, context) => {
        let classID = context.params.classID;
        let newReqsArray = change.after.get(mSubscriptionRequestsCollectionRequests);
        let oldReqsArray = change.before.get(mSubscriptionRequestsCollectionRequests);
        let deletedStudent = oldReqsArray.filter(x => newReqsArray.indexOf(x) === -1);
        let addedStudent = newReqsArray.filter(x => oldReqsArray.indexOf(x) === -1);

        if (!deletedStudent.isEmpty && !(deletedStudent[0] === undefined)) {
            // student request was deleted so assume approval and add to roster
            console.log(`teacher approved request: ${deletedStudent}`);
            console.log(`adding student to class: ${classID}`);
            let classRosterRef = mFBDB.collection(mClassRosterCollection).doc(classID);
            return classRosterRef.get()
                .then((classDocSnapshot) => {
                    return classDocSnapshot.exists
                }, err => console.error(`couldn't retrieve class roster: ${classID} : ${err}`))
                .then(documentExists => {
                    return documentExists ? classRosterRef.update({
                        [mClassRosterCollectionFieldMember]: admin.firestore.FieldValue.arrayUnion(
                            deletedStudent[0])
                    }) : classRosterRef.set({
                        [mClassRosterCollectionFieldMember]: admin.firestore.FieldValue.arrayUnion(
                            deletedStudent[0])
                    });
                }, err => console.error(`couldn't add to roster: ${classID}: ${err}`));
        }

        if (!addedStudent.isEmpty && !(addedStudent === undefined)) {
            // student request was added, "handle" by removing from request table
            console.log(`student requested subscription: ${addedStudent}`);
            console.log(`"handle" request`);
            sleep(between(3000, 10000));
            let subRequestRef = change.after.ref;
            return subRequestRef.get()
                .then((reqDocSnapshot) => {
                    return reqDocSnapshot.exists
                }, err => console.error(`couldn't retrieve request: ${classID} : ${err}`))
                .then(documentExists => {
                    return subRequestRef.update({
                        [mSubscriptionRequestsCollectionRequests]: admin.firestore.FieldValue.arrayRemove(
                            addedStudent[0])
                    });
                }, err => console.error(`couldn't remove request: ${classID}: ${err}`));
        }

        return null;
    });

// sleep function
function sleep(ms) {
    return new Promise((resolve) => {
        setTimeout(resolve, ms);
    });
}

//random number gen
function between(min, max) {
    return Math.floor(
        Math.random() * (max - min) + min
    )
}