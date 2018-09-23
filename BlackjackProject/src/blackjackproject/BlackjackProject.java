
package blackjackproject;


public class BlackjackProject {     // <----------CONTROLLERIN METODIT LÖYTYY TÄÄLTÄ ("Controller classi" = BlackjackProject class)
    
    private final View theView;
    private  Hand playerhand;
    private Hand dealerhand;
    private Hand sidehand;
    private Bankaccount playeraccount;
    private Bankaccount dealeraccount;
    private Hand cardcount;
    
    public BlackjackProject(View theView){      //CONTROLLERIN CONSTRUCTOR controller tuntee view:in
        this.theView= theView;
    }
    
    public boolean userWillingnessToPlay(){         // kysyy haluaako player jatkaa vai poistua pelistä
        return theView.askIfKeepPlaying();
    }
    
    public double whatIsBetSize(){          // kysyy alkupanoksen suuruutta playeriltä
        return theView.askBetSize();
    }
    
    public void setBankaccounts(Bankaccount playeraccount, Bankaccount dealeraccount){          // asettaa bankaccoiunts oliot
        this.playeraccount=playeraccount;
        this.dealeraccount=dealeraccount;
    }
    
    
    public boolean playerBettingSimplified(double betSize){         // parannelty metodi playerin bettaamista varten alussa elikkä alkupanos HUOM EI VIELÄ TESTATTU
        if (playeraccount.reduceMoneyAmount(betSize)){
            return true;
        }
        else{
            theView.rudeBehaviourMessage();
            return false;
        }
    }
    
    public boolean playerBetting(double bettingMoney, Bankaccount bettingAccount, Bankaccount thePot){          // wanha metodi liittyi playerin alkupanokseen, ei muistaakseni enää käytetä
        if (bettingAccount.reduceMoneyAmount(bettingMoney)){
            thePot.increaseMoneyAmount(bettingMoney);
            return true;
        }
        else{
            theView.rudeBehaviourMessage();
            return false;
        }
    }
    
    public int playingDeckGenerateShuffle(Deck thePlayingDeck){         // tärkeä metodi jolla pelipakkaan generoidaan aluksi tarvittavat korttipakat (n*52cardia) jossa n=luonnollinen luku ja sitten samalla sekoitetaan pelipakka
        int numberOfDecks= theView.askHowManyDecksToUse();
        thePlayingDeck.createPlayingDeck(numberOfDecks);
        thePlayingDeck.collectionsShuffleList();
        return numberOfDecks;
    }
   
    public void setHandsToController(Hand playerhand, Hand dealerhand, Hand cardcount){     // asetetaan playerhand ja dealerhand oliiot
        this.dealerhand=dealerhand;
        this.playerhand=playerhand;
        this.cardcount=cardcount;
    }
    
    public void setSidehandToController(Hand sidehand){     //asetetaan sidehand olio
        this.sidehand=sidehand;
    }
    
    public void createSideHandWithTwoCards(){
        this.sidehand.makeMeASidehand(playerhand);      // controlleri tuntee sidehandin sekä playerhandin. Sitten sidehandiin nostetaan normalaisti toinne kortti pelipakasta
        sidehand.hitPlayerCard();
    }
    

    public void dealTwoStartingCards(){
        playerhand.dealStartingCards();
        dealerhand.dealStartingCards();
        String revealedDealerCard= dealerhand.revealOnlyOneCard();
        theView.printString("Dealer's starting cards are ");
        theView.printString(revealedDealerCard + " - " + "UNKNOWN");
        theView.printString("Player's starting cards are ");
        theView.printString(playerhand.toString());
    }
    
    public int getPlayerHandValue(){
        return playerhand.handValueOf();
    }
    
    public int getDealerHandValue(){
        return dealerhand.handValueOf();
    }
    
    public boolean insuranceBetOffering(){
        if (dealerhand.isOpenCardAce()){
            if(theView.askInsuranceBet()){
                return true;
            }
            else
                return false;
        }
        else
            return false; 
    }
    public int evaluateInsuranceBet(double insuranceBet){
        if (!playeraccount.reduceMoneyAmount(insuranceBet)){
                       System.out.println("");
                       System.out.println("youure broke, please leave the casino!!" );
                       return 0;
                }
        else{
                System.out.println("");
                System.out.println("you offered the insurancebet valued at " + insuranceBet +", your accountsaldo is " +playeraccount.getMoneyAmount());
                
                if(dealerhand.handValueOf()==21){
                    return 2;
                }
                else {

                    return 1;
                }
            }
    }
    
