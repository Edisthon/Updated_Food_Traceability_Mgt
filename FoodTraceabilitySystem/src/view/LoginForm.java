package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.User;
import service.UserInterface;
import util2.RmiClientUtil;

public class LoginForm extends javax.swing.JFrame {

    // UI Components
    private JLabel lblTitle, lblEmail, lblOtp;
    private JTextField txtEmail;
    private JPasswordField txtOtp; // Changed to JPasswordField for OTP
    private JButton btnRequestOtp, btnLogin, btnExit;

    public LoginForm() {
        initComponents();
        setTitle("User Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null); // Center the window
        setVisible(true); // Make the frame visible after init
    }

    private void initComponents() {
        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(102, 102, 102));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        lblTitle = new JLabel("User Login");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 24)); // Larger title font
        lblTitle.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(lblTitle, gbc);

        // Reset gridwidth and anchor for subsequent components
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Email Label and Field
        lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblEmail.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(lblEmail, gbc);

        txtEmail = new JTextField(25);
        txtEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtEmail.setToolTipText("Enter your registered email address.");
        txtEmail.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(txtEmail, gbc);

        // "Send OTP" Button
        btnRequestOtp = new JButton("Send OTP");
        btnRequestOtp.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnRequestOtp.setToolTipText("Request an OTP to your email.");
        btnRequestOtp.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 1; // Align under the email field or to its right
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST; // Align button to the right of the cell
        mainPanel.add(btnRequestOtp, gbc);
        gbc.anchor = GridBagConstraints.WEST; // Reset anchor

        // OTP Label and Field
        lblOtp = new JLabel("OTP:");
        lblOtp.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblOtp.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(lblOtp, gbc);

        txtOtp = new JPasswordField(15); // Changed to JPasswordField
        txtOtp.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtOtp.setToolTipText("Enter the OTP received in your email.");
        txtOtp.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(txtOtp, gbc);

        // Initially hide OTP field and label
        lblOtp.setVisible(false);
        txtOtp.setVisible(false);

        // Buttons Panel (Login and Exit)
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setOpaque(false); // Make it transparent

        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnLogin.setToolTipText("Login with your email and OTP.");
        btnLogin.setPreferredSize(new Dimension(100, 35));
        btnLogin.setVisible(false); // Initially hidden until OTP is requested
        buttonsPanel.add(btnLogin);

        btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnExit.setToolTipText("Exit the application.");
        btnExit.setPreferredSize(new Dimension(100, 35));
        buttonsPanel.add(btnExit);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 5, 5, 5); // Add some top margin before buttons
        mainPanel.add(buttonsPanel, gbc);

        // Action Listeners
        btnRequestOtp.addActionListener(e -> btnRequestOtpActionPerformed());
        btnLogin.addActionListener(e -> btnLoginActionPerformed());
        btnExit.addActionListener(e -> System.exit(0));
        
        // Set txtEmail as default focused component
        SwingUtilities.invokeLater(() -> txtEmail.requestFocusInWindow());

        add(mainPanel);
        pack(); // Adjust frame size to fit components
    }

    private void btnRequestOtpActionPerformed() {
        String email = txtEmail.getText().trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email address is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return;
        }

        // Basic email format validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            JOptionPane.showMessageDialog(this, "Invalid email address format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return;
        }

        try {
            UserInterface userService = RmiClientUtil.getUserService();
            if (userService == null) {
                 JOptionPane.showMessageDialog(this, "Failed to connect to server. Service not available.", "Connection Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            String result = userService.requestOtpByEmail(email);
            JOptionPane.showMessageDialog(this, result);

            if (result != null && result.toLowerCase().contains("otp has been generated")) {
                lblOtp.setVisible(true);
                txtOtp.setVisible(true);
                btnLogin.setVisible(true);
                txtOtp.requestFocusInWindow();
                // self.pack(); // consider if needed after components become visible
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error requesting OTP: " + ex.getMessage(), "Request Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnLoginActionPerformed() {
        String email = txtEmail.getText().trim();
        String otp = new String(txtOtp.getPassword()).trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email address cannot be empty for login.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return;
        }
        if (otp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "OTP is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtOtp.requestFocus();
            return;
        }

        try {
            UserInterface userService = RmiClientUtil.getUserService();
            if (userService == null) {
                 JOptionPane.showMessageDialog(this, "Failed to connect to server. Service not available.", "Connection Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            User loggedInUser = userService.loginUser(email, otp);

            if (loggedInUser != null) {
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + loggedInUser.getUsername(), "Login Success", JOptionPane.INFORMATION_MESSAGE);
                // Navigate to the new DashboardForm
                new DashboardForm(loggedInUser).setVisible(true);
                this.dispose(); // Close LoginForm
            } else {
                JOptionPane.showMessageDialog(this, "Login Failed: Invalid email or OTP, or OTP may have expired.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                txtOtp.setText("");
                txtOtp.requestFocus();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during login: " + ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Set a modern Look and Feel (Nimbus)
                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception e) {
                    // If Nimbus is not available, fall back to the default L&F
                    e.printStackTrace();
                }
                new LoginForm(); // setVisible(true) is called in constructor
            }
        });
    }
}
