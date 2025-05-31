/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.ProductStatusLogDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet; // Added
import java.util.List;
import model.Product; // Added
import model.ProductStatusLog;
import org.hibernate.LazyInitializationException; // Added
import service.ProductStatusLogInterface;


public class ProductStatusLogImpl extends UnicastRemoteObject implements ProductStatusLogInterface{

    
    public  ProductStatusLogImpl() throws RemoteException{
        super();
    }
    ProductStatusLogDao dao= new ProductStatusLogDao();

    @Override
    public String registerProductStatusLog(ProductStatusLog productStatusLogs) throws RemoteException {
      return  dao.registerProductStatusLog(productStatusLogs);
    }

    @Override
    public String updateProductStatusLog(ProductStatusLog productStatusLogs) throws RemoteException {
        return dao.updateProductStatusLog(productStatusLogs);
    }

    @Override
    public String deleteProductStatusLog(ProductStatusLog productStatusLogs) throws RemoteException {
            return  dao.deleteProductStatusLog(productStatusLogs);
        }

    @Override
    public List<ProductStatusLog> retreiveAll() throws RemoteException {
        List<ProductStatusLog> logList = dao.retreiveAll();
        if (logList != null) {
            for (ProductStatusLog log : logList) {
                if (log == null) continue; // Safety check
                Product associatedProduct = log.getProducts();
                if (associatedProduct != null && associatedProduct.getProductStatus() != null) {
                    try {
                        associatedProduct.getProductStatus().size(); // Initialize
                        associatedProduct.setProductStatus(new java.util.HashSet<>(associatedProduct.getProductStatus()));
                    } catch (org.hibernate.LazyInitializationException lie) {
                        System.err.println("LazyInitializationException in ProductStatusLogImpl.retreiveAll for P:" + associatedProduct.getProductId() + ".productStatus: " + lie.getMessage());
                        associatedProduct.setProductStatus(new java.util.HashSet<>());
                    }
                }
            }
        }
        return logList;
    }

    @Override
    public ProductStatusLog retrieveById(ProductStatusLog productStatusLogInput) throws RemoteException {
        ProductStatusLog retrievedLog = dao.retrieveById(productStatusLogInput);
        if (retrievedLog != null) {
            // Assuming ProductStatusLog has a getProducts() method that returns the associated Product
            Product associatedProduct = retrievedLog.getProducts();
            if (associatedProduct != null && associatedProduct.getProductStatus() != null) {
                try {
                    associatedProduct.getProductStatus().size(); // Initialize
                    // Ensure Product model has setProductStatus(Set<ProductStatusLog>)
                    associatedProduct.setProductStatus(new java.util.HashSet<>(associatedProduct.getProductStatus()));
                } catch (org.hibernate.LazyInitializationException lie) {
                    System.err.println("LazyInitializationException in ProductStatusLogImpl.retrieveById for associatedProduct.productStatus: " + lie.getMessage());
                    associatedProduct.setProductStatus(new java.util.HashSet<>());
                }
            }
        }
        return retrievedLog;
    }
    
    @Override
    public List<ProductStatusLog> getLogsByProductId(int productId) throws RemoteException {
        List<ProductStatusLog> logList;
        try {
            logList = dao.getLogsByProductId(productId);
        } catch (Exception e) {
            System.err.println("Error in ProductStatusLogImpl.getLogsByProductId (DAO call): " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Error fetching logs by product ID from DAO: " + e.getMessage(), e);
        }

        if (logList != null) {
            for (ProductStatusLog log : logList) {
                if (log == null) continue;
                Product associatedProduct = log.getProducts();
                if (associatedProduct != null && associatedProduct.getProductStatus() != null) {
                    try {
                        associatedProduct.getProductStatus().size(); // Initialize
                        associatedProduct.setProductStatus(new java.util.HashSet<>(associatedProduct.getProductStatus()));
                    } catch (org.hibernate.LazyInitializationException lie) {
                        System.err.println("LazyInitializationException in ProductStatusLogImpl.getLogsByProductId for P:" + associatedProduct.getProductId() + ".productStatus: " + lie.getMessage());
                        associatedProduct.setProductStatus(new java.util.HashSet<>());
                    }
                }
            }
        }
        return logList;
    }
    
}
