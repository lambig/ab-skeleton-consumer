package demo.delivery;

public final class Client {
    public void dispatch(Shipment shipment) {
        shipment.dispatch();
    }

    public Runnable deferred(Shipment shipment) {
        return shipment::dispatch;
    }
}
