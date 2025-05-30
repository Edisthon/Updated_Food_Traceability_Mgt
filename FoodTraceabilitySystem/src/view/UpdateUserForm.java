package view;

import util2.RmiClientUtil;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color; // For jLabel1 color, if needed programmatically
import java.awt.Font; // For jLabel1 font, if needed programmatically
import model.User;
import service.UserInterface;


public class UpdateUserForm extends javax.swing.JFrame {
    
    private User userToUpdate; 
    // private JTextField userIdField; // This was conceptual, using lblUserId instead as per .form

    public UpdateUserForm(User user) {
        this.userToUpdate = user;
        initComponents(); 
        setResizable(false);
        setLocationRelativeTo(null); // Center the form
        setTitle("Update User Information - Food Traceability System"); // Added app name
        
        populateFormFields();
        
        allowOnlyLetters(txtUsername); 
        disallowSpaces(txtUsername);
        disallowSpaces(txtPassword); 
        
        // Tooltips
        btnUpdateUser.setToolTipText("Click to save the changes to this user.");
        btnBack.setToolTipText("Return to the previous screen without saving.");

        // Set default button
        JRootPane rootPane = SwingUtilities.getRootPane(btnUpdateUser); 
        if (rootPane != null) {
            rootPane.setDefaultButton(btnUpdateUser);
        }
    }
    
    private void populateFormFields() {
        if (userToUpdate != null) {
            lblUserId.setText("User ID: " + userToUpdate.getUserId()); // Update lblUserId
            txtUsername.setText(userToUpdate.getUsername());
            txtEmail.setText(userToUpdate.getEmail()); 
            comboRole.setSelectedItem(userToUpdate.getRole());
            txtPassword.setText(""); 
        } else {
            JOptionPane.showMessageDialog(this, "No user data provided to update.", "User Data Error", JOptionPane.ERROR_MESSAGE);
            dispose(); 
        }
    }
    
    private void allowOnlyLetters(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isLetter(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    if (!Character.isWhitespace(c)) { 
                         evt.consume();
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
                    JOptionPane.showMessageDialog(UpdateUserForm.this, "No spaces allowed in this field.", "Input Restriction", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblUserId = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        comboRole = new javax.swing.JComboBox<>();
        btnUpdateUser = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Update User Information");

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Update User");

        lblUserId.setForeground(new java.awt.Color(255, 255, 255));
        lblUserId.setText("User ID: [ID]");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Username:");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Email:");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("New Password (optional):");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Role:");

        comboRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "user" }));

        btnUpdateUser.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnUpdateUser.setText("Save Updates");
        btnUpdateUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateUserActionPerformed(evt);
            }
        });

        btnBack.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(195, 195, 195)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(lblUserId, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnUpdateUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtUsername)
                            .addComponent(txtPassword)
                            .addComponent(txtEmail)
                            .addComponent(comboRole, 0, 150, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap(330, Short.MAX_VALUE)
                        .addComponent(btnBack)))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addComponent(lblUserId)
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(comboRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(btnUpdateUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateUserActionPerformed
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String newPassword = new String(txtPassword.getPassword()).trim(); 
        String role = (String) comboRole.getSelectedItem();

        if (username.isEmpty() || email.isEmpty() || role == null) {
            JOptionPane.showMessageDialog(this, "Username, Email, and Role cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!java.util.regex.Pattern.compile(emailRegex).matcher(email).matches()) {
             JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Invalid Email Format", JOptionPane.WARNING_MESSAGE);
            return;
        }

        userToUpdate.setUsername(username);
        userToUpdate.setEmail(email);
        userToUpdate.setRole(role);
        
        if (!newPassword.isEmpty()) {
            userToUpdate.setPassword(newPassword); 
        } else {
            userToUpdate.setPassword(null); 
        }

        try {
            UserInterface userService = (UserInterface) RmiClientUtil.getUserService();
            String result; 
            result = userService.updateUser(userToUpdate);
            JOptionPane.showMessageDialog(this, result, "Update Status", JOptionPane.INFORMATION_MESSAGE);
            
            if (result != null && result.toLowerCase().contains("success")) {
                dispose(); 
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating user: " + ex.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); 
        }
    }//GEN-LAST:event_btnUpdateUserActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        JOptionPane.showMessageDialog(this, "Update User form closed.", "Form Closed", JOptionPane.INFORMATION_MESSAGE);
        dispose(); 
    }//GEN-LAST:event_btnBackActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                User testUser = new User();
                new UpdateUserForm(testUser).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnUpdateUser;
    private javax.swing.JComboBox<String> comboRole;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblUserId;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
