package org.slomka.blackjack.deck;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slomka.blackjack.Out;
import org.slomka.blackjack.deck.Card.CardNumber;
import org.slomka.blackjack.deck.Card.CardSuit;
import org.slomka.blackjack.exceptions.CardNotFoundException;

/**
 * Represents a group of cards. 
 * @author Slomka
 */
public class Deck {

    private Card[] cards = new Card[52];

    public Deck() {
        fillDeck();
    }

    /**
     * @return the last card from the deck and remove the card from deck
     */
    public Card popCard() {
        Card pick = cards[cards.length - 1];
        cards = Arrays.copyOf(cards, cards.length - 1);
        return pick;
    }

    /**
     *
     * @return cards containing in this deck
     */
    public Card[] getCards() {
        return this.cards;
    }

    /**
     * Add a list of cards to the current deck
     *
     * @param newcards the cards that will be added to the deck
     */
    public void addCards(Card[] newcards) {
        this.cards = Stream.of(newcards, this.cards).flatMap(Stream::of).toArray(Card[]::new);
    }

    /**
     * Construct a deck with 52 cards
     */
    private void fillDeck() {
        cards = Arrays.stream(CardSuit.values())
                .flatMap(suit -> Arrays.stream(CardNumber.values()).map(rank -> new Card(rank, suit)))
                .collect(Collectors.toList()).toArray(new Card[52]);
    }

    /**
     * Removes a specific card from the deck and return this card.
     * 
     * @param number the number of the card 
     * @param suit the card suit
     * @return the requested card
     * @throws CardNotFoundException if the card was not found
     */
    public Card getCard(CardNumber number, CardSuit suit) throws CardNotFoundException {
        for (int x = 0; x < cards.length; x++) {
            Card card = cards[x];
            if (card.getNumber() == number && card.getSuit() == suit) {
                Card outgoing[] = new Card[cards.length - 1];
                System.arraycopy(cards, 0, outgoing, 0, x);
                System.arraycopy(cards, x + 1, outgoing, x, cards.length - (x + 1));
                cards = outgoing;
                return card;
            }
        }
        throw new CardNotFoundException(new Card(number, suit));
    }

    /*
     * Do a simple shuffle
     */
    public void shuffle() {
        List cardList = Arrays.asList(this.cards);
        Collections.shuffle(cardList);
        this.cards = (Card[]) cardList.toArray(new Card[cardList.size()]);
        Out.print("The Deck has been Shuffled.");
    }

}
