/**
 * @author Quyet Quang Quy ID: 12118217
 * @version 1.0
 * @see java.sql.SQLException;
 * @see java.util.LinkedList;
 * @see java.util.List;
 * @see model.EStatementTags;
 * @see model.StudentMark;
 * @see view.IView;
 * @see model.IConnect;
 * @see model.IQuery;
 *
 * The presenter is to handle all business process and it is a middle layer This
 * presenter will get data from model layer then process and send processed to
 * view layer
 */
package presenter;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.EStatementTags;
import model.StudentMark;
import view.IView;
import model.IConnect;
import model.IQuery;

public class StudentMarkPresenter {

    private final IQuery iQuery;
    private final IConnect iConnect;
    private final IView iView;

    private boolean disableStatus;// for cotrol the controller on GUI
    private int counterIndex;// for knowing where is the current student
    private int counterIndexGivenGrade;// for knowing where is the current student after find student with a given grade
    private List<StudentMark> listOfMarks;
    private List<StudentMark> findingStudentByGrade;

    /**
     * Parameter constructor
     *
     * @param iQuery
     * @param iConnect
     * @param iView
     */
    public StudentMarkPresenter(IQuery iQuery, IConnect iConnect, IView iView) {
        this.iQuery = iQuery;
        this.iConnect = iConnect;
        this.iView = iView;
        disableStatus = false;
        counterIndex = 0;
        counterIndexGivenGrade = 0;
        listOfMarks = new LinkedList<>();
        findingStudentByGrade = new LinkedList<>();
    }

    /**
     * this method is to precess and show the next student when it is the last
     * student, the next student will be the first one
     */
    public void actionNext() {
        if (!listOfMarks.isEmpty()) {
            if (counterIndex < listOfMarks.size() - 1) {
                counterIndex++;
            } else {
                counterIndex = 0;
            }

            refreshStudentDetails();
        } else {
            actionBrowse();
        }
    }

    /**
     * this method is to precess and show the last student when it is the first
     * student, the next student will be the last one
     */
    public void actionPrevious() {
        if (!listOfMarks.isEmpty()) {
            if (counterIndex > 0) {
                counterIndex--;
            } else {
                counterIndex = listOfMarks.size() - 1;
            }

            refreshStudentDetails();
        } else {
            actionBrowse();
        }
    }

    /**
     * this method is to find a student by getting the keyword from view layer
     */
    public void actionSearchStudent() {

        try {
            String studentID = iView.getSearchingKeyWord().toUpperCase();// get student ID from GUI
            if (studentID.length() == 0) {
                iView.showErrorDialog("Please enter student ID");
                return;
            }
            List<StudentMark> tmp = iQuery.select(EStatementTags.FIND_ONE, studentID);// Find the student with given student ID
            if (tmp.isEmpty()) {
                iView.showErrorDialog("Student with id: " + studentID + " could not be found.");
            } else {
                setDetailsStudent(tmp.get(0), studentID + " was found");// Show the student details on GUI
            }
        } catch (SQLException ex) {
            iView.showErrorDialog("Can not find the given student:\n" + ex.getMessage());
        }
    }

    /**
     * this method is to insert a new student to database
     */
    public void actionInsertStudent() {

        try {
            iView.clearResult();// clear text area result
            disableStatus = !disableStatus;
            if (disableStatus) {
                iView.setButtonInsertNewStudentLabel("Save");// change insert student label
                clearText();// clear textbox on GUI in Student Details block

            } else {

                insertToDatabase();// insert data to database
                iView.setButtonInsertNewStudentLabel("Insert New Student");
            }
            iView.setDisdableStageForInsert(disableStatus);
        } catch (Exception ex) {
            iView.showErrorDialog("Can not insert new student: " + ex.getMessage());
        }

    }

    /**
     * if the user dose not want to insert new student anymore, they can cancel
     * the insert action
     */
    public void setCancelStage() {
        iView.setButtonInsertNewStudentLabel("Insert New Student");// change the label of insert button as original label
        iView.setDisdableStageForInsert(false);
        disableStatus = false;
        if (listOfMarks.isEmpty()) {
            actionBrowse();// reload data from database
        } else {
            refreshStudentDetails();// refresh student details block on GUI
        }
    }

