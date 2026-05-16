package com.example.gallery.dto;

public class FileResponse {

    private String name;
    private String url;
    private long size;

    public FileResponse(String name, String url, long size) {
        this.name = name;
        this.url = url;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public long getSize() {
        return size;
    }
}