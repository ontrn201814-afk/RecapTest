package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.RecapFlow
import com.example.data.model.countMyanmarSyllables
import com.example.data.repository.ProjectRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app_name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Zippy Recap Pro", appName)
    }

    @Test
    fun `myanmar syllable counter works accurately`() {
        val testText = "ညမှောင်မှောင်ထဲမှာ သူရဲကောင်းတစ်ယောက် အပြေးရောက်ရှိလာခဲ့ပါတယ်။"
        val count = countMyanmarSyllables(testText)
        assertTrue("Syllables should be calculated properly", count > 10)
    }

    @Test
    fun `sample SRT list contains characters and valid timestamps`() {
        val srtList = ProjectRepository.getSampleSrtList()
        assertTrue("Sample SRT list should not be empty", srtList.isNotEmpty())
        assertEquals("Zaw Zaw", srtList[0].character)
        assertTrue(srtList[0].endTimeMs > srtList[0].startTimeMs)
    }

    @Test
    fun `all flows have titles and descriptions`() {
        for (flow in RecapFlow.entries) {
            assertTrue(flow.titleMy.isNotBlank())
            assertTrue(flow.description.isNotBlank())
        }
    }
}
