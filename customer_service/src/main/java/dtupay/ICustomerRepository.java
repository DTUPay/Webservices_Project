package dtupay;

public interface ICustomerRepository {

    void registerCustomer(dtupay.Customer customer) throws Exception;

    void deleteCustomer(String cpr) throws Exception;

}
