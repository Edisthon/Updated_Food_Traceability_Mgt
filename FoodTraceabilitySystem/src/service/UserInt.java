/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface UserInt extends Remote{
    String requestOtp(String username) throws RemoteException;
    boolean verifyOtp(String username, String otp) throws RemoteException;
}
