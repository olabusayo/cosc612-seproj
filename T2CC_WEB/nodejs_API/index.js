let express = require('express');

let db = require('./properties/db');

let app = express();

let usersRef = db.collection('students');


app.get('/', (req, res) => {
    let users = [];
    usersRef.get()
    .then(snapshot => {
        snapshot.forEach(doc => {
            users.push(doc.data());
        });
        res.send(users);
    })
    .catch(err => {
        console.log('Error getting documents', err);
    });
    
})

.listen(process.env.PORT || 3000);