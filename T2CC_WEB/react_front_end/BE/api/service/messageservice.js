let router = require('express').Router();
const db = require('../../properties/db');

router.post('/create', (req, res) => {
    
    const { class_id, content, teacher_id } = req.body;
    const data = { class_id, content, teacher_id };
    let FieldValue = require('firebase-admin').firestore.FieldValue;
    data['sent_time'] = FieldValue.serverTimestamp();
    let messageRefs = db.collection('messages').add(data).then(ref => {
        res.send(ref);
    });
});

router.post('/save-draft', (req, res) => {
    
    const { class_id, content, teacher_id } = req.body;
    const data = { class_id, content, teacher_id };
    let FieldValue = require('firebase-admin').firestore.FieldValue;
    data['created_time'] = FieldValue.serverTimestamp();
    data['modified_time'] = FieldValue.serverTimestamp();
    let messageRefs = db.collection('drafts').add(data).then(ref => {
        res.send(ref);
    });
});

router.put('/edit', (req, res) => {
    
    const { id, content } = req.body;
    console.log(req.body);
    const data = { content };
    let FieldValue = require('firebase-admin').firestore.FieldValue;
    data['modified_time'] = FieldValue.serverTimestamp();
    let draftRefs = db.collection('drafts').doc(id);
    let result = draftRefs.update(data).then(ref => {
        res.send(ref);
    });
});

module.exports = router;