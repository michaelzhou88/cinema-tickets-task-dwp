import org.junit.jupiter.api.Test;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TicketServiceImplTest {

    TicketServiceImpl ticketService = new TicketServiceImpl();

    // Rejecting any invalid ticket purchase requests
    // Only a maximum of 20 tickets that can be purchased at a time
    @Test
    void purchasingMoreThanTwentyTicketsShouldThrowException() {
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 21);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(3L, ticketTypeRequest));
        assertEquals("Only a maximum of 20 tickets can be purchased at a time", exception.getMessage());
    }

    // Child and Infant tickets cannot be purchased without purchasing an Adult Ticket
    @Test
    void purchaseChildTicketWithoutAdultShouldThrowException() {
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(3L, ticketTypeRequest));
        assertEquals("At least one adult needs to be present!", exception.getMessage());
    }

    @Test
    void purchaseInfantTicketWithoutAdultShouldThrowException() {
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(3L, ticketTypeRequest));
        assertEquals("At least one adult needs to be present!", exception.getMessage());
    }

    // Testing Assumptions
    // All accounts with an id greater than zero are valid. They also have sufficient funds to pay for any no of tickets.
    @Test
    void AccountIdOfZeroShouldThrowException() {
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(0L));
        assertEquals("Account ID is invalid", exception.getMessage());
    }

    @Test
    void negativeAccountIdShouldThrowException() {
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(-1L));
        assertEquals("Account ID is invalid", exception.getMessage());
    }

    // Calculate the correct number of seats to reserve and make a seat reservation request to the 'SeatReservationService'
    // Infant do not need allocating a seat
    @Test
    void makePaymentForOneAdult() {
        TicketPaymentServiceImpl ticketPaymentServiceMock = mock(TicketPaymentServiceImpl.class);
        ticketPaymentServiceMock.makePayment(5L, 20);
        verify(ticketPaymentServiceMock).makePayment(5L, 20);
    }

    @Test
    void makeSeatReservationForTwoAdults() {
        SeatReservationServiceImpl seatReservationService = mock(SeatReservationServiceImpl.class);
        seatReservationService.reserveSeat(6L, 2);
        verify(seatReservationService).reserveSeat(6L, 2);
    }
}
