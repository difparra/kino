package com.diegoparra.kino

/*
NOTES:

- About kotlin.Result
    It is an inline/value class and may have some bugs in some cases.
    One example I got a bug, was when I was trying to implement isFavourite(movieId) method
    Repository. I get a Flow<Boolean> from Dao and mapped to Flow<Result<Boolean>>,
    however this threw some java.lang.ClassCastException:
    java.lang.Boolean cannot be cast to kotlin.Result.
    -> Solution:
        Decided to create my own class to deal with exceptions and data -> Either
        Additional advantages of using Either ->
            - Left is exposed. If I don't want to deal with a generic Throwable I can use some
            specific Exception in Either.
            - In addition, if necessary I could create my own Failure class and define some
            hierarchy, having more control on which exceptions can be presented, when and where in
            the viewModel / ui, rather than just getting info about a generic Exception.
            - Either is also different from Result in its name. So that it is easier to import
            this class, and don't get confused with the Result from kotlin.


  - LocalDateTime has neither offset nor zone defined.
    It can compare values, but they are independent form regions and offset.
    When called LocalDateTime, it basically gives the dateTime in the device, but without knowing
    the actual offset.

    DATE TIME NOTES: https://stackoverflow.com/questions/41427384/how-to-get-default-zoneoffset-in-java8

                                    LEGACY CLASS                    MODERN CLASS
    ------------------------------------------------------------------------------------------------
    Moment in UTC                   java.util.Date                  java.time.Instant
                                    java.sql.Timestamp
    ------------------------------------------------------------------------------------------------
    Moment with offset-from-UTC     (lacking)                       java.time.OffsetDateTime
    (hours - minutes - seconds)
    ------------------------------------------------------------------------------------------------
    Moment with time zone           java.util.GregorianCalendar     java.time.ZonedDateTime
    (continent/region)
    ------------------------------------------------------------------------------------------------
    Date & Time-of-day              (lacking)                       java.time.LocalDateTime
    (no offset - no zone)
    Not a moment

    Changed LocalDateTime to Instant when saving values in database just to compare if value is updated or not.
    Instant is based in UTC time. Having a unique reference makes it easier to compare values.
    In addition, Long can be used instead of instant, as they are basically the same, but, having instant is more readable, and is less likely to make some mistake about giving a seconds or milliseconds value.

    Using LocalDateTime, as it can't be directly saved in database, and when parsing I have to take care of timeZones is more prone to errors.



- SupervisorJob

A coroutine scope can be created using a supervisorJob as CoroutineScope(SupervisorJob())
so that exception won't propagate to other children, it will let the child itself (the one that
throw the exception) handle it.
However, if exception is not handled, and the CoroutineContext does not have a CoroutineExceptionHandler,
it will eventually reach the defaul thread's ExceptionHandler, which in Android will make the app
crash regardless of the Dispatcher.

Example: With Supervisor Job:
val scope = CoroutineScope(SupervisorJob())
scope.launch {// Child 1}
scope.launch {// Child 2}
In this case, if child1 fails, neither scope nor child2 will be cancelled.


More:
val scope = CoroutineScope(Job())
scope.launch(SupervisorJob()) {
    // new coroutine -> can suspend
   launch {
        // Child 1
    }
    launch {
        // Child 2
    }
}
In this case, child1's parent job is of type Job. SupervisoJob is not doing anything.

 */