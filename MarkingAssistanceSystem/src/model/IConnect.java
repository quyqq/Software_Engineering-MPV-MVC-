/**
 * @author Quyet Quang Quy ID: 12118217
 * @version 1.0
 *
 * @see java.sql.SQLException;
 *
 * this interface is to be implement to all model
 */
package model;

import java.sql.SQLException;

public interface IConnect {

    /**
     * This method is to create connection to database when the system fire up
     *
     * @throws SQLException
     */
    public void connect() throws SQLException;

    /**
     * this method is to close the current connection when system closing
     *
     * @throws SQLException
     */
    public void disconnect() throws SQLException;

    /**
     * this method is to create all queries after the connection is created
     *
     * @throws SQLException
     */
    public void initialise() throws SQLException;

}
