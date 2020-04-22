let router = require('express').Router();
const db = require('../../properties/db');

router.post('/add', (req, res) => {
    
    const { course_number, section, teacher_email, term, title, year} = req.body;
    const data = { course_number, section, teacher_email, term, title, year };
    let classRefs = db.collection('classes').doc(course_number + " " + section).set(data).then(ref => {
        res.send(ref);
    });
});

router.get('/getAll/:email', (req, res) => {
    const email = req.params.email;
    let classRefs = db.collection('classes');
    classRefs.where('teacher_email', '==' , email).get()
    .then(snapshot => {
      let data = [];  
      if (snapshot.empty) {
        res.send({});
      }  
  
      snapshot.forEach(doc => {
        data.push(doc.data());
      });

      res.send(data);
    })
    .catch(err => {
        res.send({ err : "Something went wrong!"});
    });
    
});

module.exports = router;