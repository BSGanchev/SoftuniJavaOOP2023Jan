package football.entities.field;

import football.common.ConstantMessages;
import football.common.ExceptionMessages;
import football.entities.player.Player;
import football.entities.supplement.Supplement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public abstract class BaseField implements Field {
    private String name;
    private int capacity;
    private Collection<Supplement> supplements;
    private Collection<Player> players;

    public BaseField(String name, int capacity) {
        this.setName(name);
        this.capacity = capacity;
        this.supplements = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    @Override
    public int sumEnergy() {
        return this.supplements.stream().map(Supplement::getEnergy).mapToInt(Integer::valueOf).sum();
    }

    @Override
    public void addPlayer(Player player) {
        if (players.size() + 1 > this.capacity) {
            throw new IllegalStateException(ConstantMessages.NOT_ENOUGH_CAPACITY);
        }
        this.players.add(player);
    }

    @Override
    public void removePlayer(Player player) {
        this.players.remove(player);

    }

    @Override
    public void addSupplement(Supplement supplement) {
        this.supplements.add(supplement);
    }

    @Override
    public void drag() {
        this.players.stream().forEach(Player::stimulation);

    }

    @Override
    public String getInfo() {
        StringBuilder info = new StringBuilder();
        Collection<Player> playerCollection = this.players;
        info.append(
                String.format("%s (%s):%n", this.name, this.getClass().getSimpleName()));
        if (players.size() == 0) {
            info.append("Player: none");
        } else {
            info.append(String.format("Player:"));

            for (Player player : playerCollection) {
                info.append(" ").append(player.getName());
            }
            info.append(System.lineSeparator());
        }
        info.append("Supplement: ");
        info.append(this.supplements.size()).append(System.lineSeparator());
        info.append("Energy: ");
        info.append(sumEnergy()).append(System.lineSeparator());
        return info.toString().trim();
    }

    @Override
    public Collection<Player> getPlayers() {
        return Collections.unmodifiableCollection(this.players);
    }

    @Override
    public Collection<Supplement> getSupplements() {
        return Collections.unmodifiableCollection(this.supplements);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new NullPointerException(ExceptionMessages.FIELD_NAME_NULL_OR_EMPTY);
        }
        this.name = name;
    }
}
