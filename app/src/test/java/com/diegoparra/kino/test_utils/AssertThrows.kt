package com.diegoparra.kino.test_utils

import org.junit.Assert

inline fun <reified T : Exception> assertThrows(block: () -> Unit) {
    try {
        block.invoke()
        //  If exception was not thrown...
        Assert.fail("expected ${T::class.qualifiedName}")
    } catch (e: Throwable) {
        if (e !is T) {
            //  If exception thrown was not the one defined
            Assert.fail("expected ${T::class.qualifiedName} but caught ${e::class.qualifiedName} instead")
        }
    }
}