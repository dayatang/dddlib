package org.dayatang.h2;

import java.io.File;
import java.sql.SQLException;

import org.dayatang.utils.Slf4JLogger;
import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;

public class H2Server {
	
	private static final Slf4JLogger LOGGER = Slf4JLogger.getLogger(H2Server.class);
	private static final String DEFAULT_DB_FILE = "h2-test-db";
	private static H2Server instance;
	
	public static final synchronized H2Server getSingleton() {
		if (instance == null) {
			instance = new H2Server(DEFAULT_DB_FILE);
		}
		return instance;
	}
	
	private Server server;

	private String dir;

	private String dbFile;

	public H2Server(String dir, String dbFile) {
		this.dir = dir;
		this.dbFile = dbFile;
	}

	public H2Server(String dbFile) {
		this(".", dbFile);
	}

	public synchronized void start() {
		if (server != null && server.isRunning(true)) {
			return;
		}
		try {
			DeleteDbFiles.execute(dir, dbFile, true);
			server = Server.createTcpServer(new String[0]);
			server.start();
			LOGGER.info("H2 server started! data file is {}", new File(dir, dbFile).getAbsolutePath());
		} catch (SQLException e) {
			LOGGER.error("Cannot start h2 server database", e);
			throw new RuntimeException("Cannot start h2 server database", e);
		}
	}

	public synchronized void shutdown() {
		if (server == null) {
			return;
		}
		if (server.isRunning(true)) {
			server.stop();
			server.shutdown();
			DeleteDbFiles.execute(dir, dbFile, true);
			LOGGER.info("H2 server shutdowned!");
		}
		server = null;
	}
}
