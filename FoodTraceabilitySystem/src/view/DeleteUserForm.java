package view;


import util2.RmiClientUtil;
import javax.swing.*;
import java.awt.Font; // For jLabel1 if needed
import model.User;
import service.UserInterface;

public class DeleteUserForm extends javax.swing.JFrame {

    public DeleteUserForm() { 
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Delete User - Food Traceability System"); // Added app name
        
        // Tooltips
        btnDeleteUser.setToolTipText("Click to permanently delete the user with the specified ID.");
        btnBack.setToolTipText("Return to the previous screen.");

        // Set default button
        JRootPane rootPane = SwingUtilities.getRootPane(btnDeleteUser); 
        if (rootPane != null) {
            rootPane.setDefaultButton(btnDeleteUser);
        }
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnDeleteUser = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        lblUserIdToDelete = new javax.swing.JLabel();
        txtUserIdToDelete = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Delete User");

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Delete User by ID");

        btnDeleteUser.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnDeleteUser.setText("Delete User");
        btnDeleteUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteUserActionPerformed(evt);
            }
        });

        btnBack.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblUserIdToDelete.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblUserIdToDelete.setForeground(new java.awt.Color(255, 255, 255));
        lblUserIdToDelete.setText("Enter User ID to Delete:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(lblUserIdToDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtUserIdToDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(btnDeleteUser)
                        .addGap(50, 50, 50)
                        .addComponent(btnBack)))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUserIdToDelete)
                    .addComponent(txtUserIdToDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteUser)
                    .addComponent(btnBack))
                .addContainerGap(50, Short.MAX_VALUE))
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

    private void btnDeleteUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteUserActionPerformed
        String userIdStr = txtUserIdToDelete.getText().trim();
        if (userIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a User ID to delete.", "User ID Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userIdToDel;
        try {
            userIdToDel = Integer.parseInt(userIdStr);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Invalid User ID format. Please enter a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to permanently delete user with ID: " + userIdToDel + "?\nThis action cannot be undone.",
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            User userToDelete = new User();
            userToDelete.setUserId(userIdToDel); 

            try {
                UserInterface userService = (UserInterface) RmiClientUtil.getUserService();
                String result = userService.deleteUser(userToDelete); 
                JOptionPane.showMessageDialog(this, result, "Deletion Status", JOptionPane.INFORMATION_MESSAGE);
                
                if (result != null && result.toLowerCase().contains("success")) { 
                     txtUserIdToDelete.setText(""); 
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage(), "Deletion Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace(); 
            }
        } else {
            JOptionPane.showMessageDialog(this, "User deletion was cancelled.", "Deletion Cancelled", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnDeleteUserActionPerformed
    
    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        JOptionPane.showMessageDialog(this, "Delete User form closed.", "Form Closed", JOptionPane.INFORMATION_MESSAGE);
        dispose(); 
    }//GEN-LAST:event_btnBackActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DeleteUserForm().setVisible(true);
            }
        });
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDeleteUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblUserIdToDelete;
    private javax.swing.JTextField txtUserIdToDelete;
    // End of variables declaration//GEN-END:variables
}
