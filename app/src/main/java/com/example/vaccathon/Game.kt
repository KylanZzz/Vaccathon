package com.example.vaccathon

import javax.net.ssl.SSLEngineResult.Status
import kotlin.random.Random

class Game() {
    private var gameOver: Boolean = false
    private var turn: Int = 0
    private var gameDeck: ArrayList<CardType> = ArrayList()
    private var playersList: ArrayList<Player> = ArrayList()
    private val startingCards = 5
    private val startingLifePoints = 10
    lateinit var user: Player


    init {
        initializeDeck()
        initializePlayers()
    }

    public fun getUserHand(): ArrayList<CardType>{
        return user.hand
    }

    public fun getPlayerList(): ArrayList<Player>{
        return playersList
    }

    private fun initializeDeck(): Unit{
        var condensedDeck = ArrayList<Pair<CardType,Int>>()

        // do not remove this bro
        condensedDeck.add(Pair(CardType.SYRINGE, 40))

        //change these instead
        condensedDeck.add(Pair(CardType.MONEY, 40))
        condensedDeck.add(Pair(CardType.COVID19, 40))

        for (pair in condensedDeck){
            for (j in 1..pair.second){
                gameDeck.add(pair.first)
            }
        }
    }

    public fun playCard(player: Player, card: CardType){
        var shouldDraw: Boolean = true

        when (card){
            CardType.ADVIL -> if(checkStatus(player.status, StatusType.COLD)) {
                removeCard(player.hand, CardType.ADVIL)
                removeStatus(player.status, StatusType.COLD)
            }
            CardType.SYRINGE -> if (player.hand.count{it == CardType.SYRINGE} >= 3){
                removeCard(player.hand, CardType.SYRINGE)
                removeCard(player.hand, CardType.SYRINGE)
                removeCard(player.hand, CardType.SYRINGE)
                player.hand.add(CardType.MEDKIT)
            }
            CardType.MEDKIT -> if (player.status.isNotEmpty()){
                if (!(player.status[0].first == StatusType.PROTECTED && player.status.count() == 1)) {
                    val tempIndexList: ArrayList<Int> = ArrayList<Int>()
                    for (i in 0..<player.status.count()){
                        if (player.status[i].first != StatusType.PROTECTED) tempIndexList.add(i)
                    }
                    for (j in tempIndexList.reversed()){
                        player.status.removeAt(j)
                    }
                }
            }
            CardType.MASK -> if(checkStatus(player.status, StatusType.COVID)) {
                removeCard(player.hand, CardType.MASK)
                removeStatus(player.status, StatusType.COVID)
            }
            CardType.HAZMATSUIT -> if(!checkStatus(player.status, StatusType.PROTECTED)){
                player.status.add(Pair(StatusType.PROTECTED, StatusType.PROTECTED.turns))
            }
            CardType.MONEY -> if (player.hand.count{it == CardType.SYRINGE} >= 2){ // add action later that allows user to select what to add
                removeCard(player.hand, CardType.MONEY)
                removeCard(player.hand, CardType.MONEY)
                player.hand.add(CardType.HAZMATSUIT)
            }
            else -> shouldDraw = false
        }

        if (shouldDraw) userDrawCard()
    }

    private fun drawCard(player: Player){
        val index: Int = Random.nextInt(0,gameDeck.count()-1)
        val card = gameDeck[index]

        // check for drawing illness cards. has to make sure player does not already have the illness
        // and is not PROTECTED
        when (card){
            CardType.COMMONCOLD -> if(!checkStatus(player.status, StatusType.COLD) && !checkStatus(player.status, StatusType.PROTECTED)) player.status.add(Pair(StatusType.COLD, StatusType.COLD.turns))
            CardType.CANCER -> if(!checkStatus(player.status, StatusType.CANCER) && !checkStatus(player.status, StatusType.PROTECTED)) player.status.add(Pair(StatusType.CANCER, StatusType.CANCER.turns))
            CardType.COVID19 -> if(!checkStatus(player.status, StatusType.COVID) && !checkStatus(player.status, StatusType.PROTECTED)) player.status.add(Pair(StatusType.COVID, StatusType.COVID.turns))
            CardType.PLAGUE -> if(!checkStatus(player.status, StatusType.PLAGUE) && !checkStatus(player.status, StatusType.PROTECTED)) player.status.add(Pair(StatusType.PLAGUE, StatusType.PLAGUE.turns))
            else -> player.hand.add(card)
        }

        gameDeck.removeAt(index)
    }

    public fun userDrawCard(){
        drawCard(user)

        // call bots to do stuff
    }

    private fun checkStatus(statusList: ArrayList<Pair<StatusType, Int>>, status: StatusType): Boolean {
        return statusList.any { it.first == status }
    }

    private fun removeStatus(statusList: ArrayList<Pair<StatusType, Int>>, status: StatusType) {
        val iter = statusList.iterator()
        while (iter.hasNext()) {
            if (iter.next().first == status) {
                iter.remove()
                break
            }
        }
    }

    private fun removeCard(hand: ArrayList<CardType>, card: CardType) {
        val iter = hand.iterator()
        while (iter.hasNext()) {
            if (iter.next() == card) {
                iter.remove()
                break
            }
        }
    }

    private  fun initializePlayers(){
        //create main player
        playersList.add(Player(ArrayList<CardType>(), startingLifePoints, ArrayList<Pair<StatusType, Int>>()))

        //create 3 bots
        for (i in 1..3){
            playersList.add(Player(ArrayList<CardType>(), startingLifePoints, ArrayList<Pair<StatusType, Int>>()))
        }

        // filling their hands
        for (player in playersList){
            createHand(player)
        }

        //set user to first player in playerList
        user = playersList[0]

    }
    private fun createHand(player: Player){
        for (i in 0..<startingCards){
            drawCard(player)
        }
    }
    private fun checkGameOver(): Boolean{
        return user.lifePoints == 0
    }

}