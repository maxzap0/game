package com.game.validate;

import com.game.entity.Player;

import java.util.Date;

public class PlayerValid {
    public static Boolean validCreatePlayer(Player player){
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
        if (name==null || name.length()>12 || name.equals("")) {
            return false;
        }
        if (title==null || title.length()>30 || title.equals("")) {
            return false;
        }
        if (exp<0 || exp > 10000000) {
            return false;
        }
        return birthDay >= 0 && birthDay <= nowDate;
    }
}
