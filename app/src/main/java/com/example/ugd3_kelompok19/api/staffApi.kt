package com.example.ugd3_kelompok19.api

class staffApi {
    companion object{
        val BASE_URL ="http://192.168.0.111/TUBES_Kelompok19_PERPUZ/public/api/"

        val GET_ALL_URL = BASE_URL + "staff/"
        val GET_BY_ID_URL = BASE_URL + "staff/"
        val ADD_URL = BASE_URL + "staff"
        val UPDATE_URL = BASE_URL + "staff/"
        val DELETE_URL = BASE_URL + "staff/"

    }
}