package com.funwithbasic.server.floppy;

import com.funwithbasic.server.tool.PropertyTool;
import com.funwithbasic.server.tool.ValidationTool;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static com.funwithbasic.server.floppy.FloppyException.Reason;

// A "floppy" is simply a directory where the user can store their files.
// It is represented by the zero-padded userId number.
// There is no concept of user's subdirectories.

public class FloppyService {

    public static final int MAX_FILENAME_LENGTH = 20;

    static final String SLASH = "/";
    static final int USER_DIRECTORY_LENGTH = 6;
    public static final int MAX_USERID = 999999;
    static final String ENCODING = "UTF-8";

    public static void createFloppy(int userId) throws FloppyException {
        String userLocation = getUserLocation(userId);
        File userArea = new File(userLocation);
        if (userArea.exists()) {
            throw new FloppyException(Reason.System, "User directory already exists, cannot create: " + userLocation);
        }
        if (!userArea.mkdir()) {
            throw new FloppyException(Reason.System, "Failed to create user directory: " + userLocation);
        }
    }

    public static void deleteFloppy(int userId) throws FloppyException {
        String userLocation = getUserLocation(userId);
        File userArea = new File(userLocation);
        if (!userArea.exists()) {
            throw new FloppyException(Reason.System, "User directory does not exist, cannot delete: " + userLocation);
        }
        for (String fileName : listFiles(userId)) {
            deleteFile(userId, fileName);
        }
        if (!userArea.delete()) {
            throw new FloppyException(Reason.System, "Failed to delete user directory: " + userLocation);
        }
    }

    public static List<String> listFloppies() throws FloppyException {
        String floppyParentLocation = getFloppyParentLocation();
        File floppyParent = new File(floppyParentLocation);
        if (!floppyParent.exists()) {
            throw new FloppyException(Reason.System, "Floppy directory does not exist, cannot list contents: " + floppyParentLocation);
        }
        return Arrays.asList(floppyParent.list());
    }

    public static List<String> listFiles(int userId) throws FloppyException {
        String userLocation = getUserLocation(userId);
        File userArea = new File(userLocation);
        if (!userArea.exists()) {
            throw new FloppyException(Reason.System, "User directory does not exist, cannot list contents: " + userLocation);
        }
        return Arrays.asList(userArea.list());
    }

    public static boolean doesFileExist(int userId, String fileName) throws FloppyException {
        if (!isFilenameValid(fileName)) {
            throw new FloppyException(Reason.InvalidFilename, "Cannot check exists, invalid fileName: " + fileName);
        }
        String fileAndPathToCheck = getUserLocation(userId) + SLASH + fileName;
        File fileToCheck = new File(fileAndPathToCheck);
        return fileToCheck.exists();
    }

