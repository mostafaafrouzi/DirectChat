package com.afrouzi.directchat

import com.afrouzi.directchat.data.model.Messenger
import com.afrouzi.directchat.domain.engine.IntentRouter
import com.afrouzi.directchat.domain.engine.PhoneNormalizer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IntentRouterTest {

    @Test
    fun testDirectLinkUrlGeneration() {
        val phone = PhoneNormalizer.parse("09123456789")

        val waLink = IntentRouter.buildDirectLinkUrl(Messenger.WHATSAPP, phone, "سلام")
        assertEquals("https://wa.me/989123456789?text=%D8%B3%D9%84%D8%A7%D9%85", waLink)

        val tgLink = IntentRouter.buildDirectLinkUrl(Messenger.TELEGRAM, phone)
        assertEquals("https://t.me/+989123456789", tgLink)

        val eitaaLink = IntentRouter.buildDirectLinkUrl(Messenger.EITAA, phone)
        assertEquals("https://eitaa.com/", eitaaLink)

        val baleLink = IntentRouter.buildDirectLinkUrl(Messenger.BALE, phone)
        assertEquals("https://ble.ir/", baleLink)
    }
}
