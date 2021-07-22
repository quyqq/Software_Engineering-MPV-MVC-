/**
 * @author Quyet Quang Quy ID: 12118217
 * @version 1.0 
 * this Enum is to store the maximum mark and passing mark of all the assessments
 */
package presenter;


public enum EAssetMaxMark {
    ASSIGN_1(20,10),// 20 is maximum mark, 10 the mark for pass this assessment
    ASSIGN_2(30,15),// 30 is maximum mark, 15 the mark for pass this assessment
    EXAM(50,25);// 50 is maximum mark, 25 the mark for pass this assessment
    
    private final int maxImunMark;
    private final int passMark;

    private EAssetMaxMark(int maxImunMark, int passMark) {
        this.maxImunMark = maxImunMark;
        this.passMark = passMark;
    }

    public int getMaxImunMark() {
        return maxImunMark;
    }

    public int getPassMark() {
        return passMark;
    }

  
    
    
}
