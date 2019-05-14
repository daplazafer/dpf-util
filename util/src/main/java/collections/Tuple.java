/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collections;

/**
 *
 * @author dpf
 * @param <X>
 * @param <Y>
 */
public class Tuple< X, Y> {

    public final X x;
    public final Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        Tuple t = (Tuple) obj;
        return this.x.equals(t.x) && this.y.equals(t.y);
    }

    


}
