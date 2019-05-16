function Message(id, roomID, postedBy, postedByID, date, type, payload) {
    this.id = id;
    this.roomID = roomID;
    this.postedBy = postedBy;
    this.postedByID = postedByID;
    this.date = date;
    this.type = type;
    this.payload = payload;

    this.getID = () => this.id;
    this.getRoomID = () => this.roomID;
    this.getPostedBy = () => this.postedBy;
    this.getPostedByID = () => this.postedByID;
    this.getDate = () => this.date;
    this.getType = () => this.type;
    this.getPayload = () => this.payload;
}

module.exports = Message;
