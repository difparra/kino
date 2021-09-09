package com.diegoparra.kino

/*
----------      BASICS         --------------------------------------------------------------------

Naming convention:
    subjectUnderTest_actionOrInput_resultState
Structure:
    Given / When / Then
    Arrange / Act / Assert

Rules:
    -> Classes that define some code that runs before and after each test runs.
    InstantTaskExecutorRule -> E.g. For testing LiveData
        Runs all architecture components related background job in the same thread, ensuring that
        the test results happen synchronously and in a repeatable order.
        Requires: testImplementation "androidx.arch.core:core-testing:$archTestingVersion"
    MainCoroutineRule -> For testing viewModelScope or in general, places where Dispatchers.Main is required
        See the implementation above in TestingViewModels - viewModelScope section or
        https://classroom.udacity.com/courses/ud940/lessons/7d4f8ac6-8239-413a-8345-a9a1e11e4357/concepts/32ea6518-247f-408a-8970-5563946d51f8

AndroidX Tests:
-   Dependency: testImplementation "androidx.test:core-ktx:$androidXTestCoreVersion"
-   Created common methods to give context and other android resources in both local (by using
    robolectric)
    and instrumented tests (by using normal testing framework).
-   When using androidXTest it is also necessary to annotate test classes to use:
    @RunWith(AndroidJUnit4::class)
    However, use this only if androidX Test is used. If used when not necessary in local unit
    tests, speed will be decreased.

Espresso:
-   Dependencies:
        androidTestImplementation "androidx.test.espresso:espresso-core:$espressoVersion"
        androidTestImplementation "androidx.test.espresso:espresso-contrib:$espressoVersion"
-   Methods: Cheat sheet: https://developer.android.com/training/testing/espresso/cheat-sheet
-   Example:
        onView(withId(R.id.task_detail_complete_checkbox))
            .perform(click())
            .check(matches(isChecked()))
-   Additional important: Turn off animations on device in order to avoid flaky espresso tests
    Config -> System -> developer options -> Window/Transition/Animator duration scale -> Animations off

*/

/*
----------      CONTEXT         --------------------------------------------------------------------

Getting context in local tests, for example to create some AndroidViewModel:
- Add AndroidX test dependencies and Robolectric dependency
    testImplementation "androidx.test.ext:junit-ktx:$androidXTestExtKotlinRunnerVersion"
    testImplementation "androidx.test:core-ktx:$androidXTestCoreVersion"
    testImplementation "org.robolectric:robolectric:$robolectricVersion"
- Use AndroidX Test
    ApplicationProvider.getApplicationContext()
- Add AndroidJUnit4 test runner
    @RunWith(AndroidJUnit4::class)

Without AndroidX:
- LocalTest - Robolectric => val appContext = RuntimeEnvironment.application
- InstrumentedTest - androidSupportLibTest => val appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext

About Robolectric errors that may happen:
Error 1: No such manifest file: ./AndroidManifest.xml
Solution: Advanced Android with Kotlin - Lesson 10: 5.1 Testing Basics - 14. AndroidX Test
*/

/*
----------      LIVEDATA & FLOW        -------------------------------------------------------------
----------      LIVEDATA        --------------------------------------------------------------------

LiveData Testing Basics
    Advanced Android with Kotlin
    Lesson 10: 5.1 Testing Basics
    15. LiveData Testing Basics
- Add InstantTaskExecutor
    testImplementation "androidx.arch.core:core-testing:$archTestingVersion"
    class TasksViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    // Other code…
    }
- Add LiveDataTestUtil.kt class
    https://classroom.udacity.com/courses/ud940/lessons/fa3a54ab-2da2-46b9-af73-e7f05b20f90f/concepts/975dc637-ce2d-439f-bc32-1571413dd843
    https://github.com/udacity/android-testing/blob/end_codelab_3/app/src/test/java/com/example/android/architecture/blueprints/todoapp/LiveDataTestUtil.kt
    This class attach an observeForever, get the value, remove the observer and asserts, through a
    getOrAwaitValue() method, so that test methods can be saved of all of that boilerplate code.


----------      TESTING FLOWS        ---------------------------------------------------------------

testCoroutineDispatcher.runBlockingTest {
    val flowResult = someRepoMethodReturningFlow()
    val result = flowResult.first()         //  Can be compared in assertions
                                            //  Represent the first value gotten from the flow,
                                            //  similar to getOrAwaitValue function from live data testing
}
*/

