
package dao;

import java.util.List;
import model.ProductStatusLog;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query; // Ensure this import is present


public class ProductStatusLogDao {
    
     public String registerProductStatusLog(ProductStatusLog productStatusLog){
        try{
            //1. Create a Session
            Session ss= HibernateUtil.getSessionFactory().openSession();
            //2.Create a transaction
            Transaction tr= ss.beginTransaction();
            ss.save(productStatusLog);
            tr.commit();
            ss.close();
            return "Data saved succesfully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
     public String updateProductStatusLog(ProductStatusLog productStatusLog){
        try{
            //1. Create a Session
            Session ss= HibernateUtil.getSessionFactory().openSession();
            //2.Create a transaction
            Transaction tr= ss.beginTransaction();
            ss.save(productStatusLog);
            tr.commit();
            ss.close();
            return "Data updated succesfully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
     public String deleteProductStatusLog(ProductStatusLog productStatusLog){
        try{
            //1. Create a Session
            Session ss= HibernateUtil.getSessionFactory().openSession();
            //2.Create a transaction
            Transaction tr= ss.beginTransaction();
            ss.save(productStatusLog);
            tr.commit();
            ss.close();
            return "Data deleted succesfully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
     
      public List<ProductStatusLog> retreiveAll(){
        Session ss= HibernateUtil.getSessionFactory().openSession();
        List<ProductStatusLog> productStatusLogList=ss.createQuery("select prod from"
                + "ProductStatusLog prod").list();
        ss.close();
        return productStatusLogList;
    }
    public ProductStatusLog retrieveById(ProductStatusLog productStatusLog){
        Session ss= HibernateUtil.getSessionFactory().openSession();
        ProductStatusLog productStatusLogs=(ProductStatusLog)ss.get(ProductStatusLog.class,productStatusLog.getLogId());
        ss.close();
        return productStatusLogs;
    }

    public List<ProductStatusLog> getLogsByProductId(int productId) {
        Session ss = HibernateUtil.getSessionFactory().openSession();
        List<ProductStatusLog> productStatusLogList = null;
        try {
            // Assuming ProductStatusLog has a field named 'productId' that maps to the database column.
            // If the field is named differently (e.g., 'product' and it's an association,
            // the HQL might need to be 'FROM ProductStatusLog psl WHERE psl.product.productId = :productId')
            // For this task, we assume a direct 'productId' field as implied by the method signature and typical usage.
            org.hibernate.Query query = ss.createQuery("FROM ProductStatusLog WHERE productId = :productId");
            query.setParameter("productId", productId);
            productStatusLogList = (List<ProductStatusLog>) query.list(); // Cast the result
        } catch (Exception ex) {
            ex.printStackTrace(); // Consider more robust logging
        } finally {
            ss.close();
        }
        return productStatusLogList;
    }
}
