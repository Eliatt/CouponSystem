package dbdao;

import dao.CouponDAO;
import entities.Coupon;
import pool.ConnectionPool;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


public class CouponDBDAO implements CouponDAO {

    private ConnectionPool pool;

    public CouponDBDAO() {
        pool = ConnectionPool.getInstance();
    }

    // add a coupon to data base

    public void addCoupon(Coupon coupon) {
        String sql = "INSERT INTO coupons " +
                "(COMPANY_ID ,CATEGORY_ID ,TITLE ,DESCRIPTION ,START_DATE ,END_DATE ,AMOUNT ,PRICE ,IMAGE )" +
                " VALUES(?,?,?,?,?,?,?,?,?)";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, coupon.getCompanyId());
            preparedStatement.setLong(2, coupon.getCategoryId());
            preparedStatement.setString(3, coupon.getTitle());
            preparedStatement.setString(4, coupon.getDescription());
            preparedStatement.setDate(5, Date.valueOf(coupon.getStartDate()));
            preparedStatement.setDate(6, Date.valueOf(coupon.getEndDate()));
            preparedStatement.setInt(7, coupon.getAmount());
            preparedStatement.setDouble(8, coupon.getPrice());
            preparedStatement.setString(9, coupon.getImage());
            preparedStatement.executeUpdate();
            System.out.println("Coupon added");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
    }

    // update an existing data base coupon

    @Override
    public void updateCoupon(Coupon coupon) {
        String sql = "UPDATE coupons SET CATEGORY_ID = ?, TITLE = ?, DESCRIPTION= ?, " +
                "START_DATE = ?, END_DATE = ?, AMOUNT = ?, PRICE = ?, IMAGE = ? WHERE ID = ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, coupon.getCategoryId());
            preparedStatement.setString(2, coupon.getTitle());
            preparedStatement.setString(3, coupon.getDescription());
            preparedStatement.setDate(4, Date.valueOf(coupon.getStartDate()));
            preparedStatement.setDate(5, Date.valueOf(coupon.getEndDate()));
            preparedStatement.setInt(6, coupon.getAmount());
            preparedStatement.setDouble(7, coupon.getPrice());
            preparedStatement.setString(8, coupon.getImage());
            preparedStatement.setLong(9, coupon.getId());
            preparedStatement.executeUpdate();
            System.out.println("Coupon: " + coupon.getTitle() + " updated");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
    }

    // delete a coupon from data base

    @Override
    public void deleteCoupon(Long id) {
        String sql = "DELETE FROM coupons WHERE ID = ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Successfully deleted");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
    }

    //retrieve all listed coupons for inspection

    @Override
    public List<Coupon> getAllCoupons() {
        List<Coupon> allCoupons = null;
        String sql = "SELECT * FROM coupons";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            allCoupons = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong(1);
                long company_id = resultSet.getLong(2);
                long category_id = resultSet.getLong(3);
                String title = resultSet.getString(4);
                String description = resultSet.getString(5);
                LocalDate start_date = resultSet.getDate(6).toLocalDate();
                LocalDate end_date = resultSet.getDate(7).toLocalDate();
                int amount = resultSet.getInt(8);
                double price = resultSet.getDouble(9);
                String image = resultSet.getString(10);
                allCoupons.add(new Coupon(id, company_id, category_id, title, description, start_date, end_date, amount, price, image));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return allCoupons;
    }

    // check if data base contains a specific coupon given it's company ID  and title

    public Boolean isCouponExists(Long companyId, String title) {
        Boolean isExists = false;
        String sql = "SELECT * FROM coupons WHERE COMPANY_ID = ? AND TITLE = ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, companyId);
            preparedStatement.setString(2, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isExists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return isExists;
    }

    //retrieve a single listed coupon given it's ID

    @Override
    public Coupon getOneCoupon(Long id) {
        Coupon coupon = null;
        String sql = "SELECT * FROM coupons WHERE ID = ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long company_id = resultSet.getLong(2);
                long category_id = resultSet.getLong(3);
                String title = resultSet.getString(4);
                String description = resultSet.getString(5);
                LocalDate start_date = resultSet.getDate(6).toLocalDate();
                LocalDate end_date = resultSet.getDate(7).toLocalDate();
                int amount = resultSet.getInt(8);
                double price = resultSet.getDouble(9);
                String image = resultSet.getString(10);
                coupon = new Coupon(id, company_id, category_id, title, description, start_date, end_date, amount, price, image);
            }
        } catch (Exception e) {

        } finally {
            pool.returnConnetion(connection);
        }
        return coupon;
    }

    //retrieve all listed coupons associated with a specific company

    public List<Coupon> getCompanyCoupons(Long company_id) {
        List<Coupon> allCompanyCoupons = null;
        String sql = "SELECT * FROM coupons WHERE COMPANY_ID = ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, company_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            allCompanyCoupons = new ArrayList<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong(1);
                Long companyId = resultSet.getLong(2);
                Long category_id = resultSet.getLong(3);
                String title = resultSet.getString(4);
                String description = resultSet.getString(5);
                LocalDate start_date = resultSet.getDate(6).toLocalDate();
                LocalDate end_date = resultSet.getDate(7).toLocalDate();
                int amount = resultSet.getInt(8);
                double price = resultSet.getDouble(9);
                String image = resultSet.getString(10);
                allCompanyCoupons.add(new Coupon(id, companyId, category_id, title, description, start_date, end_date, amount, price, image));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return allCompanyCoupons;
    }

    //retrieve all listed coupons associated with a specific company given company ID and coupon type ID

    public List<Coupon> getAllCompanyCouponsByType(Long company_id,Long categoryId) {
        List<Coupon> allCouponCompnyType = null;
        String sql = "SELECT * FROM coupons WHERE COMPANY_ID= ? AND CATEGORY_ID= ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement prdtm = connection.prepareStatement(sql)) {
            prdtm.setLong(1, company_id);
            prdtm.setLong(2, categoryId);
            ResultSet resultSet = prdtm.executeQuery();
            allCouponCompnyType = new ArrayList<>();
            while (resultSet.next()) {
                long idCoupon = resultSet.getLong(1);
                String titel = resultSet.getString(4);
                String description = resultSet.getString(5);
                LocalDate start = resultSet.getDate(6).toLocalDate();
                LocalDate end = resultSet.getDate(7).toLocalDate();
                int amount = resultSet.getInt(8);
                double price = resultSet.getDouble(9);
                String image = resultSet.getString(10);
                allCouponCompnyType.add(new Coupon(idCoupon, company_id, categoryId, titel, description, start, end, amount, price, image));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return allCouponCompnyType;
    }

    //retrieve all listed coupons associated with a specific company given maximum coupon's price

    public List<Coupon> getCompanyCouponsByMaxPrice(Long company_id,Double maxPrice) {
        List<Coupon> CompnyCouponsByMaxPrice = null;
        String sql = "SELECT * FROM coupons WHERE COMPANY_ID = ? AND PRICE <= ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1,company_id);
            preparedStatement.setDouble(2,maxPrice);
            ResultSet resultSet = preparedStatement.executeQuery();
            CompnyCouponsByMaxPrice = new ArrayList<>();
            while (resultSet.next()) {
                long idCoupon = resultSet.getLong(1);
                long cat = resultSet.getLong(3);
                String titel = resultSet.getString(4);
                String description = resultSet.getString(5);
                LocalDate start = resultSet.getDate(6).toLocalDate();
                LocalDate end = resultSet.getDate(7).toLocalDate();
                int amount = resultSet.getInt(8);
                double price = resultSet.getDouble(9);
                String image = resultSet.getString(10);
                CompnyCouponsByMaxPrice.add(new Coupon(idCoupon, company_id, cat, titel, description, start, end, amount, price, image));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return CompnyCouponsByMaxPrice;
    }


}







