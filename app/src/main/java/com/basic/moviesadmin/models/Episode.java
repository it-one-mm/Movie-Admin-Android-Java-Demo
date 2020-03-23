package com.basic.moviesadmin.models;

public class Episode {

    public static final String COLLECTION_NAME = "episodes";

    private String id;
    private String name;
    private String videoLink;
    private String seriesId;
    private String seriesTitle;
    private int epiCount;

    public Episode() {
    }

    public Episode(String id, String name, String videoLink, String seriesId, String seriesTitle, int epiCount) {
        this.id = id;
        this.name = name;
        this.videoLink = videoLink;
        this.seriesId = seriesId;
        this.seriesTitle = seriesTitle;
        this.epiCount = epiCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getSeriesTitle() {
        return seriesTitle;
    }

    public void setSeriesTitle(String seriesTitle) {
        this.seriesTitle = seriesTitle;
    }

    public int getEpisodeCount() {
        return epiCount;
    }

    public void setEpisodeCount(int episodeCount) {
        this.epiCount = episodeCount;
    }
}