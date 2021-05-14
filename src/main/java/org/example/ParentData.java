package org.example;

public class ParentData {
    private String owner_cnp;
    private int absents, progress;

    public ParentData(String owner_cnp, int absents, int progress) {
        this.owner_cnp = owner_cnp;
        this.absents = absents;
        this.progress = progress;
    }

    public void setOwner_cnp(String owner_cnp) { this.owner_cnp = owner_cnp; }

    public void setAbsents(int absents) { this.absents = absents; }

    public void setProgress(int progress) { this.progress = progress; }

    public String getOwner_cnp() { return owner_cnp; }

    public int getAbsents() { return absents; }

    public int getProgress() { return progress; }

}
