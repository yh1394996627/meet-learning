package org.example.meetlearning.vo.zoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class ZoomWebhookPayload {
    @JsonProperty("event")
    private String event;

    @JsonProperty("payload")
    private Payload payload;

    // getters and setters

    @Data
    public static class Payload {
        @JsonProperty("object")
        private Object object;

        // getters and setters

        @Data
        public static class Object {
            @JsonProperty("id")
            private String id;

            @JsonProperty("uuid")
            private String uuid;

            @JsonProperty("host_id")
            private String hostId;

            @JsonProperty("topic")
            private String topic;

            @JsonProperty("start_time")
            private String startTime;

            @JsonProperty("timezone")
            private String timezone;

            @JsonProperty("participant")
            private Participant participant;

            @Data
            public static class Participant {
                @JsonProperty("user_id")
                private String userId;

                @JsonProperty("user_name")
                private String userName;

                @JsonProperty("email")
                private String email;

                @JsonProperty("join_time")
                private String joinTime;

                @JsonProperty("id")
                private String participantId;  // 参会者唯一ID

            }
        }
    }
}