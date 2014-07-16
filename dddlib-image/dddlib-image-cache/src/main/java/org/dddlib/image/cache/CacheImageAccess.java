package org.dddlib.image.cache;

import org.dayatang.cache.Cache;
import org.dddlib.image.api.ImageAccess;
import org.dddlib.image.api.pojo.ImageFile;

import java.util.UUID;

/**
 * Created by lingen on 14-7-16.
 */
public class CacheImageAccess implements ImageAccess {

    private Cache cache;

    public CacheImageAccess(Cache cache) {
        this.cache = cache;
    }

    @Override
    public String saveImageFile(byte[] content, String filename) {
        assert cache != null : "cache不能为空";
        assert content != null : "图片内容不能为空";
        assert filename != null : "图片文件名不能为空";

        ImageFile imageFile = new ImageFile(content, filename);
        String uuid = UUID.randomUUID().toString().toUpperCase();
        cache.put(uuid, imageFile);
        return uuid;
    }

    @Override
    public ImageFile getImageFile(String imageID) {
        assert cache != null : "cache不能为空";
        assert imageID != null : "请指定图片ID";

        if (cache.containsKey(imageID)) {
            ImageFile imageFile = (ImageFile) cache.get(imageID);
            return imageFile;
        }
        return null;

    }
}
