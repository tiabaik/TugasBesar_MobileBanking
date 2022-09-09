package com.tia_0653.mobilebanking.entity


class daftarNamaBank(var namebank: String, var kodebank: String) {
    companion object{
        @JvmField
        var listOfdaftarNamaBank = arrayOf(
            daftarNamaBank("BCA", "014"),
            daftarNamaBank("Mandiri", "008"),
            daftarNamaBank("BRI", "002"),
            daftarNamaBank("CIMB Niaga", "022"),
            daftarNamaBank("Danamon", "011"),
            daftarNamaBank("Mega", "426" ),
            daftarNamaBank("Panin", "019"),
            daftarNamaBank("Kalbar", "123"),
            daftarNamaBank("kalteng", "125"),
            daftarNamaBank("sumut", "117")

        )
    }
}