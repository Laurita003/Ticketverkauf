

//import java.awt.print.Book
import java.util.concurrent.*;

/**
 * class representant a client that want to buy tickets
 */
public class Kunde implements Callable<Boolean>{
    private final Ticketverkauf ticketverkauf;
    private final int event;
    private final int tickets;
    private final String name;


    /**
     * constructor
     */

    public Kunde(Ticketverkauf ticketverkauf, int event, int tickets, String name){
        this.ticketverkauf = ticketverkauf;
        this.event = event;
        this.tickets = tickets;
        this.name = name;
    }

    /**
      * Method called by the executorsevice
      */

    @Override
    public  Boolean call(){
        Thread.currentThread().setName(name);

        System.out.println(name + " try to buy " + tickets +
                            " tickets for the event " + event);

        boolean result = ticketverkauf.kaufen(event, tickets);

        if(result){
            System.out.println("success: " + name +
                                " has bought " + tickets +
                                " tickets for the event " + event);
        }
        else{
            System.out.println("loss: " + name +
                                " couldn't buy " + tickets +
                                " for the event " + event);
        }
        return  result;
    }

    // Getters
    public String getName(){return name;}
    public int getEvent(){return event;}
    public int getTickets(){return tickets;}

}