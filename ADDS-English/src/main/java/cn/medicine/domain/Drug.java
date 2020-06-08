/**
 * 
 */
package cn.medicine.domain;

/**
 * @author Dell
 *
 */
public class Drug {

	private String DrugID;
	
	private String Name;
	
	private String Alias;
	
	private String Dose_val_rx;
	
	private String Dose_unit_rx;
	
	private String ICD9_CODE;

	public String getDrugID() {
		return DrugID;
	}

	public void setDrugID(String drugID) {
		DrugID = drugID;
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

	public String getDose_val_rx() {
		return Dose_val_rx;
	}

	public void setDose_val_rx(String dose_val_rx) {
		Dose_val_rx = dose_val_rx;
	}

	public String getDose_unit_rx() {
		return Dose_unit_rx;
	}

	public void setDose_unit_rx(String dose_unit_rx) {
		Dose_unit_rx = dose_unit_rx;
	}

	public String getICD9_CODE() {
		return ICD9_CODE;
	}

	public void setICD9_CODE(String iCD9_CODE) {
		ICD9_CODE = iCD9_CODE;
	}

	public Drug() {
		
	}
	
	public Drug(String drugID, String name, String alias, String dose_val_rx, String dose_unit_rx, String iCD9_CODE) {
		super();
		DrugID = drugID;
		Name = name;
		Alias = alias;
		Dose_val_rx = dose_val_rx;
		Dose_unit_rx = dose_unit_rx;
		ICD9_CODE = iCD9_CODE;
	}
	
	
	
}
