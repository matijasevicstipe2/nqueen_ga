package hr.java.nqueen.main;



public class Main {

    public static void main(String[] args) {

        GeneticAlgorithm g = new GeneticAlgorithm(10);
        long startTime,endTime,totalTime;

        startTime = System.nanoTime();
        if(g.algorithm()){
            endTime = System.nanoTime();
            totalTime = endTime - startTime;
            System.out.println("Runtime in nanoseconds: " + totalTime /1000000000 + "D" + totalTime +
                    "a"+ totalTime/1000000);
            System.out.println(("Found at epoch: "+g.getEpoch()));
            System.out.println(("Population size" + g.getPopSize()));


        }else{
            System.out.println("Fail");
        }
        /*Conventional c = new Conventional();
        startTime = System.nanoTime();
        boolean b = c.convent();
        endTime = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println(b+ "time:" + totalTime);*/


    }
}
