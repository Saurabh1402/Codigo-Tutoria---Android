package com.massacre.codigotutoria.models;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by saurabh on 16/7/17.
 */
public class LanguageIndex extends SugarRecord<LanguageIndex>{

    private long languageIndexId;
    private String indexTitle;
    private long indexCount;
    private Date lastModified;

    private long languageHeaderId; //foreign Key

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public long getLanguageIndexId() {
        return languageIndexId;
    }

    public void setLanguageIndexId(long languageIndexId) {
        this.languageIndexId = languageIndexId;
    }

    public String getIndexTitle() {
        return indexTitle;
    }

    public void setIndexTitle(String indexTitle) {
        this.indexTitle = indexTitle;
    }

    public long getIndexCount() {
        return indexCount;
    }

    public void setIndexCount(long indexCount) {
        this.indexCount = indexCount;
    }

    public long getLanguageHeaderId() {
        return languageHeaderId;
    }

    public void setLanguageHeaderId(long languageHeaderId) {
        this.languageHeaderId = languageHeaderId;
    }

}
