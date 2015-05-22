package com.example.model;

import java.util.List;

/**
 * Created by JongHunLee on 2015-05-22.
 */
public class Beacon {

    private String uuid;
    private String titleText;
    private List<String> pictureURL;
    private String contentText;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return titleText;
    }

    public void setTitle(String titleText) {
        this.titleText = titleText;
    }

    public List<String> getPicture() {
        return pictureURL;
    }

    public void setPicture(List<String> pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getContent() {
        return contentText;
    }

    public void setContent(String contentText) {
        this.contentText = contentText;
    }

    public String[] getPictureArray() {
        String[] array = new String[pictureURL.size()];

        for (int i = 0; i < pictureURL.size(); i++) {
            array[i] = pictureURL.get(i);
        }

        return array;
    }
}
