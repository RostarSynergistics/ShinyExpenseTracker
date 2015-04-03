// https://github.com/rayzhangcl/ESDemo/blob/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei/Hits.java

package ca.ualberta.cs.shinyexpensetracker.es.data;

import java.util.Collection;


public class Hits<T> {
    int total;
    double max_score;
    Collection<ElasticSearchResponse<T>> hits;
    public Collection<ElasticSearchResponse<T>> getHits() {
        return hits;
    }
    public String toString() {
        return (super.toString()+","+total+","+max_score+","+hits);
    }
}