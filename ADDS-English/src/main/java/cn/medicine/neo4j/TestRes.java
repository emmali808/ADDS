/**
 * 
 */
package cn.medicine.neo4j;

/**
 * @author Dell
 *
 */
public class TestRes {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Neo4jOperation operation=new Neo4jOperation();
		String s=operation.cypherPatient();
//		Neo4jRelation relation=new Neo4jRelation();
//		String s=relation.cypherMedication();
		System.out.println();
	}

}
