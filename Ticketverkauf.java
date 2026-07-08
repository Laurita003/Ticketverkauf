
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Ticketverkauf
{
    //state variables
    private final int n; //number of events
    private final int[] max_tickets;
    private final int[] available_tickets;
    private final int[] sold_tickets;

    // for synchronisation
    private final  ReentrantLock[] locks; // lock key per event
    private final Condition[] conditions; // waiting condition per event

    // //awaiting queue
    // private final Map<Integer, Queue<WaitingClient>> waitingQueues;

    // //intern class for representation of a waiting client
    // private  static class WaitingClient
    // {
    //     final int required_tickets;
    //     final Thread thread;

    //     public WaitingClient(int required_tickets)
    //     {
    //         this.required_tickets = required_tickets;
    //         this.thread = Thread.currentThread();
    //     }
    // }

    /**
     * constructor
     * @param initial_tickets table of tickets that we have at the beginning
     * @param max_tickets table of all tickets per event
    */

    public Ticketverkauf (int[] initial_tickets, int[] max_tickets)
    {
        //parameter validation
        if(initial_tickets == null || max_tickets == null)
        {
            throw new IllegalArgumentException(" The Tables can't be null ");
        }
        if(initial_tickets.length != max_tickets.length)
        {
            throw  new IllegalArgumentException(" The tables must have the same length ");
        }

        this.n = initial_tickets.length;
        this.max_tickets = max_tickets.clone();
        this.available_tickets = initial_tickets.clone();
        this.sold_tickets = new int[n];

        //initialisationn of synchronisation structures

        this.locks = new ReentrantLock[n];
        this.conditions = new Condition[n];
        //this.waitingQueues = new HashMap<>();

        for(int i = 0; i < n; i++)
        {
            // validation: initial can't be bigger than max
            if(initial_tickets[i] < 0 || max_tickets[i] < 0){
                throw new IllegalArgumentException("the number of Tickets cannot be negative");
            }
            if(initial_tickets[i] > max_tickets[i]){
                throw new IllegalArgumentException("Initial Tickets cannot be bigger than the maximum");
            }
            locks[i] = new ReentrantLock(true);
            conditions[i] = locks[i].newCondition();
            //waitingQueues.put(i, new LinkedList<>());
        }
    }

    /**
     * Methods for buying Tickets
     * @param event eventindex
     * @param tickets number of tickets
     * @return true if buying successfull and false in other case
     */
    public boolean kaufen(int event, int tickets)
    {
        //rapid validation without lock
        if(event < 0 || event >= n || tickets <= 0){
            return false;
        }
        //lock for that event
        locks[event].lock();
        try{
            //validation with lock
            if(tickets > max_tickets[event]){
                return false; //impossible
            }
            //wait while there is no tickets
            while(available_tickets[event] < tickets){
                //verify if its possible in the future
                int total_potential = sold_tickets[event] + tickets;
                if(total_potential > max_tickets[event]){
                    return false;
                }
                //add to the waiting queue
                //waitingQueues.get(event).add(new WaitingClient(tickets));
                try{
                    //awaiting
                    conditions[event].await();
                    //remove from the waiting queue
                    //waitingQueues.get(event).remove(w -> w.thread == Thread.currentThread());
                } catch(InterruptedException e) {
                    //gestion of the interruption
                    Thread.currentThread().interrupt();
                    //remove from the waiting queue in case of interruption
                    //waitingQueues.get(event).remove(w -> w.thread == Thread.currentThread());
                    return false;
                }
            }

            //  buying tickets
            available_tickets[event] -= tickets;
            sold_tickets[event] += tickets;

            System.out.println(Thread.currentThread().getName() +
                            " has bought " + tickets +
                            "tickets for the event " + event +
                            ". Rest " + available_tickets[event]);
            return true;
        } finally{
            locks[event].unlock(); //always
        }
    }

    /**
     * More tickets
     * @param event eventindex
     * @param tickets number of tickets to get
     * @return true if successfull, false if not
     */

    public boolean freigeben(int event, int tickets){
        //validation without lock
        if(event < 0 || event >= n || tickets <= 0){
            return false;
        }

        //lock for that event
        locks[event].lock();
        try{
            //we cant go onto the max
            int total_in_circulation = sold_tickets[event] + available_tickets[event];
            if(total_in_circulation + tickets > max_tickets[event]){
                System.out.println("Error: impossible to get " + tickets +
                                    " tickets for the event " + event +
                                    ".");
                return false;
            }

            //get tickets
            available_tickets[event] += tickets;

            System.out.println("Liberation of " + tickets +
                                " tickets for the event " + event +
                                ". Now available: " + available_tickets[event]);
            //option 1: call all awaiting threads
            conditions[event].signalAll();
            //option 2: just call the ones, that are satisfiable
            // signalIfPossible(event);

            return true;
        }finally{
            locks[event].unlock(); //always
        }
    }

    /**
     * optional call method
     * only if the threads are now satisfable
     */

    // private void signalIfPossible(int event)
    // {
    //     Queue<WaitingClient> queue = waitingQueues.get(event);
    //     boolean signaled = false;

    //     //go through the waitingqueue
    //     for(WaitingClient client : queue)
    //     {
    //         if(client.required_tickets <= available_tickets[event])
    //         {
    //             //we could call this specifique thread
    //             //but with the java condition, we can't call a specific thread
    //             // then we use signalAll() in each case

    //             conditions[event].signal();
    //             signaled = true;
    //             break;
    //         }
    //     }

    //     //if noone is satisfiable and we still have tickets
    //     // we call in all Cases for a new verification

    //     if(!signaled && available_tickets[event] > 0)
    //     {
    //         conditions[event].signal();
    //     }
    // }


    //method for debugage

    public int getAvailableTickets(int event){
        if(event < 0 || event >= n) return -1;
        locks[event].lock();

        try{
            return available_tickets[event];
        }finally{
            locks[event].unlock();
        }
    }

    public int getSoldTickets(int event){
        if(event < 0 || event >= n) return -1;
        locks[event].lock();
        try{
            return sold_tickets[event];
        }finally{
            locks[event].unlock();
        }
    }

    public int getMaxTickets(int event){
        if(event < 0 || event >= n) return -1;
        return max_tickets[event];
    }

    // public int getWaitingCount(int event){
    //     if(event < 0 || event >= n) return -1;
    //     locks[event].lock();
    //     try{
    //         return waitingQueues.get(event).size();
    //     }finally
    //     {
    //         locks[event].unlock();
    //     }
    // }

    /**
     * System state
     */

    public void printStatus()
    {
        System.out.println("\n === SYSTEM STATE OF TICKETS ===");
        for(int i = 0; i < n; i++)
        {
            locks[i].lock();
            try
            {
                System.out.println("event: " + i +
                                    ", Disponible: " + available_tickets[i] +
                                    ", Sale: " + sold_tickets[i] +
                                    ", Max: " + max_tickets[i]);
            }finally
            {
                locks[i].unlock();
            }
        }
        System.out.println("=====================================\n");
    }
}
