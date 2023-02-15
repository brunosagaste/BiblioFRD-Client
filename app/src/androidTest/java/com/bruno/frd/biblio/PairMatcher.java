package com.bruno.frd.biblio;

import static androidx.test.espresso.intent.Checks.checkNotNull;
import static org.hamcrest.Matchers.is;

import android.util.Pair;
import android.view.View;
import android.widget.ListView;

import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

class PairMatcher {
    static Matcher<View> withPairValue(final String substring, int position) {
        return withPairValue(is(substring), position);
    }

    static Matcher<View> withPairValue(final Matcher<String> stringMatcher, int position) {
        checkNotNull(stringMatcher);
        return new BoundedMatcher<View, ListView>(ListView.class) {

            @Override
            public boolean matchesSafely(ListView list) {
                final Pair pair = (Pair) list.getItemAtPosition(position);
                return pair.second != null && stringMatcher.matches(pair.second);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Pair with value: ");
                stringMatcher.describeTo(description);
            }
        };
    }
}