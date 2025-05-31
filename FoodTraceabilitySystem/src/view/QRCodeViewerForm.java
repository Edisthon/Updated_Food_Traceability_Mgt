package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import model.Product; // Assuming client has model.Product

// ZXing imports - these will only compile if ZXing JARs are in classpath
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.Hashtable; // Use Hashtable for zxing hintMap

public class QRCodeViewerForm extends JFrame {

    private JLabel lblQrCodeImage;
    private JLabel lblProductInfo;
    private Product product;

    public QRCodeViewerForm(Product product) {
        this.product = product;

        initComponents(); // Initialize components first

        setTitle("QR Code Viewer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null); // Center the window

        displayQrCode(); // Generate and display QR code

        pack(); // Pack after QR code might have set its size (or image label has preferred size)
        setVisible(true);
    }

    private void initComponents() {
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE); // White background for QR code visibility

        // Product Info Label
        lblProductInfo = new JLabel("", SwingConstants.CENTER);
        lblProductInfo.setFont(new Font("Tahoma", Font.BOLD, 14));
        if (product != null && product.getName() != null) {
            lblProductInfo.setText("QR Code for: " + product.getName() + " (ID: " + product.getProductId() + ")");
        } else if (product != null) {
            lblProductInfo.setText("QR Code for Product ID: " + product.getProductId());
        }
        else {
            lblProductInfo.setText("No Product Data Provided");
        }
        mainPanel.add(lblProductInfo, BorderLayout.NORTH);

        // QR Code Image Label
        lblQrCodeImage = new JLabel("Generating QR Code...", SwingConstants.CENTER);
        lblQrCodeImage.setPreferredSize(new Dimension(300, 300)); // Set preferred size for QR code image
        lblQrCodeImage.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        mainPanel.add(lblQrCodeImage, BorderLayout.CENTER);

        // Close Button
        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnClose.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5)); // Centered close button
        bottomPanel.add(btnClose);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void displayQrCode() {
        if (product == null) {
            lblQrCodeImage.setText("Product data is missing.");
            lblQrCodeImage.setIcon(null);
            return;
        }

        // Using product ID as the QR code data, as specified in the prompt.
        // Alternatively, if the Product model has a specific getQrCode() method returning a string, use that.
        String qrCodeData = String.valueOf(product.getProductId());
        // Example if Product has a getQrCode() method for specific data:
        // String qrCodeData = product.getQrCode();
        // if (qrCodeData == null || qrCodeData.trim().isEmpty()) {
        //     qrCodeData = String.valueOf(product.getProductId()); // Fallback to ID
        // }


        int width = 280; // Size of QR code image (slightly smaller than label to fit border)
        int height = 280;

        try {
            Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L); // L = ~7% correction
            hintMap.put(EncodeHintType.MARGIN, 1); // Margin around QR code (1 module)

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, width, height, hintMap);

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            lblQrCodeImage.setIcon(new ImageIcon(qrImage));
            lblQrCodeImage.setText(null); // Remove "Generating..." text

        } catch (Exception e) {
            e.printStackTrace();
            lblQrCodeImage.setText("Error generating QR Code.");
            lblQrCodeImage.setIcon(null);
            JOptionPane.showMessageDialog(this, "Could not generate QR Code: " + e.getMessage(),
                                          "QR Generation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create a dummy product for testing
            Product testProduct = new Product();
            testProduct.setProductId(12345); // Example product ID
            testProduct.setName("Test Apple Batch QR");
            // If Product class has a setQrCodeData method and you want to test that:
            // testProduct.setQrCode("CustomDataForQRCode123");

            new QRCodeViewerForm(testProduct);
        });
    }
}
