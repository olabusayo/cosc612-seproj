let express = require('express');
let app = express();
let bodyParser = require('body-parser');
const api = require('./api/');
let db = require('./properties/db');
let urlencodedParser = bodyParser.urlencoded({ extended: true });



let usersRef = db.collection('students');

app.use(bodyParser.json())
.use(urlencodedParser)
.use('/api', api)

// .get('/', (req, res) => {
//     let users = [];
//     usersRef.get()
//     .then(snapshot => {
//         snapshot.forEach(doc => {
//             users.push(doc.data());
//         });
//         res.send(users);
//     })
//     .catch(err => {
//         console.log('Error getting documents', err);
//     });
    
// })

.listen(process.env.PORT || 3000);