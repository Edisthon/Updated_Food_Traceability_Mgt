package view; 


import util2.RmiClientUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
        
        allowOnlyLettersProductname(txtName); // Allows spaces
        allowOnlyLettersLoc(txtOrigin);     // Allows spaces
        // txtName and txtOrigin can have spaces.
        // txtQrCode should ideally not have spaces for robustness, applying strict disallowSpaces.
        disallowSpaces(txtQrCode); 
        
        // Enhanced Tooltips
        txtName.setToolTipText("Enter the full name of the product (e.g., Organic Apples Batch #123).");
        txtOrigin.setToolTipText("Enter the origin or current location of the product (e.g., Farm XYZ, USA or Warehouse B).");
        txtQrCode.setToolTipText("Enter the data to be encoded in the QR code for this product. This should be unique and without spaces.");
        btnRegister.setToolTipText("Click to register this new product.");
        btnBack.setToolTipText("Return to the Admin Dashboard.");

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
    
    // Simplified to strictly disallow any space for fields where it's applied (e.g., QR Code)
    private void disallowSpaces(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();
                if (Character.isWhitespace(c)) {
                    evt.consume();
                    JOptionPane.showMessageDialog(RegisterProductForm.this, "Spaces are not allowed in this field.", "Input Restriction", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel(); // Title
        jLabel2 = new javax.swing.JLabel(); // Name Label
        txtName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel(); // Origin Label
        txtOrigin = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel(); // QR Code Label
        txtQrCode = new javax.swing.JTextField();
        btnRegister = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        // Title is set in constructor

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding
        jPanel1.setLayout(new GridBagLayout()); // GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Default insets
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        jLabel1.setFont(new Font("Tahoma", Font.BOLD, 18));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Register Product");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 20, 5); // Bottom margin for title
        jPanel1.add(jLabel1, gbc);

        gbc.gridwidth = 1; // Reset gridwidth
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Tahoma", Font.PLAIN, 13);
        Font textFont = new Font("Tahoma", Font.PLAIN, 13);
        Dimension textFieldSize = new Dimension(250, 28); // Adjusted text field size
        Dimension buttonSize = new Dimension(120, 35);

        // Product Name
        jLabel2.setFont(labelFont);
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Product Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        jPanel1.add(jLabel2, gbc);

        txtName.setFont(textFont);
        txtName.setPreferredSize(textFieldSize);
        gbc.gridx = 1;
        gbc.gridy = 1;
        jPanel1.add(txtName, gbc);

        // Origin/Location
        jLabel3.setFont(labelFont);
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Origin/Location:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        jPanel1.add(jLabel3, gbc);

        txtOrigin.setFont(textFont);
        txtOrigin.setPreferredSize(textFieldSize);
        gbc.gridx = 1;
        gbc.gridy = 2;
        jPanel1.add(txtOrigin, gbc);

        // QR Code Data
        jLabel4.setFont(labelFont);
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("QR Code Data:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        jPanel1.add(jLabel4, gbc);

        txtQrCode.setFont(textFont);
        txtQrCode.setPreferredSize(textFieldSize);
        gbc.gridx = 1;
        gbc.gridy = 3;
        jPanel1.add(txtQrCode, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new java.awt.Color(102, 102, 102));
        GridBagConstraints btnGbc = new GridBagConstraints();
        btnGbc.insets = new Insets(5,5,5,5);

        btnRegister.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnRegister.setText("Register");
        btnRegister.setPreferredSize(buttonSize);
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });
        btnGbc.gridx = 0;
        btnGbc.gridy = 0;
        buttonPanel.add(btnRegister, btnGbc);

        btnBack.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnBack.setText("Back");
        btnBack.setPreferredSize(buttonSize);
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        btnGbc.gridx = 1;
        btnGbc.gridy = 0;
        buttonPanel.add(btnBack, btnGbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        jPanel1.add(buttonPanel, gbc);

        getContentPane().removeAll();
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        String productName = txtName.getText().trim();
        String origin = txtOrigin.getText().trim();
        String qrCode = txtQrCode.getText().trim();

        if (productName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Product Name is required.", "Input Required", JOptionPane.WARNING_MESSAGE);
            txtName.requestFocus();
            return;
        }
        if (origin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Origin/Location is required.", "Input Required", JOptionPane.WARNING_MESSAGE);
            txtOrigin.requestFocus();
            return;
        }
        if (qrCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "QR Code Data is required.", "Input Required", JOptionPane.WARNING_MESSAGE);
            txtQrCode.requestFocus();
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
