package com.example.ugd3_kelompok19.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun addUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM user")
    fun getUser() : List<User>

    @Query("SELECT * FROM user WHERE id =:user_id")
    fun getUser(user_id: Int) : User

    @Query("SELECT * FROM user WHERE email = :email AND password = :password;")
    fun checkUser(email:String, password:String): User?
}
