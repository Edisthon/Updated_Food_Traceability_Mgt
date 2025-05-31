package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.Product;
import model.User;
import model.ProductUserLog;
import service.ProductInterface;
import service.UserInterface;
import service.ProductUserLogInterface;
import util2.RmiClientUtil;

public class ProductUserLogForm extends JFrame {

    private User currentUser;
    private ProductUserLogListForm logListFormInstance; // To call refresh
    private ProductUserLogInterface logService;
    private ProductInterface productService;
    private UserInterface userService;

    private JComboBox<ProductWrapper> comboProduct;
    private JComboBox<UserWrapper> comboUser;
    private JComboBox<String> comboAction;
    private JTextField txtLocation, txtTimestamp;
    private JTextArea areaDetails;
    private JButton btnSaveLog, btnCancel;
    private static final SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Static inner class ProductWrapper
    static class ProductWrapper {
        Product product;
        public ProductWrapper(Product product) { this.product = product; }
        public Product getProduct() { return product; }
        @Override public String toString() {
            return product != null ? product.getName() + " (ID:" + product.getProductId() + ")" : "Select Product";
        }
        // equals and hashCode are important for JComboBox to correctly find and select items if objects are re-added
        @Override public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            ProductWrapper that = (ProductWrapper) obj;
            if (product == null) return that.product == null;
            return product.getProductId() == that.product.getProductId();
        }
        @Override public int hashCode() {
            return product != null ? Integer.hashCode(product.getProductId()) : 0;
        }
    }

    // Static inner class UserWrapper
    static class UserWrapper {
        User user;
        public UserWrapper(User user) { this.user = user; }
        public User getUser() { return user; }
        @Override public String toString() {
            return user != null ? user.getUsername() + " (ID:" + user.getUserId() + ")" : "Select User";
        }
         @Override public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            UserWrapper that = (UserWrapper) obj;
            if (user == null) return that.user == null;
            return user.getUserId() == that.user.getUserId();
        }
        @Override public int hashCode() {
            return user != null ? Integer.hashCode(user.getUserId()) : 0;
        }
    }

    public ProductUserLogForm(User currentUser, ProductUserLogListForm listForm) {
        this.currentUser = currentUser;
        this.logListFormInstance = listForm;

        try {
            logService = RmiClientUtil.getProductUserLogService();
            productService = RmiClientUtil.getProductService();
            userService = RmiClientUtil.getUserService();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to required services: " + e.getMessage(),
                                          "Connection Error", JOptionPane.ERROR_MESSAGE);
            // Consider disabling form or parts of it
        }

        initComponents();
        populateComboBoxes();

        setTitle("Add New Product Activity Log");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false); // Or true if content might vary a lot
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblFormTitle = new JLabel("Log New Product Activity", SwingConstants.CENTER);
        lblFormTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(lblFormTitle, gbc);

        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        Font labelFont = new Font("Tahoma", Font.BOLD, 13);
        Dimension fieldDim = new Dimension(250, 28);

        // Product
        JLabel lblProduct = new JLabel("Product:");
        lblProduct.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 1; mainPanel.add(lblProduct, gbc);
        comboProduct = new JComboBox<>();
        comboProduct.setPreferredSize(fieldDim);
        gbc.gridx = 1; gbc.gridy = 1; mainPanel.add(comboProduct, gbc);

        // User
        JLabel lblUser = new JLabel("User:");
        lblUser.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 2; mainPanel.add(lblUser, gbc);
        comboUser = new JComboBox<>();
        comboUser.setPreferredSize(fieldDim);
        gbc.gridx = 1; gbc.gridy = 2; mainPanel.add(comboUser, gbc);

        // Action
        JLabel lblAction = new JLabel("Action:");
        lblAction.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 3; mainPanel.add(lblAction, gbc);
        comboAction = new JComboBox<>(new String[]{"Select Action", "Registered", "Status Updated", "Location Changed", "Transported", "Received", "Processed", "Packaged", "Sold", "Other"});
        comboAction.setPreferredSize(fieldDim);
        gbc.gridx = 1; gbc.gridy = 3; mainPanel.add(comboAction, gbc);

        // Location
        JLabel lblLocation = new JLabel("Location:");
        lblLocation.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 4; mainPanel.add(lblLocation, gbc);
        txtLocation = new JTextField();
        txtLocation.setPreferredSize(fieldDim);
        gbc.gridx = 1; gbc.gridy = 4; mainPanel.add(txtLocation, gbc);

        // Timestamp
        JLabel lblTimestamp = new JLabel("Timestamp:");
        lblTimestamp.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 5; mainPanel.add(lblTimestamp, gbc);
        txtTimestamp = new JTextField(DATE_TIME_FORMATTER.format(new Date()));
        txtTimestamp.setPreferredSize(fieldDim);
        txtTimestamp.setToolTipText("Format: yyyy-MM-dd HH:mm:ss");
        gbc.gridx = 1; gbc.gridy = 5; mainPanel.add(txtTimestamp, gbc);

        // Details
        JLabel lblDetails = new JLabel("Details:");
        lblDetails.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 6; gbc.anchor = GridBagConstraints.NORTHWEST; mainPanel.add(lblDetails, gbc);
        areaDetails = new JTextArea(5, 20);
        areaDetails.setLineWrap(true);
        areaDetails.setWrapStyleWord(true);
        JScrollPane detailsScrollPane = new JScrollPane(areaDetails);
        detailsScrollPane.setPreferredSize(new Dimension(250, 100));
        gbc.gridx = 1; gbc.gridy = 6; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0; mainPanel.add(detailsScrollPane, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weighty = 0; // Reset

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        Dimension buttonSize = new Dimension(100, 30);

        btnSaveLog = new JButton("Save Log");
        btnSaveLog.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnSaveLog.setPreferredSize(buttonSize);
        buttonPanel.add(btnSaveLog);

        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnCancel.setPreferredSize(buttonSize);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(15, 5, 5, 5);
        mainPanel.add(buttonPanel, gbc);

        btnSaveLog.addActionListener(e -> saveLogEntry());
        btnCancel.addActionListener(e -> dispose());

        add(mainPanel);
    }

    private void populateComboBoxes() {
        // Populate Products
        comboProduct.addItem(new ProductWrapper(null)); // Add a "Select Product" option
        if (productService != null) {
            try {
                List<Product> products = productService.retreiveAll();
                if (products != null) {
                    for (Product p : products) {
                        comboProduct.addItem(new ProductWrapper(p));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage(), "Service Error", JOptionPane.WARNING_MESSAGE);
            }
        }

        // Populate Users
        comboUser.addItem(new UserWrapper(null)); // Add a "Select User" option
        UserWrapper currentUserWrapper = null;
        if (userService != null) {
            try {
                List<User> users = userService.retreiveAll();
                if (users != null) {
                    for (User u : users) {
                        UserWrapper uw = new UserWrapper(u);
                        comboUser.addItem(uw);
                        if (this.currentUser != null && u.getUserId() == this.currentUser.getUserId()) {
                            currentUserWrapper = uw;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(), "Service Error", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (currentUserWrapper != null) {
            comboUser.setSelectedItem(currentUserWrapper);
        } else if (this.currentUser != null) {
            // If current user wasn't in the list (e.g. list is empty or service failed partly), add them manually
            comboUser.addItem(new UserWrapper(this.currentUser));
            comboUser.setSelectedItem(new UserWrapper(this.currentUser));
        }
    }

    private void saveLogEntry() {
        ProductWrapper selectedProductWrapper = (ProductWrapper) comboProduct.getSelectedItem();
        UserWrapper selectedUserWrapper = (UserWrapper) comboUser.getSelectedItem();
        String selectedAction = (String) comboAction.getSelectedItem();
        String location = txtLocation.getText().trim();
        String timestampStr = txtTimestamp.getText().trim();
        String details = areaDetails.getText().trim();

        // Validation
        if (selectedProductWrapper == null || selectedProductWrapper.getProduct() == null) {
            JOptionPane.showMessageDialog(this, "Please select a product.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            comboProduct.requestFocus();
            return;
        }
        if (selectedUserWrapper == null || selectedUserWrapper.getUser() == null) {
            JOptionPane.showMessageDialog(this, "Please select a user.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            comboUser.requestFocus();
            return;
        }
        if (selectedAction == null || "Select Action".equals(selectedAction)) {
            JOptionPane.showMessageDialog(this, "Please select an action.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            comboAction.requestFocus();
            return;
        }
        if (timestampStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Timestamp cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtTimestamp.requestFocus();
            return;
        }

        Date timestamp;
        try {
            timestamp = DATE_TIME_FORMATTER.parse(timestampStr);
        } catch (ParseException pe) {
            JOptionPane.showMessageDialog(this, "Invalid timestamp format. Please use yyyy-MM-dd HH:mm:ss.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtTimestamp.requestFocus();
            return;
        }

        if (logService == null) {
            JOptionPane.showMessageDialog(this, "Log service is not available.", "Service Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProductUserLog newLog = new ProductUserLog();
        newLog.setProduct(selectedProductWrapper.getProduct());
        newLog.setUser(selectedUserWrapper.getUser());
        newLog.setAction(selectedAction);
        newLog.setLocation(location.isEmpty() ? null : location); // Set null if empty
        newLog.setTimestamp(timestamp);
        newLog.setDetails(details.isEmpty() ? null : details); // Set null if empty

        try {
            String result = logService.recordProductUserLog(newLog);
            JOptionPane.showMessageDialog(this, result);
            if (result != null && result.toLowerCase().contains("success")) {
                if (logListFormInstance != null) {
                    logListFormInstance.loadLogs(); // Refresh list
                }
                dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving log entry: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Main method for testing (optional, as this form is context-dependent)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testAdmin = new User(); // Create a dummy admin user
            testAdmin.setUserId(1);
            testAdmin.setUsername("TestAdmin");
            testAdmin.setRole("admin");

            // This form needs a ProductUserLogListForm instance to refresh.
            // For standalone testing, this can be null, but refresh won't work.
            ProductUserLogListForm dummyListForm = null;
            // Example: new ProductUserLogForm(testAdmin, dummyListForm).setVisible(true);
            // Due to RMI dependencies for JComboBox population, direct running might be limited without server.
             JOptionPane.showMessageDialog(null, "This form is best tested via ProductUserLogListForm's 'Add New Log' button once RMI services are running.");
        });
    }
}
