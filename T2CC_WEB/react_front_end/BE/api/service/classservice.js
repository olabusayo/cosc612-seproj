let router = require('express').Router();
const db = require('../../properties/db');

router.post('/add', (req, res) => {
    
    const { course_number, section, teacher_id, term, title, year} = req.body;
    const data = { course_number, section, teacher_id, term, title, year };
    let classRefs = db.collection('classes').add(data).then(ref => {
      createClassRoster(ref.id);
      res.send(ref);
    });

});

router.get('/getAll/:id', (req, res) => {
    const id = req.params.id;
    let classRefs = db.collection('classes');
    classRefs.where('teacher_id', '==' , id).get()
    .then(snapshot => {
      let data = [];  
      if (snapshot.empty) {
        res.send({});
      }  
  
      snapshot.forEach(doc => {
        const result = {
          id: doc.id,
          data: doc.data()
        }
        data.push(result);
        console.log(result);
      });

      res.send(data);
    })
    .catch(err => {
        res.send({ err : "Something went wrong!"});
    });
    
});

function createClassRoster(id) {
  let classRefs = db.collection('class_roster').doc(id);
  classRefs.set({
    students : []
  })

}

module.exports = router;