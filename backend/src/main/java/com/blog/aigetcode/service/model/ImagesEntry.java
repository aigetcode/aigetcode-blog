package com.blog.aigetcode.service.model;

import java.util.List;

public class ImagesEntry {
    private String text;
    private List<String> images;

    public ImagesEntry(String text, List<String> images) {
        this.text = text;
        this.images = images;
    }

    public String getText() {
        return text;
    }

    public List<String> getImages() {
        return images;
    }
}
