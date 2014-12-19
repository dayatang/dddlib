package org.dayatang.configuration.impl;

import org.dayatang.configuration.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Created by yyang on 14/12/19.
 */
public class ConfigurationInputStreamImpl extends AbstractConfiguration  {
    private InputStream in;
    private PropertiesFileUtils pfu = new PropertiesFileUtils("utf-8");

    public ConfigurationInputStreamImpl(InputStream in) {
        this.in = in;
    }

    @Override
    public void load() {
        hTable = new Hashtable<String, String>();
        Properties props = new Properties();
        try {
            props.load(in);
            hTable = pfu.rectifyProperties(props);
            //LOGGER.debug("Load configuration from {} at {}", file.getAbsolutePath(), new Date());
        } catch (IOException e) {
            throw new ConfigurationException("Cannot load config file", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new ConfigurationException("Cannot close input stream.", e);
                }
            }
        }

    }
}
