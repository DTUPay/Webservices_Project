package dto;

import java.util.Date;
import java.util.UUID;

public class ReportRequestDTO extends PaymentDTO{
    private UUID merchantID;
    private Date startDate;
    private Date endDate;

    public UUID getMerchantID() {
        return this.merchantID;
    }

    public void setMerchantID(UUID merchantID) {
        this.merchantID = merchantID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
