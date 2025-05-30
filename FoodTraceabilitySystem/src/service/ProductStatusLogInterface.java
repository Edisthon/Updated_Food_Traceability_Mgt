/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.ProductStatusLog;


public interface ProductStatusLogInterface extends Remote{
    
    public String registerProductStatusLog(ProductStatusLog products) throws RemoteException;
    public String updateProductStatusLog(ProductStatusLog products) throws RemoteException;
    public String deleteProductStatusLog(ProductStatusLog products) throws RemoteException;
    public List<ProductStatusLog> retreiveAll() throws RemoteException;
    public ProductStatusLog retrieveById(ProductStatusLog product) throws RemoteException;
    List<ProductStatusLog> getLogsByProductId(int productId) throws RemoteException;
    
}
