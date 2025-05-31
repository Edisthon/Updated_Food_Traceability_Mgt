package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.User;
import service.UserInterface;
import util2.RmiClientUtil;

public class UserForm extends JFrame {

    private User currentAdminUser; // The admin performing the action
    private User userToEdit; // Null if adding
    private UserListForm userListFormInstance; // To call refresh
    private UserInterface userService;

    private JTextField txtUsername, txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> comboRole;
    private JButton btnSave, btnCancel;
    private boolean isEditMode = false;
    private JLabel lblPasswordInfo; // To inform about password change

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


    // Constructor for Adding
    public UserForm(User adminUser, UserListForm listForm) {
        this(adminUser, null, listForm);
    }

    // Main Constructor (for Editing/Adding)
    public UserForm(User adminUser, User userToEdit, UserListForm listForm) {
        this.currentAdminUser = adminUser;
        this.userToEdit = userToEdit;
        this.userListFormInstance = listForm;

        if (this.userToEdit != null) {
            isEditMode = true;
        }

        try {
            userService = RmiClientUtil.getUserService();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to User Service: " + e.getMessage(),
                                          "Connection Error", JOptionPane.ERROR_MESSAGE);
        }

        initComponents();
        setTitle(isEditMode ? "Edit User" : "Add New User");

        if (isEditMode) {
            populateFormFields();
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
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

        JLabel lblFormTitle = new JLabel(isEditMode ? "Edit User Details" : "Add New User", SwingConstants.CENTER);
        lblFormTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblFormTitle.setForeground(new Color(50,50,50));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(lblFormTitle, gbc);

        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Tahoma", Font.BOLD, 13);
        Font textFont = new Font("Tahoma", Font.PLAIN, 13);
        Dimension textFieldSize = new Dimension(250, 28);

        // Username
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 1; mainPanel.add(lblUsername, gbc);
        txtUsername = new JTextField();
        txtUsername.setFont(textFont);
        txtUsername.setPreferredSize(textFieldSize);
        txtUsername.setToolTipText("Enter username (cannot be changed once set).");
        gbc.gridx = 1; gbc.gridy = 1; mainPanel.add(txtUsername, gbc);

        // Password
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 2; mainPanel.add(lblPassword, gbc);
        txtPassword = new JPasswordField();
        txtPassword.setFont(textFont);
        txtPassword.setPreferredSize(textFieldSize);
        txtPassword.setToolTipText(isEditMode ? "Enter new password or leave blank to keep current." : "Enter password.");
        gbc.gridx = 1; gbc.gridy = 2; mainPanel.add(txtPassword, gbc);

        lblPasswordInfo = new JLabel(isEditMode ? "(Leave blank to keep current password)" : "(Required for new user)");
        lblPasswordInfo.setFont(new Font("Tahoma", Font.ITALIC, 10));
        lblPasswordInfo.setForeground(Color.GRAY);
        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(lblPasswordInfo, gbc);
        gbc.anchor = GridBagConstraints.WEST; // Reset

        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 4; mainPanel.add(lblEmail, gbc);
        txtEmail = new JTextField();
        txtEmail.setFont(textFont);
        txtEmail.setPreferredSize(textFieldSize);
        txtEmail.setToolTipText("Enter user's email address.");
        gbc.gridx = 1; gbc.gridy = 4; mainPanel.add(txtEmail, gbc);

        // Role
        JLabel lblRole = new JLabel("Role:");
        lblRole.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 5; mainPanel.add(lblRole, gbc);
        comboRole = new JComboBox<>(new String[]{"user", "admin"});
        comboRole.setFont(textFont);
        comboRole.setPreferredSize(textFieldSize);
        comboRole.setToolTipText("Select user role.");
        gbc.gridx = 1; gbc.gridy = 5; mainPanel.add(comboRole, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        Dimension buttonSize = new Dimension(100, 30);

        btnSave = new JButton("Save");
        btnSave.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnSave.setPreferredSize(buttonSize);
        btnSave.setBackground(new Color(50, 150, 50));
        btnSave.setForeground(Color.WHITE);
        buttonPanel.add(btnSave);

        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnCancel.setPreferredSize(buttonSize);
        btnCancel.setBackground(new Color(150, 50, 50));
        btnCancel.setForeground(Color.WHITE);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(15, 5, 5, 5);
        mainPanel.add(buttonPanel, gbc);

        btnSave.addActionListener(e -> saveUser());
        btnCancel.addActionListener(e -> dispose());

        add(mainPanel);
    }

    private void populateFormFields() {
        if (userToEdit != null) {
            txtUsername.setText(userToEdit.getUsername());
            txtUsername.setEditable(false); // Username usually not editable
            txtUsername.setBackground(new Color(230,230,230));
            txtEmail.setText(userToEdit.getEmail());
            comboRole.setSelectedItem(userToEdit.getRole());
            // Password field is intentionally left blank for editing.
        }
    }

    private void saveUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String email = txtEmail.getText().trim();
        String role = (String) comboRole.getSelectedItem();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtUsername.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid email format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return;
        }
        if (!isEditMode && password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password is required for a new user.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtPassword.requestFocus();
            return;
        }
        // Add more password complexity rules if needed

        if (userService == null) {
             try { userService = RmiClientUtil.getUserService(); }
             catch (Exception e) { e.printStackTrace();
                JOptionPane.showMessageDialog(this, "User service not available: " + e.getMessage(), "Service Error", JOptionPane.ERROR_MESSAGE); return; }
        }
        if (userService == null) {
             JOptionPane.showMessageDialog(this, "User service is not available.", "Service Error", JOptionPane.ERROR_MESSAGE); return;
        }


        try {
            String resultMessage;
            if (isEditMode) {
                userToEdit.setEmail(email);
                userToEdit.setRole(role);
                if (!password.isEmpty()) {
                    userToEdit.setPassword(password); // Assuming direct password set; hashing would be done here or on server
                }
                resultMessage = userService.updateUser(userToEdit);
            } else { // Adding new user
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword(password); // Hashing should be applied
                newUser.setEmail(email);
                newUser.setRole(role);
                // Assuming User model has setCreatedAt accepting String
                newUser.setCreatedAt(DATE_FORMAT.format(new Date()));
                resultMessage = userService.registerUser(newUser);
            }

            JOptionPane.showMessageDialog(this, resultMessage);

            if (resultMessage != null && resultMessage.toLowerCase().contains("success")) {
                if (userListFormInstance != null) {
                    userListFormInstance.loadUsers();
                }
                dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving user: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // No main method here, this form is typically opened from UserListForm.
}
