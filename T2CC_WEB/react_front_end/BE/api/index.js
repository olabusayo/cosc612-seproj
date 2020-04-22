let router = require('express').Router();

router.use('/teacher', require('./service/teacherservice'));
router.use('/class', require('./service/classservice'));

module.exports = router;