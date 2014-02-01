package com.dayatang.commons.db;

import java.io.File;
import java.sql.SQLException;

import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;

import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2Server extends ExternalResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2Server.class);

    private String dir;

    private static final String DB_FILE = "h2-test-db";

    private Server server;

    public H2Server(String dir) {
        this.dir = dir;
    }

    @Override
    protected void before() throws Throwable {
        try {
            DeleteDbFiles.execute(dir, DB_FILE, true);
            server = Server.createTcpServer(new String[0]);
            server.start();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("H2 server started! data file is {}", new File(dir, DB_FILE).getAbsolutePath());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot start h2 server database", e);
        }
    }

    @Override
    protected void after() {
        if (server == null) {
            return;
        }
        if (server.isRunning(true)) {
            server.stop();
            server.shutdown();
            DeleteDbFiles.execute(dir, DB_FILE, true);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("H2 server has been shut down!");
            }
        }
        server = null;
    }
}
