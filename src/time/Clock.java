package time;

import java.util.ArrayList;
import java.util.List;

public class Clock {
    private static volatile Clock instance;
    private volatile long sec = 0;
    private volatile boolean active = false;

    private List<ClockObservable> observables = new ArrayList<>();

    private Clock() {}

    public static Clock getInstance() {
        if (instance == null){
            synchronized (Clock.class) {
                if (instance == null){
                    instance = new Clock();
                }
            }
        }
        return instance;
    }

    private final Thread clockThread = new Thread(){
        @Override
        public void run(){
            while (!Thread.interrupted()){
                try{
                    Thread.sleep(200);
                    if (active){
                        sec++;
                        observeAll();
                    }
                }
                catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    };

    private synchronized void observeAll(){
        for (ClockObservable o : observables){
            o.onTick(sec);
        }
    }

    public synchronized void addObserver(ClockObservable o){ observables.add(o); }
    public synchronized void removeObserver(ClockObservable o){ observables.remove(o); }

    public void activate(){
        if (!clockThread.isAlive()){
            clockThread.setDaemon(true);
            clockThread.start();
            active = true;
        }
    }

    public void pause(){ active = false; }
    public void reset() { sec = 0; }
    public void resume() { active = true; }

    public long getSec(){ return sec; }
}
