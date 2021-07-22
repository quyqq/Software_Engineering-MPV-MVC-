/**
 * @author Quyet Quang Quy ID: 12118217
 * @version 1.0
 *
 * @see java.sql.Connection;
 * @see java.sql.DriverManager;
 * @see java.sql.PreparedStatement;
 * @see java.sql.ResultSet;
 * @see java.sql.SQLException;
 * @see java.util.EnumMap;
 * @see java.util.LinkedList;
 * @see java.util.List;
 *
 *
 * this class is to create all queries and connection and it handles all
 * database processing
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

public class StudentMarkModel implements IConnect, IQuery<EStatementTags, StudentMark> {

    private final String URL = "jdbc:derby://localhost:1527/StudentMark";
    private final String USER_NAME = "sa";
    private final String PASSWORD = "123456";

    private final EnumMap<EStatementTags, PreparedStatement> listOfStatements// map of all queries 
            = new EnumMap<>(EStatementTags.class);
    private Connection c;

    /**
     * create a connection
     *
     * @throws SQLException
     */
    @Override
    public void connect() throws SQLException {

        c = DriverManager.getConnection(URL, USER_NAME, PASSWORD);

    }

    /**
     * close connection
     *
     * @throws SQLException
     */
    @Override
    public void disconnect() throws SQLException {
        listOfStatements.clear();
        c.close();

    }

    /**
     * create all the queries when system is fired up
     *
     * @throws SQLException
     */
    @Override
    public void initialise() throws SQLException {
        for (EStatementTags tag : EStatementTags.values()) {
            listOfStatements.put(tag, createPreparedStatements(tag));
        }
    }

    /**
     * generic method to handle all insert delete update. however, this
     * assignment dose not require to have delete feature
     *
     * @param typeQ
     * @param dataIn
     * @return
     * @throws SQLException
     */
    @Override
    public int command(EStatementTags typeQ, StudentMark dataIn) throws SQLException {

        switch (typeQ) {
            case INSERT: {

                //call method to set data to prepared statement 
                return setValues(
                        listOfStatements.get(typeQ),
                        dataIn.getStudentID().toUpperCase(),
                        dataIn.getAssignment1(),
                        dataIn.getAssignment2(),
                        dataIn.getExam(),
                        dataIn.getTotal(),
                        dataIn.getGrade()
                ).executeUpdate();
            }
            case UPDATE: {
                //call method to set data to prepared statement 
                return setValues(
                        listOfStatements.get(typeQ),
                        dataIn.getAssignment1(),
                        dataIn.getAssignment2(),
                        dataIn.getExam(),
                        dataIn.getTotal(),
                        dataIn.getGrade(),
                        dataIn.getStudentID().toUpperCase()
                ).executeUpdate();
            }
            default: {

                System.err.println("Can not find the execute update statement tag.");
                return -1;
            }
        }

    }

    /**
     * this method is to retrieve data from database depend on what kind of
     * defined queries (Generic method)
     *
     * @param <ConditionSelect> generic type
     * @param typeQ type of queries which is show in EStatementTags Enum
     * @param conditionselects conditions can be zero or many
     * @return list of StudentMark
     * @throws SQLException
     */
    @Override
    @SuppressWarnings("unchecked")
    public <ConditionSelect> List<StudentMark> select(EStatementTags typeQ, ConditionSelect... conditionselects) throws SQLException {

        
        LinkedList<StudentMark> listOfStudent = new LinkedList<>();

        //call method to set data to prepared statement 
        ResultSet result = setValues(listOfStatements.get(typeQ), conditionselects).executeQuery();

        //add data to LinkedList if is not null
        if (result != null) {
            while (result.next()) {

                listOfStudent.add(new StudentMark(
                        result.getString("STUDENTID"),
                        result.getInt("ASSIGNMENT1"),
                        result.getInt("ASSIGNMENT2"),
                        result.getInt("EXAM"),
                        result.getInt("TOTAL"),
                        result.getString("GRADE")
                ));

            }
        }
        return listOfStudent;
    }

    /**
     * this method is to set all the given value to PreparedStatement depend on
     * the type of parameters
     *
     * @param ps given PreparedStatement
     * @param values array of values
     * @return PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement setValues(PreparedStatement ps, Object... values) throws SQLException {
        for (int i = 1; i < values.length + 1; i++) {
            if (values[i - 1] instanceof String) {// check type of data
                ps.setString(i, values[i - 1].toString());
            }
            if (values[i - 1] instanceof Integer) {// check type of data
                ps.setInt(i, Integer.valueOf(values[i - 1].toString()));
            }

        }
        return ps;
    }

    /**
     * this method is to create PreparedStatement
     *
     * @param type type of query is shown in EStatementTags Enum
     * @return PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement createPreparedStatements(EStatementTags type) throws SQLException {
        PreparedStatement statement = null;
        switch (type) {
            case INSERT: {
                statement = c.prepareStatement(
                        "INSERT "
                        + "INTO MARKS (STUDENTID,ASSIGNMENT1,ASSIGNMENT2,EXAM,TOTAL,GRADE) "
                        + "VALUES (UPPER(?),?,?,?,?,?) "
                );
            }
            break;
            case UPDATE: {
                statement = c.prepareStatement(
                        "UPDATE "
                        + "MARKS "
                        + "SET ASSIGNMENT1 = ?, ASSIGNMENT2 = ?, EXAM = ? , TOTAL = ? , GRADE = ? "
                        + "WHERE UPPER(STUDENTID) = ?"
                );
            }
            break;
            case FIND_ALL: {
                statement = c.prepareStatement("SELECT * FROM MARKS ORDER BY TOTAL ASC");
            }
            break;
            case FIND_ONE: {
                statement = c.prepareStatement(
                        "SELECT * FROM MARKS "
                        + "WHERE UPPER(STUDENTID) = ?"
                        + " ORDER BY TOTAL DESC"
                );
            }
            break;
            case FIND_BORDERLINE_CASES_EXPLICIT_RAGE: {
                statement = c.prepareStatement("SELECT * FROM MARKS "
                        + "WHERE TOTAL >= ? AND TOTAL <= ?"
                        + " ORDER BY TOTAL DESC"
                );
            }
            break;
            case FIND_BORDERLINE_CASES_TOLARANCE: {
                statement = c.prepareStatement(
                        "SELECT * FROM MARKS "
                        + "WHERE "
                        + "(TOTAL >= ? AND  TOTAL <= ?) OR "
                        + "(TOTAL >= ? AND  TOTAL <= ?) OR "
                        + "(TOTAL >= ? AND  TOTAL <= ?) OR "
                        + "(TOTAL >= ? AND  TOTAL <= ?)"
                        + " ORDER BY TOTAL DESC"
                );
            }
            break;
            case FIND_STUDENT_WITH_GIVEN_GRADE: {
                statement = c.prepareStatement(
                        "SELECT * FROM MARKS "
                        + "WHERE "
                        + "GRADE = ?"
                        + " ORDER BY TOTAL ASC"
                );
            }
            break;
            default: {
                System.err.println("Can not find the statement tag.");
            }
        }

        return statement;
    }

}
