package app.fiveminchange.model;

import java.util.List;

public class RootObject {
    private int count;
    private List<RequestDetails> requests;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<RequestDetails> getRequests() {
        return requests;
    }

    public void setRequests(List<RequestDetails> requests) {
        this.requests = requests;
    }
}
