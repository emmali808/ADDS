/**
 * 
 */
package cn.medicine.neo4j;

import cn.medicine.domain.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dell
 * 查询实体间关系
 */
public class Neo4jRelation {

	public Driver driver=Neo4jConfiguration.driver;
	public Session session=driver.session();
	
	
	public static String isNullOrEmpty(String s) {
		return s==null?"":s;
	}
	
	/**
	 * 查询诊断情况
	 * @return
	 */
	public String cypherDiagnosis() {
		
		//diagnosis连接查询
		StatementResult rsLink = session.run("MATCH p=(admission)-[r:Diagnosis]->(disease) RETURN admission,disease limit 500");
		
		List<LinkJson> linkList=new ArrayList<LinkJson>();
		
		while(rsLink.hasNext()) {
			Record record=rsLink.next();
			
			Map admissionMap=record.get("admission").asNode().asMap();
			String admissionID=admissionMap.get("HADM_ID").toString();			
			Admission admission=new Admission();
			admission.setAdmitTime(admissionMap.get("ADMITTIME").toString());
			admission.setDays(admissionMap.get("DAYS").toString());
			admission.setFlag(admissionMap.get("FLAG").toString());
			admission.setHadm_ID(admissionID);

			Map diseaseMap=record.get("disease").asNode().asMap();
			Disease disease=new Disease();
			disease.setAlias(diseaseMap.get("ALIAS").toString());
			disease.setICD9_CODE(diseaseMap.get("ICD9_CODE").toString());
			disease.setName(diseaseMap.get("name").toString());
			
			JSONObject admissionJson=JSONObject.fromObject(admission);
			JSONObject diseaseJson=JSONObject.fromObject(disease);
		
			
			linkList.add(new LinkJson(admissionJson.toString(),diseaseJson.toString(),"diagnosis"));
		}
		
		
		session.close();
		driver.close();
		JSONArray linkArray=JSONArray.fromObject(linkList);
		String s=linkArray.toString();
//		String s=admissionArray.toString()+"|"+diseaseArray.toString()+"|"+linkArray.toString();
		return s;
		
		
	}
	
	/**
	 * 查询疾病种类
	 * @return
	 */
	public String cypherDiseaseCategory() {
		
		//diseasecategory连接查询
		StatementResult rsLink = session.run("MATCH p=(disease)-[r:DiseaseCategory]->(disease_category) RETURN disease,disease_category");
		
		List<LinkJson> linkList=new ArrayList<LinkJson>();
		
		while(rsLink.hasNext()) {
			Record record=rsLink.next();
			
			Map diseaseMap=record.get("disease").asNode().asMap();
			Disease disease=new Disease();
			disease.setName(diseaseMap.get("name").toString());
			disease.setAlias(diseaseMap.get("ALIAS").toString());
			disease.setICD9_CODE(diseaseMap.get("ICD9_CODE").toString());

			Map diseaseCategoryMap=record.get("disease_category").asNode().asMap();
			DiseaseCategory disease_category=new DiseaseCategory();
			disease_category.setName(diseaseCategoryMap.get("name").toString());
			
			JSONObject diseaseJson=JSONObject.fromObject(disease);
			JSONObject disease_categoryJson=JSONObject.fromObject(disease_category);
		
			linkList.add(new LinkJson(diseaseJson.toString(),disease_categoryJson.toString(),"diseasecategory"));
		}
		
		
		session.close();
		driver.close();
		JSONArray linkArray=JSONArray.fromObject(linkList);
		String s=linkArray.toString();
//		String s=admissionArray.toString()+"|"+diseaseArray.toString()+"|"+linkArray.toString();
		return s;
				
	}
	
