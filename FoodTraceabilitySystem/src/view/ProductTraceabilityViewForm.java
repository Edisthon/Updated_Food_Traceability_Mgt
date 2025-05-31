package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList; // Added for currentLogList initialization
import java.util.Collections;
import java.util.Comparator;
import java.util.Date; // Added for main method dummy data
import model.Product;
import model.User;
import model.ProductUserLog;
import service.ProductInterface;
import service.ProductUserLogInterface;
import util2.RmiClientUtil;

public class ProductTraceabilityViewForm extends JFrame {

    private User currentUser;
    private Product productToTrace;
    private int selectedProductId = -1; // Initialize to indicate no specific ID yet

    private JTable logTable;
    private DefaultTableModel tableModel;
    private ProductUserLogInterface logService;
    private ProductInterface productService;
    private JLabel lblTitle;
    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<ProductUserLog> currentLogList = new ArrayList<>(); // Added initialization
    private JPanel mainPanel; // Declare mainPanel as a class member


    // Constructor for when a Product object is provided
    public ProductTraceabilityViewForm(User currentUser, Product product) {
        this.currentUser = currentUser;
        this.productToTrace = product;
        if (product != null) {
            this.selectedProductId = product.getProductId();
        }
        commonConstructorSetup();
        updateTitle(); // Update title after productToTrace is set
        loadTraceabilityLogs();
        setVisible(true);
    }

    // Constructor for when only a productId is provided
    public ProductTraceabilityViewForm(User currentUser, int productId) {
        this.currentUser = currentUser;
        this.selectedProductId = productId;
        // productToTrace will be fetched in fetchProductDetails
        commonConstructorSetup();
        fetchProductDetails(productId); // This will set productToTrace, then updateTitle and loadLogs
        // setVisible(true) is called after fetchProductDetails completes or in it.
    }

    private void commonConstructorSetup() {
        try {
            logService = RmiClientUtil.getProductUserLogService();
            productService = RmiClientUtil.getProductService();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error initializing services: " + e.getMessage(), "Service Error", JOptionPane.ERROR_MESSAGE);
            // Depending on severity, might want to disable form or close
        }
        initComponents(); // Initialize UI components
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setLocationRelativeTo(null); // Center on screen before sizing
    }

    private void updateTitle() {
        if (productToTrace != null) {
            setTitle("Product Traceability: " + productToTrace.getName());
            if (lblTitle != null) { // Ensure lblTitle is initialized
                 lblTitle.setText("Traceability Report for Product: " + productToTrace.getName() + " (ID: " + productToTrace.getProductId() + ")");
            }
        } else if (selectedProductId != -1) {
            setTitle("Product Traceability: ID " + selectedProductId);
            if (lblTitle != null) {
                 lblTitle.setText("Traceability Report for Product ID: " + selectedProductId + " (Loading details...)");
            }
        } else {
            setTitle("Product Traceability");
             if (lblTitle != null) {
                lblTitle.setText("Traceability Report (No Product Specified)");
             }
        }
    }


    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Use EmptyBorder for padding
        mainPanel.setBackground(new Color(240, 240, 240));

