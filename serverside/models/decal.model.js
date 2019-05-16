function Decal(id, format, img_binary, added_by) {
    this.id = id;
    this.format = format;
    this.img_binary = img_binary;
    this.added_by = added_by;

    this.getID = () => this.id;
    this.getFormat = () => this.format;
    this.getBinary = () => this.img_binary;
    this.getAddedBy = () => this.added_by;
}

module.exports = Decal;
