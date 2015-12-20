import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import scala.io.StdIn.readLine
import scala.io.StdIn.readInt


object PlayBlackjack {
  def main(args: Array[String]) {
    var game = new Blackjack
    println("Welcome to Blackjack! You have 100 chips.")
    game.newGame
  }
}


class Blackjack {
  private var gameDeck = new Deck
  private var player = new Player
  private var dealer = new Dealer

  private var firstGame = true
  private var dealerAdvantage = false
  private var playerStands = false
  private var betsAreDone = false
  private var pot = 0

  /**
   * Tulostaa jakajan ja pelaajan kädet sekä niiden kokonaispistemäärät konsoliin.
   * @.pre	true
   * @.post	true
   */
  def displayHandScore {
    evaluateHands
    Thread.sleep(1000)
    println("-----------------------------------------")
    println("Dealer's hand: " + dealer.getHand.mkString(", "))
    print("               Total score: ")
    displayCorrectValue(dealer)
    println
    println("Player's hand: " + player.getHand.mkString(", "))
    print("               Total score: ")
    displayCorrectValue(player)
  }

  /**
   * Esittää kokonaispistemäärän oikealla tavalla. (Onko kädessä ässä vai ei?)
   * @.pre	participant.displayCase != null & participant.handTotal != null &
   * 				participant.handTotalMinor != null & participant.trueHandTotal != null
   * @.post	true
   */
  def displayCorrectValue(participant: Participant){
    if (participant.displayCase == 1)
      println(participant.trueHandTotal)
    if (participant.displayCase == 2)
      println(participant.trueHandTotal)
    if (participant.displayCase == 3)
      println(participant.handTotalMinor + "/" + participant.handTotal)
  }

  /**
   * Esittää pelimerkkien määrän kädessä.
   * @.pre	player.chips != null
   * @.post	true
   */
  def displayChips {
    Thread.sleep(1000)
    println("-----------------------------------------")
    println("You have " + player.chips + " chips in hand.")
  }

 /**
  * Tarkistaa kummalla, jakajalla vai pelaajalla, on suurempi käden kokonaispistemäärä
  * ja muuttaa sen perusteella Boolean-arvoa 'dealerAdvantage'.
  * @.pre		dealerAdvantage != null
  * @.post	dealerAdvantage == true, jos jakajalla isompi tai yhtäsuuri käsi; muuten dealerAdvantage == false
  */
  def evaluateHands {
    if (dealer.maxValue >= player.maxValue){
      dealerAdvantage = true
    } else {
      dealerAdvantage = false
    }
  }

 /**
  * Tarkistaa, ettei pelaajan tai jakajan käsi ole yli 21 ja palauttaa siitä Boolean-arvon.
  * @.pre		dealer.handTotalMinor != null & player.handTotalMinor != null
  * @.post	RESULT == (true, jos kummankaan käden kokonaispistemäärä ei ylitä 21; muuten false)
  */
  def gameIsOn: Boolean = { if (dealer.trueHandTotal > 21 || player.trueHandTotal > 21) { false } else { true } }

 /**
  * Tarkistaa voittoehdot ja kysyy tarvittavat kysymykset pelin etenemisen kannalta.
  * Esittää myös pelitapahtumia konsolissa.
  * @.pre		playerStands != null & gameDeck != null & dealerAdvantage != null &
  * 				dealer.trueHandTotal != null & player.trueHandTotal != null &
  *        	gameIsOn != null & betsAreDone != null
  * @.post	Pelaajan halutessa ottaa kortin: gameDeck.length -= 1 & player.hand.length += 1
  * 				Jakajan ottaessa kortin: gameDeck.length -= 1 & dealer.hand.length += 1
  * 				Pelaajan standatessa: playerStands == true
  */
  def gameIteration {
    evaluateHands
    if (!betsAreDone){
      betHandler
    }
    if (gameIsOn){
      Thread.sleep(1000)
      println("-----------------------------------------")
      if (player.maxValue == 21) {
        println("Blackjack! Player wins!")
        winHandler(true)
        endGame
      } else if (dealer.maxValue == 21) {
        println("Blackjack! Dealer wins!")
        winHandler(false)
        endGame
      }
      if (!playerStands){
        var line = readLine("Would you like to hit or stand? ")
        Thread.sleep(1000)
        println("-----------------------------------------")
        if (line.toLowerCase == "h" || line.toLowerCase == "hit"){
          player.takeNewCard(gameDeck)
          println("Player takes a new card.")
          displayHandScore
          gameIteration
        } else if (line.toLowerCase == "s" || line.toLowerCase == "stand"){
          println("Player stands.")
          playerStands = true
          gameIteration
        } else {
          println("Excuse me? Answer 'hit' or 'stand'.")
          gameIteration
        }
      } else {
        if ((dealer.trueHandTotal < 17)){
          dealer.takeNewCard(gameDeck)
          println("Dealer takes a new card.")
          displayHandScore
          gameIteration
        } else {
          println("Dealer stands.")
          Thread.sleep(1000)
          println("-----------------------------------------")
          if (dealerAdvantage){
            println("Dealer wins with " + dealer.trueHandTotal + "!")
            winHandler(false)
          } else {
            println("Player wins with " + player.trueHandTotal + "!")
            winHandler(true)
          }
        }
      }
    } else {
      Thread.sleep(1000)
      println("-----------------------------------------")
      if (dealer.maxValue > 21){
        println("Dealer busts with " + dealer.trueHandTotal + "! You win!")
        winHandler(true)
      } else if (player.maxValue > 21){
        println("Player busts with " + player.trueHandTotal + "!")
        winHandler(false)
      }
    }
    endGame
  }

