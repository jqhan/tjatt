function Room(id, name, capacity) {
    this.name = name;
    this.id = id;
    this.capacity = capacity;
    this.messages = [];
    this.activeUsers = [];

    this.addMessage = msg => {
        this.messages.push(msg);
    }
    this.getActiveUsers = () => this.activeUsers;
    this.addActiveUser = userID => {
        this.activeUsers.push(userID);
    }
    this.removeActiveUser = userID => {
        this.activeUsers = this.activeUsers.filter( activeUserID => activeUserID != userID );    
    }
    this.getID = () => this.id;
    this.getMessages = () => this.messages;

    this.removeMessage = messageID => {
        this.messages = this.messages.filter(message => message.getID() != messageID);
    }
}

function remove() {
    //notify users that room has been destroyed
}

module.exports = Room;
