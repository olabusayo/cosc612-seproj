const functions = require('firebase-functions');
const admin = require('firebase-admin');
const alerts = require('./handleAlertNotifications');

const t2cc = admin.initializeApp({
    credential: admin.credential.applicationDefault(),
    databaseURL: "https://cosc612-student-andriod.firebaseio.com"
});

var mAuth = t2cc.auth();
var mFBDB = t2cc.firestore();
var mFCM = t2cc.messaging();

/*******************FIRESTORE INFO*******************************/
/**********Classes**********/
const mClassesCollection = "classes";
const mClassesCollectionRef = mFBDB.collection(mClassesCollection);
const mClassesCollectionFieldCourseNumber = "course_number";
const mClassesCollectionFieldSection = "section";
const mClassesCollectionFieldTerm = "term";
const mClassesCollectionFieldTitle = "title";
const mClassesCollectionFieldYear = "year";
/**********ClassRoster**********/
const mClassRosterCollection = "class_roster";
const mClassRosterCollectionFieldStudents = "students";
const mClassRosterRef = mFBDB.collection(mClassRosterCollection);
/**********Student**********/
const mStudentsCollection = "students";
const mStudentCollectionEmail = "email";
const mStudentCollectionFieldFirstName = "fname";
const mStudentCollectionFieldLastName = "lname";
/**********SubscriptionRequests**********/
const mSubscriptionRequestsCollection = "subscription_requests";
const mSubscriptionRequestsCollectionFieldRequests = "requests";
/**********Messages**********/
const  mMessagesCollection = "messages";
const  mMessagesRef = mFBDB.collection(mMessagesCollection);
const  mMessagesCollectionFieldClassID = "class_id";
const  mMessagesCollectionFieldTeacherID = "teacher_id";
const  mMessagesCollectionFieldContent = "content";
const  mMessagesCollectionFieldSentTime = "sent_time";
/**********Notifications**********/
const mNotificationsCollection = "notifications";
const mNotificationsRef = mFBDB.collection(mNotificationsCollection);
const mNotificationsCollectionFieldToken = "notificationToken";
/*******************FIRESTORE INFO*******************************/

/* FIXME FOR TESTING ONLY
This automatically approves all requests. MUST be updated with approval/denial info
if used in production
 */
exports.handleSubscriptions = functions.firestore
    .document(mSubscriptionRequestsCollection + '/' + '{classID}')
    .onWrite((change, context) => {
        let classID = context.params.classID;
        let newReqsArray = change.after.get(mSubscriptionRequestsCollectionFieldRequests);
        let oldReqsArray = change.before.get(mSubscriptionRequestsCollectionFieldRequests);
        let deletedStudent = oldReqsArray === undefined ? [] : oldReqsArray.filter(x => newReqsArray.indexOf(x) === -1);
        let addedStudent = newReqsArray === undefined ? [] : newReqsArray.filter(x => oldReqsArray.indexOf(x) === -1);

        if (!deletedStudent.isEmpty && !(deletedStudent[0] === undefined)) {
            // student request was deleted so assume approval and add to roster
            let classRosterRef = mFBDB.collection(mClassRosterCollection).doc(classID);
            return classRosterRef.get()
                .then((classDocSnapshot) => {
                    return classDocSnapshot.exists
                }, err => console.error(`couldn't retrieve class roster: ${classID} : ${err}`))
                .then(documentExists => {
                    console.log(`teacher approved student ${deletedStudent} request...`);
                    console.log(`adding student to class: ${classID}...`);
                    return documentExists ? classRosterRef.update({
                        [mClassRosterCollectionFieldStudents]: admin.firestore.FieldValue.arrayUnion(
                            deletedStudent[0])
                    }) : classRosterRef.set({
                        [mClassRosterCollectionFieldStudents]: admin.firestore.FieldValue.arrayUnion(
                            deletedStudent[0])
                    });
                }, err => console.error(`couldn't add to roster: ${classID}: ${err}`));
        }

        if (!addedStudent.isEmpty && !(addedStudent[0] === undefined)) {
            // student request was added, "handle" by removing from request table
            let subRequestRef = change.after.ref;
            return subRequestRef.get()
                .then((reqDocSnapshot) => {
                    return reqDocSnapshot.exists
                }, err => console.error(`couldn't retrieve request: ${classID} : ${err}`))
                .then(documentExists => {
                    console.log(`student ${addedStudent} requested subscription...`);
                    console.log(`"handle" request for ${classID}...`);
                    return subRequestRef.update({
                        [mSubscriptionRequestsCollectionFieldRequests]: admin.firestore.FieldValue.arrayRemove(
                            addedStudent[0])
                    });
                }, err => console.error(`couldn't remove request: ${classID}: ${err}`));
        }

        return null;
    });

