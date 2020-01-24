package com.funwithbasic.server.tool;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Properties;

public class PropertyTool {

    public static final String PROPERTY_FILE = "fwb.properties";

    public enum prop {
        databaseHostName("dbhost", "localhost"),
        databaseName("dbname", "fwb"),
        databaseUserName("dbuser", "fwbuser"),
        databaseUserPassword("dbpassword", "password"),
        floppyStorageLocation("floppyparentpath", "/var/fwb/user_floppy_data"),
        lifespanOfActiveProgramsInMinutes("aplifespan", "2000"),
        ownerPassword("ownerpassword", "fwb_rocks_");

        String propertyName;
        String defaultValue;

        prop(String propertyName, String defaultValue) {
            this.propertyName = propertyName;
            this.defaultValue = defaultValue;
        }
    }

    private EnumMap<prop, String> dbPropertyMap = new EnumMap<prop, String>(prop.class);

    private static PropertyTool instance = null;

    public static PropertyTool getInstance() {
        if (instance == null) {
            instance = new PropertyTool();
        }
        return instance;
    }

    private PropertyTool() {
        try {
            InputStream resourceAsStream = PropertyTool.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            for (prop property : prop.values()) {
                dbPropertyMap.put(property, properties.getProperty(property.propertyName, property.defaultValue));
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Failure reading properties file (" + PROPERTY_FILE + "): " + ioe.getMessage(), ioe);
        }
    }

    private String getPropertyPrivate(prop property) {
        return dbPropertyMap.get(property);
    }

    public static String get(prop property) {
        return getInstance().getPropertyPrivate(property);
    }

}
