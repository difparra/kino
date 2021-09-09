package com.diegoparra.kino

/*

----------      MOCKITO        ---------------------------------------------------------------------

Mock internal objects used for a class being tested and determine its behaviour when some method its called.

Mockito dependencies:
    androidTestImplementation "org.mockito:mockito-core:$mockitoVersion"
    androidTestImplementation "com.linkedin.dexmaker:dexmaker-mockito:$dexMakerVersion"

Init / SetUp:
    -   Either annotate class with @RunWith(MockitoJUnitRunner::class)
    -   Or use the rule: @get:Rule val mockitoRule = MockitoJUnit.rule()

Mock objects:
    -   Either Annotate classes/objects to mock with @Mock, example:
            @Mock private lateinit var moviesApi: MoviesApi
    -   or use:
            private var moviesApi = Mockito.mock(MoviesApi::class.java)

There is no need for further setUp on mocked object. With @Mock, mockito methods can be used in this object.


Main methods (when, verify) example:
    Mockito.`when`(moviesApi.getGenres()).thenReturn(genresResponse)
    val result = moviesRepositoryImpl.getGenres()
    Mockito.verify(moviesApi).getGenres()


 */

/*
----------      TESTING WITH HILT        -----------------------------------------------------------

Unit tests
Hilt isn't necessary for unit tests, since when testing a class that uses constructor injection,
you don't need to use Hilt to instantiate that class. Instead, you can directly call a class
constructor by passing in fake or mock dependencies, just as you would if the constructor weren't
annotated


End-to-end tests
https://developer.android.com/training/dependency-injection/hilt-testing#kotlin

.....   Dependencies    ............................................................................

// For Robolectric tests.
testImplementation 'com.google.dagger:hilt-android-testing:2.38.1'
kaptTest 'com.google.dagger:hilt-android-compiler:2.38.1'

// For instrumented tests.
androidTestImplementation 'com.google.dagger:hilt-android-testing:2.38.1'
kaptAndroidTest 'com.google.dagger:hilt-android-compiler:2.38.1'


.....   Initial setUp    ...........................................................................

Add @HiltAndroidTest
Add @get:Rule var hiltRule = HiltAndroidRule(this)      //  If there are more rules, use also order = 0 to ensure it runs at first
Set test application in instrumented tests
    Create CustomTestRunner class and configure it in the gradle file.

Example
        ----------      In test class
@HiltAndroidTest
class SettingsActivityTest {
    @get:Rule(order = 0)      //  If there are more rules, ensure hiltRule executes first by adding order
    var hiltRule = HiltAndroidRule(this)
    @Inject
    lateinit var analyticsAdapter: AnalyticsAdapter
    @Before
    fun init() {
        hiltRule.inject()
    }
    @Test
    fun `happy path`() {
        // Can already use analyticsAdapter here.
    }
}

        ----------      Another class in test folder
// A custom runner to set up the instrumented application class for tests.
class CustomTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
        ----------      In gradle file
//  And configure this test runner in the gradle file:
android {
    defaultConfig {
        // Replace com.example.android.dagger with your class path (can get from package top code line in the class file).
        testInstrumentationRunner "com.example.android.dagger.CustomTestRunner"
    }
}



POSSIBLE ERROR:
While trying to setUp hilt, there may appear some error:
    Could not resolve com.google.code.findbugs:jsr305:3.0.2
One way to solve/hide this error is by adding to the app gradle file:
android {
    ...
    configurations.all {
        resolutionStrategy.force 'com.google.code.findbugs:jsr305:3.0.0'
    }
}


.....   Replace modules in testing    ..............................................................

Module outside testClass -> Will be used in all the tests in the folder.
-   When the module for test is created in the folder structure in test or androidTest, the module
    will be used to all the tests in that folder.

    @Module
    @TestInstallIn(
        components = [SingletonComponent::class],
        replaces = [AnalyticsModule::class]
    )           //  @TestInstallIn annotation replace binding for all test classes in that test folder
    abstract class FakeAnalyticsModule {
        @Singleton
        @Binds
        abstract fun bindAnalyticsService(
            fakeAnalyticsService: FakeAnalyticsService
        ): AnalyticsService
    }


Module inside of the test class -> Will be used just in the test class
-   To use module in a single test class,
    - uninstall if necessary (if there is one in the test folder)
    - And create the test module within the test class

    @UninstallModules(AnalyticsModule::class)       //  Can only uninstall @InstallIn modules, not @TestInstallIn modules
    @HiltAndroidTest
    class SettingsActivityTest {
        @Module
        @InstallIn(SingletonComponent::class)
        abstract class TestModule {
            @Singleton
            @Binds
            abstract fun bindAnalyticsService(
                fakeAnalyticsService: FakeAnalyticsService
            ): AnalyticsService
        }
        ...
    }


.....   @BindValue annotation    ...................................................................

Use the @BindValue annotation to easily bind fields in your test into the Hilt dependency graph.

In the AnalyticsService example, you can replace AnalyticsService with a fake by using @BindValue:
    @UninstallModules(AnalyticsModule::class)
    @HiltAndroidTest
    class SettingsActivityTest {
        @BindValue @JvmField
        val analyticsService: AnalyticsService = FakeAnalyticsService()
        ...
    }

----------      @BindValue and @Mock

@BindValue annotation can also work with mockito
    @BindValue @Mock
    lateinit var qualifiedVariable: ExampleCustomType



.....   launchFragmentInHiltContainer    ...................................................................

launchFragmentInContainer
It is not possible to use launchFragmentInContainer from the androidx.fragment:fragment-testing
library with Hilt, because it relies on an activity that is not annotated with @AndroidEntryPoint.

Use the launchFragmentInHiltContainer code from the architecture-samples GitHub repository instead.
https://github.com/android/architecture-samples/blob/dev-hilt/app/src/androidTest/java/com/example/android/architecture/blueprints/todoapp/HiltExt.kt#L38

 */

/*
----------      TESTING COMPOSE        -------------------------------------------------------------
https://developer.android.com/jetpack/compose/testing
https://developer.android.com/codelabs/jetpack-compose-testing#1

Dependencies
    androidTestImplementation "androidx.compose.ui:ui-test-junit4:$composeVersion"

    // Test rules and transitive dependencies:
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version")
    // Needed for createComposeRule, but not createAndroidComposeRule:
    debugImplementation("androidx.compose.ui:ui-test-manifest:$compose_version")

Compose Test rule
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
        //  or createComposeRule() depending on how much you would like to
        //  interact with the framework
        //  Whenever possible use createComposeRule() but in some cases, such as when getting
        //  resources, or accessing the activity it is necessary to use createAndroidComposeRule()

Similar to espresso tests:
Example:
    @Test
    fun clickButton_incrementsCounter() {
        val textIncrement = composeTestRule.activity.getString(R.string.increment_counter)
        composeTestRule.onNodeWithText(textIncrement).performClick()
        val textClicks = composeTestRule.activity.getString(R.string.clicks, 1)
        composeTestRule.onNodeWithText(textClicks).assertExists()
    }

    @Test
    fun MyTest() {
        // Start the app
        composeTestRule.setContent {
            MyAppTheme {
                MainScreen(uiState = fakeUiState, /*...*/)
            }
        }
        composeTestRule.onNodeWithText("Continue").performClick()
        composeTestRule.onNodeWithText("Welcome").assertIsDisplayed()
    }

Compose testing cheat-sheet:
https://developer.android.com/jetpack/compose/testing-cheatsheet

 */