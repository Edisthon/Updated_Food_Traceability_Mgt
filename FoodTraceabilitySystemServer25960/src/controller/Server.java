/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import service.UserInt;
import service.UserInterface;
import service.implementation.ProductImpl;
import service.implementation.ProductStatusLogImpl;
import service.implementation.UserImpl;
import service.implementation.UserImplementation;


public class Server {
   
        private ProductImpl productImpl;
        private ProductStatusLogImpl productStatusLogImpl;
        private UserImpl userImpl;
        private UserImplementation userImplementation;
        
        public Server() throws RemoteException{
            
            this.productImpl= new ProductImpl();
            this.productStatusLogImpl= new ProductStatusLogImpl();
            this.userImpl= new UserImpl();
            this.userImplementation= new UserImplementation();
        }
        
        public static void main(String[] args) {
            
            try{
                
                System.setProperty("java.rmi.server.hostname", "127.0.0.1");
                Registry registry= LocateRegistry.createRegistry(81);
                UserInterface service= new UserImpl();
                registry.rebind("UserService", service);
                registry.rebind("user", new Server().userImpl);
                registry.rebind("product", new Server().productImpl);
                registry.rebind("productstatus", new Server().productStatusLogImpl);
                System.out.println("Server is running on 81");                      
            }catch(Exception ex){
                ex.printStackTrace();
        
            }
            
    }
        
        
}