/*
----------      DISPATCHERS - TestCoroutineDispatcher        ---------------------------------------

**  Use TestCoroutineDispatcher

TestCoroutineDispatcher & runBLockingTest
    A special coroutine dispatcher meant for testing.
    Runs code immediately and allows control over timing.
    Dependency = kotlinx-coroutines-test

runBlockingTest:
    When using runBlockingTest { ... } it creates a new TestCoroutineDispatcher every time.
    As a general rule, when writing local tests, make sure of use a single TestCoroutineDispatcher
    and run all of the code using this dispatcher. Do not create more.
    So, in order to use a define testCoroutineDispatcher it could be used:
        val testDispatcher = TestCoroutineDispatcher()
        testDispatcher.runBlockingTest { ... }
    Or with the mainCoroutineRule (implementation in TestingViewModels - viewModelScope section:
        mainCoroutineRule.runBlockingTest { ... }


Timing control with TestCoroutineDispatcher     ----------------------------------------------------

Example:    Testing loading indicator.
    - There is some refresh method in viewModel that will change loading to true,
    then perform operations, and then set loading to false.
    - In this case, we are trying to check/assert the state of the loading indicator, right in the
    middle of the refresh method execution.
    - refreshMethod:
        fun refresh() {
            _dataLoading.value = true
            //  WANTED ASSERTION
            viewModelScope.launch {
                taskRepository.refreshTasks()
                _dataLoading.value = false
            }
            //  AND ANOTHER WANTED ASSERTION
        }
    - In this case, test can be written as:
        @Test
        fun loadTasks_loading() {
            // Pause dispatcher so you can verify initial values.
            mainCoroutineRule.pauseDispatcher()
            // Load the task in the view model.
            statisticsViewModel.refresh()
            // Then assert that the progress indicator is shown.
            assertThat(statisticsViewModel.dataLoading.getOrAwaitValue(), `is`(true))
            // Execute pending coroutines actions.
            mainCoroutineRule.resumeDispatcher()
            // Then assert that the progress indicator is hidden.
            assertThat(statisticsViewModel.dataLoading.getOrAwaitValue(), `is`(false))
        }

*/

/*
----------      FRAGMENT INTEGRATION TESTS        --------------------------------------------------

Testing viewModel - Fragment pair = Integration test

FragmentScenario
    AndroidX Test library to create fragments. Gives control over starting state and lifecycle.
    Compatible with both local and instrumented tests.
    Required dependency:
        implementation "androidx.fragment:fragment-testing:$fragmentVersion"
        implementation "androidx.test:core:$androidXTestCoreVersion"   //  For androidX
    Example:
        val bundle = TaskDetailFragmentArgs(activeTask.id).toBundle()
        val scenario = launchInFragmentContainer<TaskDetailFragment>(bundle, R.style.appTheme)
        scenario.moveToState(State.CREATED)

Service Locator pattern
(AdvancedAndroidWithKotlin Udacity - Lesson 11: 5.2 Testing - 10: Service Locators)
    It is required when using FragmentScenario library from AndroidX as
    launching a fragment does not take arguments and therefore manual dependency injection
    cannot be used.
    Therefore, the only way to inject the fake repository into the fragment is by using a
    Service Locator or a dependency injection framework.

    A Service Locator is a singleton whose purpose is to store and provide dependencies.

    With ServiceLocator variable in service locator representing repository may be public
    with setter @VisibleForTesting.
    - Then in the setUp method of the test class, the repository in the ServiceLocator can be
    swapped by: ServiceLocator.taksRepository = FakeAndroidTestRepository()
    - In addition to the setUp Method the repository should also be completely reset and cleaned
    up between test, so in tearDown method, some reset method to clean the repository must be
    called. - Reset method can be added in ServiceLocator as ServiceLocator.resetRepository()
    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            runBlocking { TasksRemoteDataSource.deleteAllTasks()}
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            tasksRepository = null
        }
    }


Espresso
    Espresso is an Android UI testing library. Using Espresso you can interact with views and
    check their state.
    Espresso components in a espresso statement:
        Static espresso method -> onView, onData, ...
        ViewMatcher -> withId, onText, ... / isChecked ...
        ViewAction -> click, ...
        ViewAssertion -> matches, ...
    ***** Cheat sheet: https://developer.android.com/training/testing/espresso/cheat-sheet
    Example:
        onView(withId(R.id.task_detail_complete_checkbox))
            .perform(click())
            .check(matches(isChecked()))
    Can also be called separately example:
        onView(withId(R.id.task_detail_complete_checkbox))
            .perform(click())
        onView(withId(R.id.task_detail_complete_checkbox))
            .check(matches(isChecked()))

    ---
    Dependencies:
        androidTestImplementation "androidx.test.espresso:espresso-core:$espressoVersion"
        androidTestImplementation "androidx.test.espresso:espresso-contrib:$espressoVersion"
            //  Second one useful for example when testing recyclerviews

    Important:
        Turn off animations on device in order to avoid flaky espresso tests.
        Config -> System -> developer options -> Window/Transition/Animator duration scale -> Animations off

Mockito
    Mock: A test double that tracks which of its methods were called.
    Mocks pass or fail a test depending on whether their methods were called correctly.


TESTING NAVIGATION      ----------------------------------------------------------------------------

Steps:
1.  Mockito creates a NavController mock
2.  Attach that mocked NavController to the fragment
3.  verify navigate() called with correct action and parameters

    //  setUp and tearDown methods contain fakeRepo creation, assignment to serviceLocator,
    //  and cleaning.

    @Test
    fun clickTask_navigateToDetailFragmentOne() = runBlockingTest {
        repository.saveTask(Task("TITLE1", "DESCRIPTION1", false, "id1"))
        repository.saveTask(Task("TITLE2", "DESCRIPTION2", true, "id2"))

        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)

        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - Click on the first list item
        onView(withId(R.id.tasks_list))
            .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText("TITLE1")), click()))

        // THEN - Verify that we navigate to the first detail screen
        verify(navController).navigate(
            TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment( "id1")
        )
    }


 */