 /**
  * Hallitsee vedonlyönnin ja pelimerkkien käsittelyn. Tulostaa pelitapahtumia konsoliin.
  * @.pre  (player.chips != null && player.chips >= 0) & (pot != null && pot >= 0) & betsAreDone != null
  * @.post 	Vedonlyönnin jälkeen: player.chips == player.chips - chipsBet & pot == pot + chipsBet & betsAreDone == true
  *        	Jos poikkeusta ei heitetä: retry == false
  */
  def betHandler {
    var chipsBet = 0
    Thread.sleep(1000)
    println("-----------------------------------------")
    if (player.chips + pot < 5){
      println("You don't seem to have enough chips.")
      println
      var chipsLine = readLine("We're fair here. Would you like some more? ")
      Thread.sleep(1000)
      println("-----------------------------------------")
      if (chipsLine.toLowerCase == "y" || chipsLine.toLowerCase == "yes"){
        player.chips += 100
        println("Here you go. 100 more chips to play with.")
        Thread.sleep(1000)
        println("-----------------------------------------")
        println("You have " + player.chips + " chips in hand now.")
        betHandler
      } else if (chipsLine.toLowerCase == "n" || chipsLine.toLowerCase == "no"){
        endGame
      } else {
        println("Excuse me? Answer 'yes' or 'no'.")
        betHandler
        }
    } else {
      print("How much would you like to bet? ")
      var retry = true
      while (retry) {
        try {
          var readInteger = readInt()
          chipsBet = readInteger
          retry = false
        } catch {
          case e: NumberFormatException => errorHandler(e)
        }
      }
      Thread.sleep(1000)
      println("-----------------------------------------")
      if (chipsBet <= player.chips){
        if (chipsBet + pot < 5) {
          println("Sorry, there needs to be at least 5 chips in the pot!")
          println
          betHandler
        } else {
          player.chips -= chipsBet
          pot += chipsBet
          betsAreDone = true
          println("Pot is now " + pot + ". (You have " + player.chips + " chips left.)")
        }
      } else if (chipsBet > player.chips){
        println("You don't have that many chips. You only have " + player.chips + ".")
        betHandler
      } else {
        println("Excuse me? Use your " + player.chips + " chips to bet.")
        println
        betHandler
      }
    }
  }

 /**
  * Kysymys pelin lopettamiseksi.
  * @.pre		true
  * @.post	true
  */
  def endGame {
    displayChips
    Thread.sleep(1000)
    println("-----------------------------------------")
    var endLine = readLine("Would you still like to play? ")
    if (endLine.toLowerCase == "y" || endLine.toLowerCase == "yes"){
      newGame
    } else if (endLine.toLowerCase == "n" || endLine.toLowerCase == "no"){
      Thread.sleep(1000)
      println
      println("OK. Goodbye!")
      Thread.sleep(1000)
      System.exit(0)
    } else {
      Thread.sleep(1000)
      println("-----------------------------------------")
      println("Excuse me? Answer 'yes' or 'no'.")
      endGame
    }
  }

 /**
  * Hallitsee potin arvon muuttamista voiton/häviön mukaan ja voitonjaon.
  * @.pre		win != null & (pot != null && pot >= 0) & (player.chips != null && player.chips >= 0)
  * @.post	Jos pelaaja voitti: player.chips += pot*2, pot == 0
  * 				Jos pelaaja hävisi: pot == 0
  */
  def winHandler(win: Boolean){
    if (win){
      Thread.sleep(1000)
      println("-----------------------------------------")
      pot *= 2
      println("You won " + pot + " chips!")
      player.chips += pot
      pot = 0
    } else {
      pot = 0
    }
  }

