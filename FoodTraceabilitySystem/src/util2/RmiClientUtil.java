package util2;
import java.rmi.Naming;
import service.ProductInterface;
import service.ProductStatusLogInterface;
import service.ProductUserLogInterface; // Added import
import service.UserInterface;

public class RmiClientUtil {
    // Server is running on localhost (127.0.0.1) and port 81
    private static final String SERVER_URL_PREFIX = "rmi://127.0.0.1:81/"; 

    public static UserInterface getUserService() throws Exception {
        // Service is bound as "user"
         return (UserInterface) Naming.lookup("rmi://127.0.0.1:81/UserService");
    }
    
    
    public static ProductInterface getProductService() throws Exception {
        // Service is bound as "product"
        return (ProductInterface) Naming.lookup(SERVER_URL_PREFIX + "product"); 
    }

    public static ProductStatusLogInterface getProductStatusLogService() throws Exception {
        // Service is bound as "productstatus"
        return (ProductStatusLogInterface) Naming.lookup(SERVER_URL_PREFIX + "productstatus"); 
    }

    public static ProductUserLogInterface getProductUserLogService() throws Exception {
        // Service is bound as "productUserLogService"
        return (ProductUserLogInterface) Naming.lookup(SERVER_URL_PREFIX + "productUserLogService");
    }
}
