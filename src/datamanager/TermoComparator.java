/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;

/**
 *
 * @author charleshenriqueportoferreira
 */
public class TermoComparator implements java.util.Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Termo t1 = (Termo) o1;
        Termo t2 = (Termo) o2;
        if (t1.rank > t2.rank) {
            return -1;
        }
        if (t1.rank < t2.rank) {
            return 1;
        }
        return 0;
    }
}
