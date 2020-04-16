package dao;

import entities.Company;

import java.util.List;

public interface CompanyDAO {

    Boolean isCompanyExists(String email, String password);

    void addCompany(Company company);

    void updateCompany(Company company);

    Company deleteCompany(Long companyId);

    List<Company> getAllCompanies();

    Company getOneCompany(Long id);


}

