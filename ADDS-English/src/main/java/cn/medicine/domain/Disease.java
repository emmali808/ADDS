/**
 * 
 */
package cn.medicine.domain;

/**
 * @author Dell
 *
 */
public class Disease {

	private String DiseaseID;
	
	private String Name;
	
	private String Alias;
	
	private String ICD9_CODE;

	public String getDiseaseID() {
		return DiseaseID;
	}

	public void setDiseaseID(String diseaseID) {
		DiseaseID = diseaseID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getAlias() {
		return Alias;
	}

	public void setAlias(String alias) {
		Alias = alias;
	}

	public String getICD9_CODE() {
		return ICD9_CODE;
	}

	public void setICD9_CODE(String iCD9_CODE) {
		ICD9_CODE = iCD9_CODE;
	}

	public Disease(String diseaseID, String name, String alias, String iCD9_CODE) {
		super();
		DiseaseID = diseaseID;
		Name = name;
		Alias = alias;
		ICD9_CODE = iCD9_CODE;
	}
	
	public Disease() {}
	
}
