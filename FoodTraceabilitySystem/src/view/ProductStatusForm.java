package view; 

import util2.RmiClientUtil;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat; 
import java.util.Date; 
import java.awt.Font; // For title if needed
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
        
        allowOnlyLettersLoc(txtLocation);
        allowOnlyNumbersWithDecimal(txtTemperature); 
        allowOnlyNumbersWithDecimal(txtHumidity);    
        allowOnlyNumbers(txtStatusProductId);      
        
        disallowSpaces(txtLocation);
        // disallowSpaces(txtTemperature); // Spaces not typical but might be part of a copy-paste
        // disallowSpaces(txtHumidity);
        disallowSpaces(txtStatusProductId);
        
        // Tooltips
        btnSubmitStatus.setToolTipText("Click to submit the new status log for the product.");
        btnBack.setToolTipText("Return to the previous screen.");

        // Set default button
        JRootPane rootPane = SwingUtilities.getRootPane(btnSubmitStatus); 
        if (rootPane != null) {
            rootPane.setDefaultButton(btnSubmitStatus);
        }
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtStatusProductId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtLocation = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTemperature = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtHumidity = new javax.swing.JTextField();
        btnSubmitStatus = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Register Product Status Log");

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Product Status Log");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Enter Product ID:");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Location:");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Temperature (°C):");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Humidity (%):");

        btnSubmitStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSubmitStatus.setText("Submit Status");
        btnSubmitStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitStatusActionPerformed(evt);
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
                        .addGap(140, 140, 140)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtStatusProductId)
                            .addComponent(txtLocation)
                            .addComponent(txtTemperature)
                            .addComponent(txtHumidity, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(btnSubmitStatus)
                        .addGap(50, 50, 50)
                        .addComponent(btnBack)))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtStatusProductId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtTemperature, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtHumidity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSubmitStatus)
                    .addComponent(btnBack))
                .addContainerGap(30, Short.MAX_VALUE))
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
            return;
        }
        try {
            temperature = Double.parseDouble(tempStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Temperature. Must be a number (e.g., 23.5).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            humidity = Double.parseDouble(humStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Humidity. Must be a number (e.g., 60.2).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProductStatusLog statusLog = new ProductStatusLog();
        
        Product product = new Product();
        product.setProductId(productId);
        statusLog.setLogId(WIDTH);
        
        statusLog.setLocation(location);
        statusLog.setTemperature(temperature);
        statusLog.setHumidity(humidity);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        statusLog.setTimestamp(sdf.format(new Date()));

        try {
            ProductStatusLogInterface statusLogService = RmiClientUtil.getProductStatusLogService();
            String result = statusLogService.registerProductStatusLog(statusLog);
            JOptionPane.showMessageDialog(this, result, "Product Status Log Submission", JOptionPane.INFORMATION_MESSAGE);

            if (result != null && result.toLowerCase().contains("success")) {
                txtStatusProductId.setText("");
                txtLocation.setText("");
                txtTemperature.setText("");
                txtHumidity.setText("");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error submitting product status: " + ex.getMessage(), "Submission Error", JOptionPane.ERROR_MESSAGE);
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
