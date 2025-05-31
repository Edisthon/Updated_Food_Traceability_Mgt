/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.HibernateUtil;
import dao.ProductDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet; // Added
import java.util.List;
import model.Product;
import model.User;
import org.hibernate.LazyInitializationException; // Added
import org.hibernate.Session;
import service.ProductInterface;


public class ProductImpl extends UnicastRemoteObject implements ProductInterface{

    
    public  ProductImpl() throws RemoteException{
        super();
    }
    ProductDao dao= new ProductDao();

    @Override
    public String registerProduct(Product products) throws RemoteException {
      return  dao.registerProduct(products);
    }

    @Override
    public String updateProduct(Product products) throws RemoteException {
        return dao.updateProduct(products);
    }

    @Override
    public String deleteProduct(Product products) throws RemoteException {
            return  dao.deleteProduct(products);
        }

    @Override
    public List<Product> retreiveAll() throws RemoteException {
        List<Product> productList = dao.retreiveAll();
        if (productList != null) {
            for (Product product : productList) {
                if (product.getProductStatus() != null) {
                    try {
                        product.getProductStatus().size(); // Initialize
                        product.setProductStatus(new java.util.HashSet<>(product.getProductStatus()));
                    } catch (org.hibernate.LazyInitializationException lie) {
                        System.err.println("LazyInitializationException in ProductImpl.retreiveAll for product.productStatus (ID: " + product.getProductId() + "): " + lie.getMessage());
                        product.setProductStatus(new java.util.HashSet<>());
                    }
                }
            }
        }
        return productList;
    }

    @Override
    public Product retrieveById(Product productInput) throws RemoteException {
        Product retrievedProduct = dao.retrieveById(productInput);
        if (retrievedProduct != null) {
            if (retrievedProduct.getProductStatus() != null) {
                try {
                    retrievedProduct.getProductStatus().size(); // Initialize
                    // Ensure Product model has setProductStatus(Set<ProductStatusLog>)
                    retrievedProduct.setProductStatus(new java.util.HashSet<>(retrievedProduct.getProductStatus()));
                } catch (org.hibernate.LazyInitializationException lie) {
                    System.err.println("LazyInitializationException in ProductImpl.retrieveById for productStatus: " + lie.getMessage());
                    retrievedProduct.setProductStatus(new java.util.HashSet<>());
                }
            }
        }
        return retrievedProduct;
    }

    @Override
    public User getUserById(int userId) throws RemoteException {
         Session session = HibernateUtil.getSessionFactory().openSession();
    try {
        return (User) session.get(User.class, userId); // Cast ensures clarity
    } finally {
        session.close();
    }
    }
    
    
    
}
