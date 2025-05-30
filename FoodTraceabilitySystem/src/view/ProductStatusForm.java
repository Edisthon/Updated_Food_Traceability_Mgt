package view; 

import util2.RmiClientUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat; 
import java.util.Date; 
import model.Product;
import model.ProductStatusLog;
import model.User;
import service.ProductStatusLogInterface;

public class ProductStatusForm extends javax.swing.JFrame {
    
    private User currentUser; 

    public ProductStatusForm(User user) { 
        this.currentUser = user;
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null); // Center form
        setTitle("Register Product Status Log - Food Traceability System"); // Added app name
        
        // Apply input validation (renamed one method for clarity)
        allowAlphanumericAndSpecialCharsLoc(txtLocation);
        allowOnlyNumbersWithDecimal(txtTemperature); 
        allowOnlyNumbersWithDecimal(txtHumidity);    
        allowOnlyNumbers(txtStatusProductId);      
        
        // Disallow spaces where appropriate
        disallowSpaces(txtLocation);
        // Not disallowing spaces for temp/humidity as they might be pasted with spaces, validation handles non-numeric.
        disallowSpaces(txtStatusProductId);
        
        // Enhanced Tooltips
        txtStatusProductId.setToolTipText("Enter the numeric Product ID.");
        txtLocation.setToolTipText("Enter the current location of the product (e.g., Warehouse A, Section 3).");
        txtTemperature.setToolTipText("Enter temperature in Celsius (e.g., 4.5 or -2.0).");
        txtHumidity.setToolTipText("Enter relative humidity in percentage (e.g., 55.2).");
        btnSubmitStatus.setToolTipText("Click to submit the new status log for the product.");
        btnBack.setToolTipText("Return to the Admin Dashboard.");

