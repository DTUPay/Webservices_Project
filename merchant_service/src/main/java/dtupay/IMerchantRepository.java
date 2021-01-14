package dtupay;

import models.Merchant;

public interface IMerchantRepository {

    void addMerchant(Merchant merchant) ;

    void removeMerchant(String cvr);

    Merchant getMerchant(String cvr);

    boolean hasMerchant(String cvr);

}
