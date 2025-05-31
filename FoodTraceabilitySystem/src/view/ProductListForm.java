package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.BufferedWriter; // Added
import java.io.File; // Added
import java.io.FileWriter; // Added
import java.io.IOException; // Added
import javax.swing.JFileChooser; // Added
import javax.swing.filechooser.FileNameExtensionFilter; // Added for filter
import java.awt.Desktop; // Added to open file

import model.Product;
import model.User;
import service.ProductInterface;
import util2.RmiClientUtil;

public class ProductListForm extends JFrame {

    private User currentUser;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnViewLogs, btnSearch, btnRefresh, btnGeneratePdf, btnBack;
    private JTextField txtSearchField;
    private ProductInterface productService;
    private List<Product> productList = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public ProductListForm(User user) {
        this.currentUser = user;

        try {
            productService = RmiClientUtil.getProductService();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to Product Service: " + e.getMessage(),
                                          "Connection Error", JOptionPane.ERROR_MESSAGE);
            // Disable buttons if service is not available
        }

        initComponents();
        setTitle("Manage Products - Food Traceability System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose on close to allow dashboard to reopen
        setResizable(true);
        setMinimumSize(new Dimension(800, 500)); // Set a reasonable minimum size
        setLocationRelativeTo(null);

        if (productService != null) {
            loadProducts();
        } else {
            // Optionally disable more components if service is unavailable
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnViewLogs.setEnabled(false);
            btnSearch.setEnabled(false);
            btnRefresh.setEnabled(false);
            btnGeneratePdf.setEnabled(false);
        }
        setVisible(true);
    }

    private void initComponents() {
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Top Panel (Search & Title)
        JPanel topPanel = new JPanel(new BorderLayout(5,5));
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Product Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setForeground(new Color(50, 50, 50));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Search:"));
        txtSearchField = new JTextField(25);
        txtSearchField.setFont(new Font("Tahoma", Font.PLAIN, 13));
        searchPanel.add(txtSearchField);

        btnSearch = new JButton("Search");
        styleButton(btnSearch, "Search products by name, batch, or ID.");
        searchPanel.add(btnSearch);

        btnRefresh = new JButton("Refresh List");
        styleButton(btnRefresh, "Reload all products from the server.");
        searchPanel.add(btnRefresh);

        topPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center Panel (Table)
        String[] columnNames = {"ID", "Name", "Batch Number", "Origin", "Created Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.getTableHeader().setReorderingAllowed(false);
        productTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
        productTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        productTable.setRowHeight(25);
        productTable.setFillsViewportHeight(true);


        JScrollPane scrollPane = new JScrollPane(productTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel (Action Buttons)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Changed to CENTER
        buttonPanel.setOpaque(false);

        btnAdd = new JButton("Add Product");
        styleButton(btnAdd, "Add a new product to the system.");
        buttonPanel.add(btnAdd);

        btnEdit = new JButton("Edit Selected");
        styleButton(btnEdit, "Edit details of the selected product.");
        buttonPanel.add(btnEdit);

        btnDelete = new JButton("Delete Selected");
        styleButton(btnDelete, "Delete the selected product from the system.");
        btnDelete.setBackground(new Color(220, 53, 69)); // Red for delete
        btnDelete.setForeground(Color.WHITE);
        buttonPanel.add(btnDelete);

        btnViewLogs = new JButton("View Activity Logs");
        styleButton(btnViewLogs, "View activity and status history for the selected product.");
        buttonPanel.add(btnViewLogs);

        btnGeneratePdf = new JButton("Generate PDF Report");
        // Tooltip updated directly here as it's specific to this button's new functionality
        btnGeneratePdf.setToolTipText("Generate a text report of the product list (.txt)");
        styleButton(btnGeneratePdf, btnGeneratePdf.getToolTipText()); // Pass the updated tooltip
        buttonPanel.add(btnGeneratePdf);

        btnBack = new JButton("Back to Dashboard");
        styleButton(btnBack, "Return to the main dashboard.");
        buttonPanel.add(btnBack);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        addActionListeners(); // Call to attach action listeners
        pack(); // Pack before setting minimum size if components determine it
    }

    private void styleButton(JButton button, String tooltip) {
        button.setFont(new Font("Tahoma", Font.BOLD, 12));
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(180, 30)); // Consistent button size
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
    }

    public void loadProducts() { // Changed visibility to public
        loadProducts(null);
    }

    private void loadProducts(String searchTerm) {
        tableModel.setRowCount(0); // Clear existing data
        if (productService == null) {
             try { // Attempt to reconnect if service was null
                productService = RmiClientUtil.getProductService();
             } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to connect to Product Service: " + e.getMessage(),
                                              "Connection Error", JOptionPane.ERROR_MESSAGE);
                return;
             }
        }
        if (productService == null) { // Still null after trying
             JOptionPane.showMessageDialog(this, "Product service is not available.", "Service Error", JOptionPane.ERROR_MESSAGE);
             return;
        }


