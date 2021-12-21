package com.game.validate;

import com.game.entity.Player;

import java.util.Date;

public class PlayerValidCreate {

    public static Boolean validCreatePlayer(Player player) {
        String name = player.getName();
        String title = player.getTitle();
        Integer exp = player.getExperience();
        long nowDate = new Date().getTime();
        long birthDay;
        try {
            birthDay = player.getBirthday().getTime();
        } catch (Exception e) {
            return false;
        }
        if (name == null || name.length() > 12 || name.equals("")) {
            return false;
        }
        if (title == null || title.length() > 30 || title.equals("")) {
            return false;
        }
        if (exp < 0 || exp > 10000000) {
            return false;
        }
        return birthDay > 0 && birthDay <= nowDate;
    }

    public static Boolean validUpdatePlayer(Player player) {
        String name = player.getName();
        String title = player.getTitle();
        Integer exp = player.getExperience();
        Date nowDate = new Date();
        Date birthDay = player.getBirthday();

        if (name != null && (name.length() > 12 || name.equals(""))) {
            return false;
        }

        if (title != null && (title.length() > 30 || title.equals(""))) {
            return false;
        }

        if (exp != null && (exp < 0 || exp > 10000000)) {
            return false;
        }

        return birthDay == null || (birthDay.after(new Date(0)) && nowDate.after(birthDay));
    }
}


