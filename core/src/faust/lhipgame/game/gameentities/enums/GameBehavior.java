package faust.lhipgame.game.gameentities.enums;

public enum GameBehavior {
    WALK,
    ATTACK,
    HURT,
    KNEE,
    IDLE,
    DEAD,
    EVADE;

    public static GameBehavior getFromString(String name) {
        for (GameBehavior e : GameBehavior.values()) {
            if (e.name().equals(name)) {
                return e;
            }
        }
        return null;
    }
}
