package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Product;
import model.User;
import service.ProductInterface;
import util2.RmiClientUtil;

public class ProductForm extends JFrame {

    private User currentUser;
    private Product productToEdit; // Null if adding a new product
    private ProductListForm productListFormInstance; // To call refresh
    private ProductInterface productService;

    private JTextField txtName, txtBatchNumber, txtOrigin, txtCreatedDate;
    private JButton btnSave, btnCancel;
    private boolean isEditMode = false;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Constructor for Adding
    public ProductForm(User currentUser, ProductListForm listForm) {
        this(currentUser, null, listForm); // Calls the main constructor
    }

    // Main Constructor (for Editing/Adding)
    public ProductForm(User currentUser, Product productToEdit, ProductListForm listForm) {
        this.currentUser = currentUser;
        this.productToEdit = productToEdit;
        this.productListFormInstance = listForm;

        if (this.productToEdit != null) {
            isEditMode = true;
        }

        try {
            productService = RmiClientUtil.getProductService();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to Product Service: " + e.getMessage(),
                                          "Connection Error", JOptionPane.ERROR_MESSAGE);
            // Consider disabling form or parts of it if service is crucial from start
        }

        initComponents();
        setTitle(isEditMode ? "Edit Product" : "Add New Product");

        if (isEditMode) {
            populateFormFields();
        } else {
            // For new products, set current date as created date (non-editable)
            txtCreatedDate.setText(DATE_FORMAT.format(new Date()));
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private void initComponents() {
        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 240)); // Light background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel lblFormTitle = new JLabel(isEditMode ? "Edit Product Details" : "Add New Product", SwingConstants.CENTER);
        lblFormTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblFormTitle.setForeground(new Color(50,50,50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(lblFormTitle, gbc);

        gbc.gridwidth = 1; // Reset
        gbc.anchor = GridBagConstraints.WEST; // Reset

        // Input Fields
        Font labelFont = new Font("Tahoma", Font.BOLD, 13);
        Font textFont = new Font("Tahoma", Font.PLAIN, 13);
        Dimension textFieldSize = new Dimension(250, 28);

        // Name
        JLabel lblName = new JLabel("Name:");
        lblName.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 1; mainPanel.add(lblName, gbc);
        txtName = new JTextField();
        txtName.setFont(textFont);
        txtName.setPreferredSize(textFieldSize);
        txtName.setToolTipText("Enter the product name.");
        gbc.gridx = 1; gbc.gridy = 1; mainPanel.add(txtName, gbc);

        // Batch Number
        JLabel lblBatchNumber = new JLabel("Batch Number:");
        lblBatchNumber.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 2; mainPanel.add(lblBatchNumber, gbc);
        txtBatchNumber = new JTextField();
        txtBatchNumber.setFont(textFont);
        txtBatchNumber.setPreferredSize(textFieldSize);
        txtBatchNumber.setToolTipText("Enter the product batch number.");
        gbc.gridx = 1; gbc.gridy = 2; mainPanel.add(txtBatchNumber, gbc);

        // Origin
        JLabel lblOrigin = new JLabel("Origin:");
        lblOrigin.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 3; mainPanel.add(lblOrigin, gbc);
        txtOrigin = new JTextField();
        txtOrigin.setFont(textFont);
        txtOrigin.setPreferredSize(textFieldSize);
        txtOrigin.setToolTipText("Enter the product origin.");
        gbc.gridx = 1; gbc.gridy = 3; mainPanel.add(txtOrigin, gbc);

        // Created Date
        JLabel lblCreatedDate = new JLabel("Created Date:");
        lblCreatedDate.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 4; mainPanel.add(lblCreatedDate, gbc);
        txtCreatedDate = new JTextField();
        txtCreatedDate.setFont(textFont);
        txtCreatedDate.setPreferredSize(textFieldSize);
        txtCreatedDate.setEditable(false);
        txtCreatedDate.setToolTipText("Creation date of the product (auto-set for new).");
        txtCreatedDate.setBackground(new Color(230,230,230)); // Indicate non-editable
        gbc.gridx = 1; gbc.gridy = 4; mainPanel.add(txtCreatedDate, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false); // Transparent background
        Dimension buttonSize = new Dimension(100, 30);

        btnSave = new JButton("Save");
        btnSave.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnSave.setPreferredSize(buttonSize);
        btnSave.setBackground(new Color(50, 150, 50)); // Greenish
        btnSave.setForeground(Color.WHITE);
        buttonPanel.add(btnSave);

        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnCancel.setPreferredSize(buttonSize);
        btnCancel.setBackground(new Color(150, 50, 50)); // Reddish
        btnCancel.setForeground(Color.WHITE);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(15, 5, 5, 5); // Top margin
        mainPanel.add(buttonPanel, gbc);

        // Action Listeners
        btnSave.addActionListener(e -> saveProduct());
        btnCancel.addActionListener(e -> dispose());

        add(mainPanel);
    }

    private void populateFormFields() {
        if (productToEdit != null) {
            txtName.setText(productToEdit.getName());
            txtBatchNumber.setText(productToEdit.getBatchNumber());
            txtOrigin.setText(productToEdit.getOrigin());
            // Assuming getRegistrationDate() returns a String formatted as "yyyy-MM-dd HH:mm:ss"
            // If it's a Date object, it should be formatted: DATE_FORMAT.format(productToEdit.getRegistrationDate())
            txtCreatedDate.setText(productToEdit.getRegistrationDate() != null ? productToEdit.getRegistrationDate() : "N/A");
        }
    }

    private void saveProduct() {
        String name = txtName.getText().trim();
        String batchNumber = txtBatchNumber.getText().trim();
        String origin = txtOrigin.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Product Name is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtName.requestFocus();
            return;
        }
        if (batchNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Batch Number is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtBatchNumber.requestFocus();
            return;
        }
        if (origin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Origin is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtOrigin.requestFocus();
            return;
        }

        if (productService == null) {
            try {
                productService = RmiClientUtil.getProductService();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Product service is not available. Please check connection.", "Service Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (productService == null) { // Still null after trying
             JOptionPane.showMessageDialog(this, "Product service is not available.", "Service Error", JOptionPane.ERROR_MESSAGE);
             return;
        }


        try {
            String resultMessage;
            if (isEditMode) {
                productToEdit.setName(name);
                productToEdit.setBatchNumber(batchNumber);
                productToEdit.setOrigin(origin);
                // productToEdit.setRegistrationDate() is not typically changed after creation.
                // If user association needs update, handle here: productToEdit.setUsers(currentUser);
                resultMessage = productService.updateProduct(productToEdit);
            } else { // Adding new product
                Product newProduct = new Product();
                newProduct.setName(name);
                newProduct.setBatchNumber(batchNumber);
                newProduct.setOrigin(origin);
                if (currentUser != null) { // Should always be true if form opened correctly
                    newProduct.setUsers(currentUser);
                } else {
                     JOptionPane.showMessageDialog(this, "Error: No user session found for creating product.", "User Error", JOptionPane.ERROR_MESSAGE);
                     return;
                }
                newProduct.setRegistrationDate(DATE_FORMAT.format(new Date())); // Set current date/time as string
                // Assuming QR code is generated/set by server or another process, not part of this form directly.
                // If it needs to be set here, add a field for it.
                resultMessage = productService.registerProduct(newProduct);
            }

            JOptionPane.showMessageDialog(this, resultMessage);

            if (resultMessage != null && resultMessage.toLowerCase().contains("success")) {
                if (productListFormInstance != null) {
                    productListFormInstance.loadProducts(); // Refresh list in ProductListForm
                }
                dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving product: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Dummy User for testing
            User testUser = new User();
            testUser.setUserId(1); // Ensure ID is set if User object equality relies on it
            testUser.setUsername("testuser");
            testUser.setRole("admin");

            // Dummy ProductListForm for testing
            // In a real scenario, ProductListForm would already exist or be null if not needed.
            // For testing ProductForm in isolation, we might not need a full ProductListForm.
            // But if its `loadProducts` is crucial, a stub might be needed.
            // For this test, we can pass null or a simple instance if listForm can handle null.
            // Let's assume listForm can be null for this isolated test.
             ProductListForm dummyListForm = null;
            // If ProductListForm constructor needs a user:
            // ProductListForm dummyListForm = new ProductListForm(testUser); // This would open two windows for testing.
            // dummyListForm.setVisible(false); // Keep it hidden if only for refresh callback.

            // Test Add Mode
            // new ProductForm(testUser, dummyListForm).setVisible(true);

            // Test Edit Mode
            Product sampleProduct = new Product();
            sampleProduct.setProductId(101);
            sampleProduct.setName("Sample Apple");
            sampleProduct.setBatchNumber("B001");
            sampleProduct.setOrigin("Local Farm");
            sampleProduct.setRegistrationDate(DATE_FORMAT.format(new Date()));
            sampleProduct.setUsers(testUser);
            new ProductForm(testUser, sampleProduct, dummyListForm).setVisible(true);
        });
    }
}
