package androidyoutubeplayer.player.listeners;

import android.support.annotation.NonNull;

import androidyoutubeplayer.player.YouTubePlayer;


public interface YouTubePlayerInitListener {
    void onInitSuccess(@NonNull YouTubePlayer youTubePlayer);
}
