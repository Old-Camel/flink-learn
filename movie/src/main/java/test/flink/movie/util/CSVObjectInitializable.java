package test.flink.movie.util;

public interface CSVObjectInitializable {
    CSVObjectInitializable initFromCSV(String csv, String sep);
}
