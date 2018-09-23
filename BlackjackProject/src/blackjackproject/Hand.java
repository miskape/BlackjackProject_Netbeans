/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjackproject;
import java.util.ArrayList;

/**
 *
 * @author miskape
 */
public class Hand {
    
    private Deck carddeck;
    private ArrayList<Card> playerhand;         // instanttimuuttuja playerhand on nimetty hieman sekavasti playerhand:iksi
    
    public Hand() {                             // nämä Hand luokan metodit tehtiin ihan alkuvaiheessa kun suunnittelimme aluksi tekevämme pääohjelman ilman controller-oliota.
        this.playerhand = new ArrayList<>();    // Hand luokan constructori, ON TÄRKEÄÄ HUOMATA Hand-oliot ovat ArrayListejä jotka sisältävät Card-olioita
    }

    public Hand(Deck carddeck) {        // hand tuntee deckin, elikkä pelipakan josta kortit jaetaan dealeriller ja pelaajalle.
        this.carddeck = carddeck;
        this.playerhand = new ArrayList<>();
    }

   public Card getCardFromMainHand(){      // työn alla
       Card firstCard = this.playerhand.get(0);
       this.playerhand.remove(firstCard);
       return firstCard;
   }
   
   public void putCardIntoSidehand(Card firstCard){     /// työn alla 
       this.playerhand.add(firstCard);
   }
   
   public void makeMeASidehand( Hand mainhand ){        //// työn alla  esimerkki.   sidehand.makeMeASidehand(mainhand)
       Card firstCard = mainhand.getCardFromMainHand();
       this.playerhand.add(firstCard);
   }
   
   
    
    public void hitPlayerCard() {       // nostaa yhden kortin playingdeckistä
        playerhand.add(carddeck.getCard(0));
        carddeck.removeCard(0);

    }
    
    public int dealerHitLoop(Hand dealerhand){      // käytä tätä vain kun syötät parametriksi dealerin handin MUUTEN METODI EI OLE BLACKJACK SÄÄNTÖJEN mukaan
      while(dealerhand.handValueOf() <17){
          dealerhand.hitPlayerCard();
          int dealerValue= dealerhand.handValueOf();
          if (dealerValue >= 17) {
              System.out.println(dealerhand.toString());
              System.out.println("dealerhand is valued at " + dealerhand.handValueOf());
              return dealerValue;
          }
          System.out.println(dealerhand.toString());
          System.out.println("dealerhand is valued at " + dealerhand.handValueOf());
      }
      return dealerhand.handValueOf();
    }
    
    public int getAmountOfCards(){
        int cardsAmount= playerhand.size();
        return cardsAmount;
    }   

    public void clearHand(){        //tyhjentää Handin blackjack rundin jälkeen
        playerhand.clear();
    }
    
    
    public void dealStartingCards(){            // jakaa alkukortit
        for (int j=0; j<=1; j++){
            playerhand.add(carddeck.getCard(0));
            carddeck.removeCard(0);
        }
    }
    
    public String revealOnlyOneCard(){      // tämä metodi liittyy ihan alkutilanteeseen dealerin kortteihin, kun on jaettu 2 ja 2 korttia dealerille ja pelaajalle
        String revealedCard;                // dealer paljastaa vain yhden omista korteistaan pelaajalle tässä vaiheessa. HUOM kaikki alkukortit on kuitenkin jaettu jo, mutta 
        revealedCard= " - " + playerhand.get(0).toString();     //  sitä toista dealerin korttia ei vain printata näkyviin vielä
        return revealedCard;
    }
    
    public boolean doesPlayerHaveSameStartingCards(){           // metodi liitty splittaukseeen muistaakseni
        if (  playerhand.get(0).getValue() == playerhand.get(1).getValue()   ){
            return true;
        }
        else {
            return false;
        }
    }
    public boolean isOpenCardAce(){         // metodi liitty insuranceBetin tarjoamiseen
        Value maa= playerhand.get(0).getValue();
        if (maa==Value.ACE)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        String handOutput = "";
        for (Card aCard : playerhand) {
            handOutput = handOutput + " - "+ aCard.toString();
        }
        return handOutput;
    }
    
    
    
    
    
    public int handValueOf(){       // tämä on uusi metodi jota testataan laskemaan ässien arvot oikein. /// ei muuten ollu iha helppo tehdä mutta tulipahan tehtyä toi ässien uudelleen laskeminen tarpeen mukaan
         int handtotalValue=0, acesAmount=0;
        for (Card theCard : playerhand){
            switch (theCard.getValue()){
                case TWO: handtotalValue += 2; break;
                case THREE: handtotalValue += 3; break;
                case FOUR: handtotalValue += 4; break;
                case FIVE   : handtotalValue += 5; break;
                case SIX: handtotalValue += 6; break;
                case SEVEN: handtotalValue += 7; break;
                case EIGHT: handtotalValue += 8; break;
                case NINE: handtotalValue += 9; break;
                case TEN: handtotalValue += 10; break;
                case JACK: handtotalValue += 10; break;
                case QUEEN: handtotalValue += 10; break;
                case KING: handtotalValue += 10; break;
                case ACE: acesAmount +=1; handtotalValue +=11; break;
            }
        }
        int j=1;
        while (handtotalValue>21 && j<=acesAmount){
                handtotalValue=handtotalValue-10;
                j++;
        }
        return handtotalValue;
    }
    
    
    
      public int getRunningCountFromSingleHand(){
          
          int runningcount=0;
          for (Card aCard : playerhand){
              switch(aCard.getValue()){
                case TWO: runningcount +=1; break;
                case THREE:  runningcount +=1; break;
                case FOUR: runningcount +=1; break;
                case FIVE: runningcount +=1; break;
                case SIX: runningcount +=1; break;
                case SEVEN: runningcount +=0; break;
                case EIGHT: runningcount +=0; break;
                case NINE: runningcount +=0; break;
                case TEN: runningcount -= 1; break;
                case JACK: runningcount -= 1; break;
                case QUEEN: runningcount -= 1; break;
                case KING: runningcount -= 1; break;
                case ACE: runningcount -= 1; break;
              }
          }
          return runningcount;
      }
      
      public int getRunningCountFromTwoHands(Hand somePlayerHand, Hand dealerHand){
          int totalRunningCount=0;
          totalRunningCount= somePlayerHand.getRunningCountFromSingleHand()+dealerHand.getRunningCountFromSingleHand();
          return totalRunningCount;
      }
      
      public Value getDealerOpenCardValue(){
        Value rank = playerhand.get(0).getValue();
        return rank;
    }
      
      
//      public void basicStrategyBlackjack(){
//          
//      }
      
     }
    
    

