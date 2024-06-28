package project.onlinebookstore.repository.book.spec;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.onlinebookstore.model.Book;
import project.onlinebookstore.repository.SpecificationProvider;
import java.util.Arrays;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get("isbn").in(Arrays.stream(params).toList());
    }

    @Override
    public String getKey() {
        return "isbn";
    }
}
