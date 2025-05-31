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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;


import model.User;
import service.UserInterface;
import util2.RmiClientUtil;

public class UserListForm extends JFrame {

    private User currentUser; // The admin user viewing this form
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton btnAddUser, btnEditUser, btnDeleteUser, btnSearchUser, btnRefreshUserList, btnGenerateReport, btnBack;
    private JTextField txtSearchUserField;
    private UserInterface userService;
    private List<User> userList = new ArrayList<>();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public UserListForm(User adminUser) {
        this.currentUser = adminUser;

        // Removed admin role check block

        try {
            userService = RmiClientUtil.getUserService();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to User Service: " + e.getMessage(),
                                          "Connection Error", JOptionPane.ERROR_MESSAGE);
        }

        initComponents(); // Call even if error, to avoid null pointers if dispose is not immediate
        setTitle("Manage Users - Food Traceability System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);

        if (userService != null && currentUser != null) { // Load users if service and user are available, regardless of role
            loadUsers();
            // All buttons remain enabled by default if service is available
        } else {
            // Disable components if service failed or user is somehow null (though constructor expects a user)
            if (btnAddUser != null) btnAddUser.setEnabled(false); // Check for null in case initComponents hasn't run
            if (btnEditUser != null) btnEditUser.setEnabled(false);
            if (btnDeleteUser != null) btnDeleteUser.setEnabled(false);
            if (btnSearchUser != null) btnSearchUser.setEnabled(false);
            if (btnRefreshUserList != null) btnRefreshUserList.setEnabled(false);
            if (btnGenerateReport != null) btnGenerateReport.setEnabled(false);
        }
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));

        JPanel topPanel = new JPanel(new BorderLayout(5,5));
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("User Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setForeground(new Color(50, 50, 50));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Search User:"));
        txtSearchUserField = new JTextField(25);
        txtSearchUserField.setFont(new Font("Tahoma", Font.PLAIN, 13));
        searchPanel.add(txtSearchUserField);

        btnSearchUser = new JButton("Search");
        styleButton(btnSearchUser, "Search users by username or email.");
        searchPanel.add(btnSearchUser);

        btnRefreshUserList = new JButton("Refresh List");
        styleButton(btnRefreshUserList, "Reload all users from the server.");
        searchPanel.add(btnRefreshUserList);

        topPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"User ID", "Username", "Email", "Role", "Created At"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setReorderingAllowed(false);
        userTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
        userTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        userTable.setRowHeight(25);
        userTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(userTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);

        btnAddUser = new JButton("Add User");
        styleButton(btnAddUser, "Add a new user to the system.");
        buttonPanel.add(btnAddUser);

        btnEditUser = new JButton("Edit Selected User");
        styleButton(btnEditUser, "Edit details of the selected user.");
        buttonPanel.add(btnEditUser);

        btnDeleteUser = new JButton("Delete Selected User");
        styleButton(btnDeleteUser, "Delete the selected user from the system.");
        btnDeleteUser.setBackground(new Color(220, 53, 69));
        btnDeleteUser.setForeground(Color.WHITE);
        buttonPanel.add(btnDeleteUser);

        btnGenerateReport = new JButton("Generate Report");
        styleButton(btnGenerateReport, "Generate a text report of the user list (.txt).");
        buttonPanel.add(btnGenerateReport);

        btnBack = new JButton("Back to Dashboard");
        styleButton(btnBack, "Return to the main dashboard.");
        buttonPanel.add(btnBack);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        addActionListeners();
        pack();
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

    public void loadUsers() {
        loadUsers(null);
    }

    public void loadUsers(String searchTerm) {
        tableModel.setRowCount(0);
        if (userService == null) {
            try {
                userService = RmiClientUtil.getUserService();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "User service connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (userService == null) {
             JOptionPane.showMessageDialog(this, "User service is not available.", "Service Error", JOptionPane.ERROR_MESSAGE);
             return;
        }

        try {
            userList = userService.retreiveAll();
            List<User> displayList = new ArrayList<>();

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String lowerSearchTerm = searchTerm.toLowerCase().trim();
                for (User u : userList) {
                    if ((u.getUsername() != null && u.getUsername().toLowerCase().contains(lowerSearchTerm)) ||
                        (u.getEmail() != null && u.getEmail().toLowerCase().contains(lowerSearchTerm))) {
                        displayList.add(u);
                    }
                }
            } else {
                displayList.addAll(userList);
            }

            if (!displayList.isEmpty()) {
                for (User user : displayList) {
                    String createdAtStr = user.getCreatedAt() != null ? DATE_FORMAT.format(user.getCreatedAt()) : "N/A";
                    tableModel.addRow(new Object[]{
                            user.getUserId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getRole(),
                            createdAtStr
                    });
                }
            } else if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                tableModel.addRow(new Object[]{"No users found matching search.", "", "", "", ""});
            } else {
                tableModel.addRow(new Object[]{"No users in the system.", "", "", "", ""});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(), "Loading Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addActionListeners() {
        btnSearchUser.addActionListener(e -> loadUsers(txtSearchUserField.getText()));
        btnRefreshUserList.addActionListener(e -> loadUsers());
        btnBack.addActionListener(e -> {
            if (currentUser != null) new DashboardForm(currentUser).setVisible(true);
            else new LoginForm().setVisible(true);
            dispose();
        });

        btnAddUser.addActionListener(e -> {
            if (userService == null) { // Good practice to check if service is available
                JOptionPane.showMessageDialog(this, "User service not available.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            new UserForm(currentUser, this).setVisible(true);
            // loadUsers() will be called by UserForm upon successful save
        });

        btnEditUser.addActionListener(e -> {
            if (userService == null) { // Good practice
                JOptionPane.showMessageDialog(this, "User service not available.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User userToEdit = null;
            Object idObj = userTable.getValueAt(selectedRow, 0); // User ID from column 0
            if (idObj != null) {
                int userId = (Integer) idObj;
                // Find user in the local userList
                for (User u : userList) { // userList is a class member
                    if (u.getUserId() == userId) {
                        userToEdit = u;
                        break;
                    }
                }
            }

            if (userToEdit != null) {
                new UserForm(currentUser, userToEdit, this).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Could not find the selected user details to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                loadUsers(); // Refresh list if data seems out of sync
            }
        });

        btnDeleteUser.addActionListener(e -> {
             int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Object idObj = userTable.getValueAt(selectedRow, 0);
            if (idObj != null) {
                int userId = (Integer) idObj;
                User userToDelete = userList.stream().filter(u -> u.getUserId() == userId).findFirst().orElse(null);
                if (userToDelete != null) {
                    if (currentUser.getUserId() == userToDelete.getUserId()) {
                         JOptionPane.showMessageDialog(this, "You cannot delete your own account.", "Delete Error", JOptionPane.ERROR_MESSAGE);
                         return;
                    }
                    int confirm = JOptionPane.showConfirmDialog(this, "Delete user: " + userToDelete.getUsername() + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            String result = userService.deleteUser(userToDelete);
                            JOptionPane.showMessageDialog(this, result);
                            if (result.toLowerCase().contains("success")) loadUsers();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                     JOptionPane.showMessageDialog(this, "Selected user not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnGenerateReport.addActionListener(e -> generateUserReport());
    }

    private void generateUserReport() {
        if (userList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No user data to generate a report.", "Empty List", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save User Report");
        fileChooser.setSelectedFile(new File("users_report.txt"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".txt");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write("User Report - Food Traceability System"); writer.newLine();
                writer.write("Generated on: " + new Date().toString()); writer.newLine();
                writer.write("=============================================================================================="); writer.newLine();
                writer.write(String.format("%-10s | %-25s | %-30s | %-15s | %-20s",
                                           "User ID", "Username", "Email", "Role", "Created At")); writer.newLine();
                writer.write("----------------------------------------------------------------------------------------------"); writer.newLine();

                for (User user : userList) {
                    String createdAtStr = user.getCreatedAt() != null ? DATE_FORMAT.format(user.getCreatedAt()) : "N/A";
                    writer.write(String.format("%-10d | %-25s | %-30s | %-15s | %-20s",
                            user.getUserId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getRole(),
                            createdAtStr));
                    writer.newLine();
                }
                writer.write("=============================================================================================="); writer.newLine();
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
            User testAdminUser = new User();
            testAdminUser.setUserId(1);
            testAdminUser.setUsername("adminTest");
            testAdminUser.setRole("admin");
            new UserListForm(testAdminUser);
        });
    }
}
