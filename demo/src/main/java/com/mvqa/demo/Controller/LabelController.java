package com.mvqa.demo.Controller;

import com.mvqa.demo.Mapper.ctInfoPoMapper;
import com.mvqa.demo.Mapper.ctValPoMapper;
import com.mvqa.demo.entity.Photo;

import com.mvqa.demo.model.po.ctInfoPo;
import com.mvqa.demo.model.po.ctInfoPoExample;
import com.mvqa.demo.model.po.ctValPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="", produces = "application/json;charset=UTF-8")
public class LabelController {
    @Autowired
    private ctInfoPoMapper ctInfoPoMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetController.class);

    @Autowired
    private ctValPoMapper ctValPoMapper;

    @PostMapping("/vqa/{dataset}")
    public void uploadLabel(HttpServletResponse response, @PathVariable("dataset") String dataset,@RequestParam("patientId") String patientId, @RequestParam("diaList") String diaList, @RequestParam("photoId") String photoId,@RequestParam("description") String description,@RequestParam("boneName") String boneName,@RequestParam("direction") String direction,@RequestParam("type") String type,@RequestParam("position") String position) {
        Photo photo=new Photo();
        photo.setPatientId(patientId);
        photo.setDiaList(diaList);
        photo.setPhotoId(photoId);
        photo.setBoneName(boneName);
        photo.setDescription(description);
        photo.setDirection(direction);
        photo.setType(type);
        photo.setPosition(position);
        photo.setDataset(dataset);
        ctValPo ctValPo=photo.createCtValPo();
        int ret1=ctValPoMapper.insertSelective(ctValPo);
//        if(ret==0){
//            System.out.println("error! update ctVal by name in Controller.");
//        }
//        vqaMapper.updateVqa(photo);
        photo.setFlag("1");
        ctInfoPoExample example = new ctInfoPoExample();
        ctInfoPoExample.Criteria criteria = example.createCriteria();
        criteria.andPhotoIdEqualTo(photoId);
        criteria.andPatientIdEqualTo(patientId);
        criteria.andDatasetEqualTo(dataset);
        int ret2=ctInfoPoMapper.updateByExampleSelective(photo.createCtInfoPo(),example);
//        vqaMapper.updateUsr(photo);
//        if(photo.getId()>=0){
//            response.setStatus(200);
//        }
//        else {
//            response.setStatus(450);
//        }
    }

    @Value("${dataset.dest}")
    private String dest;

    @GetMapping("/doneLableing/{dataset}")
    public void doneLableing (@PathVariable("dataset") String dataset,HttpServletResponse response) {
        try {
            python(dataset);
            LOGGER.info(dataset);
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
        }

    }


    @GetMapping("/all/{dataset}")
    public ArrayList<Photo> getAllPatients(@PathVariable("dataset") String dataset,HttpServletResponse response){
        ArrayList<Photo> photos=new ArrayList<Photo>();
        List<ctInfoPo> ctInfoPos=new ArrayList<ctInfoPo>();
        ctInfoPoExample example = new ctInfoPoExample();
        ctInfoPoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("0");
        criteria.andDatasetEqualTo(dataset);
        ctInfoPos=ctInfoPoMapper.selectByExample(example);
        for(int i=0;i<ctInfoPos.size();i++){
            photos.add(new Photo(ctInfoPos.get(i)));
        }
        if(photos.size()!=0){
            response.setStatus(200);
        }
        else{
            response.setStatus(460);
        }
        return photos;
    }

    @Value("${dataset.pythonenv}")
    private String pythonEnv;

    @Value("${fronted.VQApythonscript}")
    private String pythonScript;

    public void python(String fileSrc) throws IOException {

        String datasetDest=dest;

        String env= pythonEnv;
        File dic=new File(".");
        System.out.println(dic.getCanonicalFile());
        String model=pythonScript;
        String cmd=env+" "+model+" --name "+fileSrc+" --dest "+datasetDest;
        System.out.println(cmd);
        Runtime run=Runtime.getRuntime();
        try{
            Process process=run.exec(cmd);
            InputStream in=process.getInputStream();
            InputStreamReader reader=new InputStreamReader(in);
            BufferedReader br =new BufferedReader(reader);
            StringBuffer sb=new StringBuffer();
            String message;
            while((message=br.readLine())!=null){
                sb.append(message);
            }
            System.out.println(sb);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}