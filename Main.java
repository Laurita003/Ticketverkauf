

import java.util.*;
import java.util.concurrent.*;
/**
 * main method for testing scenarios
 */
public class Main {
    /**
     * Scenario 1
     */

    public static void scenario1() throws Exception{
        System.out.println("\n=== SCENARIO 1 ===");


        //1. event: initial 5, max 10
        Ticketverkauf tv = new Ticketverkauf(new int[]{5}, new int[]{10});

        // clients creation

        Kunde k1 = new Kunde(tv, 0, 3, "K1");
        Kunde k2 = new Kunde(tv, 0, 4, "K2");

        //execution
        ExecutorService executor = Executors.newFixedThreadPool(2);

        //k1 buy 3 trickets
        Future<Boolean> f1 = executor.submit(k1);
        f1.get(); //wait the end.

        //get the restant 5 Tickets
        tv.freigeben(0, 5);

        //K2 buy 4 tickets
        Future<Boolean> f2 = executor.submit(k2);
        f2.get();

        tv.printStatus();
        executor.shutdown();
    }

    /**
     * Schenario 2
     */

    public static void scenario2() throws Exception{
        System.out.println("\n ===== SCENARIO 2 =====");

        Ticketverkauf tv = new  Ticketverkauf(new int[]{5}, new int[]{10});

        ExecutorService executor =Executors.newFixedThreadPool(2);

        //the 2 client at the same time
        Kunde k1 = new Kunde(tv, 0, 3, "K1");
        Kunde k2 = new Kunde(tv, 0, 4, "K2");

        Future<Boolean> f1 = executor.submit(k1);
        Future<Boolean> f2 = executor.submit(k2);

        //wait 5sec
        Thread.sleep(5000);

        //get the restant 5 tickets
        tv.freigeben(0, 5);

        //wait the results
        f1.get();
        f2.get();

        tv.printStatus();
        executor.shutdown();
    }

    /**
     * Scenario 5 exple of threadpool with 4 threads
     */

    public static void scenario5() throws Exception{
        System.out.println("\n=== SCENARIO 5 ===");

        //1st event: 1000 tickets, all initially disponible
        Ticketverkauf tv = new  Ticketverkauf(new int[]{1000}, new int[]{1000});

        //Threadpool with 4 threads max
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Future<Boolean>> results = new ArrayList<>();
        int falseCount = 0;

        //creation of 1200 clients
        for(int i = 0; i < 1200 ; i++){
            Kunde kunde = new Kunde(tv, 0, 1, "K" + i);
            results.add(executor.submit(kunde));
        }

        //collection of results
        for(Future<Boolean> future : results){
            if(!future.get()){
                falseCount++;
            }
        }

        System.out.println("Number of false: " + falseCount + "of 1200 tentative");
        tv.printStatus();
        executor.shutdown();
    }

    public static void main(String[] args) {
        try{
            scenario1();
            scenario2();
            scenario5();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
