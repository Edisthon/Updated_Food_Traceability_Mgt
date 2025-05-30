package view; 


import util2.RmiClientUtil;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font; // For title if needed
import java.awt.Color; // For title if needed
import model.Product;
import model.User;
import service.ProductInterface;


public class RegisterProductForm extends javax.swing.JFrame {
    
    private User currentUser; 
    
    public RegisterProductForm(User user) {
        this.currentUser = user;
        initComponents();      
        setResizable(false);
        setLocationRelativeTo(null); // Center form
        if (currentUser != null) {
            setTitle("Register Product - User: " + currentUser.getUsername() + " - Food Traceability System");
        } else {
            setTitle("Register Product - Food Traceability System");
            JOptionPane.showMessageDialog(this, "Warning: No user logged in. Product registration requires user association.", "User Session Warning", JOptionPane.WARNING_MESSAGE);
            // Consider disabling btnRegister if currentUser is null
            // btnRegister.setEnabled(false); 
        }
        
        allowOnlyLettersProductname(txtName);
        allowOnlyLettersLoc(txtOrigin); 
        disallowSpaces(txtName);
        disallowSpaces(txtOrigin);
        disallowSpaces(txtQrCode); 
        
        // Tooltips
        btnRegister.setToolTipText("Click to register this new product.");
        btnBack.setToolTipText("Return to the previous screen.");

        // Set default button
        JRootPane rootPane = SwingUtilities.getRootPane(btnRegister); 
        if (rootPane != null) {
            rootPane.setDefaultButton(btnRegister);
        }
    }
    
    private void allowOnlyLettersProductname(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();
                // Allow letters, numbers, spaces, and common punctuation for product names
                if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c) && c != '-' && c != '&' && c != '(' && c != ')' && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    evt.consume();
                }
            }
        });
    }
    
    private void allowOnlyLettersLoc(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c) && c != ',' && c != '-' && c != '/' && c != '(' && c != ')' && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE ) {
                    evt.consume();
                }
            }
        });
    }
    
    private void disallowSpaces(JTextField textField) { // Potentially review if spaces are truly disallowed or just leading/trailing
        textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();
                if (Character.isWhitespace(c) && textField.getText().trim().isEmpty() && c == ' ') { // Disallow leading spaces
                     evt.consume();
                     JOptionPane.showMessageDialog(RegisterProductForm.this, "Leading spaces are not allowed.", "Input Restriction", JOptionPane.WARNING_MESSAGE);
                } else if (c == ' ' && textField.getText().endsWith(" ")) { // Disallow multiple spaces
                     evt.consume();
                }
                // Original disallow all spaces:
                // if (Character.isWhitespace(c)) { 
                //     evt.consume();
                //     JOptionPane.showMessageDialog(RegisterProductForm.this, "No spaces allowed in this field.", "Input Restriction", JOptionPane.WARNING_MESSAGE);
                // }
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtOrigin = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtQrCode = new javax.swing.JTextField();
        btnRegister = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Register Product");

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Register Product");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Product Name:");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Origin/Location:");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("QR Code Data:");

        btnRegister.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnRegister.setText("Register");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        btnBack.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(txtOrigin)
                            .addComponent(txtQrCode)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(btnRegister)
                        .addGap(80, 80, 80)
                        .addComponent(btnBack)))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtOrigin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtQrCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegister)
                    .addComponent(btnBack))
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
String productName = txtName.getText().trim();
String origin = txtOrigin.getText().trim();
String qrCode = txtQrCode.getText().trim();

if (productName.isEmpty() || origin.isEmpty() || qrCode.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Product Name, Origin/Location, and QR Code Data are required.", "Input Required", JOptionPane.WARNING_MESSAGE);
    return;
}

if (currentUser == null) {
    JOptionPane.showMessageDialog(this, "No user logged in. Cannot register product.", "User Session Error", JOptionPane.ERROR_MESSAGE);
    return;
}

int productCreatorUserId = currentUser.getUserId();

try {
    ProductInterface productService = RmiClientUtil.getProductService(); // ✅ only declare once

    User fullUser = productService.getUserById(productCreatorUserId); // ✅ correct method name
    if (fullUser == null) {
        JOptionPane.showMessageDialog(this, "User not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    Product product = new Product();
    product.setName(productName);
    product.setOrigin(origin);
    product.setQrCode(qrCode);
    product.setUsers(fullUser); // ✅ now fully managed entity

    String result = productService.registerProduct(product);
    JOptionPane.showMessageDialog(this, result, "Product Registration Status", JOptionPane.INFORMATION_MESSAGE);

    if (result != null && result.toLowerCase().contains("success")) {
        txtName.setText("");
        txtOrigin.setText("");
        txtQrCode.setText("");
    }

} catch (Exception ex) {
    JOptionPane.showMessageDialog(this, "Error during product registration: " + ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
    ex.printStackTrace();
}

           
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        if (currentUser != null) {
            new AdminDashboard(currentUser).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No active user session. Please login again.", "Session Information", JOptionPane.INFORMATION_MESSAGE);
            new LoginForm().setVisible(true); 
        }
        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                User testUser = new User();
                testUser.setUserId(1); 
                testUser.setUsername("testAdmin");
                new RegisterProductForm(testUser).setVisible(true);
            }
        });
    }
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRegister;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtOrigin;
    private javax.swing.JTextField txtQrCode;
    // End of variables declaration//GEN-END:variables
}
