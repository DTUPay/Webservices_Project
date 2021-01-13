package dtupay;


import models.Merchant;

import java.util.HashMap;

public class MerchantRepository implements IMerchantRepository {

    HashMap<String, Merchant> merchants = new HashMap<>();

    @Override
    public void registerMerchant(Merchant merchant) throws Exception {
        merchants.put(merchant.getCVR(), merchant);
    }

    @Override
    public void deleteMerchant(String cpr) throws Exception {
        merchants.remove(cpr);
    }
}
