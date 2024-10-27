package com.yureiapplications.materiamedica.util;

public class Util {

    //DATABASE
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "materia_medica.db";
    public static final String DATABASE_PATH = "/data/data/com.yureiapplications.materiamedica/databases/";
    //TABLES
    public static String TABLE_NAME_1 = "Categories";
    public static String TABLE_NAME_2 = "Medicines";

    //CATEGORIES COLUMNS
    public static final String CATEGORIES_ID = "categories_id";
    public static final String CATEGORIES_NAME = "categories_name";
    public static final String CATEGORIES_DESCRIPTION = "categories_description";
    public static final String CATEGORIES_IMAGE = "categories_image";

    //MEDICINES COLUMNS
    public static final String MEDICINES_ID = "medicines_id";
    public static final String MEDICINES_CATEGORIES = "medicines_categories";
    public static final String MEDICINES_NAME = "medicines_name";
    public static final String MEDICINES_DESCRIPTION = "medicines_description";
    public static final String MEDICINES_IMAGE = "medicines_image";
    public static final String MEDICINES_REFERENCES = "medicines_references";
}
