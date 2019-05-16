function User(id, email, name, passHash) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.passHash = passHash;

    this.getEmail = () => this.email;
    this.getID = () => this.id;
    this.getName = () => this.name;
    this.getPassHash = () => this.passHash;
}

module.exports = User;
