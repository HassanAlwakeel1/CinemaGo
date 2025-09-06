package com.CinemaGo.controller;

import com.CinemaGo.model.entity.SeatReservation;
import com.CinemaGo.repository.SeatReservationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@Tag(name = "Stripe Webhook")
@CrossOrigin
public class StripeWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookController.class);

    private final SeatReservationRepository seatReservationRepository;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    public StripeWebhookController(SeatReservationRepository seatReservationRepository) {
        this.seatReservationRepository = seatReservationRepository;
    }
    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            logger.info("Received Stripe event: " + event.getType());
        } catch (SignatureVerificationException e) {
            logger.warn("⚠️ Stripe signature verification failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signature verification failed");
        }

        logger.info("Received Stripe event: {}", event.getType());

        switch (event.getType()) {

            case "checkout.session.completed":
                handleCheckoutSessionCompleted(event, payload);
                break;

            case "payment_intent.succeeded":
                handlePaymentIntentSucceeded(event, payload);
                break;

            default:
                logger.info("Unhandled event type: {}", event.getType());
        }
        return ResponseEntity.ok("Webhook received");
    }

    private void markReservationAsPaid(String reservationId) {
        if (reservationId == null) {
            logger.warn("⚠️ reservationId is null, cannot update reservation");
            return;
        }
        SeatReservation seatReservation = seatReservationRepository.findById(Long.parseLong(reservationId)).get();
        seatReservation.setStatus("PAID");
        seatReservationRepository.save(seatReservation);
    }

    private void handleCheckoutSessionCompleted(Event event, String payload) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();

        if (deserializer.getObject().isPresent()) {
            StripeObject obj = deserializer.getObject().get();
            if (obj instanceof Session session) {
                String reservationId = session.getMetadata().get("reservationId");
                markReservationAsPaid(reservationId);
            }
        } else {
            // Fallback: parse raw JSON payload
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(payload);
                JsonNode metadataNode = root.path("data").path("object").path("metadata");
                if (metadataNode.has("reservationId")) {
                    String reservationId = metadataNode.get("reservationId").asText();
                    markReservationAsPaid(reservationId);
                }
            } catch (Exception e) {
                logger.error("⚠️ Failed to parse raw JSON for checkout.session.completed", e);
            }
        }
    }
    private void handlePaymentIntentSucceeded(Event event, String payload) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();

        if (deserializer.getObject().isPresent()) {
            StripeObject obj = deserializer.getObject().get();
            if (obj instanceof PaymentIntent paymentIntent) {
                String reservationId = paymentIntent.getMetadata().get("reservationId");
                if (reservationId != null) {
                    markReservationAsPaid(reservationId);
                } else {
                    logger.warn("⚠️ payment_intent.succeeded has no reservationId metadata");
                }
            }
        } else {
            // Fallback: parse raw JSON payload
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(payload);
                JsonNode metadataNode = root.path("data").path("object").path("metadata");
                if (metadataNode.has("reservationId")) {
                    String reservationId = metadataNode.get("reservationId").asText();
                    markReservationAsPaid(reservationId);
                }
            } catch (Exception e) {
                logger.error("⚠️ Failed to parse raw JSON for payment_intent.succeeded", e);
            }
        }
    }
}
