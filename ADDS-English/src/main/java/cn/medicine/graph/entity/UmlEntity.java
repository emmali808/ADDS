package cn.medicine.graph.entity;

import java.math.BigInteger;
import java.util.ArrayList;

public class UmlEntity {
    private Long id;
    private String name;
    private ArrayList<UmlEntity> equality;
    private ArrayList<UmlEntity> inverse_is_a;
    private ArrayList<UmlEntity> is_a;
    private ArrayList<UmlEntity> related;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<UmlEntity> getEquality() {
        return equality;
    }

    public void setEquality(ArrayList<UmlEntity> equality) {
        this.equality = equality;
    }

    public ArrayList<UmlEntity> getInverse_is_a() {
        return inverse_is_a;
    }

    public void setInverse_is_a(ArrayList<UmlEntity> inverse_is_a) {
        this.inverse_is_a = inverse_is_a;
    }

    public ArrayList<UmlEntity> getIs_a() {
        return is_a;
    }

    public void setIs_a(ArrayList<UmlEntity> is_a) {
        this.is_a = is_a;
    }

    public ArrayList<UmlEntity> getRelated() {
        return related;
    }

    public void setRelated(ArrayList<UmlEntity> related) {
        this.related = related;
    }
}
