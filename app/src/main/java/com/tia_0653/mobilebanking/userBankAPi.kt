package com.tia_0653.mobilebanking

class userBankAPi {
    companion object{
        val  BASE_URl = "http://10.53.5.116:8081/TUBES_API/public/api/"

        val GET_ALL_URL = BASE_URl+ "userBank/"
        val GET_BY_ID_URL = BASE_URl+"userBank/"
        val ADD_URL = BASE_URl+"userBank"
        val UPDATE_URL = BASE_URl+"userBank/"
        val DELETE_URL = BASE_URl+"userBank/"
    }
}