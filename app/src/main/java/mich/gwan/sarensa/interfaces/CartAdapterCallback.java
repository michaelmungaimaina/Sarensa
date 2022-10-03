package mich.gwan.sarensa.interfaces;

import java.util.List;

import mich.gwan.sarensa.model.Cart;

public interface CartAdapterCallback {
    void onMethodCall(List<Cart> list);
}
