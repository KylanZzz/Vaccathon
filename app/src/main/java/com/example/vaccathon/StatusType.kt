package com.example.vaccathon

enum class StatusType(val turns: Int, val imgId: Int) {
    COLD(2, R.drawable.skull_icon),
    COVID(1, R.drawable.covid_img),
    CANCER(100, R.drawable.skull_icon),
    PROTECTED(1, R.drawable.life_point),
    HEART_ATTACK(1, R.drawable.skull_icon),
    PLAGUE(3, R.drawable.skull_icon),
}