/**
 * @author Quyet Quang Quy ID: 12118217
 * @version 1.0
 * @see java.util.List; this interface is showing all method to get and set data
 * to GUI
 */
package view;

import java.util.List;

public interface IView {

    /**
     *
     * @return
     */
    public String getSearchingKeyWord();

    /**
     *
     * @return
     */
    public String getStudentID();

    /**
     *
     * @param id
     */
    public void setStudentID(String id);

    /**
     *
     * @return
     */
    public String getAssginment1();

    /**
     *
     * @param mark
     */
    public void setAssginment1(String mark);

    /**
     *
     * @return
     */
    public String getAssginment2();

    /**
     *
     * @param mark
     */
    public void setAssginment2(String mark);

    /**
     *
     * @return
     */
    public String getExam();

    /**
     *
     * @param mark
     */
    public void setExam(String mark);

    /**
     *
     * @param mark
     */
    public void setTotal(String mark);

    /**
     *
     * @param grade
     */
    public void setGrade(String grade);

    /**
     *
     * @return
     */
    public String getTolarance();

    /**
     *
     * @return
     */
    public String getSelectedGrade();

    /**
     *
     * @param options
     */
    public void setCombobox(List<String> options);

    /**
     *
     * @param indexV
     */
    public void setLableIndex(String indexV);

    /**
     *
     * @param result
     */
    public void setRevertResult(String result);

    /**
     *
     * @param result
     */
    public void setResult(String result);

    /**
     *
     * @param msg
     */
    public void showErrorDialog(String msg);

    /**
     *
     * @param stage
     */
    public void setDisdableStageForInsert(boolean stage);

    /**
     *
     * @param labelx
     */
    public void setButtonInsertNewStudentLabel(String labelx);

    /**
     *
     * @param stage
     */
    public void setDisdableStageForUpdate(boolean stage);

    /**
     *
     * @param labelX
     */
    public void setUpdateButtonLabel(String labelX);

    /**
     *
     * @return
     */
    public String getFromTotal();

    /**
     *
     * @return 
     */
    public String getToTotal();
    
    /**
     *
     * @param msg
     */
    public void setTextBoxGradeResult(String msg);
    
    /**
     *
     * @param MSG
     */
    public void setLabelFindByGrade(String MSG);
    
    public void clearResult();
}
