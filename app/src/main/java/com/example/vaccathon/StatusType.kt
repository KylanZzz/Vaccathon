package com.example.vaccathon

enum class StatusType(val turns: Int, val imgId: Int) {
    COLD(2, R.drawable.cold),
    COVID(1, R.drawable.covid),
    CANCER(100, R.drawable.cancer),
    PROTECTED(2, R.drawable.protect),
    HEART_ATTACK(1, R.drawable.heartbreak),
    PLAGUE(3, R.drawable.plague),
}