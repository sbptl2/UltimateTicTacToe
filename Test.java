import java.util.Random;
import org.apache.commons.math3.distribution.BetaDistribution;

public class Test {
    public static void main(String[] args) {
        Random rand = new Random();
        long sum = 0;
        for (int i = 0; i < 1000; i++) {
            int alpha = rand.nextInt(100) + 3;
            int beta =  rand.nextInt(100) + 3;
            long time = System.nanoTime();
            BetaSampler.sample(alpha, beta);
            sum += System.nanoTime() - time;
        }
        System.out.println(sum / 1000);
    }
}
