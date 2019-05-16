const setupBoilerplate = require('./boilerplate/setup');

const {
    app,
    io,
    listen
} = setupBoilerplate();
const port = 443;
const Message = require('./models/message.model');
const Room = require('./models/room.model');
const mysql = require('mysql');

USE_DUMMY_MODEL = false;
var mysqlConn = null;
if (!USE_DUMMY_MODEL) {
    mysqlConn = mysql.createConnection({
        host: 'localhost',
        user: 'root',
        password: 'lule',
        database: 'luttu'
    });
}

// Bind REST controller to /api/*
const router = require('./controllers/rest.controller.js');
app.use('/api', router);

// Registers socket.io controller
const socketController = require('./controllers/socket.controller.js');

io.on('connection', socket => {
    console.log('user connected')
    socketController(socket, io, mysqlConn);
});



// Demo calls to model
const model = require('./model.js');
if (USE_DUMMY_MODEL) {
    const room1 = new Room(1, "#ducktales", 10);
    const room2 = new Room(2, "#memes", 10);
    const room3 = new Room(3, "#history", 10);
    const room4 = new Room(4, "#cooking", 10);
    model.addRoom(room1);
    model.addRoom(room2);
    model.addRoom(room3);
    model.addRoom(room4);
}
//const msg1 = new Message(1, 1, "yoyooy", "Johan", "1", "Mon 13:30");
//const msg2 = new Message(2, 1, "woowowowow", "Johan", "1", "Mon 13:31");
//model.addRoom(room1);
//model.getRoom(1).addMessage(msg1);
//model.getRoom(1).addMessage(msg2);
model.fetchUpdates(mysqlConn);
console.log(model);

listen(port, () => {
    console.log("server listening on port", port);
});
