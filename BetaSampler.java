import java.util.Random;
public class BetaSampler {
    private static double a;
    private static double b;
    private static double upperMain;
    private static double lowerMain;
    private static double mean;
    private static double standardDeviation;
    private static double lowerMainIntegral;
    private static final int iterations =  24;
    private static double incompleteBeta(double x, int iters) {
        iters /= 12;
        double integral = 0.0;
        if (x <= lowerMain) {
            return fullBooleIntegration(0, x, 12 * iters);
        }
        integral += lowerMainIntegral;
        if (x <= upperMain) {
            integral += fullBooleIntegration(lowerMain, x, 12 * iters);
            return integral;
        }
        integral += partialBooleIntegration(lowerMain, upperMain, 6 * iters);
        integral += fullBooleIntegration(upperMain, x, 6 * iters);
        return integral;
    }
    private static double fullBooleIntegration(double start, double end,
        int numIntervals) {
        double intervalSize = (end - start)/numIntervals;
        double epsilon = .0000001;
        double terminate = -3*intervalSize - epsilon;
        double integral = 0;
        double i = start + intervalSize;
        while (i - end < terminate) {
            integral += 32 * Math.pow(i, a)*Math.pow(1-i, b);
            i += intervalSize;
            integral += 12 * Math.pow(i, a)*Math.pow(1-i, b);
            i += intervalSize;
            integral += 32 * Math.pow(i, a)*Math.pow(1-i, b);
            i += intervalSize;
            integral += 14 * Math.pow(i,a)*Math.pow(1-i, b);
            i += intervalSize;
        }
        integral += 32 * Math.pow(i, a)*Math.pow(1-i, b);
        i += intervalSize;
        integral += 12 * Math.pow(i, a)*Math.pow(1-i, b);
        i += intervalSize;
        integral += 32 * Math.pow(i, a)*Math.pow(1-i, b);
        i += intervalSize;
        if (Math.abs(end - 1) > epsilon) {
            integral += 7 * Math.pow(i, a)*Math.pow(1-i, b);
        }
        integral *= intervalSize*4/90;
        return integral;
    }
    private static double partialBooleIntegration(double start, double end,
        int numIntervals) {
        double intervalSize = (end - start)/numIntervals;
        double epsilon = .0000001;
        double terminate = -3*intervalSize - epsilon;
        double integral = 0;
        double i = start + intervalSize;
        while (i - end < terminate) {
            integral += 32 * Math.pow(i, a)*Math.pow(1-i, b);
            i += intervalSize;
            integral += 12 * Math.pow(i, a)*Math.pow(1-i, b);
            i += intervalSize;
            integral += 32 * Math.pow(i, a)*Math.pow(1-i, b);
            i += intervalSize;
            integral += 14 * Math.pow(i,a)*Math.pow(1-i, b);
            i += intervalSize;
        }
        integral += 32 * Math.pow(i, a)*Math.pow(1-i, b);
        i += intervalSize;
        integral += 12 * Math.pow(i, a)*Math.pow(1-i, b);
        i += intervalSize;
        integral += 32 * Math.pow(i, a)*Math.pow(1-i, b);
        i += intervalSize;
        if (Math.abs(end - 1) > epsilon) {
            integral += 14 * Math.pow(i, a)*Math.pow(1-i, b);
        }
        integral *= intervalSize*4/90;
        return integral;
    }
    private static double inverseBetaCDF(double y) {
        double x = mean;
        for (int i = 0; i < 3; i++) {
            double delta = incompleteBeta(x, iterations)  - y;
            double f1 = Math.pow(x, a) * Math.pow(1-x, b);
            double f2 = (a) * Math.pow(x, a-1) * Math.pow(1-x, b)
                 - (b) * Math.pow(x, a) * Math.pow(1-x, b-1);
            double f3 = (a)*(a-1) * Math.pow(x, a-2) * Math.pow(1-x, b)
                -2 * (a) * (b) * Math.pow(x, a-1) * Math.pow(1-x, b-1)
                + b*(b-1) * Math.pow(x, a) * Math.pow(1-x, b-2);
            x -= (6*delta*f1*f1 - 3*delta*delta*f2) /
                (6*f1*f1*f1 - 6*delta*f1*f2 + delta*delta*f3);
        }
        return x;
    }
    public static double sample(double alpha, double beta) {
        init(alpha, beta);
        Random rand = new Random();
        double totalArea = incompleteBeta(1.0, iterations);
        double random = rand.nextDouble();
        return inverseBetaCDF(random*totalArea);
    }
    private static void init(double alpha, double beta) {
        double sum = alpha + beta;
        mean = alpha / (sum);
        standardDeviation = Math.sqrt(alpha * beta /
                (Math.pow(sum, 2)*(sum + 1)));
        double k = 10;
        lowerMain = Math.max(mean - k*standardDeviation, 0);
        upperMain = Math.min(mean + k*standardDeviation, 1);
        a = alpha - 1;
        b = beta - 1;
        lowerMainIntegral = partialBooleIntegration(0, lowerMain, iterations);
    }
}