        try {
            productList = productService.retreiveAll();
            List<Product> displayList = new ArrayList<>();

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String lowerSearchTerm = searchTerm.toLowerCase().trim();
                for (Product p : productList) {
                    // Assuming Product model has getProductId(), getName(), getBatchNumber()
                    if (String.valueOf(p.getProductId()).contains(lowerSearchTerm) ||
                        (p.getName() != null && p.getName().toLowerCase().contains(lowerSearchTerm)) ||
                        (p.getBatchNumber() != null && p.getBatchNumber().toLowerCase().contains(lowerSearchTerm))) {
                        displayList.add(p);
                    }
                }
            } else {
                displayList.addAll(productList);
            }

            if (displayList != null && !displayList.isEmpty()) {
                for (Product product : displayList) {
                    // Using getRegistrationDate() as per Product model, assuming it's a String
                    String registrationDateStr = product.getRegistrationDate() != null ? product.getRegistrationDate() : "N/A";
                    tableModel.addRow(new Object[]{
                            product.getProductId(),
                            product.getName(),
                            product.getBatchNumber() != null ? product.getBatchNumber() : "N/A",
                            product.getOrigin() != null ? product.getOrigin() : "N/A",
                            registrationDateStr // Use the string date directly
                    });
                }
            } else if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                 tableModel.addRow(new Object[]{"No products found matching your search term.", "", "", "", "", ""}); // Adjusted for 5 columns
            } else {
                 tableModel.addRow(new Object[]{"No products found in the system.", "", "", "", ""});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage(), "Loading Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Action Listeners
    private void addActionListeners() { // Call this in initComponents or constructor
        btnSearch.addActionListener(e -> loadProducts(txtSearchField.getText()));
        btnRefresh.addActionListener(e -> loadProducts());
        btnBack.addActionListener(e -> {
            if(currentUser != null) new DashboardForm(currentUser).setVisible(true);
            else new LoginForm().setVisible(true); // Fallback if no current user
            dispose();
        });

        btnAdd.addActionListener(e -> {
            if (productService == null) {
                JOptionPane.showMessageDialog(this, "Product service not available.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            new ProductForm(currentUser, this).setVisible(true);
            // loadProducts() will be called by ProductForm upon successful save
        });

        btnEdit.addActionListener(e -> {
            if (productService == null) {
                JOptionPane.showMessageDialog(this, "Product service not available.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a product to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Product productToEdit = null;
            Object idObj = productTable.getValueAt(selectedRow, 0); // ID is in column 0
            if (idObj != null) {
                int productId = (Integer) idObj;
                for (Product p : productList) { // productList should be populated by loadProducts()
                    if (p.getProductId() == productId) {
                        productToEdit = p;
                        break;
                    }
                }
            }

            if (productToEdit != null) {
                new ProductForm(currentUser, productToEdit, this).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Could not find the selected product details to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                // This might happen if productList is not in sync or product was deleted.
                loadProducts(); // Refresh the list to reflect current state
            }
        });

        btnDelete.addActionListener(e -> {
            if (productService == null) {
                JOptionPane.showMessageDialog(this, "Product service not available.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a product to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Product productToDelete = null;
            Object idObj = productTable.getValueAt(selectedRow, 0);
            String productName = "";
            if (idObj != null) {
                int productId = (Integer) idObj;
                productName = productTable.getValueAt(selectedRow, 1).toString(); // Get name for confirmation
                for (Product p : productList) {
                    if (p.getProductId() == productId) {
                        productToDelete = p;
                        break;
                    }
                }
            }

            if (productToDelete != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete product: " + productName + " (ID: " + productToDelete.getProductId() + ")?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        String result = productService.deleteProduct(productToDelete); // Assuming deleteProduct takes Product object
                        JOptionPane.showMessageDialog(this, result);
                        if (result != null && result.toLowerCase().contains("success")) {
                            loadProducts(); // Refresh list
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error deleting product: " + ex.getMessage(), "Delete Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                 JOptionPane.showMessageDialog(this, "Could not find the selected product to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                 loadProducts(); // Refresh
            }
        });

        btnViewLogs.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a product to view its logs.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Product selectedProductToViewLogs = null;
            Object idObj = productTable.getValueAt(selectedRow, 0); // Assuming ID is in column 0

            if (idObj != null) {
                int productId = (Integer) idObj;
                // Find the product in the local productList (which should be up-to-date)
                for (Product p : productList) { // productList is a class member holding all products
                    if (p.getProductId() == productId) {
                        selectedProductToViewLogs = p;
                        break;
                    }
                }
            }

            if (selectedProductToViewLogs != null) {
                // Pass the currentUser and the full Product object
                new ProductUserLogListForm(currentUser, selectedProductToViewLogs).setVisible(true);
                // DO NOT dispose ProductListForm here, user might want to view logs for another product.
            } else {
                JOptionPane.showMessageDialog(this, "Could not find the details of the selected product.", "Error", JOptionPane.ERROR_MESSAGE);
                // Optionally, refresh the list if this happens, as it might indicate a data sync issue.
                // loadProducts();
            }
        });

        btnGeneratePdf.addActionListener(e -> {
            if (productList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No product data to generate a report.", "Empty List", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Product Report");
            fileChooser.setSelectedFile(new File("products_report.txt")); // Default file name
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                // Ensure the file has a .txt extension
                if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                    fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".txt");
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                    // Write Header
                    writer.write("Product Report - Food Traceability System");
                    writer.newLine();
                    writer.write("Generated on: " + new java.util.Date().toString());
                    writer.newLine();
                    writer.write("========================================================================================================");
                    writer.newLine();
                    // Column Headers - match JTable but use fixed-width formatting for alignment
                    writer.write(String.format("%-10s | %-30s | %-20s | %-30s | %-20s",
                                               "ID", "Name", "Batch Number", "Origin", "Created Date"));
                    writer.newLine();
                    writer.write("--------------------------------------------------------------------------------------------------------");
                    writer.newLine();

                    // Write Data from productList (which backs the table)
                    for (Product product : productList) {
                        writer.write(String.format("%-10d | %-30s | %-20s | %-30s | %-20s",
                                product.getProductId(),
                                product.getName() != null ? product.getName() : "",
                                product.getBatchNumber() != null ? product.getBatchNumber() : "",
                                product.getOrigin() != null ? product.getOrigin() : "",
                                product.getRegistrationDate() != null ? product.getRegistrationDate() : ""));
                        writer.newLine();
                    }
                    writer.write("========================================================================================================");
                    writer.newLine();
                    writer.write("End of Report.");
                    writer.newLine();

                    JOptionPane.showMessageDialog(this, "Report saved successfully to: " + fileToSave.getAbsolutePath(), "Report Saved", JOptionPane.INFORMATION_MESSAGE);

                    // Optionally, try to open the file
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().open(fileToSave);
                        } catch (IOException ex) {
                            // Silently ignore if can't open, or show a small error.
                            System.err.println("Could not open the report file: " + ex.getMessage());
                        }
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error saving report: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Make sure to call addActionListeners in constructor or at the end of initComponents
    // For example, at the end of constructor:
    // public ProductListForm(User user) { ... addActionListeners(); setVisible(true); }
    // Or at the end of initComponents:
    // private void initComponents() { ... add(mainPanel); addActionListeners(); pack(); }
    // Correct placement for addActionListeners() call in this structure:
    // Call it within the ProductListForm constructor, after initComponents().
    // public ProductListForm(User user) { ... initComponents(); addActionListeners(); ... }
    // Or at the end of initComponents() before pack()
    // private void initComponents() { ... mainPanel.add(buttonPanel, BorderLayout.SOUTH); add(mainPanel); addActionListeners(); pack(); }
    // Let's add it to constructor after initComponents()

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testUser = new User();
            testUser.setUserId(1);
            testUser.setUsername("testuser");
            testUser.setRole("admin"); // or "user"
            new ProductListForm(testUser);
        });
    }
}
