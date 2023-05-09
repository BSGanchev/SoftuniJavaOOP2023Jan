package football.entities.player;

public class Men extends BasePlayer {
    public Men(String name, String nationality, int strength) {
        super(name, nationality, 85.5, strength);
    }

    @Override
    public void stimulation() {
        this.setStrength(this.getStrength() + 145);
    }
}
