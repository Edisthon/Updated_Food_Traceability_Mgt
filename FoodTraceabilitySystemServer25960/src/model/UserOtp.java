package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_otp")
public class UserOtp implements Serializable {

    private static final long serialVersionUID = 1L; // Good practice for Serializable classes

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_id")
    private int otpId;

    @Column(name = "email", nullable = false, unique = true, length = 255) // Assuming one OTP per email at a time
    private String email;

    @Column(name = "otp_value", nullable = false, length = 10)
    private String otpValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_timestamp", nullable = false)
    private Date expiryTimestamp;

    // Constructors
    public UserOtp() {}

    public UserOtp(String email, String otpValue, Date expiryTimestamp) {
        this.email = email;
        this.otpValue = otpValue;
        this.expiryTimestamp = expiryTimestamp;
    }

    // Getters and Setters
    public int getOtpId() { return otpId; }
    public void setOtpId(int otpId) { this.otpId = otpId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getOtpValue() { return otpValue; }
    public void setOtpValue(String otpValue) { this.otpValue = otpValue; }
    public Date getExpiryTimestamp() { return expiryTimestamp; }
    public void setExpiryTimestamp(Date expiryTimestamp) { this.expiryTimestamp = expiryTimestamp; }
}
