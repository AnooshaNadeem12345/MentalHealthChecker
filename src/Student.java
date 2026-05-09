public class Student extends User {
    private String semester;

    public Student(String name, int age, String semester) {
        super(name, age);  // calls User constructor
        this.semester = semester;
    }

    public String getSemester() { return semester; }

    @Override
    public String getUserInfo() {
        return super.getUserInfo() + " | Semester: " + semester;
    }
}