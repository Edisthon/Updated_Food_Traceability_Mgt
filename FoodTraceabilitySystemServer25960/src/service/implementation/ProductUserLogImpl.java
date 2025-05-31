package service.implementation;

import dao.ProductUserLogDao;
import model.Product; // Added
import model.User; // Added
import model.ProductUserLog;
import service.ProductUserLogInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet; // Added
import java.util.List;
import org.hibernate.LazyInitializationException; // Added

public class ProductUserLogImpl extends UnicastRemoteObject implements ProductUserLogInterface {

    private ProductUserLogDao dao = new ProductUserLogDao();

    public ProductUserLogImpl() throws RemoteException {
        super();
    }

    @Override
    public String recordProductUserLog(ProductUserLog log) throws RemoteException {
        try {
            return dao.saveProductUserLog(log);
        } catch (Exception e) {
            // Log server-side exception
            e.printStackTrace();
            throw new RemoteException("Server error recording product user log: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ProductUserLog> getAllLogs() throws RemoteException {
        List<ProductUserLog> logList;
        try {
            logList = dao.getAllProductUserLogs();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Server error fetching all product user logs from DAO: " + e.getMessage(), e);
        }
        if (logList != null) {
            for (ProductUserLog log : logList) {
                processLogCollections(log);
            }
        }
        return logList;
    }

    @Override
    public List<ProductUserLog> getLogsByProductId(int productId) throws RemoteException {
        List<ProductUserLog> logList;
        try {
            logList = dao.getProductUserLogsByProductId(productId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Server error fetching logs by product ID from DAO: " + e.getMessage(), e);
        }
        if (logList != null) {
            for (ProductUserLog log : logList) {
                processLogCollections(log);
            }
        }
        return logList;
    }

    @Override
    public List<ProductUserLog> getLogsByUserId(int userId) throws RemoteException {
        List<ProductUserLog> logList;
        try {
            logList = dao.getProductUserLogsByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Server error fetching logs by user ID from DAO: " + e.getMessage(), e);
        }
        if (logList != null) {
            for (ProductUserLog log : logList) {
                processLogCollections(log);
            }
        }
        return logList;
    }

    private void processLogCollections(ProductUserLog log) {
        if (log == null) return;

        Product p = log.getProduct();
        if (p != null && p.getProductStatus() != null) {
            try {
                p.getProductStatus().size(); // Initialize
                p.setProductStatus(new HashSet<>(p.getProductStatus()));
            } catch (LazyInitializationException lie) {
                System.err.println("LazyInitializationException in ProductUserLogImpl for Product ID " + p.getProductId() + "'s productStatus: " + lie.getMessage());
                p.setProductStatus(new HashSet<>());
            }
        }

        User u = log.getUser();
        if (u != null && u.getProducts() != null) { // User's own list of products they manage/created
            try {
                u.getProducts().size(); // Initialize
                u.setProducts(new HashSet<>(u.getProducts()));
            } catch (LazyInitializationException lie) {
                System.err.println("LazyInitializationException in ProductUserLogImpl for User ID " + u.getUserId() + "'s products: " + lie.getMessage());
                u.setProducts(new HashSet<>());
            }
        }
    }
}
