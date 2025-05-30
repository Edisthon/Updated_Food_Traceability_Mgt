/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.ProductStatusLogDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.ProductStatusLog;
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
        return dao.retreiveAll();
    }

    @Override
    public ProductStatusLog retrieveById(ProductStatusLog productStatusLog) throws RemoteException {
        return dao.retrieveById(productStatusLog);
    }
    
    
    
}
