package com.example.ugd3_kelompok19.api

class peminjamApi {
    companion object{
        val BASE_URL ="http://192.168.0.115/TUBES_Kelompok19_PERPUZ/public/api/"

        val GET_ALL_URL = BASE_URL + "peminjam/"
        val GET_BY_ID_URL = BASE_URL + "peminjam/"
        val ADD_URL = BASE_URL + "peminjam"
        val UPDATE_URL = BASE_URL + "peminjam/"
        val DELETE_URL = BASE_URL + "peminjam/"

    }
}