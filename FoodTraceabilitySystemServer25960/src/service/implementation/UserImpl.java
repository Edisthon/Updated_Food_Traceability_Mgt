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
import java.util.Properties; // Added for JavaMail
import javax.mail.Authenticator; // Added for JavaMail
import javax.mail.Message; // Added for JavaMail
import javax.mail.MessagingException; // Added for JavaMail
import javax.mail.PasswordAuthentication; // Added for JavaMail
import javax.mail.Session; // Added for JavaMail
import javax.mail.Transport; // Added for JavaMail
import javax.mail.internet.InternetAddress; // Added for JavaMail
import javax.mail.internet.MimeMessage; // Added for JavaMail
import model.User;
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

    // sendEmail method re-implemented for live email sending
    private void sendEmail(String to, String otp) {
        // --- BEGIN USER CONFIGURABLE SECTION ---
        // IMPORTANT: Replace these placeholders with your actual email provider's details.
        // For Gmail, you might need to enable "Less secure app access" or generate an "App Password".

        final String FROM_EMAIL = "YOUR_EMAIL@gmail.com"; // e.g., "your.app.notification@gmail.com"
        final String EMAIL_PASSWORD = "YOUR_EMAIL_APP_PASSWORD"; // Your Gmail password or App Password

        final String SMTP_HOST = "smtp.gmail.com"; // For Gmail. Change if using another provider.
        final String SMTP_PORT = "587"; // For Gmail TLS. Common ports: 587 (TLS), 465 (SSL)
        // --- END USER CONFIGURABLE SECTION ---

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server properties
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // Use true for TLS

        // Get the Session object.
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });

        // session.setDebug(true); // Enable for debugging if there are connection issues

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(FROM_EMAIL));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Your OTP Code - Food Traceability System");

            // Now set the actual message
            message.setText("Your One-Time Password (OTP) for the Food Traceability System is: " + otp +
                            "\n\nThis OTP is valid for a short period. Please do not share it with anyone.");

            // Send message
            Transport.send(message);
            System.out.println("OTP email sent successfully to " + to);

        } catch (MessagingException mex) {
            mex.printStackTrace();
            // Consider how to handle this error in the broader application.
            // For now, it prints to console. The calling method `requestOtpByEmail`
            // will still return its success/failure message based on DB operations.
            System.err.println("Error sending OTP email: " + mex.getMessage());
        }
    }
}
