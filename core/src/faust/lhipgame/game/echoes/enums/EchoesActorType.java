package faust.lhipgame.game.echoes.enums;

public enum EchoesActorType {
    DISCIPULUS("echo.discipulus.json","sprites/discipulus_sheet.png"),
    VICTIM("echo.victim.json","sprites/victim_sheet.png"),
    WOMAN("echo.woman.json","sprites/woman_sheet.png"),
    DEAD_HAND("echo.hand.json","sprites/hand_sheet.png"),
    DEAD_DOUBLE_HAND("echo.hands.json","sprites/double_hand_sheet.png");

    private final String filename;
    private final String spriteFilename;

    EchoesActorType(String filename, String spriteFilename) {
        this.filename = filename;
        this.spriteFilename = spriteFilename;
    }

    public String getFilename() {
        return filename;
    }

    public static EchoesActorType getFromString(String name) {
        for (EchoesActorType e : EchoesActorType.values()) {
            if (e.name().equals(name)) {
                return e;
            }
        }
        return null;
    }

    public String getSpriteFilename() {
        return spriteFilename;
    }
}
