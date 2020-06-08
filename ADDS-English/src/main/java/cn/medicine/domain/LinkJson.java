/**
 * 
 */
package cn.medicine.domain;

/**
 * @author Dell
 * 关系类
 */
public class LinkJson {

	private String source;
	
	private String target;
	
	private String type;
		

	public LinkJson(String source, String target, String linkType) {
		super();
		this.source = source;
		this.target = target;
		this.type = linkType;
	}
	
	public LinkJson(String source, String target, String count,String frequency) {
		super();
		this.source = source;
		this.target = target;
		this.type = "count:"+count+",frenquency:"+frequency;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getLinkType() {
		return type;
	}

	public void setLinkType(String linkType) {
		this.type = linkType;
	}
	
	
	
}
