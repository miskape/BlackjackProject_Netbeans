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
public class Bankaccount {
    
    private double moneyAmount;
    
    public Bankaccount(double alkupääoma){
        moneyAmount= alkupääoma;
    }
    
    public double getMoneyAmount(){
        return moneyAmount;
    }
    
    public void setMoneyAmount(double moneyAmount){
        this.moneyAmount=moneyAmount;
    }
    public void increaseMoneyAmount(double lisäysrahat){
        moneyAmount= moneyAmount + lisäysrahat;
    }
    
    public boolean reduceMoneyAmount(double vähennysrahat){
        
        if (vähennysrahat <= moneyAmount){
            moneyAmount -= vähennysrahat;
            return true;
        }
        else {
            return false;
        }
        
    }
}
