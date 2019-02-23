package com.bartholome.voicetransport;

public class Search {
    private long id;
    private String start;
    private String finish;
    private String duration;

    @Override
    public String toString() {
        return "Search{" +
                ", start='" + start + '\'' +
                ", end='" + finish + '\'' +
                ", duration=" + duration +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String end) {
        this.finish = end;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
