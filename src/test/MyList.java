/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;

/**
 *
 * @author nguyenminh
 */
public class MyList extends ArrayList<Integer> {
    public String toString() {
        int size = this.size();
        String tmp = "(";
        for(int i = 0; i < size; i++) {
            int data = this.get(i);
            if(i == size - 1)
                tmp += data + ") ";
            else 
                tmp += data + ",";
        }
        return tmp;
    }
}
