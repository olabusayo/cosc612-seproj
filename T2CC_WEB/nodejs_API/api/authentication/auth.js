let router = require('express').Router();
const admin = require('../../properties/firebase-admin');

router.post('/registration', (req, res) => {
    admin.auth().createUserWithEmailAndPassword(email, password).catch(function(error) {
        // Handle Errors here.
        var errorCode = error.code;
        var errorMessage = error.message;
        // ...
    });
      
});

router.post('/login', (req, res) => {
    const { email, password } = req.body;
    //console.log(email, password);
    admin.auth().signInWithEmailAndPassword(email, password).catch((error) => {
        console.log(error.message);
        res.send(error.message);
    });
    res.send("heeh");
});


module.exports = router;