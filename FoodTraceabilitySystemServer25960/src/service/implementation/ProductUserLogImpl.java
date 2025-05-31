package service.implementation;

import dao.ProductUserLogDao;
import model.ProductUserLog;
import service.ProductUserLogInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

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
        try {
            return dao.getAllProductUserLogs();
        } catch (Exception e) {
            // Log server-side exception
            e.printStackTrace();
            throw new RemoteException("Server error fetching all product user logs: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ProductUserLog> getLogsByProductId(int productId) throws RemoteException {
        try {
            return dao.getProductUserLogsByProductId(productId);
        } catch (Exception e) {
            // Log server-side exception
            e.printStackTrace();
            throw new RemoteException("Server error fetching product user logs by product ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ProductUserLog> getLogsByUserId(int userId) throws RemoteException {
        try {
            return dao.getProductUserLogsByUserId(userId);
        } catch (Exception e) {
            // Log server-side exception
            e.printStackTrace();
            throw new RemoteException("Server error fetching product user logs by user ID: " + e.getMessage(), e);
        }
    }
}