 /**
  * Alustaa uuden pelin tai käden. Jos peli on jo alkanut, jaetaan uusi käsi vanhasta pakasta.
  * @.pre		true
  * @.post	dealerAdvantage == false & playerStands == false & betsAreDone == false
  *    			Kun metodia kutsutaan ensimmäisen kerran: firstGame == false & gamedeck.length == 52
  * 				Pakka sekoitettu.
  */
  def newGame {
    dealerAdvantage = false
    playerStands = false
    betsAreDone = false
    if (firstGame){
      gameDeck.newDeck
      gameDeck.shuffleDeck
      firstGame = false
      displayChips
    }
    betHandler
    dealCards
    gameIteration
  }

 /**
  * Jakaa aloituskortit ja esittää ne konsoliin.
  * @.pre		gameDeck != null
  * @.post	true
  */
  def dealCards {
    dealer.dealNewHand(gameDeck)
    player.dealNewHand(gameDeck)
    evaluateHands
    displayHandScore
  }

 /**
  * Poikkeusten hallinta. (Tai poikkeuksen.)
  * @.pre		e != null
  * @.post	true
  */
  def errorHandler(e: Exception){
    println("-----------------------------------------")
    println("Exception caught: " + e)
    println("Please make sure you input the correct type!")
    println("-----------------------------------------")
    Thread.sleep(1000)
    print("How much would you like to bet? ")
  }
}


class Card(suit: String, value: Int){
  private var cardSuit: String = suit
  private var cardValue: Int = value

 /**
	* Getteri kortin maalle.
  * @.pre		cardSuit != null
  * @.post	RESULT == (cardSuit)
  */
  def getSuit = cardSuit

 /**
  * Getteri kortin arvolle.
  * @.pre		cardValue != null
  * @.post	RESULT == (cardValue)
  */
  def getValue = cardValue

 /**
  * toString-metodi, jotta kortit esitetään komentorivillä oikein.
  * @.pre		true
  * @.post	true
  */
  override def toString: String = valueToName(cardValue) + " of " + cardSuit

 /**
  * Kortin arvo (taikka tässä tapauksessa indeksi) muutetaan tekstinä esitettävään muotoon.
  * @.pre		value != null
  * @.post	RESULT == (kortin arvo String-muodossa)
  */
  def valueToName(value: Int): String = value match {
    case 1 => "Ace"
    case 11 => "Jack"
    case 12 => "Queen"
    case 13 => "King"
    case _ => value.toString
  }
}


class Deck {
  private var cardsInDeck = new ArrayBuffer[Card]
  private var discardedCards = new ArrayBuffer[Card]

 /**
  * Uuden pakan alustus.
  * @.pre		cardsInDeck.length == 0
  * @.post 	cardsInDeck.length == 52
  * 				Pakan kaikki 52 korttia alustettu.
  */
  def newDeck {
    for (i <- 1 to 13){
      cardsInDeck += new Card("Hearts", i)
      cardsInDeck += new Card("Diamonds", i)
      cardsInDeck += new Card("Clubs", i)
      cardsInDeck += new Card("Spades", i)
    }
  }

 /**
  * Metodi yhden kortin ottamiseksi pakasta. Jos pakasta ei saa enää korttia, kutsuu metodia
  * uuden pakan sekoittamiseksi poisheitetyistä korteista.
  * @.pre		cardsInDeck != null & discardedCards != null
  * @.post 	discardedCards(discardedCards.length-1) == cardTaken
  * 				cardsInDeck(0) != cardTaken
  * 				RESULT == (cardsInDeck(0))
  */
  def takeOneCard: Card = {
    if (cardsInDeck.length > 0){
      var cardTaken = cardsInDeck(0)
      this.discardedCards.append(cardTaken)
      this.cardsInDeck.remove(0)
      return cardTaken
    } else {
      deckFromDiscarded
      takeOneCard
    }
  }

 /**
  * Sekoittaa uuden pakan poisheitetyistä korteista.
  * @.pre		cardsInDeck != null & discardedCards != null
  * @.post	discardedCards.length == 0
  * 				Uusi pakka sekoitettu.
  */
  def deckFromDiscarded {
    println("Oops! Ran out of cards. New deck shuffled.")
    println("-----------------------------------------")
    cardsInDeck = discardedCards.clone
    for (i <- 0 until discardedCards.length){
      discardedCards.remove(0)
    }
    shuffleDeck
  }

