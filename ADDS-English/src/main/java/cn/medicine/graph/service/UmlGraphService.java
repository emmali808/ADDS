package cn.medicine.graph.service;


import cn.medicine.dao.UmlMapper;
import cn.medicine.graph.entity.GraphEntity;
import cn.medicine.graph.entity.Line;
import cn.medicine.graph.entity.Point;
import cn.medicine.graph.entity.UmlEntity;
import cn.medicine.pojo.Graph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class UmlGraphService {

    @Autowired
    UmlMapper umlMapper;

    public ArrayList<UmlEntity> getAllUmlEntity(){
        return umlMapper.getAllUmlEntity();
    }

    public UmlEntity searchUmlEntity(Long id){
        UmlEntity umlEntity = umlMapper.searchUmlEntity(id);

        ArrayList<Long> equality = umlMapper.searchEquality(umlEntity.getId());
        ArrayList<UmlEntity> equalityEntites = new ArrayList<>();
        for(Long e : equality){
            UmlEntity u = umlMapper.searchUmlEntity(e);
            if(u!=null){
                equalityEntites.add(u);
            }
        }
        umlEntity.setEquality(equalityEntites);

        ArrayList<Long> inverse_is_a = umlMapper.searchInverse_is_a(umlEntity.getId());
        ArrayList<UmlEntity> inverse_is_aEntites = new ArrayList<>();
        for(Long e : inverse_is_a){
            UmlEntity u = umlMapper.searchUmlEntity(e);
            if(u!=null){
                inverse_is_aEntites.add(u);
            }
        }
        umlEntity.setInverse_is_a(inverse_is_aEntites);

        ArrayList<Long> is_a = umlMapper.searchIs_a(umlEntity.getId());
        ArrayList<UmlEntity> is_aEntites = new ArrayList<>();
        for(Long e : is_a){
            UmlEntity u = umlMapper.searchUmlEntity(e);
            if(u!=null){
                is_aEntites.add(u);
            }
        }
        umlEntity.setIs_a(is_aEntites);

        ArrayList<Long> related = umlMapper.searchRelated(umlEntity.getId());
        ArrayList<UmlEntity> relatedEntites = new ArrayList<>();
        for(Long e : related){
            UmlEntity u = umlMapper.searchUmlEntity(e);
            if(u!=null){
                relatedEntites.add(u);
            }
        }
        umlEntity.setIs_a(relatedEntites);

        return umlEntity;
    }

    public GraphEntity searchUmlEntityDeeply(Long id, int deep){
        UmlEntity umlEntity = umlMapper.searchUmlEntity(id);
        if(umlEntity==null||deep<1){
            return null;
        }
        GraphEntity graphEntity = new GraphEntity();
        Point initPoint = new Point(umlEntity.getId(),umlEntity.getName());
        graphEntity.addPoint(initPoint);
        ArrayList<Point> buffer = new ArrayList<>();
        int i = 0;
        while(i<deep){
            if(i==0){
                GraphEntity result = this.getGraph(initPoint.getId());
                for(Point p :result.getPoints()){
                    graphEntity.addDirtyPoint(p);
                }
                for(Line l : result.getLines()){
                    graphEntity.addLine(l);
                }
            }else{
                for(int j=0; j<graphEntity.getDirtyPoints().size(); j++){
                    Point dirtyPoint = graphEntity.getDirtyPoints().get(j);
                    GraphEntity result = this.getGraph(dirtyPoint.getId());
                    for(Point p : result.getPoints()){
                        buffer.add(p); //先加入脏点缓冲区
                    }
                    for(Line l : result.getLines()){
                        graphEntity.addLine(l);
                    }
                    //把脏点去掉，加入到正常点中
                    //去重
                    int flag = 0;
                    for(Point cp : graphEntity.getPoints()){
                        if(dirtyPoint.getId()==cp.getId()){
                            flag = 1;
                            break;
                        }
                    }
                    if(flag == 0){
                        graphEntity.addPoint(dirtyPoint);
                    }
                    graphEntity.getDirtyPoints().remove(j--);
                }
                graphEntity.getDirtyPoints().addAll(buffer);
                buffer.clear();
            }
            i++;
        }
        for(int j=0; j<graphEntity.getDirtyPoints().size(); j++){
            Point dirtyPoint = graphEntity.getDirtyPoints().get(j);
            //把脏点去掉，加入到正常点中
            //去重
            int flag = 0;
            for(Point cp : graphEntity.getPoints()){
                if(dirtyPoint.getId()==cp.getId()){
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                graphEntity.addPoint(dirtyPoint);
            }
            graphEntity.getDirtyPoints().remove(j--);
        }
        return graphEntity;
    }

    //获取和该点相关的所有边
    public ArrayList<Line> getLines(Long id){
        ArrayList<Line> lines = new ArrayList<>();
        ArrayList<Long> equality = umlMapper.searchEquality(id);
        for(Long e : equality){
            UmlEntity u = umlMapper.searchUmlEntity(e);
            if(u!=null){
                lines.add(new Line(id,u.getId(),"equality"));
            }
        }

        ArrayList<Long> inverse_is_a = umlMapper.searchInverse_is_a(id);
        for(Long e : inverse_is_a){
            UmlEntity u = umlMapper.searchUmlEntity(e);
            if(u!=null){
                lines.add(new Line(id,u.getId(),"inverse_is_a"));
            }
        }

        ArrayList<Long> is_a = umlMapper.searchIs_a(id);
        for(Long e : is_a){
            UmlEntity u = umlMapper.searchUmlEntity(e);
            if(u!=null){
                lines.add(new Line(id,u.getId(),"is_a"));
            }
        }

        ArrayList<Long> related = umlMapper.searchRelated(id);
        for(Long e : related){
            UmlEntity u = umlMapper.searchUmlEntity(e);
            if(u!=null){
                lines.add(new Line(id,u.getId(),"related"));
            }
        }
        return lines;
    }

    //获取和该点相关的所有点
    public ArrayList<Point> getPoints(Long id){
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<Long> equality = umlMapper.searchEquality(id);
        for(Long e : equality){
            UmlEntity u = umlMapper.searchUmlEntity(e);
            if(u!=null){
                points.add(new Point(u.getId(),u.getName()));
            }
        }

        ArrayList<Long> inverse_is_a = umlMapper.searchInverse_is_a(id);
        for(Long e : inverse_is_a){
            UmlEntity u = umlMapper.searchUmlEntity(e);
            if(u!=null){
                points.add(new Point(u.getId(),u.getName()));
            }
        }

        ArrayList<Long> is_a = umlMapper.searchIs_a(id);
        for(Long e : is_a){
            UmlEntity u = umlMapper.searchUmlEntity(e);
            if(u!=null){
                points.add(new Point(u.getId(),u.getName()));
            }
        }

        ArrayList<Long> related = umlMapper.searchRelated(id);
        for(Long e : related){
            UmlEntity u = umlMapper.searchUmlEntity(e);
            if(u!=null){
                points.add(new Point(u.getId(),u.getName()));
            }
        }
        return points;
    }

    //获取和该点相关的所有点和边
    public GraphEntity getGraph(Long id){
        GraphEntity graphEntity = new GraphEntity();
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<Line> lines = new ArrayList<>();
        ArrayList<Long> equality = umlMapper.searchEquality(id);
        for(Long e : equality){
            UmlEntity u = umlMapper.searchUmlEntity(e);
            if(u!=null){
                lines.add(new Line(id,u.getId(),"equality"));
                points.add(new Point(u.getId(),u.getName()));
            }
        }
        ArrayList<Long> inverse_is_a = umlMapper.searchInverse_is_a(id);
        for(Long e : inverse_is_a){
            UmlEntity u = umlMapper.searchUmlEntity(e);
            if(u!=null){
                lines.add(new Line(id,u.getId(),"inverse_is_a"));
                points.add(new Point(u.getId(),u.getName()));
            }
        }
        ArrayList<Long> is_a = umlMapper.searchIs_a(id);
        for(Long e : is_a){
            UmlEntity u = umlMapper.searchUmlEntity(e);
            if(u!=null){
                lines.add(new Line(id,u.getId(),"is_a"));
                points.add(new Point(u.getId(),u.getName()));
            }
        }
        ArrayList<Long> related = umlMapper.searchRelated(id);
        for(Long e : related){
            UmlEntity u = umlMapper.searchUmlEntity(e);
            if(u!=null){
                lines.add(new Line(id,u.getId(),"related"));
                points.add(new Point(u.getId(),u.getName()));
            }
        }
        graphEntity.setLines(lines);
        graphEntity.setPoints(points);
        return graphEntity;
    }
}
