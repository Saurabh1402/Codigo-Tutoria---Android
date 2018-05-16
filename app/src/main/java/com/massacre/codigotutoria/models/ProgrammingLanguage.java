package com.massacre.codigotutoria.models;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by saurabh on 13/7/17.
 */
public class ProgrammingLanguage extends SugarRecord<ProgrammingLanguage>{
    private long languageId;
    private String title;
    private String imageResource;
    private String colorPrimary;
    private String colorPrimaryDark;
    private String colorAccent;
    private Date lastModified;
    private List<LanguageHeader> headers;

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(long languageId) {
        this.languageId = languageId;
    }

    public List<LanguageHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<LanguageHeader> headers) {
        this.headers = headers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public String getColorPrimary() {
        return colorPrimary;
    }

    public void setColorPrimary(String colorPrimary) {
        this.colorPrimary = colorPrimary;
    }

    public String getColorPrimaryDark() {
        return colorPrimaryDark;
    }

    public void setColorPrimaryDark(String colorPrimaryDark) {
        this.colorPrimaryDark = colorPrimaryDark;
    }

    public String getColorAccent() {
        return colorAccent;
    }

    public void setColorAccent(String colorAccent) {
        this.colorAccent = colorAccent;
    }

    public String toString(){
        return new Gson().toJson(this);
    }

    public static ProgrammingLanguage getFromJson(String json){
        return new Gson().fromJson(json,ProgrammingLanguage.class);
    }

    public static List<ProgrammingLanguage> getListFromJson(JSONArray jsonArray) throws JSONException {
        List<ProgrammingLanguage> languageList=new ArrayList<ProgrammingLanguage>();
        ProgrammingLanguage language=null;
        for (int i = 0; i < jsonArray.length(); i++) {
            language=ProgrammingLanguage.getFromJson(jsonArray.get(i).toString());
            languageList.add(language);
        }
        return languageList;
    }

}
