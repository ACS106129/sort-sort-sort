package utility;

import javax.swing.SwingWorker;

/**
 * Worker extends from {@code SwingWorker} with addition pausable and skippable
 * 
 * @param <T> the result type returned by this {@code SwingWorker's}
 *            {@code doInBackground} and {@code get} methods
 * @param <V> the type used for carrying out intermediate results by this
 *            {@code SwingWorker's} {@code publish} and {@code process} methods
 */
public abstract class ExecuteWorker<T, V> extends SwingWorker<T, V> {

    private volatile boolean isPaused = false;

    private volatile boolean isSkipped = false;

    public final void skip() {
        if (!isDone()) {
            isSkipped = true;
            firePropertyChange("skipped", false, true);
        }
    }

    public final void pause() {
        if (!isPaused() && !isDone()) {
            isPaused = true;
            firePropertyChange("paused", false, true);
        }
    }

    public final void resume() {
        if (isPaused() && !isDone()) {
            isPaused = false;
            firePropertyChange("paused", true, false);
        }
    }

    public final boolean isPaused() {
        return isPaused;
    }

    public final boolean isSkipped() {
        return isSkipped;
    }
}