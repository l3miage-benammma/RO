package l3m.cyber.planner.utils;

import java.util.ArrayList;

/*
 we suppose that deliveries indexes start from 0 to deliveriesNumber - 1
 */
public class PartitionKCentre extends Partition {

    public PartitionKCentre(int deliveriesNumber, int toursNumber) {
        super(deliveriesNumber, toursNumber);
    }

    /*
     * we will need a list of centers
     * and a list of tuples (deliveryIndex, centerIndex)
     * intially we will assign all deliveries to the first center (wareHouse)
     * and we add the warehouse to the list of centers
     *
     * then while our centers list length is less than the number of tours
     * we will iterate over all deliveries and for each delivery we will calculate
     * the distance between it and his center
     * we will also find a new center by choosing the delivery with the maximum
     * distance from its center
     * we will add this delivery to the list of centers
     *
     * example :
     * number of tours = 2
     * centerList = [0]
     * deliveries = [1,2,3,4]
     * distances = [ [0, 1, 2, 3, 4],
     * [1, 0, 1, 2, 3],
     * [2, 1, 0, 1, 2],
     * [3, 2, 1, 0, 1],
     * [4, 3, 2, 1, 0] ]
     *
     * tupleList = [ (1, 0), (2, 0), (3, 0), (4, 0) ]
     *
     * after one iteration :
     *
     * centerList = [0, 4]
     * deliveries = [1,2,3]
     * distances = same
     * tupleList = [ (1, 0), (2, 0), (3, 0), (4, 4) ]
     * end of example
     */
    @Override
    public void partitionne(Double[][] distances) {

        ArrayList<Integer> centersList = new ArrayList<>();

        int[] centerAssociatedToDelivery = new int[nbElem];

        centersList.add(elemSpecial);

        // iterate on tupeList and assign all deliveries to the first center
        for (int i = 0; i < nbElem; i++) {
            centerAssociatedToDelivery[i] = elemSpecial;
        }

        // while we have less centers than tours
        for (int i = 0; i < k - 1; i++) {
            // iterate over all deliveries
            // find the delivery with the maximum distance from its center using distances
            // param

            double maxDistance = -1;
            int newCenter = -1;
            for (int delivery = 0; delivery < nbElem; delivery++) {

                double deliveryToCenterDistance = distances[delivery][centerAssociatedToDelivery[delivery]];
                if (deliveryToCenterDistance > maxDistance) {
                    maxDistance = deliveryToCenterDistance;
                    newCenter = delivery;
                }
            }
            if (newCenter != -1) {
                centersList.add(newCenter);
            }

            for (int delivery = 0; delivery < nbElem; delivery++) {
                if (distances[delivery][centerAssociatedToDelivery[delivery]] > distances[delivery][newCenter]) {
                    centerAssociatedToDelivery[delivery] = newCenter;
                }
            }

        }
        // index is shared by the centers and the tours because one tour is associated
        // to one center
        for (int index = 0; index < k; index++) {
            int actualCenter = centersList.get(index);
            for (int delivery = 0; delivery < nbElem; delivery++) {
                if (delivery != elemSpecial && centerAssociatedToDelivery[delivery] == actualCenter) {
                    parties.get(index).add(delivery);
                }
            }
        }

    }
}