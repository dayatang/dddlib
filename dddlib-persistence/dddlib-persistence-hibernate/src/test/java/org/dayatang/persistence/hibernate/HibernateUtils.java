package org.dayatang.persistence.hibernate;

import org.dayatang.persistence.test.domain.Dictionary;
import org.dayatang.persistence.test.domain.DictionaryCategory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class HibernateUtils {
    private static SessionFactory sessionFactory = buildSessionFactory();

    @SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory() {
        try {

            Configuration configuration = new Configuration()
                    .addAnnotatedClass(DictionaryCategory.class)
                    .addAnnotatedClass(Dictionary.class)
                    .configure();
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties());
            new SchemaExport(configuration).create(false, true);
            return configuration.buildSessionFactory(builder.build());
        }
        catch (Exception ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
       
    }

    public static synchronized SessionFactory getSessionFactory() {
    	if (sessionFactory == null || sessionFactory.isClosed()) {
    		sessionFactory = buildSessionFactory();
    	}
        return sessionFactory;
    }

    public static void close() {
    	sessionFactory.close();
    }


}
