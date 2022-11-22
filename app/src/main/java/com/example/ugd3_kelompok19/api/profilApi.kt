package com.example.ugd3_kelompok19.api

class profilApi {
    companion object{
        val BASE_URL ="http://192.168.0.108/TUBES_Kelompok19_PERPUZ/public/api/"

        val GET_BY_ID_URL = BASE_URL + "profil/"
        val UPDATE_URL = BASE_URL + "profil/"
        val DELETE_URL = BASE_URL + "profil/"
        val REG = BASE_URL + "register"
        val LOG = BASE_URL + "loginCheck"

    }
}

