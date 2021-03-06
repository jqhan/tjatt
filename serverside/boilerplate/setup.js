require('better-logging')(console);
const path = require('path');
const expressSession = require('express-session');
const sharedSession = require('express-socket.io-session');
const express = require('express');
const https = require('https');


/** 
 * This function sets up some boilerplate for express and socket.io
 * - Creates express app
 * - Creates socket.io app
 * - Logs all incoming requests
 * - Serves static files from ../public/* at /
 * - Parses request-body & request-url 
 * - Adds a cookie based session storage to both express & socket.io
 * 
 * @returns ctx: { app: ExpressApp, io: SocketIOApp, listen: (port, callback) => void }
*/
module.exports = () => {
    const app = express(); // Creates express app

    const fs = require('fs');
    const key = fs.readFileSync('encryption/private.key');
    const cert = fs.readFileSync( 'encryption/mydomain.crt' );
    const ca = fs.readFileSync( 'encryption/mydomain.crt' );
    const bodyParser = require('body-parser');
    app.use(bodyParser.json()); // support json encoded bodies
    app.use(bodyParser.urlencoded({ extended: true })); // support encoded bodies
    const options = {
        key: key,
        cert: cert,
        ca: ca
        };
    const port = 443;

    const httpsServer = https.createServer(options, app); // Express usually does this for us, but socket.io needs the httpServer directly
    const io = require('socket.io').listen(httpsServer); // Creates socket.io app
    // Setup express
    app.use((req, res, next) => {
        // Logs each incoming request
        console.info(`${console.color.Dark_Gray} ${req.ip} ${console.color.RESET} ${req.path} ${req.body || ''}`);
        next();
    });
    app.use(express.json() /*
        This is a middleware, provided by express, that parses the body of the request into a javascript object.
        It's basically just replacing the body property like this:
        req.body = JSON.parse(req.body)
    */);
    app.use(express.urlencoded({
        extended: true
    }));

    // Setup session
    const session = expressSession({
        secret: `
        If you chose to use this boilerplate for your own project then you should really: 
        1. Change this string to something obscure, like the MD5 hash of your favorite poem.
        2. Put that string in its own separate file.
        3. Add that file to the .gitignore so that you dont accidentally commit it to git.
        `,
        resave: true,
        saveUninitialized: true,
    });
    app.use(session);
    io.use(sharedSession(session));

    return {
        app, io,
        listen: (port, cb) => httpsServer.listen(port, cb)
    }
}