    public boolean playerBettingSideBet(double sideBet){
        
        if (playeraccount.reduceMoneyAmount(sideBet)){
            
            return true;
        }
        else{
            theView.rudeBehaviourMessage();
            return false;
        }
    }
   
    public boolean playerSideBetting(double sideBet){
        if (playeraccount.reduceMoneyAmount(sideBet)){
//            pot.increaseMoneyAmount(sideBet);
            return true;
        }
        else{
            System.out.println("YOU'RE BROKE!! GET OUTTA HERE! leave the casino, you cant afford this game! ");
            return false;
        }
    }
    
    public boolean splitOffering(){         // uusi testimetodi joka on näppärämpi boolean metodi splittauksen päättämiseen EI VIELÄ TESTATTU KUNNOLLA /// PÄIVITYS ei vissi bugeja viela
        if (playerhand.doesPlayerHaveSameStartingCards()==true ){
            if(theView.askSplit()==true){
                return true;
            }
            else
                return false;
        }
        else{
            return false;
        }
    }
    
    public boolean canPlayerSplit(){            // JOKU  METODI
        if ( playerhand.doesPlayerHaveSameStartingCards() ){
            return true;
        }
        else
            return false;
    }
    
    public int playerDecisionsRegular(){
        return theView.askPlayerDecisionsRegular();

    }
    
    
    
    public void showPlayerHand(){
        theView.printString("the player has: ");
        theView.printString(playerhand.toString());
        theView.printString("player hand is valued at "+playerhand.handValueOf() );         // tämä oli tuore lisäys koodiin TÄTÄ EI OLE TESTATTU MERKITTÄVÄSTI VIELÄ /// tätä metodia nyt testattiin jonkin verran ja vaikuttaa toimivan OK, metodin sisällöt on otettu parempaan hhyötykäyttöön joissakin controller.evaluate yms metodeissa
    }
    public void showDealerHand(){
        theView.printString("the dealer has: ");
        theView.printString(dealerhand.toString());
        theView.printString("dealerhand is valued at " + dealerhand.handValueOf() );        // tämä oli tuore lisäys koodiin TÄTÄ EI OLE TESTATTU MERKITTÄVÄSTI VIELÄ /// tätä metodia nyt testattiin jonkin verran ja vaikuttaa toimivan OK, metodin sisällöt on otettu parempaan hhyötykäyttöön joissakin controller.evaluate yms metodeissa
    }
    
    public void showSideHand(){
        theView.printString("the player has: ");
        theView.printString(sidehand.toString());
        theView.printString("player hand is valued at " + sidehand.handValueOf());
    }
    public void dealerHitCard(){
         dealerhand.hitPlayerCard();
         dealerhand.handValueOf();      // paluuarvoa ei hyödynnetä tässä käsi.handValueOf() MUTTA JOKAISEN HITTAUKSEN JÄLKEEN jälkeen pitää tarkistaa uusi pelikorttien tilanne ässien arvon uudelleen laskemista varten.
         theView.printString(dealerhand.toString());
         System.out.println("dealerhand is valued at "+dealerhand.handValueOf());
    }
    
    public void playerHitCard(){        // metodia on editoidu MUTTA EI VIELÄ testattu että laskeeko se uudelleen ässien arvot oikein!! /// metodi vaikuttaisi laskevan oikein pläyerin handvaluen sekä ässien arvot oikein tilanteen mukaan 11 tai 1
        playerhand.hitPlayerCard();
        playerhand.handValueOf();
        theView.printString(playerhand.toString());
        System.out.println("playerhand is valued at "+playerhand.handValueOf());
    }
    
    public void clearBothHands(){           // metodia käytttiin kun yksi rundi pelattiin loppuun, niin hand-oliot pitää tyhjentää korteistaan.
        dealerhand.clearHand();
        playerhand.clearHand();
    }
    
    public void clearAllHands(){
        dealerhand.clearHand();
        playerhand.clearHand();
        sidehand.clearHand();
    }
    
    public int checkRemainingCardsInDeck(Deck playingdeck){         // tärkeaä metodi!!! estää korttien loppumisen playingdeckistä, metodi liittyy juuurikin siihen että kun playingdeckin cardit on tarpeeksi "alhaalla" lukumäärässä niin rundin jälkeen VIIMEISEKSI OPERAATIOKSI(??) tarkastataan riittävätkö kortit jne. jne...
        int remainingcards = playingdeck.checkCardsAmountInDeck();
        return remainingcards;
    }
    
