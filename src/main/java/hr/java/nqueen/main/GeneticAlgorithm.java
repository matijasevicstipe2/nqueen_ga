package hr.java.nqueen.main;
import java.util.*;

public class GeneticAlgorithm {

    private int MAX_LENGTH;
    private int START_SIZE;
    private int MAX_EPOCHS;
    private double MATING_PROBABILITY;
    private double MUTATION_RATE;
    private int MIN_SELECT;
    private int MAX_SELECT;
    private int OFFSPRING_PER_GENERATION;
    private int MINIMUM_SHUFFLES;
    private int MAXIMUM_SHUFFLES;
    private int nextMutation;
    private ArrayList<Chromosome> population;
    private ArrayList<Chromosome> solutions;
    private Random rand;
    private int childCount;
    private int mutations;
    private int epoch;
    private int populationSize;

    public GeneticAlgorithm(int n) {
        MAX_LENGTH = n;
        START_SIZE = 40;
        MAX_EPOCHS = 1000;
        MATING_PROBABILITY = 0.9;
        MUTATION_RATE = 0.01;
        MIN_SELECT = 2;
        MAX_SELECT = 20;
        OFFSPRING_PER_GENERATION = 20;
        MINIMUM_SHUFFLES = 7;
        MAXIMUM_SHUFFLES = 20;
        epoch = 0;
        populationSize = 0;
        rand = new Random();
    }

    public void pocetnaGeneracija(){
        population = new ArrayList<Chromosome>();
        for(int i = 0; i < START_SIZE; i++){
            Chromosome c = new Chromosome(MAX_LENGTH);
            population.add(c);
            //pocetni shuffle(mutacija) za svaku jedinku
            int shuffle = rand.nextInt(MAXIMUM_SHUFFLES - MINIMUM_SHUFFLES + 1);
            List<Integer> list;
            list =  Arrays.asList(c.getGenes());


            for(int j = 0; j < shuffle; j++){
                Collections.shuffle(list);

            }
            //izracunaj broj konflikta za svaki kromosom
            population.get(i).computeConflicts();
        }


    }
    public boolean algorithm() {
        population = new ArrayList<Chromosome>();
        solutions = new ArrayList<Chromosome>();
        rand = new Random();
        nextMutation = 0;
        childCount = 0;
        mutations = 0;
        epoch = 0;
        populationSize = 0;

        boolean done = false;
        Chromosome thisChromo = null;

        pocetnaGeneracija();


        while(!done) {
            populationSize = population.size();

            for(int i = 0; i < population.size(); i++) {
                thisChromo = population.get(i);
                if((thisChromo.getConflicts() == 0)) {
                    System.out.println(i);

                    done = true;

                }
            }
            if(done){
                break;
            }

            if(epoch == MAX_EPOCHS) {

                done = true;
            }
            fitness();

            selection();
            populationSize = population.size();

            prepNextEpoch();

            epoch++;
            System.out.println("Epoch: " + epoch);

        }

        if(epoch >= MAX_EPOCHS) {
            System.out.println("No solution found");
            done = false;
        } else {
            populationSize = population.size();
            for(int i = 0; i < populationSize; i++) {
                thisChromo = population.get(i);
                if(thisChromo.getConflicts() == 0) {
                    System.out.println(i);
                    solutions.add(thisChromo);
                    printSolution(thisChromo);
                }
            }
        }
        System.out.println("done.");

        System.out.println("Completed " + epoch + " epochs.");


        return done;
    }

    /* Starts the mating process with the selected chromosomes.
     *
     */
    public void selection() {
        int getRand = 0;
        int parentA = 0;
        int parentB = 0;
        int newIndex1 = 0;
        int newIndex2 = 0;
        Chromosome newChromo1 = null;
        Chromosome newChromo2 = null;
        List<Chromosome> help = new ArrayList<>();

        for(int i = 0; i < START_SIZE/4; i++) {
            parentA = chooseParent();

            // Test probability of mating.
            getRand = rand.nextInt(101);
            if(getRand <= MATING_PROBABILITY * 100) {
                parentB = chooseParent(parentA);
                newChromo1 = new Chromosome(MAX_LENGTH);
                newChromo2 = new Chromosome(MAX_LENGTH);
                population.add(newChromo1);
                newIndex1 = population.indexOf(newChromo1);
                population.add(newChromo2);
                newIndex2 = population.indexOf(newChromo2);

                // partiallyMappedCrossover
                partiallyMappedCrossover(parentA, parentB, newIndex1, newIndex2);

                population.get(newIndex1).computeConflicts();
                population.get(newIndex2).computeConflicts();
                help.add(population.get(parentA));
                help.add(population.get(parentB));
                help.add(population.get(newIndex1));
                help.add(population.get(newIndex2));
            }
        }
        population = (ArrayList<Chromosome>) help;
        //mutation
        Chromosome c;
        for(int i = 0; i < population.size(); i++){
            if(rand.nextInt(101) <= MUTATION_RATE * 100){
                c = population.get(i);
                List<Integer> list;
                list =  Arrays.asList(c.getGenes());
                Collections.shuffle(list);
            }
        }
    }

