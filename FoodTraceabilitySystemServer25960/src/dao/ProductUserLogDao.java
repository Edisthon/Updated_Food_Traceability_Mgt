package dao;

import model.ProductUserLog;
import model.Product;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query; // Ensure this import
import java.util.List;

public class ProductUserLogDao {

    public String saveProductUserLog(ProductUserLog log) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(log);
            transaction.commit();
            return "ProductUserLog saved successfully.";
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return "Error saving ProductUserLog: " + e.getMessage();
        } finally {
            if (session != null) session.close();
        }
    }

    public List<ProductUserLog> getAllProductUserLogs() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("FROM ProductUserLog");
            return (List<ProductUserLog>) query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    public List<ProductUserLog> getProductUserLogsByProductId(int productId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            // Assuming 'product' is the name of the field in ProductUserLog that maps to Product,
            // and 'productId' is the ID field in the Product entity.
            Query query = session.createQuery("FROM ProductUserLog pul WHERE pul.product.productId = :productId ORDER BY pul.timestamp DESC");
            query.setParameter("productId", productId);
            return (List<ProductUserLog>) query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    public List<ProductUserLog> getProductUserLogsByUserId(int userId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            // Assuming 'user' is the name of the field in ProductUserLog that maps to User,
            // and 'userId' is the ID field in the User entity.
            Query query = session.createQuery("FROM ProductUserLog pul WHERE pul.user.userId = :userId ORDER BY pul.timestamp DESC");
            query.setParameter("userId", userId);
            return (List<ProductUserLog>) query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
}
