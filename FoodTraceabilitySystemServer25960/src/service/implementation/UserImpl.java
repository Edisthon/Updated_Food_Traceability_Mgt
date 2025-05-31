/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.UserDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;
// Removed HashMap, Map, Properties, Random, javax.mail related imports implicitly by removing their usage
import model.User;
// Removed org.hibernate.Query and org.hibernate.Session as they are not directly used in UserImpl anymore (UserDao handles them)
// Removed dao.HibernateUtil as it's not directly used here.
import service.UserInterface;
import util.OtpUtil;


public class UserImpl extends UnicastRemoteObject implements UserInterface{

    // Removed otpStore and userEmails HashMaps
    
    public  UserImpl() throws RemoteException{
         super();
        // Removed dummy data initialization for userEmails
    }
    UserDao dao= new UserDao();  // UserDao instance is kept

    @Override
    public String registerUser(User users) throws RemoteException {
        return dao.registerUser(users);
    }

    @Override
    public String updateUser(User users) throws RemoteException {
        return dao.updateUser(users);
    }

    @Override
    public String deleteUser(User users) throws RemoteException {
        return dao.deleteUser(users);
    }

    @Override
    public List<User> retreiveAll() throws RemoteException {
        return dao.retreiveAll();
    }

    @Override
    public User retrieveById(User user) throws RemoteException {
        return dao.retrieveById(user);
    }

    @Override
    public User loginUser(String email, String otp) throws RemoteException { // Signature changed
        if (email == null || email.trim().isEmpty() || otp == null || otp.trim().isEmpty()) {
            return null;
        }
        User user = dao.findByEmail(email);
        if (user == null) {
            return null; // User not found
        }

        String storedOtp = user.getOtp();
        Date otpGeneratedTime = user.getOtpGeneratedTime();

        if (storedOtp == null || otpGeneratedTime == null) {
            return null; // No OTP was generated for this user
        }

        if (storedOtp.equals(otp)) {
            long currentTime = System.currentTimeMillis();
            long otpTimeMillis = otpGeneratedTime.getTime();
            long diffMinutes = (currentTime - otpTimeMillis) / (60 * 1000);

            if (diffMinutes <= 10) { // OTP valid for 10 minutes
                // OTP is valid, clear it to prevent reuse
                user.setOtp(null);
                user.setOtpGeneratedTime(null);
                try {
                    dao.updateUser(user);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Log this error, but proceed with login as OTP was verified.
                }
                return user; // Login successful
            } else {
                // OTP Expired
                 user.setOtp(null);
                 user.setOtpGeneratedTime(null);
                 try { dao.updateUser(user); } catch (Exception e) { e.printStackTrace(); }
                return null;
            }
        } else {
            return null; // OTP mismatch
        }
    }

    @Override
    public boolean verifyOtp(String email, String otp) throws RemoteException { // Signature changed
         if (email == null || email.trim().isEmpty() || otp == null || otp.trim().isEmpty()) {
            return false;
        }
        User user = dao.findByEmail(email);
        if (user == null) {
            return false; // User not found
        }

        String storedOtp = user.getOtp();
        Date otpGeneratedTime = user.getOtpGeneratedTime();

        if (storedOtp == null || otpGeneratedTime == null) {
            return false; // No OTP was generated
        }

        if (storedOtp.equals(otp)) {
            long currentTime = System.currentTimeMillis();
            long otpTimeMillis = otpGeneratedTime.getTime();
            long diffMinutes = (currentTime - otpTimeMillis) / (60 * 1000);

            return diffMinutes <= 10; // Valid if not expired (10 min window)
        }
        return false; // OTP mismatch
    }

    @Override
    public String requestOtpByEmail(String email) throws RemoteException { // Method renamed and logic changed
        if (email == null || email.trim().isEmpty()) {
            return "Email cannot be empty.";
        }
        // Consider adding more robust email format validation here if needed.

        User user = dao.findByEmail(email);
        if (user == null) {
            return "No user found with this email address.";
        }

        String otp = OtpUtil.generateOtp();
        user.setOtp(otp);
        user.setOtpGeneratedTime(new Date()); // Current time

        try {
            String updateResult = dao.updateUser(user);
            if (updateResult != null && updateResult.toLowerCase().contains("success")) {
                sendEmail(user.getEmail(), otp); // This will now print to console
                return "OTP has been generated and sent (simulated).";
            } else {
                return "Failed to store OTP. DB issue: " + (updateResult == null ? "Unknown error" : updateResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error processing OTP request: " + e.getMessage(), e);
        }
    }

    // sendEmail method simplified for simulation
    private void sendEmail(String to, String otp) {
        System.out.println("---- OTP SIMULATION ----");
        System.out.println("To: " + to);
        System.out.println("OTP: " + otp);
        System.out.println("------------------------");
    }
}
