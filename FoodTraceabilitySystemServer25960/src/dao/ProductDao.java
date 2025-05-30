
package dao;

import java.util.List;
import model.Product;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class ProductDao {
    
     public String registerProduct(Product product){
        try{
            //1. Create a Session
            Session ss= HibernateUtil.getSessionFactory().openSession();
            //2.Create a transaction
            Transaction tr= ss.beginTransaction();
            ss.save(product);
            tr.commit();
            ss.close();
            return "Data saved succesfully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
     public String updateProduct(Product product){
        try{
            //1. Create a Session
            Session ss= HibernateUtil.getSessionFactory().openSession();
            //2.Create a transaction
            Transaction tr= ss.beginTransaction();
            ss.save(product);
            tr.commit();
            ss.close();
            return "Data updated succesfully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
     public String deleteProduct(Product product){
        try{
            //1. Create a Session
            Session ss= HibernateUtil.getSessionFactory().openSession();
            //2.Create a transaction
            Transaction tr= ss.beginTransaction();
            ss.save(product);
            tr.commit();
            ss.close();
            return "Data deleted succesfully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
     
      public List<Product> retreiveAll(){
        Session ss= HibernateUtil.getSessionFactory().openSession();
        List<Product> productList=ss.createQuery("select prod from"
                + "Product prod").list();
        ss.close();
        return productList;
    }
    public Product retrieveById(Product product){
        Session ss= HibernateUtil.getSessionFactory().openSession();
        Product products=(Product)ss.get(Product.class,product.getProductId());
        ss.close();
        return products;
    }
}
