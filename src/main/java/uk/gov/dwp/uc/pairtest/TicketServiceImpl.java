package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;


public class TicketServiceImpl implements TicketService {

    public static final int MAXIMUM_TICKET_LIMIT = 20;
    private int price;
    private boolean adultRequestPresent = false;

    /**
     * Should only have private methods other than the one below.
     */

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        if (accountId <= 0) {
            throw new InvalidPurchaseException("Account ID is invalid");
        } else {
            TicketPaymentServiceImpl ticketPaymentService = new TicketPaymentServiceImpl();
            SeatReservationServiceImpl seatReservationService = new SeatReservationServiceImpl();

            for (TicketTypeRequest ticketTypeRequest: ticketTypeRequests) {
                // Calculate prices of each ticket request depending on ticket type
                if (ticketTypeRequest.getTicketType() == TicketTypeRequest.Type.ADULT) {
                    adultRequestPresent = true;
                    price = 20 * ticketTypeRequest.getNoOfTickets();
                } else if (ticketTypeRequest.getTicketType() == TicketTypeRequest.Type.CHILD) {
                    price = 10 * ticketTypeRequest.getNoOfTickets();
                } else if (ticketTypeRequest.getTicketType() == TicketTypeRequest.Type.INFANT) {
                    price = 0;
                }
                // Validation to check if there is an attempt to purchase more than 20 tickets at a time.
                validateTicketLimits(ticketTypeRequest);
                // Process payment request to 'TicketPaymentService'
                ticketPaymentService.makePayment(accountId, price);
                // Confirm seat reservations request to 'SeatReservationPaymentService'
                confirmSeatReservations(accountId, seatReservationService, ticketTypeRequest);
            }
            // Validation check to ensure child and infant tickets cannot be purchased without purchasing adult ticket
            adultCheck(adultRequestPresent);
        }
    }

    private void validateTicketLimits(TicketTypeRequest ticketTypeRequest) {
        if (ticketTypeRequest.getNoOfTickets() > MAXIMUM_TICKET_LIMIT) {
            throw new InvalidPurchaseException("Only a maximum of 20 tickets can be purchased at a time");
        }
    }

    private void confirmSeatReservations(Long accountId, SeatReservationServiceImpl seatReservationService, TicketTypeRequest ticketTypeRequest) {
        if (ticketTypeRequest.getTicketType() != TicketTypeRequest.Type.INFANT){
            seatReservationService.reserveSeat(accountId, ticketTypeRequest.getNoOfTickets());
        }
    }

    private void adultCheck(boolean adultRequestPresent) {
        if (!adultRequestPresent) {
            throw new InvalidPurchaseException("At least one adult needs to be present!");
        }
    }
}
