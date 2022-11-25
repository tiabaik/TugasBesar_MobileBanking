package com.tia_0653.mobilebanking

class transaksiBankApi {
    companion object{
        val  BASE_URl = "http://192.168.137.234:8081/TUBES_API/public/api/"

        val GET_ALL_URL = BASE_URl+ "transaksis/"
        val GET_BY_ID_URL = BASE_URl+"transaksis/"
        val ADD_URL = BASE_URl+"transaksis"
        val UPDATE_URL = BASE_URl+"transaksis/"
        val DELETE_URL = BASE_URl+"transaksis/"



    }
}