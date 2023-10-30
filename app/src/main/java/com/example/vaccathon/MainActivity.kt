package com.example.vaccathon

import android.app.ActionBar.LayoutParams
import android.graphics.Color
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setMargins
import javax.net.ssl.SSLEngineResult.Status

class MainActivity : AppCompatActivity() {
    private lateinit var playerHandLayout: LinearLayout
    private lateinit var healthBarLayout: LinearLayout
    private lateinit var statusBarLayout: LinearLayout
    private lateinit var fieldLayout: ConstraintLayout
    private lateinit var leftStatusLayout: LinearLayout
    private lateinit var rightStatusLayout: LinearLayout
    private lateinit var topStatusLayout: LinearLayout

    private lateinit var chooseCardLayout: LinearLayout

    private lateinit var rightHeartCount: TextView
    private lateinit var topHeartCount: TextView
    private lateinit var leftHeartCount: TextView


    private val game = Game(this)

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

        chooseCardLayout = findViewById<LinearLayout>(R.id.chooseCardLayout)

        rightHeartCount = findViewById<TextView>(R.id.rightPlayerHeartCount)
        topHeartCount = findViewById<TextView>(R.id.topPlayerHeartCount)
        leftHeartCount = findViewById<TextView>(R.id.leftPlayerHeartCount)


        val deckPile = LayoutInflater.from(this).inflate(R.layout.card_view, fieldLayout, false)
        fieldLayout.addView(deckPile)
        val imageView = ImageView(this)
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        imageView.layoutParams = layoutParams
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(R.drawable.cardback)
        deckPile.background = imageView.drawable


        val deckLayoutParams = ConstraintLayout.LayoutParams(
            220,
            330
        )
        deckPile.setOnClickListener(){
            game.runner()
            updateBoard()
        }
        deckLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        deckLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        deckLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        deckLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        deckLayoutParams.horizontalBias = 0.5f
        deckLayoutParams.verticalBias= 0.42f
        deckPile.layoutParams = deckLayoutParams


        generateHealthBar(10)
        createOtherHands()

        updateBoard()

