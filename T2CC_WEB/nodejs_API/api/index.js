let router = require('express').Router();

router.use('/auth', require('./authentication/auth'));

module.exports = router;