/**
 * @author Youngjae Lee
 * @version 2022-02-03
 *
 * description: Configuration
 */

package common.config;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.annotation.sql.DataSourceDefinitions;

@DataSourceDefinitions({

	@DataSourceDefinition(
	 	name="java:app/datasources/oracleUser2015DS",
	 	className="oracle.jdbc.pool.OracleDataSource",
	 	url="jdbc:oracle:thin:@localhost:1521/xepdb1",
	 	user="user2015",
	 	password="Password2015"),

})

@ApplicationScoped
public class ApplicationConfig {

}