        //test button to draw card
//        val testButton = findViewById<Button>(R.id.testButton)
//        testButton.setOnClickListener(){
//            game.runner()
//            updateBoard()
//        }
    }

    public fun updateBoard(){
        updateUserHand()
        updateAllStatuses()
        updateAllHealthPoints()
    }

    private fun updateAllHealthPoints(){
        healthBarLayout.removeAllViews()

        for (num in 1..game.user.lifePoints){
            addLifePoint()
        }

        rightHeartCount.text = "x${game.getPlayerList()[1].lifePoints}"
        topHeartCount.text = "x${game.getPlayerList()[2].lifePoints}"
        leftHeartCount.text = "x${game.getPlayerList()[3].lifePoints}"

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
            220, //220
            367, //367
        )
        rightLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        rightLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        rightLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        rightHand.layoutParams = rightLayoutParams
        val imageViewR = ImageView(this)
        val layoutParamsR = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        imageViewR.layoutParams = layoutParamsR
        imageViewR.scaleType = ImageView.ScaleType.CENTER_CROP
        imageViewR.setImageResource(R.drawable.cardback)
        rightHand.background = imageViewR.drawable



        val leftHand = LayoutInflater.from(this).inflate(R.layout.card_view, fieldLayout, false)
        leftHand.rotation=90.0f
        fieldLayout.addView(leftHand)
        val leftLayoutParams = ConstraintLayout.LayoutParams(
            220,
            367,
        )
        leftLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        leftLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        leftLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        leftHand.layoutParams = leftLayoutParams
        val imageViewL = ImageView(this)
        val layoutParamsL = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        imageViewL.layoutParams = layoutParamsL
        imageViewL.scaleType = ImageView.ScaleType.CENTER_CROP
        imageViewL.setImageResource(R.drawable.cardback)
        leftHand.background = imageViewL.drawable


        val topHand = LayoutInflater.from(this).inflate(R.layout.card_view, fieldLayout, false)
        topHand.rotation=180.0f
        fieldLayout.addView(topHand)
        val topLayoutParams = ConstraintLayout.LayoutParams(
            220,
            330,
        )
        topLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        topLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        topLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        topHand.layoutParams = topLayoutParams
        val imageViewT = ImageView(this)
        val layoutParamsT = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        imageViewT.layoutParams = layoutParamsT
        imageViewT.scaleType = ImageView.ScaleType.CENTER_CROP
        imageViewT.setImageResource(R.drawable.cardback)
        topHand.background = imageViewT.drawable

    }

    private fun addStatus(playerStatus: LinearLayout, status: StatusType){
        val statusView = ImageView(this)

        statusView.setImageResource(status.imgId)
        statusView.layoutParams = LinearLayout.LayoutParams(120,120)

        playerStatus.addView(statusView)

        //temp code so i can see what each status is
//        val statusView = TextView(this)
//
//        statusView.text = status.name
//        statusView.textSize = 14.0f
//        statusView.layoutParams = LinearLayout.LayoutParams(170,120)
//
//        playerStatus.addView(statusView)
    }

    private fun addCardToHand(card: CardType){
        val cardView = LayoutInflater.from(this).inflate(R.layout.card_view, playerHandLayout, false)

        val textBox = cardView.findViewById<TextView>(R.id.cardLabel)
        textBox.text = card.label

        val imageBox = cardView.findViewById<ImageView>(R.id.cardImage)
        imageBox.setImageResource(card.imgId)

        cardView.setOnClickListener{
            game.playCard(game.user, card)
            updateBoard()
        }


//        cardView.setBackgroundColor(Color.parseColor("@color/"))
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

    fun clearChoiceLayout(){
        chooseCardLayout.removeAllViews()
    }

    fun gameOverScreen(userWon: Boolean){
        chooseCardLayout.removeAllViews()

        fun createCardLayoutParams(width: Int, height: Int): LinearLayout.LayoutParams {
            return LinearLayout.LayoutParams(width, height).apply {
                setMargins(50, 50, 50, 50)
                // Any additional layout parameters can be set here
            }
        }

        val thirdPlayerCard = LayoutInflater.from(this).inflate(R.layout.card_view, playerHandLayout, false)
        val layoutParams2 = createCardLayoutParams(500, 833) // replace with your desired width and height
        thirdPlayerCard.layoutParams = layoutParams2
        val textBox2 = thirdPlayerCard.findViewById<TextView>(R.id.cardLabel)
        var winLossText: String = "Lost. :("
        textBox2.textSize = 10f
        if(userWon) winLossText = "Won!"
//        textBox2.text = "Game Over \n You $winLossText"
        val imageBox2 = thirdPlayerCard.findViewById<ImageView>(R.id.cardImage)
        imageBox2.setImageResource(R.drawable.game_over)
        chooseCardLayout.addView(thirdPlayerCard)
    }

    fun getPlayerChoice(card: CardType) {
        //create selection screen
        fun createCardLayoutParams(width: Int, height: Int): LinearLayout.LayoutParams {
            return LinearLayout.LayoutParams(width, height).apply {
                setMargins(50, 50, 50, 50)
                // Any additional layout parameters can be set here
            }
        }

        // create views
        val thirdPlayerCard = LayoutInflater.from(this).inflate(R.layout.card_view, playerHandLayout, false)
        val layoutParams2 = createCardLayoutParams(500, 833) // replace with your desired width and height
        thirdPlayerCard.layoutParams = layoutParams2
        val textBox2 = thirdPlayerCard.findViewById<TextView>(R.id.cardLabel)
        textBox2.text = "Player 4"
        val imageBox2 = thirdPlayerCard.findViewById<ImageView>(R.id.cardImage)
        imageBox2.setImageResource(R.drawable.pfp4)
        thirdPlayerCard.setOnClickListener{
            game.attack(card, 3)
        }
        chooseCardLayout.addView(thirdPlayerCard)

        val secondPlayerCard = LayoutInflater.from(this).inflate(R.layout.card_view, playerHandLayout, false)
        val layoutParams1 = createCardLayoutParams(500, 833) // replace with your desired width and height
        secondPlayerCard.layoutParams = layoutParams1
        val textBox1 = secondPlayerCard.findViewById<TextView>(R.id.cardLabel)
        textBox1.text = "Player 3"
        val imageBox1 = secondPlayerCard.findViewById<ImageView>(R.id.cardImage)
        imageBox1.setImageResource(R.drawable.pfp3)
        secondPlayerCard.setOnClickListener{
            game.attack(card, 2)
        }
        chooseCardLayout.addView(secondPlayerCard)

        val firstPlayerCard = LayoutInflater.from(this).inflate(R.layout.card_view, playerHandLayout, false)
        val layoutParams0 = createCardLayoutParams(500, 833) // replace with your desired width and height
        firstPlayerCard.layoutParams = layoutParams0
        val textBox0 = firstPlayerCard.findViewById<TextView>(R.id.cardLabel)
        textBox0.text = "Player 2"
        val imageBox0 = firstPlayerCard.findViewById<ImageView>(R.id.cardImage)
        imageBox0.setImageResource(R.drawable.pfp2)
        firstPlayerCard.setOnClickListener{
            game.attack(card, 1)
        }
        chooseCardLayout.addView(firstPlayerCard)

    }
}