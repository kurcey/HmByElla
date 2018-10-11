package com.wanliss.kurt.sewingmate.DTO;

import android.support.v7.widget.RecyclerView;

import java.io.Serializable;

public class StoreDisplayDTO implements Serializable {

    private String directory;
    private String name;
    private String path;
    private String thumbnail;


    public StoreDisplayDTO () {

    }

    public  StoreDisplayDTO (String directory, String name, String path, String thumbnail) {
        this.directory = directory;
        this.name = name;
        this.path = path;
        this.thumbnail = thumbnail;
    }

    public String getDirectory() {
        return directory;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
