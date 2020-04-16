package utils;

import entities.ClientType;
import facades.AdminFacade;
import facades.ClientFacade;
import facades.CompanyFacade;
import facades.CustomerFacade;


public class LoginManager {
    private static LoginManager instance = null;

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        if (instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }
// login options by intended user: Admin, Company, or Customer

    public ClientFacade login(String email, String password, ClientType clientType) throws Exception {
        switch (clientType) {
            case ADMIN:
                AdminFacade adminFacade = new AdminFacade();
                if (adminFacade.login(email, password)) {
                    return adminFacade;
                }
                break;
            case COMPANY:
                CompanyFacade companyFacade = new CompanyFacade();
                if (companyFacade.login(email, password)) {
                    return companyFacade;
                }
                break;
            case CUSTOMER:
                CustomerFacade customerFacade = new CustomerFacade();
                if (customerFacade.login(email, password)) {
                    return customerFacade;
                }
                break;
            default:
        }
        return null;
    }


}
