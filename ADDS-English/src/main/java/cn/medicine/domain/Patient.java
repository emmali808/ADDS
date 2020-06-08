/**
 * 
 */
package cn.medicine.domain;

/**
 * @author Dell
 *
 */
public class Patient {
	private String PatientID;
	
	private String Gender;
	
	private String Religion;
	
	private String Ethnicity;
	
	private String Birthtime;

	public Patient() {
		
	}
	
	public Patient(String patientID, String gender, String religion, String ethnicity, String birthtime) {
		super();
		PatientID = patientID;
		Gender = gender;
		Religion = religion;
		Ethnicity = ethnicity;
		Birthtime = birthtime;
	}

	public String getPatientID() {
		return PatientID;
	}

	public void setPatientID(String patientID) {
		PatientID = patientID;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getReligion() {
		return Religion;
	}

	public void setReligion(String religion) {
		Religion = religion;
	}

	public String getEthnicity() {
		return Ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		Ethnicity = ethnicity;
	}

	public String getBirthtime() {
		return Birthtime;
	}

	public void setBirthtime(String birthtime) {
		Birthtime = birthtime;
	}
	
	
	
}
