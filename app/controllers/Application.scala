package controllers

import play.api.Play.current
import play.api.mvc._
import play.api.db._
import models.Report

object Application extends Controller {

  val query: String = """WITH category_lead AS (
    SELECT
      otro.SAILING_ID,
      otro.CATEGORY_GROUP_ID,
      otro.RATE_TYP_CD,
      otro.LEAD_PRICE
    FROM
      (SELECT
         blah.SAILING_ID,
         blah.CATEGORY_GROUP_ID,
--  blah.RATE_TYP_CD,
         Min(blah.LEAD_PRICE) AS LEAD_PRICE
       --  bb.RATE_TYP_DSC
       FROM crzcms_o_1.CRZ_SLNG_RT_TYP_PRC_A blah
         INNER JOIN (SELECT
                       RATE_TYP_CD,
                       RATE_TYP_DSC
                     FROM crzcms_o_1.CRZ_RATE_TYP
                     WHERE INCLD_SRCH_FLG = 'Y') bb ON blah.RATE_TYP_CD = bb.RATE_TYP_CD
       GROUP BY blah.sailing_id, blah.category_group_id) my
      INNER JOIN crzcms_o_1.CRZ_SLNG_RT_TYP_PRC_A otro ON
                                                         my.SAILING_ID = otro.SAILING_ID AND
                                                         my.CATEGORY_GROUP_ID = otro.CATEGORY_GROUP_ID AND
                                                         otro.LEAD_PRICE = my.LEAD_PRICE
    ORDER BY otro.sailing_id, otro.category_group_id )
SELECT
  ss.SAILING_ID,
  ss.CRUISE_ID,
  s.EXT_SAILING_ID,
  to_char(ss.SAILING_DATE, 'YYYY-MM-DD') AS SAILING_DATE,
  ss.EMBARKATION_PORT_ID,
  ss.REGION_ID,
  region.REGION_CODE,
  v.VENDOR_ID,
  v.VENDOR_CODE,
  ship.SHIP_ID,
  ship.VENDOR_SHIP_CODE,
  ss.LENGTH_OF_CRUISE,
  ss.VENDOR_NAME,
  ss.LOWEST_PRICE,
  inside.LEAD_PRICE                      AS LEAD_PRICE_INSIDE,
  inside.RATE_TYP_CD                     AS RATE_TYP_CD_INSIDE,
  ocean_view.LEAD_PRICE                  AS LEAD_PRICE_OCEAN_VIEW,
  ocean_view.RATE_TYP_CD                 AS RATE_TYP_CD_OCEAN_VIEW,
  balcony.LEAD_PRICE                     AS LEAD_PRICE_BALCONY,
  balcony.RATE_TYP_CD                    AS RATE_TYP_CD_BALCONY,
  suite.LEAD_PRICE                       AS LEAD_PRICE_SUITE,
  suite.RATE_TYP_CD                      AS RATE_TYP_CD_SUITE
FROM crzcms_o_1.crz_sailing s
  INNER JOIN crzcms_o_1.crz_slng_search ss ON s.SAILING_ID = ss.sailing_id
  INNER JOIN crzcms_o_1.crz_vendor v ON ss.vendor_id = v.VENDOR_ID
  INNER JOIN crzcms_o_1.crz_ship ship ON ship.SHIP_ID = ss.SHIP_ID
  INNER JOIN crzcms_o_1.crz_region region ON region.REGION_ID = ss.REGION_ID
  INNER JOIN category_lead inside ON (inside.CATEGORY_GROUP_ID = 1 AND s.SAILING_ID = inside.SAILING_ID)
  INNER JOIN category_lead ocean_view ON (ocean_view.CATEGORY_GROUP_ID = 3 AND s.SAILING_ID = ocean_view.SAILING_ID)
  INNER JOIN category_lead balcony ON (balcony.CATEGORY_GROUP_ID = 5 AND s.SAILING_ID = balcony.SAILING_ID)
  INNER JOIN category_lead suite ON (suite.CATEGORY_GROUP_ID = 7 AND s.SAILING_ID = suite.SAILING_ID);
                      """

  def index = Action {
    var outString = "Number is "
    val conn = DB.getConnection()
    var reportList: List[Report] = Nil;
    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery(query)
      while (rs.next()) {
        reportList = new Report(cruiseId = rs.getString("CRUISE_ID"),
          externalSailingId = rs.getString("EXT_SAILING_ID"),
          sailingDate = rs.getString("SAILING_DATE"),
          embarcationPortId = rs.getString("EMBARKATION_PORT_ID"),
          regionCode = rs.getString("REGION_CODE"),
          vendorCode = rs.getString("VENDOR_CODE"),
          shipCode = rs.getString("VENDOR_SHIP_CODE"),
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