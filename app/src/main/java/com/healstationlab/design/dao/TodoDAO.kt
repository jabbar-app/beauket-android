package com.healstationlab.design.dao

import androidx.room.*
import com.healstationlab.design.model.Todo

@Dao
interface TodoDAO {
    @Query("SELECT * FROM Todo")
    fun getAll() : Todo

    @Insert
    fun insert(todo : Todo)

    @Update
    fun update(todo : Todo)

    @Delete
    fun delete(todo : Todo)
}