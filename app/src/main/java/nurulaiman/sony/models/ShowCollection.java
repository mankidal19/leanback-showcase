package nurulaiman.sony.models;

public class ShowCollection {
    private String videoId;
    private String title;

    public ShowCollection(String v,String t){
        videoId = v;
        title = t;
    }

    public String getTitle() {
        return title;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
