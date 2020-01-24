package com.funwithbasic.server.db;

public abstract class DbConstants {

    // When the tables are created, this is the number of characters that a description can hold
    public static final int FIELD_LENGTH_DEFAULT = 64;

    public static final int FIELD_LENGTH_USERNAME = 16;

    public static final String SCRIPT_FILENAME_CREATE_ALL_TABLES = "create_tables.sql";
    public static final String SCRIPT_FILENAME_DROP_ALL_TABLES = "drop_all_tables.sql";

}
