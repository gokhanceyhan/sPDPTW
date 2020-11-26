package output;

import java.io.FileNotFoundException;

public interface OutputDataProducer {

    void write (Solution solution, String destination) throws FileNotFoundException;
}
