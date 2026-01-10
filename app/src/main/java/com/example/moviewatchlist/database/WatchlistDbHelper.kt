package com.example.moviewatchlist.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.moviewatchlist.model.Movie

class WatchlistDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "watchlist.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "movies"

        const val COL_IMDB_ID = "imdb_id"
        const val COL_TITLE = "title"
        const val COL_YEAR = "year"
        const val COL_TYPE = "type"
        const val COL_WATCHED = "watched"
        const val COL_POSTER = "poster"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_NAME (
                $COL_IMDB_ID TEXT PRIMARY KEY,
                $COL_TITLE TEXT NOT NULL,
                $COL_YEAR INTEGER NOT NULL,
                $COL_TYPE TEXT NOT NULL,
                $COL_WATCHED INTEGER NOT NULL,
                $COL_POSTER BLOB
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insert(movie: Movie): Boolean {
        val cursor = readableDatabase.query(
            TABLE_NAME,
            arrayOf(COL_IMDB_ID),
            "$COL_IMDB_ID = ?",
            arrayOf(movie.imdbId),
            null, null, null
        )

        val exists = cursor.count > 0
        cursor.close()

        if (exists) {
            return false
        }

        val values = ContentValues().apply {
            put(COL_IMDB_ID, movie.imdbId)
            put(COL_TITLE, movie.title)
            put(COL_YEAR, movie.year)
            put(COL_TYPE, movie.type)
            put(COL_WATCHED, if (movie.watched) 1 else 0)
            put(COL_POSTER, movie.poster)
        }

        val result = writableDatabase.insert(
            TABLE_NAME,
            null,
            values
        )

        return result != -1L
    }

    fun delete(imdbId: String) {
        writableDatabase.delete(
            TABLE_NAME,
            "$COL_IMDB_ID = ?",
            arrayOf(imdbId)
        )
    }

    fun setWatched(imdbId: String, watched: Boolean) {
        val values = ContentValues().apply {
            put(COL_WATCHED, if (watched) 1 else 0)
        }

        writableDatabase.update(
            TABLE_NAME,
            values,
            "$COL_IMDB_ID = ?",
            arrayOf(imdbId)
        )
    }

    fun getAllMovies(): List<Movie> {
        val movies = mutableListOf<Movie>()
        val db = readableDatabase

        val cursor = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "$COL_TITLE ASC"
        )

        cursor.use {
            while (it.moveToNext()) {
                movies.add(
                    Movie(
                        imdbId = it.getString(it.getColumnIndexOrThrow(COL_IMDB_ID)),
                        title = it.getString(it.getColumnIndexOrThrow(COL_TITLE)),
                        year = it.getInt(it.getColumnIndexOrThrow(COL_YEAR)),
                        type = it.getString(it.getColumnIndexOrThrow(COL_TYPE)),
                        watched = it.getInt(it.getColumnIndexOrThrow(COL_WATCHED)) == 1,
                        poster = it.getBlob(it.getColumnIndexOrThrow(COL_POSTER))
                    )
                )
            }
        }

        return movies
    }
}
