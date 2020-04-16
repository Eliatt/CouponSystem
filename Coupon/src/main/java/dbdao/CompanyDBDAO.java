package dbdao;

import dao.CompanyDAO;
import entities.Company;
import pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class CompanyDBDAO implements CompanyDAO {

    private ConnectionPool pool;

    public CompanyDBDAO() {
        pool = ConnectionPool.getInstance();
    }

// check if data base contains a specific company given it's email and password

    @Override
    public Boolean isCompanyExists(String email, String password) {
        Boolean isExist = false;
        String sql = "SELECT * FROM companies WHERE EMAIL=? AND PASSWORD=?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isExist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return isExist;
    }

// add a company to data base

    @Override
    public void addCompany(Company company) {
        String sql = "INSERT INTO companies (NAME ,EMAIL,PASSWORD)VALUES (?,?,?)";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setString(3, company.getPassword());
            preparedStatement.executeUpdate();
            System.out.println("Company: " + company.getName()+ " successfully added");
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                company.setId(resultSet.getLong(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
    }

// update an existing data base company
    @Override
    public void updateCompany(Company company) {
        String sql = "UPDATE companies SET NAME = ? ,EMAIL = ? ,PASSWORD = ? WHERE ID = ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setString(3, company.getPassword());
            preparedStatement.setLong(4, company.getId());
            preparedStatement.executeUpdate();
            System.out.println("Company: " + company.getName()+ " successfully updated");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
    }

    //delete company from data base
    @Override
    public Company deleteCompany(Long companyId) {
        String sql = "DELETE FROM companies WHERE ID = ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, companyId);
            preparedStatement.executeUpdate();
            System.out.println("Company deleted");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return null;
    }

    //retrieve all listed companies for inspection

    @Override
    public ArrayList<Company> getAllCompanies() {
        ArrayList<Company> all = null;
        String sql = "SELECT * FROM companies";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            all = new ArrayList();
            while (resultSet.next()) {
                Long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");
                String email = resultSet.getString("EMAIL");
                String password = resultSet.getString("PASSWORD");
                all.add(new Company(id, name, email, password));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return all;
    }

    //retrieve a single company for inspection given it's email and password

    public Company getByEmailAndPassword(String email, String password) {
        Company company = null;
        String sql = "SELECT * FROM  companies WHERE EMAIL = ? AND PASSWORD = ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("ID");
                String name = resultSet.getString(2);
                company = new Company(id, name, email, password);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return company;
    }

    //retrieve a single company for inspection given it's ID
    @Override
    public Company getOneCompany(Long id) {
        Company company = null;
        String sql = "SELECT * FROM  companies WHERE ID = ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("NAME");
                String email = resultSet.getString("EMAIL");
                String password = resultSet.getString("PASSWORD");
                company = new Company(id, name, email, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return company;
    }

    // check if data base contains a specific company given it's name

    public Boolean isCompanyNameExists(String name) {
        Boolean isExist = false;
        String sql = "SELECT * FROM companies WHERE NAME =? ";
        Connection connection = pool.getConnection();
        try (PreparedStatement prstm = connection.prepareStatement(sql)) {
            prstm.setString(1, name);
            ResultSet resultSet = prstm.executeQuery();
            if (resultSet.next()) {
                isExist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return isExist;
    }

    // check if data base contains a specific company given it's email

    public Boolean isCompanyEmailExists(String email) {
        Boolean isExist = false;
        String sql = "SELECT * FROM companies WHERE EMAIL=?";
        Connection connection = pool.getConnection();
        try (PreparedStatement prstm = connection.prepareStatement(sql)) {
            prstm.setString(1, email);
            ResultSet resultSet = prstm.executeQuery();
            if (resultSet.next()) {
                isExist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return isExist;
    }
}