    /**
     * this method is to update student details
     */
    public void actionUpdateStudent() {

        disableStatus = !disableStatus;
        if (disableStatus) {
            iView.setUpdateButtonLabel("Save");
            iView.setDisdableStageForUpdate(disableStatus);
        } else {
            try {
                iView.clearResult();// clear text area result
                iView.setUpdateButtonLabel("Update Studentś Marks");
                iView.setDisdableStageForUpdate(disableStatus);
                StudentMark tmp = createNewStudentInstance(false);// get new data from Student details Block on GUI
                if (iQuery.command(EStatementTags.UPDATE, tmp) > 0) {// insert data to database
                    iView.setResult("The student " + tmp.getStudentID() + " marks are updated.\n" + tmp);
                    actionBrowse();
                    iView.setResult("******************Update Marks*******************");
                } else {
                    iView.showErrorDialog("Can not update student marks.\nBecause can not find the sutdent in database");
                }

            } catch (Exception e) {
                iView.showErrorDialog("Can not update student marks.\n" + e.getMessage());
                actionBrowse();
            }
        }
    }

    /**
     * this method is to find the type of fail if given student have total mark
     * from 45 to 49
     *
     * @param mark the given student
     * @return the grade of fail
     */
    private String findFailType(StudentMark mark) {
        int total = mark.getTotal();
        int mAsgn1 = mark.getAssignment1();
        int mAsgn2 = mark.getAssignment2();
        int mExam = mark.getExam();
        int mPasAsgn1 = EAssetMaxMark.ASSIGN_1.getPassMark();
        int mPasAsgn2 = EAssetMaxMark.ASSIGN_2.getPassMark();
        int mPasExam = EAssetMaxMark.EXAM.getPassMark();

        /**
         * this block of code is to find out what kind of fail that student is.
         * If the total mark is in the range 45 to 50 and the student has failed
         * only one assessment item, then the grade is SA if that item was an
         * assignment. If it was an exam, the grade is SE.
         *
         * If a student has submitted no assessment items then the grade is AF
         *
         * For all other cases, the grade is F
         */
        if (total < EBounderyOption.FROM_50_TO_64.getFromM() && total >= 45) {
            if ((mAsgn1 < mPasAsgn1 && mAsgn2 >= mPasAsgn2 && mExam >= mPasExam)
                    || (mAsgn1 >= mPasAsgn1 && mAsgn2 < mPasAsgn2 && mExam >= mPasExam)
                    || (mAsgn1 >= mPasAsgn1 && mAsgn2 >= mPasAsgn2 && mExam < mPasExam)) {
                if (mExam < mPasExam) {
                    return EBounderyOption.LESS_THEN_50_SE.getUserInterfaceGrade();
                } else {
                    return EBounderyOption.LESS_THEN_50_SA.getUserInterfaceGrade();
                }
            }
        }
        if (total == 0) {
            return EBounderyOption.LESS_THEN_50_AF.getUserInterfaceGrade();
        }
        return EBounderyOption.LESS_THEN_50_F.getUserInterfaceGrade();
    }

    /**
     * this method is to find or calculate grade for the given student the
     * EBounderyOption Enum is used in this method the rage is shown below:
     *
     * Grade Range HD 85 to 100 D 75 to 84 C 65 to 74 P 50 to 64
     *
     * less than 50 it will call method findFailType *
     *
     * @param mark given student
     * @return grade
     */
    private String calculateGrade(StudentMark mark) {

        int total = mark.getTotal();
        if (total <= EBounderyOption.LESS_THEN_50_F.getToM()) {
            return findFailType(mark);
        }
        if (total >= EBounderyOption.FROM_50_TO_64.getFromM()
                && total <= EBounderyOption.FROM_50_TO_64.getToM()) {
            return EBounderyOption.FROM_50_TO_64.getUserInterfaceGrade();
        }
        if (total >= EBounderyOption.FROM_65_TO_74.getFromM()
                && total <= EBounderyOption.FROM_65_TO_74.getToM()) {
            return EBounderyOption.FROM_65_TO_74.getUserInterfaceGrade();
        }
        if (total >= EBounderyOption.FROM_75_TO_84.getFromM()
                && total <= EBounderyOption.FROM_75_TO_84.getToM()) {
            return EBounderyOption.FROM_75_TO_84.getUserInterfaceGrade();
        }
        if (total >= EBounderyOption.OVER_85.getFromM()
                && total <= EBounderyOption.OVER_85.getToM()) {
            return EBounderyOption.OVER_85.getUserInterfaceGrade();
        }

        return String.valueOf(total);
    }

