package dbdao;

import dao.CouponDistributionDAO;
import entities.Coupon;
import pool.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CouponDistributionDBDAO implements CouponDistributionDAO {

    private ConnectionPool pool;

    public CouponDistributionDBDAO() {
        pool = ConnectionPool.getInstance();
    }

    // document coupon(s) purchased by a specific customer given their respective ID
    @Override
    public void addCouponPurchase(Long customerId, Long couponId) {
        String sql = "INSERT INTO customers_vs_coupons (CUSTOMER_ID , COUPON_ID) values (?,?)";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, customerId);
            preparedStatement.setLong(2, couponId);
            preparedStatement.executeUpdate();
            System.out.println("Coupon purchase completed");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }

    }

    // revoke coupon(s) purchasing  made by a specific customer given their respective ID

    @Override
    public void deleteCouponPurchase(Long customerId, Long couponId) {
        String sql = "DELETE FROM customers_vs_coupons WHERE  CUSTOMER_ID = ? AND COUPON_ID = ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, customerId);
            preparedStatement.setLong(2, couponId);
            preparedStatement.executeUpdate();
            System.out.println("Coupon purchase revoked");
        } catch (Exception e) {

        } finally {
            pool.returnConnetion(connection);
        }
    }

    // check if a specific customer purchased a  certain coupon

    @Override
    public Boolean isPurchased(Long customerId, Long couponId) {
        Boolean isExists = false;
        String sql = "SELECT * FROM customers_vs_coupons WHERE  CUSTOMER_ID = ? AND COUPON_ID = ?;";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, customerId);
            preparedStatement.setLong(2, couponId);
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


    // revoke coupon(s) purchasing  made by a specific customer given coupon's ID

    public void revokePurchaseByCouponId(Long couponId) {
        String sql = "SELECT * FROM customers_vs_coupons WHERE  COUPON_ID = ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, couponId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                deleteCouponPurchase(resultSet.getLong("CUSTOMER_ID"), couponId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
    }

    // revoke coupon(s) purchasing  made by a specific customer given customer's ID

    public void revokePurchaseByCustomerId(Long customerId) {
        String sql = "SELECT * FROM customers_vs_coupons WHERE  CUSTOMER_ID = ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                deleteCouponPurchase(customerId, resultSet.getLong("COUPON_ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
    }

    //retrieve all listed coupons purchased by a specific customer given customer's ID

    public List<Coupon> getPurchaseByCustomerId(Long customerId) {
        List<Coupon> coupons = new ArrayList<>();
        String sql = "SELECT * FROM coupons where id in " +
                "(select coupon_id from customers_vs_coupons where customer_id = ?)";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong(1);
                long companyId = resultSet.getLong(2);
                long categoryId = resultSet.getLong(3);
                String title = resultSet.getString(4);
                String description = resultSet.getString(5);
                LocalDate startDate = resultSet.getDate(6).toLocalDate();
                LocalDate endDate = resultSet.getDate(7).toLocalDate();
                int amount = resultSet.getInt(8);
                double price = resultSet.getDouble(9);
                String image = resultSet.getString(10);
                Coupon coupon = new Coupon(id, companyId, categoryId, title, description, startDate, endDate, amount, price, image);
                coupons.add(coupon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return coupons;
    }


    //retrieve all listed coupons purchased by a specific customer given coupon's type

    public List<Coupon> getCustomerCouponsByCategory(Long categoryId, Long customerId) {
        List<Coupon> coupons = new ArrayList<>();
        String sql = "SELECT * FROM coupons where CATEGORY_ID=? and id in " +
                "(select coupon_id from customers_vs_coupons where customer_id = ?)";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, categoryId);
            preparedStatement.setLong(2, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong(1);
                long companyId = resultSet.getLong(2);
                String title = resultSet.getString(4);
                String description = resultSet.getString(5);
                LocalDate start = resultSet.getDate(6).toLocalDate();
                LocalDate end = resultSet.getDate(7).toLocalDate();
                int amount = resultSet.getInt(8);
                double price = resultSet.getDouble(9);
                String image = resultSet.getString(10);
                Coupon coupon = new Coupon(id, companyId, categoryId, title, description, start, end, amount, price, image);
                coupons.add(coupon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return coupons;
    }


    //retrieve all listed coupons purchased by a specific customer given coupon's maximum price

    public List<Coupon> getCustomerCouponsByPrice(Double max, Long customerId) {
        List<Coupon> coupons = new ArrayList<>();
        String sql = "SELECT * FROM coupons WHERE PRICE <= ? and id in (select coupon_id from customers_vs_coupons where customer_id = ?)";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, max);
            preparedStatement.setLong(2, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong(1);
                long companyId = resultSet.getLong(2);
                long categoryId = resultSet.getLong(3);
                String title = resultSet.getString(4);
                String description = resultSet.getString(5);
                LocalDate startDate = resultSet.getDate(6).toLocalDate();
                LocalDate endDate = resultSet.getDate(7).toLocalDate();
                int amount = resultSet.getInt(8);
                double price = resultSet.getDouble(9);
                String image = resultSet.getString(10);
                Coupon coupon = new Coupon(id, companyId, categoryId, title, description, startDate, endDate, amount, price, image);
                coupons.add(coupon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return coupons;
    }
}



