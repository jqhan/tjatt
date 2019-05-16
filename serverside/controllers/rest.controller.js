const model = require("../model.js");
const express = require('express');
const router = express.Router();
const Room = require('../models/room.model');
const Message = require('../models/message.model');
const Decal = require('../models/decal.model');
const Crypto = require('crypto-js')
//Crypto.SHA256(11)
//Crypto.SHA256(11).toString()
console.log("hash: " + Crypto.SHA256("asd").toString());

function clone(obj) {
    if (null == obj || "object" != typeof obj) return obj;
    var copy = obj.constructor();
    for (var attr in obj) {
        if (obj.hasOwnProperty(attr)) copy[attr] = obj[attr];
    }
    return copy;
}

router.get('/activeUsers/:roomID', function(req, res) {
    res.json({
        list: model.getActiveUsers(req.params.roomID)
    });
});

router.get('/roomList', function(req, res) {
    console.log("model roms:");
    console.log(model.getRooms());
    res.json({
        list: model.getRooms()
    });
});

router.get('/decals', function(req, res) {
    // ugly h4ck for deep copy
    var decalList = JSON.parse(JSON.stringify(model.getDecals()));
    decalList.forEach(function(e) {
        delete e.img_binary
    });
    res.json({
        list: decalList
    });
});

router.get('/decal/:decalID', function(req, res) {
    res.contentType('image/bmp');
    const decal = model.getDecal(req.params.decalID);
    const decalBinary = decal.getBinary();
    //console.log("trying to send decal binary: " + decalBinary);
    res.send(decalBinary);
});

router.get('/messages/:roomID', function(req, res) {
    res.json({
        list: model.getMessages(req.params.roomID)
    });
});

router.post('/login', function(req, res) {
    var email = req.body.email;
    var password = req.body.password;

    console.log("email: " + email);
    console.log("password: " + password);
    const user = model.getUser(email);
    var validationData = ["BAD", null, null];
    console.log("user hash:" + user.getPassHash() + " Hashed pass: " + Crypto.SHA256(password).toString());
    if (user != null) {
        if (user.getPassHash() == Crypto.SHA256(password).toString()) {
            validationData = ["OK", user.getID(), user.getName()];
        }
    }
    res.json(validationData);
});

module.exports = router;
