package com.tia_0653.mobilebanking


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class BuktiTransaksiTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(BuktiTransaksi::class.java)

    @Test
    fun buktiTransaksiTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(500)

        val materialButton = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val materialButton2 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val materialAutoCompleteTextView = onView(
            allOf(
                withId(R.id.et_namaBank),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_namaBank),
                        0
                    ),
                    0
                )
            )
        )
        materialAutoCompleteTextView.perform(scrollTo(), click())
        onView(isRoot()).perform(waitFor(3000))

        val materialTextView = onData(anything())
            .inRoot(isPlatformPopup())
            .atPosition(0)
        materialTextView.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val materialButton3 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())
        onView(isRoot()).perform(waitFor(3000))


        val textInputEditText = onView(
            allOf(
                withId(R.id.et_tanggal_transaksi),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_tanggal_transaksi),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText.perform(scrollTo(), replaceText("12/12/2022"), closeSoftKeyboard())
        onView(isRoot()).perform(waitFor(3000))

        val materialAutoCompleteTextView2 = onView(
            allOf(
                withId(R.id.et_kode_bank),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_kodeBank),
                        0
                    ),
                    0
                )
            )
        )
        materialAutoCompleteTextView2.perform(scrollTo(), click())
        onView(isRoot()).perform(waitFor(3000))

        val materialTextView2 = onData(anything())
            .inRoot(isPlatformPopup())
            .atPosition(1)
        materialTextView2.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.et_jumlah_uang),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_jumlah_uang),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText2.perform(scrollTo(), replaceText("1000"), closeSoftKeyboard())
        onView(isRoot()).perform(waitFor(3000))

        val materialButton4 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())
        onView(isRoot()).perform(waitFor(3000))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    fun waitFor(delay: Long): ViewAction?{
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "Wait for $delay milliseconds."
            }

            override fun perform(uiController: UiController?, view: View?) {
                uiController?.loopMainThreadForAtLeast(delay)
            }
        }
    }
}