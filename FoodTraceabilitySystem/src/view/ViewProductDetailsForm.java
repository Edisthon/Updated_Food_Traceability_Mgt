package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;
import model.Product;
import model.ProductStatusLog;
import model.User;
import service.ProductInterface;
import service.ProductStatusLogInterface;
import util2.RmiClientUtil;

public class ViewProductDetailsForm extends javax.swing.JFrame {

    private User currentUser;

    // UI Components
    private JLabel lblTitle, lblProductIdSearch;
    private JLabel lblProductNameLabel, lblProductNameData;
    private JLabel lblOriginLabel, lblOriginData;
    private JLabel lblQrCodeLabel, lblQrCodeData;
    private JLabel lblStatusHistoryTitle;

    private JTextField txtProductIdSearch;
    private JButton btnSearch, btnClose;

    private JTable tableStatusHistory;
    private JScrollPane scrollPaneForTable;
    private DefaultTableModel tableModel;

    public ViewProductDetailsForm(User currentUser) {
        this.currentUser = currentUser;
        initComponents();
    }

    private void initComponents() {
        setTitle("View Product Details & History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new Dimension(600, 700)); // Adjusted size for more content

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(102, 102, 102)); // Consistent background

        // Title
        lblTitle = new JLabel("View Product Details and History", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Top Panel for Search and Details
        JPanel topContentPanel = new JPanel(new BorderLayout(10,10));
        topContentPanel.setOpaque(false);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE), "Search Product", 0, 0,
            new Font("Tahoma", Font.BOLD, 14), Color.WHITE));
        searchPanel.setOpaque(false);

        lblProductIdSearch = new JLabel("Enter Product ID:");
        lblProductIdSearch.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblProductIdSearch.setForeground(Color.WHITE);
        txtProductIdSearch = new JTextField(15);
        txtProductIdSearch.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtProductIdSearch.setToolTipText("Enter the numeric ID of the product to search.");
        btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnSearch.setToolTipText("Click to search for the product.");

        searchPanel.add(lblProductIdSearch);
        searchPanel.add(txtProductIdSearch);
        searchPanel.add(btnSearch);
        topContentPanel.add(searchPanel, BorderLayout.NORTH);

        // Details Panel
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE), "Product Information", 0, 0,
            new Font("Tahoma", Font.BOLD, 14), Color.WHITE));
        detailsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        Font labelFont = new Font("Tahoma", Font.BOLD, 13);
        Font dataFont = new Font("Tahoma", Font.PLAIN, 13);
        Color labelColor = Color.WHITE;

        lblProductNameLabel = new JLabel("Product Name:");
        lblProductNameLabel.setFont(labelFont);
        lblProductNameLabel.setForeground(labelColor);
        lblProductNameData = new JLabel("-");
        lblProductNameData.setFont(dataFont);
        lblProductNameData.setForeground(labelColor);

        lblOriginLabel = new JLabel("Origin:");
        lblOriginLabel.setFont(labelFont);
        lblOriginLabel.setForeground(labelColor);
        lblOriginData = new JLabel("-");
        lblOriginData.setFont(dataFont);
        lblOriginData.setForeground(labelColor);

        lblQrCodeLabel = new JLabel("QR Code Data:");
        lblQrCodeLabel.setFont(labelFont);
        lblQrCodeLabel.setForeground(labelColor);
        lblQrCodeData = new JLabel("-");
        lblQrCodeData.setFont(dataFont);
        lblQrCodeData.setForeground(labelColor);

        gbc.gridx = 0; gbc.gridy = 0; detailsPanel.add(lblProductNameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; detailsPanel.add(lblProductNameData, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; // Reset

        gbc.gridx = 0; gbc.gridy = 1; detailsPanel.add(lblOriginLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; detailsPanel.add(lblOriginData, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;

        gbc.gridx = 0; gbc.gridy = 2; detailsPanel.add(lblQrCodeLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; detailsPanel.add(lblQrCodeData, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;

        topContentPanel.add(detailsPanel, BorderLayout.CENTER);
        mainPanel.add(topContentPanel, BorderLayout.CENTER);


        // Status History Panel
        JPanel statusHistoryPanel = new JPanel(new BorderLayout(5, 5));
        statusHistoryPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE), "Status History", 0, 0,
            new Font("Tahoma", Font.BOLD, 14), Color.WHITE));
        statusHistoryPanel.setOpaque(false);

        lblStatusHistoryTitle = new JLabel("Product Status History Log", SwingConstants.CENTER); // Already in border title
        lblStatusHistoryTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblStatusHistoryTitle.setForeground(Color.WHITE);
        // statusHistoryPanel.add(lblStatusHistoryTitle, BorderLayout.NORTH); // Title provided by border

        tableModel = new DefaultTableModel(new Object[]{"Timestamp", "Location", "Temperature (°C)", "Humidity (%)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        tableStatusHistory = new JTable(tableModel);
        tableStatusHistory.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tableStatusHistory.setFillsViewportHeight(true); // Use available space
        tableStatusHistory.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
        scrollPaneForTable = new JScrollPane(tableStatusHistory);
        statusHistoryPanel.add(scrollPaneForTable, BorderLayout.CENTER);

        // Add Status History Panel to a new JPanel that uses a larger portion of the screen
        JPanel bottomHalfPanel = new JPanel(new BorderLayout());
        bottomHalfPanel.setOpaque(false);
        bottomHalfPanel.add(statusHistoryPanel, BorderLayout.CENTER);
        bottomHalfPanel.setPreferredSize(new Dimension(580, 250)); // Give more space to table


        // Main content panel combining top (search+details) and bottom (history table)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.add(topContentPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        contentPanel.add(bottomHalfPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);


        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setOpaque(false);
        btnClose = new JButton("Close");
        btnClose.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnClose.setToolTipText("Close this window.");
        buttonPanel.add(btnClose);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add Action Listeners
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnSearchActionPerformed(e);
            }
        });

        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnCloseActionPerformed(e);
            }
        });

        // Set default button for the frame
        JRootPane rootPane = SwingUtilities.getRootPane(btnSearch);
        if (rootPane != null) {
            rootPane.setDefaultButton(btnSearch);
        }

        add(mainPanel);
        pack();
        setLocationRelativeTo(null); // Center on screen
    }

    private void btnSearchActionPerformed(ActionEvent e) {
        String productIdStr = txtProductIdSearch.getText().trim();
        if (productIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Product ID cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtProductIdSearch.requestFocus();
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(productIdStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Product ID must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtProductIdSearch.requestFocus();
            return;
        }

        // Clear previous data
        lblProductNameData.setText("-");
        lblOriginData.setText("-");
        lblQrCodeData.setText("-");
        tableModel.setRowCount(0);

        try {
            ProductInterface productService = RmiClientUtil.getProductService();
            ProductStatusLogInterface statusLogService = RmiClientUtil.getProductStatusLogService();

            if (productService == null || statusLogService == null) {
                JOptionPane.showMessageDialog(this, "Could not connect to RMI services. Please check server connection.", "RMI Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product productToSearch = new Product();
            productToSearch.setProductId(productId); // Assuming Product model has setProductId
            Product fetchedProduct = productService.retrieveById(productToSearch);

            if (fetchedProduct != null) {
                lblProductNameData.setText(fetchedProduct.getName() != null ? fetchedProduct.getName() : "-");
                lblOriginData.setText(fetchedProduct.getOrigin() != null ? fetchedProduct.getOrigin() : "-");
                lblQrCodeData.setText(fetchedProduct.getQrCode() != null ? fetchedProduct.getQrCode() : "-");

                List<ProductStatusLog> logs = statusLogService.getLogsByProductId(productId);
                if (logs != null && !logs.isEmpty()) {
                    for (ProductStatusLog log : logs) {
                        Object[] row = {
                                log.getTimestamp(),
                                log.getLocation(),
                                log.getTemperature(), // Assuming these getters exist
                                log.getHumidity()
                        };
                        tableModel.addRow(row);
                    }
                } else {
                    // Optionally, show a message in the table area or a small popup
                     tableModel.addRow(new Object[]{"No status logs found for this product.", "", "", ""});
                }
            } else {
                JOptionPane.showMessageDialog(this, "Product with ID " + productId + " not found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "RMI Error: " + ex.getMessage(), "Service Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void btnCloseActionPerformed(ActionEvent e) {
        if (this.currentUser != null) {
            new AdminDashboard(this.currentUser).setVisible(true);
        }
        // If currentUser is null, it will simply dispose as AdminDashboard won't be created.
        // Alternative for null currentUser:
        // else {
        //     new LoginForm().setVisible(true); // Or some other default action
        // }
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Example with a dummy user for testing
                User testUser = new User();
                testUser.setUsername("testUser");
                new ViewProductDetailsForm(testUser).setVisible(true);
            }
        });
    }
}
