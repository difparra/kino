package com.diegoparra.kino.test_utils

import com.google.gson.Gson
import java.io.InputStream
import java.nio.charset.Charset

object Filenames {
    const val GENRES_FILENAME = "genres.json"
    const val MOVIES_LIST_FILENAME = "movies_list.json"
    const val MOVIE_DETAILS_FILENAME = "movie_details_suicide_squad.json"
    const val MOVIE_CREDITS_FILENAME = "movie_credits_suicide_squad.json"
    const val PEOPLE_LIST_FILENAME = "people_list.json"
}

fun <T> loadJSONFromResourcesFile(filename: String, classOfT: Class<T>): T {
    var inputStream: InputStream? = null
    var result: T? = null
    try{
        inputStream = ClassLoader.getSystemResourceAsStream(filename)
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        val jsonString = String(buffer, Charset.forName("UTF-8"))
        result = Gson().fromJson(jsonString, classOfT)
    } finally {
        inputStream?.close()
    }
    return result ?: throw NoSuchElementException()
}

/*
//  This may have been better than loading with ClassLoader, but, by requesting
//  context, in the best case it requires robolectric and AndroidJUnit4 runner.
//  On the other hand, ApplicationProvider (androidX Test classes) seem to be not smart enough
//  to get file from assets, so in that case, it is necessary to make an instrumentation test,
//  and get context with instrumentation registry.
fun <T> loadJSONFromAssetsFile(filename: String): T {
    val appContext = ApplicationProvider.getApplicationContext<Context>()
    return appContext.assets.open(filename).use { inputStream ->
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        val jsonString = String(buffer, Charset.forName("UTF-8"))
        val type: Type = object : TypeToken<T>() {}.type
        Gson().fromJson<T>(jsonString, type)
    }
}*/