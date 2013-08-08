package org.openkoala.koala.mybatis;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.ObjectUtils.isEmpty;
import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.tokenizeToStringArray;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.sql.DataSource;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.DefaultDatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.jboss.vfs.VirtualFile;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import com.dayatang.domain.Entity;

public class KoalaSqlSessionFactoryBean implements
		FactoryBean<SqlSessionFactory>, InitializingBean,
		ApplicationListener<ApplicationEvent> {

	private final Log logger = LogFactory.getLog(getClass());

	private Resource configLocation;

	private Resource[] mapperLocations;

	private DataSource dataSource;

	private TransactionFactory transactionFactory;

	private Properties configurationProperties;

	private SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

	private SqlSessionFactory sqlSessionFactory;

	private String environment = SqlSessionFactoryBean.class.getSimpleName();

	private boolean failFast;

	private Interceptor[] plugins;

	private TypeHandler<?>[] typeHandlers;

	private String typeHandlersPackage;

	private Class<?>[] typeAliases;

	private String typeAliasesPackage;

	private DatabaseIdProvider databaseIdProvider = new DefaultDatabaseIdProvider();
	
	private String basePackage;
	
	private String baseScan;

	/**
	 * Sets the DatabaseIdProvider.
	 * 
	 * @since 1.1.0
	 * @return
	 */
	public DatabaseIdProvider getDatabaseIdProvider() {
		return databaseIdProvider;
	}

	/**
	 * Gets the DatabaseIdProvider
	 * 
	 * @since 1.1.0
	 * @param databaseIdProvider
	 */
	public void setDatabaseIdProvider(DatabaseIdProvider databaseIdProvider) {
		this.databaseIdProvider = databaseIdProvider;
	}

	/**
	 * Mybatis plugin list.
	 * 
	 * @since 1.0.1
	 * 
	 * @param plugins
	 *            list of plugins
	 * 
	 */
	public void setPlugins(Interceptor[] plugins) {
		this.plugins = plugins;
	}

	/**
	 * Packages to search for type aliases.
	 * 
	 * @since 1.0.1
	 * 
	 * @param typeAliasesPackage
	 *            package to scan for domain objects
	 * 
	 */
	public void setTypeAliasesPackage(String typeAliasesPackage) {
		this.typeAliasesPackage = typeAliasesPackage;
	}

	/**
	 * Packages to search for type handlers.
	 * 
	 * @since 1.0.1
	 * 
	 * @param typeHandlersPackage
	 *            package to scan for type handlers
	 * 
	 */
	public void setTypeHandlersPackage(String typeHandlersPackage) {
		this.typeHandlersPackage = typeHandlersPackage;
	}

	/**
	 * Set type handlers. They must be annotated with {@code MappedTypes} and
	 * optionally with {@code MappedJdbcTypes}
	 * 
	 * @since 1.0.1
	 * 
	 * @param typeHandlers
	 *            Type handler list
	 */
	public void setTypeHandlers(TypeHandler<?>[] typeHandlers) {
		this.typeHandlers = typeHandlers;
	}

	/**
	 * List of type aliases to register. They can be annotated with
	 * {@code Alias}
	 * 
	 * @since 1.0.1
	 * 
	 * @param typeAliases
	 *            Type aliases list
	 */
	public void setTypeAliases(Class<?>[] typeAliases) {
		this.typeAliases = typeAliases;
	}

	/**
	 * If true, a final check is done on Configuration to assure that all mapped
	 * statements are fully loaded and there is no one still pending to resolve
	 * includes. Defaults to false.
	 * 
	 * @since 1.0.1
	 * 
	 * @param failFast
	 *            enable failFast
	 */
	public void setFailFast(boolean failFast) {
		this.failFast = failFast;
	}

	/**
	 * Set the location of the MyBatis {@code SqlSessionFactory} config file. A
	 * typical value is "WEB-INF/mybatis-configuration.xml".
	 */
	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	/**
	 * Set locations of MyBatis mapper files that are going to be merged into
	 * the {@code SqlSessionFactory} configuration at runtime.
	 * 
	 * This is an alternative to specifying "&lt;sqlmapper&gt;" entries in an
	 * MyBatis config file. This property being based on Spring's resource
	 * abstraction also allows for specifying resource patterns here: e.g.
	 * "classpath*:sqlmap/*-mapper.xml".
	 */
	public void setMapperLocations(Resource[] mapperLocations) {
		this.mapperLocations = mapperLocations;
	}

	/**
	 * Set optional properties to be passed into the SqlSession configuration,
	 * as alternative to a {@code &lt;properties&gt;} tag in the configuration
	 * xml file. This will be used to resolve placeholders in the config file.
	 */
	public void setConfigurationProperties(
			Properties sqlSessionFactoryProperties) {
		this.configurationProperties = sqlSessionFactoryProperties;
	}

	/**
	 * Set the JDBC {@code DataSource} that this instance should manage
	 * transactions for. The {@code DataSource} should match the one used by the
	 * {@code SqlSessionFactory}: for example, you could specify the same JNDI
	 * DataSource for both.
	 * 
	 * A transactional JDBC {@code Connection} for this {@code DataSource} will
	 * be provided to application code accessing this {@code DataSource}
	 * directly via {@code DataSourceUtils} or
	 * {@code DataSourceTransactionManager}.
	 * 
	 * The {@code DataSource} specified here should be the target
	 * {@code DataSource} to manage transactions for, not a
	 * {@code TransactionAwareDataSourceProxy}. Only data access code may work
	 * with {@code TransactionAwareDataSourceProxy}, while the transaction
	 * manager needs to work on the underlying target {@code DataSource}. If
	 * there's nevertheless a {@code TransactionAwareDataSourceProxy} passed in,
	 * it will be unwrapped to extract its target {@code DataSource}.
	 * 
	 */
	public void setDataSource(DataSource dataSource) {
		if (dataSource instanceof TransactionAwareDataSourceProxy) {
			// If we got a TransactionAwareDataSourceProxy, we need to perform
			// transactions for its underlying target DataSource, else data
			// access code won't see properly exposed transactions (i.e.
			// transactions for the target DataSource).
			this.dataSource = ((TransactionAwareDataSourceProxy) dataSource)
					.getTargetDataSource();
		} else {
			this.dataSource = dataSource;
		}
	}

	/**
	 * Sets the {@code SqlSessionFactoryBuilder} to use when creating the
	 * {@code SqlSessionFactory}.
	 * 
	 * This is mainly meant for testing so that mock SqlSessionFactory classes
	 * can be injected. By default, {@code SqlSessionFactoryBuilder} creates
	 * {@code DefaultSqlSessionFactory} instances.
	 * 
	 */
	public void setSqlSessionFactoryBuilder(
			SqlSessionFactoryBuilder sqlSessionFactoryBuilder) {
		this.sqlSessionFactoryBuilder = sqlSessionFactoryBuilder;
	}

	/**
	 * Set the MyBatis TransactionFactory to use. Default is
	 * {@code SpringManagedTransactionFactory}
	 * 
	 * The default {@code SpringManagedTransactionFactory} should be appropriate
	 * for all cases: be it Spring transaction management, EJB CMT or plain JTA.
	 * If there is no active transaction, SqlSession operations will execute SQL
	 * statements non-transactionally.
	 * 
	 * <b>It is strongly recommended to use the default
	 * {@code TransactionFactory}.</b> If not used, any attempt at getting an
	 * SqlSession through Spring's MyBatis framework will throw an exception if
	 * a transaction is active.
	 * 
	 * @see SpringManagedTransactionFactory
	 * @param transactionFactory
	 *            the MyBatis TransactionFactory
	 */
	public void setTransactionFactory(TransactionFactory transactionFactory) {
		this.transactionFactory = transactionFactory;
	}

	/**
	 * <b>NOTE:</b> This class <em>overrides</em> any {@code Environment} you
	 * have set in the MyBatis config file. This is used only as a placeholder
	 * name. The default value is
	 * {@code SqlSessionFactoryBean.class.getSimpleName()}.
	 * 
	 * @param environment
	 *            the environment name
	 */
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	/**
	 * {@inheritDoc}
	 */
	public void afterPropertiesSet() throws Exception {
		notNull(dataSource, "Property 'dataSource' is required");
		notNull(sqlSessionFactoryBuilder,
				"Property 'sqlSessionFactoryBuilder' is required");

		this.sqlSessionFactory = buildSqlSessionFactory();
	}

	/**
	 * Build a {@code SqlSessionFactory} instance.
	 * 
	 * The default implementation uses the standard MyBatis
	 * {@code XMLConfigBuilder} API to build a {@code SqlSessionFactory}
	 * instance based on an Reader.
	 * 
	 * @return SqlSessionFactory
	 * @throws IOException
	 *             if loading the config file failed
	 */
	protected SqlSessionFactory buildSqlSessionFactory() throws IOException {

		Configuration configuration;

		XMLConfigBuilder xmlConfigBuilder = null;
		if (this.configLocation != null) {
			xmlConfigBuilder = new XMLConfigBuilder(
					this.configLocation.getInputStream(), null,
					this.configurationProperties);
			configuration = xmlConfigBuilder.getConfiguration();
		} else {
			if (this.logger.isDebugEnabled()) {
				this.logger
						.debug("Property 'configLocation' not specified, using default MyBatis Configuration");
			}
			configuration = new Configuration();
			configuration.setVariables(this.configurationProperties);
		}

		if (hasLength(this.typeAliasesPackage)) {
			String[] typeAliasPackageArray = tokenizeToStringArray(
					this.typeAliasesPackage,
					ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
			for (String packageToScan : typeAliasPackageArray) {
				configuration.getTypeAliasRegistry().registerAliases(
						packageToScan);
				if (this.logger.isDebugEnabled()) {
					this.logger.debug("Scanned package: '" + packageToScan
							+ "' for aliases");
				}
			}
		}

		if (!isEmpty(this.typeAliases)) {
			for (Class<?> typeAlias : this.typeAliases) {
				configuration.getTypeAliasRegistry().registerAlias(typeAlias);
				if (this.logger.isDebugEnabled()) {
					this.logger.debug("Registered type alias: '" + typeAlias
							+ "'");
				}
			}
		}
		
		//lingen 自动扫描Entity实体，映射alias
		autoTypeAliases(configuration);

		if (!isEmpty(this.plugins)) {
			for (Interceptor plugin : this.plugins) {
				configuration.addInterceptor(plugin);
				if (this.logger.isDebugEnabled()) {
					this.logger.debug("Registered plugin: '" + plugin + "'");
				}
			}
		}

		if (hasLength(this.typeHandlersPackage)) {
			String[] typeHandlersPackageArray = tokenizeToStringArray(
					this.typeHandlersPackage,
					ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
			for (String packageToScan : typeHandlersPackageArray) {
				configuration.getTypeHandlerRegistry().register(packageToScan);
				if (this.logger.isDebugEnabled()) {
					this.logger.debug("Scanned package: '" + packageToScan
							+ "' for type handlers");
				}
			}
		}

		if (!isEmpty(this.typeHandlers)) {
			for (TypeHandler<?> typeHandler : this.typeHandlers) {
				configuration.getTypeHandlerRegistry().register(typeHandler);
				if (this.logger.isDebugEnabled()) {
					this.logger.debug("Registered type handler: '"
							+ typeHandler + "'");
				}
			}
		}

		if (xmlConfigBuilder != null) {
			try {
				xmlConfigBuilder.parse();
				autoParse(configuration);
				if (this.logger.isDebugEnabled()) {
					this.logger.debug("Parsed configuration file: '"
							+ this.configLocation + "'");
				}
			} catch (Exception ex) {
				throw new NestedIOException("Failed to parse config resource: "
						+ this.configLocation, ex);
			} finally {
				ErrorContext.instance().reset();
			}
		}

		if (this.transactionFactory == null) {
			this.transactionFactory = new SpringManagedTransactionFactory();
		}

		Environment environment = new Environment(this.environment,
				this.transactionFactory, this.dataSource);
		configuration.setEnvironment(environment);

		if (this.databaseIdProvider != null) {
			try {
				configuration.setDatabaseId(this.databaseIdProvider
						.getDatabaseId(this.dataSource));
			} catch (SQLException e) {
				throw new NestedIOException("Failed getting a databaseId", e);
			}
		}

		if (!isEmpty(this.mapperLocations)) {
			for (Resource mapperLocation : this.mapperLocations) {
				if (mapperLocation == null) {
					continue;
				}

				try {
					XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(
							mapperLocation.getInputStream(), configuration,
							mapperLocation.toString(),
							configuration.getSqlFragments());
					xmlMapperBuilder.parse();
				} catch (Exception e) {
					throw new NestedIOException(
							"Failed to parse mapping resource: '"
									+ mapperLocation + "'", e);
				} finally {
					ErrorContext.instance().reset();
				}

				if (this.logger.isDebugEnabled()) {
					this.logger.debug("Parsed mapper file: '" + mapperLocation
							+ "'");
				}
			}
		} else {
			if (this.logger.isDebugEnabled()) {
				this.logger
						.debug("Property 'mapperLocations' was not specified or no matching resources found");
			}
		}

		return this.sqlSessionFactoryBuilder.build(configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	public SqlSessionFactory getObject() throws Exception {
		if (this.sqlSessionFactory == null) {
			afterPropertiesSet();
		}

		return this.sqlSessionFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends SqlSessionFactory> getObjectType() {
		return this.sqlSessionFactory == null ? SqlSessionFactory.class
				: this.sqlSessionFactory.getClass();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSingleton() {
		return true;
	}
	
	

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public void setBaseScan(String baseScan) {
		this.baseScan = baseScan;
	}

	/**
	 * {@inheritDoc}
	 */
	public void onApplicationEvent(ApplicationEvent event) {
		if (failFast && event instanceof ContextRefreshedEvent) {
			// fail-fast -> check all statements are completed
			this.sqlSessionFactory.getConfiguration().getMappedStatementNames();
		}
	}
	
	private void autoTypeAliases(Configuration configuration) throws IOException {
		String basePackage = this.basePackage.replaceAll("\\.", "/") +"/";
		
		List<String> lists = new ArrayList<String>();
		Enumeration<?> urls = Thread.currentThread().getContextClassLoader().getResources(basePackage);
		
		while (urls.hasMoreElements()) {
			URL u = (URL)urls.nextElement();
			String path = u.getFile();
			System.out.println("PATH:"+path);
			String protocol = u.getProtocol();
			if("file".equals(protocol) || "jar".equals(protocol)){
				if(path.endsWith("!/"+basePackage)){
					path = path.substring(path.indexOf("file:")+5,path.lastIndexOf("!/"));
					File file = new File(path);
					JarFile jarFile = new JarFile(file);  
				    for (Enumeration<?> entries = jarFile.entries(); entries
						.hasMoreElements();) {
					JarEntry entry = (JarEntry) entries.nextElement();
					String entryName = entry.getName();
					if (entryName.startsWith(basePackage)
							&& entryName.endsWith(".class")) {
						entryName = entryName.substring(entryName.indexOf(basePackage)).replaceAll("\\\\", "/");
						lists.add(entryName);
					}
				}
				}
				else{
					File file = new File(path);
					if(file.exists()==false)continue;
					List<String> files = getClassFiles(basePackage,file);
					lists.addAll(files);
				}
			}
			else if("vfs".equals(protocol)){
				 VirtualFile vf = (VirtualFile)u.getContent();
				 String filePath = vf.getPhysicalFile().getPath();
				 String jarPath = filePath.substring(0, filePath.indexOf("contents"));
				 File[] jarFiles = new File(jarPath).listFiles();
				 for(File jar:jarFiles){
					 if(jar.getName().endsWith(".jar")){
						 JarFile jarFile = new JarFile(jar);
						 for (Enumeration<?> entries = jarFile.entries(); entries
									.hasMoreElements();) {
								JarEntry entry = (JarEntry) entries.nextElement();
								String entryName = entry.getName();
								if (entryName.startsWith(basePackage)
										&& entryName.endsWith(".class")) {
									entryName = entryName.substring(entryName.indexOf(basePackage)).replaceAll("\\\\", "/");
									lists.add(entryName);
								}
							}
					 }
				 }
			}
			
		}
		
		for(String className:lists){
			try {
				className = className.substring(0,className.indexOf(".class")).replaceAll("/", "\\.");
				Class c = Class.forName(className);
				if(Entity.class.isAssignableFrom(c)){
					configuration.getTypeAliasRegistry().registerAlias(c);
				}
				if(c.getSimpleName().toUpperCase().endsWith("DTO")){
					configuration.getTypeAliasRegistry().registerAlias(c);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public List<String> getClassFiles(String basePath,File file){
		List<String> files = new ArrayList<String>();
		File[] childs = file.listFiles();
		for(File child:childs){
			if(child.isFile() && child.getName().endsWith(".class")){
				String path = child.getAbsolutePath().replaceAll("\\\\", "/");
				files.add(path.substring(path.indexOf(basePath)));
			}else{
				files.addAll(getClassFiles(basePath,child));
			}
		}
		return files;
	}
	
	private void autoParse(Configuration configuration) throws IOException{
		String scanMappingResourceDir = this.baseScan;
		List<String> lists = new ArrayList<String>();
		Enumeration<?> urls = Thread.currentThread().getContextClassLoader().getResources(scanMappingResourceDir);
		while (urls.hasMoreElements()) {
			URL u = (URL)urls.nextElement();
			String path = u.getFile();
			System.out.println("PATH:"+path);
			String protocol = u.getProtocol();
			if("file".equals(protocol) || "jar".equals(protocol)){
				if(path.endsWith("!/"+scanMappingResourceDir)){
					path = path.substring(path.indexOf("file:")+5,path.lastIndexOf("!/"));
					File file = new File(path);
					JarFile jarFile = new JarFile(file);  
				    for (Enumeration<?> entries = jarFile.entries(); entries
						.hasMoreElements();) {
					JarEntry entry = (JarEntry) entries.nextElement();
					String entryName = entry.getName();
					if (entryName.startsWith(scanMappingResourceDir)
							&& entryName.endsWith("-mybatis.xml")) {
						lists.add(entryName);
					}
				}
				}
				else{
					File file = new File(path);
					if(file.exists()==false)continue;
					String[] files = file.list(new FilenameFilter() {
						public boolean accept(File dir, String name) {
							return name.endsWith("-mybatis.xml");
						}
					});
					for (String fileName : files) {
						lists.add(scanMappingResourceDir+File.separator+fileName);
					}
				}
			}else if("vfs".equals(protocol)){
				 VirtualFile vf = (VirtualFile)u.getContent();
				 String filePath = vf.getPhysicalFile().getPath();
				 String jarPath = filePath.substring(0, filePath.indexOf("contents"));
				 File[] jarFiles = new File(jarPath).listFiles();
				 for(File jar:jarFiles){
					 if(jar.getName().endsWith(".jar")){
						 JarFile jarFile = new JarFile(jar);
						    for (Enumeration<?> entries = jarFile.entries(); entries
								.hasMoreElements();) {
							JarEntry entry = (JarEntry) entries.nextElement();
							String entryName = entry.getName();
							if (entryName.startsWith(scanMappingResourceDir)
									&& entryName.endsWith("-mybatis.xml")) {
								lists.add(entryName);
							}
						}
					 }
				 }
			}
			
		}
		for(String resource:lists){
			 InputStream inputStream = Resources.getResourceAsStream(resource);
	          XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
	          mapperParser.parse();
		}

	}

}
