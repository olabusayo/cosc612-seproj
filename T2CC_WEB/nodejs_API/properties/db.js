const admin = require('firebase-admin');

let serviceAccount = require('./cosc612-student-andriod-firebase-adminsdk.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

let db = admin.firestore();

module.exports = db;