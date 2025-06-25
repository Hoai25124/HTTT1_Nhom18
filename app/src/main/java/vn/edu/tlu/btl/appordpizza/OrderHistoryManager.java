package vn.edu.tlu.btl.appordpizza;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryManager {
    private static List<OrderHistoryModel> historyList = new ArrayList<>();

    public static void addOrder(OrderHistoryModel order) {
        historyList.add(order);
    }

    public static List<OrderHistoryModel> getHistory() {
        return historyList;
    }

    public static void clearHistory() {
        historyList.clear();
    }
}

