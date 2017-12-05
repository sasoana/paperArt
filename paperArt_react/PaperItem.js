export default class PaperItem {
    id: number;
    name: string;
    paperType: string;
    color: string;
    duration: number;

    constructor (id, name, paperType, color, duration) {
        this.id = id;
        this.name = name;
        this.paperType = paperType;
        this.color = color;
        this.duration = duration;
    }
}