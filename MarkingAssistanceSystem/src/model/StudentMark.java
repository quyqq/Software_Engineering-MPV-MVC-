/**
 * @author Quyet Quang Quy ID: 12118217
 * @version 1.0 
 * this class is to store data of student when data is loaded from database.
 * it also use to make new student mark
 */
package model;


public class StudentMark implements IEntityMarker {

    private String studentID;
    private int assignment1;
    private int assignment2;
    private int exam;
    private int total;
    private String grade;

    /**
     * default constructor
     */
    public StudentMark() {
    }

    /**
     * parameter constructor
     * @param studentID
     * @param assignment1
     * @param assignment2
     * @param exam
     * @param total total mark is total of all assets
     * @param grade
     */
    public StudentMark(String studentID, int assignment1, int assignment2, int exam, int total, String grade) {
        this.studentID = studentID;
        this.assignment1 = assignment1;
        this.assignment2 = assignment2;
        this.exam = exam;
        this.total = total;
        this.grade = grade;
    }

    //get and set
    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public int getAssignment1() {
        return assignment1;
    }

    public void setAssignment1(int assignment1) {
        this.assignment1 = assignment1;
    }

    public int getAssignment2() {
        return assignment2;
    }

    public void setAssignment2(int assignment2) {
        this.assignment2 = assignment2;
    }

    public int getExam() {
        return exam;
    }

    public void setExam(int exam) {
        this.exam = exam;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return String.format("Student ID: %s \nAssignment 1: %d \nAssignment 2: %d\nExam: %d \nTotal: %d \nGrade: %s",
                 studentID, assignment1, assignment2, exam, total, grade);
    }

}
