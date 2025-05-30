package view;

import util2.RmiClientUtil;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color; // For jLabel1 color, if needed programmatically
import java.awt.Font; // For jLabel1 font, if needed programmatically
import model.User;
import service.UserInterface;

public class RegisterUserForm extends javax.swing.JFrame {
    
    public RegisterUserForm() {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Register New User - Food Traceability System"); // More descriptive title

        // Input validation setup
        allowOnlyLetters(txtUsername); 
        disallowSpaces(txtUsername);
        disallowSpaces(txtPassword);
        
        // Tooltips
        btnRegister.setToolTipText("Click to complete user registration.");
        btnBack.setToolTipText("Go back to the previous screen.");

        // Set default button
        JRootPane rootPane = SwingUtilities.getRootPane(btnRegister); 
        if (rootPane != null) {
            rootPane.setDefaultButton(btnRegister);
        }
    }
    
    private void allowOnlyLetters(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isLetter(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) { 
                    if (!Character.isWhitespace(c)) { 
                         evt.consume();
                         // Consider a status bar update instead of JOptionPane for this type of validation
                    }
                }            
            }
        });
    }
    
    private void disallowSpaces(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();
                if (Character.isWhitespace(c)) { 
                    evt.consume();
                    JOptionPane.showMessageDialog(RegisterUserForm.this, "No spaces allowed in this field.", "Input Restriction", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnBack = new javax.swing.JButton(); 
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        comboRole = new javax.swing.JComboBox<>();
        btnRegister = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel(); 
        txtEmail = new javax.swing.JTextField(); 

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); 

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        // For consistency, btnBack could be moved to jPanel2 in the .form file design
        // Or jPanel1 could be removed if btnBack is part of the main panel.
        // For now, keeping structure as per original generation.

        btnBack.setFont(new java.awt.Font("Tahoma", 1, 13)); 
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(btnBack)
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // Slightly larger title
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // Center title
        jLabel1.setText("Register New User"); 

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Username:");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Password:");
        
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Email:");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Role:");

        comboRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "user", "admin" })); // "user" often default

        btnRegister.setFont(new java.awt.Font("Tahoma", 1, 13)); 
        btnRegister.setText("Register");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });
        
        // This is a simplified GroupLayout. Actual .form file will be more complex.
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE) // Consistent padding
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5) 
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRegister)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtUsername)
                                .addComponent(txtPassword)
                                .addComponent(txtEmail) 
                                .addComponent(comboRole, 0, 180, Short.MAX_VALUE))))) // Increased width
                .addContainerGap(60, Short.MAX_VALUE)) // Consistent padding
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30,30,30)
                .addComponent(jLabel1)
                .addGap(40, 40, 40) 
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20) // Adjusted spacing
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20) 
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5) 
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)) 
                .addGap(20, 20, 20) 
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(comboRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40) // Adjusted spacing
                .addComponent(btnRegister)
                .addContainerGap(100, Short.MAX_VALUE)) 
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0) 
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String email = txtEmail.getText().trim(); 
        String role = (String) comboRole.getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || role == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields (Username, Password, Email, Role).", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Basic email validation using regex (more robust)
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!java.util.regex.Pattern.compile(emailRegex).matcher(email).matches()) {
             JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Invalid Email Format", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); 
        newUser.setEmail(email);
        newUser.setRole(role);

        try {
            UserInterface userService = (UserInterface) RmiClientUtil.getUserService();
            String result = userService.registerUser(newUser);
            JOptionPane.showMessageDialog(this, result, "Registration Status", JOptionPane.INFORMATION_MESSAGE);
            
            if (result != null && result.toLowerCase().contains("success")) { 
                txtUsername.setText("");
                txtPassword.setText("");
                txtEmail.setText("");
                comboRole.setSelectedIndex(0); 
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error during registration: " + ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); 
        }
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // Example: Navigate to LoginForm or AdminDashboard if applicable
        // For now, just dispose and inform user.
        // new LoginForm().setVisible(true); // Or AdminDashboard if user is logged in
        JOptionPane.showMessageDialog(this, "Registration form closed.", "Form Closed", JOptionPane.INFORMATION_MESSAGE);
        dispose(); 
    }//GEN-LAST:event_btnBackActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegisterUserForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRegister;
    private javax.swing.JComboBox<String> comboRole;
    private javax.swing.JButton btnBack; 
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5; 
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JTextField txtEmail; 
    // End of variables declaration//GEN-END:variables
}