	/**
	 * 查询药物种类
	 * @return
	 */
	public String cypherDrugCategory() {
		
		//diseasecategory连接查询
		StatementResult rsLink = session.run("MATCH p=(drug)-[r:DrugCategory]->(drugcategory) RETURN drug,drugcategory");
		
		List<LinkJson> linkList=new ArrayList<LinkJson>();
		
		while(rsLink.hasNext()) {
			Record record=rsLink.next();
			
			Map drugMap=record.get("drug").asNode().asMap();
			Drug drug=new Drug();
			drug.setAlias(drugMap.get("ALIAS").toString());
			drug.setDose_unit_rx(drugMap.get("DOSE_UNIT_RX").toString());
			drug.setDose_val_rx(drugMap.get("DOSE_VAL_RX").toString());
			drug.setName(drugMap.get("name").toString());
			drug.setICD9_CODE(drugMap.get("ICD9_CODE").toString());

			Map drugCategoryMap=record.get("drugcategory").asNode().asMap();
			DrugCategory drugcategory=new DrugCategory();
			drugcategory.setName(drugCategoryMap.get("name").toString());
			
			JSONObject drugJson=JSONObject.fromObject(drug);
			JSONObject drugCategoryJson=JSONObject.fromObject(drugcategory);
		
			linkList.add(new LinkJson(drugJson.toString(),drugCategoryJson.toString(),"drugcategory"));
		}
		
		
		session.close();
		driver.close();
		JSONArray linkArray=JSONArray.fromObject(linkList);
		String s=linkArray.toString();
//		String s=admissionArray.toString()+"|"+diseaseArray.toString()+"|"+linkArray.toString();
		return s;
				
	}
	
	
	/**
	 * 查询病例
	 * @return
	 */
	public String cypherHasCases() {
		
		//病人入院情况连接查询
		StatementResult rsLink = session.run("MATCH p=(patient)-[r:HasCases]->(admission) RETURN patient,admission");
		
		List<LinkJson> linkList=new ArrayList<LinkJson>();
		
		while(rsLink.hasNext()) {
			Record record=rsLink.next();
			
			Map patientMap=record.get("patient").asNode().asMap();
			Patient patient=new Patient();
			patient.setBirthtime(patientMap.get("BIRTHTIME").toString());
			patient.setEthnicity(patientMap.get("ETHNICITY").toString());
			patient.setGender(patientMap.get("GENDER").toString());
			Object obj=patientMap.get("RELIGION");
			String region="";
			if(obj!=null) {
				region=obj.toString();
			}
			patient.setReligion(region);
			
			Map admissionMap=record.get("admission").asNode().asMap();
			Admission admission=new Admission();
			admission.setAdmitTime(admissionMap.get("ADMITTIME").toString());
			admission.setDays(admissionMap.get("DAYS").toString());
			admission.setFlag(admissionMap.get("FLAG").toString());
			admission.setHadm_ID(admissionMap.get("HADM_ID").toString());
			
			JSONObject patientJson=JSONObject.fromObject(patient);
			JSONObject admissionJson=JSONObject.fromObject(admission);
		
			linkList.add(new LinkJson(patientJson.toString(),admissionJson.toString(),"hascases"));
		}
		
		
		session.close();
		driver.close();
		JSONArray linkArray=JSONArray.fromObject(linkList);
		String s=linkArray.toString();
//		String s=admissionArray.toString()+"|"+diseaseArray.toString()+"|"+linkArray.toString();
		return s;
				
	}
	
	/**
	 * 查询用药情况
	 * @return
	 */
	public String cypherMedication() {
		
		//用药情况连接查询
		StatementResult rsLink = session.run("MATCH p=(drug)-[r:Medication]->(disease) RETURN drug,disease,r");
		
		List<LinkJson> linkList=new ArrayList<LinkJson>();
		
		while(rsLink.hasNext()) {
			Record record=rsLink.next();
			
			Map drugMap=record.get("drug").asNode().asMap();
			Drug drug=new Drug();
			drug.setAlias(drugMap.get("ALIAS").toString());
			drug.setDose_unit_rx(drugMap.get("DOSE_UNIT_RX").toString());
			drug.setDose_val_rx(drugMap.get("DOSE_VAL_RX").toString());
			drug.setName(drugMap.get("name").toString());
			drug.setICD9_CODE(drugMap.get("ICD9_CODE").toString());
			
			Map diseaseMap=record.get("disease").asNode().asMap();
			Disease disease=new Disease();
			disease.setName(diseaseMap.get("name").toString());
			disease.setAlias(diseaseMap.get("ALIAS").toString());
			disease.setICD9_CODE(diseaseMap.get("ICD9_CODE").toString());
			
			JSONObject drugJson=JSONObject.fromObject(drug);
			JSONObject diseaseJson=JSONObject.fromObject(disease);
			
			Map linkMap=record.get("r").asRelationship().asMap();		
		
			linkList.add(new LinkJson(drugJson.toString(),diseaseJson.toString(),linkMap.get("COUNT").toString(),linkMap.get("FREQUENCY").toString()));
		}
		
		
		session.close();
		driver.close();
		JSONArray linkArray=JSONArray.fromObject(linkList);
		String s=linkArray.toString();
//		String s=admissionArray.toString()+"|"+diseaseArray.toString()+"|"+linkArray.toString();
		return s;
				
	}
	
	
	
}
