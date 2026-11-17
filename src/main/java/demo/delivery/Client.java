package demo.delivery;

public final class Client {
    public void dispatch(Shipment shipment) {
        new DispatchOperation().dispatch(shipment);
    }

    public Runnable deferred(Shipment shipment) {
        return new DispatchOperation().deferred(shipment);
    }
}
