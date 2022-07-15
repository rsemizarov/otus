package homework;


import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Objects.isNull;

public class CustomerService {

    TreeMap<Customer, String> customers = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        return getEntryCopy(customers.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return getEntryCopy(customers.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        customers.put(new Customer(customer.getId(), customer.getName(), customer.getScores()), data);
    }


    private Map.Entry<Customer, String> getEntryCopy(Map.Entry<Customer, String> entry) {
        if (isNull(entry)) {
            return null;
        }
        Customer customer = entry.getKey();
        return Map.entry(
                new Customer(customer.getId(), customer.getName(), customer.getScores()),
                entry.getValue()
        );
    }
}