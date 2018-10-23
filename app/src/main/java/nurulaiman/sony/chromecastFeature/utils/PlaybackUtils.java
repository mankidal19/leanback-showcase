package nurulaiman.sony.chromecastFeature.utils;

import java.util.Random;

public class PlaybackUtils {
    private static String[] videoIds = {"6JYIGclVQdw", "LvetJ9U_tVY", "S0Q4gqBUs7c", "zOa-rSM4nms"};
    private static Random random = new Random();
    private static String videoId = null;

    public static String getNextVideoId() {
        return videoIds[random.nextInt(videoIds.length)];
    }

    public static void setVideoId(String vId){
        videoId = vId;
    }

    public static String getVideoId(){
        return videoId;
    }
}
