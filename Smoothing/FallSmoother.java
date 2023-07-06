/**
 * Smooths values over time by limiting the second derivative.
 * Runs in constant time.
 * 
 * @author Elan Ronen
 */
public class FallSmoother implements Smoother {

    private final double maxd2ydx2; // The maximum second derivative (rate of change of rate of change)
    private final boolean fallUp;   // Whether to limit input moving away from zero
    private double output;          // The previous output
    private double dydx;            // The previous derivative (rate of change)

    public FallSmoother(double maxd2ydx2, boolean fallUp) {
        this.maxd2ydx2 = Math.abs(maxd2ydx2);
        this.fallUp = fallUp;
    }

    @Override
    public double push(double v) {
        if (Math.abs(output) >= Math.abs(v) && Math.signum(dydx) == Math.signum(output)) {
            dydx = 0;
        }
        var change = v - output - dydx;
        if ((fallUp || output > v && output > 0 || output < v && output < 0) && Math.abs(change) > maxd2ydx2) {
            change = Math.signum(change) * maxd2ydx2;
        }
        output += dydx += change;
        if (output > v && dydx > 0 || output < v && dydx < 0) {
            output = v;
        }
        return output;
    }

    @Override
    public double get() {
        return output;
    }

    @Override
    public void reset() {
        output = dydx = 0;
    }

}
