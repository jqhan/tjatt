const model = require('../model.js');
const Message = require('../models/message.model');

module.exports = (socket, io, mysqlConn) => {

    socket.on('join', function(userID, userName, roomID) {
        console.log(userName +" joined the chat");
        if(isNaN(userID) || isNaN(roomID)) {
            return;
        }
        model.addActiveUser(roomID, userID);
        io.emit('userJoinedChat', userID, userName, roomID);
    });

    socket.on('deleteMessage', function(messageID) {
        if(isNaN(messageID)) {
            return;
        }
        model.removeMessage(messageID);
        mysqlConn.query("DELETE FROM message WHERE message_id='" + messageID + "';", function(err, results, fields) {

            if (err) {
                throw err;
            } else {
                console.log("Removed message with id: " + messageID + " from DB");
            }});
        io.emit('messageDeleted', messageID);
    });

    socket.on('userDisconnect', function(userID, userName, roomID) {
        console.log(userName + " has left");
        if(isNaN(userID) || isNaN(roomID)) {
            return;
        }
        model.removeActiveUser(roomID, userID);
        io.emit("userDisconnect", userID, userName, roomID) 
    });

    socket.on('newMessage', function(roomID, userID, msgType, payload) {
        const user = model.getUser(userID);

        mysqlConn.query("INSERT INTO message(room_id, user_id, time_sent, message_type, payload) VALUES (" + roomID + ", " + userID + ", DATE_FORMAT(NOW(),'%d %b %h:%i %p'), '" + msgType + "', '" + payload + "');", function(err, results, fields) {

            if (err) {
                throw err;
            } else {
                // selects most recently added message to database (with largest ID)
                mysqlConn.query("SELECT * FROM message ORDER BY message_id DESC LIMIT 0,1", function(err, results, fields) {
                    if (err) {
                        throw err;
                    } else {
                        const user = model.getUser(userID);
                        console.log("Message from db:");
                        console.log(results);
                        const res = results[0];
                        const message = new Message(res.message_id, res.room_id,
                                            user.getName(), res.user_id,
                                            res.time_sent, res.message_type,
                                            res.payload);
                        model.getRoom(roomID).addMessage(message);
                        io.emit('message', message);
                    }
                });
            }
        });
    });
}

