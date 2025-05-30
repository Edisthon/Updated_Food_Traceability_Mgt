
package dao;

import java.util.List;
import model.ProductStatusLog;
import org.hibernate.Session;
import org.hibernate.Transaction;


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
}
