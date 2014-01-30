package models

/**
 * select ss.CRUISE_ID, ss.SAILING_DATE, EMBARKATION_PORT_ID, REGION_ID, VENDOR_ID, SHIP_ID, LENGTH_OF_CRUISE, VENDOR_NAME, LOWEST_PRICE
from crzcms_o_1.crz_sailing s, crzcms_o_1.crz_slng_search ss
where s.sailing_id = ss.sailing_id
 */
class Report(val cruiseId:String = "CruiseId",
             val externalSailingId:String = "External Sailing Id",
             val sailingDate: String = "01/01/2000",
             val embarcationPortId: String = "embarkationPortId" ,
             val regionCode:String = "regionId",
             val vendorCode:String ="vendorId",
             val shipCode:String = "ShipId",
             val lenghtOfCruise:String = "lengthOfCruise",
             val vendorName: String = "VendorName",
             val lowestPrice:String = "lowestPrice") {

}
