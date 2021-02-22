/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.ui

import androidx.core.net.toUri
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mozilla.fenix.helpers.HomeActivityTestRule
import org.mozilla.fenix.helpers.TestHelper.setNetworkConnection
import org.mozilla.fenix.ui.robots.browserScreen
import org.mozilla.fenix.ui.robots.homeScreen
import org.mozilla.fenix.ui.robots.navigationToolbar

/**
 * Tests to verify some main UI flows with Network connection off
 *
 */

class NoNetworkAccessStartupTests {
    val activityTestRule = HomeActivityTestRule()

    @Before
    fun setUp() {
        setNetworkConnection("OFF")
        activityTestRule.launchActivity(null)
    }

    @After
    fun tearDown() {
        setNetworkConnection("ON")
    }

    @Test
    // Based on STR from https://github.com/mozilla-mobile/fenix/issues/16886
    fun noNetworkConnectionStartupTest() {
        homeScreen {
        }.dismissOnboarding()
        homeScreen {
            verifyHomeScreen()
        }
    }

    @Test
    // Based on STR from https://github.com/mozilla-mobile/fenix/issues/16886
    fun networkInterruptedFromBrowserToHomeTest() {
        val url = "example.com"

        setNetworkConnection("ON")

        navigationToolbar {
        }.enterURLAndEnterToBrowser(url.toUri()) {}

        setNetworkConnection("OFF")

        browserScreen {
        }.goToHomescreen {
            verifyHomeScreen()
        }
    }

    @Test
    fun testPageReloadAfterNetworkInterrupted() {
        val url = "example.com"

        setNetworkConnection("ON")

        navigationToolbar {
        }.enterURLAndEnterToBrowser(url.toUri()) {}

        setNetworkConnection("OFF")

        browserScreen {
        }.openThreeDotMenu {
        }.refreshPage {}
    }

    @Test
    fun testSignInPageWithNoNetworkConnection() {
        homeScreen {
        }.openThreeDotMenu {
        }.openSettings {
        }.openTurnOnSyncMenu {
            tapOnUseEmailToSignIn()
        }
    }
}
