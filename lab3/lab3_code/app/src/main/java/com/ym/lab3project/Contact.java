package com.ym.lab3project;

import java.io.Serializable;

/**
 * Created by ym on 16-10-14.
 */

public class Contact implements Serializable {
    String name;
    String telNum;
    String type;
    String location;
    String bgColor;

    Contact(String name, String telNum, String type, String location, String bgColor) {
        this.name = name;
        this.telNum = telNum;
        this.type = type;
        this.location = location;
        this.bgColor = bgColor;
    }
}
