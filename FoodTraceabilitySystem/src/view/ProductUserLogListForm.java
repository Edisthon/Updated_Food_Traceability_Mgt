package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import model.Product;
import model.ProductUserLog;
import model.User;
import service.ProductUserLogInterface;
import util2.RmiClientUtil;

// Imports for report generation (if implemented in this form)
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ProductUserLogListForm extends JFrame {

    private User currentUser;
    private Product selectedProduct; // May be null if only ID is passed
    private int selectedProductId = -1;

    private JTable logTable;
    private DefaultTableModel logTableModel;
    private ProductUserLogInterface logService;
    private List<ProductUserLog> currentLogList = new ArrayList<>();

    private JTextField txtSearchProduct; // For product ID or name
    private JTextField txtSearchUser;    // For user ID or username
    private JButton btnApplyFilters;
    private JButton btnRefreshLogs;
    private JButton btnAddLog; // Placeholder for now
    private JButton btnGenerateReport;
    private JButton btnClose;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ProductUserLogListForm(User currentUser, Product product) {
        this.currentUser = currentUser;
        this.selectedProduct = product;
        if (product != null) {
            this.selectedProductId = product.getProductId();
        }
        commonConstructorSetup();
        if (this.selectedProductId != -1 && txtSearchProduct != null) {
            txtSearchProduct.setText(String.valueOf(this.selectedProductId));
            // txtSearchProduct.setEditable(false); // Optional: make it non-editable if pre-filled
        }
        loadInitialLogs();
    }

    public ProductUserLogListForm(User currentUser, int productId) {
        this.currentUser = currentUser;
        this.selectedProduct = null; // Can be fetched if needed for display details beyond ID
        this.selectedProductId = productId;
        commonConstructorSetup();
        if (txtSearchProduct != null) {
            txtSearchProduct.setText(String.valueOf(this.selectedProductId));
            // txtSearchProduct.setEditable(false); // Optional
        }
        loadInitialLogs();
    }

    private void commonConstructorSetup() {
        try {
            logService = RmiClientUtil.getProductUserLogService();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to Log Service: " + e.getMessage(),
                                          "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
        initComponents(); // Initialize components first

        String titleString;
        if (selectedProduct != null && selectedProduct.getName() != null) {
            titleString = "Activity Logs for: " + selectedProduct.getName();
        } else if (selectedProductId != -1) {
            titleString = "Activity Logs for Product ID: " + selectedProductId;
        } else {
            titleString = "Product Activity Logs";
        }
        setTitle(titleString + " - Food Traceability System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        // setVisible(true) will be called after loadInitialLogs in other constructors
    }


    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Top Panel (Filters & Title)
        JPanel topPanel = new JPanel(new BorderLayout(5,10));
        topPanel.setOpaque(false);

        JLabel lblOverallTitle = new JLabel("Product Activity Logs", SwingConstants.CENTER);
        lblOverallTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblOverallTitle.setForeground(new Color(50,50,50));
        topPanel.add(lblOverallTitle, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));
        filterPanel.setOpaque(false);

        filterPanel.add(new JLabel("Product (ID):")); // Simplified to ID for server search
        txtSearchProduct = new JTextField(10);
        txtSearchProduct.setToolTipText("Enter Product ID to filter logs.");
        filterPanel.add(txtSearchProduct);

        filterPanel.add(new JLabel("User (ID/Username):"));
        txtSearchUser = new JTextField(15);
        txtSearchUser.setToolTipText("Enter User ID or Username to filter logs (client-side filter).");
        filterPanel.add(txtSearchUser);

        btnApplyFilters = new JButton("Apply Filters");
        styleButton(btnApplyFilters, "Apply the specified filters to the log list.");
        filterPanel.add(btnApplyFilters);

        btnRefreshLogs = new JButton("Refresh / Show All");
        styleButton(btnRefreshLogs, "Clear filters and reload all relevant logs.");
        filterPanel.add(btnRefreshLogs);

        topPanel.add(filterPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center Panel (Table)
        String[] columnNames = {"Log ID", "Product", "User", "Action", "Location", "Timestamp", "Details"};
        logTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        logTable = new JTable(logTableModel);
        logTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        logTable.getTableHeader().setReorderingAllowed(false);
        logTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
        logTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        logTable.setRowHeight(25);
        logTable.setFillsViewportHeight(true);
        // Enable sorting by clicking column headers
        logTable.setAutoCreateRowSorter(true);


        JScrollPane scrollPane = new JScrollPane(logTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel (Action Buttons)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setOpaque(false);

        btnAddLog = new JButton("Add New Log");
        styleButton(btnAddLog, "Manually add a new log entry (admin/specific roles).");
        // btnAddLog.setEnabled(currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole())); // Example role check
        bottomPanel.add(btnAddLog);

        btnGenerateReport = new JButton("Generate Report");
        styleButton(btnGenerateReport, "Generate a text report of the displayed logs.");
        bottomPanel.add(btnGenerateReport);

        btnClose = new JButton("Close");
        styleButton(btnClose, "Close this window.");
        btnClose.setBackground(new Color(180, 70, 70)); // Different color for close
        bottomPanel.add(btnClose);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);
        addActionListeners();
        // pack() and setVisible(true) are called in constructors after loadInitialLogs
    }

    private void styleButton(JButton button, String tooltip) {
        button.setFont(new Font("Tahoma", Font.BOLD, 12));
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(180, 30));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
    }

    private void loadInitialLogs() {
        // If selectedProductId was set by constructor (from ProductListForm), it's already in txtSearchProduct.
        // Or if txtSearchProduct is empty and selectedProductId is available, set it.
        if (txtSearchProduct != null && txtSearchProduct.getText().trim().isEmpty() && this.selectedProductId != -1) {
            txtSearchProduct.setText(String.valueOf(this.selectedProductId));
        }
        loadLogs(); // Call the main loading logic
        setVisible(true); // Now make the frame visible
        pack(); // Pack after components and potential initial data are set
    }

    public void loadLogs() {
        logTableModel.setRowCount(0);
        currentLogList.clear();

        if (logService == null) {
            try { logService = RmiClientUtil.getProductUserLogService(); }
            catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Log service connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); return;
            }
        }
        if (logService == null) {
            JOptionPane.showMessageDialog(this, "Log service is unavailable.", "Service Error", JOptionPane.ERROR_MESSAGE); return;
        }

        String productSearchTerm = txtSearchProduct.getText().trim();
        String userSearchTerm = txtSearchUser.getText().trim().toLowerCase();

        try {
            List<ProductUserLog> fetchedLogs;
            if (!productSearchTerm.isEmpty()) {
                try {
                    int prodId = Integer.parseInt(productSearchTerm);
                    fetchedLogs = logService.getLogsByProductId(prodId);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid Product ID format. Please enter a number.", "Filter Error", JOptionPane.WARNING_MESSAGE);
                    fetchedLogs = new ArrayList<>(); // Show empty on bad Product ID format
                }
            } else {
                fetchedLogs = logService.getAllLogs(); // Get all if no product ID filter
            }

            if (fetchedLogs != null) {
                // Client-side filtering for user if userSearchTerm is provided
                if (!userSearchTerm.isEmpty()) {
                    currentLogList = fetchedLogs.stream()
                        .filter(log -> {
                            User logUser = log.getUser();
                            if (logUser == null) return false;
                            boolean matchesId = String.valueOf(logUser.getUserId()).equals(userSearchTerm);
                            boolean matchesUsername = logUser.getUsername() != null && logUser.getUsername().toLowerCase().contains(userSearchTerm);
                            return matchesId || matchesUsername;
                        })
                        .collect(Collectors.toList());
                } else {
                    currentLogList.addAll(fetchedLogs);
                }
            }

            if (!currentLogList.isEmpty()) {
                for (ProductUserLog log : currentLogList) {
                    String productName = (log.getProduct() != null && log.getProduct().getName() != null) ?
                                         log.getProduct().getName() + " (ID:" + log.getProduct().getProductId() + ")" :
                                         (log.getProduct() != null ? "ID:" + log.getProduct().getProductId() : "N/A");
                    String userName = (log.getUser() != null && log.getUser().getUsername() != null) ?
                                      log.getUser().getUsername() + " (ID:" + log.getUser().getUserId() + ")" :
                                      (log.getUser() != null ? "ID:" + log.getUser().getUserId() : "N/A");

                    logTableModel.addRow(new Object[]{
                        log.getLogId(),
                        productName,
                        userName,
                        log.getAction(),
                        log.getLocation() != null ? log.getLocation() : "N/A",
                        log.getTimestamp() != null ? DATE_FORMAT.format(log.getTimestamp()) : "N/A",
                        log.getDetails() != null ? log.getDetails() : ""
                    });
                }
            } else {
                 logTableModel.addRow(new Object[]{"No logs found matching your criteria.", "", "", "", "", "", ""});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading logs: " + e.getMessage(), "Loading Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addActionListeners() {
        btnApplyFilters.addActionListener(e -> loadLogs());
        btnRefreshLogs.addActionListener(e -> {
            txtSearchProduct.setText(this.selectedProductId != -1 ? String.valueOf(this.selectedProductId) : ""); // Reset to initial product if any, else clear
            txtSearchUser.setText("");
            loadLogs();
        });

        btnClose.addActionListener(e -> dispose());

        btnAddLog.addActionListener(e -> {
            // Pass the currentUser and the instance of this ProductUserLogListForm
            // so ProductUserLogForm can call back to refresh the log list.
            if (logService == null) { // Check if service is available
                 JOptionPane.showMessageDialog(this, "Log service is not available. Please check server connection.", "Service Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            new ProductUserLogForm(currentUser, this).setVisible(true);
            // ProductUserLogListForm remains open.
        });

        btnGenerateReport.addActionListener(e -> generateLogReport());
    }

    private void generateLogReport() {
        if (currentLogList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No log data to generate a report.", "Empty List", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        String defaultFileName = "product_activity_logs_";
        if (selectedProductId != -1) {
            defaultFileName += "prodID" + selectedProductId + "_";
        }
        defaultFileName += DATE_FORMAT.format(new Date()).replace(":", "-").replace(" ", "_") + ".txt";
        fileChooser.setSelectedFile(new File(defaultFileName));
        fileChooser.setDialogTitle("Save Log Report");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".txt");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write("Product Activity Log Report - Food Traceability System"); writer.newLine();
                writer.write("Generated on: " + new Date().toString()); writer.newLine();
                if (selectedProductId != -1) writer.write("Report for Product ID: " + selectedProductId); writer.newLine();
                writer.write("========================================================================================================================================================"); writer.newLine();
                writer.write(String.format("%-7s | %-30s | %-25s | %-20s | %-25s | %-20s | %s",
                                           "Log ID", "Product (ID)", "User (ID)", "Action", "Location", "Timestamp", "Details")); writer.newLine();
                writer.write("--------------------------------------------------------------------------------------------------------------------------------------------------------"); writer.newLine();

                for (ProductUserLog log : currentLogList) {
                    String productName = (log.getProduct() != null && log.getProduct().getName() != null) ? log.getProduct().getName() + " (ID:" + log.getProduct().getProductId() + ")" : (log.getProduct() != null ? "ID:" + log.getProduct().getProductId() : "N/A");
                    String userName = (log.getUser() != null && log.getUser().getUsername() != null) ? log.getUser().getUsername() + " (ID:" + log.getUser().getUserId() + ")" : (log.getUser() != null ? "ID:" + log.getUser().getUserId() : "N/A");
                    String timestampStr = log.getTimestamp() != null ? DATE_FORMAT.format(log.getTimestamp()) : "N/A";
                    writer.write(String.format("%-7d | %-30.30s | %-25.25s | %-20.20s | %-25.25s | %-20s | %s",
                            log.getLogId(), productName, userName,
                            log.getAction(),
                            log.getLocation() != null ? log.getLocation() : "",
                            timestampStr,
                            log.getDetails() != null ? log.getDetails().replace("\n", " ") : "")); // Replace newlines in details for single line
                    writer.newLine();
                }
                writer.write("========================================================================================================================================================"); writer.newLine();
                writer.write("End of Report."); writer.newLine();
                JOptionPane.showMessageDialog(this, "Report saved: " + fileToSave.getAbsolutePath(), "Report Saved", JOptionPane.INFORMATION_MESSAGE);
                if (Desktop.isDesktopSupported()) Desktop.getDesktop().open(fileToSave);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving report: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testUser = new User();
            testUser.setUserId(1); // Example user ID
            testUser.setUsername("testUser");

            Product testProduct = new Product();
            testProduct.setProductId(101); // Example product ID
            testProduct.setName("Organic Apples");

            // Test with Product object
            // new ProductUserLogListForm(testUser, testProduct).setVisible(true);

            // Test with Product ID
            new ProductUserLogListForm(testUser, 102).setVisible(true);
        });
    }
}
