package gov.gtas.parsers.pnrgov.enums;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum FreetextCodes {
  CONTACT("CTC"),
  ADDRESS("CTCA"),
  BUSINESS_PHONE("CTCB"),
  EMAIL("CTCE"),
  FAX("CTCF"),
  HOME_PHONE("CTCH"),
  MOBILE_PHONE("CTCM"),
  PHONE_NATURE_NOT_KNOWN("CTCP"),
  CONTACT_INFO_REFUSED("CTCR"),
  TRAVEL_AGENT("CTCT"),
  RECORD_LOCATOR("RLOC")
  ;

  private String code;

  FreetextCodes(String code) {
      this.code = code;
  }

  private static final Map<String, FreetextCodes> stringToEnum =
          Stream.of(values()).collect(
                  toMap(Object::toString, e -> e));

  public static Optional<FreetextCodes> fromString(String code) {
      return Optional.ofNullable(stringToEnum.get(code));
  }

  // Excludes RLOC
  public static boolean isContactCode(String text) {
    String txt = text.trim();
    return txt.equals(CONTACT.toString()) || txt.equals(ADDRESS.toString()) ||
      txt.equals(BUSINESS_PHONE.toString()) || txt.equals(EMAIL.toString()) ||
      txt.equals(FAX.toString()) || txt.equals(HOME_PHONE.toString()) ||
      txt.equals(MOBILE_PHONE.toString()) || txt.equals(PHONE_NATURE_NOT_KNOWN.toString()) ||
      txt.contains(CONTACT_INFO_REFUSED.toString()) || txt.contains(TRAVEL_AGENT.toString());
  }

  public static boolean isCtcCode(String text) {
    String txt = text.trim();
    return txt.equals(CONTACT.toString());
  }

  public static boolean isEmailCode(String text) {
    String txt = text.trim();
    return txt.equals(EMAIL.toString());
  }

  // PNR spec 13.1 allows email data after just a CTC code
  public static boolean isEmailOrCtcCode(String text) {
    String txt = text.trim();
    return isEmailCode(txt) || isCtcCode(txt);
  }

  // fax?!?!?
  public static boolean isPhoneCode(String text) {
    String txt = text.trim();
    return txt.equals(BUSINESS_PHONE.toString()) || txt.equals(HOME_PHONE.toString()) ||
    txt.equals(MOBILE_PHONE.toString()) || txt.equals(PHONE_NATURE_NOT_KNOWN.toString()) ||
    txt.equals(TRAVEL_AGENT.toString()) || txt.equals(FAX.toString());
  }

  public static boolean isPhoneOrCtcCode(String text) {
    String txt = text.trim();
    return isPhoneCode(txt) || isCtcCode(text);
  }

  public static boolean isAddressCode(String text) {
    String txt = text.trim();
    return txt.equals(ADDRESS.toString());
  }

  public static boolean isAddressOrCtcCode(String text) {
    String txt = text.trim();
    return isAddressCode(txt) || isCtcCode(txt);
  }

  @Override
  public String toString() {
      return this.code;
  }

}
