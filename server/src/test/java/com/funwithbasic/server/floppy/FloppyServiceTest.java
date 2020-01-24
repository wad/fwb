package com.funwithbasic.server.floppy;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class FloppyServiceTest {

    @Test
    public void testZeroPadding() throws FloppyException {
        assertEquals("000013", FloppyService.zeroPad(13));
    }

    @Test
    public void testCRUD() throws FloppyException {
        int userId = 900000;

        // make sure we're starting fresh
        try {
            FloppyService.deleteFloppy(userId);
        } catch (FloppyException e) {
            // do nothing
        }

        String fileName1 = "poem";
        String content1 = "Roses are red\nBacon is red\nPoems are hard\nBacon";
        String fileName2 = "oneline";
        String content2 = "just this";
        FloppyService.createFloppy(userId);
        assertEquals(0, FloppyService.listFiles(userId).size());
        assertFalse(FloppyService.doesFileExist(userId, fileName1));
        FloppyService.createFile(userId, fileName1, content1);
        assertTrue(FloppyService.doesFileExist(userId, fileName1));
        FloppyService.createFile(userId, fileName2, content2);
        List<String> files = FloppyService.listFiles(userId);
        assertEquals(2, files.size());
        FloppyService.deleteFile(userId, fileName2);
        files = FloppyService.listFiles(userId);
        assertEquals(1, files.size());
        String contentRead = FloppyService.readFile(userId, fileName1);
        assertEquals(content1, contentRead);
        FloppyService.updateFile(userId, fileName1, content2);
        contentRead = FloppyService.readFile(userId, fileName1);
        assertEquals(content2, contentRead);
        FloppyService.deleteFloppy(userId);
    }

}
