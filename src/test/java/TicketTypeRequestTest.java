import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketTypeRequestTest {

    @Test
    void verifyNumberOfTickets() {
        var ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        assertEquals(2, ticketTypeRequest.getNoOfTickets());
    }

    // Business Rules
    // There are three types of tickets i.e. Infant, Child and Adult
    @Test
    void verifyAdultTicketType() {
        var ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        assertEquals(TicketTypeRequest.Type.ADULT, ticketTypeRequest.getTicketType());
    }

    @Test
    void verifyChildTicketType() {
        var ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        assertEquals(TicketTypeRequest.Type.CHILD, ticketTypeRequest.getTicketType());
    }

    @Test
    void verifyInfantTicketType() {
        var ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        assertEquals(TicketTypeRequest.Type.INFANT, ticketTypeRequest.getTicketType());
    }

    // Calculations for the requested ticket prices
    @Test
    void calculatePriceOfOneAdultTicket() {
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        assertEquals(20, ticketTypeRequest.getNoOfTickets() * 20);
    }

    @Test
    void calculatePriceOfTwentyAdultTickets() {
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 20);
        assertEquals(400, ticketTypeRequest.getNoOfTickets() * 20);
    }

    @Test
    void calculatePriceOfOneChildTicket() {
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        assertEquals(10, ticketTypeRequest.getNoOfTickets() * 10);
    }

    @Test
    void calculatePriceOfFiveChildTickets() {
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 5);
        assertEquals(50, ticketTypeRequest.getNoOfTickets() * 10);
    }

    @Test
    void calculatePriceOfOneInfantTicket() {
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        assertEquals(0, ticketTypeRequest.getNoOfTickets() * 0);
    }

    @Test
    void calculatePriceForTwoAdultsOneChild() {
        TicketTypeRequest adultTickets = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest childTickets = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        int adultPrice = adultTickets.getNoOfTickets() * 20;
        int childPrice = childTickets.getNoOfTickets() * 10;
        int finalPrice = adultPrice + childPrice;
        assertEquals(50, finalPrice);
    }

    @Test
    void calculatePriceForTwoAdultsTwoInfants() {
        TicketTypeRequest adultTickets = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest infantTickets = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2);
        int adultPrice = adultTickets.getNoOfTickets() * 20;
        int infantPrice = infantTickets.getNoOfTickets() * 0;
        int finalPrice = adultPrice + infantPrice;
        assertEquals(40, finalPrice);
    }
}
