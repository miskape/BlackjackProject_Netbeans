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
public class Main {

    /*
    HUOM meidän controller class = nimeltään BlackjackProject.java
    tuolta filestä löytyy controllerin metodeja
    main program on Main.java

    näillä pitäisi toimia kun laitta run file main program
    BlackjackProject classiin tehtiin hieman liikaa metodeja, elikkä niitä tehtiin hieman yli tarpeiden, valitettavasti. Lopulta vain
    parhaat metodit jäivät main programmiin käytettäviksi.
     */
    public static void main(String[] args) {

        View view = new View();
        BlackjackProject controller = new BlackjackProject(view);                    // controlleri tuntee view:in ja tämä tulee olla constructorissa
        Bankaccount playeraccount = new Bankaccount(2000);
        Bankaccount dealeraccount = new Bankaccount(2000);
//        //        ///TESTIPAKKA ALKAA///
//        Deck testdeck= new Deck();
////        /// TESTIPAKKA PÄÄTTYY///
        Deck playingdeck = new Deck();                                                   // normaali pelipakka
        Hand playerhand = new Hand(playingdeck);                                         // myös dealerhand tuntee playingdeck:in josta nostetaan kortteja dealerhandiin itseensä.
        Hand dealerhand = new Hand(playingdeck);
        Hand cardcount = new Hand(playingdeck);
        boolean weArePlaying = true;                                                        // weArePlaying boolean muuttuja oli aluksi sitä varten että yritin koodata loopista poistumista sen avulla, mutta eihän se sitten toiminut ollenkaan. Lopulta oikea lopputulos ja haluttu ohjelmointiratkaisu saatiin break lauseita käyttämällä.
        controller.setBankaccounts(playeraccount, dealeraccount);
        controller.setHandsToController(playerhand, dealerhand, cardcount);  // controller tuntee playerhandin ja dealerhandin asetusmetodilla. controllerista löytyy instanssimuuttujat erikseen playerhand ja dealerhand, ja voidaan käyttä eri metodeja controlelrissa.
//        testdeck.createTestingDeck();

        final int numberOfDecksUsed = controller.playingDeckGenerateShuffle(playingdeck);            // asks how many decks to use for playing deck, and also populates playing deck with cards, and also shuffles it once
        final int maxNumberOfCards = numberOfDecksUsed * 52;
        final int boundaryValueForShuffle = (int) ((numberOfDecksUsed * 52) / 4);                  // boundaryValue is the variable against which the remainingCArds variable is checked
        int remainingCards;             // this variable is checked per each blackjack round, for the amount of remainingcards  in the playingdeck
        int maingameResult = 0;
        int sidegameResult = 0;
        int runningCount = 0;
//        double trueCount=0;
//        int undealtDecks;

        while (weArePlaying) {

            double insuranceBet = 0;      // bettien määrä täytyy kirjaimellisesti "nollata " aina kun tulee uusi kierros, käsittääkseni...
            double sideBet = 0;
            double betSize = 0;
            boolean playerMadeInsuranceBet = false;       // boolean variablejen resetoiminen false arvoihin ennen blackjack kierroksen alkua
            boolean playerHasWonInsuranceBet = false;
            boolean playerIsBankrupt = false;
            boolean dealerIsBankrupt = false;
            double naturalReward = 0;
            boolean splitWasMade = false;

            System.out.println("\n");
//            System.out.println("runningcount is  currently at " +runningCount);
            System.out.println("\n");
            System.out.println("playeraccount money is " + playeraccount.getMoneyAmount());
            System.out.println("casinoaccount money is " + dealeraccount.getMoneyAmount());
            
            betSize = controller.whatIsBetSize();        // 2.) asks the betsize from user,
            System.out.println("");
            System.out.println("betsize was " + betSize);

            if (betSize == 0) // 3.) if user places illegal bet, he is kicked out of casino
            {
                break;
            }

            if (!controller.playerBettingSimplified(betSize)) // 4.) betting procedure, panos menee pot
            {
                break;      //if user is out of money, he is kicked out of casino
            }
            System.out.println("playersaldo is " + playeraccount.getMoneyAmount());
            System.out.println("");
            controller.dealTwoStartingCards();      // 5.) deal starting cards to dealer and player
            if (controller.insuranceBetOffering()) {         // 6.) insurancebet haara, ja insurancebet yritetään antaa,  tässä  myös  tarkistetaan insurancebetin voittaminen ja häviäminen TIETOA VOITOSTA TAI HÄVIÖSTÄ EI SAA PALJASTAA VIELÄ PELAAJALLE
                insuranceBet = 0.5 * betSize;           // TIETO insurancebetin voitosta tai tappiosta tulee vasta kun dealer on saanut luvan paljastaa omat alkukorttinsa
                playerMadeInsuranceBet = true;  // boolean variableilla pystytään tarkastamaan että printataanko insuranceBetin voittotiedot vai ei, ja mitä printataan
                switch (controller.evaluateInsuranceBet(insuranceBet)) {
                    case 0:
                        playerIsBankrupt = true;
                        break;             // break mainloop player runs out of money when betting, he gets thrown out from casino, Mainloop== the playing loop of the game
                    case 1:
                        playerHasWonInsuranceBet = false;
                        dealeraccount.increaseMoneyAmount(insuranceBet);
                        break;         // player loses insurancebet, but the game doesn't reveal any extra information to the player yet about dealer's cards
                    case 2:
                        playerHasWonInsuranceBet = true;
                        playeraccount.increaseMoneyAmount(insuranceBet);
                        break;          // player wins the insurancebet, but the game doesn't reveal any extra information to the player yet about dealer's cards
                }
                if (playerIsBankrupt) {       // breakataan while loopista ulos kun playerIsBankrupt
                    break;
                }

            } else {                      // insurancebet  rakenne  loppuu tähän else-haaraan, player ei tehnyt insuranceBetia
                System.out.println("");
                System.out.println("you did not make insurancebet ");
            }

            int naturalBlackJackresult = controller.evaluateNaturalBlackJack();          // tässä tarkistetaan saiko pelaaja alkukorteissa "natural blackjack" elikkä joko voitto tai tasapeli heti alkuun

            if (naturalBlackJackresult == 1) {
                view.printHandValues(dealerhand, playerhand);
                System.out.println("the game is a draw ");      // draw  natural blackjack
                playeraccount.increaseMoneyAmount(betSize);
                System.out.println("you win your own money back, valued at " + betSize);
                controller.clearBothHands();
            } else if (naturalBlackJackresult == 2) {
                view.printHandValues(dealerhand, playerhand);
                System.out.println("player wins the hand ");        // player wins      natural blackjack extra maksu playeraccountille. Playerin rewardi = 1.5*betSize
                controller.clearBothHands();
                if (!dealeraccount.reduceMoneyAmount(betSize * 1.5)) {// dealeraccountilta tulee vähentää  (1.5*betSize)
                    System.out.println("CASINO IS BANKRUPT game ends we are terribly sorry for our bad  service... it is unlikely you will get paid ");
                    break;
                } else {
                    naturalReward = betSize + (1.5 * betSize);        // "pottiin" lisätään ikäänkuin dealerinrahat korotettuna natural blackjackin maksukertoimella, pelaaja voittaa potin
                    playeraccount.increaseMoneyAmount(naturalReward);
                    System.out.println("you win natural blackjack reward valued at " + naturalReward);
                }
            } else {  // naturalBlackJackresult should be  zero in this branch ==> no player natural blackjack in the starting cards...
                splitWasMade = false;                 //variable splitWasMade siihen sijoitetaan splitOffering() return value, että joko SPLITTAUS ONNISTUI tai EPÄONNISTUI
                splitWasMade = controller.splitOffering();
            }

            // sideBet on eri betti kuin betSize, ne voidaan voittaa tai hävitä tai tasapelata toisistaan riippumatta joko molemmat tai toinen tai ei kumpikaan.
            if (splitWasMade && naturalBlackJackresult == 0) {            // mennään sisään split haaraan ohjelmassa SILLOIN kun voidaan SPLITATA elikkä splittbranch ja natural blackjack alussa sulkevat toisensa pois alussa... myöhemmin kutienkin mahdollisia blackjackit kun player saa sidehand käden ja mainhand käden...
                sideBet = betSize;
                if (!playeraccount.reduceMoneyAmount(sideBet)) {       // vähennetään playeriltä sidebetin määrä rahaa pelaaja panostaa sidebetin SIDEBET = BETSIZE ALWAYS
                    System.out.println("you're bankrupt, you cannot make the sideBet!!! ");
                    break;
                } else {
                    
                    System.out.println("");
                    System.out.println("you placed the sideBet,  sidePot is now valued at " + sideBet);
                    System.out.println(" the mainPot is now valued at " + betSize);
                }
                view.printSidegameStartMessage();

                Hand sidehand = new Hand(playingdeck);      // HUOM TÄRKEÄ!!!  luodaan sidehand olio joka on sidegamen kortit playerille
                controller.setSidehandToController(sidehand);  // asetetaan liitetään sidehand ja controlleri
                controller.createSideHandWithTwoCards();        // luodaan sidehandiin tarvittavat kortit siten että, mainhand pilkotaan kahteen osaan, ja toinen kortti menee sidehandiin.
                playerhand.hitPlayerCard();         // TÄRKEÄ koodin pätkä! tämä koodi palauttaa takaisin playerhandiin = main handiin yhden puuttuvan alkukortin ==> maingamessa playerhandillä on 2 alkukorttia nytten
                int sidehandBlackJackresult = controller.evaluateNaturalBlackJackParameters(dealerhand, sidehand);       // tarkistetaan onko playerillää sidehandin alkukorteissa natural blackjackia, JOS on ==> player joko voittaa tai pelaa tasapelin sidegamessa

                if (sidehandBlackJackresult == 1) {                // tässä kohdassa molemmilal oli natural blackjack==>tasapeli
                    view.printSidegameEndsMessage();            // sidegame ends viestien tulisi olla samoja printtauksia, siten että playeri ei saa "extra tietoa dealerin korteista" jota voi hyödyntää myöhemmin pelattavassa maingamessa.
                    sidegameResult = 1;       // result muuttujat ovat integer muuttujia joihin tallennetaan tietoa että kumpi voitti vai tuliko tasapeli
                    playeraccount.increaseMoneyAmount(sideBet); // player gets his own sidebet money back
                } else if (sidehandBlackJackresult == 2) {       // elif haarassa playerilla oli alussa natural blackjack==>player voittaa
                    view.printSidegameEndsMessage();
                    sidegameResult = 2;

                    if (!dealeraccount.reduceMoneyAmount(sideBet * 1.5)) {
                        System.out.println("you have won sidegame");
                        System.out.println("CASINO IS BANKRUPT game ends we are terribly sorry for our bad  service... it is unlikely you will get paid ");
                        break;
                    } else {
                        naturalReward = sideBet + (sideBet * 1.5);
                        playeraccount.increaseMoneyAmount(naturalReward);
                    }
                } else {                                                          // else haaraan mennään koska sidegamea täytyy evaluoida ja playeriltä kysytään sidegamen valinnat
                    view.printRevealedDealerCardAndPlayerCards(dealerhand, sidehand);
                    int sidePlayerDecision = view.askPlayerDecisionsSideGame();

                    if (sidePlayerDecision == 1) {       // player stands in sidegame
                        switch (controller.evaluateStandSidegame()) {
                            case 0:
                                sidegameResult = 0;
                                view.printSidegameEndsMessage();
                                dealeraccount.increaseMoneyAmount(sideBet);
                                break;      // dealer wins
                            case 1:
                                sidegameResult = 1;
                                view.printSidegameEndsMessage();
                                playeraccount.increaseMoneyAmount(sideBet);
                                break;      // draw
                            case 2:
                                sidegameResult = 2;
                                view.printSidegameEndsMessage();
                                if (!dealeraccount.reduceMoneyAmount(sideBet)) {
                                    System.out.println("you have won sidegame");
                                    System.out.println("CASINO IS BANKRUPT game ends we are terribly sorry for our bad  service... it is unlikely you will get paid ");
                                    dealerIsBankrupt = true;
                                } else {
                                    sideBet = sideBet + sideBet;
                                    playeraccount.increaseMoneyAmount(sideBet);
                                }

                                break;     // player wins
                        }
                        if (dealerIsBankrupt) {
                            break;
                        }

                    } else if (sidePlayerDecision == 2) {           // player hits in sidegame
                        switch (controller.evaluateHitSidegame(dealerhand, sidehand)) {
                            case 0:     // dealer wins
                                sidegameResult = 0;
                                view.printSidegameEndsMessage();
                                dealeraccount.increaseMoneyAmount(sideBet);
                                break;
                            case 1:         // draw
                                sidegameResult = 1;
                                view.printSidegameEndsMessage();
                                playeraccount.increaseMoneyAmount(sideBet);
                                break;
                            case 2:         // player wins
                                sidegameResult = 2;
                                view.printSidegameEndsMessage();

                                if (!dealeraccount.reduceMoneyAmount(sideBet)) {
                                    System.out.println("you have won sidegame");
                                    System.out.println("CASINO IS BANKRUPT game ends we are terribly sorry for our bad  service... it is unlikely you will get paid ");
                                    dealerIsBankrupt = true;
                                } else {
                                    sideBet = sideBet + sideBet;
                                    playeraccount.increaseMoneyAmount(sideBet);
                                }
                                break;
                        }
                        if (dealerIsBankrupt) {
                            break;
                        }
                    } else {       // player doubles down in sidegame
                        if (!playeraccount.reduceMoneyAmount(sideBet)) {          // double down tarkoittaa panoksen tuplaamista siinä kädessä jota pelataan
                            System.out.println("you CANNOT AFFORD to doubledown, please leave the casino ");
                            break;
                        } else {
                            sideBet = sideBet * 2;
                            System.out.println("player doublesdown in sidegame, sidePot is now valued at " + sideBet);

                        }
                        switch (controller.evaluateDoubleDownSidegame()) {
                            case 0:         // dealer wins
                                sidegameResult = 0;
                                view.printSidegameEndsMessage();
                                dealeraccount.increaseMoneyAmount(sideBet);
                                break;
                            case 1:         // game is draw
                                sidegameResult = 1;
                                view.printSidegameEndsMessage();
                                playeraccount.increaseMoneyAmount(sideBet);
                                break;
                            case 2:         // player wins
                                sidegameResult = 2;
                                view.printSidegameEndsMessage();
                                if (!dealeraccount.reduceMoneyAmount(sideBet)) {
                                    System.out.println("you have won sidegame");
                                    System.out.println("CASINO IS BANKRUPT game ends we are terribly sorry for our bad  service... it is unlikely you will get paid ");
                                    dealerIsBankrupt = true;
                                } else {
                                    sideBet = sideBet + sideBet; //gives player the money from his own bet plus the casino's bet
                                    playeraccount.increaseMoneyAmount(sideBet);
                                }
                                break;
                        }
                        if (dealerIsBankrupt) {
                            break;
                        }
                    }
                }       // sidegamen evaluointi päättyy tähän...

                // TÄSTÄ ALKAA MAINGAME joka pelataan SIDEGAMEN jälkeen...
                // HUOM TÄRKEÄ DEALERILLÄ ON JO OLEMASSA OMAT KORTTINSA dealerhandissä
                // dealerhandiin nostettiin jo pakasta sidegamen aikana dealerin omat kortit.
                // HANDEJA EI SAA VIELÄ CLEARATA MISSÄÄN METODEISSA VIELÄ,
                //VASTA SITTEN CLEARATAAN HANDIT KUN TÄMÄ MAINGAME ON PELATTU LOPPUUN
                // JA TULOKSET ON PRINTATTU ULOS
                view.printMaingameStartMessage();

                int mainhandBlackJackResult = controller.evaluateNaturalBlackJackParameters(dealerhand, playerhand);

                if (mainhandBlackJackResult == 1) {      // tässä kohdassa molemmilla oli natural blackjack==>tasapeli
                    maingameResult = 1;
                    view.printMaingameEndsMessage();

                } else if (mainhandBlackJackResult == 2) {      // tässä kohdassa player voittaa natural blackjackilla==>pelaaja voitti
                    maingameResult = 2;
                    view.printMaingameEndsMessage();
                    if (!dealeraccount.reduceMoneyAmount(betSize * 1.5)) {
                        System.out.println("you have won maingame");
                        System.out.println("CASINO IS BANKRUPT game ends we are terribly sorry for our bad  service... it is unlikely you will get paid ");
                        break;
                    } else {
                        naturalReward = betSize + (betSize * 1.5);
                        playeraccount.increaseMoneyAmount(naturalReward);
                    }
                } else {                         // maingamessa player ei saanut natural blackjackia
                    view.printRevealedDealerCardAndPlayerCards(dealerhand, playerhand);
                    int mainPlayerDecision = controller.playerDecisionsRegular();       // player stands in maingame  /// HUOM TÄRKEÄ EDITOI EvaluateMetodi NIIN ETTEI NE CLEARAA HANDEJA EIVÄTKÄ PRINTTAA DEALERIN HANDIA
                    if (mainPlayerDecision == 1) {
                        switch (controller.maingameStandEvaluate()) {
                            case 0: //maingame player loses
                                maingameResult = 0;
                                dealeraccount.increaseMoneyAmount(betSize);
                                view.printMaingameEndsMessage();
                                break;
                            case 1: // maingame is draw
                                maingameResult = 1;
                                playeraccount.increaseMoneyAmount(betSize);
                                view.printMaingameEndsMessage();
                                break;
                            case 2: // maingame player wins
                                view.printMaingameEndsMessage();

                                maingameResult = 2;
                                if (!dealeraccount.reduceMoneyAmount(betSize)) {
                                    System.out.println("maingame   player wins");
                                    System.out.println("CASINO IS BANKRUPT game ends we are terribly sorry for our bad  service... it is unlikely you will get paid ");
                                    dealerIsBankrupt = true;
                                } else {
                                    betSize = betSize + betSize;
                                    playeraccount.increaseMoneyAmount(betSize);
                                }
                                break;
                        }
                        if (dealerIsBankrupt) {
                            break;
                        }
                    } else if (mainPlayerDecision == 2) {           //  player hits in maingame /// /// HUOM TÄRKEÄ EDITOI EvaluateMetodi NIIN ETTEI NE CLEARAA HANDEJA EIVÄTKÄ PRINTTAA DEALERIN HANDIA
                        switch (controller.maingameHitEvaluate()) {
                            case 0: //player loses
                                maingameResult = 0;
                                view.printMaingameEndsMessage();
                                dealeraccount.increaseMoneyAmount(betSize);
                                break;
                            case 1: // draw
                                maingameResult = 1;
                                view.printMaingameEndsMessage();
                                playeraccount.increaseMoneyAmount(betSize);
                                break;
                            case 2: //player wins
                                view.printMaingameEndsMessage();
                                maingameResult = 2;
                                if (!dealeraccount.reduceMoneyAmount(betSize)) {
                                    System.out.println("maingame  player wins ");
                                    System.out.println("CASINO IS BANKRUPT game ends we are terribly sorry for our bad  service... it is unlikely you will get paid ");
                                    dealerIsBankrupt = true;
                                } else {
                                    betSize = betSize + betSize;
                                    playeraccount.increaseMoneyAmount(betSize);
                                }
                                break;
                        }
                        if (dealerIsBankrupt) {
                            break;
                        }
                    } else {             // player doubles down in maingame
                        if (!playeraccount.reduceMoneyAmount(betSize)) {
                            System.out.println("you CANNOT AFFORD to doubledown, please leave the casino ");
                            break;
                        } else {
                            betSize = betSize * 2;
                            System.out.println("player doublesdown in maingame, mainPot is now valued at " + betSize);
                        }
                        switch (controller.maingameDoubleDownEvaluate()) {            /// HUOM TÄRKEÄ EDITOI EvaluateMetodi NIIN ETTEI NE CLEARAA HANDEJA EIVÄTKÄ PRINTTAA DEALERIN HANDIA
                            case 0: //player loses
                                maingameResult = 0;
                                dealeraccount.increaseMoneyAmount(betSize);
                                view.printMaingameEndsMessage();
                                break;
                            case 1: //draw
                                maingameResult = 1;
                                playeraccount.increaseMoneyAmount(betSize);
                                view.printMaingameEndsMessage();
                                break;
                            case 2: // player wins
                                view.printMaingameEndsMessage();
                                maingameResult = 2;
                                if (!dealeraccount.reduceMoneyAmount(betSize)) {
                                    System.out.println("maingame  player wins ");
                                    System.out.println("CASINO IS BANKRUPT game ends we are terribly sorry for our bad  service... it is unlikely you will get paid ");
                                    dealerIsBankrupt = true;
                                } else {
                                    betSize = betSize + betSize;
                                    playeraccount.increaseMoneyAmount(betSize);
                                }

                                break;
                        }
                        if (dealerIsBankrupt) {
                            break;
                        }
                    }

                }
                // lopuksi vasta printataan resultit /// ENSIKSI SIDEGAME RESULTIT
                System.out.println('\n');
                System.out.println("__________________________________________________________");
                System.out.println("results for the sidegame are :");
                view.announceResultSidegame(sidegameResult);      //// HUOM TÄRKEÄ NETBEANS HERJAA  TÄSTÄ
                controller.showDealerHand();
                controller.showSideHand();
                view.announceSidegameReward(sidegameResult, sidehandBlackJackresult, sideBet);       //metodi printaa voitetun potin suuruuden sidegamessa
//                  controller.clearAllHands();
                System.out.println("\n");

                // lopuksi vasta printataan resultit /// TOISEKSI MAINGAME RESULTIT
                System.out.println("results for the maingame are :");
                view.announceResultMaingame(maingameResult);          //// HUOM TÄRKEÄ NETBEANS HERJAA TÄSTÄ
                controller.showDealerHand();
                controller.showPlayerHand();
                view.announceMaingameReward(maingameResult, mainhandBlackJackResult, betSize);        // metodi printtaa voitetun potin suuruuden maingamessa
                System.out.println("__________________________________________________________");
//                  runningCount= sidehand.getRunningCountFromSingleHand()+dealerhand.getRunningCountFromSingleHand() +playerhand.getRunningCountFromSingleHand();
//                  undealtDecks= ((maxNumberOfCards-(maxNumberOfCards-controller.checkRemainingCardsInDeck(playingdeck)))/52);
//                  trueCount= runningCount/ (numberOfDecksUsed-undealtDecks);
//                  trueCount= controller.getTrueCount(playingdeck, numberOfDecksUsed, runningCount);
                controller.clearAllHands();       // lopuksi clearataan sidehand, playerhand ja dealerhand korteistaan.
                System.out.println('\n');

            } // splittaus haara päättyy tähän... splittaushaara sekä regularPlayerDecisions haara (toisinsanoen regulargame haara) ovat toisensa poissulkevia haaroja ohjelmassa...
           
            // REGULARGAME HAARA ALKAA TÄSTÄ ALASPÄIN tänne haaraan mennään kun player EI SPLITANNUT OLLENKAAN
            else if (!splitWasMade && naturalBlackJackresult == 0) {    ///  elif haaran sisällä on se haara, jossa ei ole sidegamea ollenkaan ==> elikkä PLAYER EI SPLITANNUT, lisätty ehtolause EI VIELÄ TESTATTU KUNNOLLA   // pelaaja voi normaaliisti tehdä regular choices 1.) stand 2.) hit 3.) doubledown
                System.out.println("");
                System.out.println(" the pot is now valued at " + betSize);
//                view.printRevealedDealerCardAndPlayerCards(dealerhand, playerhand);       //  printtaaa alkukortit uudestaan jos player unohti ne/// EDITED: näitä ei tarvinnutkaan printata turhaa printtiä

                int regularPlayerDecision = controller.playerDecisionsRegular();
                if (regularPlayerDecision == 1) {                  // player stands in regulargame (no split existed)
                    switch (controller.playerStandEvaluate()) {
                        case 0:
                            System.out.println("dealer wins the game ");
                            dealeraccount.increaseMoneyAmount(betSize);
//                            runningCount=dealerhand.getRunningCountFromSingleHand() +playerhand.getRunningCountFromSingleHand();
//                            undealtDecks= ((maxNumberOfCards-(maxNumberOfCards-controller.checkRemainingCardsInDeck(playingdeck)))/52);
//                            trueCount= runningCount/ (numberOfDecksUsed-undealtDecks);
//                            trueCount= controller.getTrueCount(playingdeck, numberOfDecksUsed, runningCount);
                            controller.clearBothHands();
                            break;     //  betSizen lisäys dealerinaccountille
                        case 1:
                            System.out.println("the game is a draw, player gets his own money back valued at " + betSize);
                            playeraccount.increaseMoneyAmount(betSize);
//                            runningCount=dealerhand.getRunningCountFromSingleHand() +playerhand.getRunningCountFromSingleHand();
//                            undealtDecks= ((maxNumberOfCards-(maxNumberOfCards-controller.checkRemainingCardsInDeck(playingdeck)))/52);
//                            trueCount= runningCount/ (numberOfDecksUsed-undealtDecks);
//                            trueCount= controller.getTrueCount(playingdeck, numberOfDecksUsed, runningCount);
                            controller.clearBothHands();
                            break;        //  betSizen lisäys playeraccountille
                        case 2:
//                            runningCount=dealerhand.getRunningCountFromSingleHand() +playerhand.getRunningCountFromSingleHand();
//                            undealtDecks= ((maxNumberOfCards-(maxNumberOfCards-controller.checkRemainingCardsInDeck(playingdeck)))/52);
//                            trueCount= runningCount/ (numberOfDecksUsed-undealtDecks);
//                            trueCount= controller.getTrueCount(playingdeck, numberOfDecksUsed, runningCount);
                            controller.clearBothHands();
                            if (!dealeraccount.reduceMoneyAmount(betSize)) {
                                System.out.println("player wins the game!");
                                System.out.println("CASINO IS BANKRUPT game ends we are terribly sorry for our bad  service... it is unlikely you will get paid ");
                                dealerIsBankrupt = true;
                            } else {
                                betSize = betSize + betSize;
                                playeraccount.increaseMoneyAmount(betSize);
                                System.out.println("player wins the game, his reward is valued at " + betSize);
                            }
                            break;
                    }
                    if (dealerIsBankrupt) {
                        break;
                    }
                } else if (regularPlayerDecision == 2) {            // player hits in regulargame (no split existed)
                    switch (controller.playerHitEvaluate()) {
                        case 0:
                            System.out.println("dealer wins the game ");
                            dealeraccount.increaseMoneyAmount(betSize);
//                            runningCount=dealerhand.getRunningCountFromSingleHand() +playerhand.getRunningCountFromSingleHand();
//                            undealtDecks= ((maxNumberOfCards-(maxNumberOfCards-controller.checkRemainingCardsInDeck(playingdeck)))/52);
//                            trueCount= runningCount/ (numberOfDecksUsed-undealtDecks);
//                            trueCount= controller.getTrueCount(playingdeck, numberOfDecksUsed, runningCount);
                            controller.clearBothHands();
                            break;
                        case 1:
                            System.out.println("the game is a draw, player gets his own money back valued at " + betSize);
                            playeraccount.increaseMoneyAmount(betSize);
//                            runningCount=dealerhand.getRunningCountFromSingleHand() +playerhand.getRunningCountFromSingleHand();
//                            undealtDecks= ((maxNumberOfCards-(maxNumberOfCards-controller.checkRemainingCardsInDeck(playingdeck)))/52);
//                            trueCount= runningCount/ (numberOfDecksUsed-undealtDecks);
//                            trueCount= controller.getTrueCount(playingdeck, numberOfDecksUsed, runningCount);
                            controller.clearBothHands();
                            break;
                        case 2:
                            System.out.println("player wins the game!");
//                            runningCount=dealerhand.getRunningCountFromSingleHand() +playerhand.getRunningCountFromSingleHand();
//                            undealtDecks= ((maxNumberOfCards-(maxNumberOfCards-controller.checkRemainingCardsInDeck(playingdeck)))/52);
//                            trueCount= runningCount/ (numberOfDecksUsed-undealtDecks);
//                            trueCount= controller.getTrueCount(playingdeck, numberOfDecksUsed, runningCount);
                            controller.clearBothHands();
                            if (!dealeraccount.reduceMoneyAmount(betSize)) {
                                System.out.println("CASINO IS BANKRUPT game ends we are terribly sorry for our bad  service... it is unlikely you will get paid ");
                                dealerIsBankrupt = true;
                            } else {
                                betSize = betSize + betSize;
                                playeraccount.increaseMoneyAmount(betSize);
                                System.out.println("player wins the game, his reward is valued at " + betSize);
                            }
                            break;
                    }
                    if (dealerIsBankrupt) {
                        break;
                    }
                } else {           // player doubles down in regulargame (no split existed)
                    if (!playeraccount.reduceMoneyAmount(betSize)) {
                        System.out.println("you CANNOT AFFORD to doubledown, please leave the casino ");
                        break;
                    } else {
                        betSize = betSize * 2;
                        System.out.println("");
                        System.out.println("player doublesdown, the pot is now valued at " + betSize);
                    }
                    switch (controller.playerDoubleDownEvaluate()) {
                        case 0:
                            System.out.println("dealer wins the game ");
                            dealeraccount.increaseMoneyAmount(betSize);
                            controller.clearBothHands();
                            break;
                        case 1:
                            System.out.println("the game is a draw, player gets his own money back valued at " + betSize);
                            playeraccount.increaseMoneyAmount(betSize);
                            controller.clearBothHands();
                            break;
                        case 2:
                            controller.clearBothHands();
                            if (!dealeraccount.reduceMoneyAmount(betSize)) {
                                System.out.println("player wins the game!");
                                System.out.println("CASINO IS BANKRUPT game ends we are terribly sorry for our bad  service... it is unlikely you will get paid ");
                                dealerIsBankrupt = true;
                            } else {
                                betSize = betSize + betSize;
                                playeraccount.increaseMoneyAmount(betSize);
                                System.out.println("player wins the game, his reward is valued at " + betSize);
                            }

                            break;
                    }
                    if (dealerIsBankrupt) {
                        break;
                    }
                }
            }

            // tähän kohtaan lisätään insurancebetin tulos ja printtaus!!!
            if (playerMadeInsuranceBet) {
                if (playerHasWonInsuranceBet) {
                    System.out.println("player has won insuranceBet, valued at " + insuranceBet);
                } else {
                    System.out.println("player has lost insuranceBet, it was valued at " + insuranceBet);
                }
            }

            // Tähän kohtaan TULEE LISÄTÄ cardsRemaining checkaus playingdeckistä
            remainingCards = controller.checkRemainingCardsInDeck(playingdeck);
            if (remainingCards <= boundaryValueForShuffle) {
                controller.restartDeckShuffle(numberOfDecksUsed, playingdeck);
                runningCount = 0;
//                trueCount=0;
//                undealtDecks=numberOfDecksUsed-1;
            }
            /// remainingCards check lisätty, NYT kortit eivät lopu pakasta kovinkaan helpolla ainakaan jos on useampi pelipakka aluksi kun tehdään "shoe" josta kortit jaetaan

//            if (!controller.userWillingnessToPlay()){        // 1.)asks if user wants to keep playing
//                break;
//            }
        }
    }
}
