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
 */