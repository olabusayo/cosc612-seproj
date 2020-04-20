let router = require('express').Router();

router.use('/teacher', require('./service/teacherservice'));

module.exports = router;