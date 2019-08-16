/*
 * All GTAS code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.parsers.pnrgov;

import static org.junit.Assert.*;

import org.eclipse.persistence.internal.sessions.DirectCollectionChangeRecord.NULL;

import gov.gtas.parsers.pnrgov.PnrUtils;

import org.junit.Test;

import gov.gtas.parsers.edifact.EdifactLexer;

public class PnrUtilsTest {

    @Test
    public void testgetSinglePnr() {
        String msg = "UNA:+.?*'\n" + 
                "UNB+IATA:1+DL++101209:2100+020A07'\n" + 
                "UNH+1+PNRGOV:10:1:IA+F6C2C268'\n" + 
                "MSG+:22'\n" + 
                "ORG+DL:ATL+52519950'\n" + 
                "TVL+121210:0915+LHR+JFK+DL+324'\n" + 
                "EQN+2'" + 
                "SRC'" + 
                "RCI+DL:MFN4TI1'" +
                "SRC'" + 
                "RCI+DL:MFN4TI2'" +
                "SRC'" + 
                "RCI+DL:MFN4TI3'" +
                "UNT+135+1'\n" + 
                "UNZ+1+020A07'";

        EdifactLexer lexer = new EdifactLexer(msg);
        assertEquals(null, PnrUtils.getSinglePnr(lexer, -1));
        assertEquals("SRC'RCI+DL:MFN4TI1'", PnrUtils.getSinglePnr(lexer, 0));
        assertEquals("SRC'RCI+DL:MFN4TI2'", PnrUtils.getSinglePnr(lexer, 1));
        assertEquals("SRC'RCI+DL:MFN4TI3'", PnrUtils.getSinglePnr(lexer, 2));
        assertEquals(null, PnrUtils.getSinglePnr(lexer, 3));
    }

    @Test
    public void isAirimpEmailTest() {
      // standard English language format
      String email = "CHRIS.TRAVELER//YAHOO.COM";
      boolean res = PnrUtils.isAirimpEmail(email);
      assertTrue(res);

      // leading/trailing spaces OK
      String email1 = "  CHRIS.TRAVELER//YAHOO.COM  ";
      boolean res1 = PnrUtils.isAirimpEmail(email1);
      assertTrue(res1);

      // multiple periods OK
      String email16 = "  CHRIS.TRA.VELER//YAHOO.GMAIL.OVALTINE.COM  ";
      boolean res16 = PnrUtils.isAirimpEmail(email16);
      assertTrue(res16);
      
      // Unicode Language tests, should all return true
      // Devanagari! 
      String email13 = " संपर्क//डाटामेल.भारत ";
      boolean res13 = PnrUtils.isAirimpEmail(email13);
      assertTrue(res13);

      // Cyrillic!
      String email14 = "медведь//с-балалайкой.рф";
      boolean res14 = PnrUtils.isAirimpEmail(email14);
      assertTrue(res14);

      // Japanese!
      String email15 = "二ノ宮//黒川.日本";
      boolean res15 = PnrUtils.isAirimpEmail(email15);
      assertTrue(res15);


      // Remaining tests for invalid patterns

      // two encodeed "@" signs (as "//")
      String email2 = "CHRIS//TRAVELER//YAHOO.COM";
      boolean res2 = PnrUtils.isAirimpEmail(email2);
      assertFalse(res2);

      // no .XX suffix
      String email3 = "CHRIS.TRAVELER//YAHOO";
      boolean res3 = PnrUtils.isAirimpEmail(email3);
      assertFalse(res3);

      // underscore (disallowed by AIRIMP)
      String email4 = "CHRIS_TRAVELER//YAHOO.COM";
      boolean res4 = PnrUtils.isAirimpEmail(email4);
      assertFalse(res4);

      // spaces
      String email5 = "CHRIS TRAVELER//YAHOO.COM  ";
      boolean res5 = PnrUtils.isAirimpEmail(email5);
      assertFalse(res5);

      //special chars, returns false
      String email6 = "CHR!ISTRAVELER.YAHOO.COM//GMAIL";
      boolean res6 = PnrUtils.isAirimpEmail(email6);
      assertFalse(res6);

      //extra slash
      String email7 = "CHRISTRAVELER///YAHOO.COM";
      boolean res7 = PnrUtils.isAirimpEmail(email7);
      assertFalse(res7);

      // no "//"
      String email8 = "CHRIS.TRAVELER.YAHOO.COM";
      boolean res8 = PnrUtils.isAirimpEmail(email8);
      assertFalse(res8);

      // top-level domain name too short
      String email9 = "CHRIS.TRAVELER//YAHOO.C";
      boolean res9 = PnrUtils.isAirimpEmail(email9);
      assertFalse(res9);

      // numeric value in domain name
      String email10 = "OTHERDATA/CHRIS.TRAVELER//YAHOO.CO3";
      boolean res10 = PnrUtils.isAirimpEmail(email10);
      assertFalse(res10);

      String email11 = null;
      boolean res11 = PnrUtils.isAirimpEmail(email11);
      assertFalse(res11);

      String email12 = "";
      boolean res12 = PnrUtils.isAirimpEmail(email12);
      assertFalse(res12);
    }

    @Test
    public void decodeAirimpEmailTest() {
      String email = "CHRIS..TRAVELER//YAHOO.COM";
      String res = PnrUtils.decodeAirimpEmail(email);
      assertEquals(res, "CHRIS_TRAVELER@YAHOO.COM");

      String email1 = "CHRIS_TRAVELER//YAHOO.COM  ";
      String res1 = PnrUtils.decodeAirimpEmail(email1);
      assertEquals(res1, email1); //unchanged

      String email2 = "CTCE CHRIS..TRAVELER//YAHOO.COM";
      String res2 = PnrUtils.decodeAirimpEmail(email2);
      assertEquals(res2, email2); //unchanged

      String email3 = "CHRIS.TRAVELER//YAHOO/OTHERDATA";
      String res3 = PnrUtils.decodeAirimpEmail(email3);
      assertEquals(res3, email3); //unchanged
    }


    @Test
    public void extractEmailByCodeTest() {
      // excludes the carrier marker
      String email1 = "YY CTCE EMAIL//ONE.COM";
      String res = PnrUtils.extractEmailByCode(email1);
      assertEquals(res, "EMAIL@ONE.COM");

      // excludes the carrier and city marker
      String email2 = "YY CTY CTCE EMAIL//TWO.COM";
      String res2 = PnrUtils.extractEmailByCode(email2);
      assertEquals(res2, "EMAIL@TWO.COM");

      // ignores leading/trailing whitespace
      String email3 = " CTCE EMAIL//THREE.COM    ";
      String res3 = PnrUtils.extractEmailByCode(email3);
      assertEquals(res3, "EMAIL@THREE.COM");

      // accepts CTC codes
      String email4 = "CTC EMAIL//FOUR.COM";
      String res4 = PnrUtils.extractEmailByCode(email4);
      assertEquals(res4, "EMAIL@FOUR.COM");

      // REJECTS non-email codes (CTCA)
      String email5 = "CTCA EMAIL//FIVE.COM";
      String res5 = PnrUtils.extractEmailByCode(email5);
      assertNull(res5);

      // REJECTS non-email codes (CTCB)
      String email6 = "CTCB EMAIL//SIX.COM";
      String res6 = PnrUtils.extractEmailByCode(email6);
      assertNull(res6);

      // REJECTS email-like codes more than 2 elements after the CTC/CTCE code
      String email7 = "CTCE XXX ZZ EMAIL-LIKE-TEXT//SEVEN.PEM";
      String res7 = PnrUtils.extractEmailByCode(email7);
      assertNull(res7);

      // Returns null for a null string
      String email8 = null;
      String res8 = PnrUtils.extractEmailByCode(email8);
      assertNull(res8);

      // Returns null for an empty string
      String email9 = "";
      String res9 = PnrUtils.extractEmailByCode(email9);
      assertNull(res9);
    }

    @Test
    public void extractPhoneByCodeOrRawTest() {
      // CTCP code
      String phone = "CTCP 2039495 30-9538";
      String res = PnrUtils.extractPhoneByCodeOrRaw(phone);
      assertEquals("2039495 30-9538", res);

      // CTC code
      String phone2 = "CTC 2039495 30-9538";
      String res2 = PnrUtils.extractPhoneByCodeOrRaw(phone2);
      assertEquals("2039495 30-9538", res2);

      // no code
      String phone3 = "2039495 30-9538";
      String res3 = PnrUtils.extractPhoneByCodeOrRaw(phone3);
      assertEquals("2039495 30-9538", res3);

      // extra spaces
      String phone4 = "  2039495 30-9538  ";
      String res4 = PnrUtils.extractPhoneByCodeOrRaw(phone4);
      assertEquals(phone4, res4);

      // CTCH phone code
      String phone5 = "CTCH 2039495 30-9538";
      String res5 = PnrUtils.extractPhoneByCodeOrRaw(phone5);
      assertEquals("2039495 30-9538", res5);

      // CTCM phone code
      String phone6 = "CTCM 2039495 30-9538";
      String res6 = PnrUtils.extractPhoneByCodeOrRaw(phone6);
      assertEquals("2039495 30-9538", res6);

      // CTCT phone code
      String phone7 = "CTCT 2039495 30-9538";
      String res7 = PnrUtils.extractPhoneByCodeOrRaw(phone7);
      assertEquals("2039495 30-9538", res7);

      // CTCB phone code
      String phone8 = "CTCB 2039495 30-9538";
      String res8 = PnrUtils.extractPhoneByCodeOrRaw(phone8);
      assertEquals("2039495 30-9538", res8);

      // CTCF phone code
      String phone10 = "CTCF 2039495 30-9538";
      String res10 = PnrUtils.extractPhoneByCodeOrRaw(phone10);
      assertEquals("2039495 30-9538", res10);

      // CTCB phone code with carrier code
      String phone9 = "YY CTCB 2039495 30-9538";
      String res9 = PnrUtils.extractPhoneByCodeOrRaw(phone9);
      assertEquals("2039495 30-9538", res9);

      // empty string
      String phone11 = "";
      String res11 = PnrUtils.extractPhoneByCodeOrRaw(phone11);
      assertNull(res11);

      // null string
      String phone12 = null;
      String res12 = PnrUtils.extractPhoneByCodeOrRaw(phone12);
      assertNull(res12);

      // CTCA address phone code, should return null
      String phone13 = "CTCA 2039495 30-9538";
      String res13 = PnrUtils.extractPhoneByCodeOrRaw(phone13);
      assertNull(res13);

      // CTCE email phone code, should return null
      String phone14 = "CTCE 2039495 30-9538";
      String res14 = PnrUtils.extractPhoneByCodeOrRaw(phone14);
      assertNull(res14);
    }
}
