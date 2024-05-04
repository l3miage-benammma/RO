package l3m.cyber.planner.utils;

import java.util.ArrayList;

/*
  La répartition des livraisons entre livreurs se fera grâce à une Partition
*/
public abstract class Partition {
    protected int nbElem; // nb sommets du graphes ( éléments à partitionner )
    protected int k; // nombre de tournées
    protected int elemSpecial; // indice du dépôt
    protected ArrayList<Integer> elems; // liste de sommets ( leur indice dans matrice de distance ) ?
    protected ArrayList<ArrayList<Integer>> parties; // liste des tournées

    public Partition(ArrayList<Integer> elems, int k, int elemSpecial) {
        this.elems = elems;
        this.k = k;
        this.elemSpecial = elemSpecial;
        this.nbElem = elems.size();
        this.parties = new ArrayList<ArrayList<Integer>>();
        this.partitionVide();
    }

    public Partition(int nbElem, int k, int elemSpecial) {
        this.nbElem = nbElem;
        this.k = k;
        this.elemSpecial = elemSpecial;
        this.elems = new ArrayList<Integer>();
        for (int i = 0; i < nbElem; i++) {
            this.elems.add(i);
        }
        this.parties = new ArrayList<ArrayList<Integer>>();
        this.partitionVide();
    }

    public Partition(int nbElem, int k) {
        this(nbElem, k, 0);
    }

    /*
     * méthode partitionVide() :
     * alloue la mémoire nécessaire pour parties (liste de k listes), et met
     * l'élément spécial dans chacune des parties.
     * Autrement dit, à la fin, chaque partie est un singleton contenant l'élément
     * spécial. Sera appelé dans le constructeur.
     */
    public void partitionVide() {
        // create this.k parties
        for (int i = 0; i < this.k; i++) {
            ArrayList<Integer> tour = new ArrayList<Integer>();
            tour.add(this.elemSpecial);
            this.parties.add(tour);
        }
        // put the special element inside each partie
    }

    /*
     * méthode toString() : crée une description textuelle de la partition
     * (présentation textuelle laissée à votre appréciation).
     */
    @Override
    public String toString() {
        return "{ deliveriesNumber = " + nbElem + ", toursNumber = " + k + ", deliveriesIndexes = "
                + elems + ", tours = " + parties + " }";
    }

    public ArrayList<Integer> getPartie(int i) {
        return this.parties.get(i);
    }

    /*
     * méthode abstraite void partitionne(Double[][] distance) :
     * sera définie dans les sous-classes non-abstraites, en fonction de la
     * stratégie de partition choisie (utilisant potentiellement distance).
     * Le but de cette méthode sera de remplir correctement l'attribut parties pour
     * obtenir la partition souhaitée.
     */
    public abstract void partitionne(Double[][] distances);

    public ArrayList<ArrayList<Integer>> getParties() {
        return this.parties;
    }

}