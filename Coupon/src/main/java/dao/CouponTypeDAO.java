package dao;

import entities.CouponType;

public interface CouponTypeDAO {

    Boolean isExist(CouponType couponType );

    void addCategory(CouponType couponType);

    Long getIdCategory(CouponType couponType);

}


