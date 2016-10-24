package org.dayatang.persistence.hibernate;

import org.dayatang.persistence.test.domain.Dictionary;
import org.dayatang.persistence.test.domain.DictionaryCategory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtils {
    private static SessionFactory sessionFactory = buildSessionFactory();

    @SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            return new MetadataSources( registry )
                    .addAnnotatedClass(DictionaryCategory.class)
                    .addAnnotatedClass(Dictionary.class)
                    .buildMetadata()
                    .buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
            throw new RuntimeException(e);
        }
    }

    public static synchronized SessionFactory getSessionFactory() {
    	if (sessionFactory == null || sessionFactory.isClosed()) {
    		sessionFactory = buildSessionFactory();
    	}
        return sessionFactory;
    }

    public static void close() {
        javax.transaction.SystemException e;
    	sessionFactory.close();
    }


}
