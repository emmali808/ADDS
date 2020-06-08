package cn.medicine.pojo;

import com.alibaba.fastjson.JSONArray;

/**
 * @ClassName:
 * @Description: TODO
 * @Function List: TODO
 * @author: ytchen
 * @version:
 * @Date: 2016/9/19
 * @History: //历史修改记录
 * <author>  // 修改人
 * <time> //修改时间
 * <version> //版本
 * <desc> // 描述修改内容
 */
public class PatientGraph {
    String time;
    JSONArray nodes;
    JSONArray links;
    public PatientGraph() {
    }
    public PatientGraph(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public JSONArray getNodes() {
        return nodes;
    }

    public void setNodes(JSONArray nodes) {
        this.nodes = nodes;
    }

    public JSONArray getLinks() {
        return links;
    }

    public void setLinks(JSONArray links) {
        this.links = links;
    }
}
