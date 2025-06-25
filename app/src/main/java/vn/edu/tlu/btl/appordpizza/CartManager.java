package vn.edu.tlu.btl.appordpizza;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static List<CartItemModel> cart = new ArrayList<>();

    public static void addToCart(CartItemModel cartItem, int quantityToAdd) {
        for (CartItemModel item : cart) {
            if (item.getName().equals(cartItem.getName())) {
                item.setQuantity(item.getQuantity() + quantityToAdd);
                return;
            }
        }
        cart.add(cartItem);
    }

    public static List<CartItemModel> getCart() {
        return cart;
    }

    public static void clearCart() {
        cart.clear();
    }
}
