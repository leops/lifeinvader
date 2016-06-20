package com.lifeinvader.tracker.models;

import java.util.List;

public class Group {
    public String key;
    public String name;
    public List<String> users;

    public Group() {
        // NOOP
    }

    public Group(String name, List<String> users) {
        this.name = name;
        this.users = users;
    }
}
