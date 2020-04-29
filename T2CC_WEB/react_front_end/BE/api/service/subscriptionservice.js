let router = require('express').Router();
const db = require('../../properties/db');
var admin = require('firebase-admin');

router.get('/getAll/:teacherId', (req, res) => {
    
    const teacher_id = req.params.teacherId;
    let classRefs = db.collection('classes');
    let studentRefs = db.collection('students');
    let subscriptionRefs = db.collection('subscription_requests');
    classRefs.where('teacher_id', '==', teacher_id).get().then(snapshot => {
        let classes = [];
        if (!snapshot.empty) {
            snapshot.forEach(doc => {
                const data = {
                    id : doc.id,
                    data: doc.data()
                }
                classes.push(data);
            });
        }  

        if(classes.length == 0) {
            res.send({});
        } else {
            subscriptionRefs.get().then(snapshot => {
                if (snapshot.empty) {
                    res.send({});
                } else {
                    
                    getAllSubs(classes, studentRefs, snapshot).then(data => {
                        
                        res.send(data);
                    })
  
                }
            });
        }
        
    });
});

router.post('/approve', (req, res) => {
    const { class_id, student_email } = req.body;
    deleteRequest(class_id, student_email);
    addStudentToClass(class_id, student_email);
    res.send({});
});

router.post('/deny', (req, res) => {
    const { class_id, student_email } = req.body;
    deleteRequest(class_id, student_email);
    res.send({});
});

function addStudentToClass(class_id, student_email) {
    let classRosterRefs = db.collection('class_roster').doc(class_id);
    db.collection('students').where('email', '==', student_email).get()
    .then(snapshot => {
        snapshot.forEach(doc => {
            classRosterRefs.update({
                students : admin.firestore.FieldValue.arrayUnion(doc.id)
            })
        });
    });
    
}

function deleteRequest(class_id, student_email) {
    let subscriptionRefs = db.collection('subscription_requests').doc(class_id);
    db.collection('students').where('email', '==', student_email).get()
    .then(snapshot => {
        snapshot.forEach(doc => {
            subscriptionRefs.update({
                requests : admin.firestore.FieldValue.arrayRemove(doc.id)
            })
        });
    });
}

async function getAllSubs(classes, studentRefs, snapshot) {

    const promises = snapshot.docs.map((doc, i) => {
        let res = getSubs(classes, doc, studentRefs)
        return res;  
    });

    let res = promises.filter(promise => promise !== null);

    return await Promise.all(res);    
}

async function getStudent(studentRefs, students) {

    const promises = students.map((student, i) => {
        return studentRefs.doc(student).get();
    }); 

    return await Promise.all(promises);    
} 

function getSubs(classes, doc, studentRefs) {
    for(var item of classes) {
        if(item.id === doc.id) {
            let students = doc.data().requests;  
            if(students.length > 0) {
                return getStudent(studentRefs, students).then(doc => {
                    let studentList = [];

                    for(var stud of doc) {
                        if(stud.exists) {
                            studentList.push(stud.data());
                        }
                    }

                    const result = {
                        class : item,
                        students : studentList
                    }
                    console.log(result);
                    return result;
                });   
            }
        } 
    }
    return null;
}

module.exports = router;