        // Set default button
        JRootPane rootPane = SwingUtilities.getRootPane(btnSubmitStatus); 
        if (rootPane != null) {
            rootPane.setDefaultButton(btnSubmitStatus);
        }
    }
    
    // Renamed for clarity as it allows more than just letters
    private void allowAlphanumericAndSpecialCharsLoc(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();
                // Allows letters, digits, whitespace, comma, hyphen, slash, parentheses
                if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c) &&
                    c != ',' && c != '-' && c != '/' && c != '(' && c != ')' &&
                    c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE ) {
                    evt.consume();
                }
            }
        });
    }
    
    private void allowOnlyNumbersWithDecimal(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();
                String currentText = textField.getText();

                if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE && c != '-') { 
                    evt.consume();
                }
                if (c == '.' && currentText.contains(".")) {
                    evt.consume(); 
                }
                if (c == '-' && currentText.length() > 0 && !currentText.equals("-")) { // Allow '-' only at the start or if field is just "-"
                     if(textField.getCaretPosition() != 0 || currentText.contains("-")){
                        evt.consume();
                     }
                }
            }
        });
    }
    
    private void allowOnlyNumbers(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    evt.consume();
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
                    JOptionPane.showMessageDialog(ProductStatusForm.this, "Spaces are not allowed in this field.", "Input Restriction", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel(); // Title Label
        jLabel2 = new javax.swing.JLabel(); // Product ID Label
        txtStatusProductId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel(); // Location Label
        txtLocation = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel(); // Temperature Label
        txtTemperature = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel(); // Humidity Label
        txtHumidity = new javax.swing.JTextField();
        btnSubmitStatus = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        // Title is set in constructor

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setBorder(new EmptyBorder(20, 20, 20, 20)); // Added padding
        jPanel1.setLayout(new GridBagLayout()); // Using GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Default insets
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        jLabel1.setFont(new Font("Tahoma", Font.BOLD, 18)); // Prominent title
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Product Status Log");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 20, 5); // Bottom margin for title
        jPanel1.add(jLabel1, gbc);

        gbc.gridwidth = 1; // Reset gridwidth
        gbc.anchor = GridBagConstraints.WEST; // Align labels to the west

        // Consistent font for labels and text fields
        Font labelFont = new Font("Tahoma", Font.PLAIN, 13);
        Font textFont = new Font("Tahoma", Font.PLAIN, 13);
        Dimension textFieldSize = new Dimension(200, 28); // Consistent text field size

        // Product ID
        jLabel2.setFont(labelFont);
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Product ID:"); // Label text updated for clarity
        gbc.gridx = 0;
        gbc.gridy = 1;
        jPanel1.add(jLabel2, gbc);

        txtStatusProductId.setFont(textFont);
        txtStatusProductId.setPreferredSize(textFieldSize);
        gbc.gridx = 1;
        gbc.gridy = 1;
        jPanel1.add(txtStatusProductId, gbc);

        // Location
        jLabel3.setFont(labelFont);
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Location:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        jPanel1.add(jLabel3, gbc);

        txtLocation.setFont(textFont);
        txtLocation.setPreferredSize(textFieldSize);
        gbc.gridx = 1;
        gbc.gridy = 2;
        jPanel1.add(txtLocation, gbc);

        // Temperature
        jLabel4.setFont(labelFont);
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Temperature (°C):");
        gbc.gridx = 0;
        gbc.gridy = 3;
        jPanel1.add(jLabel4, gbc);

        txtTemperature.setFont(textFont);
        txtTemperature.setPreferredSize(textFieldSize);
        gbc.gridx = 1;
        gbc.gridy = 3;
        jPanel1.add(txtTemperature, gbc);

        // Humidity
        jLabel5.setFont(labelFont);
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Humidity (%):");
        gbc.gridx = 0;
        gbc.gridy = 4;
        jPanel1.add(jLabel5, gbc);

        txtHumidity.setFont(textFont);
        txtHumidity.setPreferredSize(textFieldSize);
        gbc.gridx = 1;
        gbc.gridy = 4;
        jPanel1.add(txtHumidity, gbc);

        // Buttons Panel (to group buttons)
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new java.awt.Color(102, 102, 102)); // Match parent background
        GridBagConstraints btnGbc = new GridBagConstraints();
        btnGbc.insets = new Insets(5,5,5,5); // Spacing between buttons
        Dimension buttonSize = new Dimension(130, 35); // Consistent button size

        btnSubmitStatus.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnSubmitStatus.setText("Submit Status");
        btnSubmitStatus.setPreferredSize(buttonSize);
        btnSubmitStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitStatusActionPerformed(evt);
            }
        });
        btnGbc.gridx = 0;
        btnGbc.gridy = 0;
        buttonPanel.add(btnSubmitStatus, btnGbc);

        btnBack.setFont(new Font("Tahoma", Font.BOLD, 12)); // Consistent font
        btnBack.setText("Back");
        btnBack.setPreferredSize(buttonSize);
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        btnGbc.gridx = 1; // Place next to the previous button
        btnGbc.gridy = 0;
        buttonPanel.add(btnBack, btnGbc);

        gbc.gridx = 0;
        gbc.gridy = 5; // Place below the input fields
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the button panel
        gbc.insets = new Insets(20, 5, 5, 5); // Top margin for buttons
        jPanel1.add(buttonPanel, gbc);

        // Add jPanel1 to the frame's content pane directly
        // The GroupLayout code for the main content pane is removed
        // And jPanel1 is added directly.
        getContentPane().removeAll(); // Remove existing components from content pane
        getContentPane().setLayout(new java.awt.BorderLayout()); // Set a simple layout for content pane
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER); // Add jPanel1

        pack(); // Pack the frame to fit components
        setLocationRelativeTo(null); // Center form
    }// </editor-fold>//GEN-END:initComponents

    private void btnSubmitStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitStatusActionPerformed
        String productIdStr = txtStatusProductId.getText().trim();
        String location = txtLocation.getText().trim();
        String tempStr = txtTemperature.getText().trim();
        String humStr = txtHumidity.getText().trim();

        if (productIdStr.isEmpty() || location.isEmpty() || tempStr.isEmpty() || humStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields (Product ID, Location, Temperature, Humidity) are required.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId;
        double temperature;
        double humidity;

        try {
            productId = Integer.parseInt(productIdStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Product ID. Must be a whole number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            txtStatusProductId.requestFocus();
            return;
        }
        try {
            temperature = Double.parseDouble(tempStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Temperature. Must be a number (e.g., 23.5).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            txtTemperature.requestFocus();
            return;
        }
        try {
            humidity = Double.parseDouble(humStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Humidity. Must be a number (e.g., 60.2).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            txtHumidity.requestFocus();
            return;
        }

        ProductStatusLog statusLog = new ProductStatusLog();
        
        // The Product object itself isn't added to statusLog unless ProductStatusLog has a Product field.
        // We will set the productId directly on statusLog.
        statusLog.setProductId(productId);

        statusLog.setLogId(0); // LogId should ideally be generated by the database (e.g., auto-increment).
                               // Sending 0 or null might be a convention if the backend handles generation.
                               // Original statusLog.setLogId(WIDTH) was incorrect.
        
        statusLog.setLocation(location);
        statusLog.setTemperature(temperature);
        statusLog.setHumidity(humidity);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        statusLog.setTimestamp(sdf.format(new Date()));
        // Removed setRecordedBy as it's an unimplemented field


        try {
            ProductStatusLogInterface statusLogService = RmiClientUtil.getProductStatusLogService();

            // Removed productExists check as it's an unimplemented service method

            String result = null;
            if (statusLogService != null) {
                 result = statusLogService.registerProductStatusLog(statusLog);
            } else {
                throw new Exception("Could not connect to Product Status Log Service.");
            }

            JOptionPane.showMessageDialog(this, result, "Product Status Log Submission", JOptionPane.INFORMATION_MESSAGE);

            if (result != null && result.toLowerCase().contains("success")) {
                txtStatusProductId.setText("");
                txtLocation.setText("");
                txtTemperature.setText("");
                txtHumidity.setText("");
                txtStatusProductId.requestFocus(); // Set focus back to the first field
            }
        } catch (Exception ex) {
            // Check for specific RMI/connection errors if possible
            if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("connectexception")) {
                JOptionPane.showMessageDialog(this, "Error connecting to the server. Please check the server status and network connection.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            } else if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("productnotfoundexception")) { // Example custom exception
                 JOptionPane.showMessageDialog(this, ex.getMessage(), "Product Not Found", JOptionPane.ERROR_MESSAGE);
                 txtStatusProductId.requestFocus();
            }else {
                JOptionPane.showMessageDialog(this, "Error submitting product status: " + (ex.getMessage() == null ? "Unknown error" : ex.getMessage()), "Submission Error", JOptionPane.ERROR_MESSAGE);
            }
            ex.printStackTrace(); 
        }
    }//GEN-LAST:event_btnSubmitStatusActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        if (currentUser != null) { 
            new AdminDashboard(currentUser).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No active user session. Please login again.", "Session Information", JOptionPane.WARNING_MESSAGE);
            new LoginForm().setVisible(true); 
        }
        dispose();
    }//GEN-LAST:event_btnBackActionPerformed
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                User testUser = new User(); 
                testUser.setUserId(0); 
                testUser.setUsername("testUser");
                new ProductStatusForm(testUser).setVisible(true);
            }
        });
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnSubmitStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtHumidity;
    private javax.swing.JTextField txtLocation;
    private javax.swing.JTextField txtStatusProductId;
    private javax.swing.JTextField txtTemperature;
    // End of variables declaration//GEN-END:variables
}
