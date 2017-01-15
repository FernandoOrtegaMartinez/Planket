package com.fomdeveloper.planket;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.fomdeveloper.planket.data.ResponseHelper;
import com.fomdeveloper.planket.data.repository.FlickrRepository;
import com.fomdeveloper.planket.injection.MockComponent;
import com.fomdeveloper.planket.ui.presentation.searchphotos.SearchActivity;
import com.fomdeveloper.planket.ui.presentation.searchphotos.SearchType;
import com.fomdeveloper.planket.ui.view.widget.PlanketTextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


/**
 * Created by Fernando on 15/01/16.
 */
@RunWith(AndroidJUnit4.class)
public class SearchActivityTest {

    private static final String A_TEXT = "Text";
    private static final String A_TAG = "Tag";
    private static final String AN_ID = "123";
    private static final String A_TOOLBAR_TITLE = "TOOLBAR TITLE";

    @Rule
    public ActivityTestRule<SearchActivity> main = new ActivityTestRule<SearchActivity>(SearchActivity.class,true,false);

    @Inject
    FlickrRepository mockFlickrRepository;

    private String jsonResponse;

    @Before
    public void setUp() throws Exception{

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        PlanketApplication app
                = (PlanketApplication) instrumentation.getTargetContext().getApplicationContext();
        MockComponent component = (MockComponent) app.getAppComponent();
        component.inject(this);

        String fileName = "interestingness.json";
        jsonResponse = ResponseHelper.getStringFromFile(getInstrumentation().getContext(), fileName);

        Intents.init();

    }

    @After
    public void tearDown(){
        Intents.release();
    }


    @Test
    public void showToolbarTitleTextFromExtras() {

        when(mockFlickrRepository.getPhotosWithText(anyString(),anyInt())).thenReturn(ResponseHelper.interestingnessResponse(jsonResponse));

        Intent intent = new Intent();
        intent.putExtra(SearchActivity.EXTRA_SEACH_ID, A_TEXT);
        intent.putExtra(SearchActivity.EXTRA_SEARCH_TYPE, SearchType.TEXT.name());

        main.launchActivity(intent);

        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar)), not(instanceOf(PlanketTextView.class)))).check(matches(withText(A_TEXT)));

    }

    @Test
    public void showToolbarTitleTagFromExtras() {

        when(mockFlickrRepository.getPhotosWithTag(anyString(),anyInt())).thenReturn(ResponseHelper.interestingnessResponse(jsonResponse));

        Intent intent = new Intent();
        intent.putExtra(SearchActivity.EXTRA_SEACH_ID, A_TAG);
        intent.putExtra(SearchActivity.EXTRA_SEARCH_TYPE, SearchType.TAG.name());

        main.launchActivity(intent);

        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar)), not(instanceOf(PlanketTextView.class)))).check(matches(withText(A_TAG)));

    }

    @Test
    public void showToolbarTitleUserIdFromExtras() {

        when(mockFlickrRepository.getPhotosForUser(anyString(),anyInt())).thenReturn(ResponseHelper.interestingnessResponse(jsonResponse));

        Intent intent = new Intent();
        intent.putExtra(SearchActivity.EXTRA_SEACH_ID, AN_ID);
        intent.putExtra(SearchActivity.EXTRA_SEARCH_TYPE, SearchType.USER_ID.name());
        intent.putExtra(SearchActivity.EXTRA_TITLE_TYPE, A_TOOLBAR_TITLE);

        main.launchActivity(intent);

        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar)), not(instanceOf(PlanketTextView.class)))).check(matches(withText(A_TOOLBAR_TITLE)));

    }

    @Test
    public void showToolbarTitleFavFromExtras() {


        when(mockFlickrRepository.getFavesForUser(anyString(),anyInt())).thenReturn(ResponseHelper.interestingnessResponse(jsonResponse));

        Intent intent = new Intent();
        intent.putExtra(SearchActivity.EXTRA_SEACH_ID, AN_ID);
        intent.putExtra(SearchActivity.EXTRA_SEARCH_TYPE, SearchType.USER_ID.name());
        intent.putExtra(SearchActivity.EXTRA_TITLE_TYPE, A_TOOLBAR_TITLE);

        main.launchActivity(intent);

        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar)), not(instanceOf(PlanketTextView.class)))).check(matches(withText(A_TOOLBAR_TITLE)));

    }


}
