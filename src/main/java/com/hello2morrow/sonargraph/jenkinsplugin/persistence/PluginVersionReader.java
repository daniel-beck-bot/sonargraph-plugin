package com.hello2morrow.sonargraph.jenkinsplugin.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

import com.hello2morrow.sonargraph.jenkinsplugin.foundation.SonargraphLogger;

public class PluginVersionReader
{
    public static PluginVersionReader INSTANCE = new PluginVersionReader();
    private String m_version = "unknown";

    private PluginVersionReader()
    {
        InputStream is = getClass().getResourceAsStream("/com/hello2morrow/sonargraph/jenkinsplugin/version.properties");
        Properties props = new Properties();
        try
        {
            props.load(is);
            Object version = props.get("version");
            if (version != null)
            {
                m_version = (String) version;
            }
        }
        catch (IOException ex)
        {
            SonargraphLogger.INSTANCE.log(Level.SEVERE, "Failed to determine version of plugin: " + ex.getMessage());
        }
    }

    public String getVersion()
    {
        return m_version;
    }
}
