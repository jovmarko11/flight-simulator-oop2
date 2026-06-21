package time;

public class InactivityTimer implements ClockObservable {


    private static final long TICKS_PER_SEC = 5;
    private static final long LIMIT   = 60 * TICKS_PER_SEC;  // 300
    private static final long WARNING = 55 * TICKS_PER_SEC;  // 275

    private boolean paused = false;

    private long inactiveSeconds = 0;
    private final InactivityListener listener;

    public InactivityTimer(InactivityListener listener) {
        this.listener = listener;
        Clock.getInstance().addObserver(this);
    }

    @Override
    public synchronized void onTick(long sec) {
        if (paused) return;
        inactiveSeconds++;

        if (inactiveSeconds >= LIMIT) {
            listener.onLimitReached();
        } else if (inactiveSeconds >= WARNING) {
            listener.onWarning((long) Math.ceil((LIMIT - inactiveSeconds) / (double) TICKS_PER_SEC));
        }
    }

    public synchronized void pause() { paused = true; }
    public synchronized void resume() { paused = false; }
    public synchronized void reset() {
        inactiveSeconds = 0;
    }
}