    public void restartDeckShuffle(int amountOfDecks, Deck playingdeck){        // metodi liittyy korttien loppumisen playingdeckistä ja uudelleen sekoittamiseen ja playingdeckin "restarttaamiseen" 
        theView.printString("There aren't enough cards in the shoe, we will shuffle the deck(s) again now. ");
        int number= amountOfDecks;
        playingdeck.createPlayingDeck(number);
        playingdeck.shuffleArrayList();
    }
    
    public int evaluateNaturalBlackJack(){          // metodi tarkastaa että JOS PELAAJA sai ALUSSA ace + ten = 21 alkukorteissaan, niin sitten mennään tänne tarkistamaan että tuliko suoraan voitto playerille, vai tasapeli playerille.
        if (playerhand.handValueOf()==21){
            if (playerhand.handValueOf()==21 && dealerhand.handValueOf()==21){
                theView.printString("both have natural blackjack, the game is a draw  ");
                return 1;
            }
            else {
                theView.printString("player has natural blackjack, the player wins ");
                return 2;
            }
        }
        return 0;
    }
    
    public int evaluateNaturalBlackJackParameters(Hand diilerinkäsi, Hand pelaajankäsi){        // parametrillinen evaluateNaturalBlackJack metodi, liittyy muistaakseni sidegamessa tehtyihin tarkastuksiin, koska tulee uusi mahdollisuus saada natural blackjack
        if (pelaajankäsi.handValueOf()==21){
            if (pelaajankäsi.handValueOf()==21 && diilerinkäsi.handValueOf()==21){
                theView.printPlayerHandValue(pelaajankäsi);
                return 1;
            }
            else{

                theView.printPlayerHandValue(pelaajankäsi);

                return 2;
            }
        }
        else{
            return 0;
        }
        
        
        
    }
    public int runningcountRegulargame(){
        int count= playerhand.getRunningCountFromSingleHand();
        count = count + dealerhand.getRunningCountFromSingleHand();
        return count;
    }
    
    public int runningcountSplitBranch(Hand jakajankäsi, Hand sivukäsi, Hand pääkäsi){
        int count = jakajankäsi.getRunningCountFromSingleHand();
        count = count + sivukäsi.getRunningCountFromSingleHand();
        count = count + pääkäsi.getRunningCountFromSingleHand();
        return count;
    }
        
    
    public int getSidegamePlayerValue(){
        return sidehand.handValueOf();
    }
    public int getSidegameDealerValue(){
        return dealerhand.handValueOf();
    }
    
