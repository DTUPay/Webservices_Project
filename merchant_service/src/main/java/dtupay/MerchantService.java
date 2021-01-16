package dtupay;

import brokers.MerchantBroker;
import com.google.gson.Gson;
import exceptions.MerchantException;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Merchant;

import java.util.UUID;


/**
 * @author Mikkel & Benjamin
 */
@QuarkusMain
public class MerchantService {
    MerchantBroker broker;
    IMerchantRepository merchantRepository = MerchantRepository.getInstance();
    RestResponseHandler RestfulHandler = RestResponseHandler.getInstance();
    private static MerchantService instance = new MerchantService();
    Gson gson = new Gson();

    public MerchantService() {
        broker = new MerchantBroker(this);
    }
    public static MerchantService getInstance(){
        return instance;
    }

    public static void main(String[] args) {
        MerchantService service = MerchantService.getInstance();
        Quarkus.run();
    }


    public UUID registerMerchant(Merchant merchant) throws MerchantException {
        UUID merchantID;

        while(true) {
            merchantID = UUID.randomUUID();
            if(!merchantRepository.hasMerchant(merchantID)){
                break;
            }
        }

        merchant.setMerchantID(merchantID);
        merchantRepository.addMerchant(merchant);
        return merchant.getCustomerID();
    }

    // @Status: Implemented
    public void removeMerchant(UUID merchantID) throws MerchantException {
        System.out.println(merchantID);
        if (merchantRepository.hasMerchant(merchantID)) {
            merchantRepository.removeMerchant(merchantID);
        }
        else throw new MerchantException("Merchant with CVR: " + merchantID + " doesn't exist");
    }

    public Merchant getMerchant(UUID merchantID) throws MerchantException {
        if (merchantRepository.hasMerchant(merchantID)) {
            return merchantRepository.getMerchant(merchantID);
        }
        else throw new MerchantException("Merchant with CVR: " + merchantID + " doesn't exist");
    }
}
