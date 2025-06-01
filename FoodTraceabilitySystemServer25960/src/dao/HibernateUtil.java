/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

// import org.hibernate.cfg.AnnotationConfiguration; // Old import
import org.hibernate.cfg.Configuration; // New import
import org.hibernate.SessionFactory;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author HP
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    
    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file using modern Configuration class.
            sessionFactory = new Configuration().configure().buildSessionFactory();

            // Log the state of sessionFactory from HibernateUtil's perspective
            if (sessionFactory == null) {
                System.err.println("HibernateUtil: SessionFactory is NULL immediately after buildSessionFactory!");
            } else {
                System.out.println("HibernateUtil: SessionFactory created successfully. Is open: " + !sessionFactory.isClosed());
            }

        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            ex.printStackTrace(); // Print stack trace for detailed diagnostics
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