        lblTitle = new JLabel("Traceability Report", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setForeground(new Color(50,50,50));
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        // updateTitle(); // Called after productToTrace is potentially set

        // Center Panel (Table)
        String[] columnNames = {"Timestamp", "Action", "User", "Location", "Details"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        logTable = new JTable(tableModel);
        logTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        logTable.getTableHeader().setReorderingAllowed(false);
        logTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
        logTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
        logTable.setRowHeight(22);
        logTable.setFillsViewportHeight(true);
        logTable.setAutoCreateRowSorter(true);


        JScrollPane scrollPane = new JScrollPane(logTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel (Buttons)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(mainPanel.getBackground());

        JButton btnRefresh = new JButton("Refresh");
        styleButton(btnRefresh);
        btnRefresh.setToolTipText("Reload the log data for this product.");
        btnRefresh.addActionListener(e -> loadTraceabilityLogs());

        JButton btnClose = new JButton("Close");
        styleButton(btnClose);
        btnClose.setToolTipText("Close this window.");
        btnClose.addActionListener(e -> dispose());

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnClose);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        // pack() and setMinimumSize will be called after data loading or by constructors
    }


    private void styleButton(JButton button) {
        button.setFont(new Font("Tahoma", Font.BOLD, 12));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }


    private void fetchProductDetails(int productId) {
        if (productService == null) {
            JOptionPane.showMessageDialog(this, "Product service not available.", "Service Error", JOptionPane.ERROR_MESSAGE);
            updateTitle();
            loadTraceabilityLogs(); // Will show product not available in table
            packAndShow();
            return;
        }
        try {
            Product p = new Product();
            p.setProductId(productId);
            this.productToTrace = productService.retrieveById(p);
            if (this.productToTrace == null) {
                 JOptionPane.showMessageDialog(this, "Product details not found for ID: " + productId, "Not Found", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching product details: " + e.getMessage(), "Service Error", JOptionPane.ERROR_MESSAGE);
        }
        updateTitle();
        loadTraceabilityLogs();
        packAndShow();
    }

    private void packAndShow(){
        pack();
        setMinimumSize(new Dimension(700, 450)); // Ensure minimum size after pack
        setLocationRelativeTo(null); // Re-center after pack
        // setVisible(true) is handled by constructors after this flow
    }


    public void loadTraceabilityLogs() {
        if (productToTrace == null) {
            if (this.selectedProductId != -1 && productService != null) {
                // This case should ideally be handled by the constructor calling fetchProductDetails
                // If fetchProductDetails failed, productToTrace would be null.
                // So, we show a message based on selectedProductId if available.
                updateTitle(); // Ensure title reflects current state
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{"Product details for ID " + this.selectedProductId + " could not be fetched or are unavailable.", "", "", "", ""});
                return;
            }
            // If both productToTrace and selectedProductId are invalid
            updateTitle(); // Ensure title reflects current state (e.g. "No Product Specified")
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{"Product not specified or details unavailable.", "", "", "", ""});
            return;
        }

        if (logService == null) {
            JOptionPane.showMessageDialog(this, "Log service not available.", "Service Error", JOptionPane.ERROR_MESSAGE);
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{"Log service unavailable.", "", "", "", ""});
            return;
        }

        tableModel.setRowCount(0);
        currentLogList.clear();

        try {
            List<ProductUserLog> logs = logService.getLogsByProductId(productToTrace.getProductId());
            if (logs != null && !logs.isEmpty()) {
                Collections.sort(logs, Comparator.comparing(ProductUserLog::getTimestamp));
                currentLogList.addAll(logs);

                for (ProductUserLog log : currentLogList) {
                    String userName = (log.getUser() != null && log.getUser().getUsername() != null) ? log.getUser().getUsername() : "N/A";
                    String timestampStr = (log.getTimestamp() != null) ? dateTimeFormatter.format(log.getTimestamp()) : "N/A";
                    tableModel.addRow(new Object[]{
                            timestampStr,
                            log.getAction() != null ? log.getAction() : "",
                            userName,
                            log.getLocation() != null ? log.getLocation() : "",
                            log.getDetails() != null ? log.getDetails() : ""
                    });
                }
            } else {
                tableModel.addRow(new Object[]{"No activity logs found for this product.", "", "", "", ""});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading traceability logs: " + e.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{"Error loading logs.", "", "", "", ""});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testUser = new User();
            testUser.setUserId(1);
            testUser.setUsername("testadmin");
            testUser.setRole("admin");

            Product testProduct = new Product();
            testProduct.setProductId(101); // Example ID
            testProduct.setName("Test Product Alpha");
            testProduct.setBatchNumber("B101");
            testProduct.setOrigin("Test Origin");
            // Assuming Product has setRegistrationDate or similar for testing
            // testProduct.setRegistrationDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

            // Test with Product object - This constructor will call setVisible(true)
            // new ProductTraceabilityViewForm(testUser, testProduct);

            // Test with Product ID - This constructor will call setVisible(true) via fetchProductDetails
            new ProductTraceabilityViewForm(testUser, 102);
        });
    }
}
