package com.rsupport.notice.domain.file.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.rsupport.notice.common.error.CoreException;
import com.rsupport.notice.domain.file.error.FileErrorCode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class FileValidatorTest {
    private final Long maxFileSize = 10485760L; // 10MB
    private final Set<String> allowedMimeTypes = new HashSet<>(
        Arrays.asList("image/jpeg", "image/png", "application/pdf"));

    private final FileValidator fileValidator = new FileValidator(maxFileSize, allowedMimeTypes);

    @Test
    void should_ThrowCoreException_WhenFileIsNull() {
        // when, then
        CoreException exception = assertThrows(CoreException.class, () -> fileValidator.validate(null));
        assertThat(exception.getCoreErrorCode()).isEqualTo(FileErrorCode.FILE_IS_EMPTY);
    }

    @Test
    void should_ThrowCoreException_WhenFileIsEmpty() {
        // given
        MockMultipartFile emptyFile = new MockMultipartFile("file", "empty.jpg", "image/jpeg", new byte[0]);

        // when, then
        CoreException exception = assertThrows(CoreException.class, () -> fileValidator.validate(emptyFile));
        assertThat(exception.getCoreErrorCode()).isEqualTo(FileErrorCode.FILE_IS_EMPTY);
    }

    @Test
    void should_ThrowCoreException_WhenFileSizeExceedsMax() {
        // given
        byte[] content = new byte[maxFileSize.intValue() + 1];
        MockMultipartFile largeFile = new MockMultipartFile("file", "test.pdf", "application/pdf", content);

        // when, then
        CoreException exception = assertThrows(CoreException.class, () -> fileValidator.validate(largeFile));
        assertThat(exception.getCoreErrorCode()).isEqualTo(FileErrorCode.FILE_SIZE_EXCEEDED);
    }

    @Test
    void should_ThrowCoreException_WhenMimeTypeNotAllowed() {
        // given
        byte[] content = "test".getBytes();
        MockMultipartFile invalidMimeFile = new MockMultipartFile("file", "test.txt", "text/plain", content);

        // when, then
        CoreException exception = assertThrows(CoreException.class, () -> fileValidator.validate(invalidMimeFile));
        assertThat(exception.getCoreErrorCode()).isEqualTo(FileErrorCode.FILE_TYPE_NOT_ALLOWED);
    }

    @Test
    void should_NotThrowCoreException_WhenValidFile() {
        // given
        byte[] content = "test".getBytes();
        MockMultipartFile validFile = new MockMultipartFile("file", "image.jpeg", "image/jpeg", content);

        // when, then
        assertDoesNotThrow(() -> fileValidator.validate(validFile));
    }
}