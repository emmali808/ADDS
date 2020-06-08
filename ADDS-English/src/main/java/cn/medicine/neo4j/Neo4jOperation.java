/**
 * 
 */
package cn.medicine.neo4j;

import cn.medicine.domain.*;
import net.sf.json.JSONArray;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.soap.Addressing;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dell
 * 查询实体属性
 */
//@Component
public class Neo4jOperation {

//	@Autowired
	public Neo4jConfiguration neo4jConfiguration;

	/**
	 * 查询病人数据
	 */
	public String cypherPatient() {
		Driver driver = GraphDatabase.driver("bolt://47.94.174.82:7687", AuthTokens.basic("neo4j", "sa"));
		Session session = driver.session();
		StatementResult rs = session.run("MATCH (n:Patient) RETURN n.GENDER as gender,n.RELIGION as religion,n.ETHNICITY as ethnicity,n.BIRTHTIME as birthtime");
		List<Patient> patientList=new ArrayList<Patient>();
		while(rs.hasNext()) {
			Record record=rs.next();
			Patient patient=new Patient();
			patient.setGender(record.get("gender").asString());
			patient.setReligion(record.get("religion").asString());
			patient.setEthnicity(record.get("ethnicity").asString());
			patient.setBirthtime(record.get("birthtime").asString());
			patientList.add(patient);
		}
		session.close();
		driver.close();
		JSONArray listArray=JSONArray.fromObject(patientList);
		String s=listArray.toString();
		return s;
	}
	
	/**
	 * 
	 * 查询药品数据
	 */
	public  String cypherDrug() {
		Driver driver = GraphDatabase.driver("bolt://47.94.174.82:7687", AuthTokens.basic("neo4j", "sa"));
		Session session = driver.session();
		StatementResult rs = session.run("MATCH (n:Drug) RETURN n.name as name,n.ALIAS as alias,n.DOSE_VAL_RX as dose_val_rx,n.DOSE_UNIT_RX as dose_unit_rx,n.ICD9_CODE as icd9_code");
		List<Drug> drugList=new ArrayList<Drug>();
		while(rs.hasNext()) {
			Record record=rs.next();
			Drug drug=new Drug();
			drug.setAlias(record.get("name").asString());
			drug.setDose_unit_rx(record.get("dose_unit_rx").asString());
			drug.setDose_val_rx(record.get("dose_val_rx").asString());
			drug.setICD9_CODE(record.get("icd9_code").asString());
			drug.setName(record.get("name").asString());
			drugList.add(drug);
		}
		session.close();
		driver.close();
		JSONArray listArray=JSONArray.fromObject(drugList);
		String s=listArray.toString();
		return s;
	}
	
	/**
	 * 查询疾病
	 * @return
	 */
	public String cypherDisease() {
		Driver driver = GraphDatabase.driver("bolt://47.94.174.82:7687", AuthTokens.basic("neo4j", "sa"));
		Session session = driver.session();
		StatementResult rs = session.run("MATCH (n:Disease) RETURN n.name as name,n.ALIAS as alias,n.ICD9_CODE as icd9_code");
		List<Disease> diseaseList=new ArrayList<Disease>();
		while(rs.hasNext()) {
			Record record=rs.next();
			Disease disease=new Disease();			
			disease.setICD9_CODE(record.get("icd9_code").asString());
			disease.setName(record.get("name").asString());
			disease.setAlias(record.get("alias").asString());
			diseaseList.add(disease);
		}
		session.close();
		driver.close();
		JSONArray listArray=JSONArray.fromObject(diseaseList);
		String s=listArray.toString();
		return s;
	}
	
	/**
	 * 查询入院情况
	 * @return
	 */
	public String cypherAdmission() {
		Driver driver = GraphDatabase.driver("bolt://47.94.174.82:7687", AuthTokens.basic("neo4j", "sa"));
		Session session = driver.session();
		StatementResult rs = session.run("MATCH (n:Admission) RETURN n.ADMITTIME as admittime,n.DAYS as days,n.HADM_ID as hadm_id,n.FLAG as flag");
		List<Admission> admissionList=new ArrayList<Admission>();
		while(rs.hasNext()) {
			Record record=rs.next();
			Admission admission=new Admission();
			admission.setAdmitTime(record.get("admittime").asString());
			admission.setDays(record.get("days").asString());
			admission.setHadm_ID(record.get("hadm_id").asString());
			admissionList.add(admission);
		}
		session.close();
		driver.close();
		JSONArray listArray=JSONArray.fromObject(admissionList);
		String s=listArray.toString();
		return s;
	}
	
	/**
	 * 查询药物种类
	 * @return
	 */
	public String cypherDrugCategory() {
		Driver driver = GraphDatabase.driver("bolt://47.94.174.82:7687", AuthTokens.basic("neo4j", "sa"));
		Session session = driver.session();
		StatementResult rs = session.run("MATCH (n:Drug_Category) RETURN n.name as name");
		List<DrugCategory> categoryList=new ArrayList<DrugCategory>();
		while(rs.hasNext()) {
			Record record=rs.next();
			DrugCategory drugcategory=new DrugCategory(record.get("name").asString());
			categoryList.add(drugcategory);
		}
		session.close();
		driver.close();
		JSONArray listArray=JSONArray.fromObject(categoryList);
		String s=listArray.toString();
		return s;
	}
	
	/**
	 * 查询疾病种类
	 * @return
	 */
	public String cypherDiseaseCategory() {
		Driver driver = GraphDatabase.driver("bolt://47.94.174.82:7687", AuthTokens.basic("neo4j", "sa"));
		Session session = driver.session();
		StatementResult rs = session.run("MATCH (n:Disease_Category) RETURN n.name as name");
		List<DiseaseCategory> categoryList=new ArrayList<DiseaseCategory>();
		while(rs.hasNext()) {
			Record record=rs.next();
			DiseaseCategory diseasecategory=new DiseaseCategory(record.get("name").asString());
			categoryList.add(diseasecategory);
		}
		session.close();
		driver.close();
		JSONArray listArray=JSONArray.fromObject(categoryList);
		String s=listArray.toString();
		return s;
	}

}
