package hu.ak_akademia.scope;

import java.util.*;
import java.util.stream.*;

/**
 * Furcsa számok: <a href="https://hu.wikipedia.org/wiki/Furcsa_sz%C3%A1mok">...</a>
 * <p>
 * A legkisebb furcsa szám a 70. Valódi osztói 1, 2, 5, 7, 10, 14 és 35; ezek összege 74, de nem adhatók össze úgy, hogy 70-et adjanak.
 * A 12-es szám például bővelkedő, de nem furcsa szám; valódi osztói 1, 2, 3, 4 és 6, melyek összege 16; ugyanakkor 2+4+6 = 12.
 * <p>
 * Az első néhány furcsa szám:
 * <p>
 * 70, 836, 4030, 5830, 7192, 7912, 9272, 10430, 10570, 10792, 10990, 11410, 11690, 12110, 12530, 12670, 13370, 13510, 13790, 13930, 14770, ...
 * <p>
 * Feladat: írj egy metódust, amelynek tetszőleges long paramétert (limit) megadva visszaadja az addig megtalálható összes furcsa számot
 * (a határt is beleértve).
 */

public class WeirdNumbersOptimized {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        System.out.println(getWeirdNumbers(50_000));
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        System.out.println("Feldolgozáshoz szükséges idő: " + elapsedTime / 1_000_000 + " ms");
    }

    public static List<Long> getWeirdNumbers(long limit) {
        List<Long> weirdNumbers = new ArrayList<>();
        for (long n = 1; n <= limit; n++) {
            List<Long> divisors = getProperDivisors(n);
            if (getAbundantNumberRemainder(n, divisors) > 0 && !isPseudoPerfectNumber(n, divisors)) {
                weirdNumbers.add(n);
            }
        }
        return weirdNumbers;
    }

    private static List<Long> getProperDivisors(long n) {
        return LongStream.rangeClosed(1, n / 2)
                .filter(i -> n % i == 0)
                .boxed().collect(Collectors.toList());
    }

    private static long getAbundantNumberRemainder(long n, List<Long> divisors) {
        return divisors.stream()
                .mapToLong(a -> a)
                .sum() - n;
    }

    private static boolean isPseudoPerfectNumber(long n, List<Long> divisors) {
        long abundantNumberRemainder = getAbundantNumberRemainder(n, divisors);
            // perfect number                // every multiple of a semi(pseudo)-perfect number is semi-perfect, the first semi-perfect number is 6
        if (abundantNumberRemainder == 0 || (abundantNumberRemainder > 0 && n % 6 == 0)) {
            return true;
        }
        if (abundantNumberRemainder > 0) {
            int sumDivisor = 0;
            divisors.sort(Collections.reverseOrder());
            for (Long divisor : divisors) {
                if (sumDivisor + divisor == abundantNumberRemainder) {
                    return true;
                }
                if (sumDivisor + divisor > abundantNumberRemainder) {
                    continue;
                }
                sumDivisor += divisor;
            }
        }
        if (abundantNumberRemainder > 0) {
            return isRemainingNumberPseudoPerfect(n, divisors);
        }
        return false;
    }

    private static boolean isRemainingNumberPseudoPerfect(long n, List<Long> divisors) {
        int size = divisors.size();
        //  The value of subset[i][j] will be true if there is a subset of divisors[0..j-1] with sum equal to i
        boolean[][] subset = new boolean[(int) (n + 1)][size + 1];
        // If sum is 0, then answer is true
        for (int i = 0; i <= size; i++) {
            subset[0][i] = true;
        }
        //  If sum is not 0 and set is empty, then answer is false
        for (int i = 1; i <= n; i++) {
            subset[i][0] = false;
        }
        // Fill the subset table in bottom up manner
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= size; j++) {
                subset[i][j] = subset[i][j - 1];
                long test = divisors.get(j - 1);
                if (i >= test) {
                    subset[i][j] = subset[i][j] || subset[(int) (i - test)][j - 1];
                }
            }
        }
        return subset[(int) n][size];
    }
}