    /**
     * this method is to calculate a current student who is showing in details
     * block on GUI
     */
    public void actionCalculateCurrentStudentGrade() {

        try {
            iView.clearResult();// clear text area result
            StudentMark tmp = createNewStudentInstance(true);
            tmp.setGrade(calculateGrade(tmp));
            if (iQuery.command(EStatementTags.UPDATE, tmp) > 0) {
                iView.setResult("The student " + tmp.getStudentID() + " grade is updated.\n" + tmp);
                actionBrowse();
                iView.setResult("******************Calculate grade for one students*******************");
            } else {
                iView.showErrorDialog("Can not calculate greade for the student.\nBecause can not find the student in database.");
            }
        } catch (IllegalArgumentException | SQLException e) {
            iView.showErrorDialog("Can not calculate greade for the student.\n" + e.getMessage());
        }

    }

    /**
     * this method is to calculate all student is database
     */
    public void actionCalculateAllStudentGrade() {
        //actionBrowse();

        if (!listOfMarks.isEmpty()) {
            iView.clearResult();// clear text area result
            iView.setRevertResult("******************Calculate grade all students [" + listOfMarks.size() + "] *******************");
            listOfMarks.forEach((listOfMark) -> {
                try {
                    listOfMark.setTotal(// calculate total again make sure correct total
                            listOfMark.getAssignment1()
                            + listOfMark.getAssignment2()
                            + listOfMark.getExam()
                    );
                    listOfMark.setGrade(calculateGrade(listOfMark));
                    iQuery.command(EStatementTags.UPDATE, listOfMark);// update to database after calculate
                    iView.setRevertResult("The student " + listOfMark.getStudentID() + " grade is updated.\n" + listOfMark);
                } catch (SQLException ex) {
                    iView.showErrorDialog("Can not calculate greade for all students.\n" + ex.getMessage());
                }
            });
            actionBrowse();

        }

    }

    /**
     * this method is to find the grade
     *
     * @param input the text that is shown on combo box on GUI
     * @return Grade such as HD D C P ...
     */
    private String findActualGrade(String input) {
        String grade = null;

        for (EBounderyOption b : EBounderyOption.values()) {
            if (b.getUserInterfaceMsg().equalsIgnoreCase(input)) {
                return b.getUserInterfaceGrade();
            }
        }
        return grade;
    }

    /**
     * this method is to set value of textResultFindingStudentGivenGrade on GUI
     *
     * @param index the current student
     */
    private void showStudentDetailsOntextBox() {
        if (findingStudentByGrade.size() > 0) {
            iView.setTextBoxGradeResult(findingStudentByGrade.get(counterIndexGivenGrade).toString());
            iView.setLabelFindByGrade("Find Students With Given Grade Result: "
                    + (counterIndexGivenGrade + 1) + "/" + findingStudentByGrade.size());
        } else {
            iView.showErrorDialog("There is no student in the list.");
            iView.setLabelFindByGrade("Find Students With Given Grade Result");
        }
    }

    /**
     * this method is to find all student with a given grade and show it
     */
    public void actionFindAllStudentGivenGrade() {
        String selectionGrade = findActualGrade(iView.getSelectedGrade());
        if (selectionGrade != null) {
            try {
                iView.clearResult();// clear text area result
                findingStudentByGrade.clear();
                findingStudentByGrade = iQuery.select(EStatementTags.FIND_STUDENT_WITH_GIVEN_GRADE, selectionGrade);
                if (findingStudentByGrade.size() > 0) {
                    counterIndexGivenGrade = 0;
                    showStudentDetailsOntextBox();
                } else {
                    iView.showErrorDialog("There are no student with the given grade: " + selectionGrade);
                }
            } catch (SQLException ex) {
                iView.showErrorDialog("Can not find students base on given grade\n" + ex.getMessage());
            }
        } else {
            iView.showErrorDialog("Please select grade from the combo box.");
        }
    }

    /**
     * this method is to find the borderline cases in a given range
     */
    public void actionFindBoderlineCasesGivenRange() {

        try {
            iView.clearResult();// clear text area result
            List<StudentMark> listOfCase = new LinkedList<>();
            int toTotal = Integer.parseInt(iView.getToTotal());
            int fromTotal = Integer.parseInt(iView.getFromTotal());

            if (fromTotal < 0) {
                iView.showErrorDialog("From total mark must be higher or equal 0.");
                return;
            }
            if (toTotal < 0 && toTotal <= fromTotal) {
                iView.showErrorDialog("To total mark must be higher than 0 and higer than from total mark");
                return;
            }
            listOfCase.addAll(
                    iQuery.select(
                            EStatementTags.FIND_BORDERLINE_CASES_EXPLICIT_RAGE,// find all total mark in a given range
                            fromTotal, toTotal)
            );

            if (listOfCase.isEmpty()) {
                iView.showErrorDialog("There is no boderline cases.");
            } else {

                for (StudentMark studentMark : listOfCase) {
                    iView.setResult("______Student deatils____ :\n" + studentMark);
                }
                iView.setResult(String.format("**************Find boderline cases[found %d cases (From %d to %d)]***************",
                        listOfCase.size(), fromTotal, toTotal));
            }

        } catch (NumberFormatException e) {
            iView.showErrorDialog("From or to total mark is not correct number format. \n" + e.getMessage());
        } catch (Exception e) {
            //e.printStackTrace();
            iView.showErrorDialog("Can not find boderline cases. \n" + e.getMessage());
        }

    }

