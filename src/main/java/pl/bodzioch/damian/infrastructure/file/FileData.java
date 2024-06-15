package pl.bodzioch.damian.infrastructure.file;

public record FileData(
        String path,
        String name,
        String nameInZip
) {
}
