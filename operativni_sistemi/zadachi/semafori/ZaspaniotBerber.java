//package semafori;
/*Заспаниот бербер Problem 2 (2 / 3)

Потребно е да го синхронизирате сценариото на работа на една берберница. Берберницата има само еден бербер кој спие се додека не се насоберат 5 клиенти. Кога ќе се насоберат 5 клиенти, петтиот го буди берберот и тој започнува да ги потстрижува. Штом берберот ќе ги потстриже сите клиенти што чекаат, тој повторно заспива.

Кога берберот е буден, тој најпрво повикува еден клиент да влезе во берберницата. Кога клиентот ќе биде присутен, започнува процесот на потстрижување. Откако клиентот е потстрижен, тој плаќа и ја напушта берберницата, по што може да влезе нареден клиент. Доколку нема нареден клиент, берберот заспива. Доколку берберот спие, клиентите треба да чекаат додека не се соберат 5 од нив, и петтиот треба да го разбуди берберот.

Во почетниот код кој е даден, дефинирани се класите Barber и Customer, кои ги симболизираат берберот и клиентите. Има само една инстанца од класата Barber кај која методот execute() се повикува онолку пати колку што има клиенти и повеќе инстанци од класата Customer во кои методот execute() се повикува само еднаш.

Вашата задача е да ги имплементирате методите execute() од класата Barber и Customer според претходно опшаното сценарио.

Во имплементацијата, можете да ги користите следните методи од веќе дефинираната променлива state:

    state.customerArrived()
        Го симболизира пристигнувањето на клиент пред берберницата. Се повикува од клиентот, по неговото пристигнување.
    state.barberWakeUp()
        Го симболизира будењето на берберот. Се повикува од берберот кога тој се буди.
        Доколку берберот е веќе рабуден, повикот ќе фрли исклучок.
    state.barberCallCustomer()
        Го симболизира повикувањето на клиентот од страна на берберот. Се повикува кај берберот.
        Доколку нема пристигнато клиент, овој повик ќе фрли исклучок.
    state.customerEntry()
        Го симболизира влегувањето на клиентот во берберницата. Се повикува кај клиентот.
        Доколку нема пристигнато клиент, или нема повик од берберот, овој повик ќе фрли исклучок.
    state.cutHair()
        Го симболизира потстрижувањето на клиентот од страна на берберот. Се повикува кај берберот.
        Доколку нема присутен клиент во берберницата, овој повик ќе фрли исклучок.
    state.customerPay()
        Го симболизира плаќањето на клиентот и напуштањето на берберницата. Се повикува кај клиентот.
    state.barberGoToSleep()
        Го симболизира заспивањето на берберот, кога нема присутни клиенти. Се повикува кај берберот.
        Доколку берберот веќе спие или ако има клиенти што чекаат, овој повик ќе фрли исклучок.

Претходно назначените методи служат за проверка на точноста на сценариото и не смеат да бидат променети и мораат да бидат повикани.

Вашата задача е да ги имплементирате методите Barber.execute() и Customer.execute() и init().*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class TemplateNumRunsAndNumInstances {

    //TODO: definirajte gi semaforite i ostanatite promenlivi ovde (mora site da se static)
	
	static Semaphore canWakeUp = null;	
	static Semaphore canEnter = null;	
	static Semaphore canCut = null;	
	static Semaphore canPay = null;	
	static Semaphore canLeave = null;
	static Lock lock = null;
	static int c;
	
    /**
     * Metod koj treba da gi inicijalizira vrednostite na semaforite i
     * ostanatite promenlivi za sinhronizacija.
     *
     *
     * TODO: da se implementira
     *
     */
    public static void init(int numBarbers) {
    	
    	canWakeUp = new Semaphore(0);
    	canEnter = new Semaphore(0);
    	canCut = new Semaphore(0);
    	canPay = new Semaphore(0);
    	canLeave = new Semaphore(0);
    	lock = new ReentrantLock();
    	c = 0;
    	
    }

    static class Barber extends TemplateThread {

        public int barberId;

        public Barber(int numRuns, int barberId) {
            super(numRuns);
            this.barberId = barberId;
        }

        /**
         * Da se implementira odnesuvanjeto na berberot spored baranjeto na
         * zadacata.
         *
         *
         * TODO: da se implementira
         *
         */
        public void execute() throws InterruptedException {            
            // koga 5tiot klient ke notificira, berberot treba da se razbudi
        	//System.out.println("BarberStart");
        	lock.lock();
        	if(c == 0) {
            	lock.unlock();
        		canWakeUp.acquire();
        		state.barberWakeUp();
        		//System.out.println("barberWakeUp");
        	}
        	else {
        		lock.unlock();
        	}
            // koga klientot ke pristigne, go vika klientot da vleze
            state.barberCallCustomer();
            //System.out.println("barberCallCustomer");
            canEnter.release();
            canCut.acquire();
            // koga klientot ke vleze, go potstrizuva
            state.cutHair();
            //System.out.println("barberCutHair");
            canPay.release();
            canLeave.release();
            // proveruva dali ima klienti koi cekaat, ako nema, zaspiva
            lock.lock();
            c--;
            if(c == 0) {            	
                state.barberGoToSleep();    
                //System.out.println("barberGoToSleep");
            }
            lock.unlock();
            //System.out.println("BarberEND");
        }
    }

    static class Consumer extends TemplateThread {

        public Consumer(int numRuns) {
            super(numRuns);
        }

        /**
         * Da se implementira odnesuvanjeto na ucesnikot spored uslovite na
         * zadacata.
         */
        public void execute() throws InterruptedException {
        	// dokolku e pettiot, go budi berberot
        	//System.out.println("CustomerSTART");
            state.customerArrived();
            //System.out.println("customerArrived");
            //System.out.println("c= " + c);
            lock.lock();
            
            c++;
            //System.out.println("c= " + c);
            if(c == 5) {
            	canWakeUp.release();
            	//System.out.println("inside IF canWakeUp Release");
            }
            lock.unlock();
            canEnter.acquire();
            // koga ke bide povikan, vleguva            
            state.customerEntry();
            //System.out.println("customerEntry");
            canCut.release();
            canPay.acquire();
            // klientot vlegol vo berbernicata i e spremen za potstrizuvanje            
            // koga ke go potstrizat, plakja
            state.customerPay();
            //System.out.println("Cutomer PAY");
            canLeave.acquire();
            //System.out.println("CustomerEND");
        }
    }
    //<editor-fold defaultstate="collapsed" desc="This is the template code" >
    static State state;

    static class State {

        private static final Random RANDOM = new Random();
        private static final int RANDOM_RANGE = 5;
        private final int numBarbers;
        private boolean barberWaked[];

        public State(int numBarbers) {
            this.numBarbers = numBarbers;
            barberWaked = new boolean[numBarbers];
        }
        private int arrivedCustomers = 0;
        private int calledCustomers = 0;
        private int maxCuttings = 0;
        private int numCuttings = 0;

        public synchronized void customerArrived() throws RuntimeException {
        	
            log(null, "customer arrived");
            arrivedCustomers++;
        }

        public synchronized void barberWakeUp() throws RuntimeException {
            Barber b = (Barber) Thread.currentThread();
            if (barberWaked[b.barberId]) {
                PointsException e = new PointsException(5, "Berberot e veke buden i nema potreba da se razbudi.");
                log(e, null);
            } else {
                log(null, "the barber is waked up");
                barberWaked[b.barberId] = true;
            }
        }

        public synchronized void barberCallCustomer() throws RuntimeException {
            log(null, "the barber calls the customer");
            if (arrivedCustomers <= 0) {
                PointsException e = new PointsException(5, "Brojot na klienti koi cekaat e 0 i nema koj da bide povikan.");
                log(e, null);
            }
            calledCustomers++;
        }

        public synchronized void customerEntry() throws RuntimeException {
            log(null, "customer sits in the chair");
            if (arrivedCustomers <= 0) {
                PointsException e = new PointsException(5, "Brojot na klienti koi cekaat e 0 i nema koj da vleze.");
                log(e, null);
            }
            if (calledCustomers <= 0) {
                PointsException e = new PointsException(5, "Nema povikano klient i ne moze da vleze.");
                log(e, null);
            }
            arrivedCustomers--;
            calledCustomers--;

            numCuttings++;
        }

        public void cutHair() throws RuntimeException {
            synchronized (this) {
                if (numCuttings <= 0) {
                    PointsException e = new PointsException(5, "Nema prisuten klient za potstrizuvanje");
                    log(e, null);
                }

                log(null, "berber cuts the customer hair");
            }
            try {
                int r;
                synchronized (this) {
                    r = RANDOM.nextInt(RANDOM_RANGE);
                }
                Thread.sleep(r);
            } catch (Exception e) {
                //do nothing
            }
            synchronized (this) {
                if (numCuttings <= 0) {
                    PointsException e = new PointsException(5, "Brojot na klienti koi se strizat e 0 i nema koj da izleze.");
                    log(e, null);
                }
                numCuttings--;
            }

        }

        public synchronized void customerPay() throws RuntimeException {
            log(null, "customer is paying and leaving the shop");


        }

        public synchronized void barberGoToSleep() throws RuntimeException {
            Barber b = (Barber) Thread.currentThread();
            if (!barberWaked[b.barberId]) {
                PointsException e = new PointsException(5, "Berberite veke spijat i ne moze da se prezaspijat.");
                log(e, null);
            }
            if (arrivedCustomers > 0) {
                PointsException e = new PointsException(5, "Seuste ima klienti koi cekaat i berberot ne moze da odi na spienje.");
                log(e, null);
            }
            log(null, "all barbers go to sleap");
            barberWaked[b.barberId] = false;
        }
        private List<String> actions = new ArrayList<String>();
        private List<PointsException> exceptions = new ArrayList<PointsException>();

        private synchronized void log(PointsException e, String action) {
            TemplateThread t = (TemplateThread) Thread.currentThread();
            if (e == null) {
                actions.add(t.toString() + "\t(a): " + action);
            } else {
                t.setException(e);
                actions.add(t.toString() + "\t(e): " + e.getMessage());
            }
        }

        public synchronized void printLog() {
            System.out.println("Poradi konkurentnosta za pristap za pecatenje, mozno e nekoja od porakite da ne e na soodvetnoto mesto.");
            System.out.println("Log na izvrsuvanje na akciite:");
            System.out.println("=========================");
            System.out.println("tip\tid\titer\takcija/error");
            System.out.println("=========================");
            for (String l : actions) {
                System.out.println(l);
            }
        }

        public void printStatus() {
            if (!TemplateThread.hasException) {
                int poeni = 25;
                if (PointsException.getTotalPoints() == 0) {
                    System.out.println("Procesot e uspesno sinhroniziran. Osvoeni 25 poeni.");
                } else {
                    poeni -= PointsException.getTotalPoints();
                    PointsException.printErrors();
                    System.out.println("Osvoeni poeni: " + poeni);
                }

            } else {
                System.out.println("Procesot ne e sinhroniziran spored uslovite na zadacata");
                printLog();
                System.out.println("====================================================");
                PointsException.printErrors();
                System.out.println("Maksimum Poeni: " + (25 - PointsException.getTotalPoints()));
            }

        }
    }

    abstract static class TemplateThread extends Thread {

        static boolean hasException = false;
        int numRuns = 1;
        public int iteration = 0;
        private Exception exception = null;

        public TemplateThread(int numRuns) {
            this.numRuns = numRuns;
        }

        abstract void execute() throws InterruptedException;

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
                return String.format("%s\t%d\t%d", "" + current.getClass().getSimpleName().charAt(0), getId(), iteration);
            } else {
                return String.format("%s\t%d\t", "" + current.getClass().getSimpleName().charAt(0), getId());
            }
        }
    }

    static class PointsException extends RuntimeException {

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
            System.out.println("Gi imate slednite greski: ");
            for (Map.Entry<String, PointsException> e : exceptions.entrySet()) {
                System.out.println(String.format("[%s] : (-%d)", e.getKey(), e.getValue().getPoints()));
            }
        }

        public int getPoints() {
            return points;
        }
    }

    public static void main(String[] args) {
        try {
        	
            start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void start() throws Exception {
        Scanner s = new Scanner(System.in);
        int brBarbers = s.nextInt();
        int brKonzumeri = s.nextInt();
        int numBarberRuns = s.nextInt();
        int numCustomerRuns = s.nextInt();
        init(brBarbers);

        state = new State(brBarbers);
        HashSet<Thread> threads = new HashSet<Thread>();

        for (int i = 0; i < brBarbers; i++) {
            Barber prod = new Barber(numBarberRuns, i);
            threads.add(prod);
            prod.start();
            Consumer c = new Consumer(numCustomerRuns);
            threads.add(c);
            c.start();
        }

        for (int i = 0; i < brKonzumeri / 2 - brBarbers; i++) {
            Consumer c = new Consumer(numCustomerRuns);
            threads.add(c);
            c.start();
        }
        try {
            Thread.sleep(50);
        } catch (Exception e) {
            //do nothing
        }
        for (int i = 0; i < brKonzumeri / 2; i++) {
            Consumer c = new Consumer(numCustomerRuns);
            threads.add(c);
            c.start();
        }


        for (Thread t : threads) {
            t.join(1000);
        }

        for (Thread t : threads) {
            if (t.isAlive()) {
                t.interrupt();
            }
        }

        state.printStatus();
    }
    //</editor-fold>
}
