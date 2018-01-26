package com.zyt.HttpUtil.Bean;

/**
 * Created by chenbifeng on 16/8/1.
 */
public class GSCXBean {
    private String tpBuildingName;
    private String taxPriceId;
    private String tpDate;
    private String tpPrice;
    private String tpBuildingNo;
    private String tpFloor;
    private String tpRemarks;

    public String getTpBuildingName() {
        return tpBuildingName;
    }

    public void setTpBuildingName(String tpBuildingName) {
        this.tpBuildingName = tpBuildingName;
    }

    public String getTaxPriceId() {
        return taxPriceId;
    }

    public void setTaxPriceId(String taxPriceId) {
        this.taxPriceId = taxPriceId;
    }

    public String getTpDate() {
        return tpDate;
    }

    public void setTpDate(String tpDate) {
        this.tpDate = tpDate;
    }

    public String getTpPrice() {
        return tpPrice;
    }

    public void setTpPrice(String tpPrice) {
        this.tpPrice = tpPrice;
    }

    public String getTpBuildingNo() {
        return tpBuildingNo;
    }

    public void setTpBuildingNo(String tpBuildingNo) {
        this.tpBuildingNo = tpBuildingNo;
    }

    public String getTpFloor() {
        return tpFloor;
    }

    public void setTpFloor(String tpFloor) {
        this.tpFloor = tpFloor;
    }

    public String getTpRemarks() {
        return tpRemarks;
    }

    public void setTpRemarks(String tpRemarks) {
        this.tpRemarks = tpRemarks;
    }

    @Override
    public String toString() {
        return "GSCXBean{" +
                "tpBuildingName='" + tpBuildingName + '\'' +
                ", taxPriceId='" + taxPriceId + '\'' +
                ", tpDate='" + tpDate + '\'' +
                ", tpPrice=" + tpPrice +
                ", tpBuildingNo='" + tpBuildingNo + '\'' +
                ", tpFloor='" + tpFloor + '\'' +
                ", tpRemarks='" + tpRemarks + '\'' +
                '}';
    }
}
