package com.mayur.mayurmagiclens;

public class RecycleSearchModel {
    private String rTitle, link, shown_link, rSnippet;

    public String getrTitle() {
        return rTitle;
    }

    public void setrTitle(String rTitle) {
        this.rTitle = rTitle;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getShown_link() {
        return shown_link;
    }

    public void setShown_link(String shown_link) {
        this.shown_link = shown_link;
    }

    public String getrSnippet() {
        return rSnippet;
    }

    public void setrSnippet(String rSnippet) {
        this.rSnippet = rSnippet;
    }

    public RecycleSearchModel(String rTitle, String link, String shown_link, String rSnippet) {
        this.rTitle = rTitle;
        this.link = link;
        this.shown_link = shown_link;
        this.rSnippet = rSnippet;
    }
}