    /**
     * this method is to find the borderline cases in all boundary base on the
     * given Tolerance
     */
    public void actionFindBoderlineCasesTolerance() {

        try {
            List<StudentMark> listOfCase = new LinkedList<>();
            int tolarance = Integer.parseInt(iView.getTolarance());
            if (tolarance <= 0) {
                iView.showErrorDialog("Tolarance must be higher than 0.");
                return;
            }
            iView.clearResult();// clear text area result
            listOfCase.addAll(
                    iQuery.select(
                            EStatementTags.FIND_BORDERLINE_CASES_TOLARANCE,// all rages is listed below
                            EBounderyOption.LESS_THEN_50_F.getToM() + 1 - tolarance, EBounderyOption.LESS_THEN_50_F.getToM(),
                            EBounderyOption.FROM_50_TO_64.getToM() + 1 - tolarance, EBounderyOption.FROM_50_TO_64.getToM(),
                            EBounderyOption.FROM_65_TO_74.getToM() + 1 - tolarance, EBounderyOption.FROM_65_TO_74.getToM(),
                            EBounderyOption.FROM_75_TO_84.getToM() + 1 - tolarance, EBounderyOption.FROM_75_TO_84.getToM()
                    )
            );

            if (listOfCase.isEmpty()) {
                iView.showErrorDialog("There is no boderline cases.");
            } else {

                for (StudentMark studentMark : listOfCase) {
                    iView.setResult("______Student deatils____ :\n" + studentMark);
                }
                iView.setResult(String.format("**************Find boderline cases [found %d cases (Tolerance %d)]***************",
                        listOfCase.size(), tolarance));
            }

        } catch (NumberFormatException e) {
            iView.showErrorDialog("Tolarance is not correct number format. \n" + e.getMessage());
        } catch (Exception e) {
            //e.printStackTrace();
            iView.showErrorDialog("Can not find boderline cases. \n" + e.getMessage());
        }

    }

    /**
     * this method is to load the boundary options to the combo box
     */
    public void actionLoadRageOption() {
        List<String> tmp = new LinkedList<>();

        for (EBounderyOption opt : EBounderyOption.values()) {

            tmp.add(opt.toString());

        }

        iView.setCombobox(tmp);
    }

    /**
     * this method is to close connection
     *
     * @throws SQLException
     */
    public void close() throws SQLException {
        iConnect.disconnect();
    }

    /**
     * this method is to load data from database to a LinkedList
     */
    public void actionBrowse() {
        try {
            //counterIndex = 0;// can be used if multi users if they delete student mark.
            listOfMarks = iQuery.select(EStatementTags.FIND_ALL);
            refreshStudentDetails();
        } catch (SQLException ex) {
            counterIndex = 0;
            iView.showErrorDialog("Can not browse student:\n" + ex.getMessage());
        }
    }

    /**
     * this method is to show details of the given student on GUI
     *
     * @param mark given student
     * @param title the label showing on GUI
     */
    private void setDetailsStudent(StudentMark mark, String title) {
        iView.setLableIndex(title);
        iView.setStudentID(mark.getStudentID());
        iView.setAssginment1(String.valueOf(mark.getAssignment1()));
        iView.setAssginment2(String.valueOf(mark.getAssignment2()));
        iView.setExam(String.valueOf(mark.getExam()));
        iView.setTotal(String.valueOf(mark.getTotal()));
        iView.setGrade(mark.getGrade());
    }

    /**
     * this method is to renew the student details on GUI
     */
    private void refreshStudentDetails() {

        if (listOfMarks.isEmpty()) {
            iView.showErrorDialog("There is no student in database");

            iView.setLableIndex("");
        } else {
            setDetailsStudent(listOfMarks.get(counterIndex),
                    "Student " + (counterIndex + 1) + " of " + listOfMarks.size() + " students");
        }
    }

