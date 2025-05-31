package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date; // Required for new Date() if used, and for log.getTimestamp()
import model.Product;
import model.User;
import model.ProductUserLog;
import service.ProductInterface;
import service.ProductUserLogInterface;
import util2.RmiClientUtil;

public class ProductTraceabilityViewForm extends JFrame {

    private User currentUser;
    private Product productToTrace;
    private JTable logTable;
    private DefaultTableModel tableModel;
    private ProductUserLogInterface logService;
    private ProductInterface productService; // To fetch product details if only ID is given
    private JLabel lblTitle;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ProductTraceabilityViewForm(User currentUser, Product product) {
        this.currentUser = currentUser;
        this.productToTrace = product;
        commonConstructorSetup();
        if (productToTrace != null) {
            updateTitle();
            loadTraceabilityLogs();
        } else {
             if(lblTitle!=null) lblTitle.setText("Traceability Report (No Product Selected)");
             JOptionPane.showMessageDialog(this, "No product data provided.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        setVisible(true);
    }

    public ProductTraceabilityViewForm(User currentUser, int productId) {
        this.currentUser = currentUser;
        commonConstructorSetup(); // Init services and basic UI elements
        fetchProductDetails(productId); // Fetch product, then load logs
        // setVisible will be called by fetchProductDetails or its callback if async, or here if sync
    }

    private void commonConstructorSetup(){
        try {
            logService = RmiClientUtil.getProductUserLogService();
            productService = RmiClientUtil.getProductService();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to services: " + e.getMessage(),
                                          "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
        initComponents(); // Initialize UI components
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(750, 500));
        setLocationRelativeTo(null);
    }

    private void fetchProductDetails(int productId) {
        if (productService == null) {
            JOptionPane.showMessageDialog(this, "Product service not available.", "Error", JOptionPane.ERROR_MESSAGE);
            lblTitle.setText("Traceability Report (Service Error)");
            setVisible(true); // Show the form even with error
            pack();
            return;
        }
        try {
            // Assuming ProductInterface has a method like getProductById(int)
            // For now, using retrieveById which might need a Product object
            Product p = new Product();
            p.setProductId(productId); // Need to ensure Product model has a simple constructor or setters
            this.productToTrace = productService.retrieveById(p);

            if (this.productToTrace != null) {
                updateTitle();
                loadTraceabilityLogs();
            } else {
                lblTitle.setText("Traceability Report (Product ID: " + productId + " not found)");
                JOptionPane.showMessageDialog(this, "Product with ID " + productId + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblTitle.setText("Traceability Report (Error fetching product ID: " + productId + ")");
            JOptionPane.showMessageDialog(this, "Error fetching product details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        setVisible(true); // Show the form after attempting to fetch
        pack();
    }

    private void updateTitle() {
        if (productToTrace != null && lblTitle != null) {
            lblTitle.setText("Traceability Report for Product: " + productToTrace.getName() + " (ID: " + productToTrace.getProductId() + ")");
            setTitle("Product Traceability: " + productToTrace.getName());
        } else if (lblTitle != null){
             lblTitle.setText("Traceability Report");
             setTitle("Product Traceability");
        }
    }


    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));

        lblTitle = new JLabel("Traceability Report Loading...", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setForeground(new Color(50,50,50));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        String[] columnNames = {"Timestamp", "Action", "User", "Location", "Details"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        logTable = new JTable(tableModel);
        logTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        logTable.getTableHeader().setReorderingAllowed(false); // Usually chronological order is fixed
        logTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
        logTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        logTable.setRowHeight(25);
        logTable.setAutoCreateRowSorter(true); // Allow sorting by columns

        JScrollPane scrollPane = new JScrollPane(logTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setOpaque(false);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnRefresh.setToolTipText("Reload the traceability logs for this product.");
        buttonPanel.add(btnRefresh);

        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnClose.setToolTipText("Close this window.");
        buttonPanel.add(btnClose);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // Action Listeners
        btnRefresh.addActionListener(e -> loadTraceabilityLogs());
        btnClose.addActionListener(e -> dispose());
    }

    public void loadTraceabilityLogs() {
        if (productToTrace == null) {
            if (selectedProductId != -1 && productService != null) {
                // Attempt to re-fetch if productToTrace is null but we have an ID
                // This could happen if initial fetch failed but service is now up.
                fetchProductDetails(selectedProductId); // This will call loadTraceabilityLogs again if successful
                return; // Avoid proceeding with null productToTrace now
            }
            JOptionPane.showMessageDialog(this, "Product information is not available to load logs.", "Error", JOptionPane.ERROR_MESSAGE);
            tableModel.setRowCount(0); // Clear table
            tableModel.addRow(new Object[]{"Product details missing.", "", "", "", ""});
            return;
        }
        if (logService == null) {
             try { logService = RmiClientUtil.getProductUserLogService(); }
             catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Log service connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{"Log service unavailable.", "", "", "", ""});
                return;
            }
        }
         if (logService == null) { // Still null
             JOptionPane.showMessageDialog(this, "Log service is unavailable.", "Service Error", JOptionPane.ERROR_MESSAGE);
             tableModel.setRowCount(0);
             tableModel.addRow(new Object[]{"Log service unavailable.", "", "", "", ""});
             return;
         }


        tableModel.setRowCount(0);
        try {
            List<ProductUserLog> logs = logService.getLogsByProductId(productToTrace.getProductId());
            if (logs != null && !logs.isEmpty()) {
                // Sort logs by timestamp chronologically (ascending)
                Collections.sort(logs, Comparator.comparing(ProductUserLog::getTimestamp));
                for (ProductUserLog log : logs) {
                    String userName = (log.getUser() != null && log.getUser().getUsername() != null) ? log.getUser().getUsername() : "N/A";
                    tableModel.addRow(new Object[]{
                        log.getTimestamp() != null ? DATE_FORMAT.format(log.getTimestamp()) : "N/A",
                        log.getAction() != null ? log.getAction() : "N/A",
                        userName,
                        log.getLocation() != null ? log.getLocation() : "N/A",
                        log.getDetails() != null ? log.getDetails() : ""
                    });
                }
            } else {
                 tableModel.addRow(new Object[]{"No activity logs found for this product.", "", "", "", ""});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading traceability logs: " + e.getMessage(), "Loading Error", JOptionPane.ERROR_MESSAGE);
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{"Error loading logs.", "", "", "", ""});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testUser = new User();
            testUser.setUserId(1);
            testUser.setUsername("testUser");

            // Option 1: Test with a Product object
            Product testProduct = new Product();
            testProduct.setProductId(101); // Ensure Product has setProductId
            testProduct.setName("Organic Bananas Batch 7");
            // new ProductTraceabilityViewForm(testUser, testProduct);

            // Option 2: Test with a Product ID
            new ProductTraceabilityViewForm(testUser, 102); // Assuming product ID 102 exists
        });
    }
}
