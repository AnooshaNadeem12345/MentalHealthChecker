public class Assessment {
    private int sleepScore;
    private int stressScore;
    private int moodScore;
    private int socialScore;
    private int focusScore;

    public void setScores(int sleep, int stress, int mood, int social, int focus) {
        this.sleepScore = sleep;
        this.stressScore = stress;
        this.moodScore = mood;
        this.socialScore = social;
        this.focusScore = focus;
    }

    public int getTotalScore() {
        return sleepScore + stressScore + moodScore + socialScore + focusScore;
    }

    public int getSleepScore()  { return sleepScore; }
    public int getStressScore() { return stressScore; }
    public int getMoodScore()   { return moodScore; }
    public int getsocialScore(){return socialScore;}
    public int getfocusScore(){return socialScore;}
    
}