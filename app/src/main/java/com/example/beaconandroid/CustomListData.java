package com.example.beaconandroid;

public class CustomListData {

    private String m_title;
    private String m_content;
    private String[] imageUrl;

    public CustomListData(String title, String content, String[] image) {
        this.m_title = title;
        this.m_content = content;
        this.imageUrl = image;
    }

    public String getTitle() {
        return m_title;
    }

    public String getContent() {
        return m_content;
    }

    public String[] getImageUrl() {
        return imageUrl;
    }
}
