package dao;

import entities.Customer;



public interface CustomerDAO {

    Boolean isCustomerExists(String email, String password);

    void addCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomer(Long customerID);

    Customer getOneCustomer(Long customerID);
}