/*
----------      TESTING VIEWMODELS - viewModelScope        -----------------------------------------

NOTE:   Any time there is a possibility that Dispatchers.Main is used,
        it should be swapped for a testDispatcher when running test
        (mainLooper from Android is not available in local tests).
        Example: viewModelScope, which launches on Dispatchers.Main

Swapping MainDispatcher to TestDispatcher:
    -   Create a testDispatcher -> val testDispatcher = TestCoroutineDispatcher()
    -   In setUp method, swap the main dispatcher to the test dispatcher:
            Dispatchers.setMain(testDispatcher)
    -   In tearDown method:
            Dispatchers.resetMain()
            testDispatcher.cleanupTestCoroutines()

Another option to avoid setting up @Before and @After method to work with testDispatcher instead of
mainDispatcher always, and reduce boilerplate code, is by creating some custom junitRule (class that
define code that will run before and after each test):

@ExperimentalCoroutinesApi
class MainCoroutineRule(val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()):
   TestWatcher(),       //  To make this class a junitRule
   TestCoroutineScope by TestCoroutineScope(dispatcher) //  Gives this class the ability to control coroutineTiming using dispatcher in params
{
   override fun starting(description: Description?) {
       super.starting(description)
       Dispatchers.setMain(dispatcher)
   }

   override fun finished(description: Description?) {
       super.finished(description)
       cleanupTestCoroutines()
       Dispatchers.resetMain()
   }
}

To add the rule in the testClass:
@get:Rule
var mainCoroutineRule: MainCoroutineRule()


MainCoroutineRule
    Creates a TestCoroutineDispatcher
    Swaps Dispatchers.Main for this dispatcher
    Note: In general, when writing local tests, make sure of use a single TestCoroutineDispatcher
    and run all of the code using this dispatcher. Do not create more.

 */

/*
----------      TESTING ROOM        ----------------------------------------------------------------

Dependencies:
    androidTestImplementation "androidx.arch.core:core-testing:$archTestingVersion"

Database test is usually instrumented
    Because sql version in computer may be completely different to the one in a device.
    In addition, it is also good to test in different devices, as sql versions may be different.

Annotations:
    @RunWith(AndroidJUnit4::class)
    @SmallTest

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule()

setUp and tearDown methods

    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            ToDoDatabase::class.java
        ).allowMainThreadQueries()      //  On Instrumented Tests, as they are run on a device,
                                        //  Dispatchers.Main can be used in some parts of the testing
                                        //  code, so it may be a good idea to allowMainThreadQueries in db.
        .build()
    }

    @After
    fun closeDb() = database.close()





 */


/*
----------      CONFIGURE SHARED TEST FOLDER        ------------------------------------------------

Advanced Android with Kotlin - Udacity Course
Lesson 11: 5.2 Testing: Intro to test doubles
12. Use your Service Locator in your tests
https://classroom.udacity.com/courses/ud940/lessons/9434e029-dce7-4550-93f2-18a224433e72/concepts/8d3160f6-d16b-4897-85ad-ee5710d149a1

https://github.com/android/architecture-samples/blob/f4128dd8dbea5d1aac5d5acd5f346bb82187fbe6/app/build.gradle#L20
Inside gradle at app level, and inside android {  } for example below defaultConfig add:
android {
    defaultConfig { applicationId ... }
    android {
        sourceSets {
            String sharedTestDir = 'src/sharedTest/java'
            test {
                java.srcDir sharedTestDir
            }
            androidTest {
                java.srcDir sharedTestDir
            }
        }
    }
    ...
}
*/

/*
----------      END TO END TESTS        ------------------------------------------------------------

Advanced Android with Kotlin
Lesson 12: 5.3 Testing: Survey of Advanced Topics ...
10. Idling Resources and End-To-End Testing
    and onwards



Espresso Idling Resource
    A synchronization mechanism which tracks whether the application is busy or idle for Espresso.
    If the application is idle, Espresso knows it can continue testing.
    If the application is working, Espresso will wait until idle.
    Dependency
        implementation "androidx.test.espresso:espresso-idling-resource:$espressoVersion"
        testOptions.unitTests {
            includeAndroidResources = true
            returnDefaultValues = true
        }

CountingIdlingResource - Network and database
DataBindingIdlingResource - sync Espresso with DataBinding


Util espresso classes to determine idling state with espresso:
    Advanced Android with Kotlin
    Lesson 12: 5.3 Testing: Survey of Advanced Topics ...
    11. Counting Idling Resource


TESTING GLOBAL NAVIGATION
    Advanced Android with Kotlin
    Lesson 12: 5.3 Testing: Survey of Advanced Topics ...
    14. Code Checkpoint: Testing Global App Navigation

 */