package com.example.recetas.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Handler
import android.os.Looper
import okhttp3.*
import java.io.IOException

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "recipes.db", null, 1) {

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE favorites (
                id INTEGER PRIMARY KEY,
                name TEXT,
                imageUrl TEXT
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS favorites")
        onCreate(db)
    }

    fun addFavorite(id: Int, name: String, imageUrl: String) {
        val db = writableDatabase
        val values = ContentValues()

        values.put("id", id)
        values.put("name", name)
        values.put("imageUrl", imageUrl)

        db.insertWithOnConflict(
            "favorites",
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )

        db.close()
    }

    fun deleteFavorite(id: Int) {
        val db = writableDatabase
        db.delete("favorites", "id=?", arrayOf(id.toString()))
        db.close()
    }


    fun getFavorites(): List<Recipe> {
        val db = readableDatabase
        val list = mutableListOf<Recipe>()

        val cursor = db.rawQuery("SELECT id, name, imageUrl FROM favorites", null)

        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Recipe(
                        id = cursor.getInt(0),
                        name = cursor.getString(1),
                        imageUrl = cursor.getString(2)
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return list
    }

    fun fetch(url: String, callback: (String?) -> Unit) {

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post { callback(null) }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                mainHandler.post { callback(body) }
            }
        })
    }
}

