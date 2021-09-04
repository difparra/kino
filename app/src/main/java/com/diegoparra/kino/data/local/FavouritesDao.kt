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
    protected abstract fun _getFavourites(): Flow<List<String>>
    fun getFavourites(): Flow<List<String>> = _getFavourites().distinctUntilChanged()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun _addFavourite(favourite: Favourite)
    suspend fun addFavourite(movieId: String) {
        _addFavourite(Favourite(movieId = movieId, updatedAt = Instant.now()))
    }

    @Query("DELETE FROM Favourite WHERE movieId = :movieId")
    abstract suspend fun removeFavourite(movieId: String)

    @Query("SELECT EXISTS(SELECT * FROM Favourite WHERE movieId = :movieId)")
    protected abstract fun _isFavourite(movieId: String): Flow<Boolean>
    fun isFavourite(movieId: String): Flow<Boolean> =
        _isFavourite(movieId).distinctUntilChanged()

    @Query("SELECT EXISTS(SELECT * FROM Favourite WHERE movieId = :movieId)")
    abstract fun isFavouriteSingle(movieId: String): Boolean

}