/**
 * 
 */
package cn.medicine.neo4j;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Dell
 * neo4j数据库连接以及配置
 */
//@Component
public class Neo4jConfiguration {

//	@Autowired
	public static final Driver driver = GraphDatabase.driver("bolt://47.94.174.82:7687", AuthTokens.basic("neo4j", "sa"));
	
}
