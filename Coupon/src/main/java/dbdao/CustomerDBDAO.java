package dbdao;

import dao.CustomerDAO;
import entities.Customer;
import pool.ConnectionPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDBDAO implements CustomerDAO {
    private ConnectionPool pool;

    public CustomerDBDAO(){
        pool = ConnectionPool.getInstance();
    }

//check if data base contains a customer given customer's email and password

    @Override
    public Boolean isCustomerExists(String email, String password) {
            Boolean isExist = false;
            String sql = "SELECT * FROM customers WHERE EMAIL = ? AND PASSWORD = ?;";
            Connection connection = pool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    isExist = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                pool.returnConnetion(connection);
            }
            return isExist;
        }


    //check if data base contains a customer given customer's email

    public Boolean isCustomerEmailExist(String email) {
        Boolean isExits = false;
        String sql = "SELECT * FROM customers WHERE EMAIL=? ";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isExits = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return isExits;

    }

//    add a customer to data base table

    @Override
    public void addCustomer(Customer customer){
        String sql = "INSERT INTO customers (FIRST_NAME, LAST_NAME, EMAIL, PASSWORD) VALUES(?,?,?,?)";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPassword());
            preparedStatement.executeUpdate();
            System.out.println("Customer: " + customer.getFirstName()+ " " + customer.getLastName() + " added");
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                customer.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
    }

   // update an existing data base customer

    @Override
    public void updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ?, PASSWORD = ? WHERE ID = ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPassword());
            preparedStatement.setLong(5, customer.getId());
            preparedStatement.executeUpdate();
            System.out.println("Customer ID number : " + customer.getId() + " updated");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
    }


    // delete an existing data base customer

    @Override
    public void deleteCustomer(Long customerID) {
            String sql = "DELETE FROM customers WHERE ID = ?;";
            Connection connection = pool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, customerID);
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                pool.returnConnetion(connection);
            }
        }

        //retrieve all listed customers

    public List<Customer> allCustomers() {

        List<Customer> all = null;
        String sql = "SELECT * FROM customers ";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            all = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                String email = resultSet.getString(4);
                String password = resultSet.getString(5);
                Customer customer = new Customer(id, firstName, lastName, email, password);
                all.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return all;
    }


    //retrieve a single listed customer given his ID

    @Override
    public Customer getOneCustomer(Long customerID) {
        Customer customer = null;
        String sql = "SELECT * FROM customers WHERE ID = ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("FIRST_NAME");
                String lastName = resultSet.getString("LAST_NAME");
                String email = resultSet.getString("EMAIL");
                String password = resultSet.getString("PASSWORD");
                customer = new Customer(customerID, firstName, lastName, email, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return customer;
    }


    //delete customer listing from data base table

    public void deleteCustomer(long id) {
        String sql = "DELETE FROM customers WHERE ID = ?;";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
    }


    //retrieve all data base listed customers

    public List<Customer> getAllCustomers() {
        List<Customer> allCustomers = null;
        String sql = "SELECT * FROM customers ";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            allCustomers = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong(1);
                String first_Name = resultSet.getString(2);
                String last_Name = resultSet.getString(3);
                String email = resultSet.getString(4);
                String password = resultSet.getString(5);
                Customer customer = new Customer(id, first_Name, last_Name, email, password);
                allCustomers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return allCustomers;
    }



    //retrieve a single listed customer given his email and password

    public Customer getOneCustomer(String email, String password) {
        Customer customer = null;
        String sql = "SELECT * FROM customers WHERE EMAIL = ? AND PASSWORD = ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("ID");
                String first_Name = resultSet.getString("FIRST_NAME");
                String last_Name = resultSet.getString("LAST_NAME");
                customer = new Customer(id, first_Name, last_Name, email, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return customer;
    }

    }









