package project.onlinebookstore.repository.book.spec;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.onlinebookstore.model.Book;
import project.onlinebookstore.repository.SpecificationProvider;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    private static final String ISBN_FIELD_PARAMETER = "isbn";

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get(ISBN_FIELD_PARAMETER).in(Arrays.stream(params).toList());
    }

    @Override
    public String getKey() {
        return ISBN_FIELD_PARAMETER;
    }
}
