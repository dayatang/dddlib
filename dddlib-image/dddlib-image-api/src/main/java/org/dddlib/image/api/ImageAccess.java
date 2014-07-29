package org.dddlib.image.api;

import org.dddlib.image.api.pojo.ImageFile;

/**
 * Created by lingen on 14-7-16.
 */
public interface ImageAccess {

    /**
     * 存取一个图片，指定图片内容及文件名后缀
     * @param content 图片内容
     * @param fileName  文件名,包含后缀
     * @return  返回代表此图片的唯一ID
     */
    public String saveImageFile(byte[] content,String fileName);

    /**
     * 根据图片ID返回此图片
     * @param imageID  传入代表图片的唯一ID
     * @return  返回图片文件
     */
    public ImageFile getImageFile(String imageID);
}
