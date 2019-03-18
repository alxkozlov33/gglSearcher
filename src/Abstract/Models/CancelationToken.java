package Abstract.Models;

public class CancelationToken {
    public boolean isCanceled;

    public CancelationToken(boolean isCanceled) {
        this.isCanceled = isCanceled;
    }

    public void cancel() {
        isCanceled = true;
    }
}
