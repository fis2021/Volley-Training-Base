package org.example;

public class PlayerData {
    private String owner_cnp, coach;
    private int progress, absents;

    public PlayerData(String owner_cnp, String coach, int progress, int absents) {
        this.owner_cnp = owner_cnp;
        this.coach = coach;
        this.progress = progress;
        this.absents = absents;
    }

    public void setOwner_cnp(String owner_cnp) { this.owner_cnp = owner_cnp; }

    public void setCoach(String coach) { this.coach = coach; }

    public void setProgress(int progress) { this.progress = progress; }

    public void setAbsents(int absents) { this.absents = absents; }

    public String getOwner_cnp() { return owner_cnp; }

    public String getCoach() { return coach; }

    public int getProgress() { return progress; }

    public int getAbsents() { return absents; }
}
