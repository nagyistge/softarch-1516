package cat.udl.eps.softarch.mydoodle.handler;

import cat.udl.eps.softarch.mydoodle.model.TimeSlot;
import cat.udl.eps.softarch.mydoodle.model.TimeSlotAvailability;
import cat.udl.eps.softarch.mydoodle.repository.TimeSlotAvailabilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@Component
@RepositoryEventHandler(TimeSlotAvailability.class)
public class TimeSlotAvailabilityEventHandler {
    final Logger logger = LoggerFactory.getLogger(TimeSlotAvailabilityEventHandler.class);

    @Autowired
    TimeSlotAvailabilityRepository timeSlotAvailabilityRepository;

    @HandleBeforeCreate
    @Transactional
    public void handleTimeSlotAvailabilityCreate(TimeSlotAvailability slotAvailability) {
        TimeSlot slot = slotAvailability.getTimeSlot();
        if (slot != null){
            if(!slot.getMeeting().getIsOpen()){ throw new AuthorizationServiceException("Meeting is closed"); }
        }
    }

    @HandleBeforeSave
    @Transactional
    public void handleTimeSlotAvailabilitySave(TimeSlotAvailability slotAvailability) {
        logger.info("Saving: {}", slotAvailability);
    }

    @HandleBeforeLinkSave
    public void handleTimeSlotAvailabilityLinkSave(TimeSlotAvailability timeSlotAvailability, Object o) {
        logger.info("Saving link: {} to {}", timeSlotAvailability, o);
    }

    @HandleAfterCreate
    @Transactional
    public void handleTimeSlotAvailabilityAfterCreate(TimeSlotAvailability timeSlotAvailability) {
        TimeSlot slot = timeSlotAvailability.getTimeSlot();
        if(slot != null){
            slot.countAvailabilities();
        }
    }

    @HandleAfterSave
    @Transactional
    public void handleTimeSlotAvailabilityAfterSave(TimeSlotAvailability timeSlotAvailability) {
        TimeSlot slot = timeSlotAvailability.getTimeSlot();
        if(slot != null){
            slot.countAvailabilities();
        }
    }
}
