
package model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

@Entity

public class Product implements Serializable{
    @Id
    private int productId;
    private String name;
    private String origin;
    private String qrCode;
    private String registrationDate;    

    public Product(int productId, String name, String origin, String qrCode, String registrationDate, int UserId, User users, Set<ProductStatusLog> productStatus) {
        this.productId = productId;
        this.name = name;
        this.origin = origin;
        this.qrCode = qrCode;
        this.registrationDate = registrationDate;        
        this.users = users;
        this.productStatus = productStatus;
    }

   
    public Product(int productId) {
        this.productId = productId;
    }

    public Product() {
    }
    
    @ManyToOne
    @JoinColumn(name = "userID", nullable= false)
    private User users;
    
    
    @OneToMany(mappedBy = "products")
    private Set<ProductStatusLog> productStatus;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }   

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

   

    public Set<ProductStatusLog> getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Set<ProductStatusLog> productStatus) {
        this.productStatus = productStatus;
    }
    
    
    
    
}
