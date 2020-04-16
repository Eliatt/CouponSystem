package dbdao;

import dao.CouponTypeDAO;
import entities.CouponType;
import pool.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class CouponTypeDBDAO implements CouponTypeDAO {
    private ConnectionPool pool;

    public CouponTypeDBDAO() {
        pool = ConnectionPool.getInstance();
    }

    // check if data base contains a specific coupon

    @Override
    public Boolean isExist(CouponType couponType) {
        Boolean isExist = false;
        String sql = "SELECT * FROM categories WHERE NAME= ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, couponType.name());
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

// insert new coupon type to a data base table
    @Override
    public void addCategory(CouponType couponType) {
        String sql = "INSERT INTO categories (NAME ) VALUES (?)";
        Connection connection = pool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, couponType.name());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
    }

    //retrieve coupon type ID given type description (name)

    @Override
    public Long getIdCategory(CouponType couponType) {
        Long id = null;
        String sql = "SELECT * FROM categories WHERE NAME= ?";
        Connection connection = pool.getConnection();
        try (PreparedStatement prstm = connection.prepareStatement(sql)) {
            prstm.setString(1, couponType.name());
            ResultSet resultSet = prstm.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getLong("ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnConnetion(connection);
        }
        return id;
    }

        }


