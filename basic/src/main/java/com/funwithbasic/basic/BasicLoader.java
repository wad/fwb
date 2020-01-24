package com.funwithbasic.basic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BasicLoader {

    public static String loadIntoString(String filename) throws IOException {
        StringBuilder builder = new StringBuilder();
        FileInputStream fileInputStream = null;
        DataInputStream dataInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileInputStream = new FileInputStream(filename);
            dataInputStream = new DataInputStream(fileInputStream);
            inputStreamReader = new InputStreamReader(dataInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null) {
                builder.append(strLine);
                builder.append("\n");
            }
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                }
                catch (IOException x) {
                    // do nothing
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                }
                catch (IOException x) {
                    // do nothing
                }
            }
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                }
                catch (IOException x) {
                    // do nothing
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                }
                catch (IOException x) {
                    // do nothing
                }
            }
        }
        return builder.toString();
    }

    public static List<String> loadIntoStringList(String filename) throws IOException {
        List<String> lines = new ArrayList<String>();
        FileInputStream fileInputStream = null;
        DataInputStream dataInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileInputStream = new FileInputStream(filename);
            dataInputStream = new DataInputStream(fileInputStream);
            inputStreamReader = new InputStreamReader(dataInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null) {
                lines.add(strLine);
            }
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                }
                catch (IOException x) {
                    // do nothing
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                }
                catch (IOException x) {
                    // do nothing
                }
            }
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                }
                catch (IOException x) {
                    // do nothing
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                }
                catch (IOException x) {
                    // do nothing
                }
            }
        }
        return lines;
    }

}
