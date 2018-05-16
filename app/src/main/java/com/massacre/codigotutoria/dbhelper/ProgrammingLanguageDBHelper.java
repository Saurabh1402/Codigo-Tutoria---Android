package com.massacre.codigotutoria.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import com.massacre.codigotutoria.models.LanguageHeader;
import com.massacre.codigotutoria.models.LanguageIndex;
import com.massacre.codigotutoria.models.ProgrammingContainer;
import com.massacre.codigotutoria.models.ProgrammingLanguage;
import com.massacre.codigotutoria.utils.CodigoTutoriaDateFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by saurabh on 23/7/17.
 */

public class ProgrammingLanguageDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME="codigoTutoriaSQLite";
    private static final String SQL_CREATE_PROGRAMMING_LANGUAGE="CREATE TABLE IF NOT EXISTS "+ ProgrammingContract.ProgrammingLanguageDefinition.TABLE_NAME +" ( " +
            ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_LANGUAGE_ID +" LONG PRIMARY KEY, " +
            ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_TITLE + " varchar(50) NOT NULL, " +
            ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_IMAGE_RESOURCE + " varchar(50), " +
            ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_COLOR_PRIMARY + " varchar(10), " +
            ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_COLOR_PRIMARY_DARK+ " varchar(10), " +
            ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_COLOR_ACCENT + " varchar(10)," +
            ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_LAST_MODIFIED + " date )";

    private static final String SQL_CREATE_LANGUAGE_HEADER="CREATE TABLE IF NOT EXISTS "+ ProgrammingContract.LanguageHeaderDefinition.TABLE_NAME +" ( " +
            ProgrammingContract.LanguageHeaderDefinition.COLUMN_LANGUAGE_HEADER_ID+" LONG PRIMARY KEY, " +
            ProgrammingContract.LanguageHeaderDefinition.COLUMN_HEADER_TITLE+ " varchar(50) NOT NULL, " +
            ProgrammingContract.LanguageHeaderDefinition.COLUMN_HEADER_COUNT+ " LONG, " +
            ProgrammingContract.LanguageHeaderDefinition.COLUMN_PROGRAMMING_LANGUAGE_ID + " LONG," +
            ProgrammingContract.LanguageHeaderDefinition.COLUMN_LAST_MODIFIED + " date )";

    private static final String SQL_CREATE_LANGUAGE_INDEX="CREATE TABLE IF NOT EXISTS "+ ProgrammingContract.LanguageIndexDefinition.TABLE_NAME +" ( " +
            ProgrammingContract.LanguageIndexDefinition.COLUMN_LANGUAGE_INDEX_ID+" LONG PRIMARY KEY, " +
            ProgrammingContract.LanguageIndexDefinition.COLUMN_INDEX_TITLE+ " varchar(50) NOT NULL, " +
            ProgrammingContract.LanguageIndexDefinition.COLUMN_INDEX_COUNT+ " LONG , " +
            ProgrammingContract.LanguageIndexDefinition.COLUMN_PROGRAMMING_HEADER_ID + " LONG," +
            ProgrammingContract.LanguageIndexDefinition.COLUMN_LAST_MODIFIED + " date )";
    private static final String SQL_DELETE_PROGRAMMING_LANGUAGE="DROP TABLE IF EXISTS "+ ProgrammingContract.ProgrammingLanguageDefinition.TABLE_NAME;
    private static final String SQL_DELETE_LANGUAGE_HEADER="DROP TABLE IF EXISTS "+ ProgrammingContract.LanguageHeaderDefinition.TABLE_NAME;
    private static final String SQL_DELETE_LANGUAGE_INDEX="DROP TABLE IF EXISTS "+ ProgrammingContract.LanguageIndexDefinition.TABLE_NAME;

    public ProgrammingLanguageDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_PROGRAMMING_LANGUAGE);
        sqLiteDatabase.execSQL(SQL_CREATE_LANGUAGE_HEADER);
        sqLiteDatabase.execSQL(SQL_CREATE_LANGUAGE_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_LANGUAGE_INDEX);
        sqLiteDatabase.execSQL(SQL_DELETE_LANGUAGE_HEADER);
        sqLiteDatabase.execSQL(SQL_DELETE_PROGRAMMING_LANGUAGE);
        onCreate(sqLiteDatabase);

    }
    public void insertProgrammingLanuages(ProgrammingLanguage programmingLanguage){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        //Inserting into ProgrammingLanguage
        long languageId=programmingLanguage.getLanguageId();
        values.put(ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_LANGUAGE_ID,programmingLanguage.getLanguageId());
        values.put(ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_TITLE,programmingLanguage.getTitle());
        values.put(ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_COLOR_PRIMARY,programmingLanguage.getColorPrimary());
        values.put(ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_COLOR_PRIMARY_DARK,programmingLanguage.getColorPrimaryDark());
        values.put(ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_COLOR_ACCENT,programmingLanguage.getColorAccent());
        values.put(ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_IMAGE_RESOURCE,programmingLanguage.getImageResource());
        //LogPrint.e("DatabaseServerDate",programmingLanguage.getLastModified()+"");
        values.put(ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_LAST_MODIFIED, new CodigoTutoriaDateFormatter().formatYYYYMMDD(programmingLanguage.getLastModified()));
        db.insert(ProgrammingContract.ProgrammingLanguageDefinition.TABLE_NAME,null,values);

        //Inserting into LanguageHeader
        Iterator<LanguageHeader> headerIterator=programmingLanguage.getHeaders().iterator();
        while(headerIterator.hasNext()){
            LanguageHeader languageHeader=headerIterator.next();
            values.clear();
            long headerId=languageHeader.getLanguageHeaderId();
            values.put(ProgrammingContract.LanguageHeaderDefinition.COLUMN_LANGUAGE_HEADER_ID,languageHeader.getLanguageHeaderId());
            values.put(ProgrammingContract.LanguageHeaderDefinition.COLUMN_HEADER_TITLE,languageHeader.getHeaderTitle());
            values.put(ProgrammingContract.LanguageHeaderDefinition.COLUMN_HEADER_COUNT,languageHeader.getHeaderCount());
            values.put(ProgrammingContract.LanguageHeaderDefinition.COLUMN_PROGRAMMING_LANGUAGE_ID,languageId);
            values.put(ProgrammingContract.LanguageHeaderDefinition.COLUMN_LAST_MODIFIED, new CodigoTutoriaDateFormatter().formatYYYYMMDD(languageHeader.getLastModified()));
            db.insert(ProgrammingContract.LanguageHeaderDefinition.TABLE_NAME,null,values);
            Iterator<LanguageIndex> indexIterator=languageHeader.getIndex().iterator();

            while(indexIterator.hasNext()){
                values.clear();
                LanguageIndex languageIndex=indexIterator.next();
                values.put(ProgrammingContract.LanguageIndexDefinition.COLUMN_LANGUAGE_INDEX_ID,languageIndex.getLanguageIndexId());
                values.put(ProgrammingContract.LanguageIndexDefinition.COLUMN_INDEX_TITLE,languageIndex.getIndexTitle());
                values.put(ProgrammingContract.LanguageIndexDefinition.COLUMN_INDEX_COUNT,languageIndex.getIndexCount());
                values.put(ProgrammingContract.LanguageIndexDefinition.COLUMN_PROGRAMMING_HEADER_ID,headerId);
                values.put(ProgrammingContract.LanguageIndexDefinition.COLUMN_LAST_MODIFIED, new CodigoTutoriaDateFormatter().formatYYYYMMDD(languageIndex.getLastModified()));
                db.insert(ProgrammingContract.LanguageIndexDefinition.TABLE_NAME,null,values);
            }

        }

    }

    public void insertAllProgrammingLanguage(List<ProgrammingLanguage> languageList){
        for (int i=0;i<languageList.size();i++){
            ProgrammingLanguage programmingLanguage=languageList.get(i);
            Log.e("Location:","ProgrammingLanguageDBHelper.insertAllProgrammingLanguage() - "+programmingLanguage.getTitle());
            this.insertProgrammingLanuages(programmingLanguage);
        }
    }

    public JSONArray getLanguagesIdDateInSQLite() throws  JSONException {
        Log.e("Note: ","inside DBHelper.getLanguageInSQLite");
        String column[]={ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_LANGUAGE_ID,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_LAST_MODIFIED};
        SQLiteDatabase db=this.getReadableDatabase();
        List<ProgrammingContainer> container=new ArrayList<ProgrammingContainer>();
        Cursor cursor=db.query(ProgrammingContract.ProgrammingLanguageDefinition.TABLE_NAME,column,null,null,null,null, null);
        cursor.moveToFirst();
        ProgrammingContainer programmingContainer;
        while(cursor.isAfterLast()==false){
            programmingContainer=new ProgrammingContainer();
            programmingContainer.setLanguageId(cursor.getLong(0));
            //Log.e("LanguageId",cursor.getLong(0)+"");
            //Log.e("lastModified",cursor.getString(1));
            try {
                programmingContainer.setLastModified(new CodigoTutoriaDateFormatter().parse(cursor.getString(1)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            container.add(programmingContainer);
            cursor.moveToNext();
        }
        String gson=new Gson().toJson(container,List.class);
        JSONArray jsonArray=new JSONArray(gson);
        Log.e("getLanguagesIdDateLite",jsonArray.toString());
        Log.e("Note: ","Outside DBHelper.getLanguageInSQLite");
        return jsonArray;
    }
    public List<ProgrammingLanguage> getProgrammingLanguageInSQLite() throws ParseException {
        Log.e("Location","inside DBHelper getProgrammingLanguageInSQLite");
        String[] columnPrgrammmingLanguage={
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_LANGUAGE_ID,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_TITLE,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_IMAGE_RESOURCE,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_COLOR_PRIMARY,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_COLOR_PRIMARY_DARK,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_COLOR_ACCENT,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_LAST_MODIFIED
        };
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursorProgrammingLanguage=db.query(
                ProgrammingContract.ProgrammingLanguageDefinition.TABLE_NAME,
                columnPrgrammmingLanguage,
                null,null,null,null,null
        );
        cursorProgrammingLanguage.moveToFirst();
        List<ProgrammingLanguage> programmingLanguageList=new ArrayList<ProgrammingLanguage>();
        CodigoTutoriaDateFormatter dateFormatter=new CodigoTutoriaDateFormatter();
        ProgrammingLanguage programmingLanguage;
        while (!cursorProgrammingLanguage.isAfterLast()) {
            programmingLanguage = new ProgrammingLanguage();
            programmingLanguage.setLanguageId(cursorProgrammingLanguage.getLong(0));
            programmingLanguage.setTitle(cursorProgrammingLanguage.getString(1));
            programmingLanguage.setImageResource(cursorProgrammingLanguage.getString(2));
            programmingLanguage.setColorPrimary(cursorProgrammingLanguage.getString(3));
            programmingLanguage.setColorPrimaryDark(cursorProgrammingLanguage.getString(4));
            programmingLanguage.setColorAccent(cursorProgrammingLanguage.getString(5));
            programmingLanguage.setLastModified(dateFormatter.parse(cursorProgrammingLanguage.getString(6)));

            Log.e("json of prg_lang",new Gson().toJson(programmingLanguage));
            programmingLanguageList.add(programmingLanguage);
            cursorProgrammingLanguage.moveToNext();
        }
        Log.e("Location","Getting outside ProgrammingDBHelper.getProgrammingLanguageInSQLite");
        return programmingLanguageList;
        }
    public List<ProgrammingLanguage> getLanguageListInSQLite() throws ParseException {
        Log.e("Location","Inside of getLanguageListInSQLite");
        String[] columnPrgrammmingLanguage={
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_LANGUAGE_ID,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_TITLE,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_IMAGE_RESOURCE,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_COLOR_PRIMARY,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_COLOR_PRIMARY_DARK,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_COLOR_ACCENT,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_LAST_MODIFIED
                };
        String[] columnLanguageHeader={
                ProgrammingContract.LanguageHeaderDefinition.COLUMN_LANGUAGE_HEADER_ID,
                ProgrammingContract.LanguageHeaderDefinition.COLUMN_HEADER_TITLE,
                ProgrammingContract.LanguageHeaderDefinition.COLUMN_HEADER_COUNT,
                ProgrammingContract.LanguageHeaderDefinition.COLUMN_LAST_MODIFIED,
                ProgrammingContract.LanguageHeaderDefinition.COLUMN_PROGRAMMING_LANGUAGE_ID,
        };
        String[] columnLanguageIndex={
                ProgrammingContract.LanguageIndexDefinition.COLUMN_LANGUAGE_INDEX_ID,
                ProgrammingContract.LanguageIndexDefinition.COLUMN_INDEX_TITLE,
                ProgrammingContract.LanguageIndexDefinition.COLUMN_INDEX_COUNT,
                ProgrammingContract.LanguageIndexDefinition.COLUMN_PROGRAMMING_HEADER_ID,
                ProgrammingContract.LanguageIndexDefinition.COLUMN_LAST_MODIFIED
        };
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursorProgrammingLanguage=db.query(
                ProgrammingContract.ProgrammingLanguageDefinition.TABLE_NAME,
                columnPrgrammmingLanguage,
                null,null,null,null,null
                );
        cursorProgrammingLanguage.moveToFirst();
        List<ProgrammingLanguage> programmingLanguageList=new ArrayList<ProgrammingLanguage>();
        CodigoTutoriaDateFormatter dateFormatter=new CodigoTutoriaDateFormatter();
        ProgrammingLanguage programmingLanguage;
        LanguageHeader languageHeader;
        LanguageIndex languageIndex;
        while (!cursorProgrammingLanguage.isAfterLast()){
            programmingLanguage=new ProgrammingLanguage();
            programmingLanguage.setLanguageId(cursorProgrammingLanguage.getLong(0));
            programmingLanguage.setTitle(cursorProgrammingLanguage.getString(1));
            programmingLanguage.setImageResource(cursorProgrammingLanguage.getString(2));
            programmingLanguage.setColorPrimary(cursorProgrammingLanguage.getString(3));
            programmingLanguage.setColorPrimaryDark(cursorProgrammingLanguage.getString(4));
            programmingLanguage.setColorAccent(cursorProgrammingLanguage.getString(5));
            programmingLanguage.setLastModified(dateFormatter.parse(cursorProgrammingLanguage.getString(6)));
            programmingLanguage.setHeaders(new ArrayList<LanguageHeader>());
            Cursor cursorLanguageHeader=db.query(
                    ProgrammingContract.LanguageHeaderDefinition.TABLE_NAME,
                    columnLanguageHeader,
                    ProgrammingContract.LanguageHeaderDefinition.COLUMN_PROGRAMMING_LANGUAGE_ID+"=?",
                    new String[]{programmingLanguage.getLanguageId()+""},
                    null,null,null
            );
            cursorLanguageHeader.moveToFirst();
            while (!cursorLanguageHeader.isAfterLast()){
                languageHeader=new LanguageHeader();
                languageHeader.setLanguageHeaderId(cursorLanguageHeader.getLong(0));
                languageHeader.setHeaderTitle(cursorLanguageHeader.getString(1));
                languageHeader.setHeaderCount(cursorLanguageHeader.getLong(2));
                languageHeader.setLastModified(dateFormatter.parse(cursorLanguageHeader.getString(3)));
                languageHeader.setProgrammingLanguageId(cursorLanguageHeader.getLong(0));
                languageHeader.setIndex(new ArrayList<LanguageIndex>());

                programmingLanguage.getHeaders().add(languageHeader);

                Cursor cursorLanguageIndex=db.query(
                        ProgrammingContract.LanguageIndexDefinition.TABLE_NAME,
                        columnLanguageIndex,
                        ProgrammingContract.LanguageIndexDefinition.COLUMN_PROGRAMMING_HEADER_ID+"=?",
                        new String[]{languageHeader.getLanguageHeaderId()+""},
                        null,null,null);
                cursorLanguageIndex.moveToFirst();
                while (!cursorLanguageIndex.isAfterLast()){
                    languageIndex=new LanguageIndex();
                    languageIndex.setLanguageIndexId(cursorLanguageIndex.getLong(0));
                    languageIndex.setIndexTitle(cursorLanguageIndex.getString(1));
                    languageIndex.setIndexCount(cursorLanguageIndex.getLong(2));
                    languageIndex.setLanguageHeaderId(cursorLanguageIndex.getLong(3));
                    languageIndex.setLastModified(dateFormatter.parse(cursorLanguageIndex.getString(4)));

                    languageHeader.getIndex().add(languageIndex);

                    cursorLanguageIndex.moveToNext();
                }

                cursorLanguageHeader.moveToNext();

            }

            programmingLanguageList.add(programmingLanguage);

            cursorProgrammingLanguage.moveToNext();
        }
        return programmingLanguageList;
    }
    public ProgrammingLanguage getLanguageByIdInSQLite(long languageId) throws ParseException {
        Log.e("Location","Inside of getLanguageByIdInSQLite");
        String[] columnPrgrammmingLanguage={
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_LANGUAGE_ID,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_TITLE,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_IMAGE_RESOURCE,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_COLOR_PRIMARY,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_COLOR_PRIMARY_DARK,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_COLOR_ACCENT,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_LAST_MODIFIED
        };
        String[] columnLanguageHeader={
                ProgrammingContract.LanguageHeaderDefinition.COLUMN_LANGUAGE_HEADER_ID,
                ProgrammingContract.LanguageHeaderDefinition.COLUMN_HEADER_TITLE,
                ProgrammingContract.LanguageHeaderDefinition.COLUMN_HEADER_COUNT,
                ProgrammingContract.LanguageHeaderDefinition.COLUMN_LAST_MODIFIED,
                ProgrammingContract.LanguageHeaderDefinition.COLUMN_PROGRAMMING_LANGUAGE_ID,
        };
        String[] columnLanguageIndex={
                ProgrammingContract.LanguageIndexDefinition.COLUMN_LANGUAGE_INDEX_ID,
                ProgrammingContract.LanguageIndexDefinition.COLUMN_INDEX_TITLE,
                ProgrammingContract.LanguageIndexDefinition.COLUMN_INDEX_COUNT,
                ProgrammingContract.LanguageIndexDefinition.COLUMN_PROGRAMMING_HEADER_ID,
                ProgrammingContract.LanguageIndexDefinition.COLUMN_LAST_MODIFIED
        };
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursorProgrammingLanguage=db.query(
                ProgrammingContract.ProgrammingLanguageDefinition.TABLE_NAME,
                columnPrgrammmingLanguage,
                ProgrammingContract.ProgrammingLanguageDefinition.COLUMN_LANGUAGE_ID+"=?",
                new String[]{languageId+""},
                null,null,null
        );
        cursorProgrammingLanguage.moveToFirst();
        CodigoTutoriaDateFormatter dateFormatter=new CodigoTutoriaDateFormatter();
        ProgrammingLanguage programmingLanguage=null;
        LanguageHeader languageHeader;
        LanguageIndex languageIndex;
        if(!cursorProgrammingLanguage.isAfterLast()){
            programmingLanguage=new ProgrammingLanguage();
            programmingLanguage.setLanguageId(cursorProgrammingLanguage.getLong(0));
            programmingLanguage.setTitle(cursorProgrammingLanguage.getString(1));
            programmingLanguage.setImageResource(cursorProgrammingLanguage.getString(2));
            programmingLanguage.setColorPrimary(cursorProgrammingLanguage.getString(3));
            programmingLanguage.setColorPrimaryDark(cursorProgrammingLanguage.getString(4));
            programmingLanguage.setColorAccent(cursorProgrammingLanguage.getString(5));
            programmingLanguage.setLastModified(dateFormatter.parse(cursorProgrammingLanguage.getString(6)));
            programmingLanguage.setHeaders(new ArrayList<LanguageHeader>());
            Cursor cursorLanguageHeader=db.query(
                    ProgrammingContract.LanguageHeaderDefinition.TABLE_NAME,
                    columnLanguageHeader,
                    ProgrammingContract.LanguageHeaderDefinition.COLUMN_PROGRAMMING_LANGUAGE_ID+"=?",
                    new String[]{programmingLanguage.getLanguageId()+""},
                    null,null,null
            );
            cursorLanguageHeader.moveToFirst();
            while (!cursorLanguageHeader.isAfterLast()){
                languageHeader=new LanguageHeader();
                languageHeader.setLanguageHeaderId(cursorLanguageHeader.getLong(0));
                languageHeader.setHeaderTitle(cursorLanguageHeader.getString(1));
                languageHeader.setHeaderCount(cursorLanguageHeader.getLong(2));
                languageHeader.setLastModified(dateFormatter.parse(cursorLanguageHeader.getString(3)));
                languageHeader.setProgrammingLanguageId(cursorLanguageHeader.getLong(0));
                languageHeader.setIndex(new ArrayList<LanguageIndex>());

                programmingLanguage.getHeaders().add(languageHeader);

                Cursor cursorLanguageIndex=db.query(
                        ProgrammingContract.LanguageIndexDefinition.TABLE_NAME,
                        columnLanguageIndex,
                        ProgrammingContract.LanguageIndexDefinition.COLUMN_PROGRAMMING_HEADER_ID+"=?",
                        new String[]{languageHeader.getLanguageHeaderId()+""},
                        null,null,null);
                cursorLanguageIndex.moveToFirst();
                while (!cursorLanguageIndex.isAfterLast()){
                    languageIndex=new LanguageIndex();
                    languageIndex.setLanguageIndexId(cursorLanguageIndex.getLong(0));
                    languageIndex.setIndexTitle(cursorLanguageIndex.getString(1));
                    languageIndex.setIndexCount(cursorLanguageIndex.getLong(2));
                    languageIndex.setLanguageHeaderId(cursorLanguageIndex.getLong(3));
                    languageIndex.setLastModified(dateFormatter.parse(cursorLanguageIndex.getString(4)));

                    languageHeader.getIndex().add(languageIndex);

                    cursorLanguageIndex.moveToNext();
                }

                cursorLanguageHeader.moveToNext();

            }


        }
        return programmingLanguage;
    }
}
