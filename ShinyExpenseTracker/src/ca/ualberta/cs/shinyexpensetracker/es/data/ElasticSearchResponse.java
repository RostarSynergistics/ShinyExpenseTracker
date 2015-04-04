// Code taken from: https://github.com/rayzhangcl/ESDemo/blob/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei/ElasticSearchResponse.java
// April 3rd, 2015

package ca.ualberta.cs.shinyexpensetracker.es.data;

public class ElasticSearchResponse<T> {
    String _index;
    String _type;
    String _id;
    int _version;
    boolean exists;
    T _source;
    double max_score;
    public T getSource() {
        return _source;
    }
}
