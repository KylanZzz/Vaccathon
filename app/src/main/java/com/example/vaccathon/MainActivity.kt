package com.example.vaccathon

import android.app.ActionBar.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import javax.net.ssl.SSLEngineResult.Status

class MainActivity : AppCompatActivity() {
    private lateinit var playerHandLayout: LinearLayout
    private lateinit var healthBarLayout: LinearLayout
    private lateinit var statusBarLayout: LinearLayout
    private lateinit var fieldLayout: ConstraintLayout
    private lateinit var leftStatusLayout: LinearLayout
    private lateinit var rightStatusLayout: LinearLayout
    private lateinit var topStatusLayout: LinearLayout
    private val game = Game()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerHandLayout = findViewById<LinearLayout>(R.id.HandLayout)
        healthBarLayout = findViewById<LinearLayout>(R.id.HealthLayout)
        statusBarLayout = findViewById<LinearLayout>(R.id.StatusLayout)
        fieldLayout = findViewById<ConstraintLayout>(R.id.FieldLayout)
        leftStatusLayout = findViewById<LinearLayout>(R.id.leftStatusLayout)
        rightStatusLayout  = findViewById<LinearLayout>(R.id.rightStatusLayout)
        topStatusLayout = findViewById<LinearLayout>(R.id.topStatusLayout)

        generateHealthBar(10)
        createOtherHands()

        updateBoard()
    }

    private fun updateBoard(){
        updateUserHand()
        updateAllStatuses()
//        updateAllHealthPoints()
    }

    private fun updateAllHealthPoints(){

    }

    private fun updateAllStatuses(){
        statusBarLayout.removeAllViews()
        leftStatusLayout.removeAllViews()
        rightStatusLayout.removeAllViews()
        topStatusLayout.removeAllViews()

        //user statuses
        for (s in game.user.status){
            addStatus(statusBarLayout, s.first)
        }

        //other 3 players
        for (s in game.getPlayerList()[1].status){
            addStatus(rightStatusLayout, s.first)
        }
        for (s in game.getPlayerList()[2].status){
            addStatus(topStatusLayout, s.first)
        }
        for (s in game.getPlayerList()[3].status){
            addStatus(leftStatusLayout, s.first)
        }
    }

    private fun updateUserHand() {
        playerHandLayout.removeAllViews()
        for (card in game.getUserHand()){
            addCardToHand(card)
        }
    }

    private fun createOtherHands(){
        val rightHand = LayoutInflater.from(this).inflate(R.layout.card_view, fieldLayout, false)
        rightHand.rotation=270.0f
        fieldLayout.addView(rightHand)
        val rightLayoutParams = ConstraintLayout.LayoutParams(
            220,
            367,
        )
        rightLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        rightLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        rightLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        rightHand.layoutParams = rightLayoutParams

        val leftHand = LayoutInflater.from(this).inflate(R.layout.card_view, fieldLayout, false)
        leftHand.rotation=270.0f
        fieldLayout.addView(leftHand)
        val leftLayoutParams = ConstraintLayout.LayoutParams(
            220,
            367,
        )
        leftLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        leftLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        leftLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        leftHand.layoutParams = leftLayoutParams


        val topHand = LayoutInflater.from(this).inflate(R.layout.card_view, fieldLayout, false)
        topHand.rotation=270.0f
        fieldLayout.addView(topHand)
        val topLayoutParams = ConstraintLayout.LayoutParams(
            367,
            220,
        )
        topLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        topLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        topLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        topHand.layoutParams = topLayoutParams
    }

    private fun addStatus(playerStatus: LinearLayout, status: StatusType){
        val statusView = ImageView(this)

        statusView.setImageResource(status.imgId)
        statusView.layoutParams = LinearLayout.LayoutParams(120,120)

        playerStatus.addView(statusView)
    }

    private fun addCardToHand(card: CardType){
        val cardView = LayoutInflater.from(this).inflate(R.layout.card_view, playerHandLayout, false)

        val textBox = cardView.findViewById<TextView>(R.id.cardLabel)
        textBox.text = card.label

        val imageBox = cardView.findViewById<ImageView>(R.id.cardImage)
        imageBox.setImageResource(card.imgId)

        cardView.setOnClickListener{
            game.playCard(card)
            updateBoard()
        }

        playerHandLayout.addView(cardView)
    }

    private fun addLifePoint() {
        val healthPointView = ImageView(this)
        healthPointView.setImageResource(R.drawable.life_point)

        healthPointView.layoutParams = LinearLayout.LayoutParams(120, 120,)
        healthBarLayout.addView(healthPointView)
    }

    private fun generateHealthBar(numHearts: Int){
        for (i in 1..numHearts){
            addLifePoint()
        }
    }
}