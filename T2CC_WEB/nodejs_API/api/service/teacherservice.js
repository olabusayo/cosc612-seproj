let router = require('express').Router();
const admin = require('../../properties/firebase-admin');
const db = require('../../properties/db');

router.post('/add', (req, res) => {
    const { fname, lname, email, password} = req.body;
    const teacher = {
        fname : fname,
        lname : lname,
        email: email
    }
    console.log(teacher);
    let teacherRefs = db.collection('teachers').add(teacher);
    res.send(JSON.stringify(teacher));
});

router.get('/authenticate/:email', (req, res) => {
    const email = req.params.email;
    let teacherRefs = db.collection('teachers');
    teacherRefs.where('email', '==' , email).get()
    .then(snapshot => {
      if (snapshot.empty) {
        res.send({});
      }  
  
      snapshot.forEach(doc => {
        res.send(doc.data());
      });
    })
    .catch(err => {
        res.send({ err : "Something went wrong!"});
    });
    
});


module.exports = router;