package cn.medicine.graph.controller;

import cn.medicine.graph.entity.GraphEntity;
import cn.medicine.graph.entity.UmlEntity;
import cn.medicine.graph.service.UmlGraphService;
import cn.medicine.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;


@Controller
@RequestMapping("/graph")
public class GraphController {

    @Autowired
    UmlGraphService umlGraphService;

    @ResponseBody
    @RequestMapping("/getAll")
    public ArrayList<UmlEntity> getAllUmlEntity(){
        return umlGraphService.getAllUmlEntity();
    }

    @ResponseBody
    @RequestMapping("/getUmlEntityById")
    public UmlEntity getUmlEntityById(Long id){
        return umlGraphService.searchUmlEntity(id);
    }

    @ResponseBody
    @RequestMapping("/getUmlEntityDeeply")
    public GraphEntity getUmlEntityDeeply(Long id, int deep){
        return umlGraphService.searchUmlEntityDeeply(id,deep);
    }
}
