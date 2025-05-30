/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.User;


public interface UserInterface extends Remote{
    String requestOtp(String username) throws RemoteException;
    User loginUser(String username, String password) throws RemoteException;
    public String registerUser(User users) throws RemoteException;
    public String updateUser(User users) throws RemoteException;
    public String deleteUser(User users) throws RemoteException;
    public List<User> retreiveAll() throws RemoteException;
    public User retrieveById(User user) throws RemoteException;
    boolean verifyOtp(String username, String otp) throws RemoteException;
    
}
