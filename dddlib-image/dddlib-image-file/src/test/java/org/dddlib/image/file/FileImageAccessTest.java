package org.dddlib.image.file;

import org.apache.commons.io.FileUtils;
import org.dddlib.image.api.ImageAccess;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

import org.dddlib.image.api.pojo.ImageFile;
import org.junit.Test;

/**
 * Created by lingen on 14-7-16.
 */
public class FileImageAccessTest {

    private String file = "/Users/lingen/abc.jpg";

    private String director = "/Users/lingen/TTT";

    @Test
    public void test() throws IOException {
        ImageAccess imageAccess = new FileImageAccess(director);

        String mediaId =  imageAccess.saveImageFile(FileUtils.readFileToByteArray(new File(file)),"abc.jpg");

        assertTrue(mediaId!=null);

        ImageFile imageFile = imageAccess.getImageFile(mediaId);

        assertTrue(imageFile.getContent()!=null);
        assertTrue(imageFile.getFileName().equals("abc.jpg"));

    }

}
