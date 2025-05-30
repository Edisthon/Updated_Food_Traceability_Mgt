package view; 


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
import model.Product;
import model.User;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import service.ProductInterface;


public class ViewDataForm extends javax.swing.JFrame {
 
    private User currentUser; 
   
    public ViewDataForm(User user) {
        this.currentUser = user;
        initComponents(); 
        if (currentUser != null) {
            setTitle("View Data - User: " + currentUser.getUsername() + " - Food Traceability System");
        } else {
            setTitle("View Data - Food Traceability System");
        }
        setResizable(false);
        setLocationRelativeTo(null); // Center form
        
        viewUserInfo();      
        loadProductData();      
        viewProductStatusInfo();
        
        // Tooltips
        btnRefreshProducts.setToolTipText("Reload the list of all products from the server.");
        btnExportProductsPdf.setToolTipText("Export the current products list to a PDF file.");
        btnBack.setToolTipText("Return to the previous screen or dashboard.");

        // Set default button
        JRootPane rootPane = SwingUtilities.getRootPane(btnRefreshProducts); 
        if (rootPane != null) {
             rootPane.setDefaultButton(btnRefreshProducts);
        }
    }
    
    private void viewUserInfo() {
        DefaultTableModel userModel = (DefaultTableModel) tableUsers.getModel();
        userModel.setRowCount(0); 
        // Column identifiers are set in initComponents for tableUsers
        // userModel.setColumnIdentifiers(new String[]{"User ID", "Username", "Email", "Role", "Created At"});

        if (currentUser != null) {
            userModel.addRow(new Object[]{
                currentUser.getUserId(),
                currentUser.getUsername(),
                currentUser.getEmail(), 
                currentUser.getRole(),
                currentUser.getCreatedAt()
            });
        }
    }
    
    private void loadProductData() {
        DefaultTableModel productModel = (DefaultTableModel) tableProducts.getModel();
        productModel.setRowCount(0); 
        // Column identifiers are set in initComponents for tableProducts
        // productModel.setColumnIdentifiers(new String[]{"Product ID", "Name", "Origin", "QR Code", "Registration Date", "Registered By User ID"});

        try {
            ProductInterface productService = RmiClientUtil.getProductService();
            List<Product> products = productService.retreiveAll(); 

            if (products != null && !products.isEmpty()) {
                for (Product product : products) {
                    Vector<Object> row = new Vector<>();
                    row.add(product.getProductId());
                    row.add(product.getName());
                    row.add(product.getOrigin());
                    row.add(product.getQrCode());
                    row.add(product.getRegistrationDate());
//                    row.add(product.getUser() != null ? product.getUser().getUserId() : "N/A"); 
                    productModel.addRow(row);
                }
            } else {
                 // JOptionPane.showMessageDialog(this, "No products found.", "Product Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fetching products: " + ex.getMessage(), "Product Data Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); 
        }
    }
    
    private void viewProductStatusInfo() {
        DefaultTableModel statusModel = (DefaultTableModel) tableProductStatus.getModel();
        statusModel.setRowCount(0); 
        // Column identifiers are set in initComponents for tableProductStatus
        // statusModel.setColumnIdentifiers(new String[]{"Log ID", "Product ID", "Location", "Temperature", "Humidity", "Timestamp"});      
    }

    
            
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableUsers = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableProducts = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableProductStatus = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();
        btnRefreshProducts = new javax.swing.JButton();
        btnExportProductsPdf = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("View All System Data");

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

        jScrollPane2.setBackground(new java.awt.Color(102, 102, 102));

        tableProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Name", "Origin", "QR Code", "Registration Date", "Registered By User ID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableProducts);

        jScrollPane3.setBackground(new java.awt.Color(102, 102, 102));

        tableProductStatus.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Log ID", "Product ID", "Location", "Temperature", "Humidity", "Timestamp"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tableProductStatus);

        btnBack.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnBack.setText("BACK");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnRefreshProducts.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnRefreshProducts.setText("Refresh Products");
        btnRefreshProducts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshProductsActionPerformed(evt);
            }
        });

        btnExportProductsPdf.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnExportProductsPdf.setText("Export Products to PDF");
        btnExportProductsPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportProductsPdfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnRefreshProducts)
                        .addGap(18, 18, 18)
                        .addComponent(btnExportProductsPdf)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBack)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(btnRefreshProducts)
                    .addComponent(btnExportProductsPdf))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("All System Data Overview");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(220, 220, 220)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        if (currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole())) {
            new AdminDashboard(currentUser).setVisible(true);
        } else {
            // If not admin or no current user, navigate to login for safety
            JOptionPane.showMessageDialog(this, "Admin access required or session ended. Please login.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            new LoginForm().setVisible(true); 
        }
        dispose(); 
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnRefreshProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshProductsActionPerformed
        loadProductData(); 
    }//GEN-LAST:event_btnRefreshProductsActionPerformed

    private void btnExportProductsPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportProductsPdfActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Products Report as PDF");
        fileChooser.setSelectedFile(new File("ProductsReport.pdf"));
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
    }//GEN-LAST:event_btnExportProductsPdfActionPerformed


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                User testUser = new User();
                testUser.setUserId(1);
                testUser.setUsername("testUser");
                testUser.setEmail("test@example.com");
                testUser.setRole("user");
                testUser.setCreatedAt("2023-01-01");
                new ViewDataForm(testUser).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnExportProductsPdf;
    private javax.swing.JButton btnRefreshProducts;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tableProductStatus;
    private javax.swing.JTable tableProducts;
    private javax.swing.JTable tableUsers;
    // End of variables declaration//GEN-END:variables

}
