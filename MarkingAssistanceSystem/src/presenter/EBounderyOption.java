/**
 * @author Quyet Quang Quy ID: 12118217
 * @version 1.0 
 * this Enum is to store all boundaries and range and it is use for combo box on GUI
 * and it is use on finding borderline cases
 */
package presenter;


public enum EBounderyOption {
    OVER_85(85, 100, "From 85 to 100 (HD)", "HD"),// range from 85 to 100 with HD 
    FROM_75_TO_84(75, 84, "From 75 to 84 (D)", "D"),// range from 75 to 84 with D 
    FROM_65_TO_74(65, 74, "From 65 to 74 (C)", "C"),// range from 65 to 74 with V 
    FROM_50_TO_64(50, 64, "From 50 to 64 (P)", "P"),// range from 50 to 64 with P 
    LESS_THEN_50_F(0, 49, "Less than 50 (F)", "F"),// les than 50 can be F , SA, SE or AF 
    LESS_THEN_50_SA(0, 49, "Less than 50 (SA)", "SA"),
    LESS_THEN_50_AF(0, 49, "Less than 50 (AF)", "AF"),
    LESS_THEN_50_SE(0, 49, "Less than 50 (SE)", "SE");

    private final int fromM;
    private final int toM;
    private final String userInterfaceMsg;
    private final String userInterfaceGrade;

    private EBounderyOption(int fromM, int toM, String userInterfaceMsg, String userInterfaceGrade) {
        this.fromM = fromM;
        this.toM = toM;
        this.userInterfaceMsg = userInterfaceMsg;
        this.userInterfaceGrade = userInterfaceGrade;
    }

    public int getFromM() {
        return fromM;
    }

    public int getToM() {
        return toM;
    }

    public String getUserInterfaceMsg() {
        return userInterfaceMsg;
    }

    public String getUserInterfaceGrade() {
        return userInterfaceGrade;
    }

    @Override
    public String toString() {
        return userInterfaceMsg;
    }
    

}
