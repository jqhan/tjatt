/* jslint node: true */
"use strict";

const Room = require('./models/room.model');
const Message = require('./models/message.model');
const User = require('./models/user.model');
const Decal = require('./models/decal.model');

const roomList = [];
const users = [];
var activeUsers = [];
const decals = [];

exports.addRoom = room => roomList.push(room);

exports.getRooms = () => exports.roomList;

exports.removeRoom = id => {
    for (var i = 0; i < roomList.length; i++) {
        var room = roomList[i];
        if (room.id === id) {
            roomList.splice(i, 1);
            room.remove();
            break;
        }
    }
};

exports.getRoom = id => {
    var returnRoom = null;
    for (var i=0; i< exports.roomList.length; i++) {
        if (exports.roomList[i].getID() == id) {
            return exports.roomList[i];
        } 
    }
};

exports.getUsers = () => exports.users;

exports.getDecals = () => exports.decals;

exports.setUsers = userList => {
    exports.users = userList;
};

exports.setDecals = decalList => {
    exports.decals = decalList;
};

exports.getDecal = decalID => {
    for (var i = 0; i < exports.decals.length; i++) {
        if (exports.decals[i].getID() == decalID) {
            return exports.decals[i];
        }
    }
};

exports.getActiveUsers = (roomID) => {
    return exports.getRoom(roomID).getActiveUsers();
};

exports.addActiveUser = (roomID, userID) => {
    console.log("Trying to add active user " + userID + " in room " + roomID);
    exports.getRoom(roomID).addActiveUser(userID);
};

exports.removeMessage = messageID => {
    console.log("Removing message with id: " + messageID);
    exports.roomList.map(room => room.removeMessage(messageID));
};

exports.removeActiveUser = (roomID, userID) => {
    console.log("trying to remove active user: " + userID + "from room " + roomID);
    const room = exports.getRoom(roomID); 
    room.removeActiveUser(userID);
    activeUsers = activeUsers.filter( activeUserID => activeUserID != userID );    
    console.log("user: " + userID + " has been removed from active users");
};

exports.setRooms = rooms => {
    console.log("model.setRooms = " );
    console.log(rooms);
    exports.roomList = rooms;
};

exports.getMessages = roomID => {
    const room = exports.getRoom(roomID);
    return room.getMessages();
};

exports.getUser = identifier => {
    var tempUser = null;
    for (var i=0; i < exports.users.length; i++) {
        if (exports.users[i].getEmail() == identifier ||
            exports.users[i].getID() == identifier) {
            return exports.users[i];
        }
    }
};

/**
 * Removes the room object with the matching name.
 * @param {String} name - The name of the room.
 */




exports.fetchUpdates = mysqlConn => {

    mysqlConn.connect();
    mysqlConn.query("SELECT decal_id, format, img_binary, added_by FROM decals;", function(err, results, fields) {
        if (err) {
            throw err;
        } else {
            var decalList = [];
            for (var i = 0; i < results.length; i++) {
               // console.log(results[0]);
                const decalID = results[i].decal_id;
                const format = results[i].format;
                const imgBinary = results[i].img_binary;
                const addedBy = results[i].added_by;
                const decal = new Decal(decalID, format, imgBinary, addedBy);
                decalList.push(decal);
            }
            exports.setDecals(decalList);
        }
    });
    mysqlConn.query("SELECT user_id, user_name, user_email, pass_hash FROM user;", function(err, results, fields) {
        if (err) {
            throw err;
        } else {
            var userList = [];
            console.log("fetched users: ");
            console.log(results);
            for (var i = 0; i < results.length; i++) {
                const userID = results[i].user_id;
                const userName = results[i].user_name;
                const email = results[i].user_email;
                const passHash = results[i].pass_hash;
                const user = new User(userID, email, userName, passHash);
                userList.push(user);
                console.log("added user: " + userID + " " + userName + " " + email);
            }
            exports.setUsers(userList);
        }
    });
    mysqlConn.query("SELECT room_id, room_name, max_users FROM room;", function(err, results, fields) {
        if (err) {
            throw err;
        } else {
            var roomList = [];
            console.log("fetched rooms: ");
            console.log(results);
            for (var i = 0; i < results.length; i++) {
                const roomID = results[i].room_id;
                const roomName = results[i].room_name;
                const capacity = results[i].max_users;
                const room = new Room(roomID, roomName, capacity);
                roomList.push(room);
            }
            exports.setRooms(roomList);
        }
    });

    mysqlConn.query("SELECT message_id, room_id, user_id, time_sent, message_type, payload FROM message;", function(err, results, fields) {
        if (err) {
            throw err;
        } else {
//           console.log("fetched messages: ");
//            console.log(results);
            for (var i = 0; i < results.length; i++) {
                const messageID = results[i].message_id;
                const roomID = results[i].room_id;
                const postedByID = results[i].user_id;
                const timeSent = results[i].time_sent;
                const messageType = results[i].message_type;
                const payload = results[i].payload;
                const postedBy = exports.getUser(postedByID).getName();
                const message = new Message(messageID, roomID, postedBy,
                    postedByID, timeSent, messageType, payload);
                exports.getRoom(roomID).addMessage(message);
            }
        }
    });
}