    public int evaluateHitSidegame(Hand diilerhänd, Hand playerinkäsi){     // metodi liittyy sidegameen
        theView.printPlayerHandValue(playerinkäsi);
        boolean playerKeepHitting = true;
        System.out.println("the player starts hitting ");

        while (playerKeepHitting == true) {       // player keeps hitting until its illegal or until he stops hitting and therefore he actually stands
            playerinkäsi.hitPlayerCard();
            playerinkäsi.handValueOf();     // metodikutsu on tarpeellinen koska tämä metodi evaluoi ässien arvon uudelleen jokaisen pakasta vedetyn kortin jälkeen
            theView.printString(playerinkäsi.toString());
            System.out.println("playerhand is valued at " + playerinkäsi.handValueOf());
            if (playerinkäsi.handValueOf() >= 21) {      // break statement seems to be necessary (not sure though) because of method usage (playerHitCard method) /// korjattu lähinnä tuo if lauseen ehto suurempi tai yhtäsuuri kuin 21
                break;
            }
            playerKeepHitting = theView.askPlayerKeepHitting();
        }
        if (diilerhänd.handValueOf()==21){       // tests if dealer has natural blackjack in his starting cards, if dealer has natural, then player loses immediately

            return 0;
        }
        if (playerinkäsi.handValueOf() > 21) {                  // if player busts by going over 21, player  loses the hand
              System.out.println("player busts and dealer wins  the hand ");
            return 0;
        } else {                                           // if the player DID NOT bust by going over 21, we go to this else-branch to evaluate further

            

            while (diilerhänd.handValueOf() < 17 && playerinkäsi.handValueOf() <= 21) {       // dealer keeps hitting until his handvalue is big enough
                diilerhänd.hitPlayerCard();
              if(diilerhänd.handValueOf()>=17){
                break;
              }
            }
            

            int dealerHandSum = diilerhänd.handValueOf();        //  variable for dealervalue temporary usage here...

            if (dealerHandSum > 21 || playerinkäsi.handValueOf() > dealerHandSum) {
                return 2;
            } else if (playerinkäsi.handValueOf() == diilerhänd.handValueOf()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    
    public int evaluateDoubleDownSidegame() {
        
        theView.printPlayerHandValue(sidehand);
        System.out.println("the player doublesdown");
        sidehand.hitPlayerCard();
        sidehand.handValueOf();
        theView.printPlayerHandValue(sidehand);

        if (dealerhand.handValueOf() == 21) {       // dealer wins
            return 0;   
        }

        if (sidehand.handValueOf() > 21) {      // dealer wins
            System.out.println("the Player busts, dealer wins the hand ");
            return 0; //player loses
        } else {
            while (dealerhand.handValueOf() < 17 && sidehand.handValueOf() <= 21) {       // dealer keeps hitting until his handvalue is big enough
                dealerhand.hitPlayerCard();
                if (dealerhand.handValueOf() >= 17) {
                    break;
                }

            }
            if (dealerhand.handValueOf() > 21 || sidehand.handValueOf() > dealerhand.handValueOf()) {    // player wins
                return 2;
            } else if (sidehand.handValueOf() == dealerhand.handValueOf()) {     // hand is draw
                return 1;
            } else {          // dealer wins
                return 0;
            }
        }
    }
    
    public int evaluateStandSidegame(){     // returnaa sidehandin handvaluen paluuarvona JOS returnvalue==0, niin playeri hävisi sidegamen dealeria vastaan jo alkuvaiheessa
        /*
        HUOM TÄSTÄ METODISTA
        tämä metodi evaluoi lopputuloksen blackjack pelille ja palauttaa sen retutn valuena integerinä
        lisäksi tämä metodi generoi evaluoi että hittaako dealeri, vai ständääkö dealeri
        metodi ei printtaa mitään lisäinformaatiota blackjackin pelaajalle, saadusta sidegamen tuloksesta, kuten kuuluukin olla
        */
        
        
        theView.printPlayerHandValue(sidehand);
        
        // player chooses to STAND
        
            System.out.println("player stands in the sidegame ");
            int sidehandValue=sidehand.handValueOf();           // playerin sidehandValue voidaan ottaa heti talteen yhteen muuttujaan alkuvaiheesa koska, se ei tule muuttumaan koska player stands
            int dealerhandStartValue= dealerhand.handValueOf();
            
            if (dealerhandStartValue ==21 ){          // tarkistetaan aluksi natural blackjackit  dealeriltä, jos dealerblackjack ==>  autolose playerille
                return 0;
            }
            
            int dealerValue= dealerhand.handValueOf();      //  arvo otetaan talteen muuttujaan esim jos dealerin kortit olisivat olleet 10 + 8 = 18 jolloin while looppiin EI EDES MENTÄISI...
            
            while (dealerhand.handValueOf() < 17) {     // whileä käytetty tässä ikäänkuin sekä valinta ominaisuudessaan että toisto ominaisuudessaan
            dealerhand.hitPlayerCard();
            if (dealerhand.handValueOf() >= 17) {
                dealerValue = dealerhand.handValueOf();
                break;
            }
            
            }
    

                if (  sidehandValue > dealerValue || dealerValue >21 ){         // näillä ehdoilla playerin pitäisi voittaa
                    return 2;
                }
                else if( sidehandValue == dealerValue ){            // näillä ehdoilla pitäisi tulla tasapeli
                    return 1;
                }
                
                else{           // tässä dealeri voittaa
                    return 0;
                }
            
    }

    public int maingameStandEvaluate(){
        
        
        // player chooses to STAND
        
            System.out.println("player stands in the maingame ");
            theView.printPlayerHandValue(playerhand);
            int mainhandValue=playerhand.handValueOf();           // playerin sidehandValue voidaan ottaa heti talteen yhteen muuttujaan alkuvaiheesa koska, se ei tule muuttumaan koska player stands
            int dealerhandStartValue= dealerhand.handValueOf();
            
            if (dealerhandStartValue ==21 ){          // tarkistetaan aluksi natural blackjackit  dealeriltä, jos dealerblackjack ==>  autolose playerille
                return 0;
            }
            
            int dealerValue= dealerhand.handValueOf();      //  arvo otetaan talteen muuttujaan esim jos dealerin kortit olisivat olleet 10 + 8 = 18 jolloin while looppiin EI EDES MENTÄISI...
            
            while (dealerhand.handValueOf() < 17) {     // whileä käytetty tässä ikäänkuin sekä valinta ominaisuudessaan että toisto ominaisuudessaan
            dealerhand.hitPlayerCard();
            if (dealerhand.handValueOf() >= 17) {
                dealerValue = dealerhand.handValueOf();
                break;
            }
            
        
            }
    

                if (  mainhandValue > dealerValue || dealerValue >21 ){         // näillä ehdoilla playerin pitäisi voittaa

                    return 2;
                }
                else if( mainhandValue == dealerValue ){            // näillä ehdoilla pitäisi tulla tasapeli
                    return 1;
                }
                
                else{           // tässä dealeri voittaa
                    return 0;
                }
            
    }
    
    public int maingameHitEvaluate(){
         theView.printPlayerHandValue(playerhand);
        boolean playerKeepHitting = true;
        System.out.println("the player starts hitting ");

        while (playerKeepHitting == true) {       // player keeps hitting until its illegal or until he stops hitting and therefore he actually stands
            playerhand.hitPlayerCard();
            playerhand.handValueOf();     // metodikutsu on tarpeellinen koska tämä metodi evaluoi ässien arvon uudelleen jokaisen pakasta vedetyn kortin jälkeen
            theView.printString(playerhand.toString());
            System.out.println("playerhand is valued at " + playerhand.handValueOf());
            if (playerhand.handValueOf() >= 21) {      // break statement seems to be necessary (not sure though) because of method usage (playerHitCard method) /// korjattu lähinnä tuo if lauseen ehto suurempi tai yhtäsuuri kuin 21
                break;
            }
            playerKeepHitting = theView.askPlayerKeepHitting();
        }
        if (dealerhand.handValueOf()==21){       // tests if dealer has natural blackjack in his starting cards, if dealer has natural, then player loses immediately
            return 0;
        }
        if (playerhand.handValueOf() > 21) {                  // if player busts by going over 21, player  loses the hand
              System.out.println("player busts and dealer wins  the hand ");
            return 0;
        } else {                                           // if the player DID NOT bust by going over 21, we go to this else-branch to evaluate further
            

            while (dealerhand.handValueOf() < 17 && playerhand.handValueOf() <= 21) {       // dealer keeps hitting until his handvalue is big enough
                dealerhand.hitPlayerCard();
              if(dealerhand.handValueOf()>=17){
                break;
              }
            }
            

            int dealerHandSum = dealerhand.handValueOf();        //  variable for dealervalue temporary usage here...
            int playerHandSum = playerhand.handValueOf();
            if (dealerHandSum > 21 || playerHandSum > dealerHandSum) {
                return 2;
            } else if (playerHandSum == dealerHandSum  ) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    
    public int maingameDoubleDownEvaluate(){
        theView.printPlayerHandValue(playerhand); 
        System.out.println("the player doublesdown");
        playerhand.hitPlayerCard();
        int playerValue= playerhand.handValueOf();
        theView.printPlayerHandValue(playerhand);
        
        int dealerStartValue= dealerhand.handValueOf();
        
        if (dealerStartValue == 21) {       // dealer wins
            return 0;   
        }

        if (playerValue > 21) {      // dealer wins
            System.out.println("the Player busts, dealer wins the hand ");
            return 0; //player loses
        } 
        else {
        while (dealerhand.handValueOf() < 17 && playerhand.handValueOf() <= 21) {       // dealer keeps hitting until his handvalue is big enough
            dealerhand.hitPlayerCard();
            if (dealerhand.handValueOf() >= 17) {
                int dealerValue=dealerhand.handValueOf();
                break;
            }

        }
            if (dealerhand.handValueOf() > 21 || playerhand.handValueOf() > dealerhand.handValueOf()) {    // player wins
                return 2;
            } 
            else if (playerhand.handValueOf() == dealerhand.handValueOf()) {     // hand is draw
                return 1;
            } 
            else {          // dealer wins
                return 0;
            }
        }
    }
    
    public int playerStandEvaluate(){           // tämä isohko metodi evaluoi tulokset mitä tapahtui kun pelaaja ständäsi alkuperäisillä alkukorteillaan, returnvalue on integer ja muistaakseni 2==voitto 1==tappio 0==tasapeli
        
        System.out.println("");
        System.out.println("player stands");
        theView.printPlayerHandValue(playerhand);

        if (dealerhand.handValueOf()==21){       // tests if dealer has natural blackjack iin his starting cards
            theView.printString("the dealer has natural blackjack, the dealer wins: ");
            theView.printString(dealerhand.toString());
//            dealerhand.clearHand();
//            playerhand.clearHand();
            return 0;
            }

        if(dealerhand.handValueOf()>=17){
            theView.printDealerHandValue(dealerhand);
            System.out.println("the dealer stands ");
        }
        else{
            theView.printDealerHandValue(dealerhand);
            System.out.println("the dealer starts hitting ");
            
        }
                
        while (dealerhand.handValueOf() < 17) {
            dealerhand.hitPlayerCard();
            System.out.println(dealerhand.toString());
            System.out.println("the dealerhand is valued at " + dealerhand.handValueOf());
            if (dealerhand.handValueOf() >= 17) {
                break;
            }
        }

        if (  playerhand.handValueOf()>dealerhand.handValueOf() || dealerhand.handValueOf()>21 ){
                theView.printHandValues(dealerhand, playerhand);
                System.out.println("player wins the hand");
//                dealerhand.clearHand();
//                playerhand.clearHand();
                return 2;
            }
            else if( playerhand.handValueOf() == dealerhand.handValueOf() ){
                theView.printHandValues(dealerhand, playerhand);
                System.out.println("the hand is a draw ");
//                dealerhand.clearHand();
//                playerhand.clearHand();
                return 1;
            }
            else{
                theView.printHandValues(dealerhand, playerhand);
                System.out.println("dealer wins the hand ");
//                dealerhand.clearHand();
//                playerhand.clearHand();
                return 0;
            }
        }
        
    public int playerHitEvaluate() {            // tämä on erittäin iso metodi, ja tämä metodi evaluoi kokonaisuudessaan PLAYER HITS extra cards playerin valinnan, että mitä tapahtui yhden blackjack rundin aikana, tuliko tasapeli, voitto vai tappio. Metodin paluuarvo on integer ja 2== voitto 1 == tasapeli 0== tappio 
//        System.out.println("player hits ");
        theView.printPlayerHandValue(playerhand);
        boolean playerKeepHitting = true;
        System.out.println("the player starts hitting ");

        while (playerKeepHitting == true) {       // player keeps hitting until its illegal or until he stops hitting and therefore he actually stands
            System.out.println("");
            playerhand.hitPlayerCard();
            playerhand.handValueOf();
            theView.printString(playerhand.toString());
            System.out.println("playerhand is valued at " + playerhand.handValueOf());
            if (playerhand.handValueOf() >= 21) {      // break statement seems to be necessary (not sure though) because of method usage (playerHitCard method) /// korjattu suurempi tai yhtässuuuri kuin 21 varmuuden vuoksi näin...
                break;
            }
            playerKeepHitting = theView.askPlayerKeepHitting();
        }
        if (dealerhand.handValueOf()==21){       // tests if dealer has natural blackjack in his starting cards, if dealer has natural, then player loses immediately
            theView.printString("the dealer has natural blackjack, the dealer wins: ");
            theView.printString(dealerhand.toString());
//            dealerhand.clearHand();
//            playerhand.clearHand();
            return 0;
        }
        if (playerhand.handValueOf() > 21) {                  // if player busts by going over 21, player  loses the hand
            theView.printHandValues(dealerhand, playerhand);
            System.out.println("player busts and dealer wins  the hand ");
//            dealerhand.clearHand();
//            playerhand.clearHand();
            return 0;
        } else {                                           // if the player DID NOT bust by going over 21, we go to this else-branch to evaluate further
            if(dealerhand.handValueOf()>=17){
                theView.printDealerHandValue(dealerhand);
                System.out.println("the dealer stands ");
            }
            else{
                theView.printDealerHandValue(dealerhand);
                System.out.println("the dealer starts hitting ");
                
            }
            

            while (dealerhand.handValueOf() < 17 && playerhand.handValueOf() <= 21) {       // dealer keeps hitting until his handvalue is big enough
                dealerhand.hitPlayerCard();
                  theView.printString(dealerhand.toString());
                System.out.println("dealerhand is valued at " + dealerhand.handValueOf());
              if(dealerhand.handValueOf()>=17){
                
                break;
              }
            }

            int dealerHandSum = dealerhand.handValueOf();        //  variable for dealervalue temporary usage here...

            if (dealerHandSum > 21 || playerhand.handValueOf() > dealerHandSum) {
                theView.printHandValues(dealerhand, playerhand);
//                dealerhand.clearHand();
//                playerhand.clearHand();
                return 2;
            } else if (playerhand.handValueOf() == dealerhand.handValueOf()) {
                theView.printHandValues(dealerhand, playerhand);
//                dealerhand.clearHand();
//                playerhand.clearHand();
                return 1;
            } else {
                theView.printHandValues(dealerhand, playerhand);
//                dealerhand.clearHand();
//                playerhand.clearHand();
                return 0;
            }
        }
    }

    public int playerDoubleDownEvaluate(){
        System.out.println("player doubles down ");
        playerhand.hitPlayerCard();
        playerhand.handValueOf();
        theView.printPlayerHandValue(playerhand);
        
        if (dealerhand.handValueOf()==21){       // tests if dealer has natural blackjack iin his starting cards
            theView.printString("the dealer has natural blackjack, the dealer wins: ");
            theView.printString(dealerhand.toString());
//            dealerhand.clearHand();
//            playerhand.clearHand();
            return 0;
        }
                if (playerhand.handValueOf() >21){                  // if player busts by going over 21, player  loses the hand
                    theView.printHandValues(dealerhand, playerhand);
                    System.out.println("player busts and dealer wins  the hand ");
//                    dealerhand.clearHand();
//                    playerhand.clearHand();
                    return 0;
                }
                else{                                           // if the player did not bust by going over 21, we go to this else-branch to evaluate further
                    
                    if (dealerhand.handValueOf() >= 17) {
                        theView.printDealerHandValue(dealerhand);
                        System.out.println("the dealer stands ");
                    } 
                    else {
                        theView.printDealerHandValue(dealerhand);
                        System.out.println("the dealer starts hitting ");
                    }

                    while (dealerhand.handValueOf() < 17 && playerhand.handValueOf() <= 21) {       // dealer keeps hitting until his handvalue is big enough
                            dealerhand.hitPlayerCard();
                            theView.printString(dealerhand.toString());
                            System.out.println("dealerhand is valued at " + dealerhand.handValueOf());

                        if (dealerhand.handValueOf() >= 17) {
                            break;
                        }

                    }
                    
                    if (dealerhand.handValueOf() > 21 || playerhand.handValueOf()> dealerhand.handValueOf()) {
                        theView.printHandValues(dealerhand, playerhand);
                        System.out.println("player wins the hand, PLAYER WINS DOUBLE MONEY!!");
//                        dealerhand.clearHand();
//                        playerhand.clearHand();
                        return 2;
                    }
                    else if ( playerhand.handValueOf()== dealerhand.handValueOf() ) {
                        theView.printHandValues(dealerhand, playerhand);
//                        dealerhand.clearHand();
//                        playerhand.clearHand();
                        return 1;
                    }
                    else {
                        theView.printHandValues(dealerhand, playerhand);
//                        dealerhand.clearHand();
//                        playerhand.clearHand();
                        return 0;
                    }
            } 
             
        
        
        
        
    }
    
    

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {  //////////////////////////   main program begins    /////////////////////////
//        
//        View view= new View();
//        BlackjackProject controller= new BlackjackProject(view);        // controlleri tuntee view:in ja tämä tulee olla constructorissa
//        Bankaccount playeraccount= new Bankaccount(150);
//        Bankaccount dealeraccount= new Bankaccount(150);
//        Bankaccount blackjackPot= new Bankaccount(0);
//        Deck playingdeck= new Deck();                       // normaali pelipakka
//        Deck testingdeck= new Deck();
//        Hand playerhand= new Hand(testingdeck);                                                                                             // playerhand tuntee playingdeck:in, josta nostetaan kortteja handiin itseensä.
//        Hand dealerhand= new Hand(testingdeck);                                                                                                 // myös dealerhand tuntee playingdeck:in josta nostetaan kortteja dealerhandiin itseensä.
//      //  controller.simulate();
//        boolean weArePlaying=true;                                                                                                           // weArePlaying boolean muuttuja oli aluksi sitä varten että yritin koodata loopista poistumista sen avulla, mutta eihän se sitten toiminut ollenkaan. Lopulta oikea lopputulos ja haluttu ohjelmointiratkaisu saatiin break lauseita käyttämällä.
//        double betSize;                                                                                                                     // muuttuja betSize on lähinnä vain panoksen koon kysymista ja tallennusta varten. 
//          
//        
//        controller.setHandsToController(playerhand, dealerhand);                                                                                            // controller tuntee playerhandin ja dealerhandin asetusmetodilla. controllerista löytyy instanssimuuttujat erikseen playerhand ja dealerhand, ja voidaan käyttä eri metodeja controlelrissa.
//        testingdeck.createTestingDeck();
//        /*   TÄSSÄ OLI PERUSPELIPAKAN KOODIA ÄLÄ POISTA!!!
//        final int numberOfDecksUsed= controller.playingDeckGenerateShuffle(playingdeck);                                                                // asks how many decks to use for playing deck, and also populates playing deck with cards, and also shuffles it once
//        
//        int boundaryValueForShuffle= (int)( (numberOfDecksUsed*52)/4 );
//        int remainingCards;
//        */
//        
//        while(weArePlaying){      // main game loop 
//            
//            if (!controller.userWillingnessToPlay()){        // asks if user wants to keep playing
//                break;
//            }
//            
//            betSize= controller.whatIsBetSize();        // asks the betsize from user, if user places illegal bet, he is kicked out of casino
//            System.out.println("betsize was "+betSize);
//            if (betSize==0)
//                break;
//            
//            if(!controller.playerBetting(betSize, playeraccount, blackjackPot)){        // betting procedure, if user is out of money, he is kicked out of casino
//                break;
//            }
//            System.out.println("playersaldo is " +playeraccount.getMoneyAmount());
//            
//            
//            controller.dealTwoStartingCards();      // deal starting cards to dealer and player
//            
//            if (controller.getPlayerHandValue()==21){        // CASE 1.) check if the player has "natural blackjack" 
//                System.out.println("player has BLACKJACK ");
//
//                controller.showDealerHand();
//                if (controller.getDealerHandValue()==21){       // check if dealer also has "natural blackjack", in this case the game is "draw" 
//                    playeraccount.increaseMoneyAmount(blackjackPot.getMoneyAmount() );
//                    
//                    System.out.println("the game is a draw");
//                    System.out.println("playermoney is: " +playeraccount.getMoneyAmount());
//                    System.out.println("dealermoney is: " +dealeraccount.getMoneyAmount());
//                }
//                else {
//                    while(  controller.getDealerHandValue()<17   ){   // dealer hits extra cards to get to 21
//                        controller.dealerHitCard();
//                    }
//                    if (controller.getDealerHandValue()==21){
//                        System.out.println("the dealer has 21, the game is a draw: ");
//                        playeraccount.increaseMoneyAmount(blackjackPot.getMoneyAmount());
//                        System.out.println("playermoney is: " +playeraccount.getMoneyAmount());
//                        System.out.println("dealermoney is: " +dealeraccount.getMoneyAmount());
//                    }
//                    
//                    else{
//                        System.out.println("the dealer busts, the player wins the game");
//                        System.out.println("dealerhandvalue was " +controller.getDealerHandValue());
//                        blackjackPot.setMoneyAmount(1.5*betSize+betSize);
//                        System.out.println("player reward is valued at " + blackjackPot.getMoneyAmount() );
//                        if (   !dealeraccount.reduceMoneyAmount( blackjackPot.getMoneyAmount()  )  ){
//                            System.out.println("CASINO IS BANKRUPT game ends now, BYE!");
//                            break;
//                        }
//                        else
//                            playeraccount.increaseMoneyAmount(blackjackPot.getMoneyAmount());
//                            
//                        
//                        System.out.println("playermoney is: " +playeraccount.getMoneyAmount());
//                        System.out.println("dealermoney is: " +dealeraccount.getMoneyAmount());
//                    }     // player wins because he has "natural blackjack", and dealer busts
//                }
//                    
//                
//                
//            }
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            /*  ÄLÄ POISTA TÄTÄ KOODIA LIITTYY OIKEAAN `PELIPAKKAAN!!!
//            remainingCards = controller.checkRemainingCardsInDeck(playingdeck);
//            if (remainingCards <= boundaryValueForShuffle){
//                  controller.restartDeckShuffle(numberOfDecksUsed, playingdeck);
//
//
//            }
//            */
//            
//            
//            
//        }
//        
//        
//
//      
//        
//    }
    
}
