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
    String requestOtpByEmail(String email) throws RemoteException; // Changed
    User loginUser(String email, String otp) throws RemoteException; // Changed
    public String registerUser(User users) throws RemoteException;
    public String updateUser(User users) throws RemoteException;
    public String deleteUser(User users) throws RemoteException;
    public List<User> retreiveAll() throws RemoteException;
    public User retrieveById(User user) throws RemoteException;
    boolean verifyOtp(String email, String otp) throws RemoteException; // Changed
    
}
