
package service;

import java.rmi.Remote;

import java.rmi.RemoteException;


public interface UserInt extends Remote{
    String requestOtp(String username) throws RemoteException;
    boolean verifyOtp(String username, String otp) throws RemoteException;
}
