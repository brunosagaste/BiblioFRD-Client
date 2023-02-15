package com.bruno.frd.biblio;

import static androidx.test.espresso.intent.Checks.checkNotNull;
import static org.hamcrest.Matchers.is;

import android.util.Pair;
import android.view.View;
import android.widget.ListView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class CardMatcher {
    static Matcher<View> atPosition(final View view, int position) {
        return atPosition(is(view), position);
    }

    static Matcher<View> atPosition(final Matcher<View> itemMatcher, int position) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {

            @Override
            public boolean matchesSafely(RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Card with value: ");
                itemMatcher.describeTo(description);
            }
        };
    }
}
