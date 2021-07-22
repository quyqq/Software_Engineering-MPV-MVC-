/**
 * @author Quyet Quang Quy ID: 12118217
 * @version 1.0 
 * this Enum is to store all the queries name in the system.
 * it makes managing query much easier
 */
package model;


public enum EStatementTags {
    INSERT,
    UPDATE,
    FIND_ALL,// find all student
    FIND_ONE,// only one student by giving student ID
    FIND_BORDERLINE_CASES_EXPLICIT_RAGE,//find student with given range   
    FIND_BORDERLINE_CASES_TOLARANCE,// for all grade
    FIND_STUDENT_WITH_GIVEN_GRADE// find students with given grade
}
