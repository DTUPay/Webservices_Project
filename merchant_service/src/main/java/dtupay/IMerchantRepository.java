package dtupay;

import models.Merchant;

import java.util.UUID;

public interface IMerchantRepository {

    void addMerchant(Merchant merchant) ;

    void removeMerchant(UUID merchantID);

    Merchant getMerchant(UUID merchantID);

    boolean hasMerchant(UUID merchantID);

}
