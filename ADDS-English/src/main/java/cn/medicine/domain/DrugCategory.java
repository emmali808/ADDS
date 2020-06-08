/**
 * 
 */
package cn.medicine.domain;

/**
 * @author Dell
 * 药物种类名称
 *
 */
public class DrugCategory {
	
	private String name;	
	
	public DrugCategory() {
		super();
	}

	public DrugCategory(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
