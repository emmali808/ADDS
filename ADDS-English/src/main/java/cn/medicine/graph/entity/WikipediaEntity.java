package cn.medicine.graph.entity;

import java.math.BigInteger;
import java.util.ArrayList;

public class WikipediaEntity {
    private Long id;
    private String name;
    private ArrayList<WikipediaEntity> equality;
    private ArrayList<WikipediaEntity> has_a;
    private ArrayList<WikipediaEntity> is_a;
    private ArrayList<WikipediaEntity> part_of;

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

    public ArrayList<WikipediaEntity> getEquality() {
        return equality;
    }

    public void setEquality(ArrayList<WikipediaEntity> equality) {
        this.equality = equality;
    }

    public ArrayList<WikipediaEntity> getHas_a() {
        return has_a;
    }

    public void setHas_a(ArrayList<WikipediaEntity> has_a) {
        this.has_a = has_a;
    }

    public ArrayList<WikipediaEntity> getIs_a() {
        return is_a;
    }

    public void setIs_a(ArrayList<WikipediaEntity> is_a) {
        this.is_a = is_a;
    }

    public ArrayList<WikipediaEntity> getPart_of() {
        return part_of;
    }

    public void setPart_of(ArrayList<WikipediaEntity> part_of) {
        this.part_of = part_of;
    }
}
