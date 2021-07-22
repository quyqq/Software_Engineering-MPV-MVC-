/**
 * @author Quyet Quang Quy ID: 12118217
 * @version 1.0
 *
 * @see java.util.List
 * @see javafx.collections.FXCollections
 * @see javafx.event.ActionEvent
 * @see javafx.fxml.FXML
 * @see javafx.scene.control.Button
 * @see javafx.scene.control.ComboBox
 * @see javafx.scene.control.Label
 * @see javafx.scene.control.TextArea
 * @see javafx.scene.control.TextField
 * @see javax.swing.JOptionPane
 * @see presenter.StudentMarkPresenter
 *
 * this class is a presenter that will communicate to view and model layer. it
 * also process all business rules.
 */
package view;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;
import presenter.StudentMarkPresenter;

public class MarkingAssistanceSystemController implements IView {

    private StudentMarkPresenter markPresenter;

    @FXML
    private Button btnNext;

    @FXML
    private Button btnPrevious;

    @FXML
    private Label labelCurrentStudentStage;

    @FXML
    private TextField textStudentID;

    @FXML
    private TextField textAssignment1;

    @FXML
    private TextField textExam;

    @FXML
    private TextField textAssignment2;

    @FXML
    private TextField textTotal;

    @FXML
    private TextField textGrade;

    @FXML
    private Button btnCalculateOneStudent;

    @FXML
    private Button btnBoderlineCasesTolaracne;

    @FXML
    private TextField textSearch;

    @FXML
    private Button btnSearchStudent;

    @FXML
    private Button btnCalculateAllStudents;

    @FXML
    private Button btnUpdaeMark;

    @FXML
    private TextArea textAreaResult;

    @FXML
    private Button btnInsertNewStudent;

    @FXML
    private TextField textTelerance;

    @FXML
    private Button btnBrowse;

    @FXML
    private Button btnCancelInsert;

    @FXML
    private Button btnBorderlineCasesRange;

    @FXML
    private TextField txtFromTotal;

    @FXML
    private TextField txtToTotal;

    @FXML
    private ComboBox<String> comboboxGrade;

    @FXML
    private Button btnFindStudentWithGrade;

    @FXML
    private TextField textResultFindingStudentGivenGrade;

    @FXML
    private Button btnPreviousGVGrade;

    @FXML
    private Button btnNextGVGrade;

    @FXML
    private Label labelFIndByGrade;

    /**
     * This method is to bind presenter to the controller of view, therefore,
     * view can talk to presenter
     *
     * @param markPresenter the presenter
     */
    public void bind(StudentMarkPresenter markPresenter) {
        this.markPresenter = markPresenter;
        markPresenter.actionLoadRageOption();

    }

    //-------------------------------------------------------------------------
    @FXML
    void actionBrowse(ActionEvent event) {

        markPresenter.actionBrowse();
    }

    @FXML
    void actionCalculateAllStudents(ActionEvent event) {

        markPresenter.actionCalculateAllStudentGrade();
    }

    @FXML
    void actionCalculateOneStudent(ActionEvent event) {
        markPresenter.actionCalculateCurrentStudentGrade();
    }

    @FXML
    void actionCancel(ActionEvent event) {
        markPresenter.setCancelStage();
    }

    @FXML
    void actionInsertNewStudent(ActionEvent event) {
        markPresenter.actionInsertStudent();
    }

    @FXML
    void actionNext(ActionEvent event) {
        markPresenter.actionNext();
    }

    @FXML
    void actionPrevious(ActionEvent event) {
        markPresenter.actionPrevious();
    }

    @FXML
    void actionSearch(ActionEvent event) {
        markPresenter.actionSearchStudent();
    }

    @FXML
    void actionUpdateMarks(ActionEvent event) {
        markPresenter.actionUpdateStudent();
    }

    @FXML
    void actionFindBoderlineCasesTolarance(ActionEvent event) {
        markPresenter.actionFindBoderlineCasesTolerance();
    }

    @FXML
    void actionFindStudentWithGivenGrade(ActionEvent event) {
        markPresenter.actionFindAllStudentGivenGrade();
    }

    @FXML
    void actionFindborderlineCasesRange(ActionEvent event) {
        markPresenter.actionFindBoderlineCasesGivenRange();
    }

    @FXML
    void actionbtnNextGVGrade(ActionEvent event) {
        markPresenter.nextGrade();
    }

    @FXML
    void actionbtnPreviousGVGrade(ActionEvent event) {

        markPresenter.previousGrade();
    }

    //-------------------------------------------------------------------------
    @Override
    public String getSearchingKeyWord() {
        return textSearch.getText().trim();
    }

