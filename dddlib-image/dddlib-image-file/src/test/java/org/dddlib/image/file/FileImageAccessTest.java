package org.dddlib.image.file;

import org.apache.commons.io.FileUtils;
import org.dddlib.image.api.ImageAccess;
import org.dddlib.image.api.pojo.ImageFile;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * Created by lingen on 14-7-16.
 */
public class FileImageAccessTest {

    private String file = getClass().getResource("/aaa.jpg").getFile();

    private String director = new File(file).getParent();

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
