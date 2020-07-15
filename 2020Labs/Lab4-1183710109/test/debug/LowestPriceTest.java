package debug;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class LowestPriceTest {
    /*
     * Test Strategy:
     * 
     * partition with used field:
     * only use 1 offer: Input: [2,5], [[3,0,5],[3,2,10]], [3,2] Output: 10
     * only use more offers: Input: [2,3], [[3,2,11],[1,3,10]], [4,5] Output: 21(not 22)
     * use items and 1 offer: Input: [2,5], [[3,3,25],[1,3,10]], [5,6] Output: 33(not 44 or 37)
     * use items and more offers: Input: [3,5], [[3,0,5],[1,2,10],[1,4,18]], [6,7] Output: 41
     * only items: Input: [3,5], [[2,3,22],[1,2,14], [6,7] Output: 53(not 56)
     */
    @Test
    public void testOnlyOneOffer() {
        List<Integer> price = Arrays.asList(2, 5);
        List<List<Integer>> special = Arrays.asList(Arrays.asList(3, 0, 5), Arrays.asList(3, 2, 10));
        List<Integer> needs = Arrays.asList(3, 2);
        LowestPrice lowestPrice = new LowestPrice();
        assertEquals(10, lowestPrice.shoppingOffers(price, special, needs));
    }

    @Test
    public void testOnlyMoreOffer() {
        List<Integer> price = Arrays.asList(2, 3);
        List<List<Integer>> special = Arrays.asList(Arrays.asList(3, 2, 11), Arrays.asList(1, 3, 10));
        List<Integer> needs = Arrays.asList(4, 5);
        LowestPrice lowestPrice = new LowestPrice();
        assertEquals(21, lowestPrice.shoppingOffers(price, special, needs));
    }

    @Test
    public void testOneOfferAndItems() {
        List<Integer> price = Arrays.asList(2, 5);
        List<List<Integer>> special = Arrays.asList(Arrays.asList(3, 3, 25), Arrays.asList(1, 3, 10));
        List<Integer> needs = Arrays.asList(5, 6);
        LowestPrice lowestPrice = new LowestPrice();
        assertEquals(33, lowestPrice.shoppingOffers(price, special, needs));
    }

    @Test
    public void testMoreOfferAndItems() {
        List<Integer> price = Arrays.asList(3, 5);
        List<List<Integer>> special = Arrays.asList(Arrays.asList(3, 0, 5), Arrays.asList(1, 2, 10),
                Arrays.asList(1, 4, 18));
        List<Integer> needs = Arrays.asList(6, 7);
        LowestPrice lowestPrice = new LowestPrice();
        assertEquals(41, lowestPrice.shoppingOffers(price, special, needs));
    }

    @Test
    public void testOnlyItems() {
        List<Integer> price = Arrays.asList(3, 5);
        List<List<Integer>> special = Arrays.asList(Arrays.asList(2, 3, 22), Arrays.asList(1, 2, 14));
        List<Integer> needs = Arrays.asList(6, 7);
        LowestPrice lowestPrice = new LowestPrice();
        assertEquals(53, lowestPrice.shoppingOffers(price, special, needs));
    }
}