 /**
  * Pakansekoitus. Luodaan kaksi satunnaisgeneraattoria, jotka vaihtavat kahden
  * satunnaisen kortin paikkaa 7*52 kertaa (täydellä pakalla).
  * @.pre		cardsInDeck != null && cardsInDeck.length > 0
  * @.post 	Pakka sekoitettu.
  */
  def shuffleDeck {
    val rnd1 = new Random
    val rnd2 = new Random
    for (i <- 0 until (cardsInDeck.length*7)){
      var r1 = rnd1.nextInt(cardsInDeck.length)
      var r2 = rnd2.nextInt(cardsInDeck.length)
      var temp: ArrayBuffer[Card] = cardsInDeck.clone
      cardsInDeck(r1) = cardsInDeck(r2)
      cardsInDeck(r2) = temp(r1)
    }
  }
}


abstract class Participant {
  private var hand = new ArrayBuffer[Card]
  var aceInGame = false
  var aceValueRedacted = false

  var handTotal = 0
  var handTotalMinor = 0
  var trueHandTotal = 0
  var displayCase = 0

 /**
  * Getteri käden palauttamista varten.
  * @.pre 	hand != null
  * @.post 	RESULT == (käden kortit)
  */
  def getHand = hand

 /**
  * Uuden käden jakaminen. Poistaa myös vanhan käden ja alustaa kokonaispisteet ja Boolean-arvot.
  * @.pre		deck != null & hand != null
  * @.post	hand.length == 2 & handTotal == 0 & handTotalMinor == 0 & trueHandTotal == 0
  * 				aceInGame == false & aceValueRedacted == false
  */
  def dealNewHand(deck: Deck){
    for (i <- 0 until hand.length){
      this.hand.remove(0)
    }
    handTotal = 0
    handTotalMinor = 0
    trueHandTotal = 0
    aceInGame = false
    aceValueRedacted = false
    takeNewCard(deck)
    takeNewCard(deck)
  }

 /**
  * Ottaa yhden kortin pakasta, laskee sen arvon kokonaispistemäärään ja lisää sen käteen.
  * @.pre		deck != null & hand != null
  * @.post	deck.length -= 1 & hand.length += 1
  */
  def takeNewCard(deck: Deck){
    var newCard = deck.takeOneCard
    addToTotal(valueToHandTotal(newCard.getValue))
    hand += newCard
  }

 /**
  * Funktio oikean maksimiarvon päättelemiseksi.
  * @.pre		this.aceInGame != null & this.displayCase != null & this.handTotal != null &
  * 				this.handTotalMinor != null & this.trueHandTotal != null
  * @.post	this.displayCase == (1 || 2 || 3)
  * 				RESULT == (this.trueHandTotal)
  */
   def maxValue(): Int = {
    if (this.aceInGame && this.handTotalMinor < this.handTotal && this.handTotal != 21){
      if (this.handTotal > 21) {
        this.trueHandTotal = this.handTotalMinor
        this.displayCase = 1
      } else {
        this.trueHandTotal = this.handTotal
        this.displayCase = 3
      }
    } else {
      this.trueHandTotal = this.handTotal
      this.displayCase = 2
    }
    this.trueHandTotal
  }

 /**
  * Lisää kokonaislukuarvon kokonaispistemäärään.
  * @.pre		valueToAdd != null & handTotal != null & handTotalMinor != null & aceInGame != null &
  * 				aceValueRedacted != null
  * @.post	handTotal += valueToAdd & (handTotalMinor += valueToAdd || handTotalMinor += (valueToAdd - 10))
  * 				Muuttuja aceValueRedacted muuttuu true:ksi, jos ässä on pelissä (aceInGame == true).
  */
  def addToTotal(valueToAdd: Int) {
      handTotal += valueToAdd
      handTotalMinor += valueToAdd
      if (aceInGame && !aceValueRedacted){
        handTotalMinor -= 10
        aceValueRedacted = true
      }
  }



 /**
  * Palauttaa oikean kokonaislukuarvon kortin indeksiin (luokan Card cardValue) verrattuna.
  * @.pre		value != null
  * @.post	RESULT == (1 <= n <= 11)
  */
  def valueToHandTotal(value: Int): Int = value match {
    case 1 => aceValue
    case 11 => 10
    case 12 => 10
    case 13 => 10
    case _ => value
  }

 /**
  * Palauttaa oikean ässän arvon. Jos käden kokonaispistemäärä ylittää 21 ässän ollessa 11 pisteen
  * arvoinen, ässä on vain 1 pisteen arvoinen.
  * @.pre		handTotal != null & aceInGame != null
  * @.post	aceInGame == true
  * 				RESULT == (1, jos ässä on jo pelissä tai pisteet ylittäisivät 21 ässän ollessa 11; muuten 11)
  */
  def aceValue: Int = {
    if (handTotal > 21 || aceInGame == true){
      aceInGame = true
      1
    } else {
      aceInGame = true
      11
    }
  }
}


class Dealer extends Participant


class Player extends Participant {
  var chips: Int = 100
}
