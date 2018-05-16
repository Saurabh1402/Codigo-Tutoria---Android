package com.massacre.codigotutoria.models;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.List;

/**
 * Created by saurabh on 13/7/17.
 */
public class LanguageHeader extends SugarRecord<LanguageHeader>{

    private long languageHeaderId;
    private String headerTitle;
    private long headerCount;
    private List<LanguageIndex> index;
    private Date lastModified;

    private long programmingLanguageId;

    public long getLanguageHeaderId() {
        return languageHeaderId;
    }

    public void setLanguageHeaderId(long languageHeaderId) {
        this.languageHeaderId = languageHeaderId;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public List<LanguageIndex> getIndex() {
        return index;
    }

    public void setIndex(List<LanguageIndex> data) {
        this.index = data;
    }

    public long getHeaderCount() {
        return headerCount;
    }

    public void setHeaderCount(long headerCount) {
        this.headerCount = headerCount;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public long getProgrammingLanguageId() {
        return programmingLanguageId;
    }

    public void setProgrammingLanguageId(long programmingLanguageId) {
        this.programmingLanguageId = programmingLanguageId;
    }


}
