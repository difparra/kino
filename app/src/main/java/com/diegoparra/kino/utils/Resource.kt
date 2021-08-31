package com.diegoparra.kino.utils

/**
 * Used as a wrapper to set state on data going from viewModel to Ui.
 */
sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    class Success<T>(val data: T) : Resource<T>() {
        override fun toString(): String {
            return "{ Resource.Success / data = $data }"
        }
    }

    class Error(val failure: Throwable) : Resource<Nothing>() {
        override fun toString(): String {
            return "{ Resource.Failure / failure = $failure }"
        }
    }
}


fun <T> Result<T>.toResource() = this.fold(
    onSuccess = { Resource.Success(it) },
    onFailure = { Resource.Error(it) }
)

fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> =
    when (this) {
        is Resource.Loading -> Resource.Loading
        is Resource.Success -> Resource.Success(transform(this.data))
        is Resource.Error -> Resource.Error(this.failure)
    }
