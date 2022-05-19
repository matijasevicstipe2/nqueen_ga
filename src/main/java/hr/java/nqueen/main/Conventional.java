package hr.java.nqueen.main;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Conventional {
    private int MAX_LENGTH = 4;


    public  boolean convent(){
        int n = MAX_LENGTH;
        Chromosome c = new Chromosome(n);

        boolean check = false;

        while (!check){
            c.computeConflicts();
            int x = c.getConflicts();
            List<Integer> list;
            list =  Arrays.asList(c.getGenes());
            Collections.shuffle(list);
            if(x != 0){
                check = false;
            }else {
                check = true;
            }
        }
        return check;

    }




}


