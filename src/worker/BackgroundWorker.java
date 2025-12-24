package worker;

import javax.swing.*;

public abstract class BackgroundWorker {
    private SwingWorker<Void, Void> worker;
    
    public BackgroundWorker() {
        worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                doInBackgroundWork();
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get(); // Check for exceptions
                    onDone();
                } catch (Exception e) {
                    onError(e);
                }
            }
        };
    }
    
    public void execute() {
        worker.execute();
    }
    
    public void cancel() {
        if (worker != null && !worker.isDone()) {
            worker.cancel(true);
        }
    }
    
    public boolean isDone() {
        return worker != null && worker.isDone();
    }
    
    // Abstract methods to be implemented
    protected abstract void doInBackgroundWork() throws Exception;
    protected abstract void onDone();
    protected abstract void onError(Exception e);
}