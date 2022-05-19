package hr.java.nqueen.main;

import java.util.List;


public class Chromosome implements Comparable<Chromosome>{
    private int MAX_LENGTH; 					//n size
    private Integer[] gene; 						//contains the location of each queen
    private int fitness;						//the fitness of this chromosome towards the solution
    private int conflicts; 						//number of collisions
    private boolean selected; 					//if selected for mating
    private double selectionProbability; 		//probabiblity of beaing selected for mating in roulette

    /* Instantiate the chromosome.
     *
     * @param: size of n
     */
    public Chromosome(int n) {
        MAX_LENGTH = n;
        gene = new Integer[MAX_LENGTH];
        fitness = 0;
        conflicts = 0;
        selected = false;
        selectionProbability = 0.0;

        initChromosome();
    }

    /* Compares two chromosomes.
     *
     * @param: a chromosome to compare with
     */
    public int compareTo(Chromosome c) {
        return this.conflicts - c.getConflicts();
    }



    /* Computes the conflicts in the nxn board.
     *
     */
    public void computeConflicts() { //compute the number of conflicts to calculate fitness
        String board[][] = new String[MAX_LENGTH][MAX_LENGTH]; //initialize board
        int x = 0; //row
        int y = 0; //column
        int tempx = 0; //temprow
        int tempy = 0; //temcolumn

        int dx[] = new int[] {-1, 1, -1, 1}; //to check for diagonal
        int dy[] = new int[] {-1, 1, 1, -1}; //paired with dx to check for diagonal

        boolean done = false; //used to check is checking fo diagonal is out of bounds
        int conflicts = 0; //number of conflicts found

        clearBoard(board); //clears the board into empty strings
        plotQueens(board); // plots the Q in the board

        // Walk through each of the Queens and compute the number of conflicts.
        for(int i = 0; i < MAX_LENGTH; i++) {
            x = i;
            y = gene[i];

            // Check diagonals.
            for(int j = 0; j < 4; j++) { // because of dx and dy where there are 4 directions for diagonal searching for conflicts
                tempx = x;
                tempy = y; // store coordinate in temp
                done = false;

                while(!done) {//traverse the diagonals
                    tempx += dx[j];
                    tempy += dy[j];

                    if((tempx < 0 || tempx >= MAX_LENGTH) || (tempy < 0 || tempy >= MAX_LENGTH)) { //if exceeds board
                        done = true;
                    } else {
                        if(board[tempx][tempy].equals("Q")) {
                            conflicts++;
                        }
                    }
                }
            }
        }

        this.conflicts = conflicts; //set conflicts of this chromosome

    }

    /* Plots the queens in the board.
     *
     * @param: a nxn board
     */
    public void plotQueens(String[][] board) {
        for(int i = 0; i < MAX_LENGTH; i++) {
            board[i][gene[i]] = "Q";
        }
    }

    /* Clears the board.
     *
     * @param: a nxn board
     */
    public void clearBoard(String[][] board) {
        for (int i = 0; i < MAX_LENGTH; i++) {
            for (int j = 0; j < MAX_LENGTH; j++) {
                board[i][j] = "";
            }
        }
    }

    /* Initializes the chromosome into diagonal queens.
     *
     */
    public void initChromosome() {
        for(int i = 0; i < MAX_LENGTH; i++) {
            gene[i] = i;
        }
    }

    /* Gets the gene/data on a specified index.
     *
     * @param: index of data
     * @return: position of queen
     */
    public int getGene(int index) {
        return gene[index];
    }

    /* Sets the gene/data on a specified index.
     *
     * @param: index of data
     * @param: new position of queen
     */
    public void setGene(int index, int position) {
        this.gene[index] = position;
    }
    public Integer[] getGenes(){
        return this.gene;
    }

    public void setGenes(List<Integer> gene) {
        this.gene = gene.toArray(new Integer[0]);
    }

    /* Gets the fitness of a chromosome.
     *
     * @return: fitness of chromosome
     */
    public int getFitness() {
        return fitness;
    }

    /* Sets the fitness of the chromosome.
     *
     * @param: new fitness
     */
    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    /* Gets the conflicts of the chromosome.
     *
     * @return: number of conflicts of the chromosome
     */
    public int getConflicts() {
        return conflicts;
    }


    /* Gets whether the chromosome is selected.
     *
     * @return: boolean value if slected
     */
    public boolean isSelected() {
        return selected;
    }

    /* Gets whether the chromosome is selected.
     *
     * @param: boolean value if slected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}