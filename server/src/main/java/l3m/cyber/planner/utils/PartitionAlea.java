package l3m.cyber.planner.utils;

import java.util.Random;
import java.util.ArrayList;

public class PartitionAlea extends Partition {

    public PartitionAlea(ArrayList<Integer> elems, int k, int elemSpecial) {
        super(elems, k, elemSpecial);
    }

    public PartitionAlea(int n, int k, int elemSpecial) {
        super(n, k, elemSpecial);
    }

    public PartitionAlea(int n, int k) {
        super(n, k);
    }

    @Override
    public void partitionne(Double[][] distances) {
        Random rand = new Random();
        for (Integer elem : elems) {
            if (elem != elemSpecial) {
                int randomPartie = rand.nextInt(k);
                parties.get(randomPartie).add(elem);
            }
        }
    }
}
