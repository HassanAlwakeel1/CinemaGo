package com.CinemaGo.controller;

import com.CinemaGo.model.dto.ReservationRequest;
import com.CinemaGo.service.SeatReservationService;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final SeatReservationService reservationService;
    private static final Logger logger = Logger.getLogger(PaymentController.class.getName());


    @Value("${stripe.public.key}")
    private String publicKey;

    @PostMapping("/create-checkout-session")
    public Map<String, Object> createCheckoutSession(@RequestBody ReservationRequest request) throws Exception {
        // create reservation with PENDING status
        var reservationResponse = reservationService.reserveSeat(request);
        logger.info("Created reservation with ID: " + reservationResponse.getReservationId());

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:2200/api/v1/movies?pageNumber=0&pageSize=10&sortBy=id&sortDirection=asc\n")
                        .setCancelUrl("http://localhost:3000/cancel")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("usd")
                                                        .setUnitAmount((long) (request.getPrice() * 100))
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName("Seat Reservation")
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                        .putMetadata("reservationId", reservationResponse.getReservationId().toString())
                        .build();

        Session session = Session.create(params);
        logger.info("Created Stripe checkout session with ID: " + session.getId());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", session.getId());
        responseData.put("publicKey", publicKey);
        responseData.put("url", session.getUrl());
        return responseData;
    }
}
