package worker;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Ini adalah template kurir (Worker) untuk mengambil data apapun secara background.
 * T adalah tipe data yang mau diambil (misal Pasien atau Dokter).
 */
public class BaseWorker<T> extends SwingWorker<List<T>, Void> {
    
    private final Callable<List<T>> fetchTask;   // Tugas yang akan dijalankan
    private final Consumer<List<T>> onSuccess;    // Apa yang dilakukan jika berhasil
    private final Consumer<Exception> onFailure;  // Apa yang dilakukan jika gagal

    public BaseWorker(Callable<List<T>> fetchTask, 
                      Consumer<List<T>> onSuccess, 
                      Consumer<Exception> onFailure) {
        this.fetchTask = fetchTask;
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
    }

    @Override
    protected List<T> doInBackground() throws Exception {
        // Bagian ini berjalan di "belakang layar" (tidak membuat aplikasi macet)
        return fetchTask.call();
    }

    @Override
    protected void done() {
        try {
            // Bagian ini berjalan kembali di "tampilan depan" (UI) setelah data didapat
            List<T> result = get();
            onSuccess.accept(result);
        } catch (Exception e) {
            onFailure.accept(e);
        }
    }
}