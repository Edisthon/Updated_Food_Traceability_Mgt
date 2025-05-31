package dao;

import model.UserOtp;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import java.util.Date;

public class UserOtpDao {

    public String saveOrUpdateUserOtp(UserOtp userOtp) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            // Check if an OTP for this email already exists
            // Using fully qualified name for org.hibernate.Query to avoid ambiguity if javax.persistence.Query is also imported elsewhere
            org.hibernate.Query checkQuery = session.createQuery("FROM UserOtp WHERE email = :email");
            checkQuery.setParameter("email", userOtp.getEmail());
            UserOtp existingOtp = (UserOtp) checkQuery.uniqueResult();

            if (existingOtp != null) {
                existingOtp.setOtpValue(userOtp.getOtpValue());
                existingOtp.setExpiryTimestamp(userOtp.getExpiryTimestamp());
                session.update(existingOtp);
            } else {
                session.save(userOtp);
            }
            transaction.commit();
            return "UserOtp saved/updated successfully.";
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace(); // Consider using a logger
            return "Error saving/updating UserOtp: " + e.getMessage();
        } finally {
            if (session != null) session.close();
        }
    }

    public UserOtp getUserOtpByEmail(String email) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            org.hibernate.Query query = session.createQuery("FROM UserOtp uo WHERE uo.email = :email");
            query.setParameter("email", email);
            query.setMaxResults(1); // Ensure only one result, good practice though email is unique
            return (UserOtp) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace(); // Consider using a logger
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    public String deleteUserOtp(UserOtp userOtp) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            // Ensure the object is in the session before deleting if it's detached
            // However, if it's fetched and then deleted in same session, or new and then deleted, it's fine.
            // For safety, merge if unsure: UserOtp managedOtp = (UserOtp) session.merge(userOtp); session.delete(managedOtp);
            session.delete(userOtp);
            transaction.commit();
            return "UserOtp deleted successfully.";
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace(); // Consider using a logger
            return "Error deleting UserOtp: " + e.getMessage();
        } finally {
            if (session != null) session.close();
        }
    }

    public String deleteExpiredOtps() {
        Session session = null;
        Transaction transaction = null;
        int deletedCount = 0;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            org.hibernate.Query query = session.createQuery("DELETE FROM UserOtp uo WHERE uo.expiryTimestamp < :now");
            query.setParameter("now", new Date());
            deletedCount = query.executeUpdate();
            transaction.commit();
            return deletedCount + " expired OTPs deleted successfully.";
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace(); // Consider using a logger
            return "Error deleting expired OTPs: " + e.getMessage();
        } finally {
            if (session != null) session.close();
        }
    }
}
