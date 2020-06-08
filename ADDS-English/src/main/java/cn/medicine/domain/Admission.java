/**
 * 
 */
package cn.medicine.domain;

/**
 * @author Dell
 *
 */
public class Admission {

	private String AdmissionID;
	
	private String AdmitTime;
	
	private String Days;
	
	private String Hadm_ID;
	
	private String Flag;

	public String getAdmissionID() {
		return AdmissionID;
	}

	public void setAdmissionID(String admissionID) {
		AdmissionID = admissionID;
	}

	public String getAdmitTime() {
		return AdmitTime;
	}

	public void setAdmitTime(String admitTime) {
		AdmitTime = admitTime;
	}

	public String getDays() {
		return Days;
	}

	public void setDays(String days) {
		Days = days;
	}

	public String getHadm_ID() {
		return Hadm_ID;
	}

	public void setHadm_ID(String hadm_ID) {
		Hadm_ID = hadm_ID;
	}

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}

	public Admission(String admissionID, String admitTime, String days, String hadm_ID, String flag) {
		super();
		AdmissionID = admissionID;
		AdmitTime = admitTime;
		Days = days;
		Hadm_ID = hadm_ID;
		Flag = flag;
	}
	
	public Admission() {}
	
}
