/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjackproject;

/**
 *
 * @author miskape
 */
public class Card {
    
    private final Suit suit;
    private final Value value;
    
    public Card(Suit suit, Value value){
        this.value= value;
        this.suit= suit;
    }

    public Suit getSuit() {
        return suit;
    }

    public Value getValue() {
        return value;
    }
    
    @Override
    public String toString(){
        return value.toString() + " of " +suit.toString() ;
    }
    
    public String getValueAsString(){
        return value.toString();
    }
    
}
