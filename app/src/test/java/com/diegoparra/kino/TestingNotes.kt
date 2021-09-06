package com.diegoparra.kino

/*
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

 */