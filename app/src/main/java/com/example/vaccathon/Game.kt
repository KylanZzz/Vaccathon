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

    public fun playCard(card: CardType){
        when (card){
            CardType.ADVIL -> if(checkStatus(user.status, StatusType.COLD)) {
                removeCard(user.hand, CardType.ADVIL)
                removeStatus(user.status, StatusType.COLD)
                userDrawCard()
            }
            CardType.SYRINGE -> if (user.hand.count{it == CardType.SYRINGE} >= 3){
                removeCard(user.hand, CardType.SYRINGE)
                removeCard(user.hand, CardType.SYRINGE)
                removeCard(user.hand, CardType.SYRINGE)
                user.hand.add(CardType.MEDKIT)
                userDrawCard()
            }
            CardType.MEDKIT -> {
                val tempIndexList: ArrayList<Int> = ArrayList<Int>()
                for (i in 0..<user.status.count()){
                    if (user.status[i].first != StatusType.PROTECTED) tempIndexList.add(i)
                }
                for (j in tempIndexList.reversed()){
                    user.status.removeAt(j)
                }
                userDrawCard()
            }
            CardType.MASK -> if(checkStatus(user.status, StatusType.COVID)) {
                removeCard(user.hand, CardType.MASK)
                removeStatus(user.status, StatusType.COVID)
                userDrawCard()
            }
            CardType.HAZMATSUIT -> if(!checkStatus(user.status, StatusType.PROTECTED)){
                user.status.add(Pair(StatusType.PROTECTED, StatusType.PROTECTED.turns))
                userDrawCard()
            }
            else -> null
        }
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

    private fun userDrawCard(){
        drawCard(user)

        // call bots to do stuff
    }

    private  fun initializePlayers(){
        //create main player
        playersList.add(Player(createHand(startingCards), startingLifePoints, ArrayList<Pair<StatusType, Int>>(), true))

        //create 3 bots
        for (i in 1..3){
            playersList.add(Player(createHand(startingCards), startingLifePoints, ArrayList<Pair<StatusType, Int>>(), false))
        }

        //set user to first player in playerList
        user = playersList[0]

        // give random illnesses at initialization
//        user.status.add(Pair(StatusType.PROTECTED, StatusType.PROTECTED.turns))
//        user.status.add(Pair(StatusType.PLAGUE, StatusType.PLAGUE.turns))
//        user.status.add(Pair(StatusType.COVID, StatusType.COVID.turns))
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

    private fun createHand(numCards: Int): ArrayList<CardType>{
        var tempHand: ArrayList<CardType> = ArrayList()

        for (i in 1..numCards){
            val index: Int = Random.nextInt(0,gameDeck.count())
            tempHand.add(gameDeck[index])
            gameDeck.removeAt(index)
        }

        return tempHand
    }

    private fun initializeDeck(): Unit{
        var condensedDeck = ArrayList<Pair<CardType,Int>>()

        condensedDeck.add(Pair(CardType.HAZMATSUIT, 20))
        condensedDeck.add(Pair(CardType.COMMONCOLD, 20))
        condensedDeck.add(Pair(CardType.PLAGUE, 20))
        condensedDeck.add(Pair(CardType.VITAMINC, 20))
        condensedDeck.add(Pair(CardType.SYRINGE, 40))

        for (pair in condensedDeck){
             for (j in 1..pair.second){
                gameDeck.add(pair.first)
            }
        }
    }
    private fun checkGameOver(): Boolean{
        return user.lifePoints == 0
    }

}