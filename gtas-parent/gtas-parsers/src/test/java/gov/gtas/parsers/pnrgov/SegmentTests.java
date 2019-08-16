/*
 * All GTAS code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.parsers.pnrgov;

import gov.gtas.parsers.ParserTestHelper;
import gov.gtas.parsers.pnrgov.segment.*;
import org.junit.Test;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.exception.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.Assert.*;

public class SegmentTests implements ParserTestHelper {
  private static final String addy1 = "702:45 SOUTH PERRICK STREET:SLOUGH:BERKSHIRE:BRK:GB:SL1AA::LOC";
  private static final String addyphone = "700:45 SOUTH PERRICK STREET:SLOUGH:BERKSHIRE::GB:SL1AA:00441753637285";
  private static final String addylocation = "701:45 SOUTH PERRICK STREET:SLOUGH:BERKSHIRE::GB:SL1AA::XXX";
  private static final String addyemail = "702:45 SOUTH PERRICK STREET:SLOUGH:BERKSHIRE::GB:SL1AA:CTCE ANOTHERONE//GMAIL.COM:FOO";
  private static final String email = "702:::::::CTCE PEOPLE//WAHOO.COM";
  private static final String email2 = "700:::::::CTC PEOPLE//WAHOO.COM";
  private static final String email3 = "700:::::::PEOPLE//WAHOO.COM";
  private static final String location = "702::::::::IAD";
  private static final String phoneCoded = "702:::::::CTCP 00441753637285";
  private static final String all = "702:45 SOUTH PERRICK STREET:SLOUGH:BERKSHIRE:BRK:GB:SL1AA:00441753637285:HTH";
  private static final String nulls = ":::::::";
  private ADD add;
  private List<String> elements;

  // @Before
  // public void setUp() {
  // }

  @Test
  public void ADDAllFieldsTest() throws ParseException, IOException, URISyntaxException {
    elements = Arrays.asList(all.split(":"));
    Composite cmp = new Composite(elements);
    List<Composite> cmpList = new ArrayList<Composite>(2);
    cmpList.add(cmp);
    cmpList.add(cmp);

    this.add = new ADD(cmpList);

    assertTrue(this.add.getAddressType().equals("702"));
    assertTrue(add.getStreetNumberAndName().equals("45 SOUTH PERRICK STREET"));
    assertTrue(add.getCity().equals("SLOUGH"));
    assertTrue(add.getStateOrProvinceCode().equals("BERKSHIRE")); // Shouldn't this be the NAME??
    assertTrue(add.getStateOrProvinceName().equals("BRK"));       //and this be the CODE??
    assertTrue(add.getCountryCode().equals("GB"));
    assertTrue(add.getPostalCode().equals("SL1AA"));
    assertTrue(add.getTelephone().equals("00441753637285"));
    assertTrue(add.getLocation().equals("HTH"));
  }

  @Test
  public void ADDAddressFieldsTest() throws ParseException, IOException, URISyntaxException {
    elements = Arrays.asList(addy1.split(":"));
    Composite cmp = new Composite(elements);
    List<Composite> cmpList = new ArrayList<Composite>(2);
    cmpList.add(cmp);
    cmpList.add(cmp);

    this.add = new ADD(cmpList);

    assertTrue(this.add.getAddressType().equals("702"));
    assertTrue(add.getStreetNumberAndName().equals("45 SOUTH PERRICK STREET"));
    assertTrue(add.getCity().equals("SLOUGH"));
    assertTrue(add.getStateOrProvinceCode().equals("BERKSHIRE"));
    assertTrue(add.getStateOrProvinceName().equals("BRK"));
    assertTrue(add.getCountryCode().equals("GB"));
    assertTrue(add.getPostalCode().equals("SL1AA"));
    assertTrue(add.getLocation().equals("LOC"));
  }

  // Addy with email. Email populated, phone null
  @Test
  public void ADDAddressWithEmailTest() throws ParseException, IOException, URISyntaxException {
    elements = Arrays.asList(addyemail.split(":"));
    Composite cmp = new Composite(elements);
    List<Composite> cmpList = new ArrayList<Composite>(2);
    cmpList.add(cmp);
    cmpList.add(cmp);

    this.add = new ADD(cmpList);

    assertTrue(this.add.getAddressType().equals("702"));
    assertTrue(add.getStreetNumberAndName().equals("45 SOUTH PERRICK STREET"));
    assertTrue(add.getCity().equals("SLOUGH"));
    assertTrue(add.getStateOrProvinceCode().equals("BERKSHIRE"));
    assertTrue(add.getCountryCode().equals("GB"));
    assertTrue(add.getPostalCode().equals("SL1AA"));
    assertTrue(add.getLocation().equals("FOO"));
    assertTrue(add.getEmail().equals("ANOTHERONE@GMAIL.COM"));
    assertNull(add.getTelephone());
  }

  // Email in freetext field with CTCE code
  @Test
  public void ADDFindEmailWithCTCECodeTest() throws ParseException, IOException, URISyntaxException {
    elements = Arrays.asList(email.split(":"));
    Composite cmp = new Composite(elements);
    List<Composite> cmpList = new ArrayList<Composite>(2);
    cmpList.add(cmp);
    cmpList.add(cmp);

    this.add = new ADD(cmpList);

    assertTrue(this.add.getAddressType().equals("702"));
    assertTrue(add.getEmail().equals("PEOPLE@WAHOO.COM"));
    assertNull(add.getTelephone());
  }

  // Email in freetext field with CTC code
  @Test
  public void ADDFindEmailWithCtcCodeTest() throws ParseException, IOException, URISyntaxException {
    elements = Arrays.asList(email2.split(":"));
    Composite cmp = new Composite(elements);
    List<Composite> cmpList = new ArrayList<Composite>(2);
    cmpList.add(cmp);
    cmpList.add(cmp);

    this.add = new ADD(cmpList);

    assertTrue(this.add.getAddressType().equals("700"));
    assertTrue(add.getEmail().equals("PEOPLE@WAHOO.COM"));
    assertNull(add.getTelephone());
  }

  // TODO need better controls for the default.
  // Any data without code (including email data) should be treated as a phone number.
  @Test
  public void ADDFindEmailWithNoCodeTest() throws ParseException, IOException, URISyntaxException {
    elements = Arrays.asList(email3.split(":"));
    Composite cmp = new Composite(elements);
    List<Composite> cmpList = new ArrayList<Composite>(2);
    cmpList.add(cmp);
    cmpList.add(cmp);

    this.add = new ADD(cmpList);

    assertTrue(this.add.getAddressType().equals("700"));
    assertTrue(add.getTelephone().equals("PEOPLE//WAHOO.COM"));
    assertNull(add.getEmail());
  }

  @Test
  public void ADDPhoneTest() throws ParseException, IOException, URISyntaxException {
    elements = Arrays.asList(phoneCoded.split(":"));
    Composite cmp = new Composite(elements);
    List<Composite> cmpList = new ArrayList<Composite>(2);
    cmpList.add(cmp);
    cmpList.add(cmp);

    this.add = new ADD(cmpList);

    assertTrue(this.add.getAddressType().equals("702"));
    assertTrue(add.getTelephone().equals("00441753637285"));
    assertNull(add.getEmail());
  }

  @Test
  public void ADDNullsTest() throws ParseException, IOException, URISyntaxException {
    elements = Arrays.asList(nulls.split(":"));
    Composite cmp = new Composite(elements);
    List<Composite> cmpList = new ArrayList<Composite>(2);
    cmpList.add(cmp);
    cmpList.add(cmp);

    this.add = new ADD(cmpList);

    // all nulls, no exceptions tossed
    assertNull(add.getEmail());
    assertNull(this.add.getAddressType());
    assertNull(add.getStreetNumberAndName());
    assertNull(add.getCity());
    assertNull(add.getStateOrProvinceCode());
    assertNull(add.getStateOrProvinceName());
    assertNull(add.getCountryCode());
    assertNull(add.getPostalCode());
    assertNull(add.getTelephone());
    assertNull(add.getLocation());
  }



}
