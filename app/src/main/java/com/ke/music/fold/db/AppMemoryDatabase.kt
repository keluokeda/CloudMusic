package com.ke.music.fold.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ke.music.fold.db.dao.CommentDao
import com.ke.music.fold.db.entity.Comment


@Database(
    entities = [Comment::class],
    version = 1
)
abstract class AppMemoryDatabase : RoomDatabase() {

    abstract fun commentDao(): CommentDao
}