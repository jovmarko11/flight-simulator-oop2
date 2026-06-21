package time;

public interface InactivityListener {
    void onWarning(long secondsLeft);
    void onLimitReached();
}
