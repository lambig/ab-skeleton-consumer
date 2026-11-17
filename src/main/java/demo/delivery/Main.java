package demo.delivery;

import io.github.lambig.textescape.TextEscape;

public final class Main {
    public static void main(String[] args) {
        Client client = new Client();
        client.dispatch(new Shipment());
        client.deferred(new Shipment()).run();
        String message = TextEscape.escape("${operation}: ${status}")
                .where("operation", "dispatch").where("status", "ready").compile();
        if (!"dispatch: ready".equals(message)) throw new AssertionError(message);
        System.out.println(message);
    }
}
