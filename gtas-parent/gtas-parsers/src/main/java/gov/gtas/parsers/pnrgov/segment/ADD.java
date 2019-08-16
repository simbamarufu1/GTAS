/*
 * All GTAS code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.parsers.pnrgov.segment;

import java.util.List;

import com.hazelcast.internal.management.request.ForceStartNodeRequest;

import org.apache.commons.lang3.StringUtils;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.pnrgov.PnrUtils;

/**
 * <p>
 * ADD: Address Information
 * <p>
 * Address Information of a traveler in PNR
 * <p>
 * The ADD in GR.1 at level 2 may contain a contact address for the PNR.
 * <p>
 * The ADD in GR.2 at level 3 may contain emergency contact information and or/
 * UMNR delivery and collection addresses
 * <p>
 * The ADD in GR.4 at level 5 may contain the address of the payer of the
 * ticket.
 * <p>
 * If the address and/or telephone information cannot be broken down in separate
 * elements, the information may be found in OSIs and SSRs.
 * <p>
 * Ex:The contact address is 4532 Wilson Street, Philadelphia, zip code 34288
 * (ADD++700:4532 WILSON STREET:PHILADELPHIA:PA::US:34288’)
 * EMAIL
 * (ADD++700:::::::EK CTCE SOME.ABC//YAHOO.COM)
 * ADD++E:::::::FIRST.LAST@GMAIL.COM' //Issue 467
 * ADD++:::::::TBM MAIL TO+:::::::FIRST NAME LAST NAME:::::::99 STREET:::::::CITY STATE LONGZIPCODE'
 */
public class ADD extends Segment {
    private String addressType;
    private String streetNumberAndName;
    private String city;
    private String stateOrProvinceCode;
    private String stateOrProvinceName;
    private String countryCode;
    private String postalCode;
    private String location;
    private String telephone;
    private String email;
    private String freeText;

    /*
    * Taken from section 5.2 (ADD) phone for PNR 13.1
    * */
    private static final int ADDRESS_PURPOSE_CODE = 0;
    private static final int STREET_AND_NUMBER_PO_BOX = 1;
    private static final int CITY_NAME = 2;
    private static final int STATE_OR_PROVINCE_CODE = 3;
   private static final int STATE_OR_PROVINCE_NAME = 4;
    private static final int COUNTRY_CODE = 5;
    private static final int POSTAL_CODE = 6;
    private static final int FREE_TEXT = 7;
   private static final int PLACE_LOCATION = 8;

    /*The following variables are my BEST GUESS on what the numbers parse.
    As I do not have a 11.1 spec I am primarily trying to move the magic numbers from the code and
    into an initialization block. Best guess for PNR 11.1!
    * */
    private static final int FIRST_ELEMENT = 0;
    private static final int POTENTIAL_MAIL_TO_BLOCK = 7;
    private static final int EMAIL_INFORMATION_COMPOSITE = 2;
    private static final int STREET_NUMBER_AND_NAME = 14;
    private static final int CITY_STATE_ZIPCODE = 21;

    public ADD(List<Composite> composites) {
      super(ADD.class.getSimpleName(), composites);
      Composite c = getComposite(1);

      if (c == null) return;

      addressType = c.getElement(ADDRESS_PURPOSE_CODE);
      streetNumberAndName = c.getElement(STREET_AND_NUMBER_PO_BOX);
      city = c.getElement(CITY_NAME);
      stateOrProvinceCode = c.getElement(STATE_OR_PROVINCE_CODE);
      stateOrProvinceName = c.getElement(STATE_OR_PROVINCE_NAME);
      countryCode = c.getElement(COUNTRY_CODE);
      postalCode = c.getElement(POSTAL_CODE);
      location = c.getElement(PLACE_LOCATION);
      freeText = c.getElement(FREE_TEXT);

      email = PnrUtils.extractEmailByCode(freeText);
      telephone = PnrUtils.extractPhoneByCodeOrRaw(freeText);
    }

    // TODO - appears to be custom, not to any spec. Waiting for confirmation that its still required
    // This is *NOT* to the PNR 13.1 specification. We suspect this is PNR 11.1 logic and will attempt to parse as such when the first element is E, M, H, or O.
    // private void parseSuspectedPnrGov11_1(Composite c, String freeText) {

    //     if ((StringUtils.isNotBlank(c.getElement(FIRST_ELEMENT)) && ("E".equalsIgnoreCase(c.getElement(FIRST_ELEMENT))))) {
    //         this.email = freeText;
    //     }
    //     if ((StringUtils.isNotBlank(c.getElement(FIRST_ELEMENT)) && ("M".equalsIgnoreCase(c.getElement(FIRST_ELEMENT))))) {
    //         this.telephone = freeText;
    //     }
    //     if ((StringUtils.isNotBlank(c.getElement(FIRST_ELEMENT)) && ("H".equalsIgnoreCase(c.getElement(FIRST_ELEMENT))))) {
    //         this.telephone = freeText;
    //     }
    //     if ((StringUtils.isNotBlank(c.getElement(FIRST_ELEMENT)) && ("O".equalsIgnoreCase(c.getElement(FIRST_ELEMENT))))) {
    //         this.telephone = freeText;
    //     }
    //     //ADD++N:::::::M+1234567890' the number of :'s may vary.
    //     //Data | phone | ADD++N:::::::M+ #581 fix
    //     if ((StringUtils.isNotBlank(c.getElement(FIRST_ELEMENT)) && ("N".equalsIgnoreCase(c.getElement(FIRST_ELEMENT))))) {
    //         List<Composite> composits = getComposites();
    //         Composite lastOne = composits.get(composits.size() - 1);
    //         if (StringUtils.isNotBlank(lastOne.getElement(FIRST_ELEMENT))) {
    //             this.telephone = lastOne.getElement(FIRST_ELEMENT);
    //         }
    //     }
    //     if (StringUtils.isBlank(c.getElement(FIRST_ELEMENT)) && StringUtils.isNotBlank(c.getElement(POTENTIAL_MAIL_TO_BLOCK))
    //             && c.getElement(POTENTIAL_MAIL_TO_BLOCK).contains("TBM")) {
    //         //ADD++:::::::TBM MAIL TO+:::::::FIRST NAME LAST NAME:::::::99 STREET:::::::CITY STATE LONGZIPCODE'
    //         Composite c2 = getComposite(EMAIL_INFORMATION_COMPOSITE);
    //         int CITY = 0;
    //         int STATE = 1;
    //         int POST_CODE = 2;
    //         if (c2 != null) {
    //             this.addressType = "MAIL TO";
    //             this.streetNumberAndName = c2.getElement(STREET_NUMBER_AND_NAME);
    //             String temp = c2.getElement(CITY_STATE_ZIPCODE);
    //             if (StringUtils.isNotBlank(temp)) {
    //                 String[] tokens = temp.split(" ");
    //                 if (tokens.length >= 3) {
    //                     this.city = tokens[CITY];
    //                     this.stateOrProvinceCode = tokens[STATE];
    //                     this.postalCode = tokens[POST_CODE];
    //                 }
    //             }
    //         }
    //     }
    // }

    public String getEmail() {
      return email;
    }

	public String getAddressType() {
        return addressType;
    }

    public String getStreetNumberAndName() {
        return streetNumberAndName;
    }

    public String getCity() {
        return city;
    }

    public String getStateOrProvinceCode() {
        return stateOrProvinceCode;
    }

    public String getStateOrProvinceName() {
      return stateOrProvinceName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getLocation() {
      return location;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getFreeText() {
      return freeText;
    }
}
