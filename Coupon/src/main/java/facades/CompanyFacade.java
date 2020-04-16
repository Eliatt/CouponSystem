package facades;


import dbdao.CouponTypeDBDAO;
import entities.Coupon;
import entities.CouponType;
import exceptions.ExistException;
import exceptions.LoginException;
import exceptions.NotExistException;

import java.time.LocalDate;
import java.util.List;


public class CompanyFacade extends ClientFacade {

    private CouponTypeDBDAO couponTypeDBDAO = new CouponTypeDBDAO();
    private Long companyId = null;


    @Override
    public boolean login(String email, String password) {
        if (companyDBDAO.isCompanyExists(email, password)) {
            companyId = companyDBDAO.getByEmailAndPassword(email, password).getId();
            System.out.println("Successful login");
            return true;

        } else {
            companyId = null;
            System.out.println("Error logging in");
            return false;
        }
    }

    public Coupon createCoupon(CouponType couponType, String title, String description, LocalDate startDate
            , LocalDate endDate, int amount, double price, String image) throws LoginException {
        if (companyId == null) {
            throw new LoginException("Please log in");
        }
        if (!couponTypeDBDAO.isExist(couponType)) {
            couponTypeDBDAO.addCategory(couponType);
        }
        Long categoryId = couponTypeDBDAO.getIdCategory(couponType);
        Coupon newCoupon = new Coupon( companyId,categoryId, title, description, startDate, endDate, amount, price, image);

        return newCoupon;
    }

    public void addCoupon(Coupon coupon) throws LoginException, ExistException {
        if (companyId == null) {
            throw new LoginException("please log in");
        }
        if (coupon != null) {
            if (couponDBDAO.isCouponExists(coupon.getCompanyId(), coupon.getTitle())) {
                throw new ExistException("the company already have " + coupon.getTitle() + "  coupon");
            }
            couponDBDAO.addCoupon(coupon);
        }
    }

    public void updateCoupon(Coupon coupon) throws LoginException {
        if (companyId == null) {
            throw new LoginException("Please login");
        }
        Coupon coupon1 = null;
        if (coupon != null && coupon.getCompanyId().equals(companyId)) {
            coupon1 = couponDBDAO.getOneCoupon(coupon.getId());
            coupon1.setCategoryId(coupon.getCategoryId());
            coupon1.setTitle(coupon.getTitle());
            coupon1.setDescription(coupon.getDescription());
            coupon1.setStartDate(coupon.getStartDate());
            coupon1.setEndDate(coupon.getEndDate());
            coupon1.setAmount(coupon.getAmount());
            coupon1.setPrice(coupon.getPrice());
            coupon1.setImage(coupon.getImage());
        }
        couponDBDAO.updateCoupon(coupon);

    }

    public void deleteCoupon(Long couponId) throws LoginException, NotExistException {

        if (companyId == null) {
            throw new LoginException("Please login");
        }
        Coupon coupon = couponDBDAO.getOneCoupon(couponId);
        System.out.println(coupon);
        if (coupon == null || !coupon.getId().equals(couponId)) {
            throw new NotExistException("Coupon not found ");
        }
        couponDistributionDBDAO.revokePurchaseByCouponId(couponId);
        couponDBDAO.deleteCoupon(couponId);
    }

    public List<Coupon> getCompanyCoupons(Long companyId) throws LoginException {
        if (companyId == null) {
            throw new LoginException("Please login");
        }
        List<Coupon> all = couponDBDAO.getCompanyCoupons(companyId);
        return all;
    }

    public List<Coupon> getCompanyCouponsByCategory(CouponType couponType) throws LoginException {
        List<Coupon> couponsByType = null;
        if (companyId == null) {
            throw new LoginException("you need to login");
        }
        Long categoryId = couponTypeDBDAO.getIdCategory(couponType);
        if (categoryId != null) {
            couponsByType = couponDBDAO.getAllCompanyCouponsByType(companyId, categoryId);
        }
        return couponsByType;
    }

    public List<Coupon> getCompanyCouponsByMaxPrice(Double maxPrice) throws LoginException {
        List<Coupon> allCompanyCouponsByMaxPrice = null;
        if (companyId == null) {
            throw new LoginException("you need to login");
        }
        if (maxPrice != null && maxPrice > 0) {
            allCompanyCouponsByMaxPrice = couponDBDAO.getCompanyCouponsByMaxPrice(companyId, maxPrice);
        }
        return allCompanyCouponsByMaxPrice;
    }
    public Coupon getCouponById(Long id) {
        if (id != null) {
            Coupon coupon = couponDBDAO.getOneCoupon(id);
            if (coupon != null && coupon.getCompanyId().equals(companyId)) {
                return coupon;
            }
        }
        return null;
    }

    }











