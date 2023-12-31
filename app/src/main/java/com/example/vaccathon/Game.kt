package com.example.vaccathon

import javax.net.ssl.SSLEngineResult.Status
import kotlin.random.Random

class Game(private val activity: MainActivity) {
    private var gameOver: Boolean = false
    private var turn: Int = 0
    private var gameDeck: ArrayList<CardType> = ArrayList()
    private var playersList: ArrayList<Player> = ArrayList()
    private val startingCards = 5
private val startingLifePoints = 7
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

        // always have one of these on
        condensedDeck.add(Pair(CardType.SYRINGE, 40))
        condensedDeck.add(Pair(CardType.MONEY, 40))

        //change these instead
        condensedDeck.add(Pair(CardType.SYRINGE, 20))
        condensedDeck.add(Pair(CardType.SNEEZE, 10))
        condensedDeck.add(Pair(CardType.PLAGUE, 10))
        condensedDeck.add(Pair(CardType.COVID19, 10))
        condensedDeck.add(Pair(CardType.COMMONCOLD, 15))
        condensedDeck.add(Pair(CardType.ADVIL, 10))
        condensedDeck.add(Pair(CardType.VITAMINC, 10))
        condensedDeck.add(Pair(CardType.HAZMATSUIT, 5))
        condensedDeck.add(Pair(CardType.MONEY, 15))
        condensedDeck.add(Pair(CardType.CANCER, 10))
        condensedDeck.add(Pair(CardType.SCARE, 30))
        for (pair in condensedDeck){
            for (j in 1..pair.second){
                gameDeck.add(pair.first)
            }
        }
    }

    public fun playCard(player: Player, card: CardType){
        var shouldDraw: Boolean = false

        when (card){
            CardType.ADVIL -> if(checkStatus(player.status, StatusType.COLD)) {
                removeCard(player.hand, CardType.ADVIL)
                removeStatus(player.status, StatusType.COLD)
                shouldDraw = true
            }
            CardType.SYRINGE -> if (player.hand.count{it == CardType.SYRINGE} >= 3){
                removeCard(player.hand, CardType.SYRINGE)
                removeCard(player.hand, CardType.SYRINGE)
                removeCard(player.hand, CardType.SYRINGE)
                player.hand.add(CardType.MEDKIT)
                shouldDraw = true
            }
            CardType.MEDKIT -> if (player.status.isNotEmpty()){

                if (player.status.any{it.first != StatusType.PROTECTED && it.first != StatusType.HEART_ATTACK}) {
                    removeCard(player.hand, CardType.MEDKIT)
                    val tempIndexList: ArrayList<Int> = ArrayList<Int>()
                    for (i in 0..<player.status.count()){
                        if (player.status[i].first != StatusType.PROTECTED && player.status[i].first != StatusType.HEART_ATTACK) tempIndexList.add(i)
                    }
                    for (j in tempIndexList.reversed()){
                        player.status.removeAt(j)
                    }
                    shouldDraw = true
                }
            }
            CardType.MASK -> if(checkStatus(player.status, StatusType.COVID)) {
                removeCard(player.hand, CardType.MASK)
                removeStatus(player.status, StatusType.COVID)
                shouldDraw = true
            }
            CardType.HAZMATSUIT -> if(!checkStatus(player.status, StatusType.PROTECTED)){
                removeCard(player.hand, CardType.HAZMATSUIT)
                player.status.add(Pair(StatusType.PROTECTED, StatusType.PROTECTED.turns))
                shouldDraw = true
            }
            CardType.MONEY -> if (player.hand.count{it == CardType.MONEY} >= 2){ // add action later that allows user to select what to add
                removeCard(player.hand, CardType.MONEY)
                removeCard(player.hand, CardType.MONEY)
                player.hand.add(CardType.HAZMATSUIT)
                shouldDraw = true
            }
            CardType.VITAMINC -> if(checkStatus(player.status, StatusType.PLAGUE)) {
                removeCard(player.hand, CardType.VITAMINC)
                removeStatus(player.status, StatusType.PLAGUE)
                shouldDraw = true
            }
            CardType.SNEEZE -> {
                removeCard(player.hand, CardType.SNEEZE)
                activity.getPlayerChoice(CardType.SNEEZE)
            }
            CardType.SCARE -> {
                removeCard(player.hand, CardType.SCARE)
                activity.getPlayerChoice(CardType.SCARE)
            }
            else -> null
        }

        if (shouldDraw && player == user) runner()
    }

    fun attack(card: CardType, atkTarget: Int){
        var status: StatusType = StatusType.COLD
        if (card == CardType.SCARE) status = StatusType.HEART_ATTACK

        if ( !checkStatus(playersList[atkTarget].status, status) ){
            playersList[atkTarget].status.add(Pair(status, 1))
            activity.clearChoiceLayout()
        }
        activity.updateBoard()
        runner()
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

    private fun doBotStuff(){
        for (robot in 1..3) {
            println("player$robot: ${ playersList[robot].hand }")
            var count = playersList[robot].hand.count()
            if (playersList[robot].hand.contains(CardType.SCARE)) {
                var num = Random.nextInt(0,4)
                while(num == robot){
                    num = Random.nextInt(0,4)
                }
                if ( !checkStatus(playersList[num].status, StatusType.HEART_ATTACK) ) {
                    playersList[num].status.add(Pair(StatusType.HEART_ATTACK, 1))
                    removeCard(playersList[robot].hand, CardType.SCARE)
                }
            }
            else if (playersList[robot].hand.contains(CardType.SNEEZE)) {
                var num = Random.nextInt(0,4)
                while(num == robot){
                    num = Random.nextInt(0,4)
                }
                if ( !checkStatus(playersList[num].status, StatusType.COLD) ) {
                    playersList[num].status.add(Pair(StatusType.COLD, 1))
                    removeCard(playersList[robot].hand, CardType.SNEEZE)
                }
            }
            else{
                for (cards in playersList[robot].hand) {
                    playCard(playersList[robot], cards)
                    if (playersList[robot].hand.count() < count) {
                        break
                    }
                }
            }
            calculateNewHp(playersList[robot])
            drawCard(playersList[robot])
        }
    }

    public fun runner(){
        //end the users turn
        calculateNewHp(user)
        drawCard(user)

        // call bots to do stuff
        doBotStuff()

        if(checkGameOver()){
            activity.gameOverScreen(user.lifePoints != 0)
            println("GAME OVER")
        }
    }

    private fun calculateNewHp(player: Player) {
        for (index in 0..<player.status.count()){

            val statusPair = player.status[index]
            if (player.lifePoints > 0 && (statusPair.first != StatusType.PROTECTED)) player.lifePoints -= 1
            player.status[index] = Pair(statusPair.first, statusPair.second - 1)
        }

        val tempIndexList: ArrayList<Int> = ArrayList<Int>()

        for (index in 0..<player.status.count()){
            if (player.status[index].second == 0) tempIndexList.add(index)
        }
        for (index in tempIndexList.reversed()){
            player.status.removeAt(index)
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

        //give random statuses to user
    }

    private fun createHand(player: Player){
        for (i in 0..<startingCards){
            drawCard(player)
        }
    }

    private fun checkGameOver(): Boolean{
        return (user.lifePoints == 0) || (playersList[2].lifePoints == 0 && playersList[1].lifePoints == 0 && playersList[3].lifePoints == 0)
    }

}