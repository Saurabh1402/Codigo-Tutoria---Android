package com.massacre.codigotutoria.dbhelper;

import android.provider.BaseColumns;

/**
 * Created by saurabh on 23/7/17.
 */

public class ProgrammingContract {
    private ProgrammingContract(){}
    public static class ProgrammingLanguageDefinition implements BaseColumns{
        public final static String TABLE_NAME = "programminglanguage";
        public final static String COLUMN_LANGUAGE_ID="language_id";
        public final static String COLUMN_TITLE="title";
        public final static String COLUMN_IMAGE_RESOURCE="image_resource";
        public final static String COLUMN_COLOR_PRIMARY="color_primary";
        public final static String COLUMN_COLOR_PRIMARY_DARK="color_primary_dark";
        public final static String COLUMN_COLOR_ACCENT="color_accent";
        public final static String COLUMN_LAST_MODIFIED ="last_modified";

    }
    public static class LanguageHeaderDefinition implements BaseColumns{
        public final static String TABLE_NAME = "languageheader";
        public final static String COLUMN_LANGUAGE_HEADER_ID="language_header_id";
        public final static String COLUMN_HEADER_TITLE="header_title";
        public final static String COLUMN_HEADER_COUNT="header_count";
        public final static String COLUMN_PROGRAMMING_LANGUAGE_ID="programming_language_id";
        public final static String COLUMN_LAST_MODIFIED ="last_modified";

    }
    public static class LanguageIndexDefinition implements BaseColumns{
        public final static String TABLE_NAME = "languageindex";
        public final static String COLUMN_LANGUAGE_INDEX_ID="language_index_id";
        public final static String COLUMN_INDEX_TITLE="index_title";
        public final static String COLUMN_INDEX_COUNT="index_count";
        public final static String COLUMN_PROGRAMMING_HEADER_ID="programming_header_id";
        public final static String COLUMN_LAST_MODIFIED ="last_modified";
    }
}
