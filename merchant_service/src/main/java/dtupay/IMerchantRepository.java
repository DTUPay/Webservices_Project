package dtupay;

import models.Merchant;

public interface IMerchantRepository {

    void addMerchant(Merchant merchant) ;

    void removeMerchant(String merchantID);

    Merchant getMerchant(String merchantID);

    boolean hasMerchant(String merchantID);

}
