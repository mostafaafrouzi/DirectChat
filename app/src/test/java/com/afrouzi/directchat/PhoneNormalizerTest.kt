package com.afrouzi.directchat

import com.afrouzi.directchat.domain.engine.PhoneNormalizer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PhoneNormalizerTest {

    @Test
    fun testPersianAndArabicDigitsConversion() {
        val persian = "۰۹۱۲۳۴۵۶۷۸۹"
        val arabic = "٠٩١٢٣٤٥٦٧٨٩"
        assertEquals("09123456789", PhoneNormalizer.toEnglishDigits(persian))
        assertEquals("09123456789", PhoneNormalizer.toEnglishDigits(arabic))
    }

    @Test
    fun testIranianMobileParsingVariants() {
        // Standard domestic starting with 09
        val p1 = PhoneNormalizer.parse("09123456789")
        assertTrue(p1.isIranian)
        assertTrue(p1.isValid)
        assertEquals("09123456789", p1.nationalFormat)
        assertEquals("+989123456789", p1.internationalWithPlus)
        assertEquals("989123456789", p1.internationalNoPlus)

        // With Persian digits and spaces
        val p2 = PhoneNormalizer.parse("۰۹۱۲ ۳۴۵ ۶۷۸۹")
        assertTrue(p2.isIranian)
        assertTrue(p2.isValid)
        assertEquals("09123456789", p2.nationalFormat)

        // Starting with 989
        val p3 = PhoneNormalizer.parse("989123456789")
        assertTrue(p3.isIranian)
        assertTrue(p3.isValid)
        assertEquals("09123456789", p3.nationalFormat)
        assertEquals("+989123456789", p3.internationalWithPlus)

        // Starting with +989
        val p4 = PhoneNormalizer.parse("+989123456789")
        assertTrue(p4.isIranian)
        assertTrue(p4.isValid)
        assertEquals("09123456789", p4.nationalFormat)
        assertEquals("+989123456789", p4.internationalWithPlus)

        // Starting with 00989
        val p5 = PhoneNormalizer.parse("00989123456789")
        assertTrue(p5.isIranian)
        assertTrue(p5.isValid)
        assertEquals("09123456789", p5.nationalFormat)
    }

    @Test
    fun testInternationalNumbers() {
        val pUs = PhoneNormalizer.parse("+12025550123")
        assertFalse(pUs.isIranian)
        assertTrue(pUs.isValid)
        assertEquals("+12025550123", pUs.internationalWithPlus)
        assertEquals("12025550123", pUs.internationalNoPlus)
    }

    @Test
    fun testPotentialPhoneExtraction() {
        val snippet = "سلام لطفا با این شماره تماس بگیرید: ۰۹۱۲۳۴۵۶۷۸۹ ممنون"
        val extracted = PhoneNormalizer.extractPotentialPhone(snippet)
        assertNotNull(extracted)
        assertEquals("09123456789", extracted)
    }
}
