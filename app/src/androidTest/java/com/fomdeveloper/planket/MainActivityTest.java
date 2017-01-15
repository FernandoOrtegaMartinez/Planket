package com.fomdeveloper.planket;



import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityResult;
import android.content.ComponentName;
import android.content.Intent;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.fomdeveloper.planket.data.ResponseHelper;
import com.fomdeveloper.planket.data.model.transportmodel.PhotosContainer;
import com.fomdeveloper.planket.data.prefs.UserHelper;
import com.fomdeveloper.planket.data.repository.FlickrRepository;
import com.fomdeveloper.planket.injection.MockComponent;
import com.fomdeveloper.planket.ui.presentation.login.FlickrLoginActivity;
import com.fomdeveloper.planket.ui.presentation.main.MainActivity;
import com.fomdeveloper.planket.ui.presentation.profile.ProfileActivity;
import com.fomdeveloper.planket.ui.presentation.searchphotos.SearchActivity;
import com.fomdeveloper.planket.ui.presentation.searchphotos.SearchType;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import javax.inject.Inject;


import rx.Single;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Created by Fernando on 24/12/2016.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest{

    public static final String TEXT_TO_BE_SEARCHED = "Spain";
    @Rule
    public ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class,true,false);

    @Inject
    UserHelper mockUserHelper;

    @Inject
    FlickrRepository mockFlickrRepository;

    @Inject
    NetworkManager networkManager;

    @Before
    public void setUp() throws Exception{

        Instrumentation instrumentation = getInstrumentation();
        PlanketApplication app
                = (PlanketApplication) instrumentation.getTargetContext().getApplicationContext();
        MockComponent component = (MockComponent) app.getAppComponent();
        component.inject(this);

        Intents.init();

        String fileName = "interestingness.json";
        String jsonResponse = ResponseHelper.getStringFromFile(getInstrumentation().getContext(), fileName);

        when(mockFlickrRepository.getInterestingness(anyInt())).thenReturn(ResponseHelper.interestingnessResponse(jsonResponse));
        when(networkManager.isConnectedToInternet()).thenReturn(true);

    }

    @After
    public void tearDown(){
        Intents.release();
    }


    @Test
    public void drawerLayoutIsClosedByDefault() {

        main.launchActivity(new Intent());

        // Drawer should not be open to start.
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        // The drawer should now be open.
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());

    }


    @Test
    public void interestingnessFragmentIsLoadedByDefault() {

        when(mockUserHelper.isUserLoggedIn()).thenReturn(false);

        main.launchActivity(new Intent());

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        // The drawer should now be open.
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));

        onView(withText("Interestingness")).check(matches(isChecked()));
        onView(withText("About")).check(matches(not(isChecked())));
        onView(withText("Log in to Flickr")).check(matches(not(isChecked())));

        onView(withId(R.id.toolbar_logo)).check(matches(isDisplayed()));

    }

    @Test
    public void clickAboutDrawerMenuLoadAboutFragment() {

        main.launchActivity(new Intent());

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withText("About")).check(matches(isDisplayed())).perform(click());

        onView(withText("ABOUT PLANKET")).check(matches(isDisplayed()));
        onView(withText("PROJECT")).check(matches(isDisplayed()));

        onView(withId(R.id.toolbar_logo)).check(matches(isDisplayed()));
    }

    @Test
    public void clickLoginDrawerMenuLoadLoginActivity() {

        when(mockUserHelper.isUserLoggedIn()).thenReturn(false);

        main.launchActivity(new Intent());

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withText("Log in to Flickr")).perform(click());
        onView(withText("Log in to Flickr")).check(matches(not(isChecked())));

        intended(hasComponent(new ComponentName(getTargetContext(), FlickrLoginActivity.class)));

        onView(withText(R.string.login_message_permission)).check(matches(isDisplayed()));
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));

    }

    @Test
    public void canClickOnMenuDrawerHeaderToLoginWhenUserIsLoggedOut(){

        when(mockUserHelper.isUserLoggedIn()).thenReturn(false);

        main.launchActivity(new Intent());

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.logged_in_layout)).check(matches(not(isDisplayed())));
        onView(withId(R.id.no_logged_in_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.no_logged_in_layout)).perform(click());

        intended(hasComponent(new ComponentName(getTargetContext(), FlickrLoginActivity.class)));

    }


    @Test
    public void showUsernameOnMenuDrawerHeaderWhenUserIsLoggedIn(){

        when(mockUserHelper.isUserLoggedIn()).thenReturn(true);
        when(mockUserHelper.getUserName()).thenReturn("fomdeveloper");

        main.launchActivity(new Intent());

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.no_logged_in_layout)).check(matches(not(isDisplayed())));
        onView(withId(R.id.logged_in_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.user_name)).check(matches(withText("fomdeveloper")));

    }


    @Test
    public void canClickDrawerHeaderToOpenProfileScreenWhenUserIsLoggedIn(){

        when(mockUserHelper.isUserLoggedIn()).thenReturn(true);

        main.launchActivity(new Intent());

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        intending(anyIntent()).respondWith(getActivityResultOK());

        onView(withId(R.id.logged_in_layout)).check(matches(isDisplayed())).perform(click());

        intended(hasComponent(ProfileActivity.class.getName()));

    }

    @Test
    public void canSearch(){

        main.launchActivity(new Intent());

        onView(withId(R.id.action_search)).check(matches(isDisplayed()));
        onView(withId(R.id.search_src_text)).check(doesNotExist());

        onView(withId(R.id.action_search)).perform(click());

        intending(anyIntent()).respondWith(getActivityResultOK());

        onView(withId(R.id.search_src_text)).perform(typeText(TEXT_TO_BE_SEARCHED), pressImeActionButton());

        intended(allOf(hasComponent(SearchActivity.class.getName()), hasExtra(SearchActivity.EXTRA_SEACH_ID, TEXT_TO_BE_SEARCHED), hasExtra(SearchActivity.EXTRA_SEARCH_TYPE, SearchType.TEXT.name())));

    }

    @Test
    public void showNoInternetDialogWhenTryingToSearch(){

        when(networkManager.isConnectedToInternet()).thenReturn(false);

        main.launchActivity(new Intent());

        onView(withId(R.id.action_search)).perform(click());

        onView(withId(R.id.search_src_text)).perform(typeText(TEXT_TO_BE_SEARCHED), pressImeActionButton());

        onView(withText(R.string.no_internet)).check(matches(isDisplayed()));

    }

    @Test
    public void showErrorViewWhenInterestingnessError(){

        when(mockFlickrRepository.getInterestingness(anyInt())).thenReturn(Single.<PhotosContainer>error(new RuntimeException()));

        main.launchActivity(new Intent());

        onView(withId(R.id.error_view)).check(matches(isDisplayed()));

    }


    @Test
    public void searchViewIsHiddenInAboutFragment(){

        main.launchActivity(new Intent());

        onView(withId(R.id.action_search)).check(matches(isDisplayed()));

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withText("About")).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.action_search)).check(doesNotExist());

    }

    private ActivityResult getActivityResultOK(){
        Intent intent = new Intent();
        ActivityResult intentResult = new ActivityResult(Activity.RESULT_OK, intent);
        return intentResult;
    }

}
