/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.HibernateUtil;
import dao.UserDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import service.UserInterface;
import util.OtpUtil;


public class UserImpl extends UnicastRemoteObject implements UserInterface{

     private Map<String, String> otpStore = new HashMap<>(); // username -> otp
     private Map<String, String> userEmails = new HashMap<>(); // dummy userEmail DB
    
    
    public  UserImpl() throws RemoteException{
         super();
        // Dummy data
        userEmails.put("admin", "admin@example.com");
        userEmails.put("testuser", "test@example.com");

    }
    UserDao dao= new UserDao();  

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
    public User loginUser(String username, String password) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean verifyOtp(String username, String otp) throws RemoteException {

        Session session = HibernateUtil.getSessionFactory().openSession();
    try {
        Query query = session.createQuery("FROM User WHERE username = :username");
        query.setParameter("username", username);
        User user = (User) query.uniqueResult();

        if (user == null) return false;

        // Check if OTP matches and is not expired (optional)
        String storedOtp = user.getOtp();
        Date generatedTime = user.getOtpGeneratedTime();

        long currentTime = System.currentTimeMillis();
        long otpTime = generatedTime.getTime();
        long diffMinutes = (currentTime - otpTime) / (60 * 1000);

      if (storedOtp != null && storedOtp.equals(otp) && diffMinutes <= 5) {
    return true;
}
    } finally {
        session.close();
    }
    return false;
    }

    @Override
    public String requestOtp(String username) throws RemoteException {
    if (!userEmails.containsKey(username)) {
            return "Username not found!";
        }
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit
        otpStore.put(username, otp);

        String recipient = userEmails.get(username);
        sendEmail(recipient, otp);

        return "OTP generated and sent to registered email.";
    }
    private void sendEmail(String to, String otp) {
  
            String from = "your-email@gmail.com";
    String host = "smtp.gmail.com";

    Properties properties = System.getProperties();
    properties.put("mail.smtp.host", host);
    properties.put("mail.smtp.port", "587");
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");

    // Use javax.mail.Session for email sending
    javax.mail.Session emailSession = javax.mail.Session.getInstance(properties,
        new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("your-email@gmail.com", "your-app-password");
            }
        });

    try {
        javax.mail.internet.MimeMessage message = new javax.mail.internet.MimeMessage(emailSession);
        message.setFrom(new javax.mail.internet.InternetAddress(from));
        message.addRecipient(javax.mail.Message.RecipientType.TO, new javax.mail.internet.InternetAddress(to));
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);

        javax.mail.Transport.send(message);
        System.out.println("Sent message successfully.");
    } catch (javax.mail.MessagingException mex) {
        mex.printStackTrace();
    }
    }
}
