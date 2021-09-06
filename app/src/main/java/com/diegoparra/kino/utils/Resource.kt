package com.diegoparra.kino.utils

/**
 * Used as a wrapper to set state on data going from viewModel to Ui.
 */
sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    class Success<T>(val data: T) : Resource<T>()
    class Error(val failure: Exception) : Resource<Nothing>()

    override fun toString(): String {
        return when(this) {
            Loading -> "Loading"
            is Success -> "Success[data=${this.data}]"
            is Error -> "Error[exception=${this.failure}]"
        }
    }
}