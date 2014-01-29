package controllers

import play.api.Play.current
import play.api.mvc._
import play.api.db._
import models.Report

object Application extends Controller {

  val query:String = "SELECT\n  ss.CRUISE_ID,\n  s.EXT_SAILING_ID,\n  ss.SAILING_DATE,\n  EMBARKATION_PORT_ID,\n  REGION_ID,\n  VENDOR_ID,\n  SHIP_ID,\n  LENGTH_OF_CRUISE,\n  VENDOR_NAME,\n  LOWEST_PRICE\nFROM crzcms_o_1.crz_sailing s inner join crzcms_o_1.crz_slng_search ss on s.sailing_id = ss.sailing_id"

  def index = Action {
    var outString = "Number is "
    val conn = DB.getConnection()
    var reportList:List[Report] = Nil;
    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery(query)
      while (rs.next()) {
        reportList = new Report(cruiseId = rs.getString("CRUISE_ID"),
                                externalSailingId = rs.getString("EXT_SAILING_ID"),
                                sailingDate = rs.getString("SAILING_DATE"),
                                embarcationPortId = rs.getString("EMBARKATION_PORT_ID"),
                                regionId = rs.getString("REGION_ID"),
                                vendorId = rs.getString("VENDOR_ID"),
                                shipId = rs.getString("SHIP_ID"),
                                lenghtOfCruise = rs.getString("LENGTH_OF_CRUISE"),
                                vendorName = rs.getString("VENDOR_NAME"),
                                lowestPrice = rs.getString("LOWEST_PRICE")) :: reportList
      }
    } finally {
      conn.close()
    }
    Ok(views.html.index(reportList))
  }

}