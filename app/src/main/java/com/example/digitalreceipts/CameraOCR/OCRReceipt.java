package com.example.digitalreceipts.CameraOCR;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OCRReceipt {

//    @SerializedName("message")
//    @Expose
//    public String message;

    @SerializedName("result")
    @Expose
    public Result result;

//    @SerializedName("success")
//    @Expose
//    public Boolean success;

//    @SerializedName("code")
//    @Expose
//    public Integer code;
//
//    public String getMessage() {
//        return message;
//    }

    public Result getData() {
        return result;
    }

//    public Boolean getSuccess() {
//        return success;
//    }
// code always is wrong (it thinks the receipt number is item code) or null, no use anyway
//    public Integer getCode() {
//        return code;
//    }

    public class Result {

        @SerializedName("establishment")
        @Expose
        public String establishment;

        @SerializedName("date")
        @Expose
        public String date;

        @SerializedName("total")
        @Expose
        public String total;

//        @SerializedName("address")
//        @Expose
//        public String address;

        @SerializedName("lineItems")
        @Expose
        public List<LineItem> lineItems;


//        @SerializedName("totalConfidence")
//        @Expose
//        public Double totalConfidence;

        @SerializedName("expenseType")
        @Expose
        public String expenseType;

        public String getEstablishment() {
            return establishment;
        }

        public String getDate() {
            return date;
        }

        public String getTotal() {
            return total;
        }

//        public String getAddress() {
//            return address;
//        }

        public List<LineItem> getLineItems() {
            return lineItems;
        }

//        public Double getTotalConfidence() {
//            return totalConfidence;
//        }

        public String getExpenseType() {

                return expenseType;
        }

        public class LineItem {


            // May not function. Take note. Failed a few tests
            @SerializedName("qty")
            @Expose
            public Integer qty;
            // Give this priority for description
            // no longer used, TBApi only looks for descClean
//            @SerializedName("desc")
//            @Expose
//            public String desc;

            // Suppose to give unit price, doesnt work usually. Use linetotal!
            @SerializedName("price")
            @Expose
            public String price;

            // priority 2 for description
            @SerializedName("descClean")
            @Expose
            public String descClean;
            // this one is basically total price for the items, if lets say got 7 items, means this
            //will give 7*unit_cost
            @SerializedName("lineTotal")
            @Expose
            public String lineTotal;

            public Integer getQty() {
                if(qty != 0)
                {
                    return qty;
                }
                return 1;

            }

//            public String getDesc() {
//                return desc;
//            }

            public String getDescClean() {
                if (descClean.contains("-"))
                {
                    String[] processedDescClean = descClean.split(" - ", 2);
                    System.out.println(processedDescClean[1]);
                    return processedDescClean[1];
                }
                else
                {
                    return descClean;
                }
            }

            public String getPrice() {
                return price;
            }


            public String getLineTotal() {
                // REGEX handling
                lineTotal.replaceAll("$", "");
                // checks if price is a promotion
//                if (lineTotal.contains("-"))
//                {
////                    String[] lineTotalS = lineTotal.split("-", 2);
////                    System.out.println(lineTotalS [1]);
////                    return lineTotalS[1];
//                    return new String("0");
//                }
//                else
//                {
//                    return lineTotal;
//                }
                return lineTotal;
            }
        }

    }

}



