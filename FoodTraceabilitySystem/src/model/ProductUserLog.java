package model;

import java.io.Serializable;
import java.util.Date;

// No JPA annotations for the client-side DTO
public class ProductUserLog implements Serializable {

    private static final long serialVersionUID = 1L; // Good practice for Serializable classes

    private int logId;
    private Product product; // Client needs Product.java
    private User user;       // Client needs User.java
    private String action;
    private String location;
    private Date timestamp;
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
