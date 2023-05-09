package football.core;


import football.common.ConstantMessages;
import football.common.ExceptionMessages;
import football.entities.field.ArtificialTurf;
import football.entities.field.Field;
import football.entities.field.NaturalGrass;
import football.entities.player.Men;
import football.entities.player.Player;
import football.entities.player.Women;
import football.entities.supplement.Liquid;
import football.entities.supplement.Powdered;
import football.entities.supplement.Supplement;
import football.repositories.SupplementRepository;
import football.repositories.SupplementRepositoryImpl;

import java.util.ArrayList;
import java.util.Collection;

public class ControllerImpl implements Controller {
    private SupplementRepository supplementRepository;
    private Collection<Field> fields;

    public ControllerImpl() {
        this.supplementRepository = new SupplementRepositoryImpl();
        this.fields = new ArrayList<>();
    }

    @Override
    public String addField(String fieldType, String fieldName) {
        Field field;
        switch (fieldType) {
            case "ArtificialTurf":
                field = new ArtificialTurf(fieldName);
                break;
            case "NaturalGrass":
                field = new NaturalGrass(fieldName);
                break;
            default:
                throw new NullPointerException(ExceptionMessages.INVALID_FIELD_TYPE);
        }
        this.fields.add(field);
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_FIELD_TYPE, fieldType);
    }

    @Override
    public String deliverySupplement(String type) {
        Supplement supplement;
        switch (type) {
            case "Powdered":
                supplement = new Powdered();
                break;
            case "Liquid":
                supplement = new Liquid();
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.INVALID_SUPPLEMENT_TYPE);
        }
        this.supplementRepository.add(supplement);
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_SUPPLEMENT_TYPE, type);
    }

    @Override
    public String supplementForField(String fieldName, String supplementType) {
        Supplement supplement = this.supplementRepository.findByType(supplementType);
        if (supplement == null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.NO_SUPPLEMENT_FOUND, supplementType));
        } else {
            Field field = getFieldByName(fieldName);
            field.addSupplement(supplement);
        }
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_SUPPLEMENT_IN_FIELD, supplementType, fieldName);
    }

    @Override
    public String addPlayer(String fieldName, String playerType, String playerName, String nationality, int strength) {

        Player player;
        Field field = getFieldByName(fieldName);
        switch (playerType) {
            case "Women":
                player = new Women(playerName, nationality, strength);
                if (field.getClass().getSimpleName().equals("ArtificialTurf")) {
                    field.addPlayer(player);
                }
                break;
            case "Men":
                player = new Men(playerName, nationality, strength);
                if (field.getClass().getSimpleName().equals("NaturalGrass")) {
                    field.addPlayer(player);
                }
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.INVALID_PLAYER_TYPE);
        }
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_PLAYER_IN_FIELD, playerType, fieldName);
    }

    @Override
    public String dragPlayer(String fieldName) {
        Field field = getFieldByName(fieldName);
        field.drag();
        return String.format(ConstantMessages.PLAYER_DRAG, field.getPlayers().size());
    }

    @Override
    public String calculateStrength(String fieldName) {
        Field field = getFieldByName(fieldName);
        Collection<Player> players = field.getPlayers();
        int sum = players.stream().map(Player::getStrength).mapToInt(Integer::valueOf).sum();
        return String.format(ConstantMessages.STRENGTH_FIELD, fieldName, sum);
    }

    @Override
    public String getStatistics() {
        Collection<Field> fields = this.fields;
        StringBuilder statistics = new StringBuilder();
        for (Field field : fields) {
            statistics.append(field.getInfo());
        }
        return statistics.toString().trim();
    }

    private Field getFieldByName(String fieldName) {
        Field field = this.fields.stream().filter(f -> f.getName().equals(fieldName)).findFirst().get();
        return field;
    }
}
