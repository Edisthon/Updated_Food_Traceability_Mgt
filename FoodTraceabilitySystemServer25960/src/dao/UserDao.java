
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.DBConnection;
import org.hibernate.Query;

public class UserDao {
    
    public String registerUser(User user) {
        Session ss = null;
        Transaction tr = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();
            ss.save(user); // Correct for new entity
            tr.commit();
            return "Data saved successfully";
        } catch (Exception ex) {
            if (tr != null) {
                tr.rollback();
            }
            ex.printStackTrace();
            return "Error saving user: " + ex.getMessage();
        } finally {
            if (ss != null) {
                ss.close();
            }
        }
    }

    public String updateUser(User user) {
        Session ss = null;
        Transaction tr = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();
            ss.merge(user); // Correct for updating detached or existing entity
            tr.commit();
            return "Data updated successfully";
        } catch (Exception ex) {
            if (tr != null) {
                tr.rollback();
            }
            ex.printStackTrace();
            return "Error updating user: " + ex.getMessage();
        } finally {
            if (ss != null) {
                ss.close();
            }
        }
    }

    public String deleteUser(User user) {
        Session ss = null;
        Transaction tr = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();
            ss.delete(user); // Correct for deleting an entity
            tr.commit();
            return "Data deleted successfully";
        } catch (Exception ex) {
            if (tr != null) {
                tr.rollback();
            }
            ex.printStackTrace();
            return "Error deleting user: " + ex.getMessage();
        } finally {
            if (ss != null) {
                ss.close();
            }
        }
    }
     
      public List<User> retreiveAll(){
        Session ss= HibernateUtil.getSessionFactory().openSession();
        List<User> userList=ss.createQuery("select us from"
                + "User us").list();
        ss.close();
        return userList;
    }
    public User retrieveById(User user){
        Session ss= HibernateUtil.getSessionFactory().openSession();
        User users=(User)ss.get(User.class,user.getUserId());
        ss.close();
        return users;
    }
    
    public User findByUsername(String username) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = null;
    User user = null;
    try {
        tx = session.beginTransaction();
        Query query = session.createQuery("FROM User WHERE username = :uname");
        query.setParameter("uname", username);
        user = (User) query.uniqueResult();
        tx.commit();
    } catch (Exception e) {
        if (tx != null) tx.rollback();
        e.printStackTrace();
    } finally {
        session.close();
    }
    return user;
}

    public User findByEmail(String email) {
        Session session = null;
        User user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            // Transaction is not strictly needed for a read-only query like this.
            Query query = session.createQuery("FROM User WHERE email = :email");
            query.setParameter("email", email);
            query.setMaxResults(1); // Ensure only one result for unique email
            user = (User) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace(); // Consider using a logger
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return user;
    }

   public void saveOtp(String username, String otp) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = null;

    try {
        tx = session.beginTransaction();

        // Find user by username
      Query query = session.createQuery("FROM User WHERE username = :username");
      query.setParameter("username", username);
      User user = (User) query.uniqueResult();        
        if (user != null) {
            user.setOtp(otp);
            user.setOtpGeneratedTime(new Date()); // store the timestamp
            session.update(user); // or session.saveOrUpdate(user);
        } else {
            throw new RuntimeException("User not found with username: " + username);
        }

        tx.commit();
    } catch (Exception e) {
        if (tx != null) tx.rollback();
        e.printStackTrace();
        throw e;
    } finally {
        session.close();
    }
}

}
