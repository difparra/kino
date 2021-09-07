package com.diegoparra.kino.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class FakeFavouritesDao(private val initialData: List<Favourite> = emptyList()) : FavouritesDao() {

    /*
            SET UP      ----------------------------------------------------------------------------
     */

    //  In order to create a primary key and do not repeat objects with same key
    private val favouritesData: LinkedHashMap<String, Favourite> = LinkedHashMap()

    //  To return flows when necessary
    private val favouritesFlow = MutableStateFlow<List<Favourite>>(emptyList())

    private suspend fun refreshFavourites() {
        favouritesFlow.value = getFavourites()
    }

    private suspend fun addFavourites(vararg favourites: Favourite) {
        for (favourite in favourites) {
            favouritesData[favourite.movieId] = favourite
        }
        refreshFavourites()
    }

    init {
        if (!initialData.isNullOrEmpty()) {
            runBlocking { addFavourites(*initialData.toTypedArray()) }
        }
    }


    /*
            METHODS IMPLEMENTATION      ------------------------------------------------------------
     */

    private fun getFavourites(): List<Favourite> {
        return favouritesData.values.toList()
    }

    override fun _observeFavourites(): Flow<List<String>> {
        return favouritesFlow.map { it.map { it.movieId } }
    }

    override suspend fun _addFavourite(favourite: Favourite) {
        addFavourites(favourite)
    }

    override suspend fun removeFavourite(movieId: String) {
        favouritesData.remove(movieId)
        refreshFavourites()
    }

    override fun _observeIsFavourite(movieId: String): Flow<Boolean> {
        return favouritesFlow.map {
            it.any { it.movieId == movieId }
        }
    }

    override fun isFavourite(movieId: String): Boolean {
        return favouritesData[movieId] != null
    }


}