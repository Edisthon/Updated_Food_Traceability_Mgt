package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "product_user_log")
public class ProductUserLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private int logId;

    @ManyToOne(fetch = FetchType.LAZY) // Or EAGER if appropriate
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY) // Or EAGER
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "action", nullable = false, length = 100)
    private String action; // E.g., "Registered", "Status Updated", "Location Changed"

    @Column(name = "location", length = 255)
    private String location; // Optional, might be specific to certain actions

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    @Column(name = "details", columnDefinition="TEXT") // For more descriptive text if needed
    private String details;


    // Constructors
    public ProductUserLog() {
    }

    // Getters and Setters
    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
