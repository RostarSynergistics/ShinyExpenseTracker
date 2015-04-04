// Code taken from: https://github.com/rayzhangcl/ESDemo/blob/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei/ElasticSearchSearchResponse.java
// April 3rd, 2015

package ca.ualberta.cs.shinyexpensetracker.es.data;

import java.util.ArrayList;
import java.util.Collection;

public class ElasticSearchSearchResponse<T> {
    int took;
    boolean timed_out;
    transient Object _shards;
    Hits<T> hits;
    boolean exists;    
    public Collection<ElasticSearchResponse<T>> getHits() {
        return hits.getHits();        
    }
    public Collection<T> getSources() {
        Collection<T> out = new ArrayList<T>();
        for (ElasticSearchResponse<T> essrt : getHits()) {
            out.add( essrt.getSource() );
        }
        return out;
    }
    public String toString() {
        return (super.toString() + ":" + took + "," + _shards + "," + exists + ","  + hits);     
    }
}
