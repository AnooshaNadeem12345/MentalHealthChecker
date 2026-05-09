// This class holds one saved student entry (used for CRUD + file storage)
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

    // Getters
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

    // Setters (for Update/Edit)
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

    // Convert to one line for saving in .txt file
    public String toFileLine() {
        return name + "|" + age + "|" +
               sleepScore + "|" + stressScore + "|" + moodScore + "|" +
               socialScore + "|" + focusScore + "|" + totalScore + "|" +
               status + "|" + date;
    }

    // Read back from a saved .txt line
    public static StudentRecord fromFileLine(String line) {
        String[] p = line.split("\\|");
        if (p.length < 10) return null;
        return new StudentRecord(
            p[0],                        // name
            Integer.parseInt(p[1]),      // age
            Integer.parseInt(p[2]),      // sleep
            Integer.parseInt(p[3]),      // stress
            Integer.parseInt(p[4]),      // mood
            Integer.parseInt(p[5]),      // social
            Integer.parseInt(p[6]),      // focus
            Integer.parseInt(p[7]),      // total
            p[8],                        // status
            p[9]                         // date
        );
    }

    // What shows in the table row
    public Object[] toTableRow() {
        return new Object[]{ name, age, totalScore + "/25", status, date };
    }
}