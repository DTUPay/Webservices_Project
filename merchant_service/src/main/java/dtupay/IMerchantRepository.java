package dtupay;

import models.Merchant;

public interface IMerchantRepository {

    void registerMerchant(Merchant merchant) throws Exception;

    void deleteMerchant(String cpr) throws Exception;

}
