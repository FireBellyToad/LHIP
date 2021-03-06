package faust.lhipgame.game.music.enums;

public enum TuneEnum {

    TITLE("title.ogg"),
    AMBIENCE("ambience.ogg"),
    GAMEOVER("gameover.ogg"),
    DANGER("danger.ogg"),
    ATTACK("attack.ogg");
//    CHURCH("ambience");

    private final String fileName;

    TuneEnum(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return "music/" +fileName;
    }
}
