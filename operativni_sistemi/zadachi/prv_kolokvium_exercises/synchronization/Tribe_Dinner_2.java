//package exam.sychronization;

import java.util.*;
import java.util.concurrent.Semaphore;

public class SeptemberTribeDinner {

	static Semaphore checkAndFill = null;
	static Semaphore callCook = null;
	static Semaphore foodCooked = null;
	static Semaphore goEat = null;
	
    public static void init() {
    	checkAndFill = new Semaphore(1);
    	callCook = new Semaphore(0);
    	foodCooked = new Semaphore(0);
    	goEat = new Semaphore(4);
    }

    public static class TribeMember extends TemplateThread {

        public TribeMember(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
			// Vashiot kod ovde
        	checkAndFill.acquire();
        	if(state.isPotEmpty()) {
        		callCook.release();
        		foodCooked.acquire();
        	}
        	state.fillPlate();
        	checkAndFill.release();
        	goEat.acquire();
        	state.eat();
        	goEat.release();
        }

    }

    public static class Chef extends TemplateThread {

        public Chef(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
			// Vashiot kod ovde
        	callCook.acquire();
        	state.cook();
        	foodCooked.release();
        }

    }

    static SeptemberTribeDinnerState state = new SeptemberTribeDinnerState();

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            run();
        }
    }

    public static void run() {
        try {
            int numRuns = 1;
            int numIterations = 150;

            HashSet<Thread> threads = new HashSet<Thread>();

            for (int i = 0; i < numIterations; i++) {
                TribeMember h = new TribeMember(numRuns);
                threads.add(h);
            }

            Chef chef = new Chef(10);
            threads.add(chef);

            init();

            ProblemExecution.start(threads, state);
            //System.out.println(new Date().getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



class SeptemberTribeDinnerState extends AbstractState {

    private static final int EMPTY_POT = 0;

    private static final String _10_JADENJETO_NE_E_PARALELIZIRANO = "jadenjeto ne e paralelizirano. Site jadat eden po eden";

    private static final String _10_DVAJCA_ISTOVREMENO_PROVERUVAAT = "Dvajca istovremeno proveruvaat dali kazanot e prazen. Maksimum eden e dozvoleno.";
    private static final String _7_DVAJCA_ISTOVREMENO_POLNAT = "Dvajca istovremeno zemaat hrana od kazanot. Maksimum eden e dozvoleno.";
    private static final String _5_NE_MOZE_DA_POLNI_OD_PRAZEN_KAZAN = "Ne moze da se polni od kazan. Treba da se povika 'state.cook()'";
    private static final String _5_NE_MOZE_DA_SE_GOTVI_VO_KAZAN_KOJ_NE_E_PRAZEN = "Ne moze da se gotvi vo kazan koj ne e prazen";
    private static final String _7_NEMA_MESTO_NA_TRPEZATA = "Trpezata e polna. Nema mesto na trpezata za poveke od cetvorica. ";
    private static final String _7_NEMA_ZEMENO_HRANA = "NEMA ZEMENO HRANA";
    private static final String _7_POLNI_OD_PRAZEN_KAZAN = "POLNI OD PRAZEN KAZAN";

    private static final int POT_CAPACITY = 15;

    private BoundCounterWithRaceConditionCheck platesLeft = new BoundCounterWithRaceConditionCheck(
            0, null, 0, null, 0, 10, _7_POLNI_OD_PRAZEN_KAZAN);

    private BoundCounterWithRaceConditionCheck checks = new BoundCounterWithRaceConditionCheck(
            0, 1, 10, _10_DVAJCA_ISTOVREMENO_PROVERUVAAT, null, 0, null);

    private BoundCounterWithRaceConditionCheck fills = new BoundCounterWithRaceConditionCheck(
            0, 1, 7, _7_DVAJCA_ISTOVREMENO_POLNAT, 0, 5,
            _5_NE_MOZE_DA_POLNI_OD_PRAZEN_KAZAN);

    private BoundCounterWithRaceConditionCheck ready = new BoundCounterWithRaceConditionCheck(
            0, null, 0, null, 0, 7, _7_NEMA_ZEMENO_HRANA);

    private BoundCounterWithRaceConditionCheck eat = new BoundCounterWithRaceConditionCheck(
            0, 4, 7, _7_NEMA_MESTO_NA_TRPEZATA, null, 0, null);

    public SeptemberTribeDinnerState() {

    }

    /**
     * Maksimum 1 proveruva.
     *
     * @return
     * @throws RuntimeException
     */
    public boolean isPotEmpty() throws RuntimeException {
        log(checks.incrementWithMax(), "proverka dali ima hrana vo kazanot");
        boolean res = platesLeft.getValue() == 0;
        log(checks.decrementWithMin(), null);
        return res;
    }

    /**
     * Maksimum 1 zema paralelno. Ne smee da se povika od prazen kazan.
     *
     * @throws RuntimeException
     */
    public void fillPlate() throws RuntimeException {
        log(fills.incrementWithMax(), "zemanje na hrana");
        log(platesLeft.decrementWithMin(false), null);
        log(fills.decrementWithMin(), null);
        ready.incrementWithMax(false);
    }

    /**
     * Maksimum 4 jadat paralelno. Ne smeat da jadat eden po eden.
     *
     * @throws RuntimeException
     */
    public void eat() throws RuntimeException {
        log(ready.decrementWithMin(false), null);
        log(eat.incrementWithMax(false), "jadenje");
        Switcher.forceSwitch(15);
        log(eat.decrementWithMin(false), null);

    }

    /**
     * Se povikuva od gotvacot. Ne smee da se povika koga kazanot ne e prazen.
     *
     * @throws RuntimeException
     */
    public void cook() throws RuntimeException {
        log(platesLeft.assertEquals(EMPTY_POT, 5,
                _5_NE_MOZE_DA_SE_GOTVI_VO_KAZAN_KOJ_NE_E_PRAZEN), null);
        Switcher.forceSwitch(10);
        platesLeft.setValue(POT_CAPACITY);
    }

    @Override
    public void finalize() {
        if (eat.getMax() == 1) {
            logException(new PointsException(10,
                    _10_JADENJETO_NE_E_PARALELIZIRANO));
        }
    }

}
abstract class AbstractState {

    /**
     * Method called after threads ended their execution to validate the
     * correctness of the scenario
     */
    public abstract void finalize();

    /**
     * List of logged actions
     */
    private List<String> actions = new ArrayList<String>();

    /**
     *
     * @return if the current thread is instance of TemplateThread it is
     *         returned, and otherwise null is returned
     */
    protected TemplateThread getThread() {
        Thread current = Thread.currentThread();
        if (current instanceof TemplateThread) {
            TemplateThread t = (TemplateThread) current;
            return t;
        } else {
            return null;
        }
    }

    /**
     * Log this exception or action
     *
     * @param e
     *            occurred exception (null if no exception)
     * @param action
     *            Description of the occurring action
     */
    public synchronized void log(PointsException e, String action) {
        TemplateThread t = (TemplateThread) Thread.currentThread();
        if (e != null) {
            t.setException(e);
            actions.add(t.toString() + "\t(e): " + e.getMessage());
            throw e;
        } else if (action != null) {
            actions.add(t.toString() + "\t(a): " + action);
        }
    }

    /**
     * Logging exceptions
     *
     * @param e
     */
    protected synchronized void logException(PointsException e) {
        Thread t = Thread.currentThread();
        if (e != null) {
            if (t instanceof TemplateThread) {
                ((TemplateThread) t).setException(e);
            }
            TemplateThread.hasException = true;
            actions.add("\t(e): " + e.getMessage());
            throw e;
        }
    }

    /**
     * Printing of the actions and exceptions that has occurred
     */
    public synchronized void printLog() {
        System.out
                .println("Poradi konkurentnosta za pristap za pecatenje, mozno e nekoja od porakite da ne e na soodvetnoto mesto.");
        System.out.println("Log na izvrsuvanje na akciite:");
        System.out.println("=========================");
        System.out.println("tip\tid\titer\takcija/error");
        System.out.println("=========================");
        for (String l : actions) {
            System.out.println(l);
        }
    }

    /**
     * Prints the status of the execution, with the exceptions that has occur
     */
    public void printStatus() {
        try {
            finalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TemplateThread.hasException) {
            int poeni = 25;
            if (PointsException.getTotalPoints() == 0) {
                System.out
                        .println("Procesot e uspesno sinhroniziran. Osvoeni 25 poeni.");
            } else {
                poeni -= PointsException.getTotalPoints();
                PointsException.printErrors();
                System.out.println("Maksimalni osvoeni poeni: " + poeni);
            }

        } else {
            System.out
                    .println("Procesot ne e sinhroniziran spored uslovite na zadacata");
            printLog();
            System.out
                    .println("====================================================");
            PointsException.printErrors();
            int total = (25 - PointsException.getTotalPoints());
            if (total < 0) {
                total = 0;
            }
            System.out.println("Maksimum Poeni: " + total);
        }

    }
}

class PointsException extends RuntimeException {

    private static HashMap<String, PointsException> exceptions = new HashMap<String, PointsException>();
    private int points;

    public PointsException(int points, String message) {
        super(message);
        this.points = points;
        exceptions.put(message, this);
    }

    public static int getTotalPoints() {
        int sum = 0;
        for (PointsException e : exceptions.values()) {
            sum += e.getPoints();
        }
        return sum;
    }

    public static void printErrors() {
        if (!exceptions.isEmpty()) {
            System.out.println("Gi imate slednite greski: ");
            for (Map.Entry<String, PointsException> e : exceptions.entrySet()) {
                System.out.println(String.format("[%s] : (-%d)", e.getKey(), e
                        .getValue().getPoints()));
            }
        }
    }

    public int getPoints() {
        return points;
    }
}

class Switcher {
    private static final Random RANDOM = new Random();

    /*
     * This method pauses the current thread i.e. changes its state to be
     * Blocked. This should force thread switch if there are threads waiting
     */
    public static void forceSwitch(int range) {
        try {
            Thread.sleep(RANDOM.nextInt(range));
        } catch (InterruptedException e) {
        }
    }
}

abstract class TemplateThread extends Thread {

    static boolean hasException = false;
    public int iteration = 0;
    protected Exception exception = null;
    int numRuns = 1;

    public TemplateThread(int numRuns) {
        this.numRuns = numRuns;
    }

    public abstract void execute() throws InterruptedException;

    @Override
    public void run() {
        try {
            for (int i = 0; i < numRuns&&!hasException; i++) {
                execute();
                iteration++;

            }
        } catch (InterruptedException e) {
            // Do nothing
        } catch (Exception e) {
            exception = e;
            e.printStackTrace();
            hasException = true;
        }
    }

    public void setException(Exception exception) {
        this.exception = exception;
        hasException = true;
    }

    @Override
    public String toString() {
        Thread current = Thread.currentThread();
        if (numRuns > 1) {
            return String.format("[%d]%s\t%d\t%d", new Date().getTime(), ""
                            + current.getClass().getSimpleName().charAt(0), getId(),
                    iteration);
        } else {
            return String.format("[%d]%s\t%d\t", new Date().getTime(), ""
                    + current.getClass().getSimpleName().charAt(0), getId());
        }
    }
}

class BoundCounterWithRaceConditionCheck {

    private static final int RACE_CONDITION_POINTS = 25;
    private static final String RACE_CONDITION_MESSAGE = "Race condition occured";

    private int value;
    private Integer maxAllowed;
    private Integer minAllowed;
    private int maxErrorPoints;
    private int minErrorPoints;
    private String maxErrorMessage;
    private String minErrorMessage;

    public static int raceConditionDefaultTime = 3;

    private int max;

    /**
     *
     * @param value
     */
    public BoundCounterWithRaceConditionCheck(int value) {
        super();
        this.value = value;
        this.max = value;
    }

    /**
     *
     * @param value
     *            initial value
     * @param maxAllowed
     *            upper bound of the value
     * @param maxErrorPoints
     *            how many points are lost with the max value constraint
     *            violation
     * @param maxErrorMessage
     *            message shown when the upper bound constrain is violated
     * @param minAllowed
     *            lower bound of the value
     * @param minErrorPoints
     *            how many points are lost with the min value constraint
     *            violation
     * @param minErrorMessage
     *            message shown when the lower bound constrain is violated
     */
    public BoundCounterWithRaceConditionCheck(int value, Integer maxAllowed,
                                              int maxErrorPoints, String maxErrorMessage, Integer minAllowed,
                                              int minErrorPoints, String minErrorMessage) {
        super();
        this.value = value;
        this.max = value;
        this.maxAllowed = maxAllowed;
        this.minAllowed = minAllowed;
        this.maxErrorPoints = maxErrorPoints;
        this.minErrorPoints = minErrorPoints;
        this.maxErrorMessage = maxErrorMessage;
        this.minErrorMessage = minErrorMessage;
    }

    /**
     *
     * @return the maximum value of the integer variable that occurred at some
     *         point of the execution
     */
    public int getMax() {
        return max;
    }

    /**
     *
     * @return the current value
     */
    public synchronized int getValue() {
        return value;
    }

    public synchronized void setValue(int value) {
        this.value = value;
    }

    /**
     * Throws exception when the val is different than the value of the counter.
     *
     * @param val
     * @param points
     * @param errorMessage
     * @return
     */
    public synchronized PointsException assertEquals(int val, int points,
                                                     String errorMessage) {
        if (this.value != val) {
            PointsException e = new PointsException(points, errorMessage);
            return e;
        } else {
            return null;
        }
    }

    public synchronized PointsException assertNotEquals(int val, int points,
                                                        String errorMessage) {
        if (this.value == val) {
            PointsException e = new PointsException(points, errorMessage);
            return e;
        } else {
            return null;
        }
    }

    /**
     * Testing for race condition. NOTE: there are no guarantees that the race
     * condition will be detected
     *
     * @return
     */
    public PointsException checkRaceCondition() {
        return checkRaceCondition(raceConditionDefaultTime,
                RACE_CONDITION_MESSAGE);
    }

    /**
     * Testing for race condition. NOTE: there are no guarantees that the race
     * condition will be detected, but higher the time argument is, the
     * probability for race condition occurrence is higher
     *
     * @return
     */
    public PointsException checkRaceCondition(int time, String message) {
        int val;

        synchronized (this) {
            val = value;
        }
        Switcher.forceSwitch(time);
        if (val != value) {
            PointsException e = new PointsException(RACE_CONDITION_POINTS,
                    message);
            return e;
        }
        return null;

    }

    public PointsException incrementWithMax() {
        return incrementWithMax(true);
    }

    public PointsException incrementWithMax(boolean checkRaceCondition) {
        if (checkRaceCondition) {
            PointsException raceCondition = checkRaceCondition();
            if (raceCondition != null) {
                return raceCondition;
            }
        }
        synchronized (this) {
            value++;

            if (value > max) {
                max = value;
            }
            if (maxAllowed != null) {
                if (value > maxAllowed) {
                    PointsException e = new PointsException(maxErrorPoints,
                            maxErrorMessage);
                    return e;
                }
            }
        }

        return null;
    }

    public PointsException decrementWithMin() {
        return decrementWithMin(true);
    }

    public PointsException decrementWithMin(boolean checkRaceCondition) {
        if (checkRaceCondition) {
            PointsException raceCondition = checkRaceCondition();
            if (raceCondition != null) {
                return raceCondition;
            }
        }

        synchronized (this) {
            value--;
            if (minAllowed != null) {
                if (value < minAllowed) {
                    PointsException e = new PointsException(minErrorPoints,
                            minErrorMessage);
                    return e;
                }
            }
        }
        return null;
    }

}

abstract class ProblemExecution {

    public static void start(HashSet<Thread> threads, AbstractState state)
            throws Exception {

        startWithoutDeadlock(threads, state);

        checkDeadlock(threads, state);
    }

    public static void startWithoutDeadlock(HashSet<Thread> threads,
                                            AbstractState state) throws Exception {

        // start the threads
        for (Thread t : threads) {
            t.start();
        }

        // wait threads to finish
        for (Thread t : threads) {
            t.join(1000);
        }

    }

    private static void checkDeadlock(HashSet<Thread> threads,
                                      AbstractState state) {
        // check for deadlock
        for (Thread t : threads) {
            if (t.isAlive()) {
                t.interrupt();
                if (t instanceof TemplateThread) {
                    TemplateThread tt = (TemplateThread) t;
                    tt.setException(new PointsException(25, "DEADLOCK"));
                }
            }
        }

        // print the status
        state.printStatus();
    }

}

/*
One tribe eats dinner together, from a big pot with limited number of meals. If there is food in the pot, every member will serve its self, and sits to eat. If the pot is empty, the chef is called to cook food to fill the whole pot, and everyone that hasn't eaten, waits for the cook to notify them that there is new food. The table has four seats, which means, maximum of four members can eat simultaniously. If there is no free space on the table, the members wait for someone to finish.

Your task is to synchronize the following scenario.

In the starter code, there are two classes defined: TribeMember and Chef. In the implementation, you should use the following methods from the already defined variable state:

state.isPotEmpty()
Returns boolean, symbolizes an empty pot.
Called from all TribeMembers
If more than one members checks, you will get an error message.
state.fillPlate()
Symbolizes taking food from the pot and filling your plate.
Called from all tribe members.
If the pot is empty, you will get an error.
If multiple tribe members fill their plate, you will get an error.
state.eat()
Symbolizes eating on the table.
Called from all tribe members.
If more than four members eat simultaniously, you will get an error.
If the method is called sequentially (one at a time), you will get an error.
state.cook()
Symbolizes cooking on behalf of the chef.
Called only from the chef.
If the method is called, and the pot has food, you will get an error.
За решавање на задачата, преземете го проектот со клик на копчето Starter file, отпакувајте го и отворете го со Eclipse или Netbeans.

Претходно назначените методи служат за проверка на точноста на сценариото и не смеат да бидат променети и мораат да бидат повикани.

Вашата задача е да го имплементирате методот execute() од класите TribeMember и Chef, кои се наоѓаат во датотеката SeptemberTribeDinnerSynchronization.java. При решавањето можете да користите семафори и монитори по ваша желба и нивната иницијализација треба да ја направите во init() методот.

При стартувањето на класата, сценариото ќе се повика 10 пати, со креирање на голем број инстанци од класата TribeMember и една инстанца од класата Chef. Кај сите TribeMember паралелно само еднаш ќе се повика нивниот execute() метод, додека пак Chef.execute() методот се повикува повеќе пати.
*/
