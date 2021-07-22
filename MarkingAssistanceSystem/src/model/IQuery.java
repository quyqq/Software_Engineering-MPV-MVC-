/**
 * @author Quyet Quang Quy ID: 12118217
 * @version 1.0 
 * 
 * @see java.sql.SQLException;
 * @see java.util.List;
 */
package model;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * this interface is to show all queries can be used 
 * @param <TypeOfQuery> must be type EStatementTags
 * @param <Data> must be type IEntityMarker (a marker)
 */
public interface IQuery<TypeOfQuery extends EStatementTags, Data extends IEntityMarker> {

    /**
     * generic method to handle all insert delete update.
     * however, this assignment dose not require to have delete feature
     * @param typeQ
     * @param dataIn
     * @return
     * @throws SQLException
     */
    public int command(TypeOfQuery typeQ, Data dataIn) throws SQLException;

    /**
     * this method is a generic method to retrieve data from database
     * @param <ConditionSelect> generic array type
     * @param typeQ type of query must be in EStatementTags Enum
     * @param keys array keys can be zero or many
     * @return list of generic type
     * @throws SQLException
     */
    public <ConditionSelect> List<Data> select(TypeOfQuery typeQ, ConditionSelect... keys) throws SQLException;

}
