package fr.thejordan.historyland.object.common;

public record ValueWError(Object value, String msg) {

    public ValueWError(Object value, String msg) {
        this.value = value;
        this.msg = (msg == null) ? "" : msg;
    }

    public boolean isNullWithError() {
        return (value == null && !msg.equals(""));
    }


}
