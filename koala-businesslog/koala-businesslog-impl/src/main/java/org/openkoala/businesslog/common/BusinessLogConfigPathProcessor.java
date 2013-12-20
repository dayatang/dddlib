package org.openkoala.businesslog.common;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 只
 * User: zjzhai
 * Date: 12/19/13
 * Time: 10:55 AM
 */
public class BusinessLogConfigPathProcessor {

    private static final String CONFIG_PATH = "koala-log-conf";

    public static List<File> getAllConfigFiles(String configPath) {

        configPath = (configPath == null ? CONFIG_PATH : configPath);

        List<File> result = new ArrayList<File>();

        File configFilePath = new File(BusinessLogConfigPathProcessor.class.getClassLoader().getResource(configPath).getFile());

        if (!configFilePath.exists()) {
            return result;
        }

        Iterator<File> iterator = FileUtils.iterateFiles(configFilePath, new String[]{"xml"}, true);

        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    public static List<File> getAllConfigFiles() {
        return getAllConfigFiles(null);
    }

}
