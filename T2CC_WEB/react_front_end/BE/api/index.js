let router = require('express').Router();

router.use('/teacher', require('./service/teacherservice'));
router.use('/class', require('./service/classservice'));
router.use('/message', require('./service/messageservice'));
router.use('/subscription', require('./service/subscriptionservice'));
router.use('/student', require('./service/studentservice'));

module.exports = router;