package com.diegoparra.kino.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.Instant

@Dao
abstract class FavouritesDao {

    @Query("Select movieId from Favourite")
    protected abstract fun _observeFavourites(): Flow<List<String>>
    fun observeFavourites(): Flow<List<String>> = _observeFavourites().distinctUntilChanged()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun _addFavourite(favourite: Favourite)
    suspend fun addFavourite(movieId: String) {
        _addFavourite(Favourite(movieId = movieId, updatedAt = Instant.now()))
    }

    @Query("DELETE FROM Favourite WHERE movieId = :movieId")
    abstract suspend fun removeFavourite(movieId: String)

    @Query("SELECT EXISTS(SELECT * FROM Favourite WHERE movieId = :movieId)")
    protected abstract fun _observeIsFavourite(movieId: String): Flow<Boolean>
    fun observeIsFavourite(movieId: String): Flow<Boolean> =
        _observeIsFavourite(movieId).distinctUntilChanged()

    @Query("SELECT EXISTS(SELECT * FROM Favourite WHERE movieId = :movieId)")
    abstract fun isFavourite(movieId: String): Boolean

}