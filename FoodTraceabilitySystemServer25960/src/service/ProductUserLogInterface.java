package service;

import model.ProductUserLog;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ProductUserLogInterface extends Remote {
    String recordProductUserLog(ProductUserLog log) throws RemoteException;
    List<ProductUserLog> getAllLogs() throws RemoteException;
    List<ProductUserLog> getLogsByProductId(int productId) throws RemoteException;
    List<ProductUserLog> getLogsByUserId(int userId) throws RemoteException;
}
