package com.tia_0653.mobilebanking

class userBankAPi {
    companion object{
        val  BASE_URl = "http://henryyg.com/mobilebanking/public/api/"

        val GET_ALL_URL = BASE_URl+ "userBank/"
        val GET_BY_ID_URL = BASE_URl+"userBank/"
        val ADD_URL = BASE_URl+"userBank"
        val login_url = BASE_URl+"login"
        val UPDATE_URL = BASE_URl+"userBank/"
        val DELETE_URL = BASE_URl+"userBank/"
    }
}