package com.floopandbloop.game;

public interface PlayServices
{
    public void signIn();
    public void signOut();
    public void rateGame();
    public void submitScore(int highScore);
    public void showScore();
    public boolean isSignedIn();
    public long getHighScore();
    public void unlockAchievement();
    public void showAchievement();
    public void goToUrl();
}
