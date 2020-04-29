let express = require('express');
const cors = require('cors');
const path = require('path')
let bodyParser = require('body-parser');
let app = express();
const api = require('./api/');
let db = require('./properties/db');
let urlencodedParser = bodyParser.urlencoded({ extended: true });

app.use(bodyParser.json())
.use(cors())
.use(urlencodedParser)

// .use(function(req, res, next) {
//     res.header("Access-Control-Allow-Origin", "*");
//     res.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
//     res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
//     next();
// })
.use('/api', api);

if (process.env.NODE_ENV === "production") {
    app.use(express.static(path.join(__dirname, '../build')))
    .get('*', (req, res) => {
        res.sendFile(path.join(__dirname, '../build/index.html'))
    })
}



app.listen(process.env.PORT || 5000);