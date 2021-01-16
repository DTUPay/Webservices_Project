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
        merchants.put(merchant.getCVR(), merchant);
    }

    @Override
    public void removeMerchant(String cvr) {
        merchants.remove(cvr);
    }

    @Override
    public Merchant getMerchant(String cvr) {
        return merchants.get(cvr);
    }

    @Override
    public boolean hasMerchant(String cvr) {
        System.out.println(merchants.toString());
        return merchants.containsKey(cvr);
    }
}