// enables/disables students from class notifications
exports.toggleAlertNotifications = functions.firestore
    .document(mClassRosterCollection + '/' + '{classID}')
    .onWrite((change, context) => {
        let classID = context.params.classID;
        let newStudentsArray = change.after.get(mClassRosterCollectionFieldStudents);
        let oldStudentsArray = change.before.get(mClassRosterCollectionFieldStudents);
        let deletedStudent = oldStudentsArray === undefined ? [] : oldStudentsArray.filter(x => newStudentsArray.indexOf(x) === -1);
        let addedStudent = newStudentsArray === undefined ? [] : newStudentsArray.filter(x => oldStudentsArray.indexOf(x) === -1);

        if (!deletedStudent.isEmpty && !(deletedStudent[0] === undefined)) {
            // student unsubscribed from class so unsubscribe from topic
            // look up registration token
            let studentID = deletedStudent[0];
            let notificationsRef = mNotificationsRef.doc(studentID);
            return notificationsRef.get()
                .then((studentDocSnapshot) => {
                    if (studentDocSnapshot.exists) {
                        // return studentDocSnapshot.get(mNotificationsCollectionFieldToken);
                        let registrationToken = studentDocSnapshot.get(mNotificationsCollectionFieldToken);
                        //console.log(`response from ${registrationToken}`);
                        if (!(registrationToken === null) && !(registrationToken === undefined)) {
                            console.log(`student ${deletedStudent} unsubscribed from class...`);
                            console.log(`unsubscribing from topic: ${classID}...`);
                            return mFCM.unsubscribeFromTopic(registrationToken, classID);
                        }
                    }
                    console.log(`student ${studentID} not registered or not subscribed`)
                    return null;
                })
                .catch((error) => {
                    console.log(`Error unsubscribing student: ${studentID}:`, error);
                });
        }

        if (!addedStudent.isEmpty && !(addedStudent[0] === undefined)) {
            // student subscribed to class, so subscribe to topic
            // look up registration token
            let studentID = addedStudent[0];
            let notificationsRef = mNotificationsRef.doc(studentID);
            return notificationsRef.get()
                .then((studentDocSnapshot) => {
                    //return studentDocSnapshot.exists
                    if (studentDocSnapshot.exists) {
                        // return studentDocSnapshot.get(mNotificationsCollectionFieldToken);
                        let registrationToken = studentDocSnapshot.get(mNotificationsCollectionFieldToken);
                        //console.log(`response from ${registrationToken}`);
                        if (!(registrationToken === null) && !(registrationToken === undefined)) {
                            console.log(`student ${addedStudent} subscribed to class...`);
                            console.log(`subscribing to topic: ${classID}...`);
                            return mFCM.subscribeToTopic(registrationToken, classID);
                        }
                    }
                    console.log(`student ${studentID} not registered`)
                    return null;
                })
                .catch((error) => {
                    console.log(`Error subscribing student: ${studentID}:`, error);
                });
        }

        return null;
    });

// send alert notification
// change to OnWrite for testing purposes
exports.sendAlertNotifications = functions.firestore
    .document(mMessagesCollection + '/' + '{messageID}')
    .onCreate((messageSnap, context) => {
        let newMessage = messageSnap.data();
        let notificationTopic = newMessage[mMessagesCollectionFieldClassID];
        let classInfoRef = mClassesCollectionRef.doc(notificationTopic);

        return classInfoRef.get().then(
            (classInfo) => {
                let classTitle = classInfo.get(mClassesCollectionFieldTitle);
                const notificationData = {
                    "notification": {
                        title: classTitle,
                        body: "New message from class"
                    }
                };
                return mFCM.sendToTopic(notificationTopic, notificationData)
                    .then((response) => {
                        // Response is a message ID string.
                        console.log('Sending message to:', notificationTopic);
                        console.log('Successfully sent message:', response);
                        return response;
                    })
                    .catch((error) => {
                        console.log('Error sending message to:', notificationTopic);
                        console.log('Error sending message:', error);
                    });
            });

    });

