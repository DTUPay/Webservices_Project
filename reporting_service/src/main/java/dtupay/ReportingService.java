/*
@author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen
 */

package dtupay;


import brokers.ReportingBroker;
import exceptions.TokenException;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import models.Token;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Oliver O. Nielsen & Rubatharisan Thirumathyam & Benjamin Eriksen & Mikkel Rosenfeldt Anderson
 */
@QuarkusMain
public class ReportingService {
    private static ReportingService instance = new ReportingService();
    ReportingBroker broker;
    public ReportingService() {
        broker = new ReportingBroker(this);
    }

    public static void main(String[] args) {
        ReportingService service = ReportingService.getInstance();
        Quarkus.run();
    }

    public static ReportingService getInstance(){
        return instance;
    }


}
