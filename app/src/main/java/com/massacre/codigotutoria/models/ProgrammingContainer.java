package com.massacre.codigotutoria.models;

import com.google.gson.Gson;

import java.util.Date;

/**
 * Created by saurabh on 28/7/17.
 */

public class ProgrammingContainer {
    private long languageId;
    private Date lastModified;

    public long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(long languageId) {
        this.languageId = languageId;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this,ProgrammingContainer.class);

    }
}
