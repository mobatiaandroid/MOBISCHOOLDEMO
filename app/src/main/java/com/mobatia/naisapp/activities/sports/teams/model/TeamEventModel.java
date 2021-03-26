package com.mobatia.naisapp.activities.sports.teams.model;

import java.io.Serializable;

public class TeamEventModel implements Serializable {
    String id;
    String teamName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