    public static void createFile(int userId, String fileName, String content) throws FloppyException {
        if (!isFilenameValid(fileName)) {
            throw new FloppyException(Reason.InvalidFilename, "Cannot create, invalid fileName: " + fileName);
        }
        String fileAndPathToCreate = getUserLocation(userId) + SLASH + fileName;
        File fileToCreate = new File(fileAndPathToCreate);
        if (fileToCreate.exists()) {
            throw new FloppyException(Reason.CannotCreateExisting, "File already exists, cannot create: " + fileAndPathToCreate);
        }
        boolean fileWasCreated;
        try {
            fileWasCreated = fileToCreate.createNewFile();
        } catch (IOException e) {
            throw new FloppyException(Reason.System, "Could not create file (" + e.getMessage() + "): " + fileAndPathToCreate, e);
        }
        if (!fileWasCreated) {
            throw new FloppyException(Reason.System, "Could not create file : " + fileAndPathToCreate);
        }
        if (!fileToCreate.canWrite()) {
            throw new RuntimeException("Failed to be able to write to a file that I just created: " + fileAndPathToCreate);
        }
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            fileOutputStream = new FileOutputStream(fileToCreate);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, ENCODING);
            outputStreamWriter.write(content);
        } catch (UnsupportedEncodingException e) {
            throw new FloppyException(Reason.System, "Failed to write to a file that I just created due to encoding: " + fileAndPathToCreate, e);
        } catch (FileNotFoundException e) {
            throw new FloppyException(Reason.System, "Failed to write to a file that I just created: " + fileAndPathToCreate, e);
        } catch (IOException e) {
            throw new FloppyException(Reason.System, "Failed to write to a file that I just created due to IO: " + fileAndPathToCreate, e);
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }

    public static String readFile(int userId, String fileName) throws FloppyException {
        if (!isFilenameValid(fileName)) {
            throw new FloppyException(Reason.InvalidFilename, "Cannot read, invalid fileName: " + fileName);
        }
        String fileAndPathToRead = getUserLocation(userId) + SLASH + fileName;
        File fileToRead = new File(fileAndPathToRead);
        if (!fileToRead.exists()) {
            throw new FloppyException(Reason.FileNotFound, "File does not exist, cannot read: " + fileAndPathToRead);
        }
        if (!fileToRead.canRead()) {
            throw new RuntimeException("Failed to be able to read file: " + fileAndPathToRead);
        }
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        StringBuilder fileContent = new StringBuilder();
        try {
            fileReader = new FileReader(fileToRead);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileContent.append(line);
                fileContent.append("\n");
            }
            fileContent.deleteCharAt(fileContent.length() - 1);
        } catch (FileNotFoundException e) {
            throw new FloppyException(Reason.System, "Failed to find file I've already found to read: " + fileAndPathToRead, e);
        } catch (IOException e) {
            throw new FloppyException(Reason.System, "Failed to read file: " + fileAndPathToRead, e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
        return fileContent.toString();
    }

    public static void updateFile(int userId, String fileName, String newContent) throws FloppyException {
        if (!isFilenameValid(fileName)) {
            throw new FloppyException(Reason.InvalidFilename, "Cannot update, invalid fileName: " + fileName);
        }
        String fileAndPathToUpdate = getUserLocation(userId) + SLASH + fileName;
        File fileToUpdate = new File(fileAndPathToUpdate);
        if (!fileToUpdate.exists()) {
            throw new FloppyException(Reason.FileNotFound, "File does not exist, cannot update: " + fileAndPathToUpdate);
        }
        if (!fileToUpdate.canWrite()) {
            throw new FloppyException(Reason.System, "Failed to be able to write to file: " + fileAndPathToUpdate);
        }
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            fileOutputStream = new FileOutputStream(fileToUpdate);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, ENCODING);
            outputStreamWriter.write(newContent);
        } catch (UnsupportedEncodingException e) {
            throw new FloppyException(Reason.System, "Failed to update a file due to encoding: " + fileAndPathToUpdate, e);
        } catch (FileNotFoundException e) {
            throw new FloppyException(Reason.System, "Failed to update a file that I just saw: " + fileAndPathToUpdate, e);
        } catch (IOException e) {
            throw new FloppyException(Reason.System, "Failed to write to a file due to IO: " + fileAndPathToUpdate, e);
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }

    public static void deleteFile(int userId, String fileName) throws FloppyException {
        String fileAndPathToDelete = getUserLocation(userId) + SLASH + fileName;
        File fileToDelete = new File(fileAndPathToDelete);
        if (!fileToDelete.exists()) {
            throw new FloppyException(Reason.FileNotFound, "File does not exist, cannot delete: " + fileAndPathToDelete);
        }
        if (!fileToDelete.delete()) {
            throw new FloppyException(Reason.System, "Failed to delete file: " + fileAndPathToDelete);
        }
    }

    static boolean isFilenameValid(String fileName) {
        return ValidationTool.validateAlphaNumericOnlySpacelessField(fileName, MAX_FILENAME_LENGTH);
    }

    static String getFloppyParentLocation() throws FloppyException {
        String floppyParentDirectoryFilename = PropertyTool.get(PropertyTool.prop.floppyStorageLocation);
        File floppyParentDirectory = new File(floppyParentDirectoryFilename);
        if (!floppyParentDirectory.exists()) {
            throw new FloppyException(Reason.System, "Floppy parent directory does not exist: " + floppyParentDirectoryFilename);
        }
        if (!floppyParentDirectory.isDirectory()) {
            throw new FloppyException(Reason.System, "Floppy parent directory is not a directory: " + floppyParentDirectoryFilename);
        }
        if (!floppyParentDirectory.canRead() || !floppyParentDirectory.canWrite()) {
            throw new FloppyException(Reason.System, "Lacking read or write permission on floppy parent directory: " + floppyParentDirectoryFilename);
        }
        return floppyParentDirectoryFilename;
    }

    static String zeroPad(int userId) throws FloppyException {
        if (userId > (MAX_USERID)) {
            throw new FloppyException(Reason.System, "UserId too large: " + userId);
        }
        if (userId <= 0) {
            throw new FloppyException(Reason.System, "UserId zero or negative: " + userId);
        }
        return String.format("%0" + USER_DIRECTORY_LENGTH + "d", userId);
    }

    static String getUserLocation(int userId) throws FloppyException {
        return getFloppyParentLocation() + SLASH + zeroPad(userId);
    }

}