    /**
     * Check input mark not minus and total mark not higher than maximum total
     * mark of the subject for example: Assignment 1 has maximum mark is 20 so
     * the maximum mark can not be higher than 20 Assignment 1 has maximum mark
     * is 30 so the maximum mark can not be higher than 30 exam 50 so the
     * maximum mark can not be higher than 50 and maximum total can not higher
     * 100
     *
     *
     * @param as1 Assignment 1 mark
     * @param as2 Assignment 2 mark
     * @param ex exam mark
     * @param total total mark
     * @param maxM total of maximum mark
     * @return true if it is correct input
     */
    private boolean checkMarkMeetRequirement(int as1, int as2, int ex, int total, int maxM) {

        if (as1 < 0 || as1 > EAssetMaxMark.ASSIGN_1.getMaxImunMark()) {
            return false;
        }

        if (as2 < 0 || as2 > EAssetMaxMark.ASSIGN_2.getMaxImunMark()) {
            return false;
        }
        if (ex < 0 || ex > EAssetMaxMark.EXAM.getMaxImunMark()) {
            return false;
        }
        return total <= maxM;
    }

    /**
     * this method is to crate new student by getting all information from GUI
     * when user finished entering data
     *
     * @param updateGrade if is true that is mean grade will be calculated if is
     * not that mean the grade will be stored as N/A
     * @return new student instance
     * @throws IllegalArgumentException
     */
    private StudentMark createNewStudentInstance(boolean updateGrade) throws IllegalArgumentException {

        try {
            String studentID = iView.getStudentID().toUpperCase();
            int assign1 = Integer.parseInt(iView.getAssginment1());
            int assign2 = Integer.parseInt(iView.getAssginment2());
            int exam = Integer.parseInt(iView.getExam());
            int total = assign1 + assign2 + exam;
            int maxMark = EAssetMaxMark.ASSIGN_1.getMaxImunMark()
                    + EAssetMaxMark.ASSIGN_2.getMaxImunMark()
                    + EAssetMaxMark.EXAM.getMaxImunMark();
            StudentMark mark = new StudentMark(studentID, assign1, assign2, exam, total, "");

            if (!checkMarkMeetRequirement(assign1, assign2, exam, total, maxMark)) {
                throw new IllegalArgumentException("Toal of mark is higher than "
                        + maxMark
                        + " or entered negative number.\nPlease check the marks \n"
                        + String.format("Max Assignment 1 mark: %d| Max Assignment 2 mark: %d| Max Exam mark: %d",
                                EAssetMaxMark.ASSIGN_1.getMaxImunMark(),
                                EAssetMaxMark.ASSIGN_2.getMaxImunMark(),
                                EAssetMaxMark.EXAM.getMaxImunMark())
                );
            }

            if (updateGrade) {
                mark.setGrade(calculateGrade(mark));
            } else {
                mark.setGrade("N/A");
            }
            return mark;
        } catch (NumberFormatException e) {
            throw new NumberFormatException(
                    "Please enter correct number format in assignment 1,2 and exam. \n"
                    + e.getMessage());
        }

    }

    /**
     * this method is to insert new student mark to database
     */
    private void insertToDatabase() {
        StudentMark mark = null;
        try {
            mark = createNewStudentInstance(false);
            iQuery.command(EStatementTags.INSERT, mark);

            iView.setResult("Inserted student :\n" + mark);
            iView.setResult("******************Insert new student*******************");
        } catch (IllegalArgumentException | SQLException ex) {
            iView.showErrorDialog("Can not insert new student: \n" + mark + "\n" + ex.getMessage());
        }
        actionBrowse();
    }

    /**
     * this method is to clear all current text in the text box on GUI
     */
    private void clearText() {
        iView.setStudentID("");
        iView.setAssginment1("");
        iView.setAssginment2("");
        iView.setGrade("");
        iView.setExam("");
        iView.setTotal("");

    }

    /**
     * this method is to show the next mark in the result after finding the
     * student with a given grade
     */
    public void nextGrade() {
        if (!findingStudentByGrade.isEmpty()) {
            if (counterIndexGivenGrade < findingStudentByGrade.size() - 1) {
                counterIndexGivenGrade++;
            } else {
                counterIndexGivenGrade = 0;
            }

            showStudentDetailsOntextBox();
        } else {
            iView.showErrorDialog("There is no student in the list.");
        }
    }

    /**
     * this method is to show the previous mark in the result after finding the
     * student with a given grade
     */
    public void previousGrade() {
        if (!findingStudentByGrade.isEmpty()) {
            if (counterIndexGivenGrade > 0) {
                counterIndexGivenGrade--;
            } else {
                counterIndexGivenGrade = findingStudentByGrade.size() - 1;
            }

            showStudentDetailsOntextBox();
        } else {
            iView.showErrorDialog("There is no student in the list.");
        }
    }

}
