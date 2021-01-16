package dtupay;


import models.Merchant;

import java.util.HashMap;

public class MerchantRepository implements IMerchantRepository {

    HashMap<String, Merchant> merchants = new HashMap<>();
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
    public void removeMerchant(String merchantID) {
        merchants.remove(merchantID);
    }

    @Override
    public Merchant getMerchant(String merchantID) {
        return merchants.get(merchantID);
    }

    @Override
    public boolean hasMerchant(String merchantID) {
        System.out.println(merchants.toString());
        return merchants.containsKey(merchantID);
    }
}
