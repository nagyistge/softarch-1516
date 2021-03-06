package cat.udl.eps.softarch.mydoodle.handler;

import cat.udl.eps.softarch.mydoodle.model.MeetingProposal;
import cat.udl.eps.softarch.mydoodle.model.ParticipantAvailability;
import cat.udl.eps.softarch.mydoodle.utils.MailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Eric Labara
 */

@Component
@RepositoryEventHandler(MeetingProposal.class)
public class MeetingProposalEventHandler {
    final Logger logger = LoggerFactory.getLogger(MeetingProposalEventHandler.class);

    @Autowired
    MailUtils mailUtils;

    @HandleBeforeCreate
    @Transactional
    public void handleMeetingProposalCreate(MeetingProposal meetingProposal){
        logger.info("Creating: {}", meetingProposal);
    }

    @HandleBeforeSave
    @Transactional
    public void handleMeetingProposalSave(MeetingProposal meetingProposal){
        logger.info("Saving: {}", meetingProposal);
    }

    @HandleBeforeLinkSave
    public void handleMeetingProposalLinkSave(MeetingProposal meetingProposal, Object o) {
        logger.info("Saving link: {} to {}", meetingProposal, o);
    }

    @HandleAfterCreate
    @Transactional
    public void handleMeetingProposalPostCreate(MeetingProposal meetingProposal){
        logger.info("Created: {}", meetingProposal);
    }

    @HandleAfterSave
    @Transactional
    public void handleMeetingProposalSaveScheduleTime(MeetingProposal meetingProposal){
        if(!meetingProposal.getIsOpen()) {
            if(meetingProposal.getSchedule()!=null){
                List<ParticipantAvailability> availabilities = meetingProposal.getAvailabilities();
                     for(int i=0;i<availabilities.size();i++) {
                         availabilities.get(i).sendMeetingInvite(mailUtils);
                }
            }
        }
    }

}