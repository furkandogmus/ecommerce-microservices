package com.hoangtien2k3.userservice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventConsumer {
//    Gson gson = new Gson();
//    @Autowired
//    ProfileService profileService;
//
//    public EventConsumer(ReceiverOptions<String, String> receiverOptions) {
//        KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(KafkaConstant.PROFILE_ONBOARDED_TOPIC)))
//                .receive()
//                .subscribe(this::profileOnboarded);
//    }
//
//    public void profileOnboarded(ReceiverRecord<String, String> receiverRecord) {
//        log.info("Profile Onboarded event");
//        ProfileDTO dto = gson.fromJson(receiverRecord.value(), ProfileDTO.class);
//        profileService.updateStatusProfile(dto).subscribe();
//    }
}