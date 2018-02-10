/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author nguyenminh
 */
public class Frequentpattern {
    private String sequence;
    private int support;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public Frequentpattern(String sequence, int support) {
        this.sequence = sequence;
        this.support = support;
    }

    @Override
    public String toString() {
        return "Frequentpattern{" + "sequence=" + sequence + ", support=" + support + '}';
    }
    
    
}
