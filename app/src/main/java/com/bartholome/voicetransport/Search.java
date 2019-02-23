package com.bartholome.voicetransport;

import java.time.Duration;

public class Search {
    private long id;
    private String start;
    private String end;
    private Duration duration;

    @Override
    public String toString() {
        return "Search{" +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
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

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