    @Override
    public String getStudentID() {
        return textStudentID.getText().trim();
    }

    @Override
    public void setStudentID(String id) {
        textStudentID.setText(id);
    }

    @Override
    public String getAssginment1() {
        return textAssignment1.getText().trim();
    }

    @Override
    public void setAssginment1(String mark) {
        textAssignment1.setText(mark);
    }

    @Override
    public String getAssginment2() {
        return textAssignment2.getText().trim();
    }

    @Override
    public void setAssginment2(String mark) {
        textAssignment2.setText(mark);
    }

    @Override
    public String getExam() {
        return textExam.getText().trim();
    }

    @Override
    public void setExam(String mark) {
        textExam.setText(mark);
    }

    @Override
    public void setTotal(String mark) {
        textTotal.setText(mark);
    }

    @Override
    public void setGrade(String grade) {
        textGrade.setText(grade);
    }

    @Override
    public String getTolarance() {
        return textTelerance.getText().trim();
    }

    @Override
    public String getSelectedGrade() {
        return comboboxGrade.getSelectionModel().getSelectedItem();
    }

    @Override
    public void setCombobox(List<String> options) {
        comboboxGrade.setItems(FXCollections.observableArrayList(options));
    }

    @Override
    public void setResult(String result) {
        textAreaResult.setText(result + "\n" + textAreaResult.getText().trim());
    }

    @Override
    public void showErrorDialog(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(msg);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }

    @Override
    public void setLableIndex(String indexV) {
        labelCurrentStudentStage.setText(indexV);
    }

    @Override
    public void setDisdableStageForInsert(boolean stage) {
        textStudentID.setDisable(!stage);
        btnCancelInsert.setDisable(!stage);
        btnBrowse.setDisable(stage);
        btnCalculateAllStudents.setDisable(stage);
        btnCalculateOneStudent.setDisable(stage);
        btnNext.setDisable(stage);
        btnPrevious.setDisable(stage);
        btnSearchStudent.setDisable(stage);
        btnUpdaeMark.setDisable(stage);
        textSearch.setDisable(stage);
        comboboxGrade.setDisable(stage);
        textTelerance.setDisable(stage);
        textAssignment1.setDisable(!stage);
        textAssignment2.setDisable(!stage);
        textExam.setDisable(!stage);
        txtFromTotal.setDisable(stage);
        txtToTotal.setDisable(stage);
        btnBoderlineCasesTolaracne.setDisable(stage);
        btnBorderlineCasesRange.setDisable(stage);
        btnFindStudentWithGrade.setDisable(stage);
        btnNextGVGrade.setDisable(stage);
        btnPreviousGVGrade.setDisable(stage);

    }

    @Override
    public void setButtonInsertNewStudentLabel(String labelx) {
        btnInsertNewStudent.setText(labelx);
    }

    @Override
    public void setDisdableStageForUpdate(boolean stage) {
        btnCancelInsert.setDisable(stage);
        btnBrowse.setDisable(stage);
        btnCalculateAllStudents.setDisable(stage);
        btnCalculateOneStudent.setDisable(stage);
        btnNext.setDisable(stage);
        btnPrevious.setDisable(stage);
        btnSearchStudent.setDisable(stage);
        btnInsertNewStudent.setDisable(stage);
        textSearch.setDisable(stage);
        comboboxGrade.setDisable(stage);
        textTelerance.setDisable(stage);
        textAssignment1.setDisable(!stage);
        textAssignment2.setDisable(!stage);
        textExam.setDisable(!stage);
        btnNextGVGrade.setDisable(stage);
        btnPreviousGVGrade.setDisable(stage);
        txtFromTotal.setDisable(stage);
        txtToTotal.setDisable(stage);
        btnBoderlineCasesTolaracne.setDisable(stage);
        btnBorderlineCasesRange.setDisable(stage);
        btnFindStudentWithGrade.setDisable(stage);

    }

    @Override
    public void setUpdateButtonLabel(String labelX) {
        btnUpdaeMark.setText(labelX);
    }

    @Override
    public void setRevertResult(String result) {
        textAreaResult.setText(textAreaResult.getText().trim() + "\n" + result);
    }

    @Override
    public String getFromTotal() {
        return txtFromTotal.getText().trim();
    }

    @Override
    public String getToTotal() {
        return txtToTotal.getText().trim();
    }

    @Override
    public void setTextBoxGradeResult(String msg) {
        textResultFindingStudentGivenGrade.setText(msg);
    }

    @Override
    public void setLabelFindByGrade(String MSG) {
        labelFIndByGrade.setText(MSG);
    }

    @Override
    public void clearResult() {
        textAreaResult.setText("");
    }

}
