// Code take from: https://github.com/ramish94/AndroidElasticSearch, March 29, 2015

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
