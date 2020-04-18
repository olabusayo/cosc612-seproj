let express = require('express');
let app = express();
let bodyParser = require('body-parser');
const api = require('./api/');
let db = require('./properties/db');
let urlencodedParser = bodyParser.urlencoded({ extended: true });

app.use(bodyParser.json())
.use(urlencodedParser)
.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
})
.use('/api', api)

.listen(process.env.PORT || 4000);