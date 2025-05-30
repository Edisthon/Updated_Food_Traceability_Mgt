package view; 

// Updated imports
import util2.RmiClientUtil;

import javax.swing.JButton; 
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Vector; 
import java.io.File;
import java.io.IOException;
import model.User;

// PDFBox imports
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts; 
import service.UserInterface;


public class ViewUsersForm extends javax.swing.JFrame {
 
    public ViewUsersForm() { 
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("View System Users - Food Traceability System"); // Added app name
        loadUsersData(); 
        
        // Tooltips
        btnRefresh.setToolTipText("Reload the list of users from the server.");
        btnExportPdf.setToolTipText("Export the current user list to a PDF file.");
        btnBack.setToolTipText("Return to the previous screen.");

        // Set default button
        JRootPane rootPane = SwingUtilities.getRootPane(btnRefresh); 
        if (rootPane != null) {
             rootPane.setDefaultButton(btnRefresh);
        }
    }
    
    private void setupTableColumns(){
        DefaultTableModel model = (DefaultTableModel) tableUsers.getModel();
        model.setColumnIdentifiers(new String [] {
            "User ID", "Username", "Email", "Role", "Created At"
        });
    }
    
    private void loadUsersData(){
        DefaultTableModel model = (DefaultTableModel) tableUsers.getModel();
        model.setRowCount(0); 

        try {
            UserInterface userService = (UserInterface) RmiClientUtil.getUserService();
            List<User> users = userService.retreiveAll(); 

            if (users != null && !users.isEmpty()) {
                for (User user : users) {
                    Vector<Object> row = new Vector<>();
                    row.add(user.getUserId());
                    row.add(user.getUsername());
                    row.add(user.getEmail()); 
                    row.add(user.getRole());
                    row.add(user.getCreatedAt());
                    model.addRow(row);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No users found or server returned an empty list.", "User Data Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fetching users: " + ex.getMessage(), "Data Retrieval Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); 
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableUsers = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        btnExportPdf = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("View System Users");

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        jScrollPane1.setBackground(new java.awt.Color(102, 102, 102));

        tableUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User ID", "Username", "Email", "Role", "Created At"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableUsers);

        btnBack.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("System Users List");

        btnRefresh.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnExportPdf.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnExportPdf.setText("Export to PDF");
        btnExportPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportPdfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(196, 196, 196)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(btnRefresh)
                        .addGap(18, 18, 18)
                        .addComponent(btnExportPdf)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBack)))
                .addGap(40, 40, 40))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(btnRefresh)
                    .addComponent(btnExportPdf))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        JOptionPane.showMessageDialog(this, "Navigating back to the main dashboard (placeholder).", "Navigation", JOptionPane.INFORMATION_MESSAGE);
        // Example: new AdminDashboard(currentUser).setVisible(true); // If currentUser was available and needed
        // Or new MainMenu().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadUsersData(); 
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnExportPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportPdfActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Users Report as PDF");
        fileChooser.setSelectedFile(new File("UsersReport.pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".pdf");
            }
try (PDDocument document = new PDDocument()) {
    PDPage page = new PDPage();
    document.addPage(page);

    PDPageContentStream contentStream = new PDPageContentStream(document, page); // declared outside

    DefaultTableModel model = (DefaultTableModel) tableUsers.getModel();
    int colCount = model.getColumnCount();
    int rowCount = model.getRowCount();

    float margin = 50;
    float yStart = page.getMediaBox().getHeight() - margin;
    float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
    float yPosition = yStart;
    float rowHeight = 20f;
    float cellMargin = 5f;
    float tableBottomMargin = margin + rowHeight;

    float[] colWidths = new float[colCount];
    for (int i = 0; i < colCount; i++) {
        colWidths[i] = tableWidth / (float) colCount;
    }

    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
    float xPosition = margin;
    for (int i = 0; i < colCount; i++) {
        contentStream.beginText();
        contentStream.newLineAtOffset(xPosition + cellMargin, yPosition - 15);
        contentStream.showText(model.getColumnName(i));
        contentStream.endText();
        xPosition += colWidths[i];
    }
    yPosition -= rowHeight;

    contentStream.moveTo(margin, yPosition);
    contentStream.lineTo(margin + tableWidth, yPosition);
    contentStream.stroke();

    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
    for (int i = 0; i < rowCount; i++) {
        if (yPosition < tableBottomMargin) {
            contentStream.close(); // close current page stream
            page = new PDPage();
            document.addPage(page);
            contentStream = new PDPageContentStream(document, page); // reopen for new page

            // Redraw header
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
            yPosition = yStart;
            xPosition = margin;
            for (int k = 0; k < colCount; k++) {
                contentStream.beginText();
                contentStream.newLineAtOffset(xPosition + cellMargin, yPosition - 15);
                contentStream.showText(model.getColumnName(k));
                contentStream.endText();
                xPosition += colWidths[k];
            }
            yPosition -= rowHeight;

            contentStream.moveTo(margin, yPosition);
            contentStream.lineTo(margin + tableWidth, yPosition);
            contentStream.stroke();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        }

        xPosition = margin;
        for (int j = 0; j < colCount; j++) {
            Object cellValue = model.getValueAt(i, j);
            String text = (cellValue == null) ? "" : cellValue.toString();
            contentStream.beginText();
            contentStream.newLineAtOffset(xPosition + cellMargin, yPosition - 15);
            contentStream.showText(text);
            contentStream.endText();
            xPosition += colWidths[j];
        }

        yPosition -= rowHeight;
        contentStream.moveTo(margin, yPosition + rowHeight);
        contentStream.lineTo(margin + tableWidth, yPosition + rowHeight);
        contentStream.stroke();
    }

    // draw border lines
    contentStream.moveTo(margin, yStart - rowHeight);
    contentStream.lineTo(margin + tableWidth, yStart - rowHeight);
    contentStream.stroke();

    contentStream.moveTo(margin, yPosition);
    contentStream.lineTo(margin + tableWidth, yPosition);
    contentStream.stroke();

    xPosition = margin;
    for (int k = 0; k <= colCount; k++) {
        contentStream.moveTo(xPosition, yStart - rowHeight);
        contentStream.lineTo(xPosition, yPosition);
        contentStream.stroke();
        if (k < colCount) xPosition += colWidths[k];
    }

    contentStream.close(); // ✅ important: close at the end
    document.save(fileToSave);
    JOptionPane.showMessageDialog(this, "Users report saved to " + fileToSave.getAbsolutePath(), "PDF Export Successful", JOptionPane.INFORMATION_MESSAGE);
} catch (IOException ioe) {
    JOptionPane.showMessageDialog(this, "Error exporting to PDF: " + ioe.getMessage(), "PDF Export Error", JOptionPane.ERROR_MESSAGE);
    ioe.printStackTrace();
}

        }
    }//GEN-LAST:event_btnExportPdfActionPerformed

  
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewUsersForm().setVisible(true);
            }
        });
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnExportPdf;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableUsers;
    // End of variables declaration//GEN-END:variables
}
