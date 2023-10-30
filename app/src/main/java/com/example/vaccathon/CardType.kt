package com.example.vaccathon;

enum class CardType(val label: String, val imgId: Int) {
    SYRINGE("Syringe", R.drawable.syringe),
    MEDKIT("Medkit", R.drawable.medkit),
    MASK("Mask", R.drawable.mask),
    ADVIL("Advil", R.drawable.painkiller),
    COVID19("Covid19", R.drawable.covid),
    COMMONCOLD("Commoncold", R.drawable.cold),
    CANCER("Cancer", R.drawable.cancer),
    HAZMATSUIT("Hazmatsuit", R.drawable.hazmat),
    PLAGUE("Plague", R.drawable.plague),
    MONEY("Money", R.drawable.money),
    SCARE("Scare", R.drawable.scared),
    SNEEZE("Sneeze", R.drawable.sneeze),
    VITAMINC("Vitamin C", R.drawable.vitaminc),

}