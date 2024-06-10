package pl.bodzioch.damian.document.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileListExtensionValidator implements ConstraintValidator<FileListExtensionV, List<MultipartFile>> {

    private List<String> extensions;

    @Override
    public void initialize(FileListExtensionV constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.extensions = Arrays.stream(constraintAnnotation.extensions()).toList();
    }

    @Override
    public boolean isValid(List<MultipartFile> value, ConstraintValidatorContext context) {
        return value.stream()
                .map(MultipartFile::getOriginalFilename)
                .map(item -> item != null ? item.substring(item.lastIndexOf(".") + 1) : null)
                .filter(StringUtils::isNotBlank)
                .allMatch(extensions::contains);
    }
}
