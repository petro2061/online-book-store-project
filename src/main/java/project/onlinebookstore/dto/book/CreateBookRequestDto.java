package project.onlinebookstore.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import project.onlinebookstore.validation.Author;
import project.onlinebookstore.validation.CoverImage;
import project.onlinebookstore.validation.Isbn;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "Field title can't be null")
    private String title;
    @Author
    private String author;
    @Isbn
    private String isbn;
    @NotNull(message = "Field price can't be null")
    @Min(value = 0, message = "Price can't be less zero")
    private BigDecimal price;
    private String description;
    @CoverImage
    private String coverImage;
    @NotEmpty(message = "Can be empty but not null")
    private List<Long> categoriesIds;
}

