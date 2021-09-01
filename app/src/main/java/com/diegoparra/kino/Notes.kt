package com.diegoparra.kino

/*
NOTES:

- About kotlin.Result
    It is an inline/value class and may have some bugs in some cases.
    One example I got a bug, was when I was trying to implement isFavourite(movieId) method
    Repository. I get a Flow<Boolean> from Dao and mapped to Flow<Result<Boolean>>,
    however this threw some java.lang.ClassCastException:
    java.lang.Boolean cannot be cast to kotlin.Result.
    -> Solution: Decided to create my own class to deal with exceptions and data -> Either


 */