    /* Crossovers two chromosome parents. Uses partiallyMappedCrossover technique.
     *
     * @param: parent A
     * @param: parent B
     * @param: child A
     * @param: child B
     */
    public void partiallyMappedCrossover(int chromA, int chromB, int child1, int child2) {
        int j = 0;
        int item1 = 0;
        int item2 = 0;
        int pos1 = 0;
        int pos2 = 0;
        Chromosome thisChromo = population.get(chromA);
        Chromosome thatChromo = population.get(chromB);
        Chromosome newChromo1 = population.get(child1);
        Chromosome newChromo2 = population.get(child2);
        int crossPoint1 = rand.nextInt(MAX_LENGTH);
        int crossPoint2 = rand.nextInt(MAX_LENGTH);
        while(crossPoint2 == crossPoint1){
            crossPoint2 = rand.nextInt(MAX_LENGTH);
        }

        if(crossPoint2 < crossPoint1) {
            j = crossPoint1;
            crossPoint1 = crossPoint2;
            crossPoint2 = j;
        }

        // Copy Parent genes to offspring.
        for(int i = 0; i < MAX_LENGTH; i++) {
            newChromo1.setGene(i, thisChromo.getGene(i));
            newChromo2.setGene(i, thatChromo.getGene(i));
        }

        for(int i = crossPoint1; i <= crossPoint2; i++) {
            // Get the two items to swap.
            item1 = thisChromo.getGene(i);
            item2 = thatChromo.getGene(i);

            // Get the items//  positions in the offspring.
            for(j = 0; j < MAX_LENGTH; j++) {
                if(newChromo1.getGene(j) == item1) {
                    pos1 = j;
                } else if (newChromo1.getGene(j) == item2) {
                    pos2 = j;
                }
            } // j

            // Swap them.
            if(item1 != item2) {
                newChromo1.setGene(pos1, item2);
                newChromo1.setGene(pos2, item1);
            }

            // Get the items//  positions in the offspring.
            for(j = 0; j < MAX_LENGTH; j++) {
                if(newChromo2.getGene(j) == item2) {
                    pos1 = j;
                } else if(newChromo2.getGene(j) == item1) {
                    pos2 = j;
                }
            } // j

            // Swap them.
            if(item1 != item2) {
                newChromo2.setGene(pos1, item1);
                newChromo2.setGene(pos2, item2);
            }

        } // i
    }

    public int chooseParent() {
        // Overloaded function, see also "chooseparent(ByVal parentA As Integer)".
        int parent = 0;
        Chromosome thisChromo = null;
        boolean done = false;

        while(!done) {
            // Randomly choose an eligible parent.
            parent = rand.nextInt(population.size());
            thisChromo = population.get(parent);
            if(thisChromo.isSelected() == true) {

                done = true;
            }
        }
        return parent;
    }


    public int chooseParent(int parentA) {
        // Overloaded function, see also "chooseparent()".
        int parent = 0;
        Chromosome thisChromo = null;
        boolean done = false;

        while(!done) {
            // Randomly choose an eligible parent.
            parent = rand.nextInt(population.size());
            if(parent != parentA){
                thisChromo = population.get(parent);
                if(thisChromo.isSelected() == true){
                    done = true;
                }
            }
        }

        return parent;
    }

    /* Sets the fitness of each chromosome based on its conflicts
     *
     */
    public void fitness(){
        int bestScore = 0;
        int worstScore = 0;
        Chromosome thisChromo = null;
        for(int i = 0; i < population.size(); i++) {
            thisChromo = population.get(i);
            thisChromo.setFitness(thisChromo.getConflicts());
        }
        worstScore = Collections.max(population).getFitness();
        bestScore = Collections.min(population).getFitness();



        int sum = 0;
        while(sum < MIN_SELECT){
            double randomValue = rand.nextInt(worstScore - bestScore + 1) + bestScore;
            sum = 0;
            for(int i = 0; i < population.size(); i++){
                thisChromo = population.get(i);
                if(thisChromo.getFitness() <= randomValue){
                    thisChromo.setSelected(true);
                    sum = sum + 1;


                }
            }
            System.out.println(sum + "a" + population.size());
        }
    }


    /* Resets all flags in the selection
     *
     */
    public void prepNextEpoch() {
        int populationSize = 0;
        Chromosome thisChromo = null;

        // Reset flags for selected individuals.
        populationSize = population.size();
        for(int i = 0; i < populationSize; i++) {
            thisChromo = population.get(i);
            thisChromo.setSelected(false);
        }
    }

    /* Prints the nxn board with the queens
     *
     * @param: a chromosome
     */
    public void printSolution(Chromosome solution) {
        String board[][] = new String[MAX_LENGTH][MAX_LENGTH];

        // Clear the board.
        for(int x = 0; x < MAX_LENGTH; x++) {
            for(int y = 0; y < MAX_LENGTH; y++) {
                board[x][y] = "";
            }
        }

        for(int x = 0; x < MAX_LENGTH; x++) {
            board[x][solution.getGene(x)] = "Q";
        }

        // Display the board.
        System.out.println("Board:");
        for(int y = 0; y < MAX_LENGTH; y++) {
            for(int x = 0; x < MAX_LENGTH; x++) {
                if(board[x][y] == "Q") {
                    System.out.print("Q ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.print("\n");
        }
    }


    public int getEpoch() {
        return epoch;
    }

    public int getPopSize() {
        return population.size();
    }
}
