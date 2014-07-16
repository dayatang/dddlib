package org.dddlib.image.file;

import org.apache.commons.io.FileUtils;
import org.dddlib.image.api.ImageAccess;
import org.dddlib.image.api.pojo.ImageFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by lingen on 14-7-16.
 */
public class FileImageAccess implements ImageAccess {

    private static String FILE_SUFFIX = "__";

    private String directory;

    public FileImageAccess(String directory) {
        this.directory = directory;
        validateDiretor();
    }

    @Override
    public String saveImageFile(byte[] content, String fileName) {
        String uuid = UUID.randomUUID().toString().toUpperCase();
        String newFileName = uuid + FILE_SUFFIX + fileName;
        try {
            FileUtils.writeByteArrayToFile(new File(directory + File.separator + newFileName), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFileName;
    }

    @Override
    public ImageFile getImageFile(String imageId) {
        File file = new File(directory + File.separator + imageId);
        try {
            if (file.exists()) {
                byte[] contents = FileUtils.readFileToByteArray(file);
                String fileName = imageId.substring(imageId.indexOf(FILE_SUFFIX) + FILE_SUFFIX.length());
                ImageFile imageFile = new ImageFile(contents, fileName);
                return imageFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void validateDiretor() {
        File file = new File(directory);
        if (file.exists() && file.isFile()) {
            throw new RuntimeException("请指定目录");
        }
        if (file.exists() == false) {
            file.mkdirs();
        }
    }
}
