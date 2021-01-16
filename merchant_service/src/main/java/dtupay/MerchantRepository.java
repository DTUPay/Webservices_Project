package dtupay;


import models.Merchant;

import java.util.HashMap;
import java.util.UUID;

public class MerchantRepository implements IMerchantRepository {

    HashMap<UUID, Merchant> merchants = new HashMap<>();
    private static MerchantRepository instance = new MerchantRepository();
    private MerchantRepository(){}

    //Get the only object available
    public static MerchantRepository getInstance(){
        return instance;
    }


    @Override
    public void addMerchant(Merchant merchant) {
        merchants.put(merchant.getMerchantID(), merchant);
    }

    @Override
    public void removeMerchant(UUID merchantID) {
        merchants.remove(merchantID);
    }

    @Override
    public Merchant getMerchant(UUID merchantID) {
        return merchants.get(merchantID);
    }

    @Override
    public boolean hasMerchant(UUID merchantID) {
        System.out.println(merchants.toString());
        return merchants.containsKey(merchantID);
    }
}
