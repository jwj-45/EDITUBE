package team_iproject_main.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeChannel {
    private String etag;
    private String kind;
    private List<Item> items;

    @Data
    public static class Item {
        private String kind;
        private String etag;
        private String id;
        private Snippet snippet;
        private Statistics statistics;

        @Data
        public static class Snippet {
            private String title;
            private String description;
            private String customUrl;
            private Date publishedAt;
            private Thumbnails thumbnails;
        }

        @Data
        public static class Statistics {
            private boolean hiddenSubscriberCount;
            private int subscriberCount;
            private int videoCount;
            private int viewCount;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Thumbnails {
            private YoutubeChannelList.Item.Snippet.Thumbnails.Default defaultThumbnail;
            private YoutubeChannelList.Item.Snippet.Thumbnails.High high;
            private YoutubeChannelList.Item.Snippet.Thumbnails.Medium medium;

            @Data
            public static class Default {
                private int height;
                private String url;
                private int width;
            }

            @Data
            public static class High {
                private int height;
                private String url;
                private int width;
            }

            @Data
            public static class Medium {
                private int height;
                private String url;
                private int width;
            }
        }
    }
}