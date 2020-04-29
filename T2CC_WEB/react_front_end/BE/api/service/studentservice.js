let router = require('express').Router();
const db = require('../../properties/db');

router.get('/:id', (req, res) => {

    const id = req.params.id;
    let studentRefs = db.collection('students');
    studentRefs.doc(id).get().then(doc => {
        if(doc.exists) {
            res.send(doc.data());
        }
    });
});

module.exports = router;