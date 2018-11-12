package nurulaiman.sony.models;

import android.support.v17.leanback.supportleanbackshowcase.models.Card;

public class Episode extends Card {


    public Episode(String v, String t){

        super.setTitle(t);
        super.setVideoId(v);
    }

}
