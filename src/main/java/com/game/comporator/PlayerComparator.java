package com.game.comporator;

import com.game.entity.Player;

import java.util.Comparator;

public class PlayerComparator implements Comparator<Player> {

    private final String order;

    public PlayerComparator(String order) {
        this.order = order;
    }

    private int compareDate(Player d1, Player d2) {
        long diff = d1.getBirthday().getTime()-d2.getBirthday().getTime();
        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public int compare(Player o1, Player o2) {

        switch (order) {
            case "name": return o1.getName().toLowerCase().charAt(0)-o2.getName().toLowerCase().charAt(0);
            case "experience": return o1.getExperience()-o2.getExperience();
            case "birthday": return compareDate(o1,o2);
            case "level": return o1.getLevel()-o2.getLevel();
            default: return (int) (o1.getId()- o2.getId());
        }
    }
}
