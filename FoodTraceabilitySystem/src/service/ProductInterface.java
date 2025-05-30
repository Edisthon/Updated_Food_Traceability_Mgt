/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Product;
import model.User;


public interface ProductInterface extends Remote{
    
    public String  registerProduct(Product products) throws RemoteException;
    public  String  updateProduct(Product products) throws RemoteException;
    public  String  deleteProduct(Product products) throws RemoteException;
    public List<Product> retreiveAll() throws RemoteException;
    public Product retrieveById(Product product) throws RemoteException;
    User getUserById(int userId) throws RemoteException;
    
}
