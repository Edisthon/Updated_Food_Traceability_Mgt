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
import java.util.List;
import model.Product;
import model.User;
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
        return dao.retreiveAll();
    }

    @Override
    public Product retrieveById(Product product) throws RemoteException {
        return dao.retrieveById(product);
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
