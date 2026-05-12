
public class StudentRecord {
    private String name;
    private int age;
    private int sleepScore, stressScore, moodScore, socialScore, focusScore;
    private int totalScore;
    private String status;
    private String date;

    public StudentRecord(String name, int age,
                         int sleep, int stress, int mood, int social, int focus,
                         int total, String status, String date) {
        this.name        = name;
        this.age         = age;
        this.sleepScore  = sleep;
        this.stressScore = stress;
        this.moodScore   = mood;
        this.socialScore = social;
        this.focusScore  = focus;
        this.totalScore  = total;
        this.status      = status;
        this.date        = date;
    }

    
    public String getName()        { return name; }
    public int    getAge()         { return age; }
    public int    getSleepScore()  { return sleepScore; }
    public int    getStressScore() { return stressScore; }
    public int    getMoodScore()   { return moodScore; }
    public int    getSocialScore() { return socialScore; }
    public int    getFocusScore()  { return focusScore; }
    public int    getTotalScore()  { return totalScore; }
    public String getStatus()      { return status; }
    public String getDate()        { return date; }

  
    public void setName(String name)   { this.name = name; }
    public void setAge(int age)        { this.age = age; }
    public void setStatus(String status) { this.status = status; }
    public void setDate(String date)     { this.date = date; }

    public void setScores(int sleep, int stress, int mood, int social, int focus) {
        this.sleepScore  = sleep;
        this.stressScore = stress;
        this.moodScore   = mood;
        this.socialScore = social;
        this.focusScore  = focus;
        this.totalScore  = sleep + stress + mood + social + focus;
    }

 
    public String toFileLine() {
        return name + "|" + age + "|" +
               sleepScore + "|" + stressScore + "|" + moodScore + "|" +
               socialScore + "|" + focusScore + "|" + totalScore + "|" +
               status + "|" + date;
    }

    public static StudentRecord fromFileLine(String line) {
        String[] p = line.split("\\|");
        if (p.length < 10) return null;
        return new StudentRecord(
            p[0],                       
            Integer.parseInt(p[1]),      
            Integer.parseInt(p[2]),      
            Integer.parseInt(p[3]),     
            Integer.parseInt(p[4]),     
            Integer.parseInt(p[5]),      
            Integer.parseInt(p[6]),      
            Integer.parseInt(p[7]),      
            p[8],                        
            p[9]                         
        );
    }
    public Object[] toTableRow() {
        return new Object[]{ name, age, totalScore + "/25", status, date };
    